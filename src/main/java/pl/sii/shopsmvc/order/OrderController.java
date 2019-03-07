package pl.sii.shopsmvc.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.sii.shopsmvc.product.Product;
import pl.sii.shopsmvc.product.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private Basket basket;

    @Autowired
    public OrderController(@Qualifier("inMemoryProductRepository") ProductRepository productRepository, OrderRepository orderRepository, Basket basket) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.basket = basket;
    }

    @RequestMapping("/orders")
    public String displayProducts() {
        return "order/ordersPage";
    }

    @ModelAttribute("orders")
    public List<Product> orders() {
        String username = getUserName();
        return orderRepository.get(username);
    }

    @ModelAttribute("basket")
    public List<Product> ordersFromBasket() {
        return new ArrayList<>(basket.getOrders().values());
    }

    @ModelAttribute("availableProducts")
    public List<Product> availableProducts() {
        return productRepository.findAll().stream()
                .filter(product -> product.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/orders", params = {"addOrderToBasket"}, method = RequestMethod.POST)
    public String addOrderToBasket(HttpServletRequest req) throws IOException {
        String productName = req.getParameter("productName");
        Product product = productRepository.findOne(productName);
        if (product == null) {
            return "redirect:/orders";
        }
        product.setQuantity(product.getQuantity() - 1);
        basket.addOrder(product);
        return "redirect:/orders";
    }

    @RequestMapping(value = "/orders", params = {"removeOrderFromBasket"}, method = RequestMethod.POST)
    public String removeOrderFromBasket(HttpServletRequest req) {
        String productName = req.getParameter("removeOrderFromBasket");
        Product removedProduct = basket.remove(productName);
        Product product = productRepository.findOne(productName);
        product.setQuantity(product.getQuantity() + removedProduct.getQuantity());
        return "redirect:/orders";
    }

    @RequestMapping(value = "/orders", params = {"acceptBasket"}, method = RequestMethod.POST)
    public String acceptBasket() {
        String userName = getUserName();
        orderRepository.add(userName, new HashMap<>(basket.getOrders()));
        basket.clear();
        return "redirect:/orders";
    }

    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
