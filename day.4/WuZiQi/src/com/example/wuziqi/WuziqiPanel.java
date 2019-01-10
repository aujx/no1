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
	private int mPanelWidth;//���̵Ŀ��
	private float mLineHight;//ÿ���ո�ĸ߶�
	private int MAX_LINE = 10;//��������

	private Paint mPaint = new Paint();//��������
	private Bitmap mWhitePiece;//����
	private Bitmap mBlackPiece;//����
	private float ratioPieceOfLineHight = 3 * 1.0f / 4;//���������Ĵ�С����

	private boolean mIsWith = true;//�����Ƿ���������
	private List<Point> mWitharry = new ArrayList<Point>();//�������꼯��
	private List<Point> mBlackarry = new ArrayList<Point>();//�������꼯��

	private boolean mIsGemOver;//��Ϸ�����ж�
	private boolean mIsWhiteWinner;//�����Ƿ�ʤ��
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
//��ʼ��
	private void init() {
		mPaint.setColor(0x44ff0000);//������ɫ
		mPaint.setAntiAlias(true);//����ݹ���
		mPaint.setDither(true);//������
		mPaint.setStyle(Paint.Style.STROKE);//���ʷ��Ϊ����

		mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
		mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
//��ȡ������Դ�ļ�
	}

	@Override//��ȡ���꼯��
	public boolean onTouchEvent(MotionEvent event) {
		if (mIsGemOver) {
			return false;
		}
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			Point p = getVaLidPoint(x, y);// //�������ļ��ϻ��߰���ļ��ϰ���������꣬��ô����false
            //contains��eequals�ȽϵĲ����ڴ�ռ�ĵ�ַ������x,yֵ�Ƿ�һ��
			if (mWitharry.contains(p) || mBlackarry.contains(p)) {
				return false;
			}
			if (mIsWith) {
				mWitharry.add(p);
			} else {
				mBlackarry.add(p);
			}
			invalidate();
			mIsWith = !mIsWith;//ˢ��view,ʵ�ֺڰ׽���
		}
		return true;
	}

	private Point getVaLidPoint(int x, int y) {//������ʵ������ģ�������ֵ����
		return new Point((int) (x / mLineHight), (int) (y / mLineHight));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		int heighSize = MeasureSpec.getSize(heightMeasureSpec);
		int heighMode = MeasureSpec.getMode(heightMeasureSpec);
//��������Ϊ�����Σ����Գ���ѡȡ���
		int width = Math.min(widthSize, heighSize);
		//����Ϸ���һ����UNSPECIFIED�����Ӧ����һ���ߴ���0�������һ����0����ôwidth����0��ʾ������
		if (widthMode == MeasureSpec.UNSPECIFIED) {
			width = heighSize;
		} else if (heighMode == MeasureSpec.UNSPECIFIED) {
			width = widthSize;
		}//����ʵ�ʵĳ��Ϳ�������ȥ
		setMeasuredDimension(width, width);
	}
	//��view�ĳߴ�ı�ʱ����ص��������
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mPanelWidth = w;
		mLineHight = mPanelWidth * 1.0f / MAX_LINE;
		int PiceWhite = (int) (mLineHight * ratioPieceOfLineHight);//������ǰ���ڵ�λͼ����һ���ı�������һ���µ�λͼ
		mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, PiceWhite, PiceWhite, false);
		mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, PiceWhite, PiceWhite, false);
	}

	@Override
	protected void onDraw(Canvas canvas) {//�����̣�����
		super.onDraw(canvas);
		drawBoard(canvas);
		draePicec(canvas);
		checkGameOver();
	}

	private void checkGameOver() {//�ж��Ƿ����
		boolean whithWin = chechFiveInLine(mWitharry);//�����Ƿ�����
		boolean blickWin = chechFiveInLine(mBlackarry);//�����Ƿ�����
		if (whithWin || blickWin) {//��ʾ��Ӯ��ʾ
			mIsGemOver = true;
			mIsWhiteWinner = whithWin;
			String text = mIsWhiteWinner ? "����Ӯ" : "����Ӯ";
			new AlertDialog.Builder(getContext())//ѡ��Ի���
            .setMessage("��ϲ" + ",�Ƿ�����һ�֣�")
            .setCancelable(false)
            .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    myreStart();
                }
            })
            .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                	System.exit(0);
                }
            })
            .show();
		}
	}

	private boolean chechFiveInLine(List<Point> mWitharry2) {//�ж��Ƿ���������
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

	private boolean checkHorizontal(int x, int y, List<Point> mWitharry2) {//�жϺ��������Ƿ�����5��,points������ֵΪint���ͣ����Կ��Խ��м�һ���߼�һ������
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
	private boolean checkRightDiagonl(int x, int y, List<Point> mWitharry2) {//�ж���б
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
	private boolean checkLeftDiagonal(int x, int y, List<Point> mWitharry2) {//�ж���б
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
	private boolean checkVertIcal(int x, int y, List<Point> mWitharry2) {//�жϴ�ֱ
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

	private void draePicec(Canvas canvas) {//������
		for (int i = 0, n = mWitharry.size(); i < n; i++) {
			Point whitePoint = mWitharry.get(i);//drawBitmap�ǽ�ͼƬ�����½�Ϊ����
			canvas.drawBitmap(mWhitePiece, (whitePoint.x + (1 - ratioPieceOfLineHight) / 2) * mLineHight,
					(whitePoint.y + (1 - ratioPieceOfLineHight) / 2) * mLineHight, null);
		}
		for (int i = 0, n = mBlackarry.size(); i < n; i++) {
			Point blackPoint = mBlackarry.get(i);
			canvas.drawBitmap(mBlackPiece, (blackPoint.x + (1 - ratioPieceOfLineHight) / 2) * mLineHight,
					(blackPoint.y + (1 - ratioPieceOfLineHight) / 2) * mLineHight, null);
		}
	}

	private void drawBoard(Canvas canvas) {//�����̵���
		int w = mPanelWidth;
		float lineHeight = mLineHight;
		//��ʮ����
		for (int i = 0; i < MAX_LINE; i++) {
			//������������Ϊ������̿ո�Ŀ��
			int startX = (int) (lineHeight / 2);
			//�����յ�X������Ϊ��ȼ�ȥ���lineHeight(���̿ո���)
			int endX = (int) (w - lineHeight / 2);
			int y = (int) ((0.5 + i) * lineHeight);
			canvas.drawLine(startX, y, endX, y, mPaint);//������
			canvas.drawLine(y, startX, y, endX, mPaint);//�����ߣ������෴
		}
	}

}
