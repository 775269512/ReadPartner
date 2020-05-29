package com.grace.zhihunews.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;
import android.widget.Toast;

import com.grace.zhihunews.R;
import com.grace.zhihunews.network.entity.Book;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BookPageView extends View {

    //当前页
    private int curPageNum = 1;
    private int textSize = 60;

    private MyPoint a, f, g, e, h, c, j, b, k, d, i;//各点坐标

    private TextPaint paintText;//文字画笔

    private Path pathA;//区域A路径
    private Path pathC;//区域C路径
    private Path pathB;//区域B路径

    //页面宽高
    private int width;
    private int height;

    float lPathAShadowDis = 0;//A区域左阴影矩形短边长度参考值
    float rPathAShadowDis = 0;//A区域右阴影矩形短边长度参考值

    //翻页起始位置
    private static final int FROM_NORMAL = -1;
    private static final int FROM_RIGHT_TOP = 0;
    private static final int FROM_RIGHT_BOTTOM = 1;
    private static final int FROM_RIGHT = 2;
    private static final int FROM_LEFT_TOP = 3;
    private static final int FROM_LEFT_BOTTOM = 4;
    private static final int FROM_LEFT = 5;

    //滑动用
    private Scroller mScroller;
    private InputStream inputStream;
    private String filePath;
    private Book mBook;

    public BookPageView(Context context) {
        this(context, null);
    }

    public BookPageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        init();
    }

    //******************************初始化******************************

    //初始化变量
    private void init() {

        width = 600;
        height = 1000;
        mScroller = new Scroller(getContext(), new AccelerateDecelerateInterpolator());

        initPoint();
        initPaint();
        initPath();
        initGradient();
    }

    //初始化点坐标
    private void initPoint() {
        a = new MyPoint();
        f = new MyPoint();

        g = new MyPoint();
        e = new MyPoint();
        h = new MyPoint();
        c = new MyPoint();
        j = new MyPoint();
        b = new MyPoint();
        k = new MyPoint();
        d = new MyPoint();
        i = new MyPoint();
    }

    //初始化画笔
    private void initPaint() {
        paintText = new TextPaint();
        paintText.setColor(Color.BLACK);
        paintText.setAntiAlias(true);
        paintText.setTextSize(textSize);
    }

    //初始化路径
    private void initPath() {
        pathA = new Path();
        pathC = new Path();
        pathB = new Path();
    }

    //顶部翻页A区左侧阴影
    private GradientDrawable gradientDrawableATopLeft;
    //底部翻页A区左侧阴影
    private GradientDrawable gradientDrawableABottomLeft;
    //顶部翻页A区右侧阴影
    private GradientDrawable gradientDrawableATopRight;
    //底部翻页A区右侧阴影
    private GradientDrawable gradientDrawableABottomRight;
    //水平翻页A区阴影
    private GradientDrawable gradientDrawableAHorizontal;
    //顶部翻页B区阴影
    private GradientDrawable gradientDrawableBTop;
    //底部翻页B区阴影
    private GradientDrawable gradientDrawableBBottom;
    //顶部翻页C区阴影
    private GradientDrawable gradientDrawableCTop;
    //底部翻页C区阴影
    private GradientDrawable gradientDrawableCBottom;

    //初始化阴影
    private void initGradient() {
        int deepColor = 0x55333333;
        int lightColor = 0x01333333;
        int[] gradientColors = new int[]{lightColor, deepColor};

        gradientDrawableATopLeft = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        gradientDrawableATopLeft.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        gradientDrawableABottomLeft = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
        gradientDrawableABottomLeft.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x55333333;
        lightColor = 0x01333333;
        gradientColors = new int[]{lightColor, lightColor, lightColor, deepColor};
        gradientDrawableATopRight = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, gradientColors);
        gradientDrawableATopRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        gradientDrawableABottomRight = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, gradientColors);
        gradientDrawableABottomRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x55333333;
        lightColor = 0x01333333;
        gradientColors = new int[]{lightColor, deepColor};
        gradientDrawableAHorizontal = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        gradientDrawableAHorizontal.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x55333333;
        lightColor = 0x01111111;
        gradientColors = new int[]{deepColor, lightColor};
        gradientDrawableBTop = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        gradientDrawableBTop.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawableBBottom = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
        gradientDrawableBBottom.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x22333333;
        lightColor = 0x00333333;
        gradientColors = new int[]{lightColor, deepColor};
        gradientDrawableCTop = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        gradientDrawableCTop.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawableCBottom = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
        gradientDrawableCBottom.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }

    //******************************绘制******************************

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmapContentC, 0, 0, null);

        if (from == FROM_NORMAL) {//未翻页状态
            drawPathAContent(canvas, getPathAreaA());
        } else {
            switch (from) {
                case FROM_RIGHT_TOP:
                    drawPathAContent(canvas, getPathAreaARightTop());
                    drawPathCContent(canvas, getPathAreaARightTop());
                    drawPathBContent(canvas, getPathAreaARightTop());
                    break;
                case FROM_RIGHT_BOTTOM:
                case FROM_RIGHT:
                    drawPathAContent(canvas, getPathAreaARightBottom());
                    drawPathCContent(canvas, getPathAreaARightBottom());
                    drawPathBContent(canvas, getPathAreaARightBottom());
                    break;
                case FROM_LEFT_TOP:
                    drawPathAContent(canvas, getPathAreaALeftTop());
                    drawPathCContent(canvas, getPathAreaALeftTop());
                    drawPathBContent(canvas, getPathAreaALeftTop());
                    break;
                case FROM_LEFT_BOTTOM:
                case FROM_LEFT:
                    drawPathAContent(canvas, getPathAreaALeftBottom());
                    drawPathCContent(canvas, getPathAreaALeftBottom());
                    drawPathBContent(canvas, getPathAreaALeftBottom());
                    break;
            }
        }

    }

    //绘制A区内容
    private void drawPathAContent(Canvas canvas, Path path) {
        canvas.save();
        canvas.clipPath(path, Region.Op.INTERSECT);
        canvas.drawBitmap(bitmapContentA, 0, 0, null);
        if (from != FROM_NORMAL) {
            if (from == FROM_RIGHT) {
                drawPathAHorizontalShadow(canvas);
            } else if (from == FROM_RIGHT_TOP || from == FROM_RIGHT_BOTTOM) {
                drawPathALeftShadow(canvas);
                drawPathARightShadoe(canvas);
            }
        }
        canvas.restore();
    }

    //A区左侧
    private void drawPathALeftShadow(Canvas canvas) {
        canvas.restore();
        canvas.save();

        int left;
        int right;
        int top = (int) e.y;
        int bottom = (int) (e.y + height);

        GradientDrawable gradientDrawable;
        if (from == FROM_RIGHT_TOP) {
            gradientDrawable = gradientDrawableATopLeft;

            left = (int) (e.x - lPathAShadowDis / 2);
            right = (int) (e.x);
        } else {
            gradientDrawable = gradientDrawableABottomLeft;

            left = (int) (e.x);
            right = (int) (e.x + lPathAShadowDis / 2);
        }
        gradientDrawable.setBounds(left, top, right, bottom);

        //裁剪出我们需要的区域
        Path mPath = new Path();
        mPath.moveTo(a.x - Math.max(rPathAShadowDis, lPathAShadowDis) / 2, a.y);
        mPath.lineTo(d.x, d.y);
        mPath.lineTo(e.x, e.y);
        mPath.lineTo(a.x, a.y);
        mPath.close();
        canvas.clipPath(pathA);
        canvas.clipPath(mPath, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(e.x - a.x, a.y - e.y));
        canvas.rotate(mDegrees, e.x, e.y);

        gradientDrawable.draw(canvas);
    }

    //A区右侧
    private void drawPathARightShadoe(Canvas canvas) {
        canvas.restore();
        canvas.save();

        float viewDiagonalLength = (float) Math.hypot(width, height);//view对角线长度
        int left = (int) h.x;
        int right = (int) (h.x + viewDiagonalLength * 10);//需要足够长的长度
        int top;
        int bottom;

        GradientDrawable gradientDrawable;
        if (from == FROM_RIGHT_TOP) {
            gradientDrawable = gradientDrawableATopRight;

            top = (int) (h.y - rPathAShadowDis / 2);
            bottom = (int) h.y;
        } else {
            gradientDrawable = gradientDrawableABottomRight;

            top = (int) h.y;
            bottom = (int) (h.y + rPathAShadowDis / 2);
        }
        gradientDrawable.setBounds(left, top, right, bottom);

        //裁剪出我们需要的区域
        Path mPath = new Path();
        mPath.moveTo(a.x - Math.max(rPathAShadowDis, lPathAShadowDis) / 2, a.y);
        mPath.lineTo(h.x, h.y);
        mPath.lineTo(a.x, a.y);
        mPath.close();
        canvas.clipPath(pathA);
        canvas.clipPath(mPath, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(a.y - h.y, a.x - h.x));
        canvas.rotate(mDegrees, h.x, h.y);

        gradientDrawable.draw(canvas);
    }

    //A区水平翻页阴影
    private void drawPathAHorizontalShadow(Canvas canvas) {
        canvas.restore();
        canvas.save();
        canvas.clipPath(pathA, Region.Op.INTERSECT);

        int maxShadowWidth = 30;//阴影矩形最大的宽度
        int left = (int) (a.x - Math.min(maxShadowWidth, (rPathAShadowDis / 2)));
        int right = (int) (a.x);
        int top = 0;
        int bottom = height;

        gradientDrawableAHorizontal.setBounds(left, top, right, bottom);

        float mDegrees = (float) Math.toDegrees(Math.atan2(f.x - a.x, f.y - h.y));
        canvas.rotate(mDegrees, a.x, a.y);
        gradientDrawableAHorizontal.draw(canvas);
    }

    //绘制B区内容
    private void drawPathBContent(Canvas canvas, Path pathA) {

        canvas.save();
        canvas.clipPath(pathA);
        canvas.clipPath(getPathAreaC(), Region.Op.UNION);
        canvas.clipPath(getPathAreaB(), Region.Op.REVERSE_DIFFERENCE);
        canvas.drawBitmap(bitmapContentB, 0, 0, null);
        drawPathBShadow(canvas);
        canvas.restore();

    }

    //绘制投在B区域的阴影
    private void drawPathBShadow(Canvas canvas) {
        int deepOffset = 0;
        int lightOffset = 0;

        float aTof = (float) Math.hypot((a.x - f.x), (a.y - f.y));
        float viewDiagonalLength = (float) Math.hypot(width, height);

        int left;
        int right;
        int top = (int) c.y;
        int bottom = (int) (viewDiagonalLength + top);

        GradientDrawable gradientDrawable;

        if (from == FROM_RIGHT_TOP) {
            gradientDrawable = gradientDrawableBTop;

            left = (int) (c.x - deepOffset);
            right = (int) (c.x + aTof / 4 + lightOffset);
        } else {
            gradientDrawable = gradientDrawableBBottom;

            left = (int) (c.x - aTof / 4 - lightOffset);
            right = (int) (c.x + deepOffset);
        }
        gradientDrawable.setBounds(left, top, right, bottom);

        float rotateDegrees = (float) Math.toDegrees(Math.atan2(e.x - f.x, h.y - f.y));
        canvas.rotate(rotateDegrees, c.x, c.y);
        gradientDrawable.draw(canvas);
    }

    //绘制C区内容
    private void drawPathCContent(Canvas canvas, Path pathA) {

        canvas.save();
        canvas.clipPath(pathA);
        canvas.clipPath(getPathAreaC(), Region.Op.REVERSE_DIFFERENCE);

        float eh = (float) Math.hypot(f.x - e.x, h.y - f.y);
        float sin = (f.x - e.x) / eh;
        float cos = (h.y - f.y) / eh;

        float[] matrix = {0, 0, 0, 0, 0, 0, 0, 0, 1f};
        matrix[0] = -(1 - 2 * sin * sin);
        matrix[1] = 2 * sin * cos;
        matrix[3] = 2 * sin * cos;
        matrix[4] = 1 - 2 * sin * sin;

        Matrix mMatrix = new Matrix();
        mMatrix.reset();
        mMatrix.setValues(matrix);
        mMatrix.preTranslate(-e.x, -e.y);
        mMatrix.postTranslate(e.x, e.y);

        canvas.drawBitmap(bitmapContentC, mMatrix, null);
        drawPathCShadow(canvas);
        canvas.restore();
    }

    //绘制投在C区域的阴影
    private void drawPathCShadow(Canvas canvas) {

        int deepOffset = 1;//深色端的偏移值
        int lightOffset = -30;//浅色端的偏移值
        float viewDiagonalLength = (float) Math.hypot(width, height);//view对角线长度
        int midpoint_ce = (int) (c.x + e.x) / 2;//ce中点
        int midpoint_jh = (int) (j.y + h.y) / 2;//jh中点
        float minDisToControlPoint = Math.min(Math.abs(midpoint_ce - e.x), Math.abs(midpoint_jh - h.y));//中点到控制点的最小值

        int left;
        int right;
        int top = (int) c.y;
        int bottom = (int) (viewDiagonalLength + c.y);
        GradientDrawable gradientDrawable;
        if (from == FROM_RIGHT_TOP) {
            gradientDrawable = gradientDrawableCTop;

            left = (int) (c.x - lightOffset);
            right = (int) (c.x + minDisToControlPoint + deepOffset);
        } else {
            gradientDrawable = gradientDrawableCBottom;

            left = (int) (c.x - minDisToControlPoint - deepOffset);
            right = (int) (c.x + lightOffset);
        }
        gradientDrawable.setBounds(left, top, right, bottom);

        float mDegrees = (float) Math.toDegrees(Math.atan2(e.x - f.x, h.y - f.y));
        canvas.rotate(mDegrees, c.x, c.y);
        gradientDrawable.draw(canvas);
    }

    //区域A的路径 右上角翻页
    private Path getPathAreaARightTop() {
        pathA.reset();
        pathA.lineTo(c.x, c.y);
        pathA.quadTo(e.x, e.y, b.x, b.y);
        pathA.lineTo(a.x, a.y);
        pathA.lineTo(k.x, k.y);
        pathA.quadTo(h.x, h.y, j.x, j.y);
        pathA.lineTo(width, height);
        pathA.lineTo(0, height);
        pathA.close();
        return pathA;
    }

    //区域A的路径 右下角翻页
    private Path getPathAreaARightBottom() {
        pathA.reset();
        pathA.lineTo(0, height);//直线到左下角
        pathA.lineTo(c.x, c.y);//直线到c点
        pathA.quadTo(e.x, e.y, b.x, b.y);//贝塞尔曲线画弧，e为控点，b为终点
        pathA.lineTo(a.x, a.y);//弧线到直线，下半边画完
        pathA.lineTo(k.x, k.y);//上半边直线
        pathA.quadTo(h.x, h.y, j.x, j.y);//直线到弧线
        pathA.lineTo(width, 0);//弧线到右上角
        pathA.close();//封闭路径
        return pathA;
    }

    //区域A的路径 左上角翻页
    private Path getPathAreaALeftTop() {
        pathA.reset();
        pathA.lineTo(c.x, c.y);
        pathA.quadTo(e.x, e.y, b.x, b.y);
        pathA.lineTo(a.x, a.y);
        pathA.lineTo(k.x, k.y);
        pathA.quadTo(h.x, h.y, j.x, j.y);
        pathA.lineTo(0, height);
        pathA.lineTo(width, height);
        pathA.lineTo(width, 0);
        pathA.close();
        return pathA;
    }

    //区域A的路径 左下角翻页
    private Path getPathAreaALeftBottom() {
        pathA.reset();
        pathA.lineTo(width, 0);
        pathA.lineTo(width, height);
        pathA.lineTo(c.x, c.y);
        pathA.quadTo(e.x, e.y, b.x, b.y);
        pathA.lineTo(a.x, a.y);
        pathA.lineTo(k.x, k.y);
        pathA.quadTo(h.x, h.y, j.x, j.y);
        pathA.lineTo(0, 0);
        pathA.close();
        return pathA;
    }

    //完整区域A的路径
    private Path getPathAreaA() {
        pathA.reset();
        pathA.lineTo(0, height);
        pathA.lineTo(width, height);
        pathA.lineTo(width, 0);
        pathA.close();
        return pathA;
    }

    //区域A的路径
    private Path getPathAreaC() {
        pathC.reset();//原点左上角
        pathC.moveTo(i.x, i.y);//i点为起点
        pathC.lineTo(d.x, d.y);//直线到d点
        pathC.lineTo(b.x, b.y);//直线到b点
        pathC.lineTo(a.x, a.y);//直线到a点
        pathC.lineTo(k.x, k.y);//直线到k点
        pathC.close();//封闭路径
        return pathC;
    }

    //区域B的路径
    private Path getPathAreaB() {
        pathB.reset();
        pathB.lineTo(0, height);//左下角
        pathB.lineTo(width, height);//右下角
        pathB.lineTo(width, 0);//右上角
        pathB.close();
        return pathB;
    }

    //******************************测量******************************

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = measureSize(width, widthMeasureSpec);
        int h = measureSize(height, heightMeasureSpec);
        setMeasuredDimension(w, h);
        width = w;
        height = h;

        f.x = width;
        f.y = height;
        a.x = a.y = -1;

        initBitmap();
    }

    //根据MeasureSpec返回具体的宽高
    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        }

        return result;
    }

    private Bitmap bitmapBg;
    private Bitmap bitmapContentA;
    private Bitmap bitmapContentB;
    private Bitmap bitmapContentC;

    //初始化各区域显示内容，在onMeasure后才能获取宽高
    private void initBitmap() {
        bitmapBg = ((BitmapDrawable) getResources().getDrawable(R.mipmap.bg_pager)).getBitmap();

        initBitmapA(getPageContent(curPageNum));

        initBitmapB(getPageContent(curPageNum + 1));

        bitmapContentC = Bitmap.createScaledBitmap(bitmapBg, width, height, true);
        if (onPagingListener != null) {
            onPagingListener.onPageChange(curPageNum);
        }
    }

    //ABC各自显示的内容
    private void initBitmapA(String content) {

        bitmapContentA = Bitmap.createScaledBitmap(bitmapBg, width, height, true);
//        bitmapContentA = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapContentA);
//        canvas.drawPath(getPathAreaA(), paintAreaA);
        StaticLayout staticLayout = new StaticLayout(content, paintText, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        canvas.translate(20, 0);
        staticLayout.draw(canvas);
    }

    private void initBitmapB(String content) {
        bitmapContentB = Bitmap.createScaledBitmap(bitmapBg, width, height, true);
//        bitmapContentB = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapContentB);
//        canvas.drawPath(getPathAreaA(), paintAreaB);
        StaticLayout staticLayout = new StaticLayout(content, paintText, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        canvas.translate(20, 0);
        staticLayout.draw(canvas);
    }

    //******************************触点******************************

    //记录翻页方向
    private int from = FROM_NORMAL;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("lzc", "ACTION_DOWN" + "----from=" + from);
                float x = event.getX();
                float y = event.getY();

                if (x < width / 3) {//落点在左侧1/3
                    if (curPageNum == 1) {//第一页不许前翻
                        if (onPagingListener != null) {
                            onPagingListener.noMore(false);
                        }
                        break;
                    }
                    if (y < height / 3) {//左上角
                        from = FROM_LEFT_TOP;
                    } else if (y > height * 2 / 3) {//左下角
                        from = FROM_LEFT_BOTTOM;
                    } else {//左中心
                        from = FROM_LEFT;
                    }
                    initBitmapB(getPageContent(curPageNum - 1));
                } else if (x > width * 2 / 3) {//落点在右侧1/3
                    if (y < height / 3) {//右上角
                        from = FROM_RIGHT_TOP;
                    } else if (y > height * 2 / 3) {//右下角
                        from = FROM_RIGHT_BOTTOM;
                    } else {//右中心
                        from = FROM_RIGHT;
                    }
                    initBitmapB(getPageContent(curPageNum + 1));
                } else {//中心区
                    if (onCenterClickListener != null) {
                        onCenterClickListener.onCenterClick(this);
                    }
                    break;
                }

                setTouchPoint(x, y, from);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("lzc", "ACTION_MOVE" + "----from=" + from);
                if (from != FROM_NORMAL) {
                    setTouchPoint(event.getX(), event.getY(), from);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.i("lzc", "ACTION_CANCEL" + "----from=" + from);
                if (from != FROM_NORMAL) {
                    page();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    //设置触摸点
    private void setTouchPoint(float x, float y, int from) {
        a.x = x;
        a.y = y;
        switch (from) {
            case FROM_RIGHT_TOP:
                f.x = width;
                f.y = 0;
                calculatePoint();
                if (c.x < 0 && mScroller.isFinished()) {
                    calculateAPointRight();
                }
                break;
            case FROM_RIGHT_BOTTOM:
                f.x = width;
                f.y = height;
                calculatePoint();
                if (c.x < 0 && mScroller.isFinished()) {
                    calculateAPointRight();
                }
                break;
            case FROM_RIGHT:
                f.x = width;
                f.y = height;
                a.y = height - 1;
                break;
            case FROM_LEFT_TOP:
                f.x = 0;
                f.y = 0;
                calculatePoint();
                if (c.x > width && mScroller.isFinished()) {
                    calculateAPointLeft();
                }
                break;
            case FROM_LEFT_BOTTOM:
                f.x = 0;
                f.y = height;
                calculatePoint();
                if (c.x > width && mScroller.isFinished()) {
                    calculateAPointLeft();
                }
                break;
            case FROM_LEFT:
                f.x = 0;
                f.y = height;
                a.y = height - 1;
                break;
        }

        calculatePoint();
        postInvalidate();
    }

    //根据定点求动点坐标
    private void calculatePoint() {
        //g点为af的中点
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        //e点 做辅助线gm通过相似垂直三角形egm和gmf求得em，e点坐标为：(gx-em, height)
        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        //h点 同e点
        h.x = f.x;
        h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y);

        //c点 做n点为ag中点，有三角形cjf和ehf
        c.x = e.x - (f.x - e.x) / 2;
        c.y = f.y;

        //j点 同c点
        j.x = f.x;
        j.y = h.y - (f.y - h.y) / 2;

        b = getCrosspoint(a, e, c, j);
        k = getCrosspoint(a, h, c, j);

        //d点 ed连接交cb于p点，d为pe的中点
        d.x = (c.x + 2 * e.x + b.x) / 4;
        d.y = (2 * e.y + c.y + b.y) / 4;

        //i点 同d点
        i.x = (j.x + 2 * h.x + k.x) / 4;
        i.y = (2 * h.y + j.y + k.y) / 4;

        float lA = a.y - e.y;
        float lB = e.x - a.x;
        float lC = a.x * e.y - e.x * a.y;
        lPathAShadowDis = Math.abs((lA * d.x + lB * d.y + lC) / (float) Math.hypot(lA, lB));

        float rA = a.y - h.y;
        float rB = h.x - a.x;
        float rC = a.x * h.y - h.x * a.y;
        rPathAShadowDis = Math.abs((rA * i.x + rB * i.y + rC) / (float) Math.hypot(rA, rB));
    }

    //获取两条直线的交点
    private MyPoint getCrosspoint(MyPoint line1StartP, MyPoint line1EndP, MyPoint line2StartP, MyPoint line2EndP) {
        float x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = line1StartP.x;
        y1 = line1StartP.y;
        x2 = line1EndP.x;
        y2 = line1EndP.y;
        x3 = line2StartP.x;
        y3 = line2StartP.y;
        x4 = line2EndP.x;
        y4 = line2EndP.y;

        float pointX = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));
        float pointY = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
                / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));

        return new MyPoint(pointX, pointY);
    }

    //根据相似三角形，计算固定C点，从而算出A点坐标
    //计算A点X坐标，防止左边线超出屏幕
    private void calculateAPointRight() {
        float w0 = width - c.x;
        float w1 = Math.abs(f.x - a.x);
        float w2 = width * w1 / w0;
        a.x = Math.abs(f.x - w2);

        float h1 = Math.abs(f.y - a.y);
        float h2 = w2 * h1 / w1;
        a.y = Math.abs(f.y - h2);
    }

    //计算A点X坐标，防止右边线超出屏幕
    private void calculateAPointLeft() {
        float w0 = c.x;
        float w1 = a.x;
        float w2 = width * w1 / w0;
        a.x = w2;

        float h1 = Math.abs(f.y - a.y);
        float h2 = w2 * h1 / w1;
        a.y = Math.abs(f.y - h2);
    }

    //手势离开时判断翻页还是恢复
    private void page() {
        switch (from) {
            case FROM_RIGHT:
            case FROM_RIGHT_TOP:
            case FROM_RIGHT_BOTTOM:
                if (a.x < width / 2) {
                    nextPage();
                } else {
                    backToNormal();
                }
                break;
            case FROM_LEFT:
            case FROM_LEFT_TOP:
            case FROM_LEFT_BOTTOM:
                if (a.x > width / 2) {
                    prePage();
                } else {
                    backToNormal();
                }
                break;
        }
    }

    private final int PAGING_STATE_NONE = 0;
    private final int PAGING_STATE_NEXT = 1;
    private final int PAGING_STATE_PRE = 2;
    private final int PAGING_STATE_NORMAL = 3;

    private int pagingState = PAGING_STATE_NONE;

    //下一页
    private void nextPage() {
        int dx = -(width + (int) (a.x));
        int dy;
        if (from == FROM_RIGHT_TOP) {
            dy = (int) (-a.y);
        } else {
            dy = (int) (height - a.y);
        }
        pagingState = PAGING_STATE_NEXT;
        mScroller.startScroll((int) a.x, (int) a.y, dx, dy, 1000);
    }

    //上一页
    private void prePage() {
        int dx = (2 * width - (int) (a.x));
        int dy;
        if (from == FROM_LEFT_TOP) {
            dy = (int) (-a.y);
        } else {
            dy = (int) (height - a.y);
        }
        pagingState = PAGING_STATE_PRE;
        mScroller.startScroll((int) a.x, (int) a.y, dx, dy, 1000);
    }

    //返回正常状态
    private void backToNormal() {
        int dx = 0;
        int dy = 0;
        switch (from) {
            case FROM_RIGHT_TOP:
                dx = (int) (width - a.x);
                dy = (int) (-a.y);
                break;
            case FROM_RIGHT_BOTTOM:
            case FROM_RIGHT:
                dx = (int) (width - a.x);
                dy = (int) (height - a.y);
                break;
            case FROM_LEFT_TOP:
                dx = (int) (-a.x);
                dy = (int) (-a.y);
                break;
            case FROM_LEFT_BOTTOM:
            case FROM_LEFT:
                dx = (int) (-a.x);
                dy = (int) (height - a.y);
                break;
        }
        pagingState = PAGING_STATE_NORMAL;
        mScroller.startScroll((int) a.x, (int) a.y, dx, dy, 400);
    }

    //mScroller回调
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();

            if (mScroller.getFinalX() == x && mScroller.getFinalY() == y) {
                mScroller.forceFinished(true);//必需设置停止，否则下次翻页必定不响应，
                // 因为909行return true，进入后getFinalX() == x，getFinalY() == y又变回了PAGING_STATE_NORMAL，此次的事件便被忽略掉了
                if (pagingState == PAGING_STATE_NORMAL) {
                    from = FROM_NORMAL;
                } else {
                    changeContent();
                }
            } else {
                setTouchPoint(x, y, from);
            }
        }
    }

    //翻页后数据变化
    private void changeContent() {
        switch (pagingState) {
            case PAGING_STATE_NEXT:
                curPageNum++;
                break;
            case PAGING_STATE_PRE:
                curPageNum--;
                break;
        }
        initBitmapA(getPageContent(curPageNum));
        pagingState = PAGING_STATE_NONE;
        from = FROM_NORMAL;
        if (onPagingListener != null) {
            onPagingListener.onPageChange(curPageNum);
        }
    }

    private String getPageContent(int pageNum) {
        return mContentController.getContent(pageNum);
    }

    //anroid.graphics.Point的x、y为整型
    public class MyPoint {
        float x, y;

        MyPoint() {
        }

        MyPoint(float x, float y) {
            this.x = x;
            this.y = y;

        }

        @Override
        public String toString() {
            return "MyPoint{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    //******************************内容******************************

    private ContentController mContentController = new ContentController(getContext());

    private class ContentController {

        private Context mContext;
        private static final int CACHE_PAGE = 50;//txt预计一行约500个汉字，共1000字节约1k。缓存50行约50k
        private static final int CACHE_PRE_PAGE = 10;
        private static final int CACHE_NEXT_PAGE = CACHE_PAGE - CACHE_PRE_PAGE;
        private static final int THRESHOLD = 3;//触发更新的阀值

        private String[] content = new String[CACHE_PAGE];

        private ContentController(Context mContext) {
            this.mContext = mContext;
        }

        /**
         * @param curPage 实际页数
         * @return 页内容
         */
        private String getContent(int curPage) {//起始页数为1
            if (curPage < 1) {
                return "";
            }
            int position = (curPage - 1) % CACHE_PAGE;//页数对应缓存数组中的位置
            if (position > CACHE_PAGE - THRESHOLD || position < THRESHOLD) {//翻页到阀值时，预先加载
                updateContent(curPage);
            }
            return content[position];
        }

        private void updateContent(int page) {//更新page前后内数据，
            try {
                int startPage = page - CACHE_PRE_PAGE;
                int endPage = page + CACHE_NEXT_PAGE;

                if (startPage < 1) {//防止越界
                    startPage = 1;
                }
                if (startPage > endPage) {
                    endPage = startPage;
                }

                content = getContent(startPage, endPage);
            } catch (Exception e) {
                e.printStackTrace();
                if (onPagingListener != null) {
                    onPagingListener.onContentErro(e.getMessage());
                }
            }

        }

        /**
         * @param startPage 起始页码
         * @param endPage   结束页码
         * @return 缓存的数据
         * @throws Exception IO或越界异常
         */
        private String[] getContent(int startPage, int endPage) throws Exception {
            if (startPage < 1 || endPage < startPage) {
                throw new RuntimeException("内容越界");
            }

            String[] result = new String[endPage - startPage + 1];

            if (filePath == "" || filePath==null){
                Toast.makeText(getContext(), "未找到文件，打开初始书籍", Toast.LENGTH_LONG).show();
                inputStream = mContext.getResources().openRawResource(R.raw.a);
            }
            else {
                try {
                    inputStream = new FileInputStream(filePath);
                } catch (java.io.FileNotFoundException e) {
                    Log.d("TestFile", "The File doesn't not exist.");
                }
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            int pageNum = 1;//当前页码，每次加载都是从txt第一行读取

            int countMax = (width / textSize) * (height / textSize - 3) / 10;//每页最大字数，width*height/（size*size）,去个位
            countMax = countMax * 10;
            int numOfRow = width / textSize;//一行字数，width/size
            String readLine = "";//读取的行内容，或上次截剩的内容
            for (int i = startPage; i <= endPage; i++) {

                StringBuilder stringBuffer = new StringBuilder();//页内容容器
                int pageCount = 0;//当前页内的容量

                while (true) {
                    if (!TextUtils.isEmpty(readLine)) {//读取的行内容，或上次截剩的内容，还没处理完，如一行内容为1000，按432容量计算的话，要处理3次

                        int stringLen = readLine.length();//剩余长度
                        if (pageCount + stringLen <= countMax) {//当前页可装下
                            stringBuffer.append(readLine).append("\n");
                            pageCount += stringLen;
                            pageCount += numOfRow;//回车则少一行文字容量
                            readLine = "";//置空，读取新内容
                        } else {
                            int count = countMax - pageCount;//当前页装不下，还能装count量
                            if (count < numOfRow) {//count量小于一行的量，留到下一页
                                break;
                            }
                            //拼接一部分，让屏幕饱满
                            stringBuffer.append(getPreContent(readLine, count)).append("\n");
                            pageCount += count;
                            pageCount += numOfRow;
                            //剩下一部分，留到下一页
                            readLine = getRestContent(readLine, count);
                            break;
                        }
                    } else {
                        readLine = br.readLine();//读取新内容
                    }
                }
                if (pageNum >= startPage) {//为了跳过startPage的前几页
                    result[i - startPage] = stringBuffer.toString();
                }
                pageNum++;

            }
            return result;
        }

        /**
         * 返回内容的前半部分
         *
         * @param content 要取的源内容
         * @param count   返回的量
         * @return 切割后的值
         */
        private String getPreContent(String content, int count) {
            if (count > content.length()) {//总数量不足截取量
                count = content.length();
            }
            return content.substring(0, count);
        }

        /**
         * 返回内容的后半部分
         *
         * @param content 要取的源内容
         * @param start   后半部分的起始位置
         * @return 切割后的值
         */
        private String getRestContent(String content, int start) {
            if (start > content.length() - 1) {//开始位置，超过总长度
                return "";
            }
            return content.substring(start, content.length());
        }

    }

    //******************************回调及设置参数******************************

    private OnCenterClickListener onCenterClickListener;

    //点击中心区唤起工具栏
    interface OnCenterClickListener {
        void onCenterClick(BookPageView bookPageView);
    }

    public void setOnCenterClickListener(OnCenterClickListener onCenterClickListener) {
        this.onCenterClickListener = onCenterClickListener;
    }

    private OnPagingListener onPagingListener;

    interface OnPagingListener {
        void noMore(boolean next);//没有前页or后页

        void onPageChange(int pageNum);//翻页

        void onContentErro(String msg);//获取内容失败
    }

    public void setOnPagingListener(OnPagingListener onPagingListener) {
        this.onPagingListener = onPagingListener;
    }

    //打开之前设置
    public void setCurPageNum(int pageNum) {
        curPageNum = pageNum;
    }

    //设置字体大小，影响显示及内容数量
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setBook(Book book) {
        mBook = book;
        filePath = mBook.getTxt_path();
        Toast.makeText(getContext(), filePath, Toast.LENGTH_LONG).show();

    }

    public Book getBook() {
        return mBook;
    }
}