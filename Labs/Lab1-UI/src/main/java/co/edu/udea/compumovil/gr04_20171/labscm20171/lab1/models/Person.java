package co.edu.udea.compumovil.gr04_20171.labscm20171.lab1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by daemonsoft on 19/02/17.
 */
public class Person implements Parcelable {
    private String name;
    private String lastname;
    private String phone;
    private String address;
    private String email;
    private String country;
    private String city;

    private Date birthdate;
    private int education;
    private int gender;

    private int hread;
    private int hwatchtv;
    private int hswim;
    private int hsing;
    private int hdance;

    public int getHread() {
        return hread;
    }

    public void setHread(int hread) {
        this.hread = hread;
    }

    public int getHwatchtv() {
        return hwatchtv;
    }

    public void setHwatchtv(int hwatchtv) {
        this.hwatchtv = hwatchtv;
    }

    public int getHswim() {
        return hswim;
    }

    public void setHswim(int hswim) {
        this.hswim = hswim;
    }

    public int getHsing() {
        return hsing;
    }

    public void setHsing(int hsing) {
        this.hsing = hsing;
    }

    public int getHdance() {
        return hdance;
    }

    public void setHdance(int hdance) {
        this.hdance = hdance;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Person(String name, String lastname, Date birthdate, int education, int gender) {
        this.name = name;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.education = education;
        this.gender = gender;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public int getEducation() {
        return education;
    }

    public void setEducation(int education) {
        this.education = education;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    protected Person(Parcel in) {
        name = in.readString();
        lastname = in.readString();
        phone = in.readString();
        address = in.readString();
        email = in.readString();
        country = in.readString();
        city = in.readString();
        long tmpBirthdate = in.readLong();
        birthdate = tmpBirthdate != -1 ? new Date(tmpBirthdate) : null;
        education = in.readInt();
        gender = in.readInt();
        hread = in.readInt();
        hwatchtv = in.readInt();
        hswim = in.readInt();
        hsing = in.readInt();
        hdance = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(lastname);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeLong(birthdate != null ? birthdate.getTime() : -1L);
        dest.writeInt(education);
        dest.writeInt(gender);
        dest.writeInt(hread);
        dest.writeInt(hwatchtv);
        dest.writeInt(hswim);
        dest.writeInt(hsing);
        dest.writeInt(hdance);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}