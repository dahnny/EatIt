package com.danielogbuti.eatit.Common;

import com.danielogbuti.eatit.model.Model;

public class Common {
    public static Model currentUser;

    public static final String CHANNEL_ID = "20";

    public static String convertToStatus(String status) {
        if (status.equals("0")){
            return "Placed";
        }else if (status.equals("1")){
            return "On my way";
        }else {
            return "Shipped";
        }
    }
}
