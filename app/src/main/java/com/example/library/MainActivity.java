package com.example.library;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContentInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
    final public static String KEY_THEME = "theme";
    final public static String KEY_BOOK_TEXT = "book";
    final public static String KEY_POSITION = "position";

    ListView ThemesListView;

    SimpleCursorAdapter bookAdapter;
    DataBaseAccessor db;

    // создание launcher для получения данных из дочерней активити
    ActivityResultLauncher<Intent> BooksLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // все ли хорошо при получении данных из дочерней активити?
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        //получить данные
                        Intent returnedIntent = result.getData();
                        int id = returnedIntent.getIntExtra(KEY_POSITION,-1);
                        String theme = returnedIntent.getStringExtra(KEY_THEME);
                        String book = returnedIntent.getStringExtra(KEY_BOOK_TEXT);

                        //обновить БД и интерфейс
                        db.updateBook(id,theme,book);
                        bookAdapter = AdapterUpdate();
                    }
                    else
                    {
                        Log.d("MainActivity" ,"Invalid note activity result");
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // создать аксессор к бд
        db = new DataBaseAccessor(this);

        setContentView(R.layout.activity_main);
        ThemesListView = findViewById(R.id.ListView);

        bookAdapter = AdapterUpdate();
        Intent BookIntent = new Intent(this, BookEditActivity.class);

        // обработка клика по listView
        ThemesListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                //Добыть данные из адаптера
                String theme = ((Cursor) bookAdapter.getItem(position)).getString(1);
                String book = ((Cursor) bookAdapter.getItem(position)).getString(2);
                //отправить данные в дочернюю акливити
                BookIntent.putExtra(KEY_THEME, theme);
                BookIntent.putExtra(KEY_BOOK_TEXT, book);

                //id - идентификатор записи в БД
                //без приведения к int перидется и получать long а я не хотел переписывать дочернюю активити
                BookIntent.putExtra(KEY_POSITION, (int)id);
                //запустить дочернюю активити
                BooksLauncher.launch(BookIntent);
            }
        });
    }

    /**
     * Обновляет listView путем установки нового адаптера
     * @return Адаптер для обновления listView
     */
    private SimpleCursorAdapter AdapterUpdate() {
        // получить адаптер из класса
        SimpleCursorAdapter adapter = db.getCursorAdapter(this,
                android.R.layout.two_line_list_item, // Разметка одного элемента ListView
                new int[]{android.R.id.text1,android.R.id.text2}); // текст этого элемента

        // установить адаптер в listview
        ThemesListView.setAdapter(adapter);
        return adapter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // закрыть БД
        db.close();
    }}
    /*public void onAddButtonClick(View view){
        ContentValues cv = new ContentValues();
        cv.put(DataBaseAccessor.COLUMN_THEME, ThemesListView.toString());
        cv.put(DataBaseAccessor.TABLE_BOOK, ThemesListView.toString());

    }
}*/