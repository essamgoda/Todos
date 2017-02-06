package com.essam.todos.ui.views;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.essam.todos.R;
import com.essam.todos.models.TodoItem;
import com.essam.todos.ui.viewadapters.TodosAdapter;
import com.essam.todos.ui.viewadapters.TodosRealmAdapter;
import com.essam.todos.ui.viewcontrollers.RealmController;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    EditText todo;
    ImageView add;
    public TodosAdapter adapter, adapterFinished;
    private Realm realm;
    private LayoutInflater inflater;
    public RecyclerView recycler, recyclerFinished;
    private SearchView searchView;
    private MenuItem searchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recycler = (RecyclerView) findViewById(R.id.recycler_todo);
        recyclerFinished = (RecyclerView) findViewById(R.id.recycler_finished);

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        setupRecycler();


        // refresh the realm instance
        RealmController.with(this).refresh();

        setRealmAdapter(RealmController.with(this).getFinishedToDos("false"));
        setFinishedAdapter(RealmController.with(this).getFinishedToDos("true"));

        todo = (EditText) findViewById(R.id.todo_text);
        add = (ImageView) findViewById(R.id.add_todo);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodoItem toDo = new TodoItem();
                toDo.setId(RealmController.getInstance().getToDos().size() + 1);
                toDo.setTodo(todo.getText().toString());
                toDo.setIsFinished("false");

                if (todo.getText() == null || todo.getText().toString().equals("") || todo.getText().toString().equals(" ")) {
                    Toast.makeText(MainActivity.this, "Entry not saved, missing Todo", Toast.LENGTH_SHORT).show();
                } else {
                    // Persist your data easily
                    realm.beginTransaction();
                    realm.copyToRealm(toDo);
                    realm.commitTransaction();

                    adapter.notifyDataSetChanged();
                    todo.setText("");

                    // scroll the recycler view to bottom
                    recycler.scrollToPosition(RealmController.getInstance().getToDos().size() - 1);
                }
            }
        });


    }

    public void setRealmAdapter(RealmResults<TodoItem> Item) {
        TodosRealmAdapter realmAdapter = new TodosRealmAdapter(this.getApplicationContext(), Item, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();

    }

    public void notifyDataChanged() {
        adapter.notifyDataSetChanged();
        adapterFinished.notifyDataSetChanged();
    }

    public void setFinishedAdapter(RealmResults<TodoItem> Item) {
        TodosRealmAdapter realmAdapter = new TodosRealmAdapter(this.getApplicationContext(), Item, true);
        // Set the data and tell the RecyclerView to draw
        adapterFinished.setRealmAdapter(realmAdapter);
        adapterFinished.notifyDataSetChanged();

    }

    private void setupRecycler() {

        recycler.setHasFixedSize(true);
        recyclerFinished.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        final LinearLayoutManager layoutManagerF = new LinearLayoutManager(this);
        layoutManagerF.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerFinished.setLayoutManager(layoutManagerF);

        adapter = new TodosAdapter(this);
        recycler.setAdapter(adapter);
        adapterFinished = new TodosAdapter(this);
        recyclerFinished.setAdapter(adapterFinished);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        setRealmAdapter(RealmController.with(this).queryedToDos(newText,"false"));
        setFinishedAdapter(RealmController.with(this).queryedToDos(newText,"true"));
        return true;
    }
}
