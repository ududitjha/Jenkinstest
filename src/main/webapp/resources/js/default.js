/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//var baseurl ="http://nknone.staging.nkn.in:8080/NKNOne"; 
var baseUrl ="http://accounts.staging.nkn.in:8080/Accounts"; 

$(document).ready(function(){
    
    
    $(document).keypress(function (e) {
        if (e.which == 13) {
            $("input[id='submit']").click();
            return false;    //<---- Add this line
        }
    });
    
    $("input:radio").click(function()
    {
        $(this).attr("checked", !$(this).attr("checked"));
        
        
        if($(this).attr("checked"))
            $("#txtSeal").attr("disabled",true);
        else
            $("#txtSeal").removeAttr("disabled");
    });
    
    $("#txtSeal").keyup(function(){
        if($(this).val()!="")
        {
            $("#sealborder img").css("opacity","0.4");
            $("input:radio").attr("disabled",true);
        }
        else
        {
            $("#sealborder img").css("opacity","1");
            $("input:radio").removeAttr("disabled");
        }
        
    });
        
    
//            
//       
//           
     

//        setTimeout(function(){
//           checkToken(); },3000); 
        
        

});


//function checkToken(){
//      var tokenId = getCookie("tokenId");
//       
//    if(tokenId == "" || tokenId == null || tokenId == undefined ){
//     
//       location.href="index.html";
//        return;
//    }
//}

function ajaxStart(_id)
{
    $("#"+_id).attr("disabled","true");
    $("#divLoading").show();
    
    
    $("#submit").attr("disabled","disabled");
    $("#submit").css({
        "opacity":"0.5",
        "cursor":"auto"
    });

}

function ajaxEnd(_id)
{
    $("#"+_id).removeAttr("disabled");
    $("#divLoading").hide();
    
    $("#submit").removeAttr("disabled");
    $("#submit").css({
        "opacity":"1",
        "cursor":"pointer"
    });
            
}
function setService(){
       
    
    console.log(getQueryParams("service")[0]);
    
    //    var serviceFromUrl = getQueryParams("service")[0];
    
    //var serviceName = $("#serviceName").val(serviceFromUrl);
    $("#service").val(getQueryParams("service")[0]);
    //var serviceName = $("#serviceName").attr("value", getQueryParams("service")[0]);
    console.log($("#service").val());
    
    
}

