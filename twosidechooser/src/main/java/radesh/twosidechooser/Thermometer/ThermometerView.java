package radesh.twosidechooser.Thermometer;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
import radesh.twosidechooser.R;

import java.util.Locale;

/**
 * <p>Description 自定义控件，温度计/体温计
 * <p>author biansemao
 * <p>date 2017/9/22 9:12
 */
public class ThermometerView extends View {

    private int viewBg; // view background
    private float unitTextSize; // unit Text Size
    private int unitTextColor; // unit Text Color
    private float scaleTextSize; // 刻度值文字大小
    private int scaleTextColor; // 刻度值文字颜色
    private int maxScaleLineColor; // 长刻度颜色
    private int midScaleLineColor; // 中等刻度颜色
    private int minScaleLineColor; // 短刻度颜色
    private float scaleLineWidth; // 刻度线宽
    private float maxLineWidth; // 长刻度长
    private float midLineWidth; // 中等刻度长
    private float minLineWidth; // 短刻度长
    private float spaceScaleWidth; // The distance from the thermometer to the thermometer, the distance from the scale to the text
    private int thermometerBg; // 温度计颜色
    private int thermometerShadowBg; // 温度计阴影颜色
    private float maxThermometerRadius; // 温度计底部半径
    private float minThermometerRadius; // // 温度计顶部半径
    private float maxMercuryRadius; // 水银底部半径
    private float minMercuryRadius; // 水银顶部半径
    private int leftMercuryBg; // 左边水银背景颜色
    private int rightMercuryBg; // 右边水银背景颜色
    private int leftMercuryColor; // 左边水银颜色
    private int rightMercuryColor; // 右边水银颜色
    private float maxScaleValue; // 温度计最大值
    private float minScaleValue; // 温度计最小值
    private float curScaleValue; // 当前刻度值

    private int mWidth; // 宽
    private float mPaddingTop; // 上内容缩进

    private float leftTitleX; // 左title的X坐标
    private float rightTitleX; // 右title的X坐标
    private float titleY; // title的Y坐标
    private float textWidth; // title的宽度
    private float titleHeight; // title的高度
    private float scaleSpaceHeight; // 刻度间隔
    private float sumScaleValue; // 总刻度数
    private float thermometerTopX; // 温度计顶部 圆心X坐标
    private float thermometerTopY; // 温度计顶部 圆心Y坐标
    private float thermometerBottomX; // 温度计底部\水银底部 圆心X坐标
    private float thermometerBottomY; // 温度计底部\水银底部 圆心Y坐标
    private RectF thermometerRectF; // 温度计圆柱体区域
    private RectF mercuryRectF; // 水银顶部区域，底部由于完全填充，所以底部直接绘制圆形
    private float leftMercuryLeft; // 左水银left，相当于RectF.left
    private float leftMercuryRight; // 左水银right，相当于RectF.right
    private float rightMercuryLeft; // 右水银left，相当于RectF.left
    private float rightMercuryRight; // 右水银right，相当于RectF.right
    private float mercuryTop; // 水银top，相当于RectF.top
    private float mercuryBottom; // 水银bottom，相当于RectF.bottom
    private float leftWaveLeft; // 左增长水银left，相当于RectF.left
    private float leftWaveRight; // 左增长水银right，相当于RectF.right
    private float rightWaveLeft; // 右增长水银left，相当于RectF.left
    private float rightWaveRight; // 右增长水银right，相当于RectF.right
    private float waveBottom; // 增长水银bottom，相当于RectF.bottom

    private TextPaint mTextPaint;
    private Paint mLinePaint;

    private Paint mPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;

    private boolean needF = false;

    public ThermometerView(Context context) {
        super(context);
        init(null);
    }

