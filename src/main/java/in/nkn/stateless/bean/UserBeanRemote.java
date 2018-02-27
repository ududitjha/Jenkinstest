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
public interface UserBeanRemote {

    public String getUserDetails(String userid);

    public String updateUser(String uid, String firstName, String lastName,
            String email, String mobile, String dob, String gradDate, String emailSecondary, String mobileSecondary, String empId, long designation);

    public String createUser(String username, String firstName, String lastName, String email, String mobile,
            String dob, String gender, String emailSecondary, String mobileSecondary, String empId, long designation);

    public boolean addRenewalService(String renewal_date, String userid, String serviceName, String noOfMonths);

    public boolean modifyRenewalService(String renewal_date, String userid, String serviceName, String noOfMonths);

    public String listUser(String instSuffix, String from, String limit);

    public String searchUser(String userid);

    public String updateUserProfile(String uid, String firstName, String lastName,
            String email, String mobile, String dob, String gradDate, String emailSecondary, String mobileSecondary, String empId, long designation);

    public String createUserProfile(String username, String firstName, String lastName, String email, String mobile,
            String dob, String gender, String emailSecondary, String mobileSecondary, String empId, long designation, String gradDate, String picOfNKNUser);

    public String updateUserForApproval(String uid, String isProfiledOfNKNUser, String approvalDate, String lastModificationBySubAdmin);

    public String updateUserForReject(String uid, String isProfiledOfNKNUser, String rejectedDate, String rejectedReason, String lastModificationBySubAdmin);

    public String updateUserForBlock(String uid, String isActiveOfNKNUser, String blockingReason, String lastModificationBySubAdmin);

    public String updateUserForUNBlock(String uid, String isActiveOfNKNUser, String lastModificationBySubAdmin);

    public String getUserAlertSettings(String userid);

    public String getUserLoginSettings(String userid);

    public String deleteUserProfileRollBack(String username);
    public String updateUserProfilePic(String uid, String profilePic);

}
