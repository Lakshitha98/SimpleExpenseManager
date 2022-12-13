package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String db_name="200523J.db";

    public DBHelper(Context context) {
        super(context,db_name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                "CREATE TABLE accounts(" +
                        "acc_no TEXT PRIMARY KEY," +
                        "bank_name TEXT," +
                        "acc_holder TEXT,"+
                        "ini_bal REAL" +
                        ");"
        );
        sqLiteDatabase.execSQL(
                "CREATE TABLE transactions(" +
                        "transactionID INTEGER PRIMARY KEY, " +
                        "date TEXT," +
                        "account_no TEXT," +
                        "type TEXT," +
                        "amount REAL" +
                        ");"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS accounts");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS transactions");

        onCreate(sqLiteDatabase);
    }

    public boolean AddAcount(String acc_num,String bank_name,String owner,double ini_balance){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put("acc_no",acc_num);
        values.put("bank_name",bank_name);
        values.put("acc_holder",owner);
        values.put("ini_bal",ini_balance);

        db.insert("accounts",null,new ContentValues());
        return true;
    }

    public boolean delAccount(String acc_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  values = new ContentValues();
        values.put("account_no",acc_no);
        db.delete("accounts","account_no=?",new String[] {acc_no});
        return true;
    }

    public Cursor getAccount(String acc_no){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM accounts WHERE account_no='"+acc_no+"';",null);
        return res;
    }

    public Cursor getAllAccountNo(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT account_no FROM accounts;",null);
        return result;
    }

    public boolean updateBalance(double amount,String acc_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account_no",acc_no);
        db.update("accounts", values,"account_no=?",new String [] {acc_no});
        return true;
    }

    public Cursor getTransactions(){
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM transactions;",null);
        return result;
    }

    public boolean addTransaction(String acc_no,String type,double amount,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_number",acc_no);
        contentValues.put("type",type);
        contentValues.put("amount",amount);
        contentValues.put("date",date);
        db.insert("transactions",null, contentValues);
        return true;
    }



}
