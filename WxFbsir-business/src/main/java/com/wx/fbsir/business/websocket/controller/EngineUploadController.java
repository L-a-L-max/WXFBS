package com.wx.fbsir.business.websocket.controller;

import com.wx.fbsir.business.websocket.server.EngineSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Engine 截图上传控制器
 * 
 * 提供 Engine 节点截图上传接口
 * 验证主机ID是否在线后存储截图并返回访问链接
 *
 * @author wxfbsir
 * @date 2025-12-19
 */
@RestController
@RequestMapping("/engine")
public class EngineUploadController {

    private static final Logger log = LoggerFactory.getLogger(EngineUploadController.class);

    private final EngineSessionManager sessionManager;

    /**
     * 文件上传根路径
     */
    @Value("${wxfbsir.profile:/data/wxfbsir/uploadPath}")
    private String uploadPath;

    /**
     * 资源访问前缀
     */
    @Value("${wxfbsir.domain:http://localhost:8080}")
    private String resourcePrefix;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public EngineUploadController(EngineSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Engine 截图上传接口
     * 
     * @param hostId 主机ID（用于验证是否在线）
     * @param userId 用户ID（用于目录隔离）
     * @param fileName 文件名（可选，不含扩展名）
     * @param file 截图文件
     * @return 包含图片访问链接的响应
     */
    @PostMapping("/screenshot/upload")
    public ResponseEntity<Map<String, Object>> uploadScreenshot(
            @RequestParam("hostId") String hostId,
            @RequestParam("userId") String userId,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam("file") MultipartFile file) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 1. 验证主机ID是否在线
        if (!sessionManager.isEngineOnline(hostId)) {
            log.warn("[截图上传] 主机ID不在线 - hostId: {}", hostId);
            result.put("success", false);
            result.put("message", "主机ID不在线，请确保Engine已连接");
            result.put("code", "HOST_NOT_ONLINE");
            return ResponseEntity.ok(result);
        }
        
        // 2. 验证文件
        if (file == null || file.isEmpty()) {
            result.put("success", false);
            result.put("message", "文件不能为空");
            result.put("code", "FILE_EMPTY");
            return ResponseEntity.ok(result);
        }
        
        // 3. 构建存储路径: /engine/{userId}/{date}/
        String dateStr = LocalDateTime.now().format(DATE_FORMATTER);
        
        // 解析为绝对路径
        Path uploadRoot = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path targetDir = uploadRoot.resolve("engine").resolve(userId).resolve(dateStr);
        
        log.debug("[截图上传] 目标路径 - 配置路径: {}, 绝对路径: {}, 目标目录: {}", 
            uploadPath, uploadRoot, targetDir);
        
        try {
            // 创建目录
            if (!Files.exists(targetDir)) {
                log.debug("[截图上传] 创建目录: {}", targetDir);
                Files.createDirectories(targetDir);
                log.debug("[截图上传] 目录创建成功");
            }
            
            // 4. 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String finalFileName;
            
            if (fileName != null && !fileName.isEmpty()) {
                // 使用指定的文件名
                finalFileName = fileName + "_" + System.currentTimeMillis() + extension;
            } else {
                // 生成随机文件名
                finalFileName = UUID.randomUUID().toString().replace("-", "").substring(0, 16) + extension;
            }
            
            // 5. 保存文件
            Path targetPath = targetDir.resolve(finalFileName);
            file.transferTo(targetPath.toFile());
            
            // 6. 生成访问链接
            String relativePath = "/profile/engine/" + userId + "/" + dateStr + "/" + finalFileName;
            String accessUrl = resourcePrefix + relativePath;
            
            result.put("success", true);
            result.put("url", accessUrl);
            result.put("fileName", finalFileName);
            result.put("relativePath", relativePath);
            
        } catch (IOException e) {
            // 详细的错误诊断
            String errorType = "UNKNOWN";
            String errorDetail = e.getMessage();
            String suggestion = "";
            
            if (e instanceof java.nio.file.NoSuchFileException) {
                errorType = "PATH_NOT_FOUND";
                suggestion = "目录不存在或路径错误";
            } else if (e instanceof java.nio.file.AccessDeniedException) {
                errorType = "PERMISSION_DENIED";
                suggestion = "权限不足，请检查目录权限";
            } else if (e instanceof java.nio.file.FileAlreadyExistsException) {
                errorType = "FILE_EXISTS";
                suggestion = "文件已存在";
            } else if (e.getMessage() != null && e.getMessage().contains("No space left")) {
                errorType = "DISK_FULL";
                suggestion = "磁盘空间不足";
            }
            
            log.error("[截图上传] 保存文件失败 - hostId: {}, userId: {}", hostId, userId);
            log.error("[截图上传] 错误类型: {} ({})", errorType, e.getClass().getSimpleName());
            log.error("[截图上传] 错误详情: {}", errorDetail);
            log.error("[截图上传] 配置路径: {}", uploadPath);
            log.error("[截图上传] 目标路径: {}", targetDir);
            log.error("[截图上传] 路径是否存在: {}", Files.exists(targetDir.getParent()));
            log.error("[截图上传] 路径是否可写: {}", Files.isWritable(targetDir.getParent()));
            if (!suggestion.isEmpty()) {
                log.error("[截图上传] 建议: {}", suggestion);
            }
            
            result.put("success", false);
            result.put("message", "保存文件失败: " + errorType + " - " + suggestion);
            result.put("code", "SAVE_FAILED");
            result.put("errorType", errorType);
            result.put("errorDetail", errorDetail);
        }
        
        return ResponseEntity.ok(result);
    }

