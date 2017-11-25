package a3lena.a3shak.com.a3shak3lena.Dealers.Items;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import a3lena.a3shak.com.a3shak3lena.Dealers.DealerDetails;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.SplashScreen;
import a3lena.a3shak.com.a3shak3lena.util.Constatns;
import a3lena.a3shak.com.a3shak3lena.util.util;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.SingleItemRowHolder> {

    // [ Variables ]
    private ArrayList<ItemsModel> product_list;
    private Activity mContext;

    // [ Constructor Main ]
    public ItemAdapter(Activity context, ArrayList<ItemsModel> product_list) {
        this.product_list = product_list;
        this.mContext = context;
    }


    // [ Create View Holder and Call row.xml ]
    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_product, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    // [ Connect View Holder With row.xml ]
    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int i) {

        final ItemsModel singleItem = product_list.get(i);

        Picasso.with(mContext)
                .load(Constatns.imageLink+singleItem.getLogo())
                .into(holder.itemImage);

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

        protected TextView tvTitle;
        protected ImageView itemImage;

        public SingleItemRowHolder(View view) {
            super(view);

            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
        }
    }

    public void setFilter(ArrayList<ItemsModel> comList){
        product_list = new ArrayList<>();
        product_list.addAll(comList);
        notifyDataSetChanged();
    }
}