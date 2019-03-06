package pl.sii.shopsmvc.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    @Qualifier("mockMemoryProductRepository")
    private ProductRepository productRepository;

    @ModelAttribute("allProducts")
    public List<Product> allProducts() {
        return productRepository.findAll();
    }

    @RequestMapping("/products")
    public String displayProducts() {
        return "product/productsPage";
    }
}
