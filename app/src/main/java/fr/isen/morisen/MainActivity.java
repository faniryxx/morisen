package fr.isen.morisen;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

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
        mDatabase.child("salon1/quitSignal").setValue(0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createSignInIntent();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDatabase.child("salon1").child("auTourDe").setValue(1);
        mDatabase.child("salon1").child("quitSignal").setValue(1);
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
        Log.i("DESTROYED", "App destroyed");
    }

    public void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("telephone",user.getPhoneNumber());
            startActivity(intent);
        } else {
            Log.i("ERROR", "Authentication error") ;
        }
    }

}