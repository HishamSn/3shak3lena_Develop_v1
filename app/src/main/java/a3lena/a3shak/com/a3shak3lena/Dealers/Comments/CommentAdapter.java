package a3lena.a3shak.com.a3shak3lena.Dealers.Comments;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import a3lena.a3shak.com.a3shak3lena.R;
import a3lena.a3shak.com.a3shak3lena.util.util;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{

    Activity context;
    public static ArrayList<CommentModel> modelComment = new ArrayList<>();


    public CommentAdapter(Activity c, ArrayList<CommentModel> om){
        this.context = c;
        this.modelComment = om;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_comment, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final CommentModel modelComment0 = modelComment.get(position);

        holder.txtName.setText(modelComment0.getcName());
        holder.txtDate.setText(getTimeAgo(modelComment0.getcDate()));
        holder.txtDec.setText(modelComment0.getcDec());

        holder.txtName.setTypeface(util.changeFont(context));
        holder.txtDate.setTypeface(util.changeFont(context));
        holder.txtDec.setTypeface(util.changeFont(context));
    }

    @Override
    public int getItemCount() {
        return modelComment.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtName, txtDate, txtDec;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.nameComment);
            txtDec = (TextView) itemView.findViewById(R.id.commentDec);
            txtDate = (TextView) itemView.findViewById(R.id.dateComment);
        }
    }

    public String getTimeAgo(String currDate){

        long now = System.currentTimeMillis();
        String datetime1 = currDate;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate = null;
        try {
            convertedDate = dateFormat.parse(datetime1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CharSequence relavetime1 = DateUtils.getRelativeTimeSpanString(
                convertedDate.getTime(),
                now,
                DateUtils.SECOND_IN_MILLIS);

        return String.valueOf(relavetime1);
    }
}
