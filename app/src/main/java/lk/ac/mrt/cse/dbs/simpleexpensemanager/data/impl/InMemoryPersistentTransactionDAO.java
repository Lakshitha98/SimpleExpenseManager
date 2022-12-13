package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class InMemoryPersistentTransactionDAO implements TransactionDAO {
    private final DBHelper dbHelper;

    public InMemoryPersistentTransactionDAO(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Cursor res=dbHelper.getAccount(accountNo);
        if(!res.moveToFirst()){
            System.out.println("error");
            return;

        }
        double cur_bal=res.getDouble(3);
        if(ExpenseType.EXPENSE==expenseType && (cur_bal-amount)<0){
            System.out.println("error");
            return;
        }
        DateFormat fmt=new SimpleDateFormat("yyyy-mm-dd");
        String sdate=fmt.format(date);

        dbHelper.addTransaction(accountNo,expenseType.name(),amount,sdate);

    }

    @Override
    public List<Transaction> getAllTransactionLogs(){
        List<Transaction> tr_list=new ArrayList<>();
        Transaction tr;

        Cursor cursor=dbHelper.getTransactions();
        do{
            Date date=null;
            String ssdate=cursor.getString(1);
            try{
                date = new SimpleDateFormat("yyyy-mm-dd").parse(ssdate);
            }
            catch (ParseException e){
                e.printStackTrace();
            }
            ExpenseType exp=ExpenseType.valueOf(cursor.getString(3));
            String acc_num=cursor.getString(2);
            double amount=cursor.getDouble(4);
            tr=new Transaction(date,acc_num,exp,amount);
            tr_list.add(tr);
        }
        while (cursor.moveToNext());
        return tr_list;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> tr_list=getAllTransactionLogs();
        if(limit>=tr_list.size()){
            return tr_list;
        }
        return tr_list.subList((limit-tr_list.size()),tr_list.size());
    }
}
