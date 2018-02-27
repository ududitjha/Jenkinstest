/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.stateless.bean;

import javax.ejb.Remote;

/**
 *
 * @author Administrator
 */
@Remote
public interface GlobalAdminBeanRemote {

    public boolean authenticate(String username, String password);

    public String updateGlobalAdmin(String username, String password, String firstName,
            String lastName, String email, String mobile);

    public String GlobalAdminDetails(String userid);

    public String generateOTP(String userid);

    public String getOTP(String userid);
}
