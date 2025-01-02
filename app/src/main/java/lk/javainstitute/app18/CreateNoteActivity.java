package lk.javainstitute.app18;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText1 = findViewById(R.id.editTextText1);
                EditText editText2 = findViewById(R.id.editTextText2);

                if (editText1.getText().toString().isEmpty()) {
                    Toast.makeText(CreateNoteActivity.this, "Please Fill The Title", Toast.LENGTH_LONG).show();

                } else if (editText2.getText().toString().isEmpty()) {
                    Toast.makeText(CreateNoteActivity.this, "Please Fill The Content", Toast.LENGTH_LONG).show();

                } else {
                    //save

                }
            }
        });
    }
}