package com.gaoc.chou.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoc.chou.model.FileDetail;
import com.gaoc.chou.service.IFileDetailService;
import com.gaoc.constant.BaseConstant;
import com.gaoc.core.model.MinioTool;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileDetailTask {

    private final IFileDetailService fileDetailService;

    private final MinioTool minioTool;

    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 3 * 24 * 3600 * 1000)
    public void clearFile() {
        LocalDate date = LocalDate.now().minusDays(3L);
        QueryWrapper<FileDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseConstant.INT_FALSE);
        wrapper.apply("DATE_FORMAT(create_time, '%Y-%m-%d') <= {0}", date.toString());
        List<FileDetail> list = fileDetailService.list(wrapper);
        if (CollUtil.isEmpty(list)) {
            log.info("========================暂无需要清理的临时文件========================");
            return;
        }

        log.info("========================开始清理临时文件========================");
        // 获取所有相关的存储桶
        Set<String> buckets = list.stream().map(FileDetail::getBucket).collect(Collectors.toSet());
        log.info("本次清理相关的存储桶：{}", JSON.toJSONString(buckets));
        buckets.forEach(bucket -> {
            List<FileDetail> collect = list.stream().filter(fileDetail -> StrUtil.equals(bucket, fileDetail.getBucket())).collect(Collectors.toList());
            log.info("存储桶：{} 要删除的文件个数： {}", bucket, collect.size());

            List<DeleteObject> objects = collect.stream().map(fileDetail -> new DeleteObject(fileDetail.getFileName())).collect(Collectors.toList());
            RemoveObjectsArgs args = RemoveObjectsArgs.builder().bucket(bucket).objects(objects).build();
            Iterable<Result<DeleteError>> results = minioTool.removeObjects(args);
            try {
                for (Result<DeleteError> result : results) {
                    DeleteError deleteError = result.get();
                    log.info("删除错误信息：{}", JSON.toJSONString(deleteError));
                    log.info("删除文件：{} 异常：{}", deleteError.objectName(), deleteError.message());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Set<String> ids = list.stream().map(FileDetail::getId).collect(Collectors.toSet());
        fileDetailService.removeByIds(ids);
        log.info("========================清理临时文件结束========================");
    }

}
