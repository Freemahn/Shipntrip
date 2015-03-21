package com.shipntrip.shipntrip;

import java.util.List;

/**
 * Created by Freemahn on 21.03.2015.
 */
public class ServerAnswer {
    /*
    {"user":{"login":"freemahn","name":"Pavel Gordon","currentLocation":"Saint-Petersburg"},
    "orderList":[],
    "taskList":[{"owner":"xaker4544","worker":"freemahn","title":"??? ??????????? 150 ??","city":"Saint-Petersburg","address":"????? ?????? 123","comment":"?????","status":"in work","cost":"1000.0","destination":"Moskva"}],"pendingList":[]}*/
    User user;
    List<Order> orderList;
    List<Order> taskList;
    List<Order> pendingList;

    public ServerAnswer(User user, List<Order> orderList, List<Order> taskList, List<Order> pendingList) {
        this.user = user;
        this.orderList = orderList;
        this.taskList = taskList;
        this.pendingList = pendingList;
    }
}
