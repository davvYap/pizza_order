package sg.edu.nus.iss.pizza_order.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.edu.nus.iss.pizza_order.model.Delivery;
import sg.edu.nus.iss.pizza_order.model.Order;
import sg.edu.nus.iss.pizza_order.model.Pizza;
import sg.edu.nus.iss.pizza_order.service.PizzaService;

@Controller
@RequestMapping
public class PizzaController {
    @Autowired
    private PizzaService pizzaService;

    @GetMapping(path = "/")
    public String getIndex(HttpSession session, Model model, @ModelAttribute Pizza pizza) {
        session.invalidate();
        model.addAttribute("pizza", pizza);
        return "index";
    }

    @PostMapping(path = "/pizza")
    public String orderPizza(Model model, HttpSession session, @Valid Pizza pizza,
            BindingResult binding, @ModelAttribute Delivery delivery) {
        if (binding.hasErrors()) {
            System.out.println("Pizza order >>>>> has error");
            return "index";
        }
        List<ObjectError> errors = pizzaService.validatePizzaOrder(pizza);
        if (!errors.isEmpty()) {
            for (ObjectError objectError : errors) {
                binding.addError(objectError);
            }
            return "index";
        }

        session.setAttribute("pizza", pizza);
        model.addAttribute("delivery", delivery);
        return "delivery";
    }

    @PostMapping(path = "/pizza/order")
    public String savePizzaOrder(Model model, HttpSession session, @Valid Delivery delivery,
            BindingResult binding) {
        if (binding.hasErrors()) {
            return "delivery";
        }

        Pizza pizza = (Pizza) session.getAttribute("pizza");
        // IMPORTANT
        // System.out.println("Pizza >>>>>>>>>> " + pizza);
        // System.out.println("Delivery >>>>>>>>>> " + delivery);
        Order order = pizzaService.savOrder(pizza, delivery);
        // IMPORTANT
        // System.out.println("Order >>>>>>>>>>>>>>>>>>> " + order);
        model.addAttribute("order", order);
        return "order";
    }

    // NOTE using getOrder method
    @GetMapping(path = "/pizza/order/{orderId}")
    public String getOrderDetails(@PathVariable String orderId, Model model) throws IOException {
        Optional<Order> getOrder = pizzaService.getOrder(orderId);
        Order order = getOrder.get();
        model.addAttribute("order", order);
        return "order";
    }

    // NOTE using getOrderByJSON method
    @GetMapping(path = "/pizza/orderjs/{orderId}")
    public String getOrderDetailsByJSON(@PathVariable String orderId, Model model) throws IOException {
        Optional<Order> getOrder = pizzaService.getOrderByJSON(orderId);
        Order order = getOrder.get();
        model.addAttribute("order", order);
        return "all";
    }
}
