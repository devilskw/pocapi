package br.com.kazuo.config.spring;

import br.com.kazuo.shared.properties.ApplicationProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class ApplicationEnvConfig implements ApplicationProperties {
    @Value(value="${spring.config.activate.on-profile}")
    private String environment;
}


