/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.validation;

/**
 *
 * @author mkumar1
 */
public class LoginValidation extends ValidationFramework {

    public String loginUserValidation(String email) {
        String msg1 = validateLoginID(email);
        return finalMessage(msg1);
    }
     public String loginPasswordValidation(String email, String password) {
        String msg1 = validatePassword(password);
        return finalMessage(msg1);
    }
//     public String loginSealValidation(String Seal) {
//        String msg1 = validatePassword(Seal);
//        return finalMessage(msg1);
//    }

    private String validateLoginID(String email) {
        String msg1 = isEmail(email, "Email Id");
        String msg2 = lengthGTE(email, 1, "Email Id");
        String msg3 = lengthLTE(email, 50, "Email Id");
        return finalMessage(msg1, msg2, msg3);
    }

    private String validatePassword(String password) {
        String msg1 = hasWhiteSpace(password, "Password");
        String msg2 = lengthGTE(password, 6, "Password");
        String msg3 = lengthLTE(password, 50, "Password");
        return finalMessage(msg1, msg2, msg3);
    }
//     private String validateSeal(String seal) {
//        String msg1 = isPosix(seal, "Seal"); 
//        return finalMessage(msg1);
//    }
     
     
//     public static void main(String[] args) {
//      String s=   new LoginValidation().validateSeal("sbfdjkghd///////");
//         
//         System.out.println("validatev " +s);
//    }
}
