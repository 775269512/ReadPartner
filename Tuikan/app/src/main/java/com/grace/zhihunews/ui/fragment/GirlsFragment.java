package com.grace.zhihunews.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.grace.zhihunews.App;
import com.grace.zhihunews.PresenterCompl.GirlsPresenterCompl;
import com.grace.zhihunews.PresenterCompl.NewsDetailPresenterCompl;
import com.grace.zhihunews.PresenterCompl.NewsListPresenterCompl;
import com.grace.zhihunews.R;
import com.grace.zhihunews.contract.GirlsContact;
import com.grace.zhihunews.contract.NewsListContact;
import com.grace.zhihunews.network.entity.BeforeNews;
import com.grace.zhihunews.network.entity.Girl;
import com.grace.zhihunews.network.entity.LatestNews;
import com.grace.zhihunews.network.entity.Story;
import com.grace.zhihunews.network.entity.TopStory;
import com.grace.zhihunews.ui.activity.NewsDetailActivity;
import com.grace.zhihunews.ui.adapter.GirlsAdapter;
import com.grace.zhihunews.ui.adapter.StoriesAdapter;
import com.grace.zhihunews.ui.adapter.TopStoriesAdapter;
import com.grace.zhihunews.ui.base.BaseFragment;
import com.grace.zhihunews.ui.listener.EndlessRecyclerViewScrollListener;
import com.grace.zhihunews.util.DateUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zanlabs.widget.infiniteviewpager.InfiniteViewPager;
import com.zanlabs.widget.infiniteviewpager.indicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/9/2.
 */
public class GirlsFragment extends BaseFragment implements GirlsContact.IGirlsView {
    @BindView(R.id.srl)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_news_list)
    RecyclerView rvNewsList;
    @BindView(R.id.view_pager)
    InfiniteViewPager mViewPager;
    @BindView(R.id.indicator)
    CirclePageIndicator mIndicator;
    @BindView(R.id.rv_header)
    RecyclerViewHeader rvHeader;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private List<String> dateList;
    private Unbinder unbinder;
    private List<Story> mStories;
    private StoriesAdapter storiesAdapter;
    private GirlsContact.IGirlsPresenter mGirlsPresenter;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_girls;
    }

    @Override
    protected void initVariables() {
        mStories = new ArrayList<>();
        dateList = new ArrayList<>();
        storiesAdapter = new StoriesAdapter(getActivity(), mStories);
        mGirlsPresenter = new GirlsPresenterCompl((App) getActivity().getApplicationContext(), this);
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvNewsList.setLayoutManager(linearLayoutManager);
        rvNewsList.setAdapter(storiesAdapter);
        rvNewsList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .colorResId(R.color.divider_grey)
                .size(getResources().getDimensionPixelSize(R.dimen.divider_height))
                .margin(getResources().getDimensionPixelSize(R.dimen.spacing_normal_high),
                        getResources().getDimensionPixelSize(R.dimen.spacing_normal_high))
                .build());

        rvHeader.attachTo(rvNewsList, true);
        rvNewsList.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                String farthestDate;
                farthestDate = dateList.get(dateList.size() - 1);
                Log.d("farthestDate", farthestDate);
                String previousDate = DateUtil.getPreviousDay(farthestDate);
                Log.d("previousDate", previousDate);
                dateList.add(previousDate);
                for (int i = 0; i < dateList.size(); i++) {
                    Log.d("dataList", i + dateList.get(i));
                }
                mGirlsPresenter.loadBeforeNews(previousDate);
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);

            mGirlsPresenter.refreshData();
            String latestDate = DateUtil.getLatestDate();
            if (dateList != null) {
                dateList.clear();
                dateList.add(latestDate);
            }
            mGirlsPresenter.loadLatestNews();

            (new Handler()).postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1200);
        });
        fab.setOnClickListener(v -> rvNewsList.smoothScrollToPosition(0));

    }

    @Override
    protected void loadData() {
        //mNewsListPresenter.loadTopStories(false);

        String latestDate = DateUtil.getLatestDate();
        dateList.add(latestDate);
        mGirlsPresenter.loadLatestNews();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showBeforeNews(BeforeNews beforeNews) {
        List<Story> stories = beforeNews.getStories();
        mStories.addAll(stories);
        int curSize = storiesAdapter.getItemCount();
        storiesAdapter.notifyItemRangeChanged(curSize, mStories.size() - 1);
    }

    @Override
    public void gotoNewsDetailActivity(int id) {
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra(NewsDetailActivity.KEY_STORY_ID, id);
        startActivity(intent);
        //overridePendingTransition(R.anim.hold, android.R.anim.fade_in);
    }


    @Override
    public void showLatestNews(LatestNews latestNews) {
        List<TopStory> topStories = latestNews.getTopStories();
        TopStoriesAdapter topStoriesAdapter = new TopStoriesAdapter(getActivity(), topStories);
        mViewPager.setAdapter(topStoriesAdapter);
        mViewPager.setAutoScrollTime(3000);
        mViewPager.startAutoScroll();
        mIndicator.setViewPager(mViewPager);

        mStories.clear();
        List<Story> stories = latestNews.getStories();
        mStories.addAll(stories);
        storiesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadFailureMsg(String errorMsg) {

    }
}
