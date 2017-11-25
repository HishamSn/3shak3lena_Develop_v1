package a3lena.a3shak.com.a3shak3lena.Dealers.TabDealership;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class ExchangeCoupons extends Fragment{

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
                util.getDealer(getActivity(),"1",list, adapter, handler, runnable);
            }
        };
        handler.postDelayed(runnable, 0);

        return myView;
    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        handler.removeCallbacks(runnable);
//        handler.removeCallbacksAndMessages(null);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        handler.removeCallbacks(runnable);
//        handler.removeCallbacksAndMessages(null);
//    }
//
//
//
//    public void dialogErrorInternet(final Activity activity){
//        LayoutInflater inflater = activity.getLayoutInflater();
//        View v = inflater.inflate(R.layout.check_internet_connection, null);
//
//        GifImageView gifImageView = v.findViewById(R.id.gif_internet);
//        TextView txtInternet = (TextView) v.findViewById(R.id.txtinternet);
//        Button btnRetry = (Button) v.findViewById(R.id.btnRetry);
//
//        txtInternet.setTypeface(util.changeFont(activity));
//        btnRetry.setTypeface(util.changeFont(activity));
//
//        final Dialog dialog = new Dialog(activity, R.style.DialogThemeWhite);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        btnRetry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(util.haveNetworkConnection(activity)){
//                    runnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            handler.postDelayed(runnable,3000);
//                            util.getDealer(getActivity(),"1",list, adapter,handler,runnable);
//                            dialog.dismiss();
//                        }
//                    };
//                    handler.postDelayed(runnable, 0);
//                }
//            }
//        });
//        dialog.setContentView(v);
//        dialog.show();
//        Window window_register = dialog.getWindow();
//        window_register.setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
//    }
//
//


}
