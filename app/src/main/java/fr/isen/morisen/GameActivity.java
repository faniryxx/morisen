package fr.isen.morisen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private String joueur1Telephone;
    private String joueur1Pseudo;
    private String joueur2Telephone;
    private String joueur2Pseudo;

    private Long auTourDe;

    private String telephone;
    private String pseudo;
    private static int playerNumber;
    private DatabaseReference refCases;
    private DatabaseReference refJoueurs;
    private DatabaseReference refTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.auTourDe = Long.valueOf(1);
        super.onCreate(savedInstanceState);
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://morisen-9ddf9-default-rtdb.europe-west1.firebasedatabase.app");
        refJoueurs = db.getReference("salon1/joueurs");
        refCases = db.getReference("salon1/cases");
        refTour = db.getReference("salon1/auTourDe");
        setContentView(R.layout.activity_game);
        TextView opponentTextView = findViewById(R.id.opponentTextView);
        TextView tourTextView = findViewById(R.id.tourTextView);
        tourTextView.setVisibility(View.INVISIBLE);
        opponentTextView.setText("");
        addListenerToRefJoueurs();
        addListenerToRefCases();
        addListenerToRefTour();
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

    private void addListenerToRefJoueurs() {
        refJoueurs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap value = (HashMap) snapshot.getValue();
                HashMap joueur1Data = (HashMap) value.get("joueur1");
                String joueur1Pseudo = (String) joueur1Data.get("pseudo");
                String joueur1Telephone = (String) joueur1Data.get("telephone");
                HashMap joueur2Data = (HashMap) value.get("joueur2");
                String joueur2Pseudo = (String) joueur2Data.get("pseudo");
                String joueur2Telephone = (String) joueur2Data.get("telephone");
                setVariables(joueur1Pseudo,joueur1Telephone,joueur2Pseudo,joueur2Telephone);
                if(!joueur1Pseudo.isEmpty() && !joueur2Pseudo.isEmpty()){
                    TextView tourTextView = findViewById(R.id.tourTextView);
                    tourTextView.setVisibility(View.VISIBLE);
                }
                updateTextViews(GameActivity.playerNumber);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GameActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void buttonClicked(View view){
        String buttonIndex = (String) view.getTag();
        Button button = (Button) view;
        button.setClickable(false);
        refCases.child(buttonIndex).setValue(this.playerNumber);
        if (auTourDe == 1) {
            refTour.setValue(2);
        } else if (auTourDe == 2) {
            refTour.setValue(1);
        }
    }

    private List<Button> getAllButtons(){
        List<Button> listButtons = new ArrayList<Button>();
        listButtons.add(findViewById(R.id.button0));
        listButtons.add(findViewById(R.id.button1));
        listButtons.add(findViewById(R.id.button2));
        listButtons.add(findViewById(R.id.button3));
        listButtons.add(findViewById(R.id.button4));
        listButtons.add(findViewById(R.id.button5));
        listButtons.add(findViewById(R.id.button6));
        listButtons.add(findViewById(R.id.button7));
        listButtons.add(findViewById(R.id.button8));
        return listButtons;
    }

    private void updateButtons(Long[] cases){
        List<Button> listButtons = getAllButtons();
        for(int i=0;i< listButtons.size();i++){
            Button button = listButtons.get(i);
            if(cases[i] == 1){
                button.setForeground(getDrawable(R.mipmap.cercle_foreground));
                Log.i("DEBUG", "cases["+i+"+] == 1");
            }
            else if(cases[i] == 2){
                button.setForeground(getDrawable(R.mipmap.croix_foreground));
            }
            if(cases[i] != 0){
                button.setClickable(false);
            }
            else if(cases[i] == 0){
                button.setClickable(true);
                button.setForeground(getDrawable(R.drawable.cercle_background));
            }
        }
    }

    private void addListenerToRefCases() {
        refCases.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Object> values = (List<Object>) snapshot.getValue();
                Long[] cases;
                cases = new Long[9];
                for(int i=0;i<9;i++){
                    cases[i] = (Long) values.get(i);
                }
                updateButtons(cases);
                int winner = checkIfWin(cases);
                if(winner!=0) {
                    if(winner == 1)
                        showWinnerDialog(joueur1Pseudo);
                    else if(winner == 2)
                        showWinnerDialog(joueur2Pseudo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GameActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateInterfaceBasedOnTurn(Long auTourDe){
        List<Button> listButtons = getAllButtons();
        TextView tourTextView = findViewById(R.id.tourTextView);
        if(auTourDe == playerNumber) {
            tourTextView.setText("A vous de jouer !");
            for(int i=0;i< listButtons.size();i++){
                Drawable background = listButtons.get(i).getBackground();
                if(background == getDrawable(R.drawable.cercle_background))
                    listButtons.get(i).setClickable(true);
            }
        }
        else{
            for(int i=0;i< listButtons.size();i++){
                listButtons.get(i).setClickable(false);
            }
            tourTextView.setText("L'adversaire joue ...");
        }
    }

    private void addListenerToRefTour() {
        refTour.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                auTourDe = (Long) snapshot.getValue();
                updateInterfaceBasedOnTurn(auTourDe);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GameActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showWinnerDialog(String pseudo){
        new AlertDialog.Builder(this)
                .setTitle("Partie terminée")
                .setMessage(pseudo+" a gagné la partie !")
                .setPositiveButton("Rejouer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearCases();
                    }
                })
                .setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        exit();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void exit(){
        this.finishAffinity();
    }

    private void clearCases(){
        refCases.child("0").setValue(0);
        refCases.child("1").setValue(0);
        refCases.child("2").setValue(0);
        refCases.child("3").setValue(0);
        refCases.child("4").setValue(0);
        refCases.child("5").setValue(0);
        refCases.child("6").setValue(0);
        refCases.child("7").setValue(0);
        refCases.child("8").setValue(0);
    }

    private int checkIfWin(Long[] cases){
       /*
       Numéros des cases:
       0    1   2
       3    4   5
       6    7   8
        */
        int winner = 0;

        if(cases[0] == cases[1] && cases[0] == cases[2] && cases[0] != 0){
            if(cases[0] == 1){
                winner = 1;
            }
            else
                winner = 2;
        }

        if(cases[3] == cases[4] && cases[3] == cases[5] && cases[3] != 0){
            if(cases[3] == 1){
                winner = 1;
            }
            else
                winner = 2;
        }

        if(cases[6] == cases[7] && cases[6] == cases[8] && cases[6] != 0){
            if(cases[6] == 1){
                winner = 1;
            }
            else
                winner = 2;
        }

        if(cases[0] == cases[3] && cases[0] == cases[6] && cases[0] != 0){
            if(cases[0] == 1){
                winner = 1;
            }
            else
                winner = 2;
        }

        if(cases[1] == cases[4] && cases[1] == cases[7] && cases[1] != 0){
            if(cases[1] == 1){
                winner = 1;
            }
            else
                winner = 2;
        }

        if(cases[2] == cases[5] && cases[2] == cases[8] && cases[2] != 0){
            if(cases[2] == 1){
                winner = 1;
            }
            else
                winner = 2;
        }

        if(cases[0] == cases[4] && cases[0] == cases[8] && cases[0] != 0){
            if(cases[0] == 1){
                winner = 1;
            }
            else
                winner = 2;
        }

        if(cases[2] == cases[4] && cases[2] == cases[6] && cases[2] != 0){
            if(cases[2] == 1){
                winner = 1;
            }
            else
                winner = 2;
        }

        return winner;
    }

}