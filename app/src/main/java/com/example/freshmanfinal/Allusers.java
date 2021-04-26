package com.example.freshmanfinal;

public class Allusers {

    public String Email , Department;

    public Allusers()
    {

    }
    public Allusers(String Email, String Department) {
        this.Email = Email;
        this.Department = Department;

    }

    public String getUser_id() {


        return Email;
    }

    public void setUser_id(String user_id) {
        this.Email = user_id;
    }

    public String getUser_dept() { return Department; }

    public void setUser_dept(String user_department) {
        this.Department = user_department;
    }




}
