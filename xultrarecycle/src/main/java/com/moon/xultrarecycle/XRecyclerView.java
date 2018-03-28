package com.moon.xultrarecycle;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * author: moon
 * created on: 17/10/18 下午12:29
 * description:
 */
public class XRecyclerView extends FrameLayout implements LoadMoreRecyclerView.OnLoadMoreListener ,
        LoadMoreRecyclerView.IDataChangeObserver{

    private LoadMoreRecyclerView recyclerview;
    private XRParallaxPrtFrameLayout ptr_freash;
    private boolean isRefreshing = false;

    public void setDataChangeListener(DataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

    private DataChangeListener dataChangeListener;

    public XRecyclerView(Context context) {
        super(context);
        init();
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.xrefreshview, this);
        recyclerview =  findViewById(R.id.recyclervieww);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setMyLayoutManager(linearLayoutManager);
        recyclerview.setOnLoadMoreListener(this);

        ptr_freash =  findViewById(R.id.ptr_refresh);
        ptr_freash.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {
                if (recyclerview.getChildCount() == 0 || recyclerview.getChildCount() > 0 && linearLayoutManager.findFirstVisibleItemPosition() == 0 && recyclerview.getChildAt(0).getTop() >= 0) {
                    return true;
                }
                return false;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (onListListener != null && !isRefreshing) {
                    isRefreshing = true;

                    loading();

                    onListListener.onRefreshing();
                }
            }
        });
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerview.setAdapter(adapter);
        recyclerview.setDataChangeObserver(this);
    }

    @Override
    public void onLoading() {
        if (onListListener != null && !isRefreshing && !recyclerview.isCanCelLoadMore()) {
            onListListener.onLoadingMore();

            loading();
        }
    }

    private OnListListener onListListener;

    @Override
    public void dataChange(boolean isEmpty) {
        if (dataChangeListener !=null){
            dataChangeListener.change(isEmpty);
        }
    }

    public interface OnListListener {
        void onLoadingMore();

        void onRefreshing();
    }

    public void setOnListListener(OnListListener onListListener) {
        this.onListListener = onListListener;
    }

    public void loading() {
        recyclerview.loading();
    }

    public void loadingComplete() {
        recyclerview.loadingComplete();
    }

    public void refreshComplete() {
        isRefreshing = false;
        if (ptr_freash.isRefreshing()){
            ptr_freash.refreshComplete();
        }
    }

    public void setTopPading(int topPading) {
        recyclerview.setPadding(0, topPading, 0, 0);
    }

    /**
     * 关闭上拉加载更多功能
     */
    public void prohibitLoadMore() {
        if (recyclerview != null) {
            recyclerview.hideLoadMore();
        }
    }

    /**
     * 关闭下拉刷新功能
     */
    public void prohibitPull() {
        if (ptr_freash!=null){
//            ptr_freash.
        }
    }

    public interface DataChangeListener {
        void change(boolean isEmpty) ;
    }

    public void autoRefresh(){
        ptr_freash.autoRefresh();
    }

    // TODO: 17/11/13  
    public void destroy(){
        onListListener = null;

        if (ptr_freash!=null){
            ptr_freash.setPtrHandler(null);
            ptr_freash = null;
        }
        
        recyclerview = null;
        
    }

}