    public ThermometerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ThermometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    //for create inside java so nothing to do for now
    public ThermometerView(Context context, ThermometerBuilder builder) {
        super(context);
        this.viewBg = builder.viewBg;
        this.unitTextSize = builder.unitTextSize;
        this.unitTextColor = builder.unitTextColor;
        this.scaleTextSize = builder.scaleTextSize;
        this.scaleTextColor = builder.scaleTextColor;
        this.maxScaleLineColor = builder.maxScaleLineColor;
        this.midScaleLineColor = builder.midScaleLineColor;
        this.minScaleLineColor = builder.minScaleLineColor;
        this.scaleLineWidth = builder.scaleLineWidth;
        this.maxLineWidth = builder.maxLineWidth;
        this.midLineWidth = builder.midLineWidth;
        this.minLineWidth = builder.minLineWidth;
        this.spaceScaleWidth = builder.spaceScaleWidth;
        this.thermometerBg = builder.thermometerBg;
        this.thermometerShadowBg = builder.thermometerShadowBg;
        this.maxThermometerRadius = builder.maxThermometerRadius;
        this.minThermometerRadius = builder.minThermometerRadius;
        this.maxMercuryRadius = builder.maxMercuryRadius;
        this.minMercuryRadius = builder.minMercuryRadius;
        this.leftMercuryBg = builder.leftMercuryBg;
        this.rightMercuryBg = builder.rightMercuryBg;
        this.leftMercuryColor = builder.leftMercuryColor;
        this.rightMercuryColor = builder.rightMercuryColor;
        this.maxScaleValue = builder.maxScaleValue;
        this.minScaleValue = builder.minScaleValue;
        this.curScaleValue = builder.curScaleValue;

        initConfig();
    }


    //For my use
    private void init(AttributeSet attrs) {

        viewBg = Color.parseColor("#FFFFFF");// don't make this transparent!
        //units
        unitTextSize = getTextSize(R.dimen._5ssp);
        unitTextColor = Color.parseColor("#787878");
        //scales
        scaleTextColor = Color.parseColor("#464646");
        scaleTextSize = getTextSize(R.dimen._4ssp);
        maxScaleLineColor = Color.parseColor("#787878");
        midScaleLineColor = Color.parseColor("#000000");
        maxLineWidth = getDimenSize(R.dimen._5sdp);
        midLineWidth = getDimenSize(R.dimen._25sdp);
        midLineWidth =getDimenSize(R.dimen._15sdp);
        minLineWidth = getDimenSize(R.dimen._10sdp); // 短刻度长
        spaceScaleWidth = getDimenSize(R.dimen._5sdp); // 刻度离温度计距离、刻度离文字距离
        //view
        thermometerBg = Color.parseColor("#ECF7FA"); // parent shadow color
        thermometerShadowBg = Color.parseColor("#2c2c2c"); // shadow color
        maxThermometerRadius = getDimenSize(R.dimen._30sdp); //circle of button view
        minThermometerRadius = getDimenSize(R.dimen._15sdp); //Cylinder(ostovane) size
        maxMercuryRadius = getDimenSize(R.dimen._18sdp);
        minMercuryRadius = getDimenSize(R.dimen._7sdp);
        //left color
        leftMercuryBg = Color.parseColor("#FFFFFF");// 左边水银背景颜色
        rightMercuryBg = Color.parseColor("#FFFFFF");
        //base color
        leftMercuryColor = Color.parseColor("#DB4A51");
        rightMercuryColor = Color.parseColor("#DB4A51");
        //degrees
        maxScaleValue = 42f; // max degree
        minScaleValue = 32f; // minimum degree
        curScaleValue = 32f; // degree


        initConfig();
    }

    private Typeface getTypeFace(){
        return Typeface.createFromAsset(getResources().getAssets(), "font/IRANSansMobile.ttf");
    }

    public float getMaxScaleValue(){
        return maxScaleValue;
    }
    public float getMinScaleValue(){
        return minScaleValue;
    }

