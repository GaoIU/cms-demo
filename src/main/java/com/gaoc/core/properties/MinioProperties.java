package com.gaoc.core.properties;

import lombok.Data;

/**
 * @Description: Minio文件服务配置
 * @Date: 2020/9/16 14:24
 * @Author: Gaoc
 */
@Data
public class MinioProperties {

    /**
     * 主机地址
     */
    private String host = "localhost";

    /**
     * 主机端口
     */
    private int port = 9000;

    /**
     * ak秘钥
     */
    private String accessKey;

    /**
     * sk秘钥
     */
    private String secretKey;

    /**
     * 存储桶名称
     */
    private String bucketName = "chou";

}
