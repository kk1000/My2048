package com.example.my2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
/**
 * 卡片类
 * @author Administrator
 *
 */
public class Card extends FrameLayout {

	public Card(Context context) {
		super(context);
		
		label = new TextView(getContext());
		label.setTextSize(32);
		label.setBackgroundColor(0x33ffffff);
		label.setGravity(Gravity.CENTER);
		
		LayoutParams lp = new LayoutParams(-1,-1);
		lp.setMargins(10, 10, 0, 0);	// 设置文字卡片离其它卡片的距离，设置了左边和上边相差10
		addView(label, lp);
		
		setNum(0);
	}
	
	private int num = 0;	// 卡片上的数字
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
		// 如果添加的是0，那么就代表该卡片是空的
		if(num <= 0){
			label.setText("");
		}else{
			label.setText(num+"");
		}
	}
	
	// 重写equals方法，使它能够判断两个卡片的数是否相等
	public boolean equals(Card o) {
		return getNum() == o.getNum();
	}

	private TextView label;
	

}
