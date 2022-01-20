package fr.isen.morisen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameActivity extends AppCompatActivity {
    private String telephone;
    private String pseudo;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance("https://morisen-9ddf9-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        setContentView(R.layout.activity_game);
        Intent intent = this.getIntent();
        this.telephone = intent.getStringExtra("telephone");
        this.pseudo = intent.getStringExtra("pseudo");
        //Log.i("TELEPHONE", this.telephone);
        //Log.i("PSEUDO", this.pseudo);
        writePlayerDataToDatabase();
    }

    private void writePlayerDataToDatabase(){
        mDatabase.child("salon1").child("joueurs").child("joueur1").setValue(telephone);
    }

    /*
    private void checkRoomStatus(){
        String test = mDatabase.child("salon1").child("joueurs").child("joueur1").get();
        Log.i("checkRoomStatus", test);
    }*/
}