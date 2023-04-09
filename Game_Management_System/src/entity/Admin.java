package entity;

public class Admin {
    private int AdminID;

    public Admin(){
    }

    public Admin(int adminID) {
        AdminID = adminID;
    }

    public int getAdminID() {
        return AdminID;
    }

    public void setAdminID(int adminID) {
        AdminID = adminID;
    }
}
