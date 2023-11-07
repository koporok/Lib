package com.example.library;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class BookEditActivity extends AppCompatActivity {

    EditText ThemeEditText;
    EditText BookEditText;

    int Position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);
        ThemeEditText = findViewById(R.id.themeEditText);
        BookEditText = findViewById(R.id.bookEditText);

        Intent fromMainActivityIntent = getIntent();

        ThemeEditText.setText(fromMainActivityIntent.getExtras().getString(MainActivity.KEY_THEME));
        BookEditText.setText(fromMainActivityIntent.getExtras().getString(MainActivity.KEY_BOOK_TEXT));
        Position = fromMainActivityIntent.getIntExtra(MainActivity.KEY_POSITION,-1);

        if(Position == -1)
        {
            Log.d("Note activity","Invalid position");
        }


    }

    public void OnBackButtonClick(View view)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(MainActivity.KEY_THEME,ThemeEditText.getText().toString());
        returnIntent.putExtra(MainActivity.KEY_BOOK_TEXT, BookEditText.getText().toString());
        returnIntent.putExtra(MainActivity.KEY_POSITION,Position);
        setResult(RESULT_OK,returnIntent);
        finish();

    }
}