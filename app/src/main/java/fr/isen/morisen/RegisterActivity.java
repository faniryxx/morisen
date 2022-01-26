package fr.isen.morisen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity{
    private String joueur1Telephone;
    private String joueur1Pseudo;
    private String joueur2Telephone;
    private String joueur2Pseudo;

    private String telephone;
    private String pseudo;

    private int playerNumber;

    private EditText pseudoText;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        this.telephone = intent.getStringExtra("telephone");
        mDatabase = FirebaseDatabase.getInstance("https://morisen-9ddf9-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        //Log.i("INFO", this.telephone);
        getPlayerDetails();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void registerButtonClicked(View view) {
        pseudoText = findViewById(R.id.pseudoText);
        pseudo = pseudoText.getText().toString();
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("telephone",telephone);
        intent.putExtra("pseudo",pseudo);
        int playerNumber = getPlayerNumber();
        intent.putExtra("playerNumber",playerNumber);
        writePlayerDataToDatabase(playerNumber);
        startActivity(intent);
    }

    private void setVariables(String pseudo1, String tel1, String pseudo2, String tel2){
        joueur1Telephone = tel1;
        joueur2Telephone = tel2;
        joueur1Pseudo = pseudo1;
        joueur2Pseudo = pseudo2;
    }

    private int getPlayerNumber(){
        if(joueur1Telephone.isEmpty()) {
            return 1;
        }
        else
            return 2;
    }

    private void getPlayerDetails() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void writePlayerDataToDatabase(int playerNumber){
        switch (playerNumber) {
            case 1:
                mDatabase.child("salon1").child("joueurs").child("joueur1").child("telephone").setValue(telephone);
                mDatabase.child("salon1").child("joueurs").child("joueur1").child("pseudo").setValue(pseudo);
                break;
            case 2:
                mDatabase.child("salon1").child("joueurs").child("joueur2").child("telephone").setValue(telephone);
                mDatabase.child("salon1").child("joueurs").child("joueur2").child("pseudo").setValue(pseudo);
                break;
        }
    }
}