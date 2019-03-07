package pl.sii.shopsmvc.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.sii.shopsmvc.date.USLocaleDateFormatter;
import pl.sii.shopsmvc.error.EntityNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
public class ProductController {
    @Autowired
    @Qualifier("inMemoryProductRepository")
    private ProductRepository productRepository;

    @Autowired
    private ProductSession productSession;

    @Autowired USLocaleDateFormatter usLocaleDateFormatter;

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

        //String picturePath = "/" + pictureUploadProperties.getDirName() + "/" +productSession.getPicturePath().getFilename();
        //product.setPictureName(picturePath);
        productRepository.save(product);
        model.clear();
        productSession.clear();
        return "redirect:/products";
    }
}
