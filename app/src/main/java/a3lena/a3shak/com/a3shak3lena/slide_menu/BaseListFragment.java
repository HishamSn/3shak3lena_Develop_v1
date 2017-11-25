package a3lena.a3shak.com.a3shak3lena.slide_menu;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import a3lena.a3shak.com.a3shak3lena.About.About;
import a3lena.a3shak.com.a3shak3lena.About.Terms;
import a3lena.a3shak.com.a3shak3lena.Auth.Login;
import a3lena.a3shak.com.a3shak3lena.Auth.MyProfile;
import a3lena.a3shak.com.a3shak3lena.Coupons.MyCoupons;
import a3lena.a3shak.com.a3shak3lena.Dealers.DealerDetails;
import a3lena.a3shak.com.a3shak3lena.Dealers.DealersMain;
import a3lena.a3shak.com.a3shak3lena.Main;
import a3lena.a3shak.com.a3shak3lena.Map.MapDealers;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.slide_menu.recycler.SliderMenuRight.SlideMenuModel;
import a3lena.a3shak.com.a3shak3lena.slide_menu.recycler.SliderMenuRight.SliderMenuAdapter;
import a3lena.a3shak.com.a3shak3lena.util.util;
import a3lena.a3shak.com.slidemenu.SlideMenu;
import a3lena.a3shak.com.sweet_dialog.SweetAlertDialog;


public class BaseListFragment extends Fragment implements View.OnClickListener {


    TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9;
    Intent i;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Handler handlerDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_slidemenu, container, false);
        init(view);
        view.findViewById(R.id.b1).setOnClickListener(this);
        view.findViewById(R.id.b2).setOnClickListener(this);
        view.findViewById(R.id.b3).setOnClickListener(this);
        view.findViewById(R.id.b4).setOnClickListener(this);
        view.findViewById(R.id.b5).setOnClickListener(this);
        view.findViewById(R.id.b6).setOnClickListener(this);
        view.findViewById(R.id.b7).setOnClickListener(this);
        view.findViewById(R.id.b8).setOnClickListener(this);
        view.findViewById(R.id.b9).setOnClickListener(this);

        return view;
    }


    public void init(View v) {
        txt1 = (TextView) v.findViewById(R.id.txt1);
        txt2 = (TextView) v.findViewById(R.id.txt2);
        txt3 = (TextView) v.findViewById(R.id.txt3);
        txt4 = (TextView) v.findViewById(R.id.txt4);
        txt5 = (TextView) v.findViewById(R.id.txt5);
        txt6 = (TextView) v.findViewById(R.id.txt6);
        txt7 = (TextView) v.findViewById(R.id.txt7);
        txt8 = (TextView) v.findViewById(R.id.txt8);
        txt9 = (TextView) v.findViewById(R.id.txt9);


        txt1.setTypeface(util.changeFont(getActivity()));
        txt2.setTypeface(util.changeFont(getActivity()));
        txt3.setTypeface(util.changeFont(getActivity()));
        txt4.setTypeface(util.changeFont(getActivity()));
        txt5.setTypeface(util.changeFont(getActivity()));
        txt6.setTypeface(util.changeFont(getActivity()));
        txt7.setTypeface(util.changeFont(getActivity()));
        txt8.setTypeface(util.changeFont(getActivity()));
        txt9.setTypeface(util.changeFont(getActivity()));

        sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String auth;
        if (sharedPreferences.getBoolean("loggedin", false)) {
            auth = "تسجيل خروج";
        } else {
            auth = "تسجيل دخول";
        }
        txt9.setText(auth);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.b1:
                if (getActivity().getClass().getSimpleName().equals("Main")) {
                    ((Main) getActivity()).getSlideMenu().close(true);
                } else {
                    if (util.haveNetworkConnection(getActivity())) {
                        i = new Intent(getContext(), Main.class);
                        startActivity(i);
                    } else {
                        util.dialogErrorInternet(getActivity());
                    }
                }
                break;
            case R.id.b2:
                if (getActivity().getClass().getSimpleName().equals("DealersMain")) {
                    ((DealersMain) getActivity()).getSlideMenu().close(true);
                    ((DealersMain) getActivity()).selectTab(0);

                } else {
                    if (util.haveNetworkConnection(getActivity())) {
                        i = new Intent(getContext(), DealersMain.class);
                        i.putExtra("numTab", 0);
                        startActivity(i);
                    } else {
                        util.dialogErrorInternet(getActivity());
                    }
                }
                break;
            case R.id.b3:
                if (getActivity().getClass().getSimpleName().equals("DealersMain")) {
                    ((DealersMain) getActivity()).getSlideMenu().close(true);
                    ((DealersMain) getActivity()).selectTab(1);
                } else {
                    if (util.haveNetworkConnection(getActivity())) {
                        i = new Intent(getContext(), DealersMain.class);
                        i.putExtra("numTab", 1);
                        startActivity(i);
                    } else {
                        util.dialogErrorInternet(getActivity());
                    }
                }
                break;
            case R.id.b4:
                if (getActivity().getClass().getSimpleName().equals("MapDealers")) {
                    ((MapDealers) getActivity()).getSlideMenu().close(true);
                } else {
                    if (util.haveNetworkConnection(getActivity())) {
                        i = new Intent(getContext(), MapDealers.class);
                        startActivity(i);
                    } else {
                        util.dialogErrorInternet(getActivity());
                    }
                }
                break;
            case R.id.b5:
                if (getActivity().getClass().getSimpleName().equals("MyCoupons")) {
                    ((MyCoupons) getActivity()).getSlideMenu().close(true);
                } else {
                    if (util.haveNetworkConnection(getActivity())) {
                        i = new Intent(getContext(), Login.class);
                        i.putExtra("from", "coupons");
                        startActivity(i);
                    } else {
                        util.dialogErrorInternet(getActivity());
                    }
                }
                break;
            case R.id.b6:
                if (getActivity().getClass().getSimpleName().equals("MyProfile")) {
                    ((MyProfile) getActivity()).getSlideMenu().close(true);
                } else {
                    if (util.haveNetworkConnection(getActivity())) {
                        i = new Intent(getContext(), Login.class);
                        i.putExtra("from", "profile");
                        startActivity(i);
                    } else {
                        util.dialogErrorInternet(getActivity());
                    }
                }
                break;
            case R.id.b7:
                if (getActivity().getClass().getSimpleName().equals("About")) {
                    ((About) getActivity()).getSlideMenu().close(true);
                } else {
                    if (util.haveNetworkConnection(getActivity())) {
                        i = new Intent(getContext(), About.class);
                        startActivity(i);
                    } else {
                        util.dialogErrorInternet(getActivity());
                    }
                }
                break;
            case R.id.b8:
                if (getActivity().getClass().getSimpleName().equals("Terms")) {
                    ((Terms) getActivity()).getSlideMenu().close(true);
                } else {
                    if (util.haveNetworkConnection(getActivity())) {
                        i = new Intent(getContext(), Terms.class);
                        startActivity(i);
                    } else {
                        util.dialogErrorInternet(getActivity());
                    }
                }
                break;
            case R.id.b9:

                if (util.haveNetworkConnection(getActivity())) {
                    editor = sharedPreferences.edit();
                    if (sharedPreferences.getBoolean("loggedin", false)) {

                        new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                .setCustomImage(R.drawable.logo_menu)
                                .setTitleText("هل تريد تسجيل الخروج ؟")
                                .setColorTitleText(Color.parseColor("#000000"))
                                .setCancelText("إلغاء")
                                .setConfirmText("موافق")
                                .showCancelButton(true)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                             public static final int TIME_OUT = 1800;

                                                             @Override
                                                             public void onClick(final SweetAlertDialog sDialog) {
                                                                 SharedPreferences preferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                                                                 SharedPreferences.Editor editor = preferences.edit();
                                                                 //Puting the value false for loggedin
                                                                 editor.putBoolean("loggedin", false);
                                                                 //Putting blank value to email
                                                                 editor.putString("userid", "");
                                                                 editor.putString("Mobile", "");
                                                                 editor.putString("Password", "");
                                                                 editor.putString("fname", "");
                                                                 editor.putString("lname", "");
                                                                 editor.commit();
                                                                 sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);


                                                                 sDialog.setTitleText("تم تسجيل خروجك بنجاح");
                                                                 sDialog.setContentText("سيتم تحويلك الى الشاشة الرئيسية للتطبيق");
                                                                 sDialog.showCancelButton(false);
                                                                 sDialog.showConfirmButton(false);
                                                                 sDialog.setCancelClickListener(null);
                                                                 sDialog.setConfirmClickListener(null);
                                                                 sDialog.showConfirmButton(false);
                                                                 sDialog.setColorTitleText(Color.parseColor("#babd16"));
                                                                 sDialog.show();
                                                                 sDialog.showConfirmButton(false);
                                                                 final SweetAlertDialog finalSDialog = sDialog;
                                                                 handlerDialog=new Handler();
                                                                 handlerDialog.postDelayed(new Runnable() {
                                                                     @Override
                                                                     public void run() {
                                                                         sDialog.dismiss();
                                                                         Intent i = new Intent(getActivity(), Main.class);
                                                                         getActivity().startActivity(i);
                                                                         getActivity().finish();
                                                                     }
                                                                 }, TIME_OUT);
                                                             }
                                                         }
                                )
                                .show();

                    } else {
                        Intent i = new Intent(getActivity(), Login.class);
                        i.putExtra("from", "slider");
                        getActivity().startActivity(i);
                    }
                } else {
                    util.dialogErrorInternet(getActivity());
                }
                break;
        }
    }


}
