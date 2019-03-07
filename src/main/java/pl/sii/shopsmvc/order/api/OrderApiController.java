package pl.sii.shopsmvc.order.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sii.shopsmvc.order.OrderRepository;
import pl.sii.shopsmvc.product.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrderApiController {
    private OrderRepository orderRepository;

    @Autowired
    public OrderApiController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public List<Product> orders() {
        String username = getUserName();
        return orderRepository.get(username);
    }

    @RequestMapping(value = "/order/{name}", method = RequestMethod.DELETE)
    public void deleteOrders(@PathVariable String name) {
        String username = getUserName();
        orderRepository.delete(username, name);
        //TODO return product tu repository
    }

    @RequestMapping(value = "/order/{name}", method = RequestMethod.PUT)
    public void addOrders(@PathVariable Product product) {
        String username = getUserName();
        Map<String, Product> orders = new HashMap<>();
        orders.put(product.getName(), product);
        orderRepository.add(username, orders);
        //TODO return product tu repository
    }

    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
