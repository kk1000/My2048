package com.example.my2048;

import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public MainActivity() {
		mainActivity = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvScore = (TextView) findViewById(R.id.tvScore);
		tvusertime = (TextView) findViewById(R.id.my_usertime);
		
//		TimeThread timethread = new TimeThread();
//		timethread.run();
	}


	// 清空分数
	public void clearScore(){
		score = 0;
		showScore();
	}
	// 在界面上显示分数
	public void showScore(){
		tvScore.setText(score + "");
	}
	// 增加分数
	public void addScore(int s){
		score += s;
		showScore();
	}
	// 取得分数
	public int getScore(){
		return score;
	}
	
	
	
	
	
	
	

	
	private int score = 0;	// 所得的分数
	private int usertime = 0;	// 时间
	
	private TextView tvScore;	// 显示分数的控件
	private TextView tvusertime;	// 显示时间的控件
	private static MainActivity mainActivity = null;
	
	public static MainActivity getMainActivity() {
		return mainActivity;
	}
	
//	class TimeThread extends Thread{
//		
//		@Override
//		public void run() {
//			for(int i=0; i<100; i++){
//				try {
//					sleep(1000);
//					usertime++;
//					// 在子线程中修改主线程的界面
//					runOnUiThread(new Runnable() {
//						
//						@Override
//						public void run() {
//							tvusertime.setText(usertime+"");
//						}
//					});
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
//	}

	
	

}
