package com.allanlopez.mapschallenge;

/**
 * Created by allanlopez on 23/02/18.
 */

public class Place {
    public String longitude, altitude, name;

    public String toJson(){
        return  "\"" + "name" + "\"" + ":" + "\"" + name + "\"" + ","
                + "\n" + "\"" + "longitude" + "\"" + ":" + "\"" + longitude + "\"" + ","
                + "\n" + "\"" + "altitude" + "\"" + ":" + "\"" + altitude + "\"" ;
    }
}
