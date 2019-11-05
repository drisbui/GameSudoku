package com.example.gamesudoku;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnNew,btnAc,btnAb,btnE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNew=(Button)findViewById(R.id.btnNewGame);
        btnAc=(Button)findViewById(R.id.btnAchievements);
        btnAb=(Button)findViewById(R.id.btnAbout);
        btnE=(Button)findViewById(R.id.btnExit);

        btnNew.setOnClickListener(this);
        btnAc.setOnClickListener(this);
        btnAb.setOnClickListener(this);
        btnE.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnNewGame:
                openNewGameDialog();
                break;
            case R.id.btnAbout:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About");
                builder.setMessage(R.string.about);
                builder.setCancelable(false);
                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case  R.id.btnAchievements:
                Intent intent=new Intent(MainActivity.this,AchievementActivity.class);
                startActivity(intent);
                break;
            case R.id.btnExit:
                finish();
                break;
        }
    }

    private void openNewGameDialog() {
        new AlertDialog.Builder(this).setTitle("New Game").setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startGame(i);
            }
        }).show();
    }

    private void startGame(int i){
        Intent intent=new Intent(MainActivity.this,GameActivity.class);
        intent.putExtra(GameActivity.KEY_DIFFICULTY,i);
        startActivity(intent);
    }
}
