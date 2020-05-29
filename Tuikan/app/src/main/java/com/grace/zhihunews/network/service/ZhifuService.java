package com.grace.zhihunews.network.service;

import com.grace.zhihunews.network.entity.LoadApplies;
import com.grace.zhihunews.network.entity.LoadBooks;
import com.grace.zhihunews.network.entity.LoadFriends;
import com.grace.zhihunews.network.entity.BooksDetail;
import com.grace.zhihunews.network.entity.RecommondBooks;
import com.grace.zhihunews.network.entity.SocialComments;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2016/8/31.
 */
public interface ZhifuService {

    //https://www.fastmock.site/mock/1634373f74402d465e2c7dd9e40e743c/readPartner
    @GET("api/getPreBooks")
    Call<LoadBooks> getPreBooks();

    @GET("api/getRecBooks")
    Call<RecommondBooks> getRecBooks();

    @GET("api/getSocialComments")
    Call<SocialComments> getSocialComments();

    @GET("api/getLoadFriends")
    Call<LoadFriends> getLoadFriends();

    @GET("api/getLoadApplies")
    Call<LoadApplies> getLoadApplies();

    @GET("api/getBooksDetail/{id}")
    Call<BooksDetail> getBooksDetail(@Path("id") int id);
}
