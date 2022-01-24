package fr.isen.morisen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class GameActivity extends AppCompatActivity {
    private String joueur1Telephone;
    private String joueur1Pseudo;
    private String joueur2Telephone;
    private String joueur2Pseudo;

    private String telephone;
    private String pseudo;
    private static int playerNumber;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance("https://morisen-9ddf9-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        setContentView(R.layout.activity_game);
        TextView opponentTextView = findViewById(R.id.opponentTextView);
        opponentTextView.setText("");
        getPlayerDetails();
        Intent intent = this.getIntent();
        this.telephone = intent.getStringExtra("telephone");
        this.pseudo = intent.getStringExtra("pseudo");
        this.playerNumber = intent.getIntExtra("playerNumber",0);
    }

    private void updateTextViews(int playerNumber){
        TextView opponentTextView = findViewById(R.id.opponentTextView);
        TextView statusTextView = findViewById(R.id.statusTextView);
        switch (playerNumber){
            case 1:
                if(!joueur2Pseudo.isEmpty()) {
                    opponentTextView.setText(joueur2Pseudo);
                    statusTextView.setText("Vous jouez contre");
                }
                break;
            case 2:
                if(!joueur1Pseudo.isEmpty()) {
                    opponentTextView.setText(joueur1Pseudo);
                    statusTextView.setText("Vous jouez contre");
                }
                break;
        }
    }

    private void setVariables(String pseudo1, String tel1, String pseudo2, String tel2){
        joueur1Telephone = tel1;
        joueur2Telephone = tel2;
        joueur1Pseudo = pseudo1;
        joueur2Pseudo = pseudo2;
    }

    private void getPlayerDetails() {
        // calling add value event listener method
        // for getting the values from database.
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                HashMap value = (HashMap) snapshot.getValue();
                HashMap joueur1Data = (HashMap) value.get("salon1");
                joueur1Data = (HashMap) joueur1Data.get("joueurs");
                joueur1Data = (HashMap) joueur1Data.get("joueur1");
                String joueur1Pseudo = (String) joueur1Data.get("pseudo");
                String joueur1Telephone = (String) joueur1Data.get("telephone");

                HashMap joueur2Data = (HashMap) value.get("salon1");
                joueur2Data = (HashMap) joueur2Data.get("joueurs");
                joueur2Data = (HashMap) joueur2Data.get("joueur2");
                String joueur2Pseudo = (String) joueur2Data.get("pseudo");
                String joueur2Telephone = (String) joueur2Data.get("telephone");

                setVariables(joueur1Pseudo,joueur1Telephone,joueur2Pseudo,joueur2Telephone);
                updateTextViews(GameActivity.playerNumber);

                // after getting the value we are setting
                // our value to our text view in below line.
                //Log.i("VALUE", value.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(GameActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }

        });
    }
}