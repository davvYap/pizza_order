package sg.edu.nus.iss.pizza_order.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import sg.edu.nus.iss.pizza_order.model.Delivery;
import sg.edu.nus.iss.pizza_order.model.Order;
import sg.edu.nus.iss.pizza_order.model.Pizza;
import sg.edu.nus.iss.pizza_order.repository.PizzaRepository;

@Service
public class PizzaService {
    @Autowired
    private PizzaRepository pizzaRepo;

    public static final String[] PIZZA_NAMES = { "bella", "margherita", "marinara", "spianatacalabrese",
            "trioformaggio" };

    public static final String[] PIZZA_SIZES = { "sm", "md", "lg" };

    private final Set<String> pizzaNames;
    private final Set<String> pizzaSizes;

    @Value("${pizza.order.api.url}")
    private String restPizzaUrl;

    public PizzaService() {
        pizzaNames = new HashSet<>(Arrays.asList(PIZZA_NAMES));
        pizzaSizes = new HashSet<>(Arrays.asList(PIZZA_SIZES));
    }

    public Optional<Order> getOrder(String orderId) throws IOException {
        return pizzaRepo.getOrder(orderId);
    }

    public Order savOrder(Pizza pizza, Delivery delivery) {
        Order order = createPizzaOrder(pizza, delivery);
        calculateTotalCost(order);
        // // IMPORTANT
        // System.out.println("Order >>>>>>>>>>>>>>>> " + order);
        pizzaRepo.saveOrder(order);
        return order;
    }

    public Order createPizzaOrder(Pizza pizza, Delivery delivery) {
        String orderId = UUID.randomUUID().toString().substring(0, 8);
        Order order = new Order(pizza, delivery);
        order.setOrderId(orderId);
        return order;
    }

    public float calculateTotalCost(Order order) {
        float total = 0;
        switch (order.getPizzaName()) {
            case "bella", "marinara", "spianatacalabrese":
                total += 30;
                break;
            case "margherita":
                total += 22;
                break;
            case "trioformaggio":
                total += 25;
                break;

        }

        switch (order.getPizza().getSize()) {
            case "sm":
                total *= 1;
                break;
            case "md":
                total *= 1.2;
                break;
            case "lg":
                total *= 1.5;
                break;
            default:
                total *= 1;
        }

        total *= order.getPizza().getQuantity();
        // not required as we already done it in object class
        // if (order.getDelivery().isRush()) {
        // total += 2;
        // }
        order.setTotalCost(total);
        // IMPORTANT
        // System.out.println("total cost >>>>>>>>>>> " + total);
        return total;
    }

    public List<ObjectError> validatePizzaOrder(Pizza pizza) {
        List<ObjectError> errors = new LinkedList<>();
        FieldError error;

        if (!pizzaNames.contains(pizza.getPizza().toLowerCase())) {
            error = new FieldError("pizza", "pizza", "We do not have the %s pizza".formatted(pizza.getPizza()));
            errors.add(error);
        }

        if (!pizzaSizes.contains(pizza.getSize().toLowerCase())) {
            error = new FieldError("pizza", "size", "We do not have the %s pizza size".formatted(pizza.getSize()));
            errors.add(error);
        }

        return errors;
    }

    public Optional<Order> getOrderByJSON(String orderId) throws IOException {
        String url = UriComponentsBuilder.fromUriString(this.restPizzaUrl + "/" + orderId)
                .toUriString();
        System.out.println("PIZZA URL >>>>>>>>>>>>>>>>> " + url);
        RequestEntity req = RequestEntity.get(url).build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        Order order = Order.createFromJSON(resp.getBody());
        if (order == null) {
            return Optional.empty();
        }
        return Optional.of(order);
    }

}
