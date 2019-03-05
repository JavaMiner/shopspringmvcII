package pl.sii.shopsmvc.product;

import org.springframework.stereotype.Repository;
import pl.sii.shopsmvc.error.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    private Map<String, Product> products = new HashMap<>();

    @Override
    public Product update(String name, Product product) throws EntityNotFoundException {
        checkExists(name);
        product.setName(name);
        return save(product);
    }

    @Override
    public Product save(Product product) {
        return products.put(product.getName(), product);
    }

    @Override
    public Product findOne(String name) {
        return products.get(name);
    }

    @Override
    public List<Product> findAll() {
        return products.values().stream().collect(Collectors.toList());
    }

    @Override
    public void delete(String name) throws EntityNotFoundException {
        checkExists(name);
        products.remove(name);
    }

    @Override
    public boolean exists(String name) {
        return products.containsKey(name);
    }

    private void checkExists(String name) throws EntityNotFoundException {
        if (!exists(name)) {
            throw new EntityNotFoundException("Products " + name + " not exists");
        }
    }
}
