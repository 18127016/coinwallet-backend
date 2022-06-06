package me.app.coinwallet;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.app.coinwallet.jackson.WebCustomObjectMapper;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication(scanBasePackageClasses = WebConfig.class)
@Slf4j
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("PUT","DELETE","GET","POST","PATCH")
                        .allowCredentials(false);
            }
        };
    }


    @Bean
    public BasicDataSource dataSource() throws URISyntaxException {
        URI dbUri = new URI("postgres://cnvvyssokuxqix:640508c1d6c88b3920f2b3d55e31b6ffd46f1f20ca40487f4c4cec28874be970@ec2-52-204-195-41.compute-1.amazonaws.com:5432/d47n4c075ii89u");

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }


    @Bean
    public ObjectMapper objectMapper() {
        return new WebCustomObjectMapper();
    }

}
