package lk.javainstitute.app18;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lk.javainstitute.app18.model.SQLiteHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreateNoteActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView1 = findViewById(R.id.recyclerView1);

        X x = new X();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(x);
        itemTouchHelper.attachToRecyclerView(recyclerView1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(layoutManager);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(
                MainActivity.this,
                "mynotebook.db",
                null,
                1
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();

                Cursor cursor = sqLiteDatabase.query(
                        "notes",
                        null,
                        null,
                        null,
                        null,
                        null,
                        "`id` DESC"
                );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NoteListAdapter noteListAdapter = new NoteListAdapter(cursor);
                        recyclerView1.setAdapter(noteListAdapter);
                    }
                });

            }
        }).start();
    }
}

class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

    Cursor cursor;

    public NoteListAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView contentView;
        TextView date_createdView;
        View containerView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.textView5);
            contentView = itemView.findViewById(R.id.textView6);
            date_createdView = itemView.findViewById(R.id.textView7);
            containerView = itemView;
        }
    }

    @NonNull
    @Override
    public NoteListAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.note_item, parent, false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.NoteViewHolder holder, int position) {
        cursor.moveToPosition(position);

        String id = cursor.getString(0);
        String title = cursor.getString(1);
        String content = cursor.getString(2);
        String date = cursor.getString(3);

        holder.titleView.setText(title);
        holder.contentView.setText(content);
        holder.date_createdView.setText(date);

        holder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.i("MyNoteBookLog","Item Clicked");
                Intent i = new Intent(view.getContext(), CreateNoteActivity.class);
                i.putExtra("id", id);
                i.putExtra("title", title);
                i.putExtra("content", content);
                view.getContext().startActivity(i);
            }
        });

//        holder.containerView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Log.i("MyNoteBookLog", "On Long Click");
//
//                SQLiteHelper sqLiteHelper = new SQLiteHelper(
//                        view.getContext(),
//                        "mynotebook.db",
//                        null,
//                        1
//                );
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
//                        int row = sqLiteDatabase.delete(
//                                "notes",
//                                "`id`=?",
//                                new String[]{id}
//                        );
//
//                        Log.i("MyNoteBookLog", row+" Row Deleted");
//                    }
//                }).start();
//
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}

class X extends ItemTouchHelper.Callback{

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//        return 0;
        return makeMovementFlags(0,ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Log.i("MyNoteBookLog", "On Swiped");
    }
}
