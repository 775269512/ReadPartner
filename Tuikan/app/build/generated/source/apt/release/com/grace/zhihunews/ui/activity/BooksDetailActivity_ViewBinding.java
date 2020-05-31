// Generated code from Butter Knife. Do not modify!
package com.grace.zhihunews.ui.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.grace.zhihunews.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BooksDetailActivity_ViewBinding<T extends BooksDetailActivity> implements Unbinder {
  protected T target;

  @UiThread
  public BooksDetailActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.ivHeaderImg = Utils.findRequiredViewAsType(source, R.id.iv_header_img, "field 'ivHeaderImg'", ImageView.class);
    target.tvHeaderTitle = Utils.findRequiredViewAsType(source, R.id.tv_header_title, "field 'tvHeaderTitle'", TextView.class);
    target.tvImgSource = Utils.findRequiredViewAsType(source, R.id.tv_img_source, "field 'tvImgSource'", TextView.class);
    target.wbNewsContent = Utils.findRequiredViewAsType(source, R.id.wb_story_content, "field 'wbNewsContent'", WebView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivHeaderImg = null;
    target.tvHeaderTitle = null;
    target.tvImgSource = null;
    target.wbNewsContent = null;

    this.target = null;
  }
}
