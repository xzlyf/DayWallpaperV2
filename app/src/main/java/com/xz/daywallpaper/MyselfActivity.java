package com.xz.daywallpaper;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerButton;
import com.xz.daywallpaper.base.BaseActivity;
import com.xz.daywallpaper.entity.UserInfo;
import com.xz.daywallpaper.utils.TransparentBarUtil;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyselfActivity extends BaseActivity implements View.OnClickListener {
    private Shimmer shimmer;
    private ImageView back;
    private RelativeLayout layout1;
    private EditText userId;
    private EditText userPsd;
    private ShimmerButton loginBtn;
    private TextView registerText;
    private RelativeLayout layout2;
    private TextInputEditText userInputId;
    private TextInputEditText userInputPsd;
    private TextInputEditText userInputRepsd;
    private ShimmerButton registerBtn;
    private CircleImageView userImage;
    private ConstraintLayout layout3;

    /**
     * 登录后用户信息界面
     */
    private ImageView userPhoto;
    private TextView userName;




    @Override
    public int getLayoutResource() {
        return R.layout.activity_myself;
    }

    @Override
    public void findID() {
        back = findViewById(R.id.back);
        layout1 = findViewById(R.id.layout_1);
        userId = findViewById(R.id.user_id);
        userPsd = findViewById(R.id.user_psd);
        loginBtn = findViewById(R.id.login_btn);
        registerText = findViewById(R.id.register_text);
        layout2 = findViewById(R.id.layout_2);
        userInputId = findViewById(R.id.user_input_id);
        userInputPsd = findViewById(R.id.user_input_psd);
        userInputRepsd = findViewById(R.id.user_input_repsd);
        registerBtn = findViewById(R.id.register_btn);
        layout3 = findViewById(R.id.layout_3);
        userImage = findViewById(R.id.user_image);
        userPhoto = findViewById(R.id.user_photo);
        userName = findViewById(R.id.user_name);

        back.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        registerText.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

        shimmer = new Shimmer();
    }

    @Override
    public void init_Data() {
        TransparentBarUtil.makeStatusBarTransparent(this);


    }

    @Override
    public void showData(Object object) {

        if (object instanceof Boolean){
            //登陆失败
            if (!(boolean)object){

                setWidgetEnable(true);
                shimmer.cancel();
                loginBtn.setTextColor(Color.WHITE);
                loginBtn.setText("登陆");

            }else{
                setWidgetEnable(true);
                shimmer.cancel();
                registerBtn.setTextColor(Color.WHITE);
                registerBtn.setText("登陆");

            }
        }else if (object instanceof UserInfo){
            UserInfo info = (UserInfo) object;
            //得到用户信息
            layout1.setVisibility(View.GONE);
            layout3.setVisibility(View.VISIBLE);

            Glide.with(this).load(info.getUserPhoto()).into(userPhoto);
            userName.setText(info.getUserName());

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (layout2.getVisibility()==View.VISIBLE){
                    layout2.setVisibility(View.GONE);
                    layout1.setVisibility(View.VISIBLE);
                }else{
                    finish();
                }
                break;
            case R.id.login_btn:
                Map<String,String> userMap = new HashMap<>();
                userMap.put("name",userId.getText().toString().trim());
                userMap.put("psw",userPsd.getText().toString().trim());
                presenter.login(userMap);

                shimmer.start(loginBtn);
                setWidgetEnable(false);
                loginBtn.setTextColor(Color.parseColor("#757575"));
                loginBtn.setText("正在登陆");

                break;
            case R.id.register_text:
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);

                break;
            case R.id.register_btn:
                shimmer.start(registerBtn);
                setWidgetEnable(false);
                registerBtn.setTextColor(Color.parseColor("#757575"));
                registerBtn.setText("正在加入社区");

                presenter.register();

                break;
            case R.id.user_image:
                break;
        }
    }

    /**
     * 设置控件是否启用
     * @param b
     */
    private void setWidgetEnable(boolean b){
        loginBtn.setEnabled(b);
        userId.setEnabled(b);
        userPsd.setEnabled(b);
        registerBtn.setEnabled(b);
        loginBtn.setEnabled(b);
        userInputId.setEnabled(b);
        userInputPsd.setEnabled(b);
        userInputRepsd.setEnabled(b);
        userImage.setEnabled(b);
    }
}
