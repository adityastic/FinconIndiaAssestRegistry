package recoveryportal.adityagupta.recoveryportal.Data;

/**
 * Created by adityagupta on 17/11/17.
 */

public class LoginDetails {
    public String Full_Name,User_Name,Address,Mobile_No,Email_Id,ID,Gender,Password;

    public LoginDetails(String full_Name, String user_Name, String address, String mobile_No, String email_Id, String ID, String gender, String password) {
        Full_Name = full_Name;
        User_Name = user_Name;
        Address = address;
        Mobile_No = mobile_No;
        Email_Id = email_Id;
        this.ID = ID;
        Gender = gender;
        Password = password;
    }
}
