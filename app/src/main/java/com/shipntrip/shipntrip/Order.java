package com.shipntrip.shipntrip;

/**
 * Created by Freemahn on 21.03.2015.
 */
public class Order {
    String owner;
    String worker;
    String title;
    String city;
    String address;
    String link;
    String comment;
    String status;
    String cost;

    String destination;
    String date;

    public Order() {


    }

    public Order(String owner, String worker, String title, String city, String address, String link, String comment, String status, String cost, String destination, String date) {
        this.owner = owner;
        this.worker = worker;
        this.title = title;
        this.city = city;
        this.address = address;
        this.link = link;
        this.comment = comment;
        this.status = status;
        this.cost = cost;
        this.destination = destination;
        this.date = date;
    }

    @Override
    public String toString() {
        return  title + ", " + city + ", " + date + ", " + cost;
    }
}
