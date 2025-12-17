package com.demo.wxchat.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

/**
 * 企业微信消息加解密工具类
 * 用于与腾讯元器API通信时的消息加解密
 */
public class WXBizMsgCrypt {

    private byte[] aesKey;
    private String token;
    private String corpId;

    public WXBizMsgCrypt(String token, String encodingAesKey, String corpId) throws Exception {
        if (encodingAesKey.length() != 43) {
            throw new Exception("EncodingAESKey长度错误，必须是43位字符");
        }
        this.token = token;
        this.corpId = corpId;
        this.aesKey = Base64.getDecoder().decode(encodingAesKey + "=");
    }

    public String verifyURL(String msgSignature, String timeStamp, String nonce, String echoStr) throws Exception {
        String signature = getSHA1(token, timeStamp, nonce, echoStr);
        if (!signature.equals(msgSignature)) {
            throw new Exception("签名验证失败");
        }
        return decrypt(echoStr);
    }

    public String decryptMsg(String msgSignature, String timeStamp, String nonce, String postData) throws Exception {
        String encrypt = extractEncrypt(postData);
        String signature = getSHA1(token, timeStamp, nonce, encrypt);
        if (!signature.equals(msgSignature)) {
            throw new Exception("签名验证失败");
        }
        return decrypt(encrypt);
    }

    public String encryptMsg(String replyMsg, String timeStamp, String nonce) throws Exception {
        String encrypt = encrypt(getRandomStr(), replyMsg);
        String signature = getSHA1(token, timeStamp, nonce, encrypt);
        return generateXml(encrypt, signature, timeStamp, nonce);
    }

    private String decrypt(String text) throws Exception {
        byte[] original;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            byte[] encrypted = Base64.getDecoder().decode(text);
            original = cipher.doFinal(encrypted);
        } catch (Exception e) {
            throw new Exception("AES解密失败: " + e.getMessage());
        }

        String xmlContent;
        try {
            byte[] bytes = PKCS7Decode(original);
            int msgLen = recoverNetworkBytesOrder(Arrays.copyOfRange(bytes, 16, 20));
            xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + msgLen), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new Exception("解密后解析失败: " + e.getMessage());
        }
        return xmlContent;
    }

    private String encrypt(String randomStr, String text) throws Exception {
        byte[] randomBytes = randomStr.getBytes(StandardCharsets.UTF_8);
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] corpIdBytes = corpId.getBytes(StandardCharsets.UTF_8);
        byte[] networkOrder = getNetworkBytesOrder(textBytes.length);

        int len = randomBytes.length + networkOrder.length + textBytes.length + corpIdBytes.length;
        byte[] padBytes = PKCS7Encode(len);

        byte[] unencrypted = new byte[len + padBytes.length];
        int pos = 0;
        System.arraycopy(randomBytes, 0, unencrypted, pos, randomBytes.length);
        pos += randomBytes.length;
        System.arraycopy(networkOrder, 0, unencrypted, pos, networkOrder.length);
        pos += networkOrder.length;
        System.arraycopy(textBytes, 0, unencrypted, pos, textBytes.length);
        pos += textBytes.length;
        System.arraycopy(corpIdBytes, 0, unencrypted, pos, corpIdBytes.length);
        pos += corpIdBytes.length;
        System.arraycopy(padBytes, 0, unencrypted, pos, padBytes.length);

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(unencrypted);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new Exception("AES加密失败: " + e.getMessage());
        }
    }

    private String getSHA1(String token, String timestamp, String nonce, String encrypt) throws Exception {
        String[] array = new String[]{token, timestamp, nonce, encrypt};
        Arrays.sort(array);
        StringBuilder sb = new StringBuilder();
        for (String s : array) {
            sb.append(s);
        }
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(sb.toString().getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        StringBuilder hexStr = new StringBuilder();
        for (byte b : digest) {
            String shaHex = Integer.toHexString(b & 0xFF);
            if (shaHex.length() < 2) {
                hexStr.append(0);
            }
            hexStr.append(shaHex);
        }
        return hexStr.toString();
    }

    private String extractEncrypt(String xml) {
        int start = xml.indexOf("<Encrypt><![CDATA[");
        int end = xml.indexOf("]]></Encrypt>");
        if (start >= 0 && end > start) {
            return xml.substring(start + 18, end);
        }
        start = xml.indexOf("<Encrypt>");
        end = xml.indexOf("</Encrypt>");
        if (start >= 0 && end > start) {
            return xml.substring(start + 9, end);
        }
        return "";
    }

    private String generateXml(String encrypt, String signature, String timestamp, String nonce) {
        return "<xml>\n" +
                "<Encrypt><![CDATA[" + encrypt + "]]></Encrypt>\n" +
                "<MsgSignature><![CDATA[" + signature + "]]></MsgSignature>\n" +
                "<TimeStamp>" + timestamp + "</TimeStamp>\n" +
                "<Nonce><![CDATA[" + nonce + "]]></Nonce>\n" +
                "</xml>";
    }

    private String getRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(base.charAt(random.nextInt(base.length())));
        }
        return sb.toString();
    }

    private byte[] getNetworkBytesOrder(int length) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (length & 0xFF);
        orderBytes[2] = (byte) (length >> 8 & 0xFF);
        orderBytes[1] = (byte) (length >> 16 & 0xFF);
        orderBytes[0] = (byte) (length >> 24 & 0xFF);
        return orderBytes;
    }

    private int recoverNetworkBytesOrder(byte[] orderBytes) {
        int length = 0;
        for (int i = 0; i < 4; i++) {
            length <<= 8;
            length |= orderBytes[i] & 0xFF;
        }
        return length;
    }

    private byte[] PKCS7Decode(byte[] decrypted) {
        int pad = decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    private byte[] PKCS7Encode(int count) {
        int blockSize = 32;
        int amountToPad = blockSize - (count % blockSize);
        if (amountToPad == 0) {
            amountToPad = blockSize;
        }
        byte[] tmp = new byte[amountToPad];
        Arrays.fill(tmp, (byte) amountToPad);
        return tmp;
    }
}