function submitUser(){
    //alert("submitUser");
  
    var browserId = LoadFromLocalStorage("deviceId");
    var sessionId = LoadFromLocalStorage("sessionId");
    // var serviceName = getCookie('serviceName');
    var serviceName = $("#service").val();
    //alert("service:"+serviceName);
   
    var username = $("#username").val();

    //        //create the seal local storage entry
    //        if ( LoadFromLocalStorage('seal') ) {
    //        seal = JSON.parse( LoadFromLocalStorage('seal') );

    //    $("#email_error_message").html(""); // setting html of error msg span to blank
        
        
    if(!validateBlankField("username","User Name"))
        return;
    
    if (!validateEmail("username", "User Name"))
        return;
        
    //    var URL = baseUrl + "/openam/login/checkUser?userName=" + username +
    //    "&realm=users&service=Accounts&browserID=" + browserId+"&sessionID=" + sessionId;
    var URL = baseUrl + "/openam/login/checkUser?userName=" + username +
    "&realm=users&service="+serviceName+"&browserID=" + browserId+"&sessionID=" + sessionId;


    //$("#txtSeal").attr("disabled",true);
    
    //          $("#txtSeal").removeAttr("disabled");


    ajaxStart("submit");
    
    $.ajax({
        url: URL,
        type: "GET",
        success: function (result, status, xhr) {
            ajaxEnd("submit");
            console.log("response from server :--- "+result);
            var data = JSON.parse(result);
            if(data.reason=="Invalid Parameters")
            {
                
                location.href=baseUrl + "/index.jsp?service="+serviceName;  
            }
            else
            {
                if(data.status!="failure")
                {
                    var sessID = data.sessionID;
                    var brID = data.browserID;
                    
              
                    var dataToStore = JSON.stringify(data.seal);
                    console.log("before replace"+dataToStore);
                    if(dataToStore!=undefined && dataToStore!=null)
                        dataToStore = dataToStore.replace(/\"/g, '');
                    console.log("after replace"+dataToStore);
                    if(dataToStore !=undefined||dataToStore !=null) // store the latest seal object 
                        SaveInLocalStorage('seal', dataToStore);//
                    else
                        SaveInLocalStorage('seal', "");//
                
                    SaveInLocalStorage('userName', username);
                    //SaveInLocalStorage('sessionId', data.sessionId);
            
                    if(data.reason.indexOf("authorised") != -1)
                    {
                        $("#username").val("");
                        $("#captcha").val("");
                        showErrorPopup(data.reason);
                        return;
                    }
            
            
                    if(data.reason.indexOf("sub admin") != -1)
                    {
                        showErrorPopup(data.reason);
                        return;
                    }
           
           
                    else if(data.pageName=="UserNameWithCaptcha")            
                        alert("Invalid Username");
            
           
                    location.href =  data.pageName  + ".html?service="+serviceName; 
                }
                else
                {
                    location.href=baseUrl + "/index.jsp?service="+serviceName;    
                }
            }
        }
    });
   
}

function submitUserWithCaptcha()
{
    if(!validateBlankField("username", "UserName"))
        return;
    
    if(!validateBlankField("captcha", "Captcha"))
        return;
    
    if($("#captcha").val()!= LoadFromLocalStorage("captchaValue"))
    {
        showErrorPopup("Please enter correct captcha.");
        return;
    }
        
        
    submitUser();
    
    
}

function checkPassword()  // passwordPage.html
{
    //alert("Inside checkPassword")
    var browserId = LoadFromLocalStorage("deviceId");
    var password = $("#password").val();
    var userName = LoadFromLocalStorage("userName");
    var isUpdate=getQueryParams("action")[0]=="update";
    //    var serviceName=getQueryParams("service");

    var serviceName = $("#service").val();
    
    //alert(serviceName);
    var tokenID = getCookie('tokenId');
     
    if(password==""){
        showErrorPopup("please enter password");
        return;
    }
    
    var myKeyVals = {
        userName : userName , 
        password : password ,
        staySignedIn : $("#chkStaySignedIn").is(":checked") // on/off
    };
    //var cookie = getCookie('serviceName');
    
    SaveInLocalStorage("staySignedIn", $("#chkStaySignedIn").is(":checked"))
    var URL = null;
    if(isUpdate){
        //URL = baseUrl + "/openam/login/checkpasswordwithupdate?realm=users&service=" + cookie + "&browserID=" + browserId;
        URL = baseUrl + "/openam/login/checkpasswordwithupdate?realm=users&service=" + serviceName + "&browserID=" + browserId+"&tokenId="+tokenID;    
    }
    else
    {
        URL = baseUrl + "/openam/login/checkpassword?realm=users&service=" + serviceName + "&browserID=" + browserId;
    }
    ajaxStart("submit");
    $.ajax({
        //url : baseUrl + "/openam/login/checkpassword?realm=users&service=" + cookie + "&browserID=" + browserId,
        url : URL,
        type:"POST",
        data:myKeyVals,
        success:function(result, status, xhr){
            ajaxEnd("submit");
            var data = JSON.parse(result);
            
            if(data.reason=="Invalid Parameters")
            {
                
                location.href=baseUrl + "/index.jsp?service="+serviceName;  
            }
            else
            {
                // console.log(data);
                // alert("____________"+data.tokenID);
                if(data.status=="success"){
                    //                if(data.service=="Accounts")
                    //                {
                    //                    if(data.pageURL){
                    //                        //alert(data.pageURL);
                    //                        location.href=data.pageURL;
                    //                    }
                    //                }
                    //                else
                    //                {   
                    //                    location.href = data.url;
                    //                }
                    //                return;
                    if(data.codeVerification!="Y" )
                    {
                        //SaveInLocalStorage("tokenId", data.tokenID);
                        document.cookie = "sessionId=; expires=Thu,  01 Jan 1970 12:00:00 UTC; path=/;domain=.staging.nkn.in;";
                    
                        if(data.service!="Accounts") // any client service
                            location.href = data.url;
                        else
                            location.href = "home.html";
                        return;
                    }
                    else
                    {
                        // {"status":"success","OTPOnPhone":"Y","OTPOnEmail":"Y","uid":"shounak.acharya@nic.in","service":"Accounts","realm":"users","time":"25-05-2016 15:24:30","tokenID":"a6d72d87798f4facbe5eff3cf0bf6d50ed884fd7b19294f690659462738f133c"}
                        if(data.OTPOnPhone=="Y" ||data.twoStepOnPhone=="Y")
                            SaveInLocalStorage("OTPOnPhone", true);    
                        if (data.OTPOnEmail=="Y" ||data.twoStepOnEmail=="Y")
                            SaveInLocalStorage("OTPOnEmail", true);    

                        SaveInLocalStorage("currentCase", data.currentCase);    
                        location.href = "OTPPage.html?service="+serviceName;
                        return;
                    }
                }    
                else
                {
                    if(data.noOfAttempts<3)
                    {
                        showErrorPopup("Incorrect Password. Password attempts : " + data.noOfAttempts+ ". Left : " + (3-data.noOfAttempts));
                    }
                    else if(data.noOfAttempts==3)
                    {
                        location.href = "UserNameWithCaptcha.html?service="+serviceName;
                    }
                    else if(data.reason=="Matched Previous Password")
                    {
                        //ask for locking account
                        var txt;
                        var r = confirm("It seems your password has been changed recently, If its not changed by you, lock your account?");
                        if (r == true) {
                    
                            //send otp for verification of locking account
                            var browserId = LoadFromLocalStorage("deviceId");
                            var userName = LoadFromLocalStorage("userName");
                            var sessionId = LoadFromLocalStorage("sessionId");
                        
                      
                            $.ajax({
                                type:"POST",
                                url:baseUrl + "/openam/SecurityView/SendLockCode?userName=" + userName +"&realm=users&service=Accounts&settingName=" + "lockAccount" + "&browserID=" + browserId+ "&sessionId=" + sessionId,
                                success: function (data) {
                                    if(JSON.parse(data).status=="success")
                                
                                        OpenOtpVerify();
                                }
                            });
                        
                        //                        deleteAllCookies();
                        //                        localStorage.clear();
                        //                        document.cookie = "serviceName=" +serviceCookie + "; expires=Thu, 18 Dec 2020 12:00:00 UTC; path=/" ;
                        //                        location.href="index.html";
                        } 
                        
                    }
                }
            }
        }
    });
 
    
}
var IsotpCaptchaEnabled=false;
function LockAccount()
{
    //verify captcha if captcha enabled
    var browserId = LoadFromLocalStorage("deviceId");
    var sessionId = LoadFromLocalStorage("sessionId");
    var userName = LoadFromLocalStorage("userName");
        
    var cookie = getCookie('serviceName');
    
    
    //verifying otp and if verified, lock user account
    $.ajax({
        url:baseUrl + "/openam/login/lock?realm=users&service="+ cookie + "&browserID=" + browserId + "&sessionId=" + sessionId+ "&otp=" + $("#txtOtp").val()+ "&userName=" + userName,
        type:"POST",
        //        data:{
        //            userName: LoadFromLocalStorage("userName"),
        //            otp: $("#txtOtp").val()
        //        },
        success:function(result, status, xhr){
                    
            var data = JSON.parse(result);
                    
                    
                    
            if(data.status=="success"){
                alert("Account Locked");
                
                //if(data.pageURL){
                
                var serviceCookie= getCookie("serviceName");
                deleteAllCookies();
                localStorage.clear();
                document.cookie = "serviceName=" +serviceCookie + "; expires=Thu, 18 Dec 2020 12:00:00 UTC; path=/" ;
                location.href=baseUrl+ "/index.jsp";
                return;
            //}
                
            //location.href =  "index.html";
            }    
            else
            {
                //showErrorPopup("Account Lock Failed. Try Again.");
                
                if(data.AccountLockOTPAttempts<3)
                {
                    alert("Incorrect Otp. Otp attempts : " + data.AccountLockOTPAttempts+ ". Left : " + (3-data.AccountLockOTPAttempts));
                }
                else if(data.AccountLockOTPAttempts==3)
                {
                    location.href = "UserNameWithCaptcha.html";
                }
                else
                    showErrorPopup(data.reason);    
                
                
                
            //                showErrorPopup("otpAttempts : " + data.otpAttempts);
            //                if(data.otpAttempts>=3)
            //                {
            //                    $("#divCaptcha").show();
            //                    IsotpCaptchaEnabled=true;
            //                }
            }
        }
              
    });
}

function resendOtpLockAccount()
{
    var browserId = LoadFromLocalStorage("deviceId");
    // resend otp for locking account
    var userName = LoadFromLocalStorage("userName");
    var sessionId = LoadFromLocalStorage("sessionId");
    alert(sessionId)        ;
    $.ajax({
        type:"POST",
        url:baseUrl + "/openam/SecurityView/SendLockCode?userName=" + userName +"&realm=users&service=Accounts&settingName=" + "lockAccount" + "&browserID=" + browserId+ "&sessionId=" + sessionId,
        success: function (data) {
            if(JSON.parse(data).status=="success")
            {
                alert("OTP Sent")
            }
        }
    });
}

function verifyPasswordFromLdap()
{
         
    var browserId = LoadFromLocalStorage("deviceId");
         
    //        var userName  = $("#txtUserName").val();
    //        var password = $("#txtPassword").val();
        
    if(!validateBlankField("txtUserName", "Username")) 
        return;
    if(!validateBlankField("txtPassword", "Password")) 
        return;
    
    if(!validateBlankField("captcha", "Captcha"))
        return;
    
    if($("#captcha").val()!= LoadFromLocalStorage("captchaValue"))
    {
        showErrorPopup("Please enter correct captcha.");
        return;
    }
    //        if(userName  ==""){
    //            alert("please enter userName");
    //        }
    //        
    //        if(password==""){
    //            alert("please enter password");
    //        }
    var userName  = $("#txtUserName").val();
    var password = $("#txtPassword").val();
        
    var myKeyVals = {
        username : LoadFromLocalStorage("userName"), 
        password : password
    };
    
    ajaxStart("submit");
        
    $.ajax({
        url:baseUrl + "/openam/login/verifyLdap?realm=users&service=Accounts&browserID=" + browserId,
        type:"POST",
        data:myKeyVals,
        success:function(result, status, xhr){
            ajaxEnd("submit");
                    
            var data = JSON.parse(result);
                    
            if(data.status=="success"){
                SaveInLocalStorage("tokenId", data.tokenID);
                document.cookie = "sessionId=; expires=Thu,  01 Jan 1970 12:00:00 UTC; path=/;domain=.staging.nkn.in;";
            //showErrorPopup("Successfully Logged in");
            }    
            else
            {
                if(data.noOfLDAPAttempt<3)
                {
                    showErrorPopup("Incorrect Password. Password attempts : " + data.noOfLDAPAttempt+ ". Left : " + (3-data.noOfLDAPAttempt));
                    return;
                }
                else if(data.noOfLDAPAttempt==3)
                {
                 
                    location.href = "UserNameWithCaptcha.html";
                    return;
                }
                else
                {
                    showErrorPopup(data.reason);    
                    return;
                }
            }
                
            //location.href =  data.pageName + ".html";
            location.href =  "home.html";    
        }
              
    });
}

function VerifyOtp()
{
    var browserId = LoadFromLocalStorage("deviceId");
    var twoStepCheck="";
    
    if(!$("#txtcode").is(":hidden"))
    {
        if(!validateBlankField("txtcode", "OTP"))
            return;
    }
    
    //    if(!$("#txtcodeEmail").is(":hidden"))
    //    {
    //        if(!validateBlankField("txtcodeEmail", "Email OTP"))
    //            return;
    //    }
    
    
    if($("#remember").is(":checked")==false)
        twoStepCheck="N";
    else
        twoStepCheck="Y";
    
    var currentCase= LoadFromLocalStorage("currentCase");
    //   var cookie = getCookie('serviceName');
    var serviceName = $("#service").val();
    ajaxStart("submit");
    
    var urll = baseUrl + "/openam/login/verifyCode?realm=users&service=" + serviceName + "&browserID=" + browserId;
    
    //    if($("#chkUseBackUpCode").is(":checked")==true)
    //    {
    //        urll = baseUrl + "/openam/login/verifyBackupCode?realm=users&service=" + cookie + "&browserID=" + browserId;
    //    }
    
    $.ajax({
        url:urll,
        type:"POST",
        data:{
            userName: LoadFromLocalStorage("userName") ,
            code: $("#txtcode").val(),
            currentCase : currentCase,
            twoStepCheck : twoStepCheck,
            staySignedIn : LoadFromLocalStorage("staySignedIn")
        },
        success:function(result, status, xhr){
            
            ajaxEnd("submit");
            var data = JSON.parse(result);
                
            if(data.status=="success"){
                
                if(data.service=="Accounts")
                {
                    if(data.pageURL){
                        //alert(data.pageURL);
                        //location.href=data.pageURL;
                        location.href = "home.html?service="+serviceName;
                    }
                }
                else
                {   
                    location.href = data.url;
                }
                
                SaveInLocalStorage("tokenId", data.tokenID);
                document.cookie = "sessionId=; expires=Thu,  01 Jan 1970 12:00:00 UTC; path=/;domain=.staging.nkn.in;";
                //eraseCookie("sessionId");
                
                //alert("Successfully Logged in");
                //location.href = "home.html";
                return;
                
            }    
            else
            {
                
                if(data.noOfOTPAttempts<3)
                {
                    showErrorPopup("Incorrect OTP. OTP attempts : " + data.noOfOTPAttempts+ ". Left : " + (3-data.noOfOTPAttempts));
                }
                else if(data.noOfOTPAttempts==3)
                {
                    location.href = "UserNameWithCaptcha.html";
                }
                else if(data.reasonPending=="Your Profile has been Rejected")
                {
                    alert(data.reasonPending);
                    location.href = baseUrl+ "/index.jsp?service="+serviceName;
                    return;
                }
                else if(data.reasonPending=="Profile is pending for approval")
                {
                    SaveInLocalStorage("profile", "pending");
                    alert(data.reasonPending);
                    location.href = data.pageName+".html";
                    return;
                }
                else
                {
                    showErrorPopup(data.reason);    
                    return;
                }
                
            //showErrorPopup("Error");   
            //location.href = "UserNameWithCaptcha.html";
            }
        }
    });
    
}

var errArray=new Array();


function loadDepartment()
{
    
    var browserId = LoadFromLocalStorage("deviceId");
    var suffix= LoadFromLocalStorage("userName").split("@")[1]; // getting institute suffix from username
               
    $.ajax({
        type: "GET",
        url:baseUrl + "/openam/profile/department/list?instSuffix=" + suffix + "&browserID=" + browserId,
        dataType: "json",
        success: function (data) {
            $.each(data.departments,function(i,val)
            {
                var div_data = "<option value="+val.id+">"+val.name+"</option>";
                $(div_data).appendTo('#department'); 
            });  
            
            
        }
    });
}

function loadDesignation(obj)
{
    var browserId = LoadFromLocalStorage("deviceId");
    var id = $(obj).find(":selected").val();
    console.log(id);
                   
    $.ajax({
        url: baseUrl + "/openam/profile/designation/list?departmentId=" +id+ "&browserID=" + browserId,
                        
        type: 'GET',
        dataType: "json",
        success: function (result) {
            // Variable data contains the data you get from the action method
                        
            if(result.Designations.length==undefined)
            {
                var div_data = "<option value="+result.Designations.id+">"+result.Designations.name+"</option>";
                $(div_data).appendTo('#designation'); 
            }
            else
            {
                $.each(result.Designations,function(i,val)
                {
                    var div_data = "<option value="+val.id+">"+val.name+"</option>";
                    $(div_data).appendTo('#designation'); 
                }); 
            }
        }
    });
}

function checkFirstName()
{
    var field="firstName";
    if(!highlightBlankField(field, "First Name")) 
        return false;
            
    if(!highlightRange(field, "First Name", 1, 25))
        return false;
           
    var regex= /^[A-Za-z ]+$/;
            
    var selector=$("#firstName");
    var error="Special characters and Numbers are not allowed in First Name";
    if(!selector.val().match(regex))  
    {  
        addErrorArray(field,error);
        return false;
    }
    else{
        removeErrorArray(field);
        return true;
    }
    
    return true;
}

function checkLastName()
{
    if(!highlightBlankField("lastName", "Last Name")) 
        return false;
            
    if(!highlightRange("lastName", "Last Name", 1, 25))
        return false;
            
    if(!highlightAlphabetOnly("lastName", "Last Name"))
        return false;
    
    return true;
}

function checkPhonePrimary()
{
    if(!highlightBlankField("phonePrimary", "Primary Phone Number")) 
        return false;
            
    if(!highlightLength("phonePrimary", "Primary Phone Number", 10))
        return false;
    
    return true;
}

function checkPhoneSecondary()
{
    var field="phoneSecondary";
    if(!highlightLength(field, "Secondary Phone Number", 10))
        return false;
    
    var error="Primary Phone Number should not be same as Secondary Phone Number";
    
    if($("#phonePrimary").val()==$("#phoneSecondary").val()&&$("#phonePrimary").val()!="")
    {
        $("#phonePrimary").addClass("error");
        addErrorArray(field,error);
        return false;
    }
    else if($("#phonePrimary").val()!=""&&$("#phoneSecondary").val()!=""){
        
        $("#phonePrimary").removeClass("error");
        removeErrorArray(field);
        return true;
    }   
    
    return true;
}

function checkEmailPrimary()
{
    if(!highlightBlankField("emailPrimary", "Primary Email Id")) 
        return false;
            
    if(!highlightEmail("emailPrimary","Primary Email Id"))
        return false;
    
    return true;
}

function checkEmailSecondary()
{
    var field="emailSecondary";
    if(!highlightEmail(field,"Secondary Email Id"))
        return false;
    
    var error="Primary Email should not be same as Secondary Email";
    
    if($("#emailPrimary").val()==$("#emailSecondary").val()&&$("#emailPrimary").val()!="")
    {
        $("#emailPrimary").addClass("error");
        addErrorArray(field,error);
        return false;
    }
    else if($("#emailPrimary").val()!=""&&$("#emailSecondary").val()!=""){
        
        $("#emailPrimary").removeClass("error");
        removeErrorArray(field);
        return true;
    } 
    
    return true;
}

function checkDOB()
{
    if(!highlightBlankField("dob", "Date of Birth")) 
        return false;
    
    return true;
}

function checkEmpId()
{
    if(!highlightBlankField("empId", "Employee Id")) 
        return false;
    
    return true;
}

function chkPassword()
{
    if(!highlightBlankField("password", "Password")) // if return false , return function
        return false;
            
    if(!highlightLength("password", "Password Prefix", 6))
        return false;
            
    if(!highlightIndexOfString("password", "space not allowed in Password Prefix", " "))
        return false;
    
    return true;
}

function checkConfirmPassword()
{
    var field="confirmpassword";
    if(!highlightBlankField(field, "confirm Password")) 
        return;
            
    if(!highlightRange(field, "Confirm Password Prefix", 6, 50))
        return;
            
    if(!highlightIndexOfString(field, "space not allowed in Confirm Password Prefix", " "))
        return;   
         
    var error="Password Prefix and Confirm Password Prefix are not matching";
    
    if($("#password").val()!=$("#confirmpassword").val())
    {
        $("#password").addClass("error");
        addErrorArray(field,error);
        return false;
    }
    else{
        $("#password").removeClass("error");
        removeErrorArray(field);
        return true;
    } 
    
    return true;
}

function checkDepartment()
{
    var field="department";
    var error="Please select Department";
    
    
    
    if($("#department").val()==0||$("#department").val()==undefined||$("#department").val()==null) 
    {
        addErrorArray(field, error);
        return false;
    }
    else{
        removeErrorArray(field);
        showError();
        return true;
    } 
    
    return true;
}


function checkGender()
{
    var field="gender";
    var error="Please select Gender";
    
    if($("#gender").val()==0||$("#gender").val()==undefined||$("#gender").val()==null) 
    {
        addErrorArray(field, error);
        return false;
    }
    else{
        removeErrorArray(field);
        showError();
        return true;
    } 
    
    return true;
}


function checkDesignation()
{
    var field="designation";
    var error="Please select Designation";
    
    if($("#designation").val()==0) 
    {
        addErrorArray(field, error);
        return false;
    }
    else{
        removeErrorArray(field);
        return true;
    } 
    
    return true;
}



function createProfile()
{
    
    
    var browserId = LoadFromLocalStorage("deviceId");
    var sessionId = LoadFromLocalStorage("sessionId");
    var isUpdate=getQueryParams("action")[0]=="update";
    
    errArray=new Array();
    var userName = LoadFromLocalStorage("userName");
    
    
   
    
    //first name
    if(!checkFirstName())
    {
        showError();
        return;
    }

    // last name
    if(!checkLastName())
    {
        console.log("last name");            
        console.log(errArray);            
        showError();
        return;
    }

    // phone primary
    if(!checkPhonePrimary())
    {
        console.log(errArray);
        showError();
        return;
    }
    
    if(!isP_Mobile && $("#phonePrimary").attr("disabled")==undefined)
    {
        addErrorArray("phonePrimary","Please verify Primary Phone");
        showError();
        return;
    }
    else
    {
        removeErrorArray("phonePrimary");
        showError();
    }
   

            
    // phone secondary
    if(!checkPhoneSecondary())
    {
        showError();
        return;
    }
    
    if(!isS_Mobile&&$("#phoneSecondary").val()!="" && $("#phoneSecondary").attr("disabled")==undefined)
    {
        addErrorArray("phoneSecondary","Please verify Secondary Phone");
        showError();
        return;
    }
    else
    {
        removeErrorArray("phoneSecondary");
        showError();
    }

       
    // email primary
    if(!checkEmailPrimary())
    {
        showError();
        return;
    }
    
    if(!isP_Email && $("#emailPrimary").attr("disabled")==undefined)
    {
        addErrorArray("emailPrimary","Please verify Primary Email");
        showError();
        return;
    }
    else
    {
        removeErrorArray("emailPrimary");
        showError();
    }
   

            
    //email secondary
    if(!checkEmailSecondary())
    {
        showError();
        return;
    }
    
    if(!isS_Email&&$("#emailSecondary").val()!="" && $("#emailSecondary").attr("disabled")==undefined)
    {
        addErrorArray("emailSecondary","Please verify Secondary Email");
        showError();
        return;
    }
    else
    {
        removeErrorArray("emailSecondary");
        showError();
    }

          
    //dob
    if(!checkDOB())
    {
        showError();
        return;
    }
    
    
    //department
    if(!checkGender())
    {
        showError();
        return;
    }

           
    //department
    if(!checkDepartment())
    {
        showError();
        return;
    }

    
    //designation
    if(!checkDesignation())
    {
        showError();
        return;
    }
    
    
    //empId
    if(!checkEmpId())
    {
        showError();
        return;
    }
    
     
    if(!isUpdate)
    {
     
        //password
        if(!chkPassword())
        {
            showError();
            return;
        }

         
        // confirm password
        if(!checkConfirmPassword())
        {
            showError();
            return;
        }
    
        console.log(errArray);  

        var sealImage=$('input[name="seal"]:checked').next().attr("src");
        var error="Please select Seal Image or Type Seal Text";
    
        if(sealImage!=undefined && $("#txtSeal").val()!="")
        {
            addErrorArray("", error);
            return false;
        }
        else
            removeErrorArray("");
    }
         
    showError();

    //if(!isUpdate)
    {
        if(!highlightIndexOfString("txtSeal", "'/' not allowed in Seal Text", "/"))
            return; 
    
        var sealValue="";
        
        
        if($("#txtSeal").val()=="")
        {
            
            var value= $('input[name="seal"]:checked').next().attr("src");
            sealValue= value;
        }
        else
            sealValue= $("#txtSeal").val();
    }
    
    
    
    var form = $("#form");
    
    var myform = $('#form');

    // Find disabled inputs, and remove the "disabled" attribute
    //var disabled = myform.find(':input:disabled').removeAttr('disabled');
    var disabled = myform.find('.disabledInput').removeAttr('disabled');


    
    //console.log(form.serialize());
    var service = getCookie("serviceName");
    var URL=baseUrl + "/openam/profile/create?realm=users&service="+service+"&username=" + userName+"&browserID=" + browserId+"&sessionId=" + sessionId;
    
    if(isUpdate)
        URL=baseUrl + "/openam/profile/update?realm=users&service=Accounts&username=" + userName +"&browserID=" + browserId+"&sessionId=" + sessionId;
   
    ajaxStart("submit");
   
    $.ajax({
        url : URL,
        type : "POST",
        data : myform.serialize() + '&' + $.param({
            "signOnSeal" : sealValue 
        }) + '&' + $.param({
            "profileUpdateOTP" : $("#otp").val()
        }),
        success : function(result, status, xhr){
            disabled.attr('disabled','disabled');
            
           
            // alert(result);
            var data = JSON.parse(result);
            //alert(data);
            console.log("update data"+data);
            if(data.status=="success"){
                //"picofnknuser" : $('#profileImg')[0].files[0] 
                
                
                if( $('#profileImg')[0].files[0] !=undefined&&$('#profileImg')[0].files[0] !=null)
                {
                    
                    //upload profile picture 
                    
                    
                    var formData = new FormData();
                    formData.append('picofnknuser', $('#profileImg')[0].files[0]);
                    
                    var imageUploadURL=baseUrl + "/openam/profile/upload/pic?realm=users&service="+service+"&username=" + userName+"&browserID=" + browserId+"&sessionId=" + sessionId;
                    
                    $.ajax({
                        url : imageUploadURL,
                        type : "POST",
                        data : formData,
                        contentType: false,
                        processData: false,
                        success : function(result, status, xhr){
                            
                            var data = JSON.parse(result);
                            //alert(data);
                            console.log("update data"+data);
                            if(data.status=="success"){
                                
                            }
                            else
                            {
                                alert(data.reason);
                            }
                            
                            ajaxEnd("submit");
                            showErrorPopup("Profile Successfully created");
                            
                            location.href="home.html";
                            return;
                        }
                    });
                      
                }
                else
                {
                    ajaxEnd("submit");
                    showErrorPopup("Profile Successfully created");
                    location.href="home.html";
                    return;
                }
                  
                  
                  
                  
                  
                  
                
            //                alert("Redirecting");    
            //                  alert(data.page);    
            //                location.href = data.page;
            //  location.href = "homePage.html";
            }    
            else
            {
                showErrorPopup(data.reason);   
                
            }
            
            ajaxEnd("submit");
        }
    });
 
}

function migrateUser(){
    
    
    if(!validateBlankField("oldUsername", "User Name"))
        return;
    
    if(!validateBlankField("oldPassword", "Password"))
        return;
    
    var browserId = LoadFromLocalStorage("deviceId");
    var sessionId = LoadFromLocalStorage("sessionId");
    var userName = LoadFromLocalStorage("userName");
    var oldUserName = $('#oldUsername').val();
    var oldPassWord = $('#oldPassword').val();
    var newUserName = LoadFromLocalStorage('userName');
    
    $.ajax({
        url:baseUrl + "/openam/profile/create/migrate?realm=users&service=Accounts&browserID=" + browserId+"&sessionId=" + sessionId,
        type:"POST",
        data:{
            oldUName : oldUserName,
            password : oldPassWord,
            newUName :newUserName 
        },
        success:function(result, status, xhr){
            
            
            if(result=="success")
            {
                showErrorPopup("Successfully Migrated");
                location.href =  "homePage.html";
            }
            else
            {
                showErrorPopup("Failure");
            }
            
                    
        //            var data = JSON.parse(result);
        //                    
        //            if(data.status=="success"){
        //                alert("Migrated");
        //            }    
        //            else
        //            {
        //                alert("error");   
        //            }
                
        // location.href =  data.pageName + ".html";
                
        }
              
    });
        
    
    
}

function resendCode(type)
{
    
    var browserId = LoadFromLocalStorage("deviceId");
    var userName = LoadFromLocalStorage("userName");
    var currentCase = LoadFromLocalStorage("currentCase");
    //var cookie = getCookie('serviceName');
    var serviceName = $("#service").val();
    
    $.ajax({
        url:baseUrl + "/openam/login/resend?realm=users&service=" +serviceName + "&userName=" + userName + "&currentCase=" + currentCase + "&type=" + type +"&browserID=" + browserId,
        type:"POST",
        success:function(result, status, xhr){
            
                    
            var data = JSON.parse(result);
                            
            if(data.status!="success"){
                showErrorPopup("Error in resending code");
            }    
            else
            {
                showErrorPopup("Resend code successful");
                
            }
            
                
        // location.href =  data.pageName + ".html";
                
        }
              
    });
    
}

function listSetting(){
            
    var browserId = LoadFromLocalStorage("deviceId");
    var  userName = LoadFromLocalStorage("userName");
    var  sessionId = LoadFromLocalStorage("sessionId");
    
    $.ajax({
        url:baseUrl + "/openam/SecurityView/listSettings?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
        type: "GET",
        success: function (result, status, xhr) {
                        
            var data = JSON.parse(result);
            
            $('#chkTwoStepPhone').prop('checked', data.settingList.twoStepOnPhone=="Y");
            $('#chkTwoStepEmail').prop('checked', data.settingList.twoStepOnEmail=="Y");
            $('#chkOTPPhone').prop('checked', data.settingList.OTPOnPhone=="Y");
            $('#chkOTPEmail').prop('checked', data.settingList.OTPOnEmail=="Y");
            $('#chkLoginAlert').prop('checked', data.settingList.loginAlert=="Y");
            $('#chkChangePassword').prop('checked', data.settingList.passwordAlert=="Y");
            $('#chkProfileUpdationAlert').prop('checked', data.settingList.profileUpdateAlert=="Y");
            $('#chkHackAlert').prop('checked', data.settingList.hackAlert=="Y");
            $('#chkPrevPassAlert').prop('checked', data.settingList.prevPasswordAlert=="Y");
            $('#chkOTPChangePassword').prop('checked', data.settingList.changePassOtp=="Y");
            $('#chkOTPProfileUpdation').prop('checked', data.settingList.profileUpdateOtp=="Y");
            
            $("#loader").hide();
            $("#fadeLoader").hide();
        }
    });
}

function OpenRename(deviceId)
{
    SaveInLocalStorage("currentDevice",deviceId);
    lightbox_RenameDevice();
}

function OpenOtpVerify()
{
    //SaveInLocalStorage("currentDevice",deviceId);
    lightbox_OtpVerify();
}


function userProfilePic(){
    
    var userName = LoadFromLocalStorage("userName");
    var browserId = LoadFromLocalStorage("deviceId");
    var sessionId = LoadFromLocalStorage("sessionId");
    
    $.ajax({
        
        url: baseUrl + "/openam/profile/getBasicDetails?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
        type:"GET",
        crossDomain : true,
        
        success: function(result,status,xhr) {
            console.log(result);
            var data = JSON.parse(result);
            
            if(data.details==undefined)
            {
                //alert("get user details error");
                return;
            }
            
            var name=data.details.User.firstName+ " " + data.details.User.lastName +"<br/><span>" + data.details.User.designation+ "</span>";
            var desig=data.details.User.designation;
            
            //$("#main-user-seal").src("src",data.ProfilePic);
            $("#name-desig").html(name);
            $.ajax({
                url: baseUrl + "/openam/profile/download/pic?username=" + userName +"&realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
                type:"GET",
                crossDomain : true,
                success: function(result,status,xhr) {
                    $("#main-user-seal img").attr("src","data:image/png;base64,"+result);
                }
            });

            
            
        //$("#designatn").html(desig);
        }
    });
}


// list Recent Activities

function listRecentActivities()
{
    var browserId = LoadFromLocalStorage("deviceId");
    var  userName = LoadFromLocalStorage("userName");
    var sessionId = LoadFromLocalStorage("sessionId");
    $.ajax({
        url:baseUrl + "/openam/SecurityView/listActivities?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId+ "&sessionId=" + sessionId,
        type:"GET",
        success: function(result,status,xhr) {
            $("#recentActivities").html("");    
            var data = JSON.parse(result);
            console.log("----"+data.status);
            console.log("----"+data.activityList);
            
            if(data.status=="success"){
                            
                var output = "<tr></tr>"
                        
                output+= "<tr>";
                output+= "<th>Activity</th> ";
                output+= "<th>Time</th> ";
                output+= "<th>Browser</th> ";
                output+= "<th>IP</th> ";
                
                console.log(output);
                output+= "</tr>";
                       
                var flag=false;
                $.each(data.activityList, function (index, val) {
                           
                    var userAgent = val["userAgent"];
                    var nVer = navigator.appVersion;
                    //                    var nAgt = navigator.userAgent;
                    var nAgt = val["userAgent"];
                    var browserName  = navigator.appName;
                    var fullVersion  = ''+parseFloat(navigator.appVersion); 
                    var majorVersion = parseInt(navigator.appVersion,10);
                    var nameOffset,verOffset,ix;

                    // In Opera 15+, the true version is after "OPR/" 
                    if ((verOffset=nAgt.indexOf("OPR/"))!=-1) {
                        browserName = "Opera";
                        fullVersion = nAgt.substring(verOffset+4);
                    }
                    // In older Opera, the true version is after "Opera" or after "Version"
                    else if ((verOffset=nAgt.indexOf("Opera"))!=-1) {
                        browserName = "Opera";
                        fullVersion = nAgt.substring(verOffset+6);
                        if ((verOffset=nAgt.indexOf("Version"))!=-1) 
                            fullVersion = nAgt.substring(verOffset+8);
                    }
                    // In MSIE, the true version is after "MSIE" in userAgent
                    else if ((verOffset=nAgt.indexOf("MSIE"))!=-1) {
                        browserName = "Microsoft Internet Explorer";
                        fullVersion = nAgt.substring(verOffset+5);
                    }
                    // In Chrome, the true version is after "Chrome" 
                    else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
                        browserName = "Chrome";
                        fullVersion = nAgt.substring(verOffset+7);
                    }
                    // In Safari, the true version is after "Safari" or after "Version" 
                    else if ((verOffset=nAgt.indexOf("Safari"))!=-1) {
                        browserName = "Safari";
                        fullVersion = nAgt.substring(verOffset+7);
                        if ((verOffset=nAgt.indexOf("Version"))!=-1) 
                            fullVersion = nAgt.substring(verOffset+8);
                    }
                    // In Firefox, the true version is after "Firefox" 
                    else if ((verOffset=nAgt.indexOf("Firefox"))!=-1) {
                        browserName = "Firefox";
                        fullVersion = nAgt.substring(verOffset+8);
                    }
                    // In most other browsers, "name/version" is at the end of userAgent 
                    else if ( (nameOffset=nAgt.lastIndexOf(' ')+1) < 
                        (verOffset=nAgt.lastIndexOf('/')) ) 
                        {
                        browserName = nAgt.substring(nameOffset,verOffset);
                        fullVersion = nAgt.substring(verOffset+1);
                        if (browserName.toLowerCase()==browserName.toUpperCase()) {
                            browserName = navigator.appName;
                        }
                    }
                    // trim the fullVersion string at semicolon/space if present
                    if ((ix=fullVersion.indexOf(";"))!=-1)
                        fullVersion=fullVersion.substring(0,ix);
                    if ((ix=fullVersion.indexOf(" "))!=-1)
                        fullVersion=fullVersion.substring(0,ix);

                    majorVersion = parseInt(''+fullVersion,10);
                    if (isNaN(majorVersion)) {
                        fullVersion  = ''+parseFloat(navigator.appVersion); 
                        majorVersion = parseInt(navigator.appVersion,10);
                    }
                    
                    
                           
                    if(val.name ==undefined)
                    {
                                        
                        val.name="(no name)";     
                    }
                    
                    flag=true;        
                                
                    output+= "<tr>";
                    output+= "<td>" + val.activity + "</td> ";
                    output+= "<td>"+ val._id +"</td> ";
                    output+= "<td>" + browserName + "</td> ";
                    output+= "<td>"+ val.ip +"</td> ";

                    //  console.log(output);
                    output+= "</tr>";
                    
                                
                });
                        
                if(!flag)
                {
                    $("#divRecentActivities").html("No Recent Activities Found").show();
                    //$("#btnRemoveAllDevices").hide();
                    
                    //alert("No Device Found");
                    return;
                }
                            
                        
               
                    
                $("#recentActivities").html(output);
                           
                
                $("#divRecentActivities").show("slow");
                    
                
                         
            }//if loop ends
            else
            {
                showErrorPopup(" No Devices are saved");   
                            
            }
            
            $("#loader").hide();
            $("#fadeLoader").hide();
            
        }      //success ends
          
    });        //ajax call ends                         

}


            
////list device name
//function listDevice(){
//    var browserId = LoadFromLocalStorage("deviceId");
//    var  userName = LoadFromLocalStorage("userName");
//    $.ajax({
//        url:baseUrl + "/openam/SecurityView/listDevices?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId,
//        type:"GET",
//        success: function(result,status,xhr) {
//            $("#listDevice").html("");    
//            var data = JSON.parse(result);
//            console.log("----"+data.status);
//                        
//            if(data.status=="success"){
//                            
//                var output = "<table class='table table-bordered display table' id='myTable'>";
//                        
//                output+= "<thead><tr>";
//                output+= "<th>Name of Device</th> ";
//                output+= "<th>Type</th> ";
//                output+= "<th>IP</th> ";
//                output+= "<th>Remove</th> ";
//                output+= "<th>Rename</th> ";
//                console.log(output);
//                output+= "</thead></tr><tbody>";
//                data.deviceList = [{
//                    name: "abc", 
//                    known:"Y" , 
//                    type :"windows",
//                    ip:"192.168.2.1"
//                },
//
//                {
//                    name: "abcd", 
//                    known:"Y" ,
//                    type :"windows7",
//                    ip:"192.168.2.2"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcr", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.4"
//                },
//
//                {
//                    name: "abcq", 
//                    known:"Y",
//                    type :"windows8",
//                    ip:"192.168.2.7"
//                },{
//                    name: "abcw", 
//                    known:"Y",
//                    type :"windows7",
//                    ip:"192.168.2.6"
//                }]
//                var flag=false;
//                $.each(data.deviceList, function (index, val) {
//                                
//                    if(val.name ==undefined)
//                    {
//                                        
//                        val.name="(no name)";     
//                    }
//                    if(val.known == "Y"){
//                        flag=true;        
//                                
//                        output+= "<tr>";
//                        output+= "<td>" + val.name + "</td> ";
//                        output+= "<td>" + val.type + "</td> ";
//                        output+= "<td>" + val.ip + "</td> ";
//                        output+= "<td><button class='btn bg-primary' id='removeDevice' onclick='removeDevice(\""+val._id+"\")'> Remove Link </button></td> ";
//                        output+= "<td><button class='btn bg-danger' onclick='OpenRename(\""+val._id+"\")'> Rename Link </button></td> ";
//                        //  console.log(output);
//                        output+= "</tr>";
//                    }
//                                
//                });
//                        
//                if(!flag)
//                {
//                    $("#divDevices").html("No Device Found").show();
//                    $("#btnRemoveAllDevices").hide();
//                    
//                    //alert("No Device Found");
//                    return;
//                }
//                            
//                        
//               
//                //                    output="<table id='myTable'>  <thead>  <tr>  <th>ENO</th>  <th>EMPName</th>  <th>Country</th>  <th>Salary</th>  </tr>  </thead>  <tbody>  ";
//                //                    output+="<tr>  <td>001</td>  <td>Anusha</td>  <td>India</td>  <td>10000</td>  </tr>  ";
//                //                    output+="<tr>  <td>001</td>  <td>usha</td>  <td>India</td>  <td>10000</td>  </tr>  ";
//                //                    output+="<tr>  <td>001</td>  <td>Anusha</td>  <td>India</td>  <td>10000</td>  </tr>  ";
//                //                    output+="<tr>  <td>001</td>  <td>Anusha</td>  <td>India</td>  <td>10000</td>  </tr>  ";
//                //                    output+="<tr>  <td>001</td>  <td>Anusha</td>  <td>India</td>  <td>10000</td>  </tr>  ";
//                //                    output+="<tr>  <td>001</td>  <td>Anusha</td>  <td>India</td>  <td>10000</td>  </tr>  ";
//                //          output+="</tbody> ";
//                //          
//          
//          
//          
//                $("#divDevicesList").html(output+"</tbody></table>");
//                $("#myTable").dataTable();
//                  
//                
//                  
//                   
//            
//                if(!removeDeviceActive)
//                {
//                    $("#divDevices").slideToggle("slow",function(){
//                        $(this).is(":visible") ? $("#deviceBtn").text("Hide All Devices") : $("#deviceBtn").text("Show All Devices");
//                            
//                    });
//                    
//                }
//                else
//                    removeDeviceActive=false;
//                         
//            }//if loop ends
//            else
//            {
//                showErrorPopup(" No Devices are saved");   
//                            
//            }
//            
//        }      //success ends
//          
//    });        //ajax call ends                         
//
//}// function list device endslist
            
