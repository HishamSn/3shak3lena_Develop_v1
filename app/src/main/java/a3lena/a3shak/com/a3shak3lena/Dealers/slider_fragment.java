package a3lena.a3shak.com.a3shak3lena.Dealers;

import android.app.DialogFragment;
import android.app.Fragment;
import android.icu.text.DisplayContext;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.SplashScreen;
import a3lena.a3shak.com.a3shak3lena.util.Constatns;
import a3lena.a3shak.com.slider_image_lib.banners.Banner;
import a3lena.a3shak.com.slider_image_lib.banners.RemoteBanner;
import a3lena.a3shak.com.slider_image_lib.events.OnBannerClickListener;
import a3lena.a3shak.com.slider_image_lib.views.BannerSlider;

/**
 * Created by aldaboubi on 10/11/2017.
 */

public class slider_fragment extends DialogFragment {

    private BannerSlider bannerSlider;
    List<Banner> remoteBanners=new ArrayList<>();
    ArrayList<String> slide_list;
    Bundle bundle;


    int age;

    public static slider_fragment newInstance(int age) {
        Bundle bundle = new Bundle();
        bundle.putInt("age", age);

        slider_fragment fragment = new slider_fragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            age = bundle.getInt("age");
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dialog_slider_pager,null);
        readBundle(getArguments());

        bannerSlider = (BannerSlider) v.findViewById(R.id.ban);
        bundle = getActivity().getIntent().getExtras();
        slide_list = bundle.getStringArrayList("images");
        Toast.makeText(getActivity(), String.valueOf(age), Toast.LENGTH_LONG).show();
        setSlider(bannerSlider);
        bannerSlider.setCurrentSlide(age);
        ImageView imageView = (ImageView) v.findViewById(R.id.close_dialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Sdfsdf", Toast.LENGTH_LONG).show();
                closefragment();
            }
        });
        return v;
    }

    private void closefragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    public void setSlider(BannerSlider bn){

        for (int i = 1; i < slide_list.size(); i++) {
            String image = Constatns.imageLink + slide_list.get(i);
            remoteBanners.add(new RemoteBanner(image));
        }
        bn.setBanners(remoteBanners);
        bn.setLoopSlides(true);
        bn.setInterval(5000);
    }


}
