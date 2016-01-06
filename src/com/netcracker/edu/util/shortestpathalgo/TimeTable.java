package com.netcracker.edu.util.shortestpathalgo;

import java.sql.Time;

/**
 * This class contains static methods for doing time arithmetic.
 *
 * @version JDSL 2
 */

public class TimeTable {

    // Compute the difference between times a and b, taking into account
    // the possibility of crossing days.
    public static int diff(int a, int b) {
        int result = (a - b) % 1440;

        if (result < 0)
            result += 1440;

        return result;
    }

    public static int convertJavaSqlTimeToInt(Time time){
        //hh:mm:ss
        String strTime=time.toString();
        return Integer.parseInt(strTime.substring(0,2))*60+Integer.parseInt(strTime.substring(3,5));
    }

    public static int parseTime(String strTime){
        return Integer.parseInt(strTime.substring(0,2))*60+Integer.parseInt(strTime.substring(3,5));
    }

}
