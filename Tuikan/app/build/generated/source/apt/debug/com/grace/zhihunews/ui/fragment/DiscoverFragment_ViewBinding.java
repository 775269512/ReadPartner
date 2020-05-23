// Generated code from Butter Knife. Do not modify!
package com.grace.zhihunews.ui.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.grace.zhihunews.R;
import com.zanlabs.widget.infiniteviewpager.InfiniteViewPager;
import com.zanlabs.widget.infiniteviewpager.indicator.CirclePageIndicator;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DiscoverFragment_ViewBinding<T extends DiscoverFragment> implements Unbinder {
  protected T target;

  @UiThread
  public DiscoverFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.mSwipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.srl_discover, "field 'mSwipeRefreshLayout'", SwipeRefreshLayout.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.recommendList = Utils.findRequiredViewAsType(source, R.id.rv_recommend_list, "field 'recommendList'", RecyclerView.class);
    target.mViewPager = Utils.findRequiredViewAsType(source, R.id.view_pager, "field 'mViewPager'", InfiniteViewPager.class);
    target.mIndicator = Utils.findRequiredViewAsType(source, R.id.indicator, "field 'mIndicator'", CirclePageIndicator.class);
    target.rvHeader = Utils.findRequiredViewAsType(source, R.id.rv_header, "field 'rvHeader'", RecyclerViewHeader.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mSwipeRefreshLayout = null;
    target.toolbar = null;
    target.recommendList = null;
    target.mViewPager = null;
    target.mIndicator = null;
    target.rvHeader = null;

    this.target = null;
  }
}