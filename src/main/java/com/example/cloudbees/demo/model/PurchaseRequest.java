package com.example.cloudbees.demo.model;

import com.example.cloudbees.demo.entity.Train;
import com.example.cloudbees.demo.entity.User;
import jakarta.persistence.*;

public class PurchaseRequest {
    private String fromLocation;
    private String toLocation;
    private double pricePaid;
    private User user;

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public double getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(double pricePaid) {
        this.pricePaid = pricePaid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
