/*
 * 修改密码
 */
package com.example.idcard.activity;

import com.example.idcard.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class ChangePasswordActivity extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	this.setContentView(R.layout.activity_changepassword);
}
}
