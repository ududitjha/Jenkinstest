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
public interface MigratedFromBeanRemote {

    public String getUserMigratedFrom(String username);

    public String createMigratedFrom(String username, String oldInstituteId, String newInstituteId, String status, String date);

    public String updateMigratedFrom(String id, String username, String oldInstituteId, String newInstituteId, String status, String date);
    public String getMigratedFromList();

}
