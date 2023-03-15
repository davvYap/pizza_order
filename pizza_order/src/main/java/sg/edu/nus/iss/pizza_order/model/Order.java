package sg.edu.nus.iss.pizza_order.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private float totalCost = 0;
    private String orderId;
    private Pizza pizza;
    private Delivery delivery;

    public Order(Pizza pizza, Delivery delivery) {
        this.pizza = pizza;
        this.delivery = delivery;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    @Override
    public String toString() {
        return "Order [totalCost=" + totalCost + ", orderId=" + orderId + ", pizza=" + pizza + ", delivery=" + delivery
                + "]";
    }

    // Optional
    public String getCustomerName() {
        return this.getDelivery().getName();
    }

    public String getPizzaName() {
        return this.getPizza().getPizza();
    }

    public float getTotalPizzaCost() {
        return this.delivery.getRush() ? totalCost + 2 : totalCost;
    }

    // public static JsonObject toJSON(String json) {
    // JsonReader jsReader = Json.createReader(new StringReader(json));
    // return jsReader.readObject();
    // }

    // public static Order createFromJSON(String json) {
    // JsonObject jsObj = toJSON(json);
    // Pizza pizza = Pizza.createFromJSON(jsObj);
    // Delivery delivery = Delivery.createFromJSON(jsObj);
    // Order order = new Order(pizza, delivery);
    // order.setOrderId(jsObj.getString("orderId"));
    // order.setTotalCost((float) jsObj.getJsonNumber("total").doubleValue());
    // return order;
    // }

    public static Order createFromJSON(String json) throws IOException {
        Order order = new Order(null, null);
        if (json != null) {
            try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
                JsonReader jsReader = Json.createReader(is);
                JsonObject jsObj = jsReader.readObject();
                Pizza pizza = Pizza.createFromJSON(jsObj);
                Delivery delivery = Delivery.createFromJSON(jsObj);
                order = new Order(pizza, delivery);
                // althernative:
                // order.setDelivery(delivery);
                // order.setPizza(pizza);
                order.setOrderId(jsObj.getString("orderId"));
                order.setTotalCost((float) jsObj.getJsonNumber("total").doubleValue());
            }
        }
        return order;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("orderId", this.orderId)
                .add("name", this.getCustomerName())
                .add("address", this.getDelivery().getAddress())
                .add("phone", this.getDelivery().getPhoneNum())
                .add("rush", this.getDelivery().getRush())
                .add("comments", this.getDelivery().getComments())
                .add("pizza", this.getPizzaName())
                .add("size", this.getPizza().getSize())
                .add("quantity", this.getPizza().getQuantity())
                .add("total", this.getTotalPizzaCost())
                .build();
    }
}
