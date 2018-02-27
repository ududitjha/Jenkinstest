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
public interface DepartmentBeanRemote {

    public String getDepartment(String departmentId);

    public String createDepartment(String departmentName, String instituteId);

    public String updateDepartment(String departmentName, String instituteId, String departmentId);

    public String deleteDepartment(String departmentId);

    public String getDepartmentByInstSuffix(String instsuffix);

}
