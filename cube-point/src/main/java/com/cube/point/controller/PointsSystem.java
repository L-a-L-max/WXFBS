package com.cube.point.controller;

import com.cube.common.annotation.Log;
import com.cube.common.core.controller.BaseController;
import com.cube.common.utils.StringUtils;
import com.cube.point.domain.EthConstant;
import com.cube.point.domain.Points;
import com.cube.point.mapper.PointsMapper;
import com.cube.point.util.RedisCli;
import com.cube.point.util.RestCli;
import com.cube.point.util.ResultBody;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author AspireLife
 * @version JDK 1.8
 * @date 2024年09月12日 14:10
 */
@RestController
@RequestMapping("/points")
public class PointsSystem extends BaseController {

    @Autowired
    private PointsMapper pointsMapper;

    @Value("${geth.httpUrl}")
    private String httpUrl;

    private String mainAddress = "0x2edc4228a84d672affe8a594033cb84a029bcafc";

    @Value("${geth.mainPrivateKey}")
    private String mainPrivateKey;

    @Autowired
    private RedisCli redisCli;

    @PostMapping("/updateUserPoints")
    @Log(title = "企微管理-用户设置积分")
    public ResultBody updateUserPoints(@RequestBody Points points){
        points.setCreateId(getUserId());
        points.setCreateName(getNickName());
        pointsMapper.updateUserPoints(points);
        pointsMapper.saveUserPointsRecord(points);
        
        // 🔥 暂时注释以太坊相关调用，避免资金不足异常
        // points.setMainAddress("0x2edc4228a84d672affe8a594033cb84a029bcafc");
        // points.setMainPrivateKey("f34f737203aa370f53ef0e041c1bff36bf59db8eb662cdb447f01d9634374dd");
        // ethTranPC(points);
        
        System.out.println("✅ 积分更新完成 | 用户:" + points.getUserId() + " | 变更:" + points.getChangeAmount());
        return ResultBody.success("修改成功");
    }

