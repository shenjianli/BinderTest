package com.shenjianli.bindertest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.open_account)
    Button mOpenAccount;
    @Bind(R.id.save_money)
    Button mSaveMoney;
    @Bind(R.id.get_money)
    Button mGetMoney;
    @Bind(R.id.close_account)
    Button mCloseAccount;
    @Bind(R.id.text_info)
    TextView mTextInfo;

    //private BankBinder mBankBinder;

    //Client来保存远程Service端的引用
    private IBankAIDL mBankBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Client进行Service绑定
        Intent intent = new Intent(this,BankService.class);
        intent.setAction("com.shenjianli.aidl.bank.BankService");
        bindService(intent,conn,BIND_AUTO_CREATE);

    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //mBankBinder = (BankBinder) binder;
            //绑定成功后，获得远程Binder的引用
            mBankBinder = IBankAIDL.Stub.asInterface(binder);
            try {
                binder.linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient   deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if(mBankBinder == null){
                return;
            }
            mBankBinder.asBinder().unlinkToDeath(deathRecipient,0);
            mBankBinder = null;
            //TODO 进行重新绑定
        }
    };

    //点击按钮后，调用另一进行中Binder中的方法获得返回值
    @OnClick({R.id.open_account, R.id.save_money, R.id.get_money, R.id.close_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_account:
                try {
                    mTextInfo.setText(mBankBinder.openAccount("shenjianli","cqtddt@163.com"));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.save_money:
                try {
                    mTextInfo.setText(mBankBinder.saveMoney(1000000000,"shenjianli"));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.get_money:
                try {
                    mTextInfo.setText(mBankBinder.takeMoney(100,"shenjianli","cqtddt@163.com"));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.close_account:
                try {
                    mTextInfo.setText(mBankBinder.closeAccount("shenjianli","cqtddt@163.com"));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //进行解绑Service
        unbindService(conn);
    }
}
