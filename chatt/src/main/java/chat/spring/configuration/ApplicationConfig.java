package chat.spring.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "chat.spring.db")
@ComponentScan(basePackages = "chat.spring")
public class ApplicationConfig {

}
