package com.shenjianli.bindertest;

/**
 * Created by shenjianli on 16/11/18.
 */
public interface IBank {

    /*
    开户
     */
    String openAccount(String name,String password);

    String saveMoney(int money,String account);

    String takeMoney(int money,String account,String password);

    String closeAccount(String account,String password);
}
