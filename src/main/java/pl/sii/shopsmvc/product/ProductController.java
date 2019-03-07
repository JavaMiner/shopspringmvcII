package pl.sii.shopsmvc.product;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.sii.shopsmvc.config.PictureUploadProperties;
import pl.sii.shopsmvc.date.USLocaleDateFormatter;
import pl.sii.shopsmvc.error.EntityNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
public class ProductController {
    private ProductRepository productRepository;
    private ProductSession productSession;
    private USLocaleDateFormatter usLocaleDateFormatter;
    private PictureUploadProperties pictureUploadProperties;
    private MessageSource messageSource;

    public ProductController(@Qualifier("inMemoryProductRepository") ProductRepository productRepository,
                             ProductSession productSession, USLocaleDateFormatter usLocaleDateFormatter,
                             PictureUploadProperties pictureUploadProperties, MessageSource messageSource) {
        this.productRepository = productRepository;
        this.productSession = productSession;
        this.usLocaleDateFormatter = usLocaleDateFormatter;
        this.pictureUploadProperties = pictureUploadProperties;
        this.messageSource = messageSource;
    }

    @ModelAttribute("product")
    public Product getProduct() {
        return productSession.toProduct();
    }

    @ModelAttribute("allProducts")
    public List<Product> allProducts() {
        return productRepository.findAll();
    }

    @ModelAttribute("units")
    public List<Unit> units() {
        return Arrays.asList(Unit.values());
    }

    @ModelAttribute("dateFormat")
    public String localFormat(Locale locale) {
        return usLocaleDateFormatter.getPattern(locale);
    }

    @RequestMapping("/products")
    public String displayProducts() {
        return "product/productsPage";
    }

    @RequestMapping(value = "/products", params = {"addAttribute"}, method = RequestMethod.POST)
    public String addAttribute(Product product, BindingResult bindingResult) {
        product.getAttributes().add(null);
        return "product/productsPage";
    }

    @RequestMapping(value = "/products", params = {"removeAttribute"})
    public String removeAttribute(Product product, HttpServletRequest req, BindingResult bindingResult) {
        Integer rowId =  Integer.valueOf(req.getParameter("removeAttribute"));
        product.getAttributes().remove(rowId.intValue());
        return "product/productsPage";
    }

    @RequestMapping(value = "/products", params = {"removeProduct"}, method = RequestMethod.POST)
    public String removeProduct(HttpServletRequest req) throws EntityNotFoundException {
        String productName = req.getParameter("removeProduct");
        productRepository.delete(productName);
        return "redirect:/products";
    }

    @RequestMapping(value = "/products", params = {"addProduct"}, method = RequestMethod.POST)
    public String saveProduct(@Valid Product product, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            return "product/productsPage";
        }

        String picturePath = "/" + pictureUploadProperties.getDirName() + "/" +productSession.getPicturePath().getFilename();
        product.setPictureName(picturePath);
        productRepository.save(product);
        model.clear();
        productSession.clear();
        return "redirect:/products";
    }


    @ExceptionHandler(value = IOException.class)
    public String handleIOException(Locale locale, Model model) {
        model.addAttribute("error", messageSource.getMessage("upload.io.exception", null, locale));
        return "product/productsPage";
    }

    @RequestMapping("uploadError")
    public String onUploadError(Locale locale, Model model) {
        model.addAttribute("error", messageSource.getMessage("upload.file.too.big", null, locale));
        return "product/productsPage";
    }
}
