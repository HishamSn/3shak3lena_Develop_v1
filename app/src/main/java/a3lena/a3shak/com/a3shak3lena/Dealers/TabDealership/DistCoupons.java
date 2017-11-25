package a3lena.a3shak.com.a3shak3lena.Dealers.TabDealership;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import a3lena.a3shak.com.a3shak3lena.Dealers.Category.CategoryAdapter;
import a3lena.a3shak.com.a3shak3lena.Dealers.Category.CategoryModel;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.util.util;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aldaboubi on 10/10/2017.
 */

public class DistCoupons extends Fragment{

    ArrayList<CategoryModel> list = new ArrayList<>();
    CategoryAdapter adapter;

    Handler handler;
    Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.exchange_coupons, container, false);

        handler = new Handler();
        RecyclerView my_recycler_view = (RecyclerView) myView.findViewById(R.id.recycler_given);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        my_recycler_view.setHasFixedSize(true);
        adapter = new CategoryAdapter(getActivity(), list);
        my_recycler_view.setLayoutManager(linearLayoutManager);
        my_recycler_view.setAdapter(adapter);

        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, 7000);
                util.getDealer(getActivity(),"2",list, adapter, handler, runnable);
            }
        };
        handler.postDelayed(runnable, 0);

        return myView;
    }

}
