package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class InMemoryPersistentAccountDAO implements AccountDAO {
    private DBHelper dbHelper;
    private Context context;


    public InMemoryPersistentAccountDAO(DBHelper dbHelper,Context context){
        this.dbHelper=dbHelper;
        this.context=context;
    }
    @Override
    public List<String> getAccountNumbersList() {

        List<String> acc_num_list=new ArrayList<>();
        Cursor cursor=dbHelper.getAllAccountNo();

        if(!cursor.moveToFirst()){
            return acc_num_list;

        }
        do{
            acc_num_list.add(cursor.getString(0));
        }
        while(cursor.moveToNext());
        return acc_num_list;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> acc_list=new ArrayList<>();
        Cursor cursor=dbHelper.getAllAccountNo();
        Account acc;
        if(!cursor.moveToFirst()){
            return acc_list;
        }
        do {
            String acc_num=cursor.getString(0);
            String bank=cursor.getString(1);
            String holder=cursor.getString(2);
            double bal=cursor.getDouble(3);
            acc= new Account(acc_num,bank,holder,bal);
            acc_list.add(acc);
        }
        while (cursor.moveToNext());
        return acc_list;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account acc;
        Cursor cursor=dbHelper.getAccount(accountNo);

        if(cursor.moveToFirst()){
            String acc_num=cursor.getString(0);
            String bank=cursor.getString(1);
            String holder=cursor.getString(2);
            double bal=cursor.getDouble(3);

            acc=new Account(acc_num,bank,holder,bal);
            return acc;
        }
        return null;
    }

    @Override
    public void addAccount(Account account) {
        Cursor cursor= dbHelper.getAccount(account.getAccountNo());

        dbHelper.AddAcount(account.getAccountNo(),account.getBankName(),account.getAccountHolderName(),account.getBalance());
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor=dbHelper.getAccount(accountNo);
        if(!cursor.moveToFirst()){
            String eror="NO ACCOUNTS";
            throw new InvalidAccountException(eror);
        }
        dbHelper.delAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Cursor cursor=dbHelper.getAccount(accountNo);

        double cur_bal=cursor.getDouble(3);
        double new_bal=0;

        if(ExpenseType.EXPENSE==expenseType){
            new_bal=cur_bal-amount;
        }
        else if(ExpenseType.INCOME==expenseType){
            new_bal=cur_bal+amount;
        }

        dbHelper.updateBalance(amount,accountNo);
    }
}
