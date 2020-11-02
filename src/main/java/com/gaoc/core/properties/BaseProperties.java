package com.gaoc.core.properties;

import com.gaoc.core.enums.TimeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "com.gaoc.base")
public class BaseProperties {

    /**
     * 创建时间字段，默认："createTime"
     */
    private String createTimeName = "createTime";

    /**
     * 创建时间字段类型，默认：LOCAL_DATE_TIME
     */
    private TimeEnum createTimeType = TimeEnum.LOCAL_DATE_TIME;

    /**
     * 更新时间字段，默认："updateTime"
     */
    private String updateTimeName = "updateTime";

    /**
     * 更新时间字段类型，默认：LOCAL_DATE_TIME
     */
    private TimeEnum updateTimeType = TimeEnum.LOCAL_DATE_TIME;

    /**
     * 开启用户ID认证，默认：false
     */
    private Boolean enableUserId = false;

    /**
     * 需要跳过用户认证的URL，开启用户ID认证时有效
     */
    private List<String> excludeUrl;

    /**
     * 开启跨域，默认：false
     */
    private Boolean enableCors = false;

    /**
     * 登录账号参数名称
     */
    private String usernameParameter = "username";

    /**
     * 登录密码参数名称
     */
    private String passwordParameter = "password";

    /**
     * 文件服务器配置
     */
    private MinioProperties minio = new MinioProperties();

}
