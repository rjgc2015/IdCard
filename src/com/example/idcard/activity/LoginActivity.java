/*
 * 登陆
 */
package com.example.idcard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.idcard.R;
import com.example.idcard.until.ToastUtil;

public class LoginActivity extends Activity {
	private String name1="1";
	private String word="1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_login);
		TextView textViewForgetPassword = (TextView) this
				.findViewById(R.id.forget);
		Button button = (Button) this.findViewById(R.id.login);
		TextView textViewresigner = (TextView) this.findViewById(R.id.register);
		final EditText count = (EditText) this
				.findViewById(R.id.zhanghao);
		final EditText pass = (EditText) this
				.findViewById(R.id.mima);
		textViewForgetPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//登陆->忘记密码 ForgetActivity
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, ForgetActivity.class);
				startActivity(intent);
			}
		});
		textViewresigner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//登陆->注册 ResignerActivity
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, ResignerActivity.class);
				startActivity(intent);

			}
		});
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
              String count1 = count.getText().toString().trim();
              String pass1 = pass.getText().toString().trim();
              if (count1.length() <= 0 || pass1.length() <= 0) {
					ToastUtil.show(LoginActivity.this, "账号密码不能为空");
				} 
              else if (count1.equals(name1) && pass1.equals(word)) {
            	  //登陆->主界面MainActivity
            	  Intent intent = new Intent();
  				intent.setClass(LoginActivity.this, MainActivity.class);
  				startActivity(intent);
  				finish();
				}
              else{
            	  ToastUtil.show(LoginActivity.this, "账号密码错误");
              }
			}
		});
	}

}
