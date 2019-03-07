package pl.sii.shopsmvc.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sii.shopsmvc.config.PictureUploadProperties;
import pl.sii.shopsmvc.product.ProductSession;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Locale;

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

    @RequestMapping(value = "/products", params = {"uploadPicture"}, method = RequestMethod.POST)
    public String onUpload(MultipartFile file, RedirectAttributes redirectAttributes, Model model, Locale locale) throws IOException {
        if (file.isEmpty() || !isImage(file)) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("upload.io.wrong.type", null, locale));
            return "redirect:/products";
        }

        Resource picturePath = copyFileToPicture(file);
        productSession.setPicturePath(picturePath);
        return "redirect:/products";
    }

    private Resource copyFileToPicture(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String fileExtension = getFileExtension(filename);
        File tempFile = File.createTempFile("pic", fileExtension, pictureDir.getFile());
        try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return new FileSystemResource(tempFile);//new DefaultResourceLoader().getResource("file:" + tempFile.getPath());
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }

    private boolean isImage(MultipartFile file) {
        return file.getContentType().startsWith("image");
    }
}
