package pl.sii.shopsmvc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import pl.sii.shopsmvc.date.USLocaleDateFormatter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.time.LocalDate;

@Configuration
@EnableSwagger2
public class WebConfiguration implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

    private final USLocaleDateFormatter usLocaleDateFormatter;
    private final PictureUploadProperties pictureUploadProperties;

    public WebConfiguration(USLocaleDateFormatter usLocaleDateFormatter, PictureUploadProperties pictureUploadProperties) {
        this.usLocaleDateFormatter = usLocaleDateFormatter;
        this.pictureUploadProperties = pictureUploadProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = null;
        try {
            absolutePath = pictureUploadProperties.getUploadPath().getFile().getAbsolutePath();
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }

        registry.addResourceHandler("/" + pictureUploadProperties.getDirName() + "/**").addResourceLocations("file:" + absolutePath +"/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(LocalDate.class, usLocaleDateFormatter);
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(paths ->  paths.startsWith("/api"))
                .build();
    }
}
