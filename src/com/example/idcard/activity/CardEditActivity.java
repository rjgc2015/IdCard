/*
 * 名片编辑
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

public class CardEditActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_cardedit);
		Button back = (Button) findViewById(R.id.myid_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
        Button addButton  = (Button) findViewById(R.id.addcompany);
        addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 名片编辑->公司编辑 CompanyEditActivity
				Intent intent = new Intent();
				intent.setClass(CardEditActivity.this, CompanyEditActivity.class);
				startActivity(intent);

			}
		});
        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 名片编辑->我的名片 MyidActivity
				Intent intent = new Intent();
				intent.setClass(CardEditActivity.this,MyidActivity.class);
				startActivity(intent);
			}
		});
	}
}