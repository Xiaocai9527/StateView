package xiaokun.com.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import static android.provider.Settings.ACTION_WIRELESS_SETTINGS;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/03/26
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */

public class StateView extends FrameLayout
{
    /**
     * 空布局
     */
    private int emptyView;
    /**
     * 加载布局
     */
    private int loadingView;
    /**
     * 错误布局
     */
    private int errorView;
    /**
     * 手机无网络布局
     */
    private int noNetView;
    /**
     * 内容布局
     */
    private int contentView;
    /**
     * 加载动画
     */
    private int animation;

    private StateView mStateView;

    /**
     * 点击重试接口
     */
    private OnClickListener onRetryClickListener;
    /**
     * 点击刷新接口
     */
    private OnClickListener onRefreshClickListener;

    public StateView(@NonNull Context context)
    {
        this(context, null);
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateView, defStyleAttr, 0);
        try
        {
            emptyView = typedArray.getResourceId(R.styleable.StateView_emptyView, R.layout.state_empty_view);
            loadingView = typedArray.getResourceId(R.styleable.StateView_loadingView, R.layout.state_loading_view);
            errorView = typedArray.getResourceId(R.styleable.StateView_errorView, R.layout.state_error_view);
            noNetView = typedArray.getResourceId(R.styleable.StateView_noNetView, R.layout.state_no_net_view);
            contentView = typedArray.getResourceId(R.styleable.StateView_contentView, 0);
            animation = typedArray.getResourceId(R.styleable.StateView_animation, 0);

            LayoutInflater inflater = LayoutInflater.from(context);
            //第一空布局 第二错误布局  第三加载布局  第四无网络连接布局
            inflater.inflate(emptyView, this, true);
            inflater.inflate(errorView, this, true);
            inflater.inflate(loadingView, this, true);
            inflater.inflate(noNetView, this, true);
            inflater.inflate(contentView, this, true);
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            typedArray.recycle();
            mStateView = this;
        }
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        int size = getChildCount() - 1;
        //默认保留最后一个add进去的子view，也就是contentView
        for (int i = 0; i < size; i++)
        {
            getChildAt(i).setVisibility(GONE);
        }

        findViewById(R.id.btn_retry).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (onRetryClickListener != null)
                {
                    onRetryClickListener.onClick(v);
                }
            }
        });

        findViewById(R.id.btn_refresh).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (onRefreshClickListener != null)
                {
                    onRefreshClickListener.onClick(v);
                }
            }
        });

        findViewById(R.id.btn_set_network).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //跳转到网络设置
                Activity currentActivity = (Activity) mStateView.getContext();
                //Wi-Fi, Bluetooth and Mobile networks.
                currentActivity.startActivity(new Intent(ACTION_WIRELESS_SETTINGS));
                //Wi-Fi
                //currentActivity.startActivity(new Intent(ACTION_WIFI_SETTINGS));
                //Mobile networks
                //currentActivity.startActivity(new Intent(ACTION_DATA_ROAMING_SETTINGS));
            }
        });
    }

    /**
     * 设置点击重试按钮
     *
     * @param onRetryClickListener
     */
    public void setOnRetryClickListener(OnClickListener onRetryClickListener)
    {
        this.onRetryClickListener = onRetryClickListener;
    }

    /**
     * 设置刷新按钮
     *
     * @param onRefreshClickListener
     */
    public void setOnRefreshClickListener(OnClickListener onRefreshClickListener)
    {
        this.onRefreshClickListener = onRefreshClickListener;
    }

    /**
     * 显示默认空布局
     */
    public void showEmpty()
    {
        this.showEmpty(null, 0);
    }

    public void showEmpty(String info, int resId)
    {
        show(info, resId, 0);
    }

    /**
     * 显示默认错误布局
     */
    public void showError()
    {
        this.showError(null, 0);
    }

    public void showError(String info, int resId)
    {
        show(info, resId, 1);
    }

    /**
     * 显示默认加载布局
     */
    public void showLoading()
    {
        showLoading(null);
    }

    public void showLoading(String info)
    {
        show(info, 0, 2);
    }

    /**
     * 显示无网络连接布局
     */
    public void showNoNet()
    {
        this.showNoNet(null, 0);
    }

    public void showNoNet(String info, int resId)
    {
        show(info, resId, 3);
    }

    /**
     * 显示内容布局
     */
    public void showContent()
    {
        for (int i = 0; i < this.getChildCount(); i++)
        {
            View child = this.getChildAt(i);
            if (i == 4 || child.getId() == contentView)
            {
                child.setVisibility(VISIBLE);
            } else
            {
                child.setVisibility(GONE);
            }
        }
    }

    private void show(String info, int resId, int index)
    {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View child = getChildAt(i);
            //根据index来显示和隐藏
            if (i == index)
            {
                setInfoCustom(info, resId, child);
                child.setVisibility(VISIBLE);
            } else
            {
                child.setVisibility(GONE);
            }
        }
    }

    /**
     * 自定义设置各布局中info和图片信息
     *
     * @param info
     * @param resId
     * @param child
     */
    private void setInfoCustom(String info, int resId, View child)
    {
        if (!TextUtils.isEmpty(info))
        {
            TextView tv = (TextView) child.findViewById(R.id.state_info);
            if (tv != null)
            {
                tv.setText(info);
            }
        }
        if (resId > 0)
        {
            //loading布局是没有imageView的
            try
            {
                ImageView iv = (ImageView) child.findViewById(R.id.state_icon);
                if (iv != null)
                {
                    iv.setImageResource(resId);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