    /**
     * 批量上传截图
     */
    @PostMapping("/screenshot/batch-upload")
    public ResponseEntity<Map<String, Object>> batchUploadScreenshots(
            @RequestParam("hostId") String hostId,
            @RequestParam("userId") String userId,
            @RequestParam("files") MultipartFile[] files) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 验证主机ID是否在线
        if (!sessionManager.isEngineOnline(hostId)) {
            result.put("success", false);
            result.put("message", "主机ID不在线");
            result.put("code", "HOST_NOT_ONLINE");
            return ResponseEntity.ok(result);
        }
        
        if (files == null || files.length == 0) {
            result.put("success", false);
            result.put("message", "文件列表不能为空");
            result.put("code", "FILES_EMPTY");
            return ResponseEntity.ok(result);
        }
        
        String dateStr = LocalDateTime.now().format(DATE_FORMATTER);
        Path targetDir = Paths.get(uploadPath, "engine", userId, dateStr);
        
        try {
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "创建目录失败");
            result.put("code", "DIR_CREATE_FAILED");
            return ResponseEntity.ok(result);
        }
        
        java.util.List<Map<String, String>> uploadedFiles = new java.util.ArrayList<>();
        int success = 0;
        int failed = 0;
        
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                failed++;
                continue;
            }
            
            try {
                String extension = getFileExtension(file.getOriginalFilename());
                String finalFileName = UUID.randomUUID().toString().replace("-", "").substring(0, 16) + extension;
                Path targetPath = targetDir.resolve(finalFileName);
                file.transferTo(targetPath.toFile());
                
                String relativePath = "/profile/engine/" + userId + "/" + dateStr + "/" + finalFileName;
                String accessUrl = resourcePrefix + relativePath;
                
                Map<String, String> fileInfo = new HashMap<>();
                fileInfo.put("url", accessUrl);
                fileInfo.put("fileName", finalFileName);
                uploadedFiles.add(fileInfo);
                success++;
                
            } catch (IOException e) {
                failed++;
                log.warn("[批量上传] 保存文件失败: {}", e.getMessage());
            }
        }
        
        log.info("[批量上传] 完成 - hostId: {}, userId: {}, 成功: {}, 失败: {}", 
            hostId, userId, success, failed);
        
        result.put("success", true);
        result.put("files", uploadedFiles);
        result.put("successCount", success);
        result.put("failedCount", failed);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return ".png";
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0) {
            return filename.substring(dotIndex);
        }
        return ".png";
    }
}
