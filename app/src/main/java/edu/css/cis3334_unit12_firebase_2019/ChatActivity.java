package edu.css.cis3334_unit12_firebase_2019;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    Button btnPost;
    Button btnLogout;
    EditText etMessage;
    TextView tvMsgList;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnPost = findViewById(R.id.buttonPost);
        btnLogout = findViewById(R.id.buttonChatLogout);
        etMessage = findViewById(R.id.editTextMessage);
        tvMsgList = findViewById(R.id.textViewMsgList);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("part12firebaseauth");

        String key = myRef.push().getKey();
        myRef.child(key).setValue("FIRST test message");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String msg = dataSnapshot.getValue(String.class);
                Toast.makeText(getApplicationContext(), "onDataChange called !!! ", Toast.LENGTH_LONG).show();

                tvMsgList.setText("");           // clear out the all messages on the list
                for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()) {
                    String msg = msgSnapshot.getValue(String.class);
                    tvMsgList.setText(msg+ "\n" + tvMsgList.getText());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "btnPost.setOnClickListener called !!! ", Toast.LENGTH_LONG).show();

                String msgText = etMessage.getText().toString();
                etMessage.setText("");           // clear out the message text box to be ready for the next message

                // ---- Get a new database key for the vote
                String key = myRef.push().getKey();
                Toast.makeText(getApplicationContext(), "btnPost.setOnClickListener key = "+key, Toast.LENGTH_LONG).show();

                // ---- write the message to Firebase
                myRef.child(key).setValue(msgText);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();           // quit this activity and return to mainActivity
            }
        });

    }
}
