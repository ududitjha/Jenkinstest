/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.stateless.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author mkumar1
 */
public class Utilities {

    public static Date getSQLDate(String date) {
        int dateYear = Integer.parseInt(date.split("-")[2]);
        int dateMonth = Integer.parseInt(date.split("-")[1]) - 1;
        int dateDay = Integer.parseInt(date.split("-")[0]);
        Calendar dateCalendar = GregorianCalendar.getInstance();
        dateCalendar.set(dateYear, dateMonth, dateDay);
        return dateCalendar.getTime();

    }

}
