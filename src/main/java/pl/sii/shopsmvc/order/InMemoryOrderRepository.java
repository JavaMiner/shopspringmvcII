package pl.sii.shopsmvc.order;

import org.springframework.stereotype.Repository;
import pl.sii.shopsmvc.product.Product;

import java.util.*;

@Repository
public class InMemoryOrderRepository implements OrderRepository {
    Map<String, Map<String, Product>> userOrders = new HashMap<>();

    @Override
    public List<Product> get(String username) {
        Map orders = getUserOrders(username);
        return new ArrayList<>(orders.values());
    }

    @Override
    public void delete(String username, String productName) {
        Map<String, Product> orders = getUserOrders(username);
        if (!orders.isEmpty()) {
            orders.remove(productName);
        }
    }

    @Override
    public void add(String userName, Map<String, Product> orders) {
        if (orders.isEmpty()) {
            return;
        }
        userOrders.put(userName, orders);
    }

    private Map getUserOrders(String username) {
        return Optional.ofNullable(userOrders.get(username)).orElse(Collections.EMPTY_MAP);
    }
}
