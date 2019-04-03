package com.zs.buding.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.zs.buding.R;

/**
 * @author mrzhu
 * on 2019/4/2
 * ClassName：仿布丁机器人眼睛 动画
 */
public class EyeView extends View {

    private int color = R.color.color_E1F650;

    private int viewWidth;
    private int viewHeight;
    private Context context;
    private Paint eyePaint;
    private Paint eyelidPaint;


    private int oneEyeMargin;

    private int leftEyeHeight;

    private int leftEyeWidth;

    private int rightEyeHeight;

    private int rightEyeWidth;

    private int leftEyePositionLeft;
    private int leftEyePositionTop;
    private int leftEyePositionRight;
    private int leftEyePositionBottom;

    private int rightEyePositionLeft;
    private int rightEyePositionTop;
    private int rightEyePositionRight;
    private int rightEyePositionBottom;

    /**
     * 眼睛大小
     */
    private int eyeSize = 80;
    /**
     * 两眼之间的间距
     */
    private int eyeMargin = 150;
    /**
     * 眼睛变 方
     */
    private int squareSize;
    /**
     * 眨眼最小幅度
     */
    private int blinkAmplitudeSize;
    /**
     * 眨眼时间
     */
    private int blinkTime = 200;
    /**
     * 是否启动了眨眼动画 true 启动
     */
    private boolean isStartBlinkAnim = false;

    private ValueAnimator blinkSmallAnimator, blinkBigAnimator;

    public EyeView(Context context) {
        super(context);
        initView(context);
    }

    public EyeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EyeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        this.context = context;
        eyePaint = new Paint();
        eyePaint.setColor(ContextCompat.getColor(context, color));
        // 画笔属性是实心圆
        eyePaint.setStyle(Paint.Style.FILL);
        //画笔属性是空心圆
        //eyePaint.setStyle(Paint.Style.STROKE);
        //设置画笔粗细
        eyePaint.setStrokeWidth(3);
        eyePaint.setAntiAlias(true);

