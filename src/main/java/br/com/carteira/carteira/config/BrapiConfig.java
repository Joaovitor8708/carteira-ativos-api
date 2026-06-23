package br.com.carteira.carteira.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "brapi")
public class BrapiConfig {
    private String token;
    private String url;
}
