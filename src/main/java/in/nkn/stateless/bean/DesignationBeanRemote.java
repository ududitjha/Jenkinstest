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
public interface DesignationBeanRemote {

    public String getDesignation(String designationId);

    public String createDesignation(String designationName, String departmentId);

    public String updateDesignation(String designationName, String departmentId, String designationId);

    public String deleteDesignation(String designationId);

    public String getDesignationByDepartment(String departmentId);

}
