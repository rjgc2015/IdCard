/*
 * 好友名片
 */
package com.example.idcard.activity;

import java.security.PublicKey;

import com.example.idcard.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class FriendActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_friendcard);
		Button back = (Button)findViewById(R.id.friendcard_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		ImageButton deletelButton  = (ImageButton) findViewById(R.id.delete);
        deletelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.delete:
					AlertDialog.Builder dialog =new AlertDialog.Builder(FriendActivity.this);
					dialog.setTitle("警告");
					dialog.setMessage("是否删除名片");
					dialog.setCancelable(false);
					dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					dialog.show();
					break;

				default:
					break;
				}

			}
		});
        
        TextView companyTextView = (TextView) findViewById(R.id.companyname1);
        companyTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				//好友名片->公司介绍 CompanyActivity
				Intent intent = new Intent();
				intent.setClass(FriendActivity.this, CompanyActivity.class);
				startActivity(intent);

			}
		});
        		
	}
}
