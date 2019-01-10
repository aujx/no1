package com.example.wuziqi;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class WuziqiPanel extends View {
	private int mPanelWidth;//棋盘的宽度
	private float mLineHight;//每个空格的高度
	private int MAX_LINE = 10;//棋盘行数

	private Paint mPaint = new Paint();//创建画笔
	private Bitmap mWhitePiece;//白棋
	private Bitmap mBlackPiece;//黑棋
	private float ratioPieceOfLineHight = 3 * 1.0f / 4;//旗子与棋格的大小比例

	private boolean mIsWith = true;//白子是否画在棋盘上
	private List<Point> mWitharry = new ArrayList<Point>();//白棋坐标集合
	private List<Point> mBlackarry = new ArrayList<Point>();//黑棋坐标集合

	private boolean mIsGemOver;//游戏结束判断
	private boolean mIsWhiteWinner;//白棋是否胜利
	private String TAG = "CHESSVIEW";

	public WuziqiPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public void myclick(View source) {
		System.exit(0);
	}
	public void myreStart() {
        mWitharry.clear();
        mBlackarry.clear();
        mIsGemOver = false;
        Log.i(TAG, "myreStart: " + mWitharry.size() + ":::" +  mBlackarry.size());
        invalidate();

    }
//初始化
	private void init() {
		mPaint.setColor(0x44ff0000);//画笔颜色
		mPaint.setAntiAlias(true);//抗锯齿功能
		mPaint.setDither(true);//防抖动
		mPaint.setStyle(Paint.Style.STROKE);//画笔风格为空心

		mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
		mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
//获取旗子资源文件
	}

	@Override//获取坐标集合
	public boolean onTouchEvent(MotionEvent event) {
		if (mIsGemOver) {
			return false;
		}
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			Point p = getVaLidPoint(x, y);// //如果黑棋的集合或者白棋的集合包含这个坐标，那么返回false
            //contains和eequals比较的不是内存空间的地址，而是x,y值是否一致
			if (mWitharry.contains(p) || mBlackarry.contains(p)) {
				return false;
			}
			if (mIsWith) {
				mWitharry.add(p);
			} else {
				mBlackarry.add(p);
			}
			invalidate();
			mIsWith = !mIsWith;//刷新view,实现黑白交替
		}
		return true;
	}

	private Point getVaLidPoint(int x, int y) {//根据真实的坐标模拟出绝对值坐标
		return new Point((int) (x / mLineHight), (int) (y / mLineHight));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		int heighSize = MeasureSpec.getSize(heightMeasureSpec);
		int heighMode = MeasureSpec.getMode(heightMeasureSpec);
//由于棋盘为正方形，所以长宽选取最短
		int width = Math.min(widthSize, heighSize);
		//如果上方有一个是UNSPECIFIED，相对应的有一个尺寸是0，如果有一个是0，那么width就是0显示不出来
		if (widthMode == MeasureSpec.UNSPECIFIED) {
			width = heighSize;
		} else if (heighMode == MeasureSpec.UNSPECIFIED) {
			width = widthSize;
		}//设置实际的长和宽设置上去
		setMeasuredDimension(width, width);
	}
	//当view的尺寸改变时，会回掉这个方法
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mPanelWidth = w;
		mLineHight = mPanelWidth * 1.0f / MAX_LINE;
		int PiceWhite = (int) (mLineHight * ratioPieceOfLineHight);//按照以前存在的位图按照一定的比例构建一个新的位图
		mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, PiceWhite, PiceWhite, false);
		mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, PiceWhite, PiceWhite, false);
	}

	@Override
	protected void onDraw(Canvas canvas) {//画棋盘，旗子
		super.onDraw(canvas);
		drawBoard(canvas);
		draePicec(canvas);
		checkGameOver();
	}

	private void checkGameOver() {//判断是否结束
		boolean whithWin = chechFiveInLine(mWitharry);//白棋是否五子
		boolean blickWin = chechFiveInLine(mBlackarry);//黑棋是否五子
		if (whithWin || blickWin) {//显示输赢提示
			mIsGemOver = true;
			mIsWhiteWinner = whithWin;
			String text = mIsWhiteWinner ? "白旗赢" : "黑棋赢";
			new AlertDialog.Builder(getContext())//选择对话框
            .setMessage("恭喜" + ",是否再来一局？")
            .setCancelable(false)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    myreStart();
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                	System.exit(0);
                }
            })
            .show();
		}
	}

	private boolean chechFiveInLine(List<Point> mWitharry2) {//判断是否五子连珠
		for (Point p : mWitharry2) {
			int x = p.x;
			int y = p.y;

			boolean win = checkHorizontal(x, y, mWitharry2);
			if (win) return true;
			 win = checkVertIcal(x, y, mWitharry2);
			if (win) return true;
			 win = checkLeftDiagonal(x, y, mWitharry2);
			if (win) return true;
			 win = checkRightDiagonl(x, y, mWitharry2);
			if (win) return true;
		}
		return false;
	}

	private boolean checkHorizontal(int x, int y, List<Point> mWitharry2) {//判断横向向右是否练成5子,points里面存的值为int类型，所以可以进行加一或者减一的运算
		int count = 1;
		for (int i = 1; i < 5; i++) {
			if (mWitharry2.contains(new Point(x-i,y))) {
				count++;
			}else {
				break;
			}
		}
		if (count==5) return true;
		for (int i = 1; i < 5; i++) {
			if (mWitharry2.contains(new Point(x+i,y))) {
				count++;
			}else {
				break;
			}
			if (count==5) return true;
		}
		return false;
	}
	private boolean checkRightDiagonl(int x, int y, List<Point> mWitharry2) {//判断右斜
		int count = 1;
		for (int i = 1; i < 5; i++) {
			if (mWitharry2.contains(new Point(x-i,y-i))) {
				count++;
			}else {
				break;
			}
		}
		if (count==5) return true;
		for (int i = 1; i < 5; i++) {
			if (mWitharry2.contains(new Point(x+i,y+i))) {
				count++;
			}else {
				break;
			}
			if (count==5) return true;
		}
		return false;
	}
	private boolean checkLeftDiagonal(int x, int y, List<Point> mWitharry2) {//判断左斜
		int count = 1;
		for (int i = 1; i < 5; i++) {
			if (mWitharry2.contains(new Point(x-i,y+i))) {
				count++;
			}else {
				break;
			}
		}
		if (count==5) return true;
		for (int i = 1; i < 5; i++) {
			if (mWitharry2.contains(new Point(x+i,y-i))) {
				count++;
			}else {
				break;
			}
			if (count==5) return true;
		}
		return false;
	}
	private boolean checkVertIcal(int x, int y, List<Point> mWitharry2) {//判断垂直
		int count = 1;
		for (int i = 1; i < 5; i++) {
			if (mWitharry2.contains(new Point(x,y-i))) {
				count++;
			}else {
				break;
			}
		}
		if (count==5) return true;
		for (int i = 1; i < 5; i++) {
			if (mWitharry2.contains(new Point(x,y+i))) {
				count++;
			}else {
				break;
			}
			if (count==5) return true;
		}
		return false;
	}

	private void draePicec(Canvas canvas) {//画旗子
		for (int i = 0, n = mWitharry.size(); i < n; i++) {
			Point whitePoint = mWitharry.get(i);//drawBitmap是将图片的右下角为坐标
			canvas.drawBitmap(mWhitePiece, (whitePoint.x + (1 - ratioPieceOfLineHight) / 2) * mLineHight,
					(whitePoint.y + (1 - ratioPieceOfLineHight) / 2) * mLineHight, null);
		}
		for (int i = 0, n = mBlackarry.size(); i < n; i++) {
			Point blackPoint = mBlackarry.get(i);
			canvas.drawBitmap(mBlackPiece, (blackPoint.x + (1 - ratioPieceOfLineHight) / 2) * mLineHight,
					(blackPoint.y + (1 - ratioPieceOfLineHight) / 2) * mLineHight, null);
		}
	}

	private void drawBoard(Canvas canvas) {//画棋盘的线
		int w = mPanelWidth;
		float lineHeight = mLineHight;
		//画十条线
		for (int i = 0; i < MAX_LINE; i++) {
			//设置起点横坐标为半个棋盘空格的宽度
			int startX = (int) (lineHeight / 2);
			//设置终点X横坐标为宽度减去半个lineHeight(棋盘空格宽度)
			int endX = (int) (w - lineHeight / 2);
			int y = (int) ((0.5 + i) * lineHeight);
			canvas.drawLine(startX, y, endX, y, mPaint);//画横线
			canvas.drawLine(y, startX, y, endX, mPaint);//画竖线，坐标相反
		}
	}

}
