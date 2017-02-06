package com.essam.todos.ui.viewcontrollers;


import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.essam.todos.models.TodoItem;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by essam on 06/02/17.
 */

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    public void refresh() {

        realm.refresh();
    }

    public void clearAll() {

        realm.beginTransaction();
        realm.clear(TodoItem.class);
        realm.commitTransaction();
    }

    public RealmResults<TodoItem> getToDos() {

        return realm.where(TodoItem.class).findAll();
    }

    public TodoItem getToDos(String id) {

        return realm.where(TodoItem.class).equalTo("id", id).findFirst();
    }

    public RealmResults<TodoItem> getFinishedToDos(String Finished) {

        return realm.where(TodoItem.class).equalTo("isFinished", Finished).findAll();
    }

    public boolean hasToDos() {

        return !realm.allObjects(TodoItem.class).isEmpty();
    }

    public RealmResults<TodoItem> queryedToDos(String text,String isFinished) {

        return realm.where(TodoItem.class)
                .contains("todo", text)
                .contains("isFinished",isFinished)
                .findAll();

    }
}

