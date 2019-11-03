package com.example.gamesudoku;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamesudoku.fragments.CellGroupFragment;
import com.example.gamesudoku.model.Achievements;
import com.example.gamesudoku.model.Board;
import com.example.gamesudoku.model.DBManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GameActivity extends AppCompatActivity implements CellGroupFragment.OnFragmentInteractionListener {


    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btnDel;
    public static final String KEY_DIFFICULTY ="com.example.demosudoku3.difficulty";

    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_MEDIUM = 1;
    public static final int DIFFICULTY_HARD = 2;
    private final String TAG = "GameActivity";
    private TextView clickedCell;
    private int clickedGroup;
    private int clickedCellId;
    private Board startBoard;
    private Board currentBoard;
    //đếm thời gian
    TextView txtTimer;
    long lStartTime, lPauseTime, lSystemTime = 0L;
    Handler handler = new Handler();
    boolean isRun;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            lSystemTime = SystemClock.uptimeMillis() - lStartTime;
            long lUpdateTime = lPauseTime + lSystemTime;
            long secs = (long)(lUpdateTime/1000);
            long mins= secs/60;
            secs = secs %60;
            txtTimer.setText(""+mins+":" + String.format("%02d",secs) );
            handler.postDelayed(this,0);
        }
    };

    //--------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // Khai báo
        btn1=(Button)findViewById(R.id.btn1);
        btn2=(Button)findViewById(R.id.btn2);
        btn3=(Button)findViewById(R.id.btn3);
        btn4=(Button)findViewById(R.id.btn4);
        btn5=(Button)findViewById(R.id.btn5);
        btn6=(Button)findViewById(R.id.btn6);
        btn7=(Button)findViewById(R.id.btn7);
        btn8=(Button)findViewById(R.id.btn8);
        btn9=(Button)findViewById(R.id.btn9);
        btnDel=(Button)findViewById(R.id.btnDel);
        txtTimer=(TextView)findViewById(R.id.tvTimer);

        btn1.setOnClickListener(new MyEvenButton());
        btn2.setOnClickListener(new MyEvenButton());
        btn3.setOnClickListener(new MyEvenButton());
        btn4.setOnClickListener(new MyEvenButton());
        btn5.setOnClickListener(new MyEvenButton());
        btn6.setOnClickListener(new MyEvenButton());
        btn7.setOnClickListener(new MyEvenButton());
        btn8.setOnClickListener(new MyEvenButton());
        btn9.setOnClickListener(new MyEvenButton());
        btnDel.setOnClickListener(new MyEvenButton());

        int difficulty = getIntent().getIntExtra(KEY_DIFFICULTY,
                DIFFICULTY_EASY);
        ArrayList<Board> boards = readGameBoards(difficulty);
        startBoard = chooseRandomBoard(boards);
        currentBoard = new Board();
        currentBoard.copyValues(startBoard.getGameCells());

        int cellGroupFragments[] = new int[]{R.id.cellGroupFragment, R.id.cellGroupFragment2, R.id.cellGroupFragment3, R.id.cellGroupFragment4,
                R.id.cellGroupFragment5, R.id.cellGroupFragment6, R.id.cellGroupFragment7, R.id.cellGroupFragment8, R.id.cellGroupFragment9};
        for (int i = 1; i < 10; i++) {
            CellGroupFragment thisCellGroupFragment = (CellGroupFragment) getSupportFragmentManager().findFragmentById(cellGroupFragments[i-1]);
            thisCellGroupFragment.setGroupId(i);
        }

        //Appear all values from the current board
        CellGroupFragment tempCellGroupFragment;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int column = j / 3;
                int row = i / 3;

                int fragmentNumber = (row * 3) + column;
                tempCellGroupFragment = (CellGroupFragment) getSupportFragmentManager().findFragmentById(cellGroupFragments[fragmentNumber]);
                int groupColumn = j % 3;
                int groupRow = i % 3;

                int groupPosition = (groupRow * 3) + groupColumn;
                int currentValue = currentBoard.getValue(i, j);

                if (currentValue != 0) {
                    tempCellGroupFragment.setValue(groupPosition, currentValue);
                }
            }
        }
        TimeStart();
    }

    private ArrayList<Board> readGameBoards(int difficulty) {
        ArrayList<Board> boards = new ArrayList<>();
        int fileId;
        if (difficulty == DIFFICULTY_EASY) {
            fileId = R.raw.normal;
        } else if (difficulty == DIFFICULTY_MEDIUM) {
            fileId = R.raw.easy;
        } else {
            fileId = R.raw.hard;
        }

        InputStream inputStream = getResources().openRawResource(fileId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                Board board = new Board();
                // read all lines in the board
                for (int i = 0; i < 9; i++) {
                    String rowCells[] = line.split(" ");
                    for (int j = 0; j < 9; j++) {
                        if (rowCells[j].equals("-")) {
                            board.setValue(i, j, 0);
                        } else {
                            board.setValue(i, j, Integer.parseInt(rowCells[j]));
                        }
                    }
                    line = bufferedReader.readLine();
                }
                boards.add(board);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        //reading from internal storage (/data/data/<package-name>/files)
        String fileName = "boards-";
        if (difficulty == 0) {
            fileName += "easy";
        } else if (difficulty == 1) {
            fileName += "normal";
        } else {
            fileName += "hard";
        }

        FileInputStream fileInputStream;
        try {
            fileInputStream = this.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader internalBufferedReader = new BufferedReader(inputStreamReader);
            String line = internalBufferedReader.readLine();
            line = internalBufferedReader.readLine();
            while (line != null) {
                Board board = new Board();
                // read all lines in the board
                for (int i = 0; i < 9; i++) {
                    String rowCells[] = line.split(" ");
                    for (int j = 0; j < 9; j++) {
                        if (rowCells[j].equals("-")) {
                            board.setValue(i, j, 0);
                        } else {
                            board.setValue(i, j, Integer.parseInt(rowCells[j]));
                        }
                    }
                    line = internalBufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                }
                boards.add(board);
                line = internalBufferedReader.readLine();
            }
            internalBufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boards;
    }
    private Board chooseRandomBoard(ArrayList<Board> boards) {
        int randomNumber = (int) (Math.random() * boards.size());
        return boards.get(randomNumber);
    }

    @Override
    public boolean checkAllGroups() {
        int cellGroupFragments[] = new int[]{R.id.cellGroupFragment, R.id.cellGroupFragment2, R.id.cellGroupFragment3, R.id.cellGroupFragment4,
                R.id.cellGroupFragment5, R.id.cellGroupFragment6, R.id.cellGroupFragment7, R.id.cellGroupFragment8, R.id.cellGroupFragment9};
        for (int i = 0; i < 9; i++) {
            CellGroupFragment thisCellGroupFragment = (CellGroupFragment) getSupportFragmentManager().findFragmentById(cellGroupFragments[i]);
            if (!thisCellGroupFragment.checkGroupCorrect()) {
                return false;
            }
        }
        return true;
    }

    public void onGoBackButtonClicked(View view) {
        finish();
    }


    @Override
    public void onFragmentInteraction(int groupId, int cellId, View view) {
        try {
            String a=clickedCell.getText().toString();
            if(a.length()==0)
                clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell));
        }catch (Exception ex)
        {

        }

        clickedGroup = groupId;
        clickedCellId = cellId;
        clickedCell = (TextView) view;
        Log.i(TAG, "Clicked group " + groupId + ", cell " + cellId);
        if (!isStartPiece(groupId, cellId)) {
            clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell_unsure));
        } else {
            Toast.makeText(this, "Không được! đây là số mặt định", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isStartPiece(int group, int cell) {
        int row = ((group-1)/3)*3 + (cell/3);
        int column = ((group-1)%3)*3 + ((cell)%3);
        return startBoard.getValue(row, column) != 0;
    }
    private class MyEvenButton implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            int row = ((clickedGroup - 1) / 3) * 3 + (clickedCellId / 3);
            int column = ((clickedGroup - 1) % 3) * 3 + ((clickedCellId) % 3);
            Button buttonCheckBoard = findViewById(R.id.buttonCheckBoard);
            int number;
            switch (view.getId())
            {
                case R.id.btn1:
                    number=1;break;
                case R.id.btn2:
                    number=2;break;
                case R.id.btn3:
                    number=3;break;
                case R.id.btn4:
                    number=4;break;
                case R.id.btn5:
                    number=5;break;
                case R.id.btn6:
                    number=6;break;
                case R.id.btn7:
                    number=7;break;
                case R.id.btn8:
                    number=8;break;
                case R.id.btn9:
                    number=9;break;
                case R.id.btnDel:
                    number=0;break;
                default:
                    throw new IllegalStateException("Unexpected value: " + view.getId());
            }

            if(number==0)
            {
                clickedCell.setText("");
                clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell));
                currentBoard.setValue(row, column, 0);
                buttonCheckBoard.setVisibility(View.INVISIBLE);
            }
            else {
                currentBoard.isBoardCell(number,row,column);
                if (currentBoard.isBoardCell(number, row, column) == false) {
                    clickedCell.setBackground(getResources().getDrawable(R.drawable.table_broder_cell_er));
                } else
                {
                    clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell));
                }
                clickedCell.setText(String.valueOf(number));
                currentBoard.setValue(row, column, number);
                if (currentBoard.isBoardFull()) {
                    buttonCheckBoard.setVisibility(View.VISIBLE);
                } else {
                    buttonCheckBoard.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void onCheckBoardButtonClicked(View view) {
        currentBoard.isBoardCorrect();
        if(checkAllGroups() && currentBoard.isBoardCorrect()) {
            TimeStop();
            Toast.makeText(this, "Bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
            displayAlertDialog();
        } else {
            Toast.makeText(this, "Lời giải không chính xác", Toast.LENGTH_SHORT).show();
        }
    }

    //Đo thời gian

    public void TimeStart()
    {
        if(isRun)
            return;
        isRun = true;
        lStartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }




    public void TimeStop()
    {
       if(!isRun) return;
       isRun = false;
       lPauseTime = 0;
       handler.removeCallbacks(runnable);

    }

    public void displayAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        final TextView tvThongBao = (TextView) alertLayout.findViewById(R.id.tvThongBao);
        final EditText edName = (EditText) alertLayout.findViewById(R.id.edName);

        tvThongBao.setText("You have finished sudoku in: "+txtTimer.getText()+"\n Enter a name to save the achievement");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Notification");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        final DBManager dbManager=new DBManager(GameActivity.this);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                dateFormatter.setLenient(false);
                Date today = new Date();
                String s = dateFormatter.format(today);
                if(edName.getText().toString()=="") {
                    edName.setText("no name");
                }
                Achievements achievements = new Achievements(edName.getText().toString(),txtTimer.getText().toString(),s);
                if(dbManager.addAchievements(achievements))
                {
                    Toast.makeText(GameActivity.this,"Lưu thành công",Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

}
