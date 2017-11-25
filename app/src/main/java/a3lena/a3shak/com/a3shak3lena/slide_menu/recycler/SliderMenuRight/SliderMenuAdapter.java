package a3lena.a3shak.com.a3shak3lena.slide_menu.recycler.SliderMenuRight;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import a3lena.a3shak.com.a3shak3lena.R;


/**
 * Created by aldaboubi on 8/18/2017.
 */

public class SliderMenuAdapter extends RecyclerView.Adapter<SliderMenuAdapter.SingleItemRowHolder> {

    private ArrayList<SlideMenuModel> favtModels = new ArrayList<>();
    private final Activity context;

    public SliderMenuAdapter(Activity context, ArrayList<SlideMenuModel> product_list) {
        this.favtModels = product_list;
        this.context = context;

    }


    // [ Create View Holder and Call row.xml ]
    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_list_menu, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    // [ Connect View Holder With row.xml ]
    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int i) {

        final SlideMenuModel singleItem = favtModels.get(i);
        holder.tvTitle.setText(singleItem.getTitle());
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "HacenTunisia.ttf");
        holder.tvTitle.setTypeface(typeface);
        holder.imgIcon.setBackground(singleItem.getImageView());
        holder.tvTitle.setTextDirection(View.TEXT_DIRECTION_RTL);
        holder.imgIcon.setTextDirection(View.TEXT_DIRECTION_RTL);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (i == 0) {
//                    Intent intent = new Intent(context, MainActivity.class);
//                    context.startActivity(intent);
//                    context.finish();
////                    customMenu.closeMenu();
////                    holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
//                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != favtModels ? favtModels.size() : 0);
    }


    // [ Create Class View Holder and init xml ]
    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;
        protected ImageView imgIcon;

        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.title);
            this.imgIcon = (ImageView) view.findViewById(R.id.imgIcon);


        }
    }

}
