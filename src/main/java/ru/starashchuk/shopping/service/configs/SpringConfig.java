package ru.starashchuk.shopping.service.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@ComponentScan("ru.starashchuk.shopping.service")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {
}
