package com.professional.app;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by morten on 26/07/2017.
 */

public class TestResult extends RealmObject
{
    public String patientName;
    public String email;
    public String phone;
    public int age;
    public int gender;
    public byte left[];
    public byte right[];
    public byte freqs[];
    public boolean fromHistory;
    public boolean completed;
    public Date date;
    public boolean submitted;
    public RealmList<LogEntry> log;

    public static TestResult createFromMeta(String patientName, int age, int gender, String email, String phone)
    {
        TestResult r = new TestResult();
        r.patientName = patientName;
        r.age = age;
        r.gender = gender;
        r.email = email;
        r.phone = phone;
        r.fromHistory = false;
        r.completed = false;
        r.left = new byte[4];
        r.right = new byte[4];
        r.date = new Date();
        r.submitted = false;
        return r;
    }

    public String genderString()
    {
        if (this.gender == 1)
        {
            return "Male";
        }
        else if (this.gender == 2)
        {
            return "Female";
        }
        else
        {
            return "Unknown Gender";
        }
    }

}
