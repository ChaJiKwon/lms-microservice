package test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "test") // Quét các lớp có @Component
class AppConfig {
}
