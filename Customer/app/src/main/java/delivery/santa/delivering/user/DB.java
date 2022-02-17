package delivery.santa.delivery.customer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "CartManager";
    private static final String TABLE_CART = "Cart";
    private SQLiteDatabase db;

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String AMOUNT = "amount";
    private static final String PRICE = "price";
    private static final String QUANTITY = "qty";
    private static final String VENDORS = "vendor";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        String CREATE_CART_TABLE="CREATE TABLE "+TABLE_CART+ " ( " + ID + " INTEGER PRIMARY KEY, " + VENDORS +" TEXT, " + NAME + " TEXT, "
                + PRICE +" TEXT, " + QUANTITY + " TEXT, "+AMOUNT+" TEXT "+" ) ";
        db.execSQL(CREATE_CART_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public long addItem(Item item)
    {
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(VENDORS, item.getVendor());
        values.put(NAME, item.getName());
        values.put(PRICE, item.getPrice());
        values.put(QUANTITY, item.getQuantity());
        values.put(AMOUNT, item.getAmount());
        long rowID=db.insert(TABLE_CART,null,values);
        db.close();
        return rowID;
    }

    public void updateItem(int id, String qty,String amt)
    {
        db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_CART + " SET " + QUANTITY + " = ? WHERE " + ID + " = " + id,
                new String[]{
                        qty
                });
        db.execSQL("UPDATE " + TABLE_CART + " SET " + AMOUNT + " = ? WHERE " + ID + " = " + id,
                new String[]{
                        amt
                });
    }

    public int deleteItem(int id)
    {
        db=this.getWritableDatabase();
        return db.delete(TABLE_CART, ID + " = " + id, null );
    }

    public void deleteAllItems()
    {
        db=this.getWritableDatabase();
        String query="DELETE FROM "+TABLE_CART;
        db.execSQL(query);
        db.close();
    }

    public int getID(String name, String vendor)
    {
        int id = -1;
        db = this.getReadableDatabase();
        String selectQuery = "SELECT "+ ID +" FROM " + TABLE_CART + " WHERE " + NAME + " = '" +name+"' AND "+VENDORS+" = '"+vendor+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return id;
    }

    public Cursor getItemDetail(int id)
    {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CART + " WHERE " + ID + " = " +id;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }

    public Cursor getAllItems()
    {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CART;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }

}
