package com.shipntrip.shipntrip;

/**
 * Created by Freemahn on 21.03.2015.
 */
public class User {
    /*:{"login":"freemahn","name":"Pavel Gordon","currentLocation":"Saint-Petersburg"}*/
    String login;
    String name;
    String currentLocation;
    String nextLocation;
    String date;

    public User(String login, String name, String currentLocation, String nextLocation, String date) {
        this.login = login;
        this.name = name;
        this.currentLocation = currentLocation;
        this.nextLocation = nextLocation;
        this.date = date;
    }
}