//remove device from list



function listDevice(){
    var browserId = LoadFromLocalStorage("deviceId");
    var  userName = LoadFromLocalStorage("userName");
    var  sessionId = LoadFromLocalStorage("sessionId");
    
    $.ajax({
        url:baseUrl + "/openam/SecurityView/listDevices?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
        type:"GET",
        success: function(result,status,xhr) {
            console.log("--result of devices list"+result);
            $("#imgLoaderDevices").hide();
            $("#listDevice").html("");    
            var data = JSON.parse(result);
            console.log("----device list data        "+data);
                        
            if(data.status=="success"){
                            
                var output = "<table class='table table-bordered display table' id='myTable'>";
                        
                output+= "<thead><tr>";
                output+= "<th aria-sort='ascending'>Name</th> ";
                output+= "<th>Browser</th> ";
                output+= "<th>O/S</th> ";
                output+= "<th>IP</th> ";
                output+= "<th>Known</th> ";
                output+= "<th>Remove</th> ";
                output+= "<th>Rename</th> ";
                console.log(output);
                output+= "</thead></tr><tbody>";
                //                data.deviceList = [{
                //                    name: "abc", 
                //                    known:"Y" , 
                //                    type :"windows",
                //                    ip:"192.168.2.1"
                //                },
                //
                //                {
                //                    name: "abcd", 
                //                    known:"Y" ,
                //                    type :"windows7",
                //                    ip:"192.168.2.2"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"Y",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"Y",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"Y",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"Y",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"N",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"Y",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"Y",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"N",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"Y",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"Y",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"Y",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"Y",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcr", 
                //                    known:"N",
                //                    type :"windows8",
                //                    ip:"192.168.2.4"
                //                },
                //
                //                {
                //                    name: "abcq", 
                //                    known:"N",
                //                    type :"windows8",
                //                    ip:"192.168.2.7"
                //                },{
                //                    name: "abcw", 
                //                    known:"N",
                //                    type :"windows7",
                //                    ip:"192.168.2.6"
                //                }]
                var flag=false;
                $.each(data.deviceList, function (index, val) {   
                    console.log("user agent"+val["user-agent"]);
                    
                    var userAgent = val["user-agent"];
                    var nVer = navigator.appVersion;
                    //                    var nAgt = navigator.userAgent;
                    var nAgt = val["user-agent"];
                    var browserName  = navigator.appName;
                    var fullVersion  = ''+parseFloat(navigator.appVersion); 
                    var majorVersion = parseInt(navigator.appVersion,10);
                    var nameOffset,verOffset,ix;

                    // In Opera 15+, the true version is after "OPR/" 
                    if ((verOffset=nAgt.indexOf("OPR/"))!=-1) {
                        browserName = "Opera";
                        fullVersion = nAgt.substring(verOffset+4);
                    }
                    // In older Opera, the true version is after "Opera" or after "Version"
                    else if ((verOffset=nAgt.indexOf("Opera"))!=-1) {
                        browserName = "Opera";
                        fullVersion = nAgt.substring(verOffset+6);
                        if ((verOffset=nAgt.indexOf("Version"))!=-1) 
                            fullVersion = nAgt.substring(verOffset+8);
                    }
                    // In MSIE, the true version is after "MSIE" in userAgent
                    else if ((verOffset=nAgt.indexOf("MSIE"))!=-1) {
                        browserName = "Microsoft Internet Explorer";
                        fullVersion = nAgt.substring(verOffset+5);
                    }
                    // In Chrome, the true version is after "Chrome" 
                    else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
                        browserName = "Chrome";
                        fullVersion = nAgt.substring(verOffset+7);
                    }
                    // In Safari, the true version is after "Safari" or after "Version" 
                    else if ((verOffset=nAgt.indexOf("Safari"))!=-1) {
                        browserName = "Safari";
                        fullVersion = nAgt.substring(verOffset+7);
                        if ((verOffset=nAgt.indexOf("Version"))!=-1) 
                            fullVersion = nAgt.substring(verOffset+8);
                    }
                    // In Firefox, the true version is after "Firefox" 
                    else if ((verOffset=nAgt.indexOf("Firefox"))!=-1) {
                        browserName = "Firefox";
                        fullVersion = nAgt.substring(verOffset+8);
                    }
                    // In most other browsers, "name/version" is at the end of userAgent 
                    else if ( (nameOffset=nAgt.lastIndexOf(' ')+1) < 
                        (verOffset=nAgt.lastIndexOf('/')) ) 
                        {
                        browserName = nAgt.substring(nameOffset,verOffset);
                        fullVersion = nAgt.substring(verOffset+1);
                        if (browserName.toLowerCase()==browserName.toUpperCase()) {
                            browserName = navigator.appName;
                        }
                    }
                    // trim the fullVersion string at semicolon/space if present
                    if ((ix=fullVersion.indexOf(";"))!=-1)
                        fullVersion=fullVersion.substring(0,ix);
                    if ((ix=fullVersion.indexOf(" "))!=-1)
                        fullVersion=fullVersion.substring(0,ix);

                    majorVersion = parseInt(''+fullVersion,10);
                    if (isNaN(majorVersion)) {
                        fullVersion  = ''+parseFloat(navigator.appVersion); 
                        majorVersion = parseInt(navigator.appVersion,10);
                    }

                    var OSName="Unknown OS";
                    if (navigator.appVersion.indexOf("Win")!=-1) OSName="Windows";
                    if (navigator.appVersion.indexOf("Mac")!=-1) OSName="MacOS";
                    if (navigator.appVersion.indexOf("X11")!=-1) OSName="UNIX";
                    if (navigator.appVersion.indexOf("Linux")!=-1) OSName="Linux";
           
               
                    if(val.name ==undefined)
                    {
                                        
                        val.name="(no name)";     
                    }
                                     
                    flag=true;        
                                
                    output+= "<tr>";
                    
                    output+= "<td>" + val.name + "</td> ";
                    output+= "<td>" + browserName  + "</td> ";
                    output+= "<td>" + OSName  + "</td> ";
                    output+= "<td>" + val.ip + "</td> ";
                    output+= "<td id='knownDevice'>" + val.known + "</td> ";
                    
                    if(val.known=="N")
                        output+= "<td></td>";
                    else
                        output+= "<td><button class='btn btn-danger' id='removeDevice' onclick='removeDevice(\""+val._id+"\")'> Remove </button></td> ";    
                   
                   
                    
                    output+= "<td><button class='btn btn-primary ' onclick='OpenRename(\""+val._id+"\",\""+val.name+"\")'> Rename </button></td> ";
                    //  console.log(output);
                    output+= "</tr>";
                //     }
                     
                     
                });

                if(!flag)
                {
                    $("#divDevices").html("No Device Found").show();
                    $("#btnRemoveAllDevices").hide();
                    
                    //alert("No Device Found");
                    return;
                }
                            
          
          
                $("#divDevicesList").html(output+"</tbody></table>");
                $("#myTable td:contains('Y')").html("Yes");
                $("#myTable td:contains('N')").html("No");
                $("#myTable").dataTable({
                    "lengthMenu": [ 6 ],
                    "order": [[ 0, "desc" ]]
                    
                });
                  
               
                //                    "lengthMenu": [[7, 14, 21, -1], [7, 14, 21, "All"]]
                     
                
                
                  
                   
            
                if(!removeDeviceActive)
                {
                    $("#divDevices").slideToggle("slow",function(){
                        $(this).is(":visible") ? $("#deviceBtn").text("Hide All Devices") : $("#deviceBtn").text("Show All Devices");
                            
                    });
                    
                }
                else
                    removeDeviceActive=false;
                         
            }//if loop ends
            else
            {
                showErrorPopup(" No Devices are saved");   
                            
            }
            
            $("#loader").hide();
            $("#fadeLoader").hide();
            
        }      //success ends
          
    });        //ajax call ends                         

}// function list device ends


