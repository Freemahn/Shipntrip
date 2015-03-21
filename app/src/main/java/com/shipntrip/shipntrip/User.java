package com.shipntrip.shipntrip;

/**
 * Created by Freemahn on 21.03.2015.
 */
public class User {
    /*:{"login":"freemahn","name":"Pavel Gordon","currentLocation":"Saint-Petersburg"}*/
    String login;
    String name;
    String currentLocation;

    public User(String login, String name, String currentLocation) {
        this.login = login;
        this.name = name;
        this.currentLocation = currentLocation;
    }
}
