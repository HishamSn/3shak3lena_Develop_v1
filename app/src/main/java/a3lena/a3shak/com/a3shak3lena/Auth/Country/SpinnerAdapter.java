package a3lena.a3shak.com.a3shak3lena.Auth.Country;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.util.util;


/**
 * Created by aldaboubi on 9/21/2017.
 */

public class SpinnerAdapter extends BaseAdapter {
    private List<ContryModel> lstData;
    private Activity activity;
    private LayoutInflater inflater;

    public SpinnerAdapter(List<ContryModel> lstData, Activity activity) {
        this.lstData = lstData;
        this.activity = activity;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lstData.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = view;
        if(view == null)
            view1 = inflater.inflate(R.layout.spinner_item, null);
        TextView txtRow = (TextView) view1.findViewById(R.id.txtRowItem);
        txtRow.setText(lstData.get(i).getCname());
        txtRow.setTypeface(util.changeFont(activity));
        txtRow.setTextColor(Color.parseColor("#575757"));
        return view1;
    }
}
