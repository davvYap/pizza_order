package sg.edu.nus.iss.pizza_order.model;

import java.io.Serializable;

import jakarta.json.JsonObject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Delivery implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Please enter your name")
    @Size(min = 3, message = "Name cannot be less than 3 characters")
    private String name;

    @NotNull(message = "Please enter your address")
    @NotEmpty(message = "Address cannot be empty")
    private String address;

    @NotNull(message = "Please enter your phone number")
    @Pattern(regexp = "^[0-9]{8,}$", message = "Must be a valid phone number")
    private String phoneNum;

    private Boolean rush = false;

    private String comments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Boolean getRush() {
        return rush;
    }

    public void setRush(Boolean rush) {
        this.rush = rush;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Delivery [name=" + name + ", address=" + address + ", phoneNum=" + phoneNum + ", rush=" + rush
                + ", comments=" + comments + "]";
    }

    public static Delivery createFromJSON(JsonObject jsObj) {
        Delivery delivery = new Delivery();
        delivery.setName(jsObj.getString("name"));
        delivery.setAddress(jsObj.getString("address"));
        delivery.setPhoneNum(jsObj.getString("phone"));
        delivery.setRush(jsObj.getBoolean("rush"));
        delivery.setComments(jsObj.getString("comments"));
        return delivery;
    }

}
