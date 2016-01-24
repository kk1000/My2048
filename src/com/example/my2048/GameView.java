package com.example.my2048;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;




public class GameView extends GridLayout {

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initGameView();
	}

	// xml创建view时。多了一个AttributeSet类型的参数，在通过布局文件xml创建一个view时，这个参数会将xml里设定的属性传递给构造函数。如果你采用xml inflate的方法却没有在code里实现该 方法，那么运行时就会报错。
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}

	// 代码创建view时。如果你只打算用code动态创建一个view而不使用布局文件xml inflate
	public GameView(Context context) {
		super(context);
		initGameView();
	}

	// 进入界面的初始化方法
	private void initGameView(){

		this.setColumnCount(4);	// 设置为4*4列的布局
		this.setBackgroundColor(0xffbbada0);

		// 设置对点击事件的监听，判断用户点击的动机
		this.setOnTouchListener(new View.OnTouchListener() {

			private float startX, startY, offsetX, offsetY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();

					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;

					// 进行方位判断，上下或者左右
					if(Math.abs(offsetX) > Math.abs(offsetY)){	// 向左右滑动
						if(offsetX < -5){	// 向左滑动
							swipeLeft();
						} else if(offsetX > 5){	// 向右滑动
							swipeRight();
						}
					} else {	// 向上下滑动
						if(offsetY < -5){	// 向上滑动
							swipeUp();
						} else if(offsetY > 5){	// 向下滑动
							swipeDown();
						}
					}

					break;

				default:
					break;
				}

				return true;
			}
		});

	}// end initGameView()


	// 这个方法是当前的View发生改变的时候调用，w、h代表当前的宽高，oldw、oldh代表改变之前的宽高
	// 该方法会在onCreate之后，onDraw之前调用
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 对宽高取最小值
		int cardWidth = (Math.min(w, h) - 10) / 4;

		addCard(cardWidth, cardWidth);	// 因为卡片是正方形的，所以宽高一样
		// 开始游戏
		startGame();
	}
	// 添加卡片
	private void addCard(int cardWidth, int cardHeight){

		Card c = null;

		for(int y=0; y<4; y++){
			for(int x=0; x<4; x++){
				c = new Card(getContext());
				c.setNum(0);	// 刚开始全部添加为0，即为空卡片
				addView(c, cardWidth, cardHeight);

				cardsMap[x][y] = c;	// 记录该网格当前的卡片
			}
		}
	}
	// 开始游戏
	private void startGame(){
		// 清理分数
		MainActivity.getMainActivity().clearScore();

		// 清理界面上的所有数
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cardsMap[x][y].setNum(0);
			}
		}
		// 添加两个随机数
		addRandomNum();
		addRandomNum();
	}

	// 添加一个随机数
	private void addRandomNum(){
		emptyPoints.clear();

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				// 该位置必须为0
				if(cardsMap[x][y].getNum() <= 0){
					emptyPoints.add(new Point(x, y));
				}
			}
		}// end for
		// 随机取出一个卡片，并且移除那个卡片
		Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
		// 在移除掉的那个点上添加2或者4，2和4出现的概率是9:1
		cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
	}

	private void swipeLeft(){

		boolean merge = false;

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {

				for (int x1 = x+1; x1 < 4; x1++) {
					if(cardsMap[x1][y].getNum() > 0){
						if(cardsMap[x][y].getNum() <= 0){
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);

							x--;
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);

							// 在合并的时候增加分数
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}// end x1 for

			}
		}
		// 如果有动作，那么就添加一个新的随机数，再判断是否满足结束游戏的条件
		if(merge){
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeRight(){
		boolean merge = false;
		for (int y = 0; y < 4; y++) {
			for (int x = 3; x >= 0; x--) {

				for (int x1 = x-1; x1 >= 0; x1--) {
					if(cardsMap[x1][y].getNum() > 0){
						if(cardsMap[x][y].getNum() <= 0){
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);

							x++;
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);

							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}// end x1 for

			}
		}
		if(merge){
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeUp(){
		boolean merge = false;
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {

				for (int y1 = y+1; y1 < 4; y1++) {
					if(cardsMap[x][y1].getNum() > 0){
						if(cardsMap[x][y].getNum() <= 0){
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);

							y--;
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);

							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}// end y1 for

			}
		}
		if(merge){
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeDown(){
		boolean merge = false;
		for (int x = 0; x < 4; x++) {
			for (int y = 3; y >= 0; y--) {

				for (int y1 = y-1; y1 >= 0; y1--) {
					if(cardsMap[x][y1].getNum() > 0){
						if(cardsMap[x][y].getNum() <= 0){
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);

							y++;
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);

							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}// end y1 for

			}
		}
		if(merge){
			addRandomNum();
			checkComplete();
		}
	}
	// 判断游戏是否结束
	private void checkComplete(){
		boolean complete = true;

		// 判断卡片的四周是否有相同的数字
		ALL:
			for (int y = 0; y < 4; y++) {
				for (int x = 0; x < 4; x++) {
					if(cardsMap[x][y].getNum() == 0 ||
							(x>0 && cardsMap[x][y].equals(cardsMap[x-1][y])) ||
							(x<3 && cardsMap[x][y].equals(cardsMap[x+1][y])) ||
							(y>0 && cardsMap[x][y].equals(cardsMap[x][y-1])) ||
							(y<3 && cardsMap[x][y].equals(cardsMap[x][y+1]))){
						complete = false;
						break ALL;
					}
				}
			}

		// 如果complete为true，弹出一个对话框，提示重新开始游戏
		if (complete) {
			
			new AlertDialog
			.Builder(getContext())
			.setTitle("你好")
			.setMessage("游戏结束，您的最终得分："+MainActivity.getMainActivity().getScore())
			.setPositiveButton("重新开始", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 点击按钮后重新开始
					startGame();
				}

			}).show();
		}
	}
	


	

	// 写一个二维数组来存储该方正
	private Card[][] cardsMap = new Card[4][4];
	private List<Point> emptyPoints = new ArrayList<Point>();

}
