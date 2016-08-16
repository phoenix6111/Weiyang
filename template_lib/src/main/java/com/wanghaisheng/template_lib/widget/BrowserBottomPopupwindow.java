package com.wanghaisheng.template_lib.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wanghaisheng.template_lib.R;
import com.wanghaisheng.template_lib.component.flowlayout.TagFlowLayout;


/**
 * Created by sheng on 2016/6/8.
 */
public class BrowserBottomPopupwindow extends PopupWindow implements View.OnClickListener{

    public static final int FONT_SIZE_S = 0X001;
    public static final int FONT_SIZE_M = 0X002;
    public static final int FONT_SIZE_L = 0X003;
    public static final int FONT_SIZE_XL = 0X004;

    private View mConvertView;
    private SeekBar seekBar;
    TagFlowLayout tagFlowLayout;
    TextView sTextView;
    TextView mTextView;
    TextView lTextView;
    TextView xlTextView;

    TextView tvComplete;

    BrowserPopupwindowListenter listenter;

    public void setBrowserPopupwindowListener(BrowserPopupwindowListenter listener) {
        this.listenter = listener;
    }

    public void setLightValue(float value) {
        int bright = (int) value;
        seekBar.setProgress(bright);
    }

    @Override
    public void onClick(View v) {
        if(R.id.font_s == v.getId()) {
            listenter.fontSelect(FONT_SIZE_S);
        } else if(R.id.font_m == v.getId()) {
            listenter.fontSelect(FONT_SIZE_M);
        } else if(R.id.font_l == v.getId()) {
            listenter.fontSelect(FONT_SIZE_L);
        } else if(R.id.font_xl == v.getId()) {
            listenter.fontSelect(FONT_SIZE_XL);
        }

    }

    public interface BrowserPopupwindowListenter {
        void lightOnclick(int progress);

        void fontSelect(int font);
    }

    public BrowserBottomPopupwindow(Context context) {

        this.mConvertView = LayoutInflater.from(context).inflate(R.layout.bottom_popupwindow,null);
        setContentView(mConvertView);

        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.anim_dir_popwindow);
        setOutsideTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        initView();

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //如果在PopupWindow外面点击，则让它消失
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }

                return false;
            }
        });


    }

    private void initView() {
        this.seekBar = (SeekBar) mConvertView.findViewById(R.id.brightness_filter_seekbar);
        seekBar.setMax(255);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                LogUtils.d(" progress   "+progress);
                listenter.lightOnclick(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tagFlowLayout = (TagFlowLayout) mConvertView.findViewById(R.id.id_flowlayout);
        sTextView = (TextView) mConvertView.findViewById(R.id.font_s);
        sTextView.setOnClickListener(this);
        mTextView = (TextView) mConvertView.findViewById(R.id.font_m);
        mTextView.setOnClickListener(this);
        lTextView = (TextView) mConvertView.findViewById(R.id.font_l);
        lTextView.setOnClickListener(this);
        xlTextView = (TextView) mConvertView.findViewById(R.id.font_xl);
        xlTextView.setOnClickListener(this);

        tvComplete = (TextView) mConvertView.findViewById(R.id.tv_complete);
        tvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