    @Async
    public void ethTranPC(Points points) {
        try {
            // 添加空值检查
            if (points.getChangeAmount() == null) {
                System.err.println("Change amount is null for user: " + points.getUserId());
                return;
            }

            String address = (String) redisCli.get("geth.address."+points.getUserId());
            String privateKey = (String) redisCli.get("geth.privateKey."+points.getUserId());

            if (address == null || privateKey == null) {
                System.err.println("Address or private key is null for user: " + points.getUserId());
                return;
            }

            String tranId = null;
            Map gethMap = new HashMap();
            if(points.getChangeAmount() > 0){
                //说明是增积分，从主账户转到用户账户
                tranId = ethTran(points.getMainAddress(), address, BigInteger.valueOf(points.getChangeAmount()), points.getMainPrivateKey());
                gethMap.put("from", points.getMainAddress());
                gethMap.put("to", address);
                gethMap.put("ether", points.getChangeAmount());
            } else {
                //说明是减积分, 从用户账号转到主账户
                tranId = ethTran(address, points.getMainAddress(), BigInteger.valueOf(Math.abs(points.getChangeAmount())), privateKey);
                gethMap.put("from", address);
                gethMap.put("to", points.getMainAddress());
                gethMap.put("ether", Math.abs(points.getChangeAmount()));
            }
            gethMap.put("tranId", tranId);
            gethMap.put("changeType", "积分充值");
            pointsMapper.saveUserGethRecord(gethMap);
        } catch (Exception e) {
            // 记录详细的错误日志
            System.err.println("Error in ethTranPC for user: " + points.getUserId() + ", error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PostMapping("/getUserPointsRecord")
    @Log(title = "企微管理-查询用户积分")
    public ResultBody getUserPointsRecord(@RequestBody Points points){
        PageHelper.startPage(points.getPage(),points.getLimit());
        List<Map> list = pointsMapper.getUserPointsRecord(points);
        PageInfo pageInfo = new PageInfo(list);
        return ResultBody.success(pageInfo);
    }

    public Integer getUserPoints(String userId){
        return pointsMapper.getUserPoints(userId);
    }
    /*
    * 积分埋点方法
    * */
    @GetMapping("/setUserPoint")
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void setUserPoint(String userId,String changeType,Integer changeAmount,String mainAddress,String mainPrivateKey){
        try {
            if(StringUtils.isNotEmpty(userId)){
                Thread.sleep(1000);
                // 修改用户积分余额
                pointsMapper.setUserPoints(userId,changeType,changeAmount);
                // 插入记录
                pointsMapper.setUserPointRecord(userId,changeType,changeAmount);
                
                // 🔥 暂时注释以太坊相关调用，避免资金不足异常
                // ethTranApp(userId,changeType,changeAmount,mainAddress,mainPrivateKey);
                
                System.out.println("✅ 积分埋点完成 | 用户:" + userId + " | 类型:" + changeType + " | 变更:" + changeAmount);
            }
        } catch (Exception e) {
            System.err.println("❌ 积分埋点失败 | 用户:" + userId + " | 错误:" + e.getMessage());
        }
    }
    @Async
    public void ethTranApp(String userId,String changeType,Integer changeAmount,String mainAddress,String mainPrivateKey) {
        try {
        Integer pointVal = pointsMapper.getPointRuleVal(changeType);
        String address = (String) redisCli.get("geth.address."+userId);
        String privateKey = (String) redisCli.get("geth.privateKey."+userId);
        String tranId = null;
        if(StringUtils.isNotEmpty(address)){
            Map gethMap = new HashMap();
            if(changeAmount !=null){
                pointVal = changeAmount;
            }
            // 添加空值检查，避免NullPointerException
            if(pointVal != null && pointVal > 0){
                //说明是增积分，从主账户转到用户账户
                tranId = ethTran(mainAddress,address,BigInteger.valueOf(pointVal),mainPrivateKey);
                gethMap.put("from",mainAddress);
                gethMap.put("to",address);
                gethMap.put("ether",pointVal);
            } else if(pointVal != null && pointVal < 0) {
                //说明是减积分, 从用户账号转到主账户
                tranId = ethTran(address,mainAddress,BigInteger.valueOf(Math.abs(pointVal)),privateKey);
                gethMap.put("from",address);
                gethMap.put("to",mainAddress);
                gethMap.put("ether",Math.abs(pointVal));
            } else {
                // pointVal为null或为0时，记录日志并跳过处理
                System.err.println("Invalid pointVal for user: " + userId + ", changeType: " + changeType + ", pointVal: " + pointVal);
                return;
            }
            gethMap.put("tranId",tranId);
            gethMap.put("changeType",changeType);
            pointsMapper.saveUserGethRecord(gethMap);
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/registerAccount")
    public void registerAccount(Long userId) {

        Map gethMap = RestCli.get(httpUrl+"?userId="+userId);
        if(gethMap != null){
            pointsMapper.saveUserGethAccount(gethMap);
            redisCli.set("geth.address."+userId,gethMap.get("address"));
            redisCli.set("geth.privateKey."+userId,gethMap.get("privateKey"));
        }

    }

    @GetMapping("/migrationHisUser")
    public String migrationHisUser(){
        //首先查出没有上链的用户ID
        List<Map> user = pointsMapper.getNoGethUserId();
        for (Map map : user) {
            Long userId = (Long) map.get("userId");
            Integer point = (Integer) map.get("points");
            registerAccount(userId);
            String tranId = null;
            Map gethMap = new HashMap();
            String address = (String) redisCli.get("geth.address."+userId);
            try {
                Thread.sleep( 3000);
                tranId = ethTran(mainAddress,address,BigInteger.valueOf(point),mainPrivateKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            gethMap.put("from",mainAddress);
            gethMap.put("to",address);
            gethMap.put("ether",point);
            gethMap.put("tranId",tranId);
            gethMap.put("changeType","历史迁移上链");
            pointsMapper.saveUserGethRecord(gethMap);
            System.out.println(userId+"迁移完成");
        }
        return "迁移完成";
    }

    public static String ethTran(String from,String to, BigInteger num,String privateKey) throws Exception {

        BigDecimal etherAmount = new BigDecimal(num); // 使用 BigDecimal 表示 2000 Ether
        BigInteger weiAmount = Convert.toWei(etherAmount, Convert.Unit.ETHER).toBigInteger();

        Web3j web3 = Web3j.build(new HttpService("http://101.34.87.103:8545"));

        Credentials credentials = Credentials.create(privateKey);

        EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(
                from, DefaultBlockParameterName.LATEST).send();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, Convert.toWei(EthConstant.GAS_PRICE, Convert.Unit.GWEI).toBigInteger(),
                Convert.toWei(EthConstant.GAS_LIMIT, Convert.Unit.WEI).toBigInteger(), to, weiAmount);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();

        if (ethSendTransaction.hasError()) {
            System.out.println("transfer error:"+ethSendTransaction.getError().getMessage());
            throw new Exception(ethSendTransaction.getError().getMessage());
        } else {
            String transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("交易成功:" + transactionHash);
            return transactionHash;
        }
    }

    public Integer getPointRule(String changeType){
        return pointsMapper.getPointRule(changeType);
    };

    /*
    * 校验是否出发积分规则
    * */
    public Integer checkPointIsOk(String changeType,String userId,Integer isToday){
        return pointsMapper.checkPointIsOk(changeType,userId,isToday);
    };

    /*
    * 查询当前的积分规则
    * */
    public List<Map> getPointTask(){
        return pointsMapper.getPointTask();
    }



}