var removeDeviceActive=false;

function removeDevice(deviceId){
    var browserId = LoadFromLocalStorage("deviceId");
    removeDeviceActive=true;
    var  userName = LoadFromLocalStorage("userName");
    $.ajax({
        url:baseUrl + "/openam/SecurityView/removeDevice?deviceId="+deviceId+"&userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId,
        type:"GET",
        success: function(result,status,xhr) {
                       
            var data = JSON.parse(result);
            console.log("----"+data.status);
            if(data.status=="success"){
                showErrorPopup("Device Removed");
                listDevice();
            }
        }
    });
        
}
            
//remove all devices
function removeAllDevices(){
    var browserId = LoadFromLocalStorage("deviceId");
    var  userName = LoadFromLocalStorage("userName");
    $.ajax({
        url:baseUrl + "/openam/SecurityView/removeAllDevices?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId,
        type:"GET",
        success: function(result,status,xhr) {
            console.log("removeDevice"+result);
            var data = JSON.parse(result);
            console.log("----"+data.status);
            if(data.status=="success"){
                listDevice();
            }
        }
    });
}
            
function removeAllSessions(){
    
    var browserId = LoadFromLocalStorage("deviceId");
    var  userName = LoadFromLocalStorage("userName");
    var  sessionId = LoadFromLocalStorage("sessionId");
    
    $.ajax({
        url:baseUrl + "/openam/SecurityView/removeAllSession?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
        type:"GET",
        success: function(result,status,xhr) {
            console.log("removeAllSession"+result);
            var data = JSON.parse(result);
            console.log("----"+data.status);
            if(data.status=="success"){
                activeSession();
                alert("Only Current Session Active.");
            }
        }
    });
}
            
