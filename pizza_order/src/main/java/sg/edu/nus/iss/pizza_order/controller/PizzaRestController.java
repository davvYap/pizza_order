package sg.edu.nus.iss.pizza_order.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.pizza_order.model.Order;
import sg.edu.nus.iss.pizza_order.service.PizzaService;

@RestController
@RequestMapping(path = "/order")
public class PizzaRestController {
    @Autowired
    private PizzaService pizzaService;

    @GetMapping(path = "{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrder(@PathVariable String orderId) throws IOException {
        Optional<Order> order = pizzaService.getOrder(orderId);
        // IMPORTANT
        // System.out.println("orderId >>>>>>>>>>>>>>>> " + orderId);
        // System.out.println("order >>>>>>>>>>>>>>>> " + order);

        if (order.isEmpty()) {
            JsonObject response = Json.createObjectBuilder().add("message", "Order %s not found".formatted(orderId))
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(response.toString());
        }
        return ResponseEntity.ok(order.get().toJSON().toString());
    }
}
