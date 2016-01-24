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

	// xml����viewʱ������һ��AttributeSet���͵Ĳ�������ͨ�������ļ�xml����һ��viewʱ����������Ὣxml���趨�����Դ��ݸ����캯������������xml inflate�ķ���ȴû����code��ʵ�ָ� ��������ô����ʱ�ͻᱨ��
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}

	// ���봴��viewʱ�������ֻ������code��̬����һ��view����ʹ�ò����ļ�xml inflate
	public GameView(Context context) {
		super(context);
		initGameView();
	}

	// �������ĳ�ʼ������
	private void initGameView(){

		this.setColumnCount(4);	// ����Ϊ4*4�еĲ���
		this.setBackgroundColor(0xffbbada0);

		// ���öԵ���¼��ļ������ж��û�����Ķ���
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

					// ���з�λ�жϣ����»�������
					if(Math.abs(offsetX) > Math.abs(offsetY)){	// �����һ���
						if(offsetX < -5){	// ���󻬶�
							swipeLeft();
						} else if(offsetX > 5){	// ���һ���
							swipeRight();
						}
					} else {	// �����»���
						if(offsetY < -5){	// ���ϻ���
							swipeUp();
						} else if(offsetY > 5){	// ���»���
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


	// ��������ǵ�ǰ��View�����ı��ʱ����ã�w��h����ǰ�Ŀ�ߣ�oldw��oldh����ı�֮ǰ�Ŀ��
	// �÷�������onCreate֮��onDraw֮ǰ����
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// �Կ��ȡ��Сֵ
		int cardWidth = (Math.min(w, h) - 10) / 4;

		addCard(cardWidth, cardWidth);	// ��Ϊ��Ƭ�������εģ����Կ��һ��
		// ��ʼ��Ϸ
		startGame();
	}
	// ��ӿ�Ƭ
	private void addCard(int cardWidth, int cardHeight){

		Card c = null;

		for(int y=0; y<4; y++){
			for(int x=0; x<4; x++){
				c = new Card(getContext());
				c.setNum(0);	// �տ�ʼȫ�����Ϊ0����Ϊ�տ�Ƭ
				addView(c, cardWidth, cardHeight);

				cardsMap[x][y] = c;	// ��¼������ǰ�Ŀ�Ƭ
			}
		}
	}
	// ��ʼ��Ϸ
	private void startGame(){
		// �������
		MainActivity.getMainActivity().clearScore();

		// ��������ϵ�������
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cardsMap[x][y].setNum(0);
			}
		}
		// ������������
		addRandomNum();
		addRandomNum();
	}

	// ���һ�������
	private void addRandomNum(){
		emptyPoints.clear();

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				// ��λ�ñ���Ϊ0
				if(cardsMap[x][y].getNum() <= 0){
					emptyPoints.add(new Point(x, y));
				}
			}
		}// end for
		// ���ȡ��һ����Ƭ�������Ƴ��Ǹ���Ƭ
		Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
		// ���Ƴ������Ǹ��������2����4��2��4���ֵĸ�����9:1
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

							// �ںϲ���ʱ�����ӷ���
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}// end x1 for

			}
		}
		// ����ж�������ô�����һ���µ�����������ж��Ƿ����������Ϸ������
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
	// �ж���Ϸ�Ƿ����
	private void checkComplete(){
		boolean complete = true;

		// �жϿ�Ƭ�������Ƿ�����ͬ������
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

		// ���completeΪtrue������һ���Ի�����ʾ���¿�ʼ��Ϸ
		if (complete) {
			
			new AlertDialog
			.Builder(getContext())
			.setTitle("���")
			.setMessage("��Ϸ�������������յ÷֣�"+MainActivity.getMainActivity().getScore())
			.setPositiveButton("���¿�ʼ", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// �����ť�����¿�ʼ
					startGame();
				}

			}).show();
		}
	}
	


	

	// дһ����ά�������洢�÷���
	private Card[][] cardsMap = new Card[4][4];
	private List<Point> emptyPoints = new ArrayList<Point>();

}
