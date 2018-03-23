package co.edu.udea.compumovil.gr04_20171.lab4.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by daemonsoft on 10/03/17.
 */
@IgnoreExtraProperties
public class User {
    private String name;
    private String age;
    private Boolean keepLogged;
    private Boolean notification;
    private Boolean sound;
    private String seconds;

    public User() {
    }

    public User(String name, String age, Boolean keepLogged, Boolean notification, Boolean sound, String seconds) {
        this.name = name;
        this.age = age;
        this.keepLogged = keepLogged;
        this.notification = notification;
        this.sound = sound;
        this.seconds = seconds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Boolean getKeepLogged() {
        return keepLogged;
    }

    public void setKeepLogged(Boolean keepLogged) {
        this.keepLogged = keepLogged;
    }

    public Boolean getNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

    public Boolean getSound() {
        return sound;
    }

    public void setSound(Boolean sound) {
        this.sound = sound;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }
}