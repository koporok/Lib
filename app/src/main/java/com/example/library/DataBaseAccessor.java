package com.example.library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseAccessor extends SQLiteOpenHelper {
    // Основные данные базы
    private static final String DATABASE_NAME = "db.db";
    private static final int DB_VERSION = 3;

    // таблицы
    public static final String TABLE_BOOK = "BOOK";

    // столбцы таблицы Book
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_THEME = "theme";
    public static final String COLUMN_AUTHOR = "author";

    public DataBaseAccessor(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создать таблицу
        db.execSQL("CREATE TABLE " + TABLE_BOOK + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_THEME + " TEXT,"
                + COLUMN_AUTHOR + " TEXT);");

        // Добавить пару записей в таблицу
        db.execSQL("INSERT INTO " + TABLE_BOOK + "(" + COLUMN_THEME + ", "+ COLUMN_AUTHOR +") values('theme 1','book')");
        db.execSQL("INSERT INTO " + TABLE_BOOK + "(" + COLUMN_THEME + ", "+ COLUMN_AUTHOR +") values('theme 2','book')");
    }

    // Остальной код...

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // Удалить старую таблицу
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            // Создать новую таблицу и заполнить ее
            onCreate(db);
        }
    }
}
