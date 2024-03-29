package cn.waynechu.mmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author waynechu
 * Created 2018-05-22 13:07
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    private static final String API_VERSION = "1.0.0";
    private static final String SWAGGER_SCAN_BASE_PACKAGE = "cn.waynechu.mmall.web";

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .version(API_VERSION)
                .title("MMALL商城接口")
                .description("MMALL商城前后端接口")
                .license("Apache 2.0")
                .termsOfServiceUrl("http://localhost:8080")
                .contact(new Contact("Wayne Chu", "", "waynechu@waynechu.cn"))
                .build();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }
}
