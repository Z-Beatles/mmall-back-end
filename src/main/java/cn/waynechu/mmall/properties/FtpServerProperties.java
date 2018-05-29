package cn.waynechu.mmall.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author waynechu
 * Created 2018-05-23 15:29
 */
@Component
@ConfigurationProperties(prefix = "ftp.server")
@Data
public class FtpServerProperties {

    private String ip;

    private String urlPrefix;

    private String user;

    private String password;

}
