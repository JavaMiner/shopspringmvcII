package pl.sii.shopsmvc.product;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static pl.sii.shopsmvc.product.Unit.Kg;

@Repository
public class MockMemoryProductRepository extends InMemoryProductRepository {
    @PostConstruct
    private void init(){
        Product product = new Product();
        product.setName("Test");
        product.setPrice(new BigDecimal(10));
        product.setQuantity(2);
        product.setUnit(Kg);
        product.setProductionDate(LocalDate.now());
        product.setAttributes(Arrays.asList("Ładne", "czerwone"));
        save(product);
    }
}