//rename deviceName
function renameDevice(deviceName){
    
    var browserId = LoadFromLocalStorage("deviceId");
    removeDeviceActive=true;
    var  userName = LoadFromLocalStorage("userName");
    
    var  newName = $("#newName").val();
    var  deviceId = LoadFromLocalStorage("currentDevice");
    var  sessionId = LoadFromLocalStorage("sessionId");
                
    $.ajax({
        url:baseUrl + "/openam/SecurityView/renameDevice?deviceName="+newName+"&userName=" + userName +"&deviceId=" + deviceId +"&realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
        type:"GET",
        success: function(result,status,xhr) {
                       
            var data = JSON.parse(result);
            console.log("----"+data.status);
            if(data.status=="success"){
                lightbox_close();
                listDevice();
                            
            }
        }
    });
            
}
            
//list active sessions 
function  activeSession(){
    
    var browserId = LoadFromLocalStorage("deviceId");
    var  userName = LoadFromLocalStorage("userName");
    var  sessionId = LoadFromLocalStorage("sessionId");
    
    $.ajax({
        url:baseUrl + "/openam/SecurityView/listActiveSessions?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
        type:"GET",
        success: function(result,status,xhr) {
            console.log(result);
            $("#listSession").html("");
            var data = JSON.parse(result);
            console.log("---- Active Session"       +data);
            //console.log(data.removeDevice);
            if(data.status=="success"){
                var output = "<tr></tr>"
                        
                output+= "<tr>";
                output+= "<th>IP</th> ";
                output+= "<th>Start Time</th> ";
                output+= "<th>User Agent</th> ";
                output+= "<th>Remove</th> ";
                            
                console.log(output);
                output+= "</tr>";
                        
                        
                //console.log(data.activeSessionList.length);
                        
                if(data.activeSessionList.length==0)
                {
                    $("#divSession").html("No Active Session Found").show();
                    $("#btnRemoveAllSessions").hide();
                    //alert("No Active Session Found");
                    return;
                }
                        
                $.each(data.activeSessionList, function (index, val) {
                           
                    output+= "<tr>";
                    output+= "<td>" + val.ip + "</td> ";
                    output+= "<td>" + val.sessionStartTime + "</td> ";
                    output+= "<td>" + val.userAgent + "</td> ";
                            
                    //                    output+= "<td><a href='#' id='removeSession' class='btn btn bg-danger' onclick='removeSession(\""+val.sessionId +"\")'> Remove Session </a></td> ";
                    
                    if(val.current==0)
                        output+= "<td><button id='removeSession' class='btn btn bg-danger' onclick='removeSession(\""+val._id +"\")'> Remove Session </button></td> ";
                    else
                        output+= "<td></td>";
                                
                    //  console.log(output);
                    output+= "</tr>";
                                
                                
                });
                        
                
                    
                $("#listSession").html(output);
                //$("#listDevice").slideToggle(200);
                            
                if(!removeSessionActive)
                {
                    $("#divSession").slideToggle("slow",function(){
                        $(this).is(":visible") ? $("#activeSessionBtn").text("Hide All Session") : $("#activeSessionBtn").text("Show Active Sessions");
                            
                    });
                
                }
                else
                {
                    removeSessionActive=false;    
                }
                            
                            
            }//if loop ends
            else
            {
                showErrorPopup(" No session");   
                            
            }
            
            $("#loader").hide();
            $("#fadeLoader").hide();
            
        }      //success ends
          
    });        //ajax call ends                         

}// function list session ends
           
           
//remove session
var removeSessionActive=false;
function removeSession(sessionId){
    
    var browserId = LoadFromLocalStorage("deviceId");
    removeSessionActive=true;
    var  userName = LoadFromLocalStorage("userName");
    var  sessionId = LoadFromLocalStorage("sessionId");
    
    $.ajax({
        url:baseUrl + "/openam/SecurityView/removeSession?userName=" + userName +"&sessionId="+sessionId+"&realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
        type:"GET",
        success: function(result,status,xhr) {
                       
            var data = JSON.parse(result);
            console.log("----"+data.status);
            if(data.status=="success"){
                            
                activeSession();
                alert("Only Current Session Active.");
            }
        }
    });
}

