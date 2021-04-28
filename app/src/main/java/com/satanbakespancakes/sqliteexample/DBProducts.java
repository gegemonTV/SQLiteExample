package com.satanbakespancakes.sqliteexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Path;

import java.util.ArrayList;

public class DBProducts {
    // Данные базы данных и таблиц
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "ProductsData";

    // Название столбцов
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PRICE = "Price";
    private static final String COLUMN_NAME = "Name";

    // Номера столбцов
    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_PRICE = 2;
    private static final int NUM_COLUMN_NAME = 1;

    private SQLiteDatabase mDB;

    public DBProducts(Context context){
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDB = mOpenHelper.getWritableDatabase();
    }

    public long insert(String name, long price){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_PRICE, price);
        return mDB.insert(TABLE_NAME, null, cv);
    }

    public int update(Product md){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, md.getName());
        cv.put(COLUMN_PRICE, md.getPrice());
        return mDB.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(md.getId())});
    }

    public void deleteAll(){
        mDB.delete(TABLE_NAME,null,null);
    }

    public void delete(long id){
        mDB.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Product select(long id) {
        Cursor mCursor = mDB.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        ArrayList<Product> arr = new ArrayList<>();
        mCursor.moveToFirst();
        String name = mCursor.getString(NUM_COLUMN_NAME);
        long price = mCursor.getLong(NUM_COLUMN_PRICE);
        return new Product(id, name, price);

    }

    public ArrayList<Product> selectAll(){
        Cursor cursor = mDB.query(TABLE_NAME, null,null,null,null,null,null);

        ArrayList<Product> list = new ArrayList<>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            do{
                long id = cursor.getLong(NUM_COLUMN_ID);
                String name = cursor.getString(NUM_COLUMN_NAME);
                long price = cursor.getLong(NUM_COLUMN_PRICE);
                list.add(new Product(id, name, price));
            } while (cursor.moveToNext());
        }
        return list;
    }
}
