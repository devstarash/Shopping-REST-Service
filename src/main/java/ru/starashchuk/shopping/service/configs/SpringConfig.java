package ru.starashchuk.shopping.service.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.*;
import ru.starashchuk.email.EmailConfig;
import ru.starashchuk.email.EmailService;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@ComponentScan("ru.starashchuk.shopping.service")
@PropertySource("classpath:application.properties")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {

    @Autowired
    private AdminInterceptor adminInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("/")
                .setCachePeriod(3600);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Используем настроенный jacksonConverter с поддержкой JavaTimeModule,
        // чтобы даты (LocalDate/LocalDateTime) не превращались в Invalid Date на фронтенде!
        converters.add(jacksonConverter());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // Отключаем форматирование дат в виде массивов чисел [2026,5,26]
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter(mapper);
        converter.setSupportedMediaTypes(
                List.of(new MediaType("application", "json", StandardCharsets.UTF_8))
        );
        return converter;
    }


    @Bean
    public EmailConfig emailConfig(
            @Value("${mail.host}") String host,
            @Value("${mail.port}") int port,
            @Value("${mail.username}") String username,
            @Value("${mail.password}") String password) {
        return new EmailConfig(host, port, username, password);
    }

    @Bean
    public EmailService emailService(EmailConfig emailConfig) {
        return new EmailService(emailConfig);
    }
}