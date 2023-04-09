package entity;

public class Gamer {

    private String UserName;
    private int GamerID;
    private String Email;
    private String Phone;
    private String PaymentMethod;

    public Gamer(){
    }

    public Gamer(String userName, int gamerID, String email, String phone, String paymentMethod) {
        UserName = userName;
        GamerID = gamerID;
        Email = email;
        Phone = phone;
        PaymentMethod = paymentMethod;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getGamerID() {
        return GamerID;
    }

    public void setGamerID(int gamerID) {
        GamerID = gamerID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }
}
