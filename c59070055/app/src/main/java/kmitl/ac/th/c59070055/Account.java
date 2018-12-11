package kmitl.ac.th.c59070055;

public class Account  {
    private static Account accountInstance;

    private int primaryid;
    private String username = null;
    private String password;
    private String fullname;
    private String age;

    private Account() {}

    public static Account getAccountInstance() {
        if (accountInstance == null) {
            accountInstance = new Account();
        }
        return accountInstance;
    }

    public int getPrimaryid() {
        return primaryid;
    }

    public void setPrimaryid(int primaryid) {
        this.primaryid = primaryid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
