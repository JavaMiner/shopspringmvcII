package pl.sii.shopsmvc.product;

import pl.sii.shopsmvc.error.EntityNotFoundException;

import java.util.List;

public interface ProductRepository {
    Product update(String name, Product product) throws EntityNotFoundException;
    Product save(Product product);
    Product findOne(String name);
    List<Product> findAll();
    void delete(String name) throws EntityNotFoundException;
    boolean exists(String name);
}
