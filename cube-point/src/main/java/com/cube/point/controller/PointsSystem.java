package com.cube.point.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cube.common.annotation.Log;
import com.cube.common.constant.CacheConstants;
import com.cube.common.core.controller.BaseController;
import com.cube.common.core.domain.model.LoginUser;
import com.cube.common.core.redis.RedisCache;
import com.cube.common.utils.SecurityUtils;
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
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author AspireLife
 * @version JDK 1.8
 * @date 2024年09月12日 14:10
 */
@RestController
@RequestMapping("/points")
public class PointsSystem extends BaseController {

    private static final String LIMIT_KEY_PREFIX = "points:limit";
    private static final String MAX_AMOUNT_KEY_PREFIX = "points:max";

    @Autowired
    private PointsMapper pointsMapper;

    @Value("${geth.httpUrl}")
    private String httpUrl;

    private String mainAddress = "0x2edc4228a84d672affe8a594033cb84a029bcafc";

    @Value("${geth.mainPrivateKey}")
    private String mainPrivateKey;

    @Autowired
    private RedisCli redisCli;

    @Autowired(required = false)
    private RedisCache redisCache;
    
    // 积分更新标记：记录最近更新的用户ID和时间戳，用于profile()方法判断是否需要刷新
    private static final String POINTS_UPDATE_FLAG_PREFIX = "points:update:";

