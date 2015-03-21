package com.shipntrip.shipntrip;

/**
 * Created by Freemahn on 21.03.2015.
 */
public class Order1 {
    String owner;
    String worker;
    String title;
    String city;
    String address;
    String comment;
    String status;
    String cost;
    String destination;

    public Order1(String owner, String worker, String title, String city, String address, String comment, String status, String cost, String destination) {
        this.owner = owner;
        this.worker = worker;
        this.title = title;
        this.city = city;
        this.address = address;
        this.comment = comment;
        this.status = status;
        this.cost = cost;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "[" + owner + ", " + title + ", " + city + "]";
    }
}
