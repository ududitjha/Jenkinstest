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
public interface MigratedToBeanRemote {

    public String getUserMigratedTo(String username);

    public String createMigratedTo(String username, String oldInstituteId, String newInstituteId, String status, String date);

    public String updateMigratedTo(String id, String username, String oldInstituteId, String newInstituteId, String status, String date);
    public String getMigratedToList();

}
