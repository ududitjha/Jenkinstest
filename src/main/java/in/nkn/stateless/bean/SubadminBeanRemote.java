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
public interface SubadminBeanRemote {

    public boolean authenticate(String username, String password);

    public String getSubadminDetails(String userid);

    public String updateSubadmin(String username, String password, String firstName, String lastName, String email, String mobile,
            String expiryDate, String isActive, SimpleDateFormat sdfDate);

    public String createSubadmin(String username, String password, String firstName, String lastName, String email, String mobile, String userService,
            String expiryDate, String isActive, SimpleDateFormat sdfDate);

    public String deleteSubadmin(String userid);

    public String listSubAdmin(String instSuffix, String userService);
}
