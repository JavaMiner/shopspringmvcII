package pl.sii.shopsmvc.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.sii.shopsmvc.date.USLocaleDateFormatter;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
public class ProductController {
    @Autowired
    @Qualifier("mockMemoryProductRepository")
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

    @RequestMapping("/products")
    public String displayProducts() {
        return "product/productsPage";
    }

    @RequestMapping(value = "/products", params = {"addAttribute"}, method = RequestMethod.POST)
    public String addAttribute(Product product, BindingResult bindingResult) {
        product.getAttributes().add(null);
        //productSession.saveProduct(product);
        return "product/productsPage";
    }

    @RequestMapping(value = "/products", params = {"removeAttribute"})
    public String removeAttribute(Product product, HttpServletRequest req, BindingResult bindingResult) {
        Integer rowId =  Integer.valueOf(req.getParameter("removeAttribute"));
        product.getAttributes().remove(rowId.intValue());
        return "product/productsPage";
    }

    @ModelAttribute("dateFormat")
    public String localFormat(Locale locale) {
        return usLocaleDateFormatter.getPattern(locale);
    }
}
