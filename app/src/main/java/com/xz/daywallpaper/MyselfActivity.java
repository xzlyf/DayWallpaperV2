package com.xz.daywallpaper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.xz.daywallpaper.base.BaseActivity;

public class MyselfActivity extends BaseActivity {
    private EditText userId;
    private EditText userPsd;
    private Button loginBtn;
    private Button registerBtn;



    @Override
    public int getLayoutResource() {
        return R.layout.activity_myself;
    }

    @Override
    public void findID() {
        userId = findViewById(R.id.user_id);
        userPsd = findViewById(R.id.user_psd);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);
    }

    @Override
    public void init_Data() {

    }

    @Override
    public void showData(Object object) {

    }
}
