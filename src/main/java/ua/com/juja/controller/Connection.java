package ua.com.juja.controller;

public class Connection {
    private String dbName;
    private String userName;
    private String password;
    private String fromPage;

    public Connection(String page) {
        this.fromPage = page;
    }

    public Connection() {
        //NOP
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbname) {
        this.dbName = dbname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFromPage() {
        return fromPage;
    }

    public void setFromPage(String fromPage) {
        this.fromPage = fromPage;
    }



}
