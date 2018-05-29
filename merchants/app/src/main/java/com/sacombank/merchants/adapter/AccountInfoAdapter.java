package com.sacombank.merchants.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sacombank.merchants.R;
import com.stb.api.bo.MerchantAccountInquiry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by DUY on 8/3/2017.
 */

public class AccountInfoAdapter extends RecyclerView.Adapter<AccountInfoAdapter.InfoViewHolder> {
    private Context context;
    private List<String> infoValueList = Collections.emptyList();
    private String[] titleList;
    public AccountInfoAdapter(Context context, List<String> infoValueList) {
        this.context = context;
        this.infoValueList = infoValueList;
        titleList = this.context.getResources().getStringArray(R.array.info_account_item);
    }

    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_info_item, parent, false);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {

        holder.tvTitle.setText(titleList[position]);
        if (!infoValueList.isEmpty())
            holder.tvValue.setText(infoValueList.get(position));

    }

    @Override
    public int getItemCount() {
        return titleList.length;
    }

    public static class InfoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvValue;

        public InfoViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_item_title);
            tvValue = (TextView) itemView.findViewById(R.id.tv_item_value);
        }
    }

    public static List<String> getDummyData() {
        List<String> values = new ArrayList<>();
        values.add("S123456789");
        values.add("Nhà hàng MoMo");
        values.add("Tp. Hồ Chí Minh");
        values.add("0000374");
        values.add("18275029843");
        values.add("48320922200");
        values.add("22220393444");
        values.add("0902 888 999");
        values.add("1/2 Tô Ký, quận 12, Tp. Hồ Chí Minh");


        return values;
    }

    public static List<String> getData(MerchantAccountInquiry data) {
        if (!data.RespCode.equalsIgnoreCase("00")) {
            return new ArrayList<>();
        }
        List<String> values = new ArrayList<>();
        values.add(data.MerchantObject.UserID);
        values.add(data.MerchantObject.MerchantName);
        values.add(data.MerchantObject.City);
        values.add(data.MerchantObject.MCC);
        values.add(data.MerchantObject.Phone);
        values.add(data.MerchantObject.Address);
        return values;
    }
}
