package in.nkn.utils;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mkumar1
 */
public class NKNUserPOJO {

    private long id;
    private String useridofnknuser;
    private long instituteId;
    private String lastName;
    private String dob;
    private String graduationDate;
    private int isProfiled;
    private int showdob;
    private String gender;
    private int isActive;
    private String profileCreationDate;
    private String expiryDate;
    private int showmob;
    private int showemail;
    private String firstName;
    private String mobileNo;
    private String email;
    private long roleid;
    private String[] services;

    public String[] getServices() {
        return services;
    }

    public void setServices(String[] services) {
        this.services = services;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInstituteId() {
        return instituteId;
    }

    public String getUseridofnknuser() {
        return useridofnknuser;
    }

    public void setUseridofnknuser(String useridofnknuser) {
        this.useridofnknuser = useridofnknuser;
    }

    public void setInstituteId(long instituteId) {
        this.instituteId = instituteId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getIsProfiled() {
        return isProfiled;
    }

    public void setIsProfiled(int isProfiled) {
        this.isProfiled = isProfiled;
    }

    public int getShowdob() {
        return showdob;
    }

    public void setShowdob(int showdob) {
        this.showdob = showdob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getShowmob() {
        return showmob;
    }

    public void setShowmob(int showmob) {
        this.showmob = showmob;
    }

    public int getShowemail() {
        return showemail;
    }

    public void setShowemail(int showemail) {
        this.showemail = showemail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate) {
        this.graduationDate = graduationDate;
    }

    public String getProfileCreationDate() {
        return profileCreationDate;
    }

    public void setProfileCreationDate(String profileCreationDate) {
        this.profileCreationDate = profileCreationDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public long getRoleid() {
        return roleid;
    }

    public void setRoleid(long roleid) {
        this.roleid = roleid;
    }
}
