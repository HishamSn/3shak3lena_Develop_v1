package a3lena.a3shak.com.a3shak3lena.Dealers.ItemsOfCategory;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import a3lena.a3shak.com.a3shak3lena.Dealers.DealersMain;
import a3lena.a3shak.com.a3shak3lena.Dealers.Items.ItemsModel;
import a3lena.a3shak.com.a3shak3lena.Dealers.NonSwipeableViewPager;
import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.slide_menu.BaseSlideMenuActivity;
import a3lena.a3shak.com.a3shak3lena.slide_menu.recycler.SliderMenuRight.SliderMenuAdapter;
import a3lena.a3shak.com.slidemenu.SlideMenu;

public class ItemOfCategory extends BaseSlideMenuActivity {

    // Slider
    private SlideMenu mSlideMenu;
    ImageView btnMenu;

    ArrayList<ItemsModel> itemsModels;
    RecyclerView recycler_all_products;
    Bundle bundle;
    AllProductsAdapter adapter;
    TextView txtTitleallproductactivity;
    SearchView searchView;
    LinearLayout root_layout;
    SliderMenuAdapter adapter_slider;

    private RecyclerView slidingListView;
    private View rightMenuView;
    int itemPage = 1;

    private List<String> strings;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        setSlideRole(R.layout.activity_item_of_category);
        setSlideRole(R.layout.layout_secondary_menu);
        mSlideMenu = getSlideMenu();
        // Slider Toggle
        click_menu();
        init();

//        int magId = getResources().getIdentifier("android:id/search", null, null);
//        ImageView magImage = (ImageView) searchView.findViewById(magId);
//        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
//        magImage.setVisibility(View.GONE);

        searchView.setQueryHint("البحث .....");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();

                ArrayList<ItemsModel> comList = new ArrayList<>();
                for(ItemsModel company : itemsModels){
                    String name = company.getName().toLowerCase();
                    if(name.contains(newText)){
                        comList.add(company);
                    }
                }

                adapter.setFilter(comList);
                return true;
            }
        });
        changeSearchViewTextColor(searchView);


        txtTitleallproductactivity = (TextView) findViewById(R.id.titleallproductactivity);
        bundle = getIntent().getExtras();
        txtTitleallproductactivity.setText(bundle.getString("title"));
        Typeface typeface = Typeface.createFromAsset(getAssets(), "HacenTunisia.ttf");
        txtTitleallproductactivity.setTypeface(typeface);
        itemsModels = new ArrayList<>();
        adapter = new AllProductsAdapter(this, itemsModels);
        setData1();
//        setData2();
        recycler_all_products = (RecyclerView) findViewById(R.id.recycler_all_products);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_all_products.setLayoutManager(linearLayoutManager);
        recycler_all_products.setHasFixedSize(true);
        recycler_all_products.setItemAnimator(new DefaultItemAnimator());
        recycler_all_products.setAdapter(adapter);


    }

    public void init(){
        root_layout = (LinearLayout) findViewById(R.id.root_layout);
        searchView= (SearchView) findViewById(R.id.search);
    }

    public void click_menu(){
        btnMenu = (ImageView) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSlideMenu.isOpen()){
                    mSlideMenu.close(true);
                    mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
                }else{
                    mSlideMenu.open(true, true);
                    mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
                }
            }
        });
    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                Typeface typeface = Typeface.createFromAsset(getAssets(), "HacenTunisia.ttf");
                ((TextView) view).setTextColor(getResources().getColor(R.color.grey_dark));
                ((TextView) view).setHintTextColor(getResources().getColor(R.color.grey_dark));
                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                ((TextView) view).setTypeface(typeface);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        searchView.setQuery("", false);
        root_layout.requestFocus();
    }



    public void setData1(){
        ArrayList<String> arrr = bundle.getStringArrayList("xx");


        try {
            for(int i=0; i < arrr.size(); i++){
                JSONObject jsonObject = new JSONObject(arrr.get(i));
                Log.e("vcvcvc", arrr.get(i));
                JSONObject jsonObject1 = jsonObject.getJSONObject("nameValuePairs");
                ItemsModel itemsModel = new ItemsModel();
                Log.e("webis", jsonObject1.toString());
                itemsModel.setId(jsonObject1.getString("id"));
                itemsModel.setName(jsonObject1.getString("name"));
                itemsModel.setDescr(jsonObject1.getString("descr"));
                Log.e("dessss", jsonObject1.getString("descr"));
                itemsModel.setLogo(jsonObject1.getString("logo"));
                itemsModel.setFacebookLink(jsonObject1.getString("fblink"));
                itemsModel.setYoutubeLink(jsonObject1.getString("youtubelink"));
                itemsModel.setTwitterLink(jsonObject1.getString("twitterlink"));
                itemsModel.setShowCommite(jsonObject1.getString("showcommite"));
                itemsModel.setCategoryId(jsonObject1.getString("catid"));
                itemsModel.setLatitude(jsonObject1.getString("latitude"));
                itemsModel.setWebLink(jsonObject1.getString("weblink"));
                itemsModel.setPhone(jsonObject1.getString("mobile"));
                itemsModel.setLongitude(jsonObject1.getString("longitude"));
                itemsModel.setWorkingHours(jsonObject1.getString("workinghours"));
                itemsModel.setPoints(jsonObject1.getString("points"));
                ArrayList<String> arrImages = new ArrayList<>();
                JSONArray jsonArray2 = new JSONArray(jsonObject1.getString("dealershipimages"));
                for(int k=0; k < jsonArray2.length(); k++){
                    arrImages.add(jsonArray2.get(k).toString());
                }
                itemsModel.setDealershipImages(arrImages);
                itemsModels.add(itemsModel);
                adapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("erz", e.getMessage().toString());
        }
    }
}
