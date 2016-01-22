/*
 * 我的名片
 */
package com.example.idcard.activity;

import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.idcard.R;

public class MyidActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_myid);
		Button back = (Button)findViewById(R.id.myid_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

        Button changeButton  = (Button) findViewById(R.id.change);
        final EditText name = (EditText) findViewById(R.id.editname);
        final EditText post = (EditText) findViewById(R.id.editpost);
        changeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 我的名片->公司情况编辑 CompanyEditActivity
//				Intent intent = new Intent();
//				intent.setClass(MyidActivity.this, CompanyEditActivity.class);
//				startActivity(intent);
				writeFileSdcard("/sdcard/myfile.txt",name.getText().toString()+"\n"+post.getText().toString());

			}
		});
        TextView companyTextView = (TextView) findViewById(R.id.companyname);
        companyTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// 我的名片->公司情况简介 CompanyActivity
				Intent intent = new Intent();
				intent.setClass(MyidActivity.this, CompanyActivity.class);
				startActivity(intent);

			}
		});
        /**
         * 启动发送名片按钮
         */
        Button btnSendCard=(Button)findViewById(R.id.button_send);
        btnSendCard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MyidActivity.this,WiFiDirectActivity.class);
				startActivity(intent);
				
				
			}
		});
        		
	}
	public void writeFileSdcard(String fileName, String message) {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