        //眼皮画笔
        eyelidPaint = new Paint();
        eyelidPaint.setColor(ContextCompat.getColor(context, color));
        eyelidPaint.setStyle(Paint.Style.FILL);
        eyelidPaint.setStrokeWidth(3);
        eyelidPaint.setAntiAlias(true);
    }

    private void resetSize() {
        //单眼间距
        oneEyeMargin = eyeMargin / 2;
        //左眼height位置
        leftEyeHeight = viewHeight / 2;
        //左眼width位置
        leftEyeWidth = (viewWidth / 2 - eyeSize) - oneEyeMargin;
        //右眼height位置
        rightEyeHeight = viewHeight / 2;
        //右眼width位置
        rightEyeWidth = (viewWidth / 2 + eyeSize) + oneEyeMargin;
        //眼睛变方
        squareSize = eyeSize;
        //左眼左边位置
        leftEyePositionLeft = leftEyeWidth - eyeSize;
        //左眼上边位置
        leftEyePositionTop = leftEyeHeight - eyeSize;
        //左眼右边位置
        leftEyePositionRight = leftEyeWidth + eyeSize;
        //左眼底边位置
        leftEyePositionBottom = leftEyeHeight + eyeSize;

        //右眼左边位置
        rightEyePositionLeft = rightEyeWidth - eyeSize;
        //右眼上边位置
        rightEyePositionTop = rightEyeHeight - eyeSize;
        //右眼右边位置
        rightEyePositionRight = rightEyeWidth + eyeSize;
        //右眼底边位置
        rightEyePositionBottom = rightEyeHeight + eyeSize;


        //眨眼幅度
        blinkAmplitudeSize = eyeSize / 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLeftEye(canvas);

        drawRightEye(canvas);

    }

    public void startBlinkAnim() {
        isStartBlinkAnim = true;
        handler.sendEmptyMessage(2);
    }

    public void stopBlinkAnim() {
        isStartBlinkAnim = false;
        handler.removeMessages(2);
    }

    public boolean isStartBlinkAnim() {
        return isStartBlinkAnim;
    }

    /**
     * 眼睛变大
     */
    public void small() {
        if (blinkSmallAnimator == null) {
            blinkSmallAnimator = blinkSmall(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    handler.sendEmptyMessage(3);
                }
            });
        }
        blinkSmallAnimator.start();
    }

    /**
     * 眼睛变小
     */
    public void big() {
        if (blinkBigAnimator == null) {
            blinkBigAnimator = blinkBig(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (isStartBlinkAnim) {
                        handler.sendEmptyMessageDelayed(2, 2000);
                    }
                }
            });
        }
        blinkBigAnimator.start();
    }


    public void square() {
        handler.sendEmptyMessage(1);
    }

    /**
     * 眨眼动画 眼睛变大 不提供启动
     *
     * @param listenerAdapter 动画 坚挺
     * @return ValueAnimator
     */
    private ValueAnimator blinkBig(AnimatorListenerAdapter listenerAdapter) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, eyeSize / 2);
        valueAnimator.setDuration(blinkTime);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int values = ((int) animation.getAnimatedValue());
                if (repeatValues == values) {
                    return;
                }
                repeatValues = values;

                if (leftEyePositionBottom - leftEyePositionTop <= eyeSize * 2) {

                    leftEyePositionTop -= values;
                    leftEyePositionBottom += values;

                    rightEyePositionTop -= values;
                    rightEyePositionBottom += values;


                    if ((leftEyeHeight - eyeSize) >= leftEyePositionTop) {
                        leftEyePositionTop = (leftEyeHeight - eyeSize);
                    }
                    if ((leftEyeHeight + eyeSize) <= leftEyePositionBottom) {
                        leftEyePositionBottom = (leftEyeHeight + eyeSize);
                    }
                    if ((rightEyeHeight - eyeSize) >= rightEyePositionTop) {
                        rightEyePositionTop = (rightEyeHeight - eyeSize);
                    }
                    if ((rightEyeHeight + eyeSize) <= rightEyePositionBottom) {
                        rightEyePositionBottom = (rightEyeHeight + eyeSize);
                    }

                    postInvalidate();
                } else if (values != 0) {
                    animation.cancel();
                }
            }
        });
        if (listenerAdapter != null) {
            valueAnimator.setInterpolator(new AccelerateInterpolator());
        }
        valueAnimator.addListener(listenerAdapter);
        return valueAnimator;
    }

    private int repeatValues;

    /**
     * 眨眼动画 眼睛变小 不提供启动
     *
     * @param listenerAdapter 动画 坚挺
     * @return ValueAnimator
     */
    private ValueAnimator blinkSmall(AnimatorListenerAdapter listenerAdapter) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, (leftEyePositionBottom - leftEyePositionTop) / 2);
        valueAnimator.setDuration(blinkTime);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int values = ((int) animation.getAnimatedValue());
                if (repeatValues == values) {
                    return;
                }
                repeatValues = values;
                if (leftEyePositionBottom - leftEyePositionTop >= blinkAmplitudeSize) {

                    leftEyePositionTop = (leftEyeHeight - eyeSize) + values;
                    leftEyePositionBottom = (leftEyeHeight + eyeSize) - values;

                    rightEyePositionTop = (rightEyeHeight - eyeSize) + values;
                    rightEyePositionBottom = (rightEyeHeight + eyeSize) - values;

                    postInvalidate();
                } else if (values != 0) {
                    animation.cancel();
                }
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        if (listenerAdapter != null) {
            valueAnimator.addListener(listenerAdapter);
        }
        return valueAnimator;
    }

    /**
     * 执行者
     */
    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                //眼睛变方
                if (squareSize > 20) {
                    handler.sendEmptyMessageDelayed(1, 50);
                }
                squareSize -= 2;
                postInvalidate();
            } else if (msg.what == 2) {
                //眨眼变小
                small();
            } else if (msg.what == 3) {
                //眨眼变大
                big();
            }

            return false;
        }
    });

    /**
     * 画左眼
     *
     * @param canvas 画布
     */
    private void drawLeftEye(Canvas canvas) {
        RectF oval3 = new RectF(leftEyePositionLeft, leftEyePositionTop, leftEyePositionRight, leftEyePositionBottom);
        canvas.drawRoundRect(oval3, squareSize, squareSize, eyePaint);
    }

    /**
     * 画右眼
     *
     * @param canvas 画布
     */
    private void drawRightEye(Canvas canvas) {
        RectF oval3 = new RectF(rightEyePositionLeft, rightEyePositionTop, rightEyePositionRight, rightEyePositionBottom);
        canvas.drawRoundRect(oval3, squareSize, squareSize, eyePaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取宽的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取高的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取宽的尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //获取高的尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        viewWidth = widthSize;
        viewHeight = heightSize;
        if (widthMode == MeasureSpec.EXACTLY) {

        } else {

        }
        if (heightMode == MeasureSpec.EXACTLY) {
            //如果match_parent或者具体的值，直接赋值

        } else {

        }

        //保存测量宽度和测量高度
        setMeasuredDimension(widthSize, heightSize);

        resetSize();
    }
}
