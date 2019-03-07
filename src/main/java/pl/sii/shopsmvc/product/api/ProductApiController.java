package pl.sii.shopsmvc.product.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sii.shopsmvc.product.Product;
import pl.sii.shopsmvc.product.ProductRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductApiController {
    private final ProductRepository productRepository;

    public ProductApiController(@Qualifier("inMemoryProductRepository") ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        HttpStatus status = HttpStatus.OK;
        if (!productRepository.exists(product.getName())) {
            status = HttpStatus.CREATED;
        }
        Product saveProduct = productRepository.save(product);
        return new ResponseEntity<>(saveProduct, status);
    }

}
