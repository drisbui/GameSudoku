package com.example.gamesudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamesudoku.model.AchievementAdapter;
import com.example.gamesudoku.model.Achievements;
import com.example.gamesudoku.model.DBManager;

import java.util.ArrayList;

public class AchievementActivity extends AppCompatActivity {

    Button btnBack;
    GridView gridView;
    TextView tvXXX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        btnBack=(Button)findViewById(R.id.btnBack);
        DBManager dbManager=new DBManager(this);
        //doc du lieu
        ArrayList<Achievements> arrayList=dbManager.getAllAchievements();
        gridView=(GridView) findViewById(R.id.gridViewA);
        final AchievementAdapter adapter=new AchievementAdapter(AchievementActivity.this,arrayList);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(AchievementActivity.this,adapter.getCount()+"",Toast.LENGTH_LONG).show();
                finish();

            }
        });
        tvXXX=findViewById(R.id.tvXXX);
        String chuoi="";
        for (Achievements item:arrayList) {
            chuoi+=item.toString()+"\n";
        }
        tvXXX.setText(chuoi);
    }
}
