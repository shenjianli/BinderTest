package com.shenjianli.bindertest;

import java.util.UUID;

/**
 * Created by shenjianli on 16/11/18.
 * public class BankBinder extends Binder implements IBank
 *  利用AIDL生成的类来实现远程Binder并传递参数
 */
public class BankBinder extends IBankAIDL.Stub{
    @Override
    public String openAccount(String name, String password) {
        String name1 = Thread.currentThread().getName();
        return name + "开户成功！账号为：" + UUID.randomUUID().toString() + "当前线程：" + name1;
    }

    @Override
    public String saveMoney(int money, String account) {
        return "账户：" + account + " 存入 " + money + "单位  人民币";
    }

    @Override
    public String takeMoney(int money, String account, String password) {
        return "账户" + account + " 支取 " + money + " 单位  人民币";
    }

    @Override
    public String closeAccount(String account, String password) {
        return account + "销户成功！";
    }
}
