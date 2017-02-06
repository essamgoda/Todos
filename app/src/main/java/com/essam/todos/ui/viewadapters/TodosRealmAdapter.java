package com.essam.todos.ui.viewadapters;

import android.content.Context;

import com.essam.todos.models.TodoItem;

import io.realm.RealmResults;

/**
 * Created by essam on 06/02/17.
 */

public class TodosRealmAdapter extends TodosModelAdapter<TodoItem> {

    public TodosRealmAdapter(Context context, RealmResults<TodoItem> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}
