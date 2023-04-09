package entity;

public class User {
    private int UserID;
    private String UserName;
    private String Password;
    private String Email;
    private String Phone;

    public User(){
    }

    public User(int userID, String userName, String password, String email, String phone) {
        UserID = userID;
        UserName = userName;
        Password = password;
        Email = email;
        Phone = phone;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getUserID() {
        return UserID;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return Password;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }
}
