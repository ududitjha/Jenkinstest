/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.utils;

import helper.GenericEJBHelper;
import in.nkn.utils.NKNUserPOJO;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 *
 * @author mkumar1
 */
public class JSONParser {

    public String getAttribute(String jsonString, String attributeName) {

        if (!jsonString.equalsIgnoreCase("Failure")) {
            JSONObject jo = (JSONObject) JSONSerializer.toJSON(jsonString);
            JSONObject jo1 = (JSONObject) JSONSerializer.toJSON(jo.get("User"));

            String attributeValue = jo1.getString(attributeName).toString();
            return attributeValue;
        } else {
            return null;
        }
    }

    public String getValue(String jsonString, String attributeName) {

        if (!jsonString.equalsIgnoreCase("Failure")) {
            JSONObject jo = (JSONObject) JSONSerializer.toJSON(jsonString);


            String attributeValue = jo.getString(attributeName).toString();
            return attributeValue;
        } else {
            return null;
        }
    }

    public NKNUserPOJO getNKNUserPOJO(String jsonString) {

        System.out.println(jsonString);
        //jsonString = "{\"User\":{\"username\":\"shounak.acharya@nic.in\",\"profileCreated\":1,\"dateOfBirth\":\"1986-11-08\",\"email\":\"9811068796\",\"expiryDate\":\"2013-12-13\",\"firstName\":\"Shounak\",\"graduationDate\":\"2013-09-14\",\"instituteID\":\"inst002\",\"isActive\":0,\"lastName\":\"Acharya\",\"mobileNo\":\"9811068796\",\"profileCreationDate\":\"1970-01-01\",\"showDOB\":1,\"showMob\":1,\"showEmail\":1}}";

        NKNUserPOJO user = new NKNUserPOJO();

        if (!jsonString.equals("Failure")) {

            System.out.println("JSON STRING is " + jsonString);
            JSONObject jo = (JSONObject) JSONSerializer.toJSON(jsonString);
            JSONObject jo1 = (JSONObject) JSONSerializer.toJSON(jo.get("User"));
//            JSONObject jo2 = (JSONObject) JSONSerializer.toJSON(jo.get("user"));

            String dobString = jo1.getString("dateOfBirth").toString();
            //String expiryDate = jo1.getString("expiryDate").toString();
            String graduationDate = jo1.getString("graduationDate").toString();
            String profileCreationDate = jo1.getString("profileCreationDate").toString();

            /*
             Date dob = new Date(Integer.parseInt(dobString.split("-")[0]), Integer.parseInt(dobString.split("-")[1]) - 1, Integer.parseInt(dobString.split("-")[2]));
             Date expDate = new Date(Integer.parseInt(expiryDate.split("-")[0]), Integer.parseInt(expiryDate.split("-")[1]) - 1, Integer.parseInt(expiryDate.split("-")[2]));
             Date gradDate = new Date(Integer.parseInt(graduationDate.split("-")[0]), Integer.parseInt(graduationDate.split("-")[1]) - 1, Integer.parseInt(graduationDate.split("-")[2]));
             Date profileDate = new Date(Integer.parseInt(profileCreationDate.split("-")[0]), Integer.parseInt(profileCreationDate.split("-")[1]) - 1, Integer.parseInt(profileCreationDate.split("-")[2]));
             */
            user.setDob(dobString);
            user.setEmail(jo1.getString("email").toString());
            //System.out.println("Email of user is " + jo1.getString("email").toString());
            //user.setExpiryDate(expiryDate);
            user.setFirstName(jo1.getString("firstName").toString());
            user.setGraduationDate(graduationDate);
            System.out.println(jo1.getString("instituteID"));
            user.setInstituteId(Long.parseLong(jo1.getString("instituteID").toString()));
            user.setIsActive(Integer.parseInt(jo1.getString("isActive").toString()));
            user.setIsProfiled(Integer.parseInt(jo1.getString("profileCreated").toString()));
            user.setLastName(jo1.getString("lastName").toString());
            user.setMobileNo(jo1.getString("mobileNo").toString());
            user.setProfileCreationDate(profileCreationDate);
            user.setRoleid(Long.parseLong("3"));
            user.setShowdob(Integer.parseInt(jo1.getString("showDOB").toString()));
            user.setShowemail(Integer.parseInt(jo1.getString("showEmail").toString()));
            user.setShowmob(Integer.parseInt(jo1.getString("showMob").toString()));
            user.setUseridofnknuser(jo1.getString("username").toString());
            user.setGender(jo1.getString("gender").toString());

            String[] test = (String[]) jo1.getJSONArray("services").toArray(new String[jo1.getJSONArray("services").size()]);

            user.setServices(test);

            return user;

        } else {
            return user;
        }
    }

    public String[] getJSONArray(String abc) {

        abc = abc.substring(1, abc.length() - 1);
        String[] test = abc.split(",");
        List<String> xyz = new ArrayList<String>();

        for (int i = 0; i < test.length; i++) {
            String string = test[i];
            string = string.substring(1, string.length() - 1);
            xyz.add(string);
        }
        String[] arr = (String[]) xyz.toArray(new String[xyz.size()]);
        return arr;
    }

    public JSONObject getJSONObject(String string) {
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(string);
        return jsonObject;

    }

    public JSONObject getUserDetailsJSON(String details) {

        JSONObject detail = new JSONObject();
        String res = new GenericEJBHelper().getUserDetails(details);
        if (!res.equals("Failure")) {
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(res);
            JSONObject innerJSON = jsonObj.getJSONObject("User");
            JSONObject jo = (JSONObject) JSONSerializer.toJSON(innerJSON);
            System.out.println("jo:" + jo);
            detail.put("userName", jo.get("username"));
            detail.put("dateOfBirth", jo.get("dateOfBirth"));
            detail.put("email", jo.get("email"));
            detail.put("graduationDate", jo.get("graduationDate"));
            detail.put("lastName", jo.get("lastName"));
            detail.put("mobileNo", jo.get("mobileNo"));
            detail.put("profileCreationDate", jo.get("profileCreationDate"));
            detail.put("gender", jo.get("gender"));
            detail.put("services", jo.get("services"));
            detail.put("designation", jo.get("designation"));
            detail.put("department", jo.get("department"));
            detail.put("empId", jo.get("empid"));
            detail.put("emailSecondary", jo.get("emailsecondary"));
            detail.put("mobSecondary", jo.get("mobSecondary"));
            detail.put("pic", jo.get("picOff"));
            System.out.println("Details: " + details);
            return detail;
        } else {
            return null;
        }
    }
}
