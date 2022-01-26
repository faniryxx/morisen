package fr.isen.morisen;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String joueur1;
    private String joueur2;

    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance("https://morisen-9ddf9-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        //getPlayerNumber();
        mDatabase.child("salon1/quitSignal").setValue(0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createSignInIntent();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDatabase.child("salon1").child("auTourDe").setValue(1);
        mDatabase.child("salon1").child("quitSignal").setValue(0);
        mDatabase.child("salon1").child("joueurs").child("joueur1").child("telephone").setValue("");
        mDatabase.child("salon1").child("joueurs").child("joueur1").child("pseudo").setValue("");
        mDatabase.child("salon1").child("joueurs").child("joueur2").child("telephone").setValue("");
        mDatabase.child("salon1").child("joueurs").child("joueur2").child("pseudo").setValue("");
        mDatabase.child("salon1").child("cases").child("0").setValue(0);
        mDatabase.child("salon1").child("cases").child("1").setValue(0);
        mDatabase.child("salon1").child("cases").child("2").setValue(0);
        mDatabase.child("salon1").child("cases").child("3").setValue(0);
        mDatabase.child("salon1").child("cases").child("4").setValue(0);
        mDatabase.child("salon1").child("cases").child("5").setValue(0);
        mDatabase.child("salon1").child("cases").child("6").setValue(0);
        mDatabase.child("salon1").child("cases").child("7").setValue(0);
        mDatabase.child("salon1").child("cases").child("8").setValue(0);
        Log.i("DESTROYED", "Activity destroyed");
    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_create_intent]
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            //Log.i("INFO", "Authenticated successfully") ;
            Intent intent = new Intent(this, RegisterActivity.class);
            // Retrieve phone number
            //Log.i("INFO", user.getPhoneNumber());
            intent.putExtra("telephone",user.getPhoneNumber());
            startActivity(intent);
        } else {
            Log.i("ERROR", "Authentication error") ;
        }
    }
/*
    private void getPlayerNumber(){
        mDatabase.child("salon1").child("joueurs").child("joueur1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });

        Log.i("joueur1", joueur1) ;
        Log.i("joueur2", joueur2) ;
    }*/
}