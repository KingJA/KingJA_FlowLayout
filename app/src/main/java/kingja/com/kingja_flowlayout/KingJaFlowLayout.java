package kingja.com.kingja_flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinelon on 2016/1/9.
 */
public class KingJaFlowLayout extends ViewGroup {

    private int DEFAULT_SPACING = App.dip2px(10);
    private int mHorizontalSpacing = DEFAULT_SPACING;
    private int mVerticalSpacing = DEFAULT_SPACING;
    private Line mLine;
    private List<Line> mLines = new ArrayList<>();
    private int mCurrentWidth;
    private LayoutParams childLayoutParams;
    private int childrenWidth;
    private boolean ifAverage;

    public KingJaFlowLayout(Context context) {
        this(context, null);
    }

    public KingJaFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KingJaFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AttrKingJaFlowLayout);
        float horizontalSpacing = typedArray.getDimension(R.styleable.AttrKingJaFlowLayout_horizontalSpacing, DEFAULT_SPACING);
        float verticalSpacing = typedArray.getDimension(R.styleable.AttrKingJaFlowLayout_verticalSpacing, DEFAULT_SPACING);
        ifAverage = typedArray.getBoolean(R.styleable.AttrKingJaFlowLayout_average, false);
        typedArray.recycle();
        mHorizontalSpacing = (int) horizontalSpacing;
        mVerticalSpacing = (int) verticalSpacing;
        //背景色
        setBackgroundColor(0xFFEEEEEE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //布局区域
        childrenWidth = widthSize - getPaddingLeft() - getPaddingRight();
        int childrenHeight = heightSize - getPaddingTop() - getPaddingBottom();
        reset();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            if (mLine == null) {
                mLine = new Line();
            }
            int measuredWidth = child.getMeasuredWidth();
            //子View最大宽度不超过内容区域
            if (measuredWidth> childrenWidth){
                child.measure(MeasureSpec.makeMeasureSpec(childrenWidth, MeasureSpec.EXACTLY),childHeightMeasureSpec);
            }
            mCurrentWidth += measuredWidth;
            //不超过一行

            if (mCurrentWidth <= childrenWidth) {
                mLine.addView(child);
                mCurrentWidth += mHorizontalSpacing;
                //加上间距后超过一行
                if (mCurrentWidth >= childrenWidth) {
                    newLine();
                }
                //超过一行
            } else {
                if (mLine.size() == 0) {
                    mLine.addView(child);
                    newLine();
                } else {
                    newLine();
                    mLine.addView(child);
                    mCurrentWidth += child.getMeasuredWidth() + mHorizontalSpacing;
                }

            }
//把最后一行加到集合里

        }
        if (mLine != null && mLine.size() > 0) {
            mLines.add(mLine);
        }
        int totelHeight = 0;
        if (heightMode == MeasureSpec.EXACTLY) {
            totelHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            for (int i = 0; i < mLines.size(); i++) {
                int lineHeight = mLines.get(i).getLineHeight();
                totelHeight += lineHeight;
                if (i != 0) {
                    totelHeight += mVerticalSpacing;
                }
            }
            totelHeight = Math.min(childrenHeight, totelHeight);
        }

        setMeasuredDimension(widthSize, totelHeight + getPaddingTop() + getPaddingBottom());
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
            for (int i = 0; i < mLines.size(); i++) {
                if (i == 0) {
                    l = getPaddingLeft();
                    t = getPaddingTop();
                }
                Line line = mLines.get(i);
                line.laytou(l, t);
                t += line.getLineHeight() + mVerticalSpacing;
            }
    }

    private void newLine() {
        if (mLine != null && mLine.size() > 0 && !mLines.contains(mLine)) {
            mLines.add(mLine);
        }
        mLine = new Line();
        mCurrentWidth = 0;
    }

    private void reset() {
        mLine = new Line();
        mLines.clear();
        mCurrentWidth = 0;
    }

    class Line {
        private List<View> line = new ArrayList<>();
        private int mMaxHeight;
        private int mCurrentWidth;

        public void addView(View view) {
            line.add(view);
            mMaxHeight = Math.max(mMaxHeight, view.getMeasuredHeight());
            if (mCurrentWidth>0){
                mCurrentWidth+=mHorizontalSpacing;
            }
            mCurrentWidth+=view.getMeasuredWidth();
        }

        public int size() {
            return line.size();
        }

        public int getLineHeight() {
            return mMaxHeight;
        }

        public void laytou(int l, int t) {
            if (ifAverage){
                int totelLeftWidth=childrenWidth-mCurrentWidth;
                int perLeftWidth= (int) (totelLeftWidth/(line.size()*1.0f)+0.5f);
                for (int i = 0; i < line.size(); i++) {
                    View view = line.get(i);
                    int measureSpeWidth = MeasureSpec.makeMeasureSpec(view.getMeasuredWidth() + perLeftWidth, MeasureSpec.EXACTLY);
                    int measureSpeHeight = MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), MeasureSpec.EXACTLY);
                    view.measure(measureSpeWidth,measureSpeHeight);
                }
            }
            for (int i = 0; i < line.size(); i++) {
                View view = line.get(i);
                int top = t + (int) ((mMaxHeight - view.getMeasuredHeight()) / 2.0f + 0.5f);
                int right = l + view.getMeasuredWidth();
                int bottom = top + view.getMeasuredHeight();
                view.layout(l, top, right, bottom);
                l += view.getMeasuredWidth() + mHorizontalSpacing;
            }

        }
    }
}
