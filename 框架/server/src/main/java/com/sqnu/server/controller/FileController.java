package com.sqnu.server.controller;

import com.sqnu.server.common.CommonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 * <p>
 * 提供文件上传功能，支持图片和普通文件的上传。
 * 文件保存到配置的D盘uploads目录下，按日期分类存储。
 * </p>
 *
 * @author sqnu
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    /**
     * 上传路径配置
     */
    @Value("${file.upload.path:D:/uploads/}")
    private String uploadPath;

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @param dir  指定目录（可选，如 farmland、crop 等）
     * @return 上传结果（包含文件URL）
     */
    @PostMapping("/upload")
    public CommonResult<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "dir", required = false) String dir) {

        try {
            // 1. 检查文件是否为空
            if (file.isEmpty()) {
                return CommonResult.error("请选择要上传的文件");
            }

            // 2. 检查文件大小（最大10MB）
            if (file.getSize() > 10 * 1024 * 1024) {
                return CommonResult.error("文件大小不能超过10MB");
            }

            // 3. 获取文件扩展名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                return CommonResult.error("文件名格式不正确");
            }
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 4. 生成新的文件名（日期 + UUID）
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String newFileName = datePath + "/" + UUID.randomUUID().toString().replace("-", "") + extension;

            // 5. 构建存储目录
            String saveDir = uploadPath + (dir != null ? dir + "/" : "") + datePath;
            File saveDirFile = new File(saveDir);
            if (!saveDirFile.exists()) {
                saveDirFile.mkdirs();
            }

            // 6. 保存文件
            File targetFile = new File(saveDir, newFileName.substring(newFileName.lastIndexOf("/") + 1));
            file.transferTo(targetFile);

            // 7. 返回文件URL
            String fileUrl = "/uploads/" + (dir != null ? dir + "/" : "") + newFileName;

            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("filename", originalFilename);

            return CommonResult.success("上传成功", result);
        } catch (IOException e) {
            e.printStackTrace();
            return CommonResult.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param url 文件URL
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    public CommonResult<Void> deleteFile(@RequestParam("url") String url) {
        try {
            // 构建文件路径
            String filePath = uploadPath + url.replace("/uploads/", "");

            File file = new File(filePath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    return CommonResult.success("删除成功");
                } else {
                    return CommonResult.error("删除失败");
                }
            } else {
                return CommonResult.error("文件不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}