    @PostMapping("/updateUserPoints")
    @Log(title = "企微管理-用户设置积分")
    public ResultBody updateUserPoints(@RequestBody Points points){


           points.setCreateId(getUserId());
           points.setCreateName(getNickName());
           pointsMapper.updateUserPoints(points);
           pointsMapper.saveUserPointsRecord(points);
          // points.setMainAddress("0x2edc4228a84d672affe8a594033cb84a029bcafc");
          // points.setMainPrivateKey("f34f737203aa370f53ef0e041c1bff36bf59db8eb662cdb447f01d9634374dd");
          // ethTranPC(points);
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
    @Transactional(rollbackFor = Exception.class)
    public ResultBody setUserPoint(String userId,String changeType,Integer changeAmount,String mainAddress,String mainPrivateKey){
        Integer actualChange = changeAmount;
        try {
            if(StringUtils.isNotEmpty(userId)){
                // 获取积分规则配置
                Map<String,Object> ruleConfig = pointsMapper.getPointRuleConfig(changeType);
                JSONObject limitConfig = parseLimitConfig(ruleConfig != null ? ruleConfig.get("limitConfig") : null);
                
                // 查询当前积分余额，防止出现负数
                Integer currentPoints = pointsMapper.getUserPoints(userId);
                if (currentPoints == null) {
                    currentPoints = 0;
                }
                
                // 计算本次实际变动值：优先使用传入 changeAmount，否则取规则默认值
                if (actualChange == null) {
                    actualChange = parseInteger(ruleConfig != null ? ruleConfig.get("pointsValue") : null);
                }
                if (actualChange == null) {
                    System.err.println("积分变动值为空，终止发放。userId=" + userId + ", changeType=" + changeType);
                    return ResultBody.error(400, "积分规则未配置，无法发放");
                }
                
                // 限频校验
                if (!checkLimit(userId, changeType, limitConfig)) {
                    return ResultBody.error(429, "积分规则达到领取上限，请稍后再试");
                }
                
                // 累积金额校验
                if (!checkMaxAmount(userId, changeType, actualChange, limitConfig)) {
                    return ResultBody.error(429, "已达到积分累计上限，无法继续发放");
                }
                
                // 扣减场景时先行校验，余额不足直接阻断
                if (actualChange < 0 && (currentPoints + actualChange) < 0) {
                    System.err.println("积分扣减失败：余额不足，userId=" + userId 
                            + ", current=" + currentPoints + ", delta=" + actualChange);
                    return ResultBody.error(400, "积分余额不足，扣减失败");
                }
                
                Thread.sleep( 1000);
                //先修改用户积分余额
                pointsMapper.setUserPoints(userId,changeType,actualChange);
//                插入记录
                pointsMapper.setUserPointRecord(userId,changeType,actualChange);
                
                // 更新缓存中的用户积分
                Integer newPoints = currentPoints + actualChange;
                boolean cacheUpdated = updateUserPointsCache(userId, newPoints);
                // 设置积分更新标记（即使缓存更新成功也设置，确保前端能及时获取最新积分）
                // 标记有效期30秒，让profile()在短时间内从数据库刷新一次
                setPointsUpdateFlag(userId, 30);
                
//                调用以太坊产生交易，并通过poa机制一秒出一个块打包交易
 //               ethTranApp(userId,changeType,actualChange,mainAddress,mainPrivateKey);
                return ResultBody.success("积分发放成功");
            }
            return ResultBody.error(400,"用户ID为空");
        } catch (Exception e) {
            try {
            Thread.sleep( 1000);
            if (actualChange != null) {
                ethTranApp(userId,changeType,actualChange,mainAddress,mainPrivateKey);
            } else {
                ethTranApp(userId,changeType,changeAmount,mainAddress,mainPrivateKey);
            }
            e.printStackTrace();
            } catch (Exception e1) {
                if (actualChange != null) {
                    ethTranApp(userId,changeType,actualChange,mainAddress,mainPrivateKey);
                } else {
                    ethTranApp(userId,changeType,changeAmount,mainAddress,mainPrivateKey);
                }
                e1.printStackTrace();
            }
            return ResultBody.error(500,"积分发放异常：" + e.getMessage());
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

    /**
     * 获取用户积分概览
     */
    @GetMapping("/getUserPointsSummary")
    public ResultBody getUserPointsSummary(@RequestParam String userId) {
        if (StringUtils.isEmpty(userId)) {
            return ResultBody.error(400, "用户ID为空");
        }
        try {
            Map<String, Object> summary = buildUserPointsSummary(userId);
            return ResultBody.success(summary);
        } catch (Exception e) {
            return ResultBody.error(500, "获取积分概览失败：" + e.getMessage());
        }
    }

    /**
     * 构建用户积分概览数据
     */
    public Map<String, Object> buildUserPointsSummary(String userId) {
        Map<String, Object> summary = new HashMap<>();
        if (StringUtils.isEmpty(userId)) {
            summary.put("balance", 0);
            summary.put("todayGain", 0);
            summary.put("weekGain", 0);
            summary.put("lastChange", null);
            return summary;
        }

        Integer balance = pointsMapper.getUserPoints(userId);
        summary.put("balance", balance == null ? 0 : balance);

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        summary.put("todayGain", sumPositivePointsSince(userId, todayStart));

        LocalDateTime weekStart = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();
        summary.put("weekGain", sumPositivePointsSince(userId, weekStart));

        Map<String, Object> lastChange = pointsMapper.getLastPointChange(userId);
        summary.put("lastChange", lastChange);

        return summary;
    }

    /**
     * 获取积分任务清单（包含详细信息）
     * 返回所有可获取积分的途径，包括任务名称、积分值、限频信息等
     * 如果用户已登录，会自动判断任务完成状态
     */
    @GetMapping("/getPointTaskList")
    public ResultBody getPointTaskList(){
        try {
            // 尝试获取当前登录用户ID
            String userId = null;
            try {
                Long currentUserId = SecurityUtils.getUserId();
                if (currentUserId != null) {
                    userId = currentUserId.toString();
                }
            } catch (Exception e) {
                // 用户未登录，继续执行但不判断完成状态
            }
            
            List<Map<String, Object>> taskList = pointsMapper.getPointTaskList();
            // 处理每个任务，解析限频配置
            for (Map<String, Object> task : taskList) {
                Object remarkObj = task.get("remark");
                JSONObject limitConfig = null;
                if (remarkObj != null) {
                    try {
                        limitConfig = parseLimitConfig(remarkObj);
                        if (limitConfig != null) {
                            task.put("limitConfig", limitConfig);
                            // 解析限频类型和值，转换为用户友好的描述
                            String limitType = limitConfig.getString("limitType");
                            Integer limitValue = limitConfig.getInteger("limitValue");
                            if (limitType != null && limitValue != null && limitValue > 0) {
                                String limitDesc = getLimitDescription(limitType, limitValue);
                                task.put("limitDesc", limitDesc);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("解析任务限频配置失败: " + remarkObj);
                    }
                }
                // 设置任务图标和分类
                String taskName = (String) task.get("taskName");
                task.put("icon", getTaskIcon(taskName));
                task.put("category", getTaskCategory(taskName));
                
                // 如果用户已登录，判断任务完成状态
                if (StringUtils.isNotEmpty(userId)) {
                    boolean isCompleted = checkTaskCompleted(userId, taskName, limitConfig);
                    task.put("isCompleted", isCompleted);
                    // 计算今日完成次数和剩余次数
                    Map<String, Object> progress = getTaskProgress(userId, taskName, limitConfig);
                    task.put("progress", progress);
                } else {
                    task.put("isCompleted", false);
                    task.put("progress", null);
                }
            }
            return ResultBody.success(taskList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBody.error(500, "获取积分任务清单失败：" + e.getMessage());
        }
    }

    /**
     * 判断任务是否完成（根据限频规则）
     * 对于一次性任务（如"首次登录"），查询数据库记录
     * 对于限频任务，查询Redis计数器
     */
    private boolean checkTaskCompleted(String userId, String taskName, JSONObject limitConfig) {
        if (limitConfig == null) {
            // 没有限频配置，检查数据库中是否有完成记录
            int completedCount = pointsMapper.checkUserTaskCompleted(userId, taskName);
            return completedCount > 0;
        }
        String limitType = limitConfig.getString("limitType");
        Integer limitValue = limitConfig.getInteger("limitValue");
        if (StringUtils.isEmpty(limitType) || limitValue == null || limitValue <= 0) {
            // 没有有效的限频配置，检查数据库中是否有完成记录
            int completedCount = pointsMapper.checkUserTaskCompleted(userId, taskName);
            return completedCount > 0;
        }
        
        // 对于一次性任务（TOTAL类型且limitValue=1），查询数据库记录
        if ("TOTAL".equalsIgnoreCase(limitType) && limitValue == 1) {
            int completedCount = pointsMapper.checkUserTaskCompleted(userId, taskName);
            return completedCount >= limitValue;
        }
        
        // 对于限频任务，查询Redis计数器
        String limitKey = buildLimitKey(userId, taskName, limitType.toUpperCase());
        Object currentObj = redisCli.get(limitKey);
        long current = currentObj == null ? 0L : Long.parseLong(currentObj.toString());
        
        // 如果Redis中没有记录，再查询数据库作为补充
        if (current == 0) {
            int dbCount = pointsMapper.checkUserTaskCompleted(userId, taskName);
            if (dbCount > 0) {
                return dbCount >= limitValue;
            }
        }
        
        // 如果已达到限频上限，认为已完成
        return current >= limitValue;
    }

    /**
     * 获取任务进度信息
     */
    private Map<String, Object> getTaskProgress(String userId, String taskName, JSONObject limitConfig) {
        Map<String, Object> progress = new HashMap<>();
        if (limitConfig == null) {
            // 没有限频配置，查询数据库记录
            int dbCount = pointsMapper.checkUserTaskCompleted(userId, taskName);
            progress.put("current", dbCount);
            progress.put("max", -1); // -1表示无限制
            progress.put("remaining", -1);
            return progress;
        }
        
        String limitType = limitConfig.getString("limitType");
        Integer limitValue = limitConfig.getInteger("limitValue");
        if (StringUtils.isEmpty(limitType) || limitValue == null || limitValue <= 0) {
            int dbCount = pointsMapper.checkUserTaskCompleted(userId, taskName);
            progress.put("current", dbCount);
            progress.put("max", -1);
            progress.put("remaining", -1);
            return progress;
        }
        
        // 对于一次性任务（TOTAL类型且limitValue=1），查询数据库记录
        if ("TOTAL".equalsIgnoreCase(limitType) && limitValue == 1) {
            int dbCount = pointsMapper.checkUserTaskCompleted(userId, taskName);
            progress.put("current", dbCount);
            progress.put("max", limitValue);
            progress.put("remaining", Math.max(0, limitValue - dbCount));
            return progress;
        }
        
        // 对于限频任务，查询Redis计数器
        String limitKey = buildLimitKey(userId, taskName, limitType.toUpperCase());
        Object currentObj = redisCli.get(limitKey);
        long current = currentObj == null ? 0L : Long.parseLong(currentObj.toString());
        
        // 如果Redis中没有记录，再查询数据库作为补充
        if (current == 0) {
            int dbCount = pointsMapper.checkUserTaskCompleted(userId, taskName);
            if (dbCount > 0) {
                current = dbCount;
            }
        }
        
        progress.put("current", current);
        progress.put("max", limitValue);
        progress.put("remaining", Math.max(0, limitValue - current));
        
        return progress;
    }

    /**
     * 获取限频描述
     */
    private String getLimitDescription(String limitType, Integer limitValue) {
        switch (limitType.toUpperCase()) {
            case "DAILY":
                return "每日最多" + limitValue + "次";
            case "WEEKLY":
                return "每周最多" + limitValue + "次";
            case "MONTHLY":
                return "每月最多" + limitValue + "次";
            case "TOTAL":
                return "总计最多" + limitValue + "次";
            default:
                return "限频：" + limitValue + "次";
        }
    }

    /**
     * 获取任务图标
     */
    private String getTaskIcon(String taskName) {
        if (taskName == null) {
            return "el-icon-star-on";
        }
        if (taskName.contains("登录")) {
            return "el-icon-user";
        } else if (taskName.contains("模板") || taskName.contains("上架")) {
            return "el-icon-document";
        } else if (taskName.contains("购买")) {
            return "el-icon-shopping-cart-full";
        } else if (taskName.contains("评分") || taskName.contains("评价")) {
            return "el-icon-star-on";
        } else if (taskName.contains("咨询")) {
            return "el-icon-chat-line-round";
        } else if (taskName.contains("记忆")) {
            return "el-icon-collection";
        } else if (taskName.contains("分成") || taskName.contains("销售")) {
            return "el-icon-money";
        } else {
            return "el-icon-trophy";
        }
    }

    /**
     * 获取任务分类
     */
    private String getTaskCategory(String taskName) {
        if (taskName == null) {
            return "其他";
        }
        if (taskName.contains("登录")) {
            return "每日任务";
        } else if (taskName.contains("模板") || taskName.contains("上架") || taskName.contains("分成")) {
            return "创作任务";
        } else if (taskName.contains("购买")) {
            return "消费任务";
        } else if (taskName.contains("评分") || taskName.contains("评价") || taskName.contains("咨询")) {
            return "互动任务";
        } else {
            return "其他任务";
        }
    }

    /**
     * 解析限频配置JSON
     */
    private JSONObject parseLimitConfig(Object configObj) {
        if (configObj == null) {
            return null;
        }
        try {
            return JSON.parseObject(configObj.toString());
        } catch (Exception e) {
            System.err.println("解析积分规则remark失败: " + configObj);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析整数
     */
    private Integer parseInteger(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 检查限频
     */
    private boolean checkLimit(String userId, String changeType, JSONObject config) {
        if (config == null) {
            return true;
        }
        String limitType = config.getString("limitType");
        Integer limitValue = config.getInteger("limitValue");
        if (StringUtils.isEmpty(limitType) || limitValue == null || limitValue <= 0) {
            return true;
        }
        limitType = limitType.toUpperCase();
        String limitKey = buildLimitKey(userId, changeType, limitType);
        Object currentObj = redisCli.get(limitKey);
        long current = currentObj == null ? 0L : Long.parseLong(currentObj.toString());
        if (current >= limitValue) {
            return false;
        }
        long newVal = redisCli.incr(limitKey, 1);
        if (newVal == 1) {
            long ttl = calculateTTLSeconds(limitType);
            if (ttl > 0) {
                redisCli.expire(limitKey, ttl);
            }
        }
        return true;
    }

    /**
     * 检查累计上限
     */
    private boolean checkMaxAmount(String userId, String changeType, Integer actualChange, JSONObject config) {
        if (config == null || actualChange == null || actualChange <= 0) {
            return true;
        }
        Integer maxAmount = config.getInteger("maxAmount");
        if (maxAmount == null || maxAmount <= 0) {
            return true;
        }
        String key = MAX_AMOUNT_KEY_PREFIX + ":" + changeType + ":" + userId;
        Object obj = redisCli.get(key);
        long current = obj == null ? 0L : Long.parseLong(obj.toString());
        if (current + actualChange > maxAmount) {
            return false;
        }
        redisCli.set(key, current + actualChange);
        return true;
    }

    /**
     * 统计自指定时间以来的正向积分
     */
    private int sumPositivePointsSince(String userId, LocalDateTime startTime) {
        if (StringUtils.isEmpty(userId) || startTime == null) {
            return 0;
        }
        Integer sum = pointsMapper.sumUserPointsChangeSince(userId, startTime);
        return sum == null ? 0 : sum;
    }

    /**
     * 构建限频key
     */
    private String buildLimitKey(String userId, String changeType, String limitType) {
        String periodKey = "total";
        LocalDate today = LocalDate.now();
        switch (limitType) {
            case "DAILY":
                periodKey = today.toString();
                break;
            case "WEEKLY":
                int week = today.get(WeekFields.ISO.weekOfWeekBasedYear());
                periodKey = today.getYear() + "-W" + week;
                break;
            case "MONTHLY":
                periodKey = today.getYear() + "-" + today.getMonthValue();
                break;
            default:
                break;
        }
        return LIMIT_KEY_PREFIX + ":" + changeType + ":" + userId + ":" + periodKey;
    }

    /**
     * 计算TTL秒数
     */
    private long calculateTTLSeconds(String limitType) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime;
        switch (limitType) {
            case "DAILY":
                expireTime = now.toLocalDate().plusDays(1).atStartOfDay();
                break;
            case "WEEKLY":
                expireTime = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
                break;
            case "MONTHLY":
                expireTime = now.with(TemporalAdjusters.firstDayOfNextMonth()).toLocalDate().atStartOfDay();
                break;
            default:
                return -1;
        }
        long seconds = Duration.between(now, expireTime).getSeconds();
        return Math.max(seconds, 1);
    }

    /**
     * 更新缓存中的用户积分
     * 通过扫描Redis中所有登录token缓存，找到该用户的缓存并更新积分
     * @return true表示更新成功，false表示更新失败
     */
    private boolean updateUserPointsCache(String userId, Integer newPoints) {
        if (userId == null || newPoints == null) {
            return false;
        }
        // 优先使用RedisCache更新
        if (redisCache != null) {
            try {
                // 通过Redis的keys命令查找所有登录token（注意：生产环境建议使用scan）
                String pattern = CacheConstants.LOGIN_TOKEN_KEY + "*";
                Collection<String> keys = redisCache.keys(pattern);
                if (keys != null && !keys.isEmpty()) {
                    for (String key : keys) {
                        try {
                            LoginUser loginUser = redisCache.getCacheObject(key);
                            if (loginUser != null && loginUser.getUser() != null 
                                    && loginUser.getUser().getUserId() != null
                                    && loginUser.getUser().getUserId().toString().equals(userId)) {
                                // 找到该用户的缓存，更新积分
                                loginUser.getUser().setPoints(newPoints);
                                // 重新设置缓存，保持原有的过期时间
                                redisCache.setCacheObject(key, loginUser);
                                System.out.println("已更新用户缓存积分，userId=" + userId + ", points=" + newPoints);
                                return true; // 更新成功
                            }
                        } catch (Exception e) {
                            // 忽略单个key的解析错误，继续处理下一个
                            continue;
                        }
                    }
                    System.out.println("未找到用户缓存，userId=" + userId);
                } else {
                    System.out.println("未找到任何登录token缓存");
                }
            } catch (Exception e) {
                System.err.println("更新用户缓存积分失败: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("RedisCache未注入，跳过缓存更新");
        }
        return false; // 更新失败
    }
    
    /**
     * 设置积分更新标记，用于profile()方法判断是否需要刷新
     * @param userId 用户ID
     * @param expireSeconds 过期时间（秒），默认30秒
     */
    private void setPointsUpdateFlag(String userId, int expireSeconds) {
        if (userId == null) {
            return;
        }
        try {
            String flagKey = POINTS_UPDATE_FLAG_PREFIX + userId;
            // 优先使用RedisCache（与TokenService使用相同的Redis实例）
            if (redisCache != null) {
                redisCache.setCacheObject(flagKey, System.currentTimeMillis(), expireSeconds, java.util.concurrent.TimeUnit.SECONDS);
                System.out.println("已设置积分更新标记，userId=" + userId + ", 有效期=" + expireSeconds + "秒");
            } else if (redisCli != null) {
                // 备用方案：使用RedisCli
                redisCli.set(flagKey, String.valueOf(System.currentTimeMillis()));
                redisCli.expire(flagKey, expireSeconds);
            }
        } catch (Exception e) {
            System.err.println("设置积分更新标记失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
