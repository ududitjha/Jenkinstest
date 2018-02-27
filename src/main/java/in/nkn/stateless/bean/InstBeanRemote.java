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
public interface InstBeanRemote {

    String getLDAPDetails(String uid);
    public String getInstituteDetails(String uid);
    public String getInstituteList(String from,String limit);
    public String getLikeInstituteList(String from, String like);
}
