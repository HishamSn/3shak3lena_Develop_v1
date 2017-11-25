package a3lena.a3shak.com.a3shak3lena.Coupons.Coupon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import a3lena.a3shak.com.a3shak3lena.Coupons.CouponDetails;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.util.util;


/**
 * Created by aldaboubi on 9/15/2017.
 */

public class AllCouponsAdapter extends RecyclerView.Adapter<AllCouponsAdapter.SingleItemRowHolder>{
    // [ Variables ]
    private ArrayList<AllCouponsModel> product_list;
    private Activity mContext;

    // [ Constructor Main ]
    public AllCouponsAdapter(Activity context, ArrayList<AllCouponsModel> product_list) {
        this.product_list = product_list;
        this.mContext = context;
    }


    // [ Create View Holder and Call row.xml ]
    @Override
    public AllCouponsAdapter.SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_all_coupons, null);
        AllCouponsAdapter.SingleItemRowHolder mh = new AllCouponsAdapter.SingleItemRowHolder(v);
        return mh;
    }

    // [ Connect View Holder With row.xml ]
    @Override
    public void onBindViewHolder(final AllCouponsAdapter.SingleItemRowHolder holder, final int i) {

        final AllCouponsModel singleItem = product_list.get(i);


        Picasso.with(mContext)
                .load(R.drawable.logo_menu)
                .into(holder.imgImgallcoupon);

        holder.tvTitleallcoupon.setText(singleItem.getBarcode());

        holder.tvDate_created.setText("تاريخ الاضافة : "+singleItem.getAddDateTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date futureDate = dateFormat.parse(singleItem.getScandate()+" "+singleItem.getScanendtime());
            Date currentDate = dateFormat.parse(singleItem.getCurrentdate()+" "+singleItem.getCurrenttime());
            if (currentDate.after(futureDate)) {
                holder.status_coupon.setText("الحالة : غير فعال");
                holder.status_coupon.setTextColor(Color.parseColor("#f15a25"));
            }else{
                holder.status_coupon.setText("الحالة : فعال");
                holder.status_coupon.setTextColor(Color.parseColor("#babd16"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "HacenTunisia.ttf");
        holder.tvTitleallcoupon.setTypeface(typeface);
        holder.tvDate_created.setTypeface(typeface);
        holder.status_coupon.setTypeface(typeface);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(util.haveNetworkConnection(mContext)){
                    Intent i = new Intent(mContext, CouponDetails.class);
//                i.putExtra("id", singleItem.getId());
                    i.putExtra("barcode", singleItem.getBarcode());
//                i.putExtra("enddate", singleItem.getEnddate());
//                i.putExtra("serialnumber", singleItem.getSerialnumber());
//                i.putExtra("points", singleItem.getPoints());
//                i.putExtra("used", singleItem.getUsed());
//                i.putExtra("flag", singleItem.getFlag());
//                i.putExtra("did", singleItem.getDid());
//                i.putExtra("AddDateTime", singleItem.getAddDateTime());
//                i.putExtra("scandate", singleItem.getScandate());
//                i.putExtra("scantime", singleItem.getScantime());
//                i.putExtra("scanenddate", singleItem.getScanenddate());
//                i.putExtra("scanendtime", singleItem.getScanendtime());
//                i.putExtra("msg", singleItem.getMsg());
//                i.putExtra("msg2", singleItem.getMsg2());
                    mContext.startActivity(i);
                    mContext.finish();
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


    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitleallcoupon, tvDate_created, status_coupon;
        protected ImageView imgImgallcoupon, imgIcon;
        protected RelativeLayout relativeLayout;

        public SingleItemRowHolder(View view) {
            super(view);

            this.imgImgallcoupon = (ImageView) view.findViewById(R.id.imgallcoupon);
            this.tvTitleallcoupon = (TextView) view.findViewById(R.id.titleallcoupon);
            this.tvDate_created = (TextView) view.findViewById(R.id.date_created);
            this.status_coupon = (TextView) view.findViewById(R.id.status_coupon);
            this.relativeLayout = view.findViewById(R.id.relativeiew);
            this.imgIcon = (ImageView) view.findViewById(R.id.imageView2);
        }
    }

    public void setFilter(ArrayList<AllCouponsModel> comList){
        product_list = new ArrayList<>();
        product_list.addAll(comList);
        notifyDataSetChanged();
    }
}
