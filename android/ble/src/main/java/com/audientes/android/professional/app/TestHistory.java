package com.audientes.android.professional.app;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by morten on 26/07/2017.
 */

public class TestHistory
{
    TestResult latest = null;

    public void addResult(TestResult result)
    {
        Realm realm = AudientesApp.instance.realm;
        realm.beginTransaction();
        realm.copyToRealm(result); // Create managed objects directly
        realm.commitTransaction();
        latest = result;
    }

    public TestResult getLatest()
    {
        return latest;
    }

    public ArrayList<TestResult> getList() {
        final RealmResults<TestResult> results = AudientesApp.instance.realm.where(TestResult.class).findAll();

        return new ArrayList<TestResult>(results);
    }

    public void eraseList()
    {
        Realm realm = AudientesApp.instance.realm;
        realm.beginTransaction();
        AudientesApp.instance.realm.delete(TestResult.class);
        realm.commitTransaction();
    }

    public TestResult get(int i)
    {
        final RealmResults<TestResult> results = AudientesApp.instance.realm.where(TestResult.class).findAll();
        return results.get(i);
    }

}
