package javafxproyek;

public class UserInfo {
    private int id;
    private String username;

    public UserInfo(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUnserNama(String usename){
        this.username = usename;
    }
}
