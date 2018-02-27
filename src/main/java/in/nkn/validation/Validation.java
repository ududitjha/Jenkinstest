package in.nkn.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Sushmita
 */
public class Validation {

     Logger log = Logger.getLogger(Validation.class);

    public String validateShareForm(String recipients, String noOfAttempts, String duration) {
        log.debug(" validateShareForm() method invoked ");
        final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d)";
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Z a-z]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,})$";
        JSONObject jsonObject = new JSONObject();
        JSONArray errorArray = new JSONArray();

        if (null == recipients || recipients.length() == 0) {
            errorArray.add("Please Enter Recipients Email.");
        }
        if (recipients.contains(" ")) {
            errorArray.add("Recipients may not contain spaces.");
        }
        if (recipients.startsWith(",") && recipients.endsWith(",")) {
            errorArray.add("Recipients must not start or end with comma.");
        }

        String[] emailArray = recipients.split(",");

        Pattern p = Pattern.compile(EMAIL_PATTERN);

        for (int i = 0; i < emailArray.length; i++) {

            Matcher m = p.matcher(emailArray[i]);
            boolean matchFound = m.matches();
            if (!matchFound) {
                errorArray.add("Please enter a valid email address.");
                break;
            }

        }

        if (null == noOfAttempts || noOfAttempts.length() == 0) {
            errorArray.add("Please Enter no of attempts.");
        }

        if (null != noOfAttempts && noOfAttempts.length() > 0) {

            p = Pattern.compile("[0-9]+");

            Matcher m = p.matcher(noOfAttempts);
            boolean matchFound = m.matches();
            if (!matchFound) {
                errorArray.add("Please Enter numbers only in no. of attempts.");
            } else {
                int attemptCounts = Integer.parseInt(noOfAttempts);
                if (!((attemptCounts > 0) && (attemptCounts <= 10))) {
                    errorArray.add("No. of attemps must be greater than 0 and less than 10.");
                }
            }
        }

        if (null == duration || duration.length() == 0) {
            errorArray.add("Please Enter no of duration.");
        }

        if (null != duration && duration.length() > 0) {

            p = Pattern.compile("[0-9]+");

            Matcher m = p.matcher(duration);
            boolean matchFound = m.matches();
            if (!matchFound) {
                errorArray.add("Please Enter numbers only in duration.");
            } else {
                int shareHours = Integer.parseInt(duration);
                if (!((shareHours > 0) && (shareHours <= 48))) {
                    errorArray.add("You can only share this file for 0-48 hrs period.");
                }
            }
        }
        if (errorArray.isEmpty()) {
            jsonObject.put("STATUS", "SUCCESS");
        } else {
            jsonObject.put("STATUS", "FAILURE");
            jsonObject.put("MESSAGE", errorArray);
        }

        return jsonObject.toString();
    }

    public String ValidateTags(String tags) {
        Logger log = Logger.getLogger(Validation.class);
        log.debug(" ValidateTags() method invoked ");
        JSONObject jsonObject = new JSONObject();
        if (null == tags || tags.length() == 0) {
            jsonObject.put("TAGS", "Please Enter TAGS");
        }
        String[] tagsArray = tags.split(",");

        Pattern p = Pattern.compile("[A-Z a-z]+$");

        if (!tags.contains(",") && tags.contains(" ")) {
            jsonObject.put("TAGSERROR", "Please Enter comma");
        }

        for (int i = 0; i < tagsArray.length; i++) {

            Matcher m = p.matcher(tagsArray[i]);
            boolean matchFound = m.matches();
            if (!matchFound) {
                jsonObject.put("TAGS" + i, "Please Enter the Only Character");
                break;
            }

        }
        if (jsonObject.isEmpty()) {
            jsonObject.put("status", "Succcess");
        }

        return jsonObject.toString();

    }

    public String ValidateInterests(String Interests) {
        Logger log = Logger.getLogger(Validation.class);
        log.debug(" ValidateInterests() method invoked ");
        JSONObject jsonObject = new JSONObject();
        if (null == Interests || Interests.length() == 0) {
            jsonObject.put("Interests", "Please Enter Interests");
        }
        String[] InterestsArray = Interests.split(",");

        Pattern p = Pattern.compile("[A-Z a-z]+$");

        if (!Interests.contains(",") && Interests.contains(" ")) {
            jsonObject.put("InterestsError", "Please Enter comma");
        }

        for (int i = 0; i < InterestsArray.length; i++) {

            Matcher m = p.matcher(InterestsArray[i]);
            boolean matchFound = m.matches();
            if (!matchFound) {
                jsonObject.put("Interests" + i, "Please Enter the Only Character");
                break;
            }

        }
        if (jsonObject.isEmpty()) {
            jsonObject.put("status", "Succcess");
        }

        return jsonObject.toString();

    }
}