var phoneCodesArray=new Array();
var emailCodesArray=new Array();
var CodesArray=new Array();
         
function showPhoneBackupCode()
{
    var browserId = LoadFromLocalStorage("deviceId");
    CodesArray.length=0;
    var  userName = LoadFromLocalStorage("userName");
    var  sessionId = LoadFromLocalStorage("sessionId");
    $("#imgLoader").show();
    //$('.ajax-loader').css("visibility", "visible");
    $.ajax({
        url:baseUrl + "/openam/SecurityView/getBackupCode?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId +"&sessionId=" + sessionId,
        type:"GET",
        success: function(result,status,xhr) {
            //        $('.ajax-loader').css("visibility", "hidden");
            $("#imgLoader").hide();
            var data = JSON.parse(result);
            showBackupCodesTable(data);
        }      //success ends
          
    });        //ajax call ends     
             
}
     
     
function showBackupCodesTable(data){
    if(data.status=="success"){
                        
        var usedCodes = "";
        //                usedCodes = "<tr></tr>"
        //                usedCodes+= "<tr>";
        //                usedCodes+= "<th>Used Codes</th> ";
        //                usedCodes+= "</tr>";
                
        var UnUsed  ="";
        //        UnUsed  = "<tr></tr>"
        //        UnUsed+= "<tr>";
        //        UnUsed+= "<th>UnUsed Codes</th> ";
        //                
        //        UnUsed+= "</tr>";   

        if(data.codes.length!=0)
        {
            // change text of generate button to regenerate codes
    
            $("#generateCodesBtn").text('Regenerate Codes');
            $("#downloadBackupCodeButton").show();
        }

        //        data.codes=[{
        //            "123456" : "Y",
        //            "163456" : "N",
        //            "125456" : "Y",
        //            "123456" : "Y",
        //            "123436" : "Y",
        //            "123356" : "N",
        //            "123756" : "Y",
        //            "123456" : "Y",
        //            "123556" : "Y",
        //            "123456" : "Y"
        //        }];
            
        //        for(var obj in data.codes){
        //            if(data.codes.hasOwnProperty(obj)){
        //                for(var prop in data.codes[obj]){
        //                    if(data.codes[obj].hasOwnProperty(prop)){
        //                        
        //                        
        //                        var status = data.codes[obj][prop]; //y/n
        //                    
        //                        var code = prop; //code
        //                  
        //                        if(status == 'Y'){
        //                       
        //                            usedCodes+= "<tr>";
        //                            usedCodes+= "<td>" + code + "</td> ";
        //                            usedCodes+= "</tr>"
        //                            CodesArray.push(code);
        //                        }
        //                        else{
        //                        
        //                            UnUsed+= "<tr>";
        //                            UnUsed+= "<td>" + code + "</td> ";
        //                            UnUsed+= "</tr>";
        //                            CodesArray.push(code);
        //                        }
        //                            
        //                        
        //                        
        //                    //alert(prop + ':' + data.codes[obj][prop]);
        //                    }
        //                }
        //            }
        //        }
        //            
            
        $.each(data.codes, function (index, val) {
            
            
            
            //            alert(val);
            
            //            if(active=="Phone")
            //                           phoneCodesArray.push(val);
            //            else
            //                emailCodesArray.push(val);
            
            
            
            
            
            
            
            
            var parts_Code = val.split(":");
            var status = parts_Code.pop(); //y/n
                    
            var code = parts_Code.join(':'); //code
                  
            if(status == 'Y'){
                       
                usedCodes+= "<tr>";
                usedCodes+= "<td>" + code + "</td> ";
                usedCodes+= "</tr>"
                CodesArray.push(code);
            }
            else{
                        
                UnUsed+= "<tr>";
                UnUsed+= "<td>" + code + "</td> ";
                UnUsed+= "</tr>";
                CodesArray.push(code);
            }
        });
                        
        $("#usedBackUpCode").html("");
        $("#UnUsedBackUpCode").html("");
    



        if(usedCodes != ""){
            $("#usedBackUpCode").html("<table><tr><th>Used Codes</th></tr>"+usedCodes+"</table>");
        }
        if(UnUsed!="")
            $("#UnUsedBackUpCode").html("<table><tr><th>Unused Codes</th></tr>"+UnUsed+"</table>");
             
        $("#divPhoneCodes").show();
        // 10th feb ...bhawna dixit
        $("#divNoCodes").hide(); 
                   
    }//if loop ends
    else
    {
        // 10th feb ...bhawna dixit
        $("#divNoCodes").html("No Backup Codes Found").show();
    }
}
     
//phone backup codes
function generateBackupCode(){
    
    var browserId = LoadFromLocalStorage("deviceId");
    phoneCodesArray.length=0;
    CodesArray.length=0;
    var  userName = LoadFromLocalStorage("userName");
    var  sessionId = LoadFromLocalStorage("sessionId");
    
    $.ajax({
        url:baseUrl + "/openam/SecurityView/generateBackupCode?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId +"&sessionId=" + sessionId,
        type:"GET",
        success: function(result,status,xhr) {
            var data = JSON.parse(result);
            showBackupCodesTable(data);
        }     
    });       
}
    
    
//getting email backup codes from database
    
//function showEmailBackupCode()
//{
//    var browserId = LoadFromLocalStorage("deviceId");
//    emailCodesArray.length=0;
//    var  userName = LoadFromLocalStorage("userName");
//    
//    $.ajax({
//        url:baseUrl + "/openam/SecurityView/getBackupCodePhone?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId,
//        type:"GET",
//        success: function(result,status,xhr) {
//            var data = JSON.parse(result);
//            showBackupCodesTable(data, "Email");
//        }      //success ends
//          
//    });        //ajax call ends     
//             
//}
  
    
// generate email backup codes
           
