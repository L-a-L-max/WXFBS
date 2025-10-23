package com.playwright.utils.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

@Configuration
public class RestUtils {

    private static final RestTemplate restTemplate;
    
    static {
        // 配置RestTemplate的超时时间
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(30000); // 连接超时：30秒
        factory.setReadTimeout(60000);    // 读取超时：60秒
        restTemplate = new RestTemplate(factory);
    }

    public static JSONObject get(String url, Map<String,String> urlParams){
        return get(urlToUri(url,urlParams));
    }

    //在处理企业微信某些参数时有问题
    public static JSONObject get(String url){
        return get(URI.create(url));
    }

    private static JSONObject get(URI uri){
        ResponseEntity<JSONObject> responseEntity =restTemplate.getForEntity(uri,JSONObject.class);
        serverIsRight(responseEntity);   //判断服务器返回状态码
        return responseEntity.getBody();
    }

    public static JSONObject post(String url,Map<String,String> urlParams,JSONObject json){
        //组装url
        return post(urlToUri(url,urlParams),json);
    }

    public static JSONObject post(String url, Object data){
        try {
            return post(URI.create(url), JSONObject.parseObject(JSON.toJSONString(data)));
        } catch (RestClientException e) {
            // 捕获RestTemplate异常，包装为更友好的异常信息
            String errorMsg = "RestUtils.post调用失败 | URL: " + url + " | 错误: " + e.getMessage();
            throw new RuntimeException(errorMsg, e);
        }
    }

    public static JSONObject aiPost(String url,JSONObject json,String bearerToken){
        //组装urL
        return aiPost(URI.create(url),json,bearerToken);
    }

    private static JSONObject aiPost(URI uri,JSONObject json,String bearerToken){
        //组装url
        //设置提交json格式数据
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken); // 添加Bearer Token
        HttpEntity<JSONObject> request = new HttpEntity(json, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(uri,request,JSONObject.class);
        serverIsRight(responseEntity);  //判断服务器返回状态码
        return responseEntity.getBody();
    }
    private static JSONObject post(URI uri,JSONObject json){
        //组装url
        //设置提交json格式数据
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JSONObject> request = new HttpEntity(json, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(uri,request,JSONObject.class);
        serverIsRight(responseEntity);  //判断服务器返回状态码
        return responseEntity.getBody();
    }

    private static URI urlToUri(String url,Map<String,String> urlParams){
        //设置提交json格式数据
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        for(Map.Entry<String,String> entry : urlParams.entrySet())  {
            uriBuilder.queryParam((String)entry.getKey(),  (String) entry.getValue()) ;
        }
        return  uriBuilder.build(true).toUri();
    }

    public static JSONObject upload(String url,MultiValueMap formParams){
        //设置表单提交
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formParams, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url,request,JSONObject.class);
        serverIsRight(responseEntity);  //判断服务器返回状态码
        return responseEntity.getBody();
    }

    public static String download(String url,String targetPath,HttpEntity<MultiValueMap<String, String>> httpEntity ) throws IOException {

        ResponseEntity<byte[]> rsp = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
        if(rsp.getStatusCode() != HttpStatus.OK){
        }
        // 将下载下来的文件内容保存到本地
        Files.write(Paths.get(targetPath), Objects.requireNonNull(rsp.getBody()));
        return targetPath;
    }

    public static String download(String url,String targetPath) throws IOException {

        ResponseEntity<byte[]> rsp = restTemplate.getForEntity(url, byte[].class);
        if(rsp.getStatusCode() != HttpStatus.OK){
        }
        // 将下载下来的文件内容保存到本地
        Files.write(Paths.get(targetPath), Objects.requireNonNull(rsp.getBody()));
        return targetPath;

    }

    public static byte[] dowload(String url){
        ResponseEntity<byte[]> rsp = restTemplate.getForEntity(url, byte[].class);
        return rsp.getBody();
    }

    private static void serverIsRight(ResponseEntity responseEntity){
        if(responseEntity.getStatusCodeValue()==200){
        }else {
        }
    }


}
