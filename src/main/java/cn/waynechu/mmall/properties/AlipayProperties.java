package cn.waynechu.mmall.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author waynechu
 * Created 2018-05-28 14:01
 */
@Component
@ConfigurationProperties
@Data
public class AlipayProperties {

    private String alipayCallbackUrl;

}
