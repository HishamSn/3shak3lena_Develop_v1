package a3lena.a3shak.com.a3shak3lena.Dealers.Category;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import a3lena.a3shak.com.a3shak3lena.Dealers.Items.ItemAdapter;
import a3lena.a3shak.com.a3shak3lena.Dealers.ItemsOfCategory.ItemOfCategory;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.util.util;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemRowHolder> {

    private ArrayList<CategoryModel> section_list;
    private Activity mContext;

    public CategoryAdapter(Activity context, ArrayList<CategoryModel> section_list) {
        this.section_list = section_list;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_section, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {

        final String sectionName = section_list.get(i).getCategoryName();

        final ArrayList singleSectionItems = section_list.get(i).getItems();

        if(!singleSectionItems.isEmpty()){
            itemRowHolder.section_name.setText(sectionName);

            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "HacenTunisia.ttf");
            itemRowHolder.section_name.setTypeface(typeface);

            ItemAdapter itemListDataAdapter = new ItemAdapter(mContext, singleSectionItems);
            itemRowHolder.recycler_section.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            itemRowHolder.recycler_section.setLayoutManager(linearLayoutManager);
            itemRowHolder.recycler_section.setAdapter(itemListDataAdapter);
            itemRowHolder.recycler_section.setNestedScrollingEnabled(false);

            itemRowHolder.btnSeeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(util.haveNetworkConnection(mContext)){
                        for(int i=0; i< section_list.size(); i++){
                            Log.e("vovovov", section_list.get(i).getItems().get(0).getWebLink());
                        }
                        ArrayList<String> arr = new ArrayList<String>();
                        for (int w=0; w < section_list.get(i).getItems().size(); w++){
                            Gson gson = new Gson();
                            JSONObject arra = section_list.get(i).getItems().get(w).toJson();

                            arr.add(gson.toJson(arra));
                        }
                        Intent intent = new Intent(mContext, ItemOfCategory.class);
                        intent.putExtra("title", section_list.get(i).getCategoryName());
                        intent.putStringArrayListExtra("xx", arr);
                        intent.putExtra("cat_id", String.valueOf(section_list.get(i).getCategoryID()));
                        intent.putExtra("type", String.valueOf(section_list.get(i).getCategoryType()));
                        mContext.startActivity(intent);
                    }else{
                        util.dialogErrorInternet(mContext);
                    }


                }
            });

            itemRowHolder.btnSeeMore.setTypeface(typeface);
        }else{
            itemRowHolder.itemView.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return (null != section_list ? section_list.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView section_name;
        protected RecyclerView recycler_section;
        protected Button btnSeeMore;


        public ItemRowHolder(View view) {
            super(view);

            this.section_name = (TextView) view.findViewById(R.id.TVsection_name);
            this.recycler_section = (RecyclerView) view.findViewById(R.id.recycler_section);
            this.btnSeeMore = (Button) view.findViewById(R.id.btnSeeMore);
        }
    }
}