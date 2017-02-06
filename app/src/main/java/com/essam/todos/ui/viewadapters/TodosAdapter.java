package com.essam.todos.ui.viewadapters;

import com.essam.todos.R;
import com.essam.todos.models.TodoItem;
import com.essam.todos.ui.viewcontrollers.RealmController;
import com.essam.todos.ui.views.MainActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by essam on 06/02/17.
 */

public class TodosAdapter extends TodosViewAdapter<TodoItem> {
    final Context context;
    private Realm realm;
    private LayoutInflater inflater;

    public TodosAdapter(Context context) {

        this.context = context;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        realm = RealmController.getInstance().getRealm();

        final TodoItem todo = getItem(position);
        final CardViewHolder holder = (CardViewHolder) viewHolder;
        int count = position + 1;
        holder.textCounter.setText(count + ". ");
        holder.textToDos.setText(todo.getTodo());

        if (todo.getIsFinished().equals("true"))
            holder.finished.setChecked(true);
        else
            holder.finished.setChecked(false);


            holder.finished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Get the book title to show it in toast message
                    TodoItem b = realm.where(TodoItem.class).equalTo("id",todo.getId()).findFirst();

                    String to=b.getTodo();

                    TodoItem edit=new TodoItem();
                    edit.setTodo(b.getTodo());
                    edit.setId(RealmController.getInstance().getToDos().size() + 1);
                    realm.beginTransaction();
                    if (todo.getIsFinished().equals("false")) {
                        b.setIsFinished("true");
                    }
                    else {
                        b.setIsFinished("false");
                    }


                    realm.commitTransaction();

                    ((MainActivity) context).notifyDataChanged();
                    if (todo.getIsFinished().equals("false"))
                        Toast.makeText(context, to+ " is Finished", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, to+ " is unFinished", Toast.LENGTH_SHORT).show();
                }
            });

    }

    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {

        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout card;
        public TextView textCounter;
        public TextView textToDos;
        public CheckBox finished;


        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);

            card = (RelativeLayout) itemView.findViewById(R.id.card);
            textCounter = (TextView) itemView.findViewById(R.id.count);
            textToDos = (TextView) itemView.findViewById(R.id.text);
            finished = (CheckBox) itemView.findViewById(R.id.finished);

        }
    }
}
