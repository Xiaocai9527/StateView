package cn.xiaokun.stateview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.xiaokun.state_view.StateView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mLoading;
    private Button mEmpty;
    private Button mError;
    private Button mContent;
    private Button mNoNetView;
    private StateView mStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener(mLoading, mEmpty, mError, mNoNetView, mContent);
    }

    private void initView() {
        mLoading = findViewById(R.id.loading);
        mEmpty = findViewById(R.id.empty);
        mError = findViewById(R.id.error);
        mNoNetView = findViewById(R.id.no_net);
        mContent = findViewById(R.id.content);

        mStateView = findViewById(R.id.state_view);

        mStateView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStateView.showLoading();
                //执行网络加载
            }
        });

        mStateView.setOnRefreshClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStateView.showLoading();
            }
        });
    }

    private void initListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loading:
                mStateView.showLoading();
                break;
            case R.id.empty:
                mStateView.showEmpty();
                break;
            case R.id.error:
                mStateView.showError();
                break;
            case R.id.no_net:
                mStateView.showNoNet();
                break;
            case R.id.content:
                mStateView.showContent();
                break;
            default:
                break;
        }
    }
}