//function generateEmailBackupCode(){
//    
//    var browserId = LoadFromLocalStorage("deviceId");
//    emailCodesArray.length=0;
//    var  userName = LoadFromLocalStorage("userName");
//    $.ajax({
//        url:baseUrl + "/openam/SecurityView/generateEmailBackupCode?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId,
//        type:"GET",
//        success: function(result,status,xhr) {
//            var data = JSON.parse(result);
//            showBackupCodesTable(data, "Email");
//        }      //success ends
//    });        //ajax call ends     
//}
        
        
function initiateCheckBox()
{
    var browserId = LoadFromLocalStorage("deviceId"); 
    var sessionId = LoadFromLocalStorage("sessionId"); 
    // check box change - Two Step on Phone
    
    
    
    $("#chkTwoStepPhone").change(function(){
        
        $("#divChkLoading").show();
        
        $.ajax({
            url:baseUrl + "/openam/securityUpdate/enableTwoStepOnPhone?realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
            type:"POST",
            data:{
                userName: LoadFromLocalStorage("userName") ,
                enableTwoStepOnPhone : $(this).is(":checked")
            },
            success:function(result){
                
                $("#divChkLoading").hide();
                $("#loader").hide();
                $("#fadeLoader").hide();
                
                var data = JSON.parse(result);
                
                if(data.status!="success")
                    showErrorPopup("Error in enableTwoStepOnPhone");   
            }
        });
    }); 
                
                
                
    //check box change - two step on email
              
    $("#chkTwoStepEmail").change(function(){
                 
        $.ajax({
            url:baseUrl + "/openam/securityUpdate/enableTwoStepOnEmail?realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
            type:"POST",
            data:{
                userName: LoadFromLocalStorage("userName") ,
                enableTwoStepOnEmail : $(this).is(":checked")
            
            },
            success:function(result){
                $("#loader").hide();
                $("#fadeLoader").hide();
                
                var data = JSON.parse(result);
                if(data.status!="success")
                    showErrorPopup("Error in enableTwoStepOnEmail");   
            }
        });
    }); 
    
    $("#chkTwoStepPhoneEmail").change(function(){
                 
        $.ajax({
            url:baseUrl + "/openam/securityUpdate/enableTwoStepUsingEither?realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
            type:"POST",
            data:{
                userName: LoadFromLocalStorage("userName") ,
                enableTwoStepUsingEither : $(this).is(":checked")
            
            },
            success:function(result){
                $("#loader").hide();
                $("#fadeLoader").hide();
                
                var data = JSON.parse(result);
                if(data.status!="success")
                    showErrorPopup("Error in enableTwoStepOnEmail");   
            }
        });
    }); 
                
                
    //check OTP on phone 
               
    $("#chkOTPPhone").change(function(){

        $.ajax({
            url:baseUrl + "/openam/securityUpdate/enableOTPOnPhone?realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
            type:"POST",
            data:{
                userName: LoadFromLocalStorage("userName") ,
                enableOTPOnPhone : $(this).is(":checked")
            
            },
            success:function(result){
                var data = JSON.parse(result);
                
                if(data.status!="success")
                    showErrorPopup("Error in enableOTPOnPhone");   
            }
        });
    }); 



    // check otp on email
             
    $("#chkOTPEmail").change(function(){

        $.ajax({
            url:baseUrl + "/openam/securityUpdate/enableOTPOnEmail?realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
            type:"POST",
            data:{
                userName: LoadFromLocalStorage("userName") ,
                enableOTPOnEmail : $(this).is(":checked")
            
            },
            success:function(result, status, xhr){
                var data = JSON.parse(result);
                
                if(data.status!="success")
                    showErrorPopup("Error in enableOTPOnEmail");   
            }
        });
    });
    
    
    
    
    $("#chkOTPChangePassword").change(function(){

        $.ajax({
            url:baseUrl + "/openam/securityUpdate/enableChangePasswordOtp?realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
            type:"POST",
            data:{
                userName: LoadFromLocalStorage("userName") ,
                enableChangePasswordOtp : $(this).is(":checked")
            
            },
            success:function(result, status, xhr){
                var data = JSON.parse(result);
                
                if(data.status!="success")
                    showErrorPopup("Error in enableChangePasswordOtp");   
            }
        });
    });
    
   
    
    
    $("#chkOTPProfileUpdation").change(function(){

        $.ajax({
            url:baseUrl + "/openam/securityUpdate/enableProfileUpdationOtp?realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
            type:"POST",
            data:{
                userName: LoadFromLocalStorage("userName") ,
                enableProfileUpdationOtp : $(this).is(":checked")
            
            },
            success:function(result, status, xhr){
                var data = JSON.parse(result);
                
                if(data.status!="success")
                    showErrorPopup("Error in enableProfileUpdationOtp");   
            }
        });
    });
    
    
    //check box change - Login Alert
    
    $("#chkLoginAlert").change(function(){

        $.ajax({
            url:baseUrl + "/openam/securityUpdate/enableLoginAlert?realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
            type:"POST",
            data:{
                userName: LoadFromLocalStorage("userName") ,
                enableLoginAlert : $(this).is(":checked")
            
            },
            success:function(result, status, xhr){
                var data = JSON.parse(result);
                
                if(data.status!="success")
                    showErrorPopup("Error in Login Alert Change");   
            }
        });
    });
    
    
    //check box change - Profile Updation Alert
    
    $("#chkProfileUpdationAlert").change(function(){

        $.ajax({
            url:baseUrl + "/openam/securityUpdate/enableProfileUpdationAlert?realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
            type:"POST",
            data:{
                userName: LoadFromLocalStorage("userName") ,
                enableProfileUpdationAlert : $(this).is(":checked")
            
            },
            success:function(result, status, xhr){
                var data = JSON.parse(result);
                
                if(data.status!="success")
                    showErrorPopup("Error in Login Alert Change");   
            }
        });
    });
    
    
    //check box change - Hack Alert
    
    $("#chkHackAlert").change(function(){

        $.ajax({
            url:baseUrl + "/openam/securityUpdate/enableHackAlert?realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
            type:"POST",
            data:{
                userName: LoadFromLocalStorage("userName") ,
                enableHackAlert : $(this).is(":checked")
            
            },
            success:function(result, status, xhr){
                var data = JSON.parse(result);
                
                if(data.status!="success")
                    showErrorPopup("Error in Login Alert Change");   
            }
        });
    });
    
    
    //check box change - Previous Password Alert
    
    $("#chkPrevPassAlert").change(function(){

        $.ajax({
            url:baseUrl + "/openam/securityUpdate/enablePrevPasswordAlert?realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
            type:"POST",
            data:{
                userName: LoadFromLocalStorage("userName") ,
                enablePrevPasswordAlert : $(this).is(":checked")
            
            },
            success:function(result, status, xhr){
                var data = JSON.parse(result);
                
                if(data.status!="success")
                    showErrorPopup("Error in Login Alert Change");   
            }
        });
    });
    
    
}


function changeSeal()
{
    var browserId = LoadFromLocalStorage("deviceId");
    var sessionId = LoadFromLocalStorage("sessionId");
    var sealImage=$('input[name="seal"]:checked').next().attr("src");
    
    if(sealImage==undefined && $("#txtSeal").val()=="")
    {
        showErrorPopup("Please select Seal Image or Type Seal Text");
        return;
    }
    
    
    // if radio button is selected and text seal is entered
    if(sealImage!=undefined && $("#txtSeal").val()!="")
    {
        showErrorPopup("Please select Seal Image or Type Seal Text");
        return;
    }
    
    if(!validateIndexOfString("txtSeal", "'/' not allowed in Seal Text", "/"))
        return; 
    
    var sealValue="";
    
    if($("#txtSeal").val()=="")
    {
        var value= $('input[name="seal"]:checked').next().attr("src");
        
        sealValue= value;
    }
    else
    {
        sealValue= $("#txtSeal").val();
    }
    
    $.ajax({
        url:baseUrl + "/openam/securityUpdate/changeSeal?realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
        type:"POST",
        data:{
            userName: LoadFromLocalStorage("userName") ,
            changeSeal : sealValue
        },
        success:function(result){
            $("#loader").hide();
            $("#fadeLoader").hide();
            alert('hide');
            var data = JSON.parse(result);
                
            if(data.status!="success")
                showErrorPopup("Error in changing Seal");   
            else
            {
                showErrorPopup("Seal changed Successfully");
                $("#txtSeal").val("");
                $('input:radio').removeAttr('checked');

                $('#divSeal').slideUp(1500);
                loadSeal();
                
            }
        }
    });
    
    
   
    
}


function loadSeal()
{
    var browserId = LoadFromLocalStorage("deviceId");
    var userName = LoadFromLocalStorage("userName");
    var sessionId = LoadFromLocalStorage("sessionId");
    
    $.ajax({
        url:baseUrl + "/openam/SecurityView/getSeal?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
        type:"GET",
        success: function(result,status,xhr) {
            console.log(result);
            var data = JSON.parse(result);
            console.log("----"+data.status);
            //console.log(data.removeDevice);
            if(data.status=="success"){
                
                if(data.seal==""){
                    // show no seal image
                    $("#sealImage").attr('src',baseUrl + "/NKN/images/noSeal.svg");
                    $("#sealImage").show();   
    
                }else{
                    
                    if(data.seal.indexOf("/") != -1){
                        $("#sealImage").attr('src',data.seal);
                        $("#sealImage").show();   
                    }
                    else
                    {
                        //$("#imgLoad").hide();   
                        $("#sealImage").hide();   
                        $(".divSeal").html(data.seal);   
                        $(".divSeal").show();
                    }
                }
          
            }//if loop ends
            else
            {
                showErrorPopup("Error in loading seal");   
                            
            }
            
            $("#loader").hide();
            $("#fadeLoader").hide();
            
        }      //success ends
          
    });        //ajax call ends     
}



function logout()
{

    var browserId = LoadFromLocalStorage("deviceId");
    var userName = LoadFromLocalStorage("userName");
    var sessionId = LoadFromLocalStorage("sessionId");
    var tokenId = getCookie("tokenId");
    console.log(tokenId);
    $.ajax({//SaveInLocalStorage("tokenId", data.tokenID);
        url:baseUrl + "/openam/login/logout?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId + "&tokenID=" + tokenId,
        type:"GET",
        success: function(result,status,xhr) {
            console.log(result);
            //            return;
            
            var data = JSON.parse(result);
            console.log("----"+data.status);
            //console.log(data.removeDevice);
            if(data.status=="success"){
                //showErrorPopup("Successfully Logged out");
                localStorage.clear();
                
                SaveInLocalStorage("deviceId", browserId);
                
                var serviceCookie= getCookie("serviceName");
                
                deleteAllCookies();
                //deleteCookies();
                
                
                //                document.cookie = "serviceName=" +serviceCookie + "; expires=Thu, 18 Dec 2020 12:00:00 UTC; path=/" ;
                location.href="index.html";

            }//if loop ends
            else{
                
                console.log("error in sesion ");
                 
                showErrorPopup("Error"); 
           
            }
            
        }      //success ends
          
    }); 
}

function downloadphoneBackupCode()
{
    downloadArray(CodesArray, "BackUpCodes.txt");
}

//function downloadEmailBackupCode()
//{
//    downloadArray(emailCodesArray, "emailBackUpCodes.txt");
//}


function downloadArray(arr, fileName)
{
    var codesStr="";
    
    $.each(arr,function(i,val)
    {
        codesStr+=val + ", ";
    });  

    var element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(codesStr));
    element.setAttribute('download', fileName);

    element.style.display = 'none';
    var txtFile = document.body.appendChild(element);

    element.click();
}

function onLoadGetDeviceId(isWelcome){
    var deviceId= LoadFromLocalStorage("deviceId");
    if(deviceId==undefined || deviceId=="" ||deviceId==null)
    {
        console.log("getting device id ");    
        
        $.ajax({
            url:baseUrl + "/openam/login/getDeviceId",
            type:"GET",
            success:function(result){
                
                console.log(result);
                var data = JSON.parse(result);
                
                console.log(data.status);
                
                if(data.status=="success")
                {
                    SaveInLocalStorage("deviceId",data.deviceId);
                    
                    if(!isWelcome)
                        location.href="index.html";
                }
                
                else
                {
                    showErrorPopup("Error!!!!!"); 
                }
            }
        });
    
    }
}

function checkLocalStorage()
{
    if(window.localStorage!==undefined){
        $("#storageChk").hide();
    }else{
        $("#storageChk").show();
    }
}


function FillForm()
{
    
    var browserId = LoadFromLocalStorage("deviceId");
    var userName = LoadFromLocalStorage("userName");
    var sessionId = LoadFromLocalStorage("sessionId");
    
    $.ajax({
        url:baseUrl + "/openam/profile/getBasicDetails?userName=" + userName +"&realm=users&service=Accounts&browserID=" + browserId + "&sessionId=" + sessionId,
        type:"GET",
        success: function(result,status,xhr) {
            
            //hiding loading image
            
            $("#loader").hide();
            $("#fadeLoader").hide();
            
            var data = JSON.parse(result);
            console.log(data);
            console.log("----"+data.status);
            //console.log(data.removeDevice);
            if(data.status=="success"){
                
                var details=data.details.User;
                
                $("#firstName").val(details.firstName);
                $("#lastName").val(details.lastName);
                $("#phonePrimary").val(details.mobileNo);
                $("#phoneSecondary").val(details.mobsecondary);
                $("#emailPrimary").val(details.email);
                $("#emailSecondary").val(details.emailsecondary);
                $("#dob").val(details.dateOfBirth);
                $("#graduationDate").val(details.graduationDate);
                $("#gender").val(details.gender);
                $("#profileImg").val(details.picOff);
                $("#empId").val(details.empid);
                
                if(details.seal.indexOf("/") != -1){
                    
                    $(".sealborder").each(function()
                    {
                        
                        if($("img",this).attr("src")==details.seal)
                        {
                            $("input[type=radio]",$(this)).attr("checked", "checked");
                        }
                    
                    });
                    
                    $("#sealImage").attr('src',details.seal);
                    $("#sealImage").show();   
                }
                else
                {
                    $("#txtSeal").html(details.seal);   
                }
                
                //                var desigObj=JSON.parse( details.designationName);
                //                console.log(desigObj);
                //                
                //                console.log(desigObj.NknDesignation.departmentname);
                //                console.log(desigObj.NknDesignation.name);
                //                
                //                $('#department option').filter(function () {
                //                    if($(this).text() === desigObj.NknDesignation.departmentname )
                //                        $(this).attr('selected', 'selected');
                //                });

                $('#department').val(details.departmentId);
                
                loadDesignation($("#department"));
                
                
                setTimeout(function(){
                    
                    $('#designation').val(details.designationId);
                    
                //                    $('#designation option').filter(function () {
                //                        if($(this).text() === desigObj.NknDesignation.name )
                //                            $(this).attr('selected', 'selected');
                //                    });
                },1000);
            
                
            //                if(details.txtSeal.indexOf("/")==-1) //seal is a text value
            //                    $("#txtSeal").val(details.Seal);
            //                else
            //                {
            //                    var selectedradio= $("img[src='"+details.Seal+"']").prev();
            //                    $(selectedradio).attr("checked","checked");
            //                }
            }//if loop ends
            else
            {
                showErrorPopup("Error");   
                            
            }
            
        }      //success ends
          
    }); 
}

