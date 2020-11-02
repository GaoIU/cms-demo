package com.gaoc.core.model;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.minio.MinioClient;

import java.util.*;

public class MinioTool extends MinioClient {

    public MinioTool(String endpoint, int port, String accessKey, String secretKey) throws IllegalArgumentException {
        super(endpoint, port, accessKey, secretKey);
    }

    public String signConfig(String bucketName, String objectName) {
        if (StrUtil.isBlank(bucketName)) {
            return null;
        }
        if (StrUtil.isBlank(objectName)) {
            objectName = "";
        }

        List<Map<String, Object>> statements = new ArrayList<>(2);
        Map<String, Object> s1 = new HashMap<>(4);
        s1.put("Action", Arrays.asList("s3:GetBucketLocation", "s3:ListBucket"));
        s1.put("Effect", "Allow");
        s1.put("Principal", "*");
        s1.put("Resource", "arn:aws:s3:::" + bucketName);

        Map<String, Object> s2 = new HashMap<>(4);
//        s2.put("Action", Arrays.asList("s3:GetObject", "s3:DeleteObject"));
        s2.put("Action", "s3:GetObject");
        s2.put("Effect", "Allow");
        s2.put("Principal", "*");
        s2.put("Resource", "arn:aws:s3:::" + bucketName + "/" + objectName + "*");

        statements.add(s1);
        statements.add(s2);

        Map<String, Object> config = new HashMap<>(2);
        config.put("Statement", statements);
        config.put("Version", "2012-10-17");

        return JSON.toJSONString(config);
    }

}
