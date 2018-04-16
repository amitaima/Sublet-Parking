package com.subletparking.subletparking;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 15-Mar-18.
 */

public class Message {
    Long clientId;
    String type;
    String address;
    Date OPhour;
    Date ENDhour;

    public Message(Long id, String ty, String ad, String OP, String END)
    {
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        Date result = null;
        try {
            OPhour = df.parse(OP);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            ENDhour = df.parse(END);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        clientId = id;
        type = ty;
        address = ad;
    }

    public Long getClientId(){return clientId;}
    public String getType(){return type;}
    public String getAddress(){return address;}
    public Date getOPhour(){return OPhour;}
    public Date getENDhour(){return ENDhour;}
}
