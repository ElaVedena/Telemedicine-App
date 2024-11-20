package com.database;

import java.util.Date;

// new User(name, email, username, password, createdAt)
public class User {
    public String name;
    public String email;
    public String username;
    public String password;
    public int average;
    public final String createdAt = new Date().toString();
    public User()
    {

    }

    public User(String name, String email, String username, String password)
    {
        setName(name);
        setPassword(password);
        setEmail(email);
        setUsername(username);
    }

    // Setters
    public void setaverage(int average)
    {
        this.average = average;}
    public void setName(String name)
    {
        this.name = name;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setUsername(String username){
        this.username= username;
    }
    public void setPassword(String password){
        this.password= password;
    }


    // Getters
    public int getaverage()
    {
        return this.average;}

    public String getName()
    {
        return this.name;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getEmail()
    {
        return email;
    }
}