    //        viewBg = typedArray.getColor(R.styleable.ThermometerView_viewBg, Color.parseColor("#F5F5F5"));
//        unitTextSize = getTextSize(R.dimen._18ssp);
//        unitTextColor = typedArray.getColor(R.styleable.ThermometerView_unitTextColor, Color.parseColor("#787878"));
//        scaleTextSize = typedArray.getDimension(R.styleable.ThermometerView_scaleTextSize, 18f);
//        scaleTextColor = typedArray.getColor(R.styleable.ThermometerView_scaleTextColor, Color.parseColor("#464646"));
//        maxScaleLineColor = typedArray.getColor(R.styleable.ThermometerView_maxScaleLineColor, Color.parseColor("#787878"));
//        midScaleLineColor = typedArray.getColor(R.styleable.ThermometerView_midScaleLineColor, Color.parseColor("#A9A9A9"));
//        minScaleLineColor = typedArray.getColor(R.styleable.ThermometerView_minScaleLineColor, Color.parseColor("#A9A9A9"));
//        scaleLineWidth = typedArray.getDimension(R.styleable.ThermometerView_scaleLineWidth, 1.5f);
//        maxLineWidth = typedArray.getDimension(R.styleable.ThermometerView_maxLineWidth, 70f);
//        midLineWidth = typedArray.getDimension(R.styleable.ThermometerView_midLineWidth, 50f);
//        minLineWidth = typedArray.getDimension(R.styleable.ThermometerView_minLineWidth, 40f);
//        spaceScaleWidth = typedArray.getDimension(R.styleable.ThermometerView_spaceScaleWidth, 30f);
//        thermometerBg = typedArray.getColor(R.styleable.ThermometerView_thermometerBg, Color.parseColor("#270058"));//parent shadow color
//        thermometerShadowBg = typedArray.getColor(R.styleable.ThermometerView_thermometerShadowBg, Color.parseColor("#2c2c2c")); //shadow
//        maxThermometerRadius = typedArray.getDimension(R.styleable.ThermometerView_maxThermometerRadius, 120f);//circle of button view
//        minThermometerRadius = typedArray.getDimension(R.styleable.ThermometerView_minThermometerRadius, 60f);//Cylinder(ostovane) size
//        maxMercuryRadius = typedArray.getDimension(R.styleable.ThermometerView_maxMercuryRadius, 100f);
//        minMercuryRadius = typedArray.getDimension(R.styleable.ThermometerView_minMercuryRadius, 40f);
//        leftMercuryBg = typedArray.getColor(R.styleable.ThermometerView_leftMercuryBg, Color.parseColor("#FFE6E0"));
//        rightMercuryBg = typedArray.getColor(R.styleable.ThermometerView_rightMercuryBg, Color.parseColor("#FDE1DE"));
//        leftMercuryColor = typedArray.getColor(R.styleable.ThermometerView_leftMercuryColor, Color.parseColor("#FF8063"));
//        rightMercuryColor = typedArray.getColor(R.styleable.ThermometerView_rightMercuryColor, Color.parseColor("#F66A5C"));
//        maxScaleValue = typedArray.getDimension(R.styleable.ThermometerView_maxScaleValue, 42f);//max degree
//        minScaleValue = typedArray.getDimension(R.styleable.ThermometerView_minScaleValue, 35f);//minimum degree
//        curScaleValue = typedArray.getDimension(R.styleable.ThermometerView_curScaleValue, 37f);//degree
//        typedArray.recycle();
//
//        initConfig();
//    }

    //        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ThermometerView);
//    private void init(AttributeSet attrs) {


