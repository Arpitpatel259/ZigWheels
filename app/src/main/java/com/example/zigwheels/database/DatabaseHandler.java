package com.example.zigwheels.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zigwheels.models.VehicalModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSON = 1;
    private static final String DATABASE_NAME = "ZigWheels";

    private static final String TABLE_CART = "cart";
    private static final String KEY_ID = "id";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_NAME = "name";
    private static final String KEY_COMPANY = "company";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_LAUNCHYEAR = "launchyear";
    private static final String KEY_PRICE = "price";

    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSON);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART +
                "(" + KEY_ID + "  TEXT PRIMARY KEY , " +
                KEY_CATEGORY + " TEXT, " +
                KEY_NAME + " TEXT, " +
                KEY_COMPANY + " TEXT, " +
                KEY_DESCRIPTION + " TEXT, " +
                KEY_IMAGE + " TEXT, " +
                KEY_LAUNCHYEAR + " TEXT, " +
                KEY_PRICE + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_CART_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public void addToCart(VehicalModel vehicalModel) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_ID, vehicalModel.getId());
        contentValues.put(KEY_CATEGORY, vehicalModel.getCategory());
        contentValues.put(KEY_NAME, vehicalModel.getModal());
        contentValues.put(KEY_COMPANY, vehicalModel.getCompany());
        contentValues.put(KEY_IMAGE, vehicalModel.getImage());
        contentValues.put(KEY_DESCRIPTION, vehicalModel.getDescription());
        contentValues.put(KEY_LAUNCHYEAR, vehicalModel.getLaunchyear());
        contentValues.put(KEY_PRICE, vehicalModel.getPrice());

        sqLiteDatabase.insert(TABLE_CART, null, contentValues);
        sqLiteDatabase.close();


    }

    public List<VehicalModel> getCartItems() {

        String selectQuery = " SELECT * FROM " + TABLE_CART;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        List<VehicalModel> cartItems = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                VehicalModel vehicalModel = new VehicalModel();
                vehicalModel.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                vehicalModel.setModal(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                vehicalModel.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
                vehicalModel.setCompany(cursor.getString(cursor.getColumnIndex(KEY_COMPANY)));
                vehicalModel.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                vehicalModel.setImage(cursor.getString(cursor.getColumnIndex(KEY_IMAGE)));
                vehicalModel.setLaunchyear(cursor.getString(cursor.getColumnIndex(KEY_LAUNCHYEAR)));
                vehicalModel.setPrice(cursor.getString(cursor.getColumnIndex(KEY_PRICE)));
                cartItems.add(vehicalModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }

    public void deleteCart()
    {
        String deleteQuery = "DELETE FROM " + TABLE_CART;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(deleteQuery);
    }
    public void removeItem(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_CART, KEY_ID + " = ?", new String[]{id});
        sqLiteDatabase.close();
    }
}
