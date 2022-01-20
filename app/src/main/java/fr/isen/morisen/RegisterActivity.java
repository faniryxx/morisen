package fr.isen.morisen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity{

    private String telephone;
    private String pseudo;
    private EditText pseudoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        this.telephone = intent.getStringExtra("telephone");
        //Log.i("INFO", this.telephone);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void registerButtonClicked(View view) {
        pseudoText = findViewById(R.id.pseudoText);
        pseudo = pseudoText.getText().toString();
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("telephone",telephone);
        intent.putExtra("pseudo",pseudo);
        startActivity(intent);
    }
}