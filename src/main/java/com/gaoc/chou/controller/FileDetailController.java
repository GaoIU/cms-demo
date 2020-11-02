package com.gaoc.chou.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.gaoc.chou.model.FileDetail;
import com.gaoc.chou.model.SysUser;
import com.gaoc.chou.service.IFileDetailService;
import com.gaoc.chou.vo.FileVO;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.model.MinioTool;
import com.gaoc.core.model.R;
import com.gaoc.core.properties.BaseProperties;
import com.gaoc.core.properties.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import io.minio.SetBucketPolicyArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.InputStream;

/**
 * <p>
 * 文件上传明细表 前端控制器
 * </p>
 *
 * @author Gaoc
 * @since 2020-09-17
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
public class FileDetailController {

    private final IFileDetailService fileDetailService;

    private final MinioTool minioTool;

    private final BaseProperties baseProperties;

    @PostMapping("/upload")
    public R upload(MultipartFile file, HttpSession session) {
        if (file == null || file.isEmpty()) {
            return R.fail("上传文件不能为空");
        }

        MinioProperties minio = baseProperties.getMinio();
        String bucketName = minio.getBucketName();
        BucketExistsArgs args = BucketExistsArgs.builder().bucket(bucketName).build();
        try {
            // 判断存储桶是否存在，不存在则创建
            boolean exists = minioTool.bucketExists(args);
            if (!exists) {
                minioTool.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                // 设置公有访问
                SetBucketPolicyArgs policyArgs = SetBucketPolicyArgs.builder().bucket(bucketName)
                        .config(minioTool.signConfig(bucketName, null)).build();
                minioTool.setBucketPolicy(policyArgs);
            }

            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();
            long size = file.getSize();
            String originalFilename = file.getOriginalFilename();
            String extName = FileUtil.extName(originalFilename);
            String objectName = IdUtil.objectId() + "." + extName;

            PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket(minio.getBucketName()).object(objectName)
                    .contentType(contentType).stream(inputStream, size, -1L).build();
            minioTool.putObject(putObjectArgs);
            String url = minioTool.getObjectUrl(minio.getBucketName(), objectName);

            SysUser sysUser = (SysUser) session.getAttribute(BaseConstant.DEFAULT_SESSION_KEY);
            FileDetail fileDetail = new FileDetail(originalFilename, bucketName, objectName, url, size, extName, BaseConstant.INT_FALSE, sysUser.getId());
            boolean save = fileDetailService.save(fileDetail);
            if (save) {
                FileVO fileVO = new FileVO(fileDetail.getId(), originalFilename, url);
                return R.ok(fileVO);
            }

            return R.fail("上传文件失败");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("上传文件失败");
        }
    }

}
