package com.sacombank.merchants.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sacombank.merchants.R;

public class HomePageAdapter extends BaseAdapter {

    private Context context;
    private boolean isLogined;
    private static final int[] drawableList = {R.drawable.ic_mobilepos_notlogin,
            R.drawable.ic_login,
            R.drawable.ic_qrcode_notlogin,
            R.drawable.ic_history_notlogin,
            R.drawable.ic_introduce,
            R.drawable.ic_guideline};
    private static final int[] drawableListLogin = {R.drawable.ic_mobilepos,
            R.drawable.ic_account_manager,
            R.drawable.ic_qrcode,
            R.drawable.ic_history,
            R.drawable.ic_introduce,
            R.drawable.ic_guideline};
    private static String[] titleList = {};


    public HomePageAdapter(Context context, boolean isLogined) {
        this.context = context;
        this.isLogined = isLogined;
        if (isLogined)
            titleList = this.context.getResources().getStringArray(R.array.home_login_item);
        else
            titleList = this.context.getResources().getStringArray(R.array.home_item);
    }

    @Override
    public int getCount() {
        return drawableList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final MyToolViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (isLogined)
                view = inflater.inflate(R.layout.item_custom_home_logined, viewGroup, false);
            else
                view = inflater.inflate(R.layout.item_custom_home, viewGroup, false);

            holder = new MyToolViewHolder();
            holder.tvItem = (TextView) view.findViewById(R.id.item_title);
            holder.imgIcon = (ImageView) view.findViewById(R.id.item_image);
            view.setTag(holder);
        } else {
            holder = (MyToolViewHolder) view.getTag();
        }

        holder.tvItem.setText(titleList[position]);
        holder.imgIcon.setImageResource(isLogined ? drawableListLogin[position] : drawableList[position]);
        return view;
    }

    public static class MyToolViewHolder {
        TextView tvItem;
        ImageView imgIcon;

        public MyToolViewHolder() {
        }
    }
}
