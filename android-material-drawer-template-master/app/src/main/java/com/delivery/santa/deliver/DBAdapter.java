package com.delivery.santa.deliver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="StudentsDetails";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_VENDORS="vendorlist";
    private static final String TABLE_ORDER="ordersList";
    private Context context;
    private static final String KEY_ID="id";
    private static final String KEY_NAME="name";
    private static final String AMOUNT="amount";
    private static final String CUSTOMER="customer";
    private static final String BILL="bill";
    private static final String BAGS="bags";
    private static final String KEY_OID="orderno";
    private static final String CONTACT="contact";
    private static final String CODE="code";


    public DBAdapter(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
        this.context=context;
    }

    public Cursor getInsertedData() {
        // TODO Auto-generated method stub
        SQLiteDatabase db=this.getReadableDatabase();
        return db.query(TABLE_VENDORS,new String[] {KEY_NAME,AMOUNT},null,null,null,null,null);
    }

    void insert(String name, String amount){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_NAME, name);
        values.put(AMOUNT,amount);
        db.insert(TABLE_VENDORS, null, values);
        db.close();
    }

    void insertData(String id, String cus, String bill, String bags, String contact, String code)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_OID, id);
        values.put(CUSTOMER,cus);
        values.put(BILL, bill);
        values.put(BAGS,bags);
        values.put(CONTACT, contact);
        values.put(CODE, code);
        db.insert(TABLE_ORDER, null, values);
        db.close();
    }

    public Cursor fetchData() {
        // TODO Auto-generated method stub
        SQLiteDatabase db=this.getReadableDatabase();
        return db.query(TABLE_ORDER,new String[] {KEY_OID,CUSTOMER,BILL,BAGS,CONTACT,CODE},null,null,null,null,null);
    }

    void deleteData(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_ORDER, null, null);
        db.close();
    }

    void delete(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        String where = "name='" + name + "'";
        db.delete(TABLE_VENDORS, where, null);
        db.close();
    }


    public void clearData(){
        context.deleteDatabase(DATABASE_NAME);
    }
    @Override
    public void onCreate(SQLiteDatabase arg0)
    {
        // TODO Auto-generated method stub
        String CREATE_CONTACTS_TABLE="CREATE TABLE "+TABLE_VENDORS+"("+KEY_ID+" INTEGER PRIMARY KEY,"+KEY_NAME+" TEXT, "+AMOUNT+" TEXT"+")";
        arg0.execSQL(CREATE_CONTACTS_TABLE);
        String CREATE_ORDERS_TABLE="CREATE TABLE "+ TABLE_ORDER+"("+KEY_OID+" TEXT, "+CUSTOMER+" TEXT, "+BILL+" TEXT, "+BAGS+" TEXT, "
                +CONTACT+" TEXT, "+CODE+" TEXT"+")";
        arg0.execSQL(CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int oldversion, int newversion) {
        // TODO Auto-generated method stub
        arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDORS);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        onCreate(arg0);

    }

}
