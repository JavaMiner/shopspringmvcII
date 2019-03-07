package pl.sii.shopsmvc.order;

import pl.sii.shopsmvc.product.Product;

import java.util.List;
import java.util.Map;

public interface OrderRepository {
    List<Product> get(String username);
    void delete(String username, String productName);
    void add(String userName, Map<String, Product> orders);
}
