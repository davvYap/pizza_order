package sg.edu.nus.iss.pizza_order.model;

import java.io.Serializable;

import jakarta.json.Json;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Pizza implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Must select a pizza !")
    private String pizza;

    @NotNull(message = "Must select a size !")
    private String size;

    @Min(value = 1, message = "Minimum order is 1")
    @Max(value = 10, message = "Maximum order is 10")
    private int quantity;

    public String getPizza() {
        return pizza;
    }

    public void setPizza(String pizza) {
        this.pizza = pizza;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Pizza [pizza=" + pizza + ", size=" + size + ", quantity=" + quantity + "]";
    }

    public static Pizza createFromJSON(JsonObject jsObj) {
        Pizza pizza = new Pizza();
        String jsPizza = jsObj.getString("pizza");
        String jsSize = jsObj.getString("size");
        JsonNumber jsQuantity = jsObj.getJsonNumber("quantity");
        pizza.setPizza(jsPizza);
        pizza.setSize(jsSize);
        pizza.setQuantity(jsQuantity.intValue());
        return pizza;
    }

    public JsonObjectBuilder toJSON() {
        return Json.createObjectBuilder()
                .add("pizza", this.pizza)
                .add("size", this.size)
                .add("quantity", this.quantity);
    }

}
