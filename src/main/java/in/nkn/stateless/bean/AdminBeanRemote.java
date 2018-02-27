/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.stateless.bean;

import java.text.SimpleDateFormat;
import javax.ejb.Remote;

/**
 *
 * @author Administrator
 */

@Remote
public interface AdminBeanRemote {

    public boolean authenticate(String username, String password);
    public String updateAdmin(String username, String password, String firstName, 
            String lastName, String email, String mobile, SimpleDateFormat sdfDate);
       public String updateAdminUserAggrement(String userid, String userAggrement);
    public String AdminDetails(String userid);
    public String generateOTP(String userid);
    public String getOTP(String userid);
}
