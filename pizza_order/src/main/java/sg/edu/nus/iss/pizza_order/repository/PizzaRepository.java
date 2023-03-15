package sg.edu.nus.iss.pizza_order.repository;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.pizza_order.model.Order;

@Repository
public class PizzaRepository {
    @Autowired
    @Qualifier("pizza")
    private RedisTemplate<String, String> redisTemplate;

    public void saveOrder(Order order) {
        redisTemplate.opsForValue().set(order.getOrderId(), order.toJSON().toString());
    }

    public Optional<Order> getOrder(String orderId) throws IOException {
        String json = redisTemplate.opsForValue().get(orderId);
        if (json == null) {
            return Optional.empty();
        }
        Order order = Order.createFromJSON(json);
        return Optional.of(order);
    }

}
