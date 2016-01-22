/*
 * 账号管理
 */
package com.example.idcard.activity;

import com.example.idcard.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ManageCountActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_managecount);
		Button back = (Button) findViewById(R.id.manage_back);
		Button change = (Button) findViewById(R.id.changepassword);
		Button logout = (Button) findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//账号管理->登陆 LoginActivity
				Intent intent = new Intent(ManageCountActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
		change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//账号管理->修改密码 ,ChangePasswordActivity
				Intent intent = new Intent(ManageCountActivity.this,ChangePasswordActivity.class);
				startActivity(intent);
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
}