window.onbeforeunload = function () {
    logoutOnBrowserClose();
};

function logoutOnBrowserClose()
{
//    alert(LoadFromLocalStorage("staySignedIn"));
//    var staySignedIn=LoadFromLocalStorage("staySignedIn");
//    
//    if(!staySignedIn)
//    {
//        logout();
//    }
}


function getCookie( name ) {    

    var start = document.cookie.indexOf( name + "=" );

    var len = start + name.length + 1;

    if ( ( !start ) && ( name != document.cookie.substring( 0, name.length ) ) ) {

        return null;

    }

    if ( start == -1 ) return null;

    var end = document.cookie.indexOf( ';', len );

    if ( end == -1 ) end = document.cookie.length;

    return unescape( document.cookie.substring( len, end ) );

}



function checkOnLoadParam(isLoggedInPage)
{
    
    
 
    //alert("checkOnLoadParam");
    var deviceId = LoadFromLocalStorage("deviceId");
    var sessionId = LoadFromLocalStorage("sessionId");
    //var tokenId = LoadFromLocalStorage("tokenId");
    
    var tokenIdLocal = LoadFromLocalStorage("tokenId");
    
    var tokenId = getCookie("tokenId");
    //alert("token compare "+ tokenIdLocal +"\n"+tokenId);
    
    console.log("checkOnLoadParam method called");
    console.log("deviceId :"+deviceId);
    console.log("sessionId :"+sessionId);
    var serviceName = $("#service").val();

  
    if(deviceId==undefined || deviceId=="" ||deviceId==null || deviceId == "undefined" || sessionId==undefined || sessionId=="" ||sessionId==null||sessionId=="undefined" )
    {

        //  alert("inside if condition");
        if(isLoggedInPage)
        {
            //   alert("logout called");
            logout(true);   
        }
        else
        {
            
            // alert("delete all cookies called");  
            deleteAllCookies();
            localStorage.clear();
            
            deviceId=guid();
            SaveInLocalStorage("deviceId",deviceId);
            
            sessionId=guid();
            SaveInLocalStorage("sessionId",sessionId); 
            //
            
            location.href = baseUrl + "/index.jsp?service="+serviceName;
        }
        
    }
    //alert("tokenid in checkOnloadParam"+tokenId);
    
    
    if(tokenId!=undefined && tokenId!="" && tokenId!=null)
    {
        //alert("inside tokenid check");
        //alert("isLoggedInPage"+isLoggedInPage);
        var query = getQueryParams("action")[0];
        if(query=="update")
        {
            isLoggedInPage = true;
        }
        else
        {
            if(isLoggedInPage==undefined)
            {
                isLoggedInPage = false;
            }
        }
        
        //if(isLoggedInPage)  // after login pages
        validateToken(isLoggedInPage);
    }
            
    
    
//    if(!isLoggedInPage)
//    {
//        //        var tokenId = LoadFromLocalStorage("tokenId");
//        //           alert("tokenId: "+tokenId); 
//        //            if(tokenId!=undefined && tokenId!="" &&tokenId!=null && tokenId != "undefined")
//        //       
//        //alert("validateToken");
//        alert("validateToken called from checkonloadparam");
//        validateToken(isLoggedInPage);
//    }
            
    
}


function validateToken(isLoggedInPage){
    
    var serviceName =  $("#service").val();
    var query = getQueryParams("action")[0];
    console.log("query: "+query);
    if((query!="update")){
        //alert("inside update")
        var tokenId = getCookie("tokenId");
        
        //alert(tokenId);
        //token null--logout all
    
        
    
        //alert(serviceName);
        //  alert("tokenId: "+tokenId);
        
        if(tokenId == undefined || tokenId=="" || tokenId == null ){
           // alert("Inside IF TokenID");
        //logoutAll
        }
        else{
            if(serviceName == undefined || serviceName=="" || serviceName == null ){
                serviceName = "Accounts";
            }
            //alert(serviceName);
            $.ajax({
                url: baseUrl+ "/openam/login/validateToken?tokenId=" + tokenId+"&service="+serviceName,
                type:"GET",
                success: function(data,status,xhr) {
                
          
         
                    var result = JSON.parse(data);
                    console.log(result);
                    if(status == "success"){ //token valid and page is "before login page"
                        //alert("result.pageURL: "+result.pageURL)  
                        //location.href = baseUrl + "/NKN/home.html";
                    
                    
                        if(result.pageURL == "home"){
                            if(!isLoggedInPage)
                                location.href = baseUrl + "/NKN/home.html";
                        }
                        else {
                            location.href = result.pageURL;
                        }
                    
                    }   
                    else if(status == "failure") {
                        //logoutAll 
                        eraseCookie("tokenId");
                        location.href = baseUrl + "/NKN/index.html?service="+serviceName;
                    }     
                            
                }
            });
        }
    }
    else
    {
            
        var URL =  "http://accounts.staging.nkn.in:8080/Accounts/ClientManagement?service=" + serviceName;
        $.ajax({
            url: URL, 
            type:"GET",
            success: function(data,status,xhr) {
                
          
         
                var result = JSON.parse(data);
                console.log(result);
            //                    if(status == "success"){ //token valid and page is "before login page"
            //                        alert("result.pageURL: "+result.pageURL)  
            //                        //location.href = baseUrl + "/NKN/home.html";
            //                    
            //                    
            //                        if(result.pageURL == "home"){
            //                            if(!isLoggedInPage)
            //                                location.href = baseUrl + "/NKN/home.html";
            //                        }
            //                        else {
            //                            location.href = result.pageURL;
            //                        }
            //                    
            //                    }   
            //                    else if(status == "failure") {
            //                        //logoutAll 
            //                        eraseCookie("tokenId");
            //                        location.href = baseUrl + "/NKN/index.html?service="+serviceName;
            //                    }     
            //                            
            //                }
            }    
        });
    }
}




//function checkOnLoadParam(isLoggedInPage)
//{
//    
//   
//    var deviceId = LoadFromLocalStorage("deviceId");
//    var sessionId = LoadFromLocalStorage("sessionId");
//   
//  
//  
//    if(deviceId==undefined || deviceId=="" ||deviceId==null || deviceId == "undefined" || sessionId==undefined || sessionId=="" ||sessionId==null||sessionId=="undefined")
//   
//    {
//        
//        if(isLoggedInPage)
//        {
//          
//            logout(true);
//            //logoutall
//        }
//        else
//        {
//            deleteAllCookies();
//            location.href = baseUrl + "/index.jsp";
//        }
//        
//    }
//    else
//    {
//        validateToken(isLoggedInPage); // get true for valid token id 
//   
//    }
//
//}
function guid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
        .toString(16)
        .substring(1);
    }
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
    s4() + '-' + s4() + s4() + s4();
}

function openChangePassword()
{
    loadingImg();
    var browserId = LoadFromLocalStorage("deviceId");
    // getting setting if change password otp is enabled or not
    var  userName = LoadFromLocalStorage("userName");
    
    var sessionId = LoadFromLocalStorage("sessionId");
                    
    $.ajax({
        url:baseUrl + "/openam/SecurityView/checkAndSendCode?userName=" + userName +"&realm=users&service=Accounts&settingName=" + "changePassOtp" + "&browserID=" + browserId+"&sessionId=" + sessionId,
        type:"POST",
        success: function(result,status,xhr) {
                      
            var data = JSON.parse(result);
            console.log("----"+data.status);
         
            if(data.status=="success"){
                alert("OTP sent")   
            }
            else
                alert("Error in sending OTP....Please click resend..");
            
            
            $("#loader").hide();
            $("#fadeLoader").hide();   
            
        }      //success ends
          
    });        //ajax call ends                         



//SaveInLocalStorage("currentDevice",deviceId);
//lightbox_ChangePassword();
}

function changePassword(){
    
    if(!validateBlankField("currentPassword", "Current Password"))
        return;
    
    if(!validateBlankField("newPassword", "New Password"))
        return;
                    
    if(!validateBlankField("confirmNewPassword", "Confirm New Password"))
        return;
    
    if($("#newPassword").val()!=$("#confirmNewPassword").val())
    {
        alert("new password and confirm new password shounld be same");
        return;
    }
    
    if(!validateBlankField("changePassOtp", "OTP"))
        return;
    
    
    var browserId = LoadFromLocalStorage("deviceId");
    var sessionId = LoadFromLocalStorage("sessionId");
    
    $.ajax({
        url:baseUrl + "/openam/profile/changePassword?realm=users&service=Accounts&username=" + LoadFromLocalStorage("userName")+"&browserID=" + browserId+"&sessionId=" + sessionId,
        type:"POST",
        data:{
            currentPassword : $("#currentPassword").val(),
            passPrefix : $("#newPassword").val(),
            passChangeOTP : $("#changePassOtp").val()==""?"none":$("#changePassOtp").val()
        },
        success:function(result){
            var data = JSON.parse(result);
            
            if(data.status=="success")
            {
                alert("Password changed successfully");
                location.href = "home.html";
            }
            else
            {
                if(data.changePassOtp<3)
                {
                    showErrorPopup("Incorrect OTP. OTP attempts : " + data.changePassOtp+ ". Left : " + (3-data.changePassOtp));
                    $("#changePassOtp").val("");
                }
                else if(data.changePassOtp==3)
                {
                    alert("OTP Attempts Exceeded. Try Again");
                    location.href = "home.html";
                }
                else
                {
                    if(data.field=="0")
                        showErrorPopup("Incorrect Current Password");    
                    else if(data.field=="1")
                        showErrorPopup("Current Password and New Password can not be same");    
                    else
                        showErrorPopup("You can not set any of your old password as new password");    
                    return;
                }
            }
                
        //$("#newPassword").val("");
        //$("#confirmNewPassword").val("");
        //$("#currentPassword").val("");
            
                
            
        //lightbox_close();
        }
    });
}
//function userProfilePic(){
////    alert("in function");
////    var browserId = LoadFromLocalStorage("deviceId");
////    
////    var userName = LoadFromLocalStorage("userName");
////    $("#name-desig").html("userName");
//////   
////    $.ajax({
////        url:baseUrl + "/openam/profile/create?realm=users&service="+service+"&username=" + userName+"&browserID=" + browserId,
////        type:"POST",
////       
////        
////        success:function(result){
////            console.log(result);
////            
////           
////        }
////    })
//}


