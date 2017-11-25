package a3lena.a3shak.com.a3shak3lena.Dealers.ItemsOfCategory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import a3lena.a3shak.com.a3shak3lena.Dealers.DealerDetails;
import a3lena.a3shak.com.a3shak3lena.Dealers.Items.ItemsModel;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.SplashScreen;
import a3lena.a3shak.com.a3shak3lena.util.Constatns;
import a3lena.a3shak.com.a3shak3lena.util.util;


/**
 * Created by aldaboubi on 8/19/2017.
 */

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.SingleItemRowHolder>{

    // [ Variables ]
    private ArrayList<ItemsModel> product_list;
    private Activity mContext;

    // [ Constructor Main ]
    public AllProductsAdapter(Activity context, ArrayList<ItemsModel> product_list) {
        this.product_list = product_list;
        this.mContext = context;
    }


    // [ Create View Holder and Call row.xml ]
    @Override
    public AllProductsAdapter.SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_all_product, null);
        AllProductsAdapter.SingleItemRowHolder mh = new AllProductsAdapter.SingleItemRowHolder(v);
        return mh;
    }

    // [ Connect View Holder With row.xml ]
    @Override
    public void onBindViewHolder(final AllProductsAdapter.SingleItemRowHolder holder, final int i) {

        final ItemsModel singleItem = product_list.get(i);



        Picasso.with(mContext)
                .load(Constatns.imageLink+singleItem.getLogo())
                .into(holder.itemImage);

        Log.e("blaaka", singleItem.getName());

        holder.tvTitle.setText(singleItem.getName());
//        Log.e("lege", singleItem.getDescr());
        holder.tvPara.setText(singleItem.getDescr()+" ...");

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "HacenTunisia.ttf");
        holder.tvTitle.setTypeface(typeface);
        holder.tvPara.setTypeface(typeface);

            holder.imgIcon.setImageResource(R.drawable.f5min);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(util.haveNetworkConnection(mContext)){
                    Intent i = new Intent(mContext, DealerDetails.class);
                    i.putExtra("id", singleItem.getId());
                    i.putExtra("name", singleItem.getName());
                    i.putExtra("mobile",singleItem.getPhone());
                    i.putExtra("lat", singleItem.getLatitude());
                    i.putExtra("longitude", singleItem.getLongitude());
                    i.putExtra("desc", singleItem.getDescr());
                    i.putExtra("hours", singleItem.getWorkingHours());
                    i.putExtra("fb", singleItem.getFacebookLink());
                    i.putExtra("twitter", singleItem.getTwitterLink());
                    i.putExtra("youtube", singleItem.getYoutubeLink());
                    i.putExtra("web", singleItem.getWebLink());
                    i.putExtra("Logo", singleItem.getLogo());
                    i.putExtra("show_comment", singleItem.getShowCommite());
//                i.putExtra("points", singleItem.getPoints());
                    i.putStringArrayListExtra("images", singleItem.getDealershipImages());
                    for(int m=0; m < singleItem.getDealershipImages().size(); m++){
                        Log.e("ccccc22", singleItem.getDealershipImages().get(m).toString());
                    }
                    mContext.startActivity(i);
                }else{
                    util.dialogErrorInternet(mContext);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != product_list ? product_list.size() : 0);
    }


    // [ Create Class View Holder and init xml ]
    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle, tvPara;
        protected ImageView itemImage, imgIcon;

        public SingleItemRowHolder(View view) {
            super(view);

            this.itemImage = (ImageView) view.findViewById(R.id.imgallproduct);
            this.tvTitle = (TextView) view.findViewById(R.id.titleallcoupon);
            this.tvPara = (TextView) view.findViewById(R.id.paraallproduct);
            this.imgIcon = (ImageView) view.findViewById(R.id.icon);

        }
    }

    public void setFilter(ArrayList<ItemsModel> comList){
        product_list = new ArrayList<>();
        product_list.addAll(comList);
        notifyDataSetChanged();
    }
}
