//package com.ruoyi.common.controller;
//
//import cn.hutool.core.io.IoUtil;
//import com.itgaohe.config.MinioConfig;
//import com.itgaohe.utils.MinioUtils;
//import com.itgaohe.utils.R;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@RestController
//@RequestMapping("/common")
//public class CommonController {
//
//    @Autowired
//    MinioUtils minioUtils;
//    @Autowired
//    MinioConfig minioConfig;
//
//    @PostMapping("/upload")
//    public R updateimage(@RequestParam("file") MultipartFile file){
//        //获取上传时提供的文件信息
//        //1.指定需要上传的桶
//        String bucketName = minioConfig.getBucketName();
//        //2.file 对象
//        String originalFilename = file.getOriginalFilename();
//        System.out.println("originalFilename = " + originalFilename);
//        //3.上传后保存在minio的图片名
//        String contentType = StringUtils.substringAfterLast(originalFilename, ".");
//        String newFileName = System.currentTimeMillis() + "." + contentType;
//        System.out.println("newFileName = " + newFileName);
//        //上传的文件类型
//        //执行上传
//        minioUtils.uploadFile(bucketName, file, newFileName,contentType);
//        return R.success(newFileName);
//    }
//    //图片的预览下载
//    @GetMapping("/download")
//    public R downLoad(@RequestParam("name") String name, HttpServletResponse response){
//        try {
//            InputStream inputStream = minioUtils.getObject(minioConfig.getBucketName(), name);
//            IoUtil.copy(inputStream,response.getOutputStream());
//            return R.success("回显成功");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
