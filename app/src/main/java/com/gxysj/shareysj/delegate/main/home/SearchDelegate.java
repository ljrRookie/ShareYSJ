package com.gxysj.shareysj.delegate.main.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.utils.SPUtil;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.progressing.animation.interpolator.KeyFrameInterpolator;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/31.
 */

public class SearchDelegate extends YSJDelegate {
    private static final String All_KEY = "all";
    private static final String GOODS_KEY = "goods";
    private static final String AD_KEY = "ad";

    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.edit_key)
    EditText mEditKey;
    @BindView(R.id.btn_search)
    Button mBtnSearch;
    @BindView(R.id.ll_toolbar)
    LinearLayout mLlToolbar;

    @BindView(R.id.btn_del)
    ImageView mBtnDel;
    @BindView(R.id.tag)
    TagFlowLayout mTag;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    Unbinder unbinder;
    private List<String> historyList = new ArrayList<>();

    @Override

    public Object setLayout() {
        return R.layout.delegate_search;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
    showTag();
        initEditText();
    }

    private void initEditText() {
        mEditKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = mEditKey.getText().toString().trim();
                    mEditKey.setText("");
                   doSearch(key);

                }
                return false;
            }
        });
    }


    private void showTag() {
        historyList.clear();
            Map<String, ?> all = SPUtil.getAll(getContext(), All_KEY);
            for (Map.Entry<String, ?> entry : all.entrySet()) {
                historyList.add((String) entry.getValue());
        }
        if (historyList != null && historyList.size() > 0) {
            mTag.setVisibility(View.VISIBLE);
            mTvEmpty.setVisibility(View.GONE);
            mTag.setAdapter(new TagAdapter<String>(historyList) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_tag,
                            mTag, false);
                    tv.setText(s);
                    return tv;
                }
            });
            mTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    String key = historyList.get(position);
                    doSearch(key);
                    return false;
                }
            });
        } else {
            mTag.setVisibility(View.GONE);
            mTvEmpty.setVisibility(View.VISIBLE);
        }


    }


    @OnClick({R.id.btn_back, R.id.btn_search, R.id.btn_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_search:
                String key = mEditKey.getText().toString().trim();
                if (TextUtils.isEmpty(key)) {
                    RxToast.normal("搜索内容不能为空");
                } else {
                    doSearch(key);

                }

                break;
            case R.id.btn_del:
                SPUtil.clear(getContext(), All_KEY);
                mTag.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void doSearch(String key) {
        RxToast.normal(key);
        SPUtil.put(getContext(), All_KEY, key, key);
        showTag();
getSupportDelegate().start(SearchADDelegate.create(key));
    }

}
