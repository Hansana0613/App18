package lk.javainstitute.app18;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

import lk.javainstitute.app18.model.SQLiteHelper;

public class CreateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent i = getIntent();
        String id = i.getStringExtra("id");
        String title = i.getStringExtra("title");
        String content = i.getStringExtra("content");

        EditText editText1 = findViewById(R.id.editTextText1);
        EditText editText2 = findViewById(R.id.editTextText2);

        if (editText1 != null) {
            editText1.setText(title);
        }

        if (editText2 != null) {
            editText2.setText(content);
        }

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText1.getText().toString().isEmpty()) {
                    Toast.makeText(CreateNoteActivity.this, "Please Fill The Title", Toast.LENGTH_LONG).show();

                } else if (editText2.getText().toString().isEmpty()) {
                    Toast.makeText(CreateNoteActivity.this, "Please Fill The Content", Toast.LENGTH_LONG).show();

                } else {
                    //save
                    SQLiteHelper sqLiteHelper = new SQLiteHelper(
                            CreateNoteActivity.this,
                            "mynotebook.db",
                            null,
                            1
                    );

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("title", editText1.getText().toString());
                            contentValues.put("content", editText2.getText().toString());

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                            contentValues.put("date_created", format.format(new Date()));

                            long insertedId = sqLiteDatabase.insert("notes", null, contentValues);
                            Log.i("MyNoteBookLog", String.valueOf(insertedId));

                            sqLiteDatabase.close();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    editText1.setText("");
                                    editText2.setText("");

                                    editText1.requestFocus();
                                }
                            });
                        }
                    }
                    ).start();
                }
            }
        });
    }
}