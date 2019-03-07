package pl.sii.shopsmvc.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sii.shopsmvc.config.PictureUploadProperties;
import pl.sii.shopsmvc.product.ProductSession;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLClassLoader;
import java.net.URLConnection;

@Controller
public class PictureUpdateController {
    private final Resource pictureDir;
    private final Resource anonymousPicture;
    private final Resource defaultProductPicture;
    private final ProductSession productSession;
    private final MessageSource messageSource;

    @Autowired
    public PictureUpdateController(PictureUploadProperties pictureUploadProperties, ProductSession productSession, MessageSource messageSource) {
        pictureDir = pictureUploadProperties.getUploadPath();
        anonymousPicture = pictureUploadProperties.getAnonymousPicture();
        defaultProductPicture = pictureUploadProperties.getDefaultProductPicture();
        this.productSession = productSession;
        this.messageSource = messageSource;
    }

    @RequestMapping("/uploadedProductPicture")
    public void getUploadedPicture(HttpServletResponse response) throws IOException {
        Resource picturePath = productSession.getPicturePath();
        if (picturePath == null) {
            picturePath = defaultProductPicture;
            productSession.setPicturePath(picturePath);
        }

        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.getFilename()));
        IOUtils.copy(picturePath.getInputStream(), response.getOutputStream());
    }
}
