/*
 * 主界面
 */
package com.example.idcard.activity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idcard.R;
import com.example.idcard.until.ListViewCompat;
import com.example.idcard.until.SlideView;
import com.example.idcard.until.SlideView.OnSlideListener;

public class MainActivity extends Activity  implements OnItemClickListener, OnClickListener,
OnSlideListener,OnTouchListener {
	
	public static final int SNAP_VELOCITY = 200;
	private int screenWidth;
	private int leftEdge;
	private int rightEdge = 0;
	private int menuPadding = 100;
	private View content;
	private View menu;
	private LinearLayout.LayoutParams menuParams;
	private float xDown;
	private float xMove;
	private float xUp;
	private boolean isMenuVisible;
	private VelocityTracker mVelocityTracker;
	 private ListViewCompat mListView;
	    SlideAdapter slideAdapter;
	    private List<MessageItem> mMessageItems = new ArrayList<MainActivity.MessageItem>();

	    private SlideView mLastSlideViewWithStatusOn;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_main);
		initValues();
		content.setOnTouchListener(this);  
		 initView();
		ImageButton photo = (ImageButton) this.findViewById(R.id.myidbutton);
		// step 1. create a MenuCreator
		Button countButton = (Button) this.findViewById(R.id.managecount);
		countButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//我的名片->账号管理 ManageCountActivity
				Intent intent = new Intent(MainActivity.this,
						ManageCountActivity.class);
				startActivity(intent);
			}
		});
		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 主界面->我的名片MyidActivity
				Intent intent = new Intent(MainActivity.this,
						MyidActivity.class);
				startActivity(intent);
			}
		});
	}
	 private void initView() {
	        mListView = (ListViewCompat) findViewById(R.id.list);

	        for (int i = 0; i < 1; i++) {
	            MessageItem item = new MessageItem();
	            StringBuffer buffer = new StringBuffer();
				try {
					FileInputStream fis = new FileInputStream(
							"/sdcard/myfile.txt");
					InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
					// Unicode,UTF-8,ASCII,GB2312,Big5
					Reader in = new BufferedReader(isr);
					int ch;
					while ((ch = in.read()) > -1) {
						buffer.append((char) ch);
					}
					in.close();
//					textView.setText(buffer.toString());
				} catch (IOException e) {
//					textView.setText("文件不存在!");
				}
	                item.iconRes = R.drawable.default_qq_avatar;
	                item.msg = "青岛爆炸满月：大量鱼虾死亡";
	               
	            
	            mMessageItems.add(item);
	        }
	        
//	        footer = LayoutInflater.from(this).inflate(R.layout.footer, null);
//	        mListView.addFooterView(footer);
	        slideAdapter = new SlideAdapter();
	        mListView.setAdapter(slideAdapter);
	        mListView.setOnItemClickListener(this);
	    }

	    private class SlideAdapter extends BaseAdapter {

	        private LayoutInflater mInflater;

	        SlideAdapter() {
	            super();
	            mInflater = getLayoutInflater();
	        }

	        @Override
	        public int getCount() {
	            return mMessageItems.size();
	        }

	        @Override
	        public Object getItem(int position) {
	            return mMessageItems.get(position);
	        }

	        @Override
	        public long getItemId(int position) {
	            return position;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            ViewHolder holder;
	            SlideView slideView = (SlideView) convertView;
	            if (slideView == null) {
	                View itemView = mInflater.inflate(R.layout.list_item, null);

	                slideView = new SlideView(MainActivity.this);
	                slideView.setContentView(itemView);

	                holder = new ViewHolder(slideView);
	                slideView.setOnSlideListener(MainActivity.this);
	                slideView.setTag(holder);
	            } else {
	                holder = (ViewHolder) slideView.getTag();
	            }
	            MessageItem item = mMessageItems.get(position);
	            item.slideView = slideView;
	            item.slideView.reset();
	            holder.icon.setImageResource(item.iconRes);
	            holder.title.setText(item.title);
	            holder.msg.setText(item.msg);
	            holder.time.setText(item.time);
	            holder.leftHolder.setOnClickListener(MainActivity.this);
	            holder.rightHolder.setOnClickListener(MainActivity.this);
	            return slideView;
	        }

	    }

	    public class MessageItem {
	        public int iconRes;
	        public String title;
	        public String msg;
	        public String time;
	        public SlideView slideView;
	    }

	    private static class ViewHolder {
	        public ImageView icon;
	        public TextView title;
	        public TextView msg;
	        public TextView time;
	        public ViewGroup leftHolder;
	        public ViewGroup rightHolder;

	        ViewHolder(View view) {
	            icon = (ImageView) view.findViewById(R.id.icon);
	            title = (TextView) view.findViewById(R.id.title);
	            msg = (TextView) view.findViewById(R.id.msg);
	            time = (TextView) view.findViewById(R.id.time);
	            leftHolder = (ViewGroup)view.findViewById(R.id.left_holder);
	            rightHolder = (ViewGroup)view.findViewById(R.id.right_holder);
	        }
	    }

	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position,
	            long id) {
	        SlideView slideView =  mMessageItems.get(position).slideView;
	        if(slideView.ismIsMoveClick()){//如果是滑动中触发的点击事件，不做处理
	        	return;
	        }
	        if (slideView.close() && slideView.getScrollX() == 0) {
				if (mLastSlideViewWithStatusOn == null || mLastSlideViewWithStatusOn.getScrollX() == 0) {
					//此处添加item的点击事件
					Toast.makeText(this, "onItemClick position=" + position, 100).show();
				}
			}
	    }

	    @Override
	    public void onSlide(View view, int status) {
	        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
	            mLastSlideViewWithStatusOn.shrink();
	        }

	        if (status == SLIDE_STATUS_ON) {
	            mLastSlideViewWithStatusOn = (SlideView) view;
	        }
	    }

	    @Override
	    public void onClick(View v) {
	    	if (v.getId() == R.id.left_holder) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("提示").setIcon(R.drawable.ic_launcher).setMessage("确定删此条目？")  
						.setNegativeButton("取消", null);
				builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mMessageItems.remove(mListView.getPosition());
						slideAdapter.notifyDataSetChanged();
					}
				});
				builder.show();
	        }else if (v.getId() == R.id.right_holder) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("提示").setIcon(R.drawable.ic_launcher).setMessage("确定删此条目？")  
						.setNegativeButton("取消", null);
				builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mMessageItems.remove(mListView.getPosition());
						slideAdapter.notifyDataSetChanged();
					}
				});
				builder.show();
	        }
	    }
	
	private void initValues() {
		WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		screenWidth = window.getDefaultDisplay().getWidth();
		content = findViewById(R.id.content);
		menu = findViewById(R.id.menu);
		menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
		
		menuParams.width = screenWidth - menuPadding;
		
		leftEdge = -menuParams.width;
		
		menuParams.leftMargin = leftEdge;
		
		content.getLayoutParams().width = screenWidth;
	}

	public boolean onTouch(View v, MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			xDown = event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			
			xMove = event.getRawX();
			int distanceX = (int) (xMove - xDown);
			if (isMenuVisible) {
				menuParams.leftMargin = distanceX;
			} else {
				menuParams.leftMargin = leftEdge + distanceX;
			}
			if (menuParams.leftMargin < leftEdge) {
				menuParams.leftMargin = leftEdge;
			} else if (menuParams.leftMargin > rightEdge) {
				menuParams.leftMargin = rightEdge;
			}
			menu.setLayoutParams(menuParams);
			break;
		case MotionEvent.ACTION_UP:
			
			xUp = event.getRawX();
			if (wantToShowMenu()) {
				if (shouldScrollToMenu()) {
					scrollToMenu();
				} else {
					scrollToContent();
				}
			} else if (wantToShowContent()) {
				if (shouldScrollToContent()) {
					scrollToContent();
				} else {
					scrollToMenu();
				}
			}
			recycleVelocityTracker();
			break;
		}
		return true;
	}

	
	private boolean wantToShowContent() {
		return xUp - xDown < 0 && isMenuVisible;
	}

	
	private boolean wantToShowMenu() {
		return xUp - xDown > 0 && !isMenuVisible;
	}

	
	private boolean shouldScrollToMenu() {
		return xUp - xDown > screenWidth / 2
				|| getScrollVelocity() > SNAP_VELOCITY;
	}

	
	private boolean shouldScrollToContent() {
		return xDown - xUp + menuPadding > screenWidth / 2
				|| getScrollVelocity() > SNAP_VELOCITY;
	}

	
	private void scrollToMenu() {
		new ScrollTask().execute(30);
	}

	
	private void scrollToContent() {
		new ScrollTask().execute(-30);
	}

	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = menuParams.leftMargin;
			
			while (true) {
				leftMargin = leftMargin + speed[0];
				if (leftMargin > rightEdge) {
					leftMargin = rightEdge;
					break;
				}
				if (leftMargin < leftEdge) {
					leftMargin = leftEdge;
					break;
				}
				publishProgress(leftMargin);
				
				sleep(20);
			}
			if (speed[0] > 0) {
				isMenuVisible = true;
			} else {
				isMenuVisible = false;
			}
			return leftMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
			menuParams.leftMargin = leftMargin[0];
			menu.setLayoutParams(menuParams);
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			menuParams.leftMargin = leftMargin;
			menu.setLayoutParams(menuParams);
		}
	}

	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	

}
