package co.edu.udea.compumovil.gr04_20171.lab3.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daemonsoft on 10/03/17.
 */
public class User implements Parcelable {
    private String name;
    private String email;
    private String password;
    private Integer age;
    private Boolean keepSession;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getKeepSession() {
        return keepSession;
    }

    public void setKeepSession(Boolean keepSession) {
        this.keepSession = keepSession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {

    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = 0;
        this.keepSession = false;
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        password = in.readString();
        age = in.readInt();
        byte keepSessionVal = in.readByte();
        keepSession = keepSessionVal == 0x02 ? null : keepSessionVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeInt(age);
        if (keepSession == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (keepSession ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}