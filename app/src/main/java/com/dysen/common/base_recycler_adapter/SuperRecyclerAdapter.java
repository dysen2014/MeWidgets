package com.dysen.common.base_recycler_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @email dy.sen@qq.com
 * created by dysen on 2018/9/19 - 上午10:38
 * @info
 */
public abstract class SuperRecyclerAdapter<T> extends RecyclerView.Adapter<SuperRecyclerHolder>{
    private Context mCtx;
    private List<T> mValueList = new ArrayList<>();

    public static final int HEADER_VIEW = 0x00000111;
    public static final int LOADING_VIEW = 0x00000222;
    public static final int FOOTER_VIEW = 0x00000333;
    public static final int EMPTY_VIEW = 0x00000555;

    public void setDatas(List<T> items) {
        mValueList.clear();
        mValueList.addAll(items);
        notifyDataSetChanged();
    }

    public SuperRecyclerAdapter() {
    }

    public SuperRecyclerAdapter(List<T> mValueList) {
        this.mValueList = mValueList;
    }

    public SuperRecyclerAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    public SuperRecyclerAdapter(Context mCtx, List<T> mValueList) {
        this.mCtx = mCtx;
        this.mValueList = mValueList;
    }

    @Override
    public SuperRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        mCtx = parent.getContext();
        View itemView = LayoutInflater.from(mCtx).inflate(viewType, parent, false);
        return SuperRecyclerHolder.createViewHolder(mCtx, itemView);
    }

    @Override
    public void onBindViewHolder(SuperRecyclerHolder holder, int position) {
        convert(holder, getValue(position), getItemViewType(position), position);
    }

    @Override
    public int getItemCount() {
        return mValueList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutAsViewType(getValue(position), position);
    }

    public List<T> getValueList() {
        return mValueList;
    }

    public T getValue(int position) {
        return getValueList().get(position);
    }

    public abstract void convert(SuperRecyclerHolder holder, T t, int layoutType, int position);

    @LayoutRes
    public abstract int getLayoutAsViewType(T t, int position);
}
