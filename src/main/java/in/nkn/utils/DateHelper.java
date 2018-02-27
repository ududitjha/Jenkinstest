/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;

/**
 *
 * @author bdixit
 */
public class DateHelper {

    static final Logger log = Logger.getLogger(DateHelper.class);

    public static String getCurrentDate() {
        return new Date().toString();
    }

    public static String getConvertedDate(String date) {
        log.debug("getConvertedDate() called ");
        log.debug("date " + date);
        try {
            Calendar cal = Calendar.getInstance();
            String calString = cal.getTime().toString();
            String split[] = calString.split(" ");
            String currentTime = split[3];

            Date sDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            String dateAfterParsingIntoString = sDate.toString();
            System.out.println("dateAfterParsingIntoString: " + dateAfterParsingIntoString);
            String dateArray[] = dateAfterParsingIntoString.split(" ");

            String dateConverted = dateArray[0] + " " + dateArray[1] + " " + dateArray[2] + " " + currentTime + " " + dateArray[4] + " " + dateArray[5];
            log.debug("dateConverted " + dateConverted);
            return dateConverted;

        } catch (Exception e) {
            log.error(e.getClass().getName() + ":" + e.getMessage());
            return null;
        }
    }

    public static String convertComplexDateToSimpleString(String dateStr) throws ParseException {
        try {
            log.info("convertComplexDateToSimpleString Called");
            log.debug("dateStr: " + dateStr);
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            Date date = (Date) formatter.parse(dateStr);
            System.out.println(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String formatedDate = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);
            log.debug("formatedDate : " + formatedDate);
            return formatedDate;
        } catch (Exception e) {
            log.error(e.getClass().getName() + ":" + e.getMessage());
            return null;
        }
    }

    public static String getEndDate(String duration) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, Integer.parseInt(duration));
        return cal.toString();
    }

    public static boolean ifDateExpired(String endDate) {
        return false;
    }

    public static void main(String[] args) throws ParseException {
//        String date = DateHelper.getConvertedDate("05/05/2017");
//        System.out.println("Date: " + date);
//        
        String date = DateHelper.convertComplexDateToSimpleString(DateHelper.getCurrentDate());
        System.out.println("Date: " + date);
//      
        //convertStringToSimpleDate("dd");
//        String date = DateHelper.getCurrentDate();
//        System.out.println("date "+date);
        
    }
}
