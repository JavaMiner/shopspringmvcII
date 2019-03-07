package pl.sii.shopsmvc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "upload.pictures")
public class PictureUploadProperties {
    private String dirName;
    private Resource uploadPath;
    private Resource anonymousPicture;
    private Resource defaultProductPicture;

    public void setUploadPath(String uploadPath) {
        this.uploadPath = new DefaultResourceLoader().getResource(uploadPath);
    }

    public void setAnonymousPicture(String anonymousPicture) {
        this.anonymousPicture = new DefaultResourceLoader().getResource(anonymousPicture);
    }

    public void setDefaultProductPicture(String defaultProductPicture) {
        this.defaultProductPicture = new DefaultResourceLoader().getResource(defaultProductPicture);
    }

    public Resource getUploadPath() {
        return uploadPath;
    }

    public Resource getAnonymousPicture() {
        return anonymousPicture;
    }

    public Resource getDefaultProductPicture() {
        return defaultProductPicture;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }
}
