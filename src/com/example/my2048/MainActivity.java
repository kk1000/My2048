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


	// ��շ���
	public void clearScore(){
		score = 0;
		showScore();
	}
	// �ڽ�������ʾ����
	public void showScore(){
		tvScore.setText(score + "");
	}
	// ���ӷ���
	public void addScore(int s){
		score += s;
		showScore();
	}
	// ȡ�÷���
	public int getScore(){
		return score;
	}
	
	
	
	
	
	
	

	
	private int score = 0;	// ���õķ���
	private int usertime = 0;	// ʱ��
	
	private TextView tvScore;	// ��ʾ�����Ŀؼ�
	private TextView tvusertime;	// ��ʾʱ��Ŀؼ�
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
//					// �����߳����޸����̵߳Ľ���
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