    private float getTextSize(int dimen){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(dimen), getResources().getDisplayMetrics());
    }
    private float getDimenSize(int dimen){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(dimen), getResources().getDisplayMetrics());
    }


    public void initConfig() {
        if (minThermometerRadius >= maxThermometerRadius) {
            throw new UnsupportedOperationException("The shape of the thermometer is set incorrectly。");
        }
        if (minMercuryRadius >= maxMercuryRadius || minMercuryRadius >= minThermometerRadius) {
            throw new UnsupportedOperationException("Mercury shape is set incorrectly。");
        }
        if (minScaleValue >= maxScaleValue) {
            throw new UnsupportedOperationException("Scale value setting is incorrect");
        }
        setResetCurValue(curScaleValue);

        sumScaleValue = (maxScaleValue - minScaleValue) * 10;

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(scaleLineWidth);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        String title = "℃";
        mTextPaint.setTextSize(unitTextSize);
        textWidth = Layout.getDesiredWidth(title, mTextPaint);
        Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
        titleHeight = -(float) (fmi.bottom + fmi.top);

        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null); // Turn off hardware acceleration, otherwise the shadow is invalid

        mTextPaint.setTypeface(getTypeFace());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = this.getWidth();
        int mHeight = this.getHeight();
        mPaddingTop = getPaddingTop();
        float mPaddingBottom = getPaddingBottom();

        leftTitleX = mWidth / 2 - spaceScaleWidth - minThermometerRadius - maxLineWidth / 2 - textWidth / 2;
        rightTitleX = mWidth / 2 + spaceScaleWidth + minThermometerRadius + maxLineWidth / 2 - textWidth / 2;
        titleY = mPaddingTop + titleHeight;

        float mercuryHeight = mHeight - titleHeight - mPaddingTop - mPaddingBottom - minThermometerRadius - 2 * maxThermometerRadius;

        scaleSpaceHeight = mercuryHeight / sumScaleValue;

        thermometerTopX = thermometerBottomX = mWidth / 2;
        thermometerTopY = mPaddingTop + titleHeight + minThermometerRadius;
        thermometerBottomY = mHeight - mPaddingBottom - maxThermometerRadius;

        thermometerRectF = new RectF();
        thermometerRectF.left = mWidth / 2 - minThermometerRadius;
        thermometerRectF.top = mPaddingTop + titleHeight + minThermometerRadius;
        thermometerRectF.right = mWidth / 2 + minThermometerRadius;
        thermometerRectF.bottom = mHeight - mPaddingBottom - maxThermometerRadius;

        mercuryRectF = new RectF();
        mercuryRectF.left = mWidth / 2 - minMercuryRadius;
        mercuryRectF.top = mPaddingTop + titleHeight + minThermometerRadius;
        mercuryRectF.right = mWidth / 2 + minMercuryRadius;
        mercuryRectF.bottom = mPaddingTop + titleHeight + minThermometerRadius + 2 * minMercuryRadius;

        leftMercuryLeft = mWidth / 2 - minMercuryRadius;
        leftMercuryRight = rightMercuryLeft = mWidth / 2;
        rightMercuryRight = mWidth / 2 + minMercuryRadius;

        mercuryTop = mPaddingTop + titleHeight + minThermometerRadius + minMercuryRadius;
        mercuryBottom = mHeight - mPaddingBottom - maxThermometerRadius;

        leftWaveLeft = mWidth / 2 - maxMercuryRadius;
        leftWaveRight = rightWaveLeft = mWidth / 2;
        rightWaveRight = mWidth / 2 + maxMercuryRadius;

        waveBottom = mHeight - mPaddingBottom - (maxThermometerRadius - maxMercuryRadius);

        //自己创建一个Bitmap
        bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (curScaleValue < minScaleValue || curScaleValue > maxScaleValue) {
            return;
        }

        canvas.drawColor(viewBg);

        drawScaleTitleText(canvas);
        drawScaleText(canvas);

        drawShapeBg(mPaint, canvas);
        drawShape(mPaint, bitmapCanvas);
        drawWaveShape(mPaint, bitmapCanvas);

        canvas.drawBitmap(bitmap, 0, 0, mPaint);
    }

    /**
     * 绘制单位文字
     */
    private void drawScaleTitleText(Canvas canvas) {
        mTextPaint.setColor(unitTextColor);
        mTextPaint.setTextSize(unitTextSize);

        if (needF){
            canvas.drawText("℉", leftTitleX, titleY, mTextPaint);
        }

        canvas.drawText("℃", rightTitleX, titleY, mTextPaint);
    }

    /**
     * Draw scales and text
     */
    private void drawScaleText(Canvas canvas) {
        /* Draw the scale and text on the left */
        if (needF){
            drawScaleTextForF(canvas);
        }

        /* Draw the scale and text on the right */
        for (int i = 0; i <= sumScaleValue; i++) {
            if (i % 10 == 0) {
                float curValue = maxScaleValue - i / 10;
                String curValueStr = String.format(Locale.getDefault(),"%.0f", curValue);

                mTextPaint.setColor(scaleTextColor);
                mTextPaint.setTextSize(scaleTextSize);

                Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
                float baselineY = -(float) (fmi.bottom + fmi.top);
                canvas.drawText(curValueStr,
                        mWidth / 2 + minThermometerRadius + 2 * spaceScaleWidth + maxLineWidth,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i + baselineY / 2,
                        mTextPaint);

                mLinePaint.setColor(maxScaleLineColor);
                canvas.drawLine(mWidth / 2 + spaceScaleWidth + minThermometerRadius,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i,
                        mWidth / 2 + spaceScaleWidth + minThermometerRadius + maxLineWidth,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i, mLinePaint);
            } else if (i % 5 == 0) {
                mLinePaint.setColor(midScaleLineColor);

                canvas.drawLine(mWidth / 2 + spaceScaleWidth + minThermometerRadius,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i,
                        mWidth / 2 + spaceScaleWidth + minThermometerRadius + midLineWidth,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i, mLinePaint);
            } else {
                mLinePaint.setColor(minScaleLineColor);

                canvas.drawLine(mWidth / 2 + spaceScaleWidth + minThermometerRadius,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i,
                        mWidth / 2 + spaceScaleWidth + minThermometerRadius + minLineWidth,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i, mLinePaint);
            }
        }
    }

    private void drawScaleTextForF(Canvas canvas) {
        for (int i = 0; i <= sumScaleValue; i++) {
            if (i % 10 == 0) {
                double curValue = (maxScaleValue - i / 10) * 1.8 + 32; // Celsius temperature converted to Fahrenheit
                String curValueStr = String.format("%.1f", curValue);

                mTextPaint.setColor(scaleTextColor);
                mTextPaint.setTextSize(scaleTextSize);
                float textWidth = Layout.getDesiredWidth(curValueStr, mTextPaint);
                Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
                float baselineY = -(float) (fmi.bottom + fmi.top);
                canvas.drawText(curValueStr,
                        mWidth / 2 - minThermometerRadius - 2 * spaceScaleWidth - maxLineWidth - textWidth,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i + baselineY / 2, mTextPaint);

                mLinePaint.setColor(maxScaleLineColor);
                canvas.drawLine(mWidth / 2 - spaceScaleWidth - minThermometerRadius - maxLineWidth,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i,
                        mWidth / 2 - spaceScaleWidth - minThermometerRadius,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i, mLinePaint);
            } else if (i % 5 == 0) {
                mLinePaint.setColor(midScaleLineColor);

                canvas.drawLine(mWidth / 2 - spaceScaleWidth - minThermometerRadius - midLineWidth,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i,
                        mWidth / 2 - spaceScaleWidth - minThermometerRadius,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i, mLinePaint);
            } else {
                mLinePaint.setColor(minScaleLineColor);

                canvas.drawLine(mWidth / 2 - spaceScaleWidth - minThermometerRadius - minLineWidth,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i,
                        mWidth / 2 - spaceScaleWidth - minThermometerRadius,
                        mPaddingTop + titleHeight + minThermometerRadius + scaleSpaceHeight * i, mLinePaint);
            }
        }
    }

    /**
     * Draw thermometer shape
     *
     * @param shapePaint Paint
     * @param canvas     Canvas
     */
    private void drawShapeBg(Paint shapePaint, Canvas canvas) {
        shapePaint.setColor(thermometerBg);
        shapePaint.setShadowLayer(20, 0, 0, thermometerShadowBg);

        canvas.drawCircle(thermometerTopX, thermometerTopY, minThermometerRadius, shapePaint);
        canvas.drawCircle(thermometerBottomX, thermometerBottomY, maxThermometerRadius, shapePaint);
        canvas.drawRect(thermometerRectF, shapePaint);

        /* 以下三句是为了去除部分不需要的阴影 */
        shapePaint.clearShadowLayer();
        canvas.drawCircle(thermometerTopX, thermometerTopY, minThermometerRadius, shapePaint);
        canvas.drawCircle(thermometerBottomX, thermometerBottomY, maxThermometerRadius, shapePaint);
    }

    /**
     * Drawing a mercury shape
     * Note: must be combined with the {@link #drawWaveShape(Paint, Canvas)} method
     *
     * @param shapePaint Paint
     * @param canvas     Canvas
     */
    private void drawShape(Paint shapePaint, Canvas canvas) {
        shapePaint.clearShadowLayer();

        shapePaint.setColor(leftMercuryBg);
        canvas.drawArc(mercuryRectF, 90, 180, true, shapePaint);

        canvas.drawRect(leftMercuryLeft, mercuryTop, leftMercuryRight, mercuryBottom, shapePaint);

        shapePaint.setColor(rightMercuryBg);
        canvas.drawArc(mercuryRectF, -90, 180, true, shapePaint);

        canvas.drawRect(rightMercuryLeft, mercuryTop, rightMercuryRight, mercuryBottom, shapePaint);

        canvas.drawCircle(thermometerBottomX, thermometerBottomY, maxMercuryRadius, shapePaint);

    }

    /**
     * Draw thermometer mercury height
     * Note: Must be combined with the {@link #drawShape(Paint, Canvas)} method
     *
     * @param shapePaint Paint
     * @param canvas     Canvas
     */
    private void drawWaveShape(Paint shapePaint, Canvas canvas) {
        float waveTop = mPaddingTop + titleHeight + minThermometerRadius
                + (maxScaleValue - curScaleValue) * 10 * scaleSpaceHeight;

        shapePaint.setColor(leftMercuryColor);
        shapePaint.clearShadowLayer();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        canvas.drawRect(leftWaveLeft, waveTop, leftWaveRight, waveBottom, shapePaint);

        shapePaint.setColor(rightMercuryColor);
        shapePaint.clearShadowLayer();

        canvas.drawRect(rightWaveLeft, waveTop, rightWaveRight, waveBottom, shapePaint);
    }

    /**
     * Set the temperature value
     * @param curValue current temperature value float(°C)
     */
    public void setCurValue(float curValue) {
        setResetCurValue(curValue);
        invalidate();
    }

    /**
     * Set the temperature value
     *
     * @param curFValue current temperature value float(°F)
     */
    public void setCurFValue(float curFValue) {
        String curValueStr = String.format("%.0f", (curFValue - 32) / 1.8);
        setResetCurValue(Float.valueOf(curValueStr));
        invalidate();
    }

    /**
     * Reset (calibrate) temperature value
     *
     * @param curValue current temperature value float(°C)
     */
    private void setResetCurValue(float curValue) {
        if (curValue < minScaleValue) {
            curValue = minScaleValue;
        }
        if (curValue > maxScaleValue) {
            curValue = maxScaleValue;
        }
        this.curScaleValue = curValue;
    }

    /**
     * Set the temperature value and start the animation (interpolator: LinearInterpolator)
     *
     * @param newFValue new temperature value float(°F)
     */
    public void setFValueAndStartAnim(float newFValue) {
        String curValueStr = String.format("%.0f", (newFValue - 32) / 1.8);
        setValueAndStartAnim(Float.valueOf(curValueStr));
    }

    /**
     * Set the temperature value and start the animation (interpolator: LinearInterpolator)
     *
     * @param newValue new temperature value float(°C)
     *                 fixed by radesh
     */
    public void setValueAndStartAnim(float newValue) {
        if (newValue < minScaleValue) {
            newValue = minScaleValue;
        }
        if (newValue > maxScaleValue) {
            newValue = maxScaleValue;
        }
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(this, "curValue", curScaleValue, newValue);
        waveShiftAnim.setRepeatCount(0);
        waveShiftAnim.setDuration(500);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        waveShiftAnim.start();
        this.curScaleValue = newValue;
    }



    /**
     * Constructor
     */
    public static class ThermometerBuilder {

        private Context context;
        private int viewBg = Color.parseColor("#F5F5F5"); // background color
        private float unitTextSize = 36f; // Unit text size
        private int unitTextColor = Color.parseColor("#787878"); // 单位文字颜色
        private float scaleTextSize = 18f; // 刻度值文字大小
        private int scaleTextColor = Color.parseColor("#464646"); // 刻度值文字颜色
        private int maxScaleLineColor = Color.parseColor("#787878"); // 长刻度颜色
        private int midScaleLineColor = Color.parseColor("#A9A9A9"); // 中等刻度颜色
        private int minScaleLineColor = Color.parseColor("#A9A9A9"); // 短刻度颜色
        private float scaleLineWidth = 1.5f; // 刻度线宽
        private float maxLineWidth = 70f; // 长刻度长
        private float midLineWidth = 50f; // 中等刻度长
        private float minLineWidth = 40f; // 短刻度长
        private float spaceScaleWidth = 30f; // 刻度离温度计距离、刻度离文字距离
        private int thermometerBg = Color.WHITE; // 温度计颜色
        private int thermometerShadowBg = Color.parseColor("#2c2c2c"); // 温度计阴影颜色
        private float maxThermometerRadius = 80f; // 温度计底部半径
        private float minThermometerRadius = 40f; // // 温度计顶部半径
        private float maxMercuryRadius = 60f; // 水银底部半径
        private float minMercuryRadius = 20f; // 水银顶部半径
        private int leftMercuryBg = Color.parseColor("#FFE6E0"); // 左边水银背景颜色
        private int rightMercuryBg = Color.parseColor("#FDE1DE"); // 右边水银背景颜色
        private int leftMercuryColor = Color.parseColor("#FF8063"); // 左边水银颜色
        private int rightMercuryColor = Color.parseColor("#F66A5C"); // 右边水银颜色
        private float maxScaleValue = 42f; // 温度计最大值
        private float minScaleValue = 35f; // 温度计最小值
        private float curScaleValue = 35f; // 当前刻度值

        public ThermometerBuilder(Context context) {
            this.context = context;
        }

        public ThermometerBuilder setViewBg(int viewBg) {
            this.viewBg = viewBg;
            return this;
        }

        public ThermometerBuilder setUnitTextSize(float unitTextSize) {
            this.unitTextSize = unitTextSize;
            return this;
        }

        public ThermometerBuilder setUnitTextColor(int unitTextColor) {
            this.unitTextColor = unitTextColor;
            return this;
        }

        public ThermometerBuilder setScaleTextSize(float scaleTextSize) {
            this.scaleTextSize = scaleTextSize;
            return this;
        }

        public ThermometerBuilder setScaleTextColor(int scaleTextColor) {
            this.scaleTextColor = scaleTextColor;
            return this;
        }

        public ThermometerBuilder setMaxScaleLineColor(int maxScaleLineColor) {
            this.maxScaleLineColor = maxScaleLineColor;
            return this;
        }

        public ThermometerBuilder setMidScaleLineColor(int midScaleLineColor) {
            this.midScaleLineColor = midScaleLineColor;
            return this;
        }

        public ThermometerBuilder setMinScaleLineColor(int minScaleLineColor) {
            this.minScaleLineColor = minScaleLineColor;
            return this;
        }

        public ThermometerBuilder setScaleLineWidth(float scaleLineWidth) {
            this.scaleLineWidth = scaleLineWidth;
            return this;
        }

        public ThermometerBuilder setMaxLineWidth(float maxLineWidth) {
            this.maxLineWidth = maxLineWidth;
            return this;
        }

        public ThermometerBuilder setMidLineWidth(float midLineWidth) {
            this.midLineWidth = midLineWidth;
            return this;
        }

        public ThermometerBuilder setMinLineWidth(float minLineWidth) {
            this.minLineWidth = minLineWidth;
            return this;
        }

        public ThermometerBuilder setSpaceScaleWidth(float spaceScaleWidth) {
            this.spaceScaleWidth = spaceScaleWidth;
            return this;
        }

        public ThermometerBuilder setThermometerBg(int thermometerBg) {
            this.thermometerBg = thermometerBg;
            return this;
        }

        public ThermometerBuilder setThermometerShadowBg(int thermometerShadowBg) {
            this.thermometerShadowBg = thermometerShadowBg;
            return this;
        }

        public ThermometerBuilder setMaxThermometerRadius(float maxThermometerRadius) {
            this.maxThermometerRadius = maxThermometerRadius;
            return this;
        }

        public ThermometerBuilder setMinThermometerRadius(float minThermometerRadius) {
            this.minThermometerRadius = minThermometerRadius;
            return this;
        }

        public ThermometerBuilder setMaxMercuryRadius(float maxMercuryRadius) {
            this.maxMercuryRadius = maxMercuryRadius;
            return this;
        }

        public ThermometerBuilder setMinMercuryRadius(float minMercuryRadius) {
            this.minMercuryRadius = minMercuryRadius;
            return this;
        }

        public ThermometerBuilder setLeftMercuryBg(int leftMercuryBg) {
            this.leftMercuryBg = leftMercuryBg;
            return this;
        }

        public ThermometerBuilder setRightMercuryBg(int rightMercuryBg) {
            this.rightMercuryBg = rightMercuryBg;
            return this;
        }

        public ThermometerBuilder setLeftMercuryColor(int leftMercuryColor) {
            this.leftMercuryColor = leftMercuryColor;
            return this;
        }

        public ThermometerBuilder setRightMercuryColor(int rightMercuryColor) {
            this.rightMercuryColor = rightMercuryColor;
            return this;
        }

        public ThermometerBuilder setMaxScaleValue(float maxScaleValue) {
            this.maxScaleValue = maxScaleValue;
            return this;
        }

        public ThermometerBuilder setMinScaleValue(float minScaleValue) {
            this.minScaleValue = minScaleValue;
            return this;
        }

        public ThermometerBuilder setCurScaleValue(float curScaleValue) {
            this.curScaleValue = curScaleValue;
            return this;
        }

        public ThermometerView builder() {
            return new ThermometerView(context, this);
        }

    }

}
