package pl.sii.shopsmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.sii.shopsmvc.config.PictureUploadProperties;

@SpringBootApplication
@EnableConfigurationProperties(PictureUploadProperties.class)
public class MainClass {
    public static void main(String[] args) {
        SpringApplication.run(MainClass.class, args);
    }
}
