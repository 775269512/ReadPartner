// Generated code from Butter Knife. Do not modify!
package com.grace.zhihunews.ui.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.grace.zhihunews.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SimilarBookAdapter$ViewHolder_ViewBinding<T extends SimilarBookAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public SimilarBookAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.bookImg = Utils.findRequiredViewAsType(source, R.id.book_img, "field 'bookImg'", ImageView.class);
    target.bookTitle = Utils.findRequiredViewAsType(source, R.id.book_title, "field 'bookTitle'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.bookImg = null;
    target.bookTitle = null;

    this.target = null;
  }
}
