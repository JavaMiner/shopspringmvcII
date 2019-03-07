package pl.sii.shopsmvc.order;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import pl.sii.shopsmvc.product.Product;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Basket {
    Map<String, Product> orders = new HashMap<>();

    public Map<String, Product> getOrders() {
        return orders;
    }

    public void addOrder(Product product) {
        Product existsProduct = orders.get(product.getName());
        if (existsProduct == null) {
            existsProduct = product.copy();
            existsProduct.setQuantity(0);
        }

        existsProduct.incQuantity();
        orders.put(existsProduct.getName(), existsProduct);
    }

    public void clear() {
        orders.clear();
    }

    public Product remove(String productName) {
        return orders.remove(productName);
    }
}
