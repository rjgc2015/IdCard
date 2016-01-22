/*
 * 注册界面 
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.idcard.R;
import com.example.idcard.until.ToastUtil;

public class ResignerActivity extends Activity {
	private static final int FILE_SELECT_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_resigner);
		Button sure = (Button) this.findViewById(R.id.makesure);
		Button back = (Button) this.findViewById(R.id.resigner_back);
		ImageButton checkButton = (ImageButton) this.findViewById(R.id.checkphoto);
		final EditText editphone = (EditText) this
				.findViewById(R.id.editphonenumber);
		final EditText editpassword = (EditText) this
				.findViewById(R.id.editpassword);
		final EditText makesurepassword = (EditText) this
				.findViewById(R.id.editmakesure);
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone = editphone.getText().toString().trim();
				String password = editpassword.getText().toString().trim();
				String makesure = makesurepassword.getText().toString().trim();
				if (phone.length() <= 0 || password.length() <= 0) {
					ToastUtil.show(ResignerActivity.this, "账号或密码不能为空");
				} 
				 else if (password.equals(makesure) && phone != null
						&& password != null) {
					ToastUtil.show(ResignerActivity.this, "注册成功");
				}
				 else  {
						ToastUtil.show(ResignerActivity.this, "两次密码不一致");
				 }
			}

		});
		checkButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("*/*");
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				try {
					startActivityForResult(Intent.createChooser(intent,
							"Select a File to Upload"), FILE_SELECT_CODE);
				} catch (android.content.ActivityNotFoundException ex) {
					ToastUtil.show(getApplicationContext(), "Please install a File Manager.");
				}

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
