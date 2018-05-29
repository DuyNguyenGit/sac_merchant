package com.sacombank.merchants.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sacombank.merchants.R;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.model.staticmodel.HistoryData;
import com.sacombank.merchants.util.Utils;
import com.stb.api.bo.TransactionObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.List;

/**
 * Created by DUY on 8/5/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private HistoryItemClickListener listener;
    private List<TransactionObject> historyList = Collections.emptyList();

    public HistoryAdapter(Context context, List<TransactionObject> historyList, HistoryItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.historyList = historyList;
    }

    public interface HistoryItemClickListener {
        void onItemClick(TransactionObject data, int pos);
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_custom, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        if (position % 2 == 0)
            holder.layout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.bg_row_history, null));
        if (historyList == null || historyList.isEmpty()) {
            return;
        }
        final TransactionObject data = historyList.get(position);
        if (data != null) {
            holder.tvBill.setText(data.BillNo);
            holder.tvDate.setText(data.TransactionDateTime.substring(6, 8).concat("/").concat(data.TransactionDateTime.substring(4, 6).concat("/").concat(data.TransactionDateTime.substring(2, 4))));

            holder.tvMoney.setText(Utils.formatMoney(data.Amount));
            holder.imgStatus.setImageResource((data.Status.equals(ApiManager.TransactionStatus.SUCCESSFUl.toString())) ?
                    R.drawable.ic_history_status_checked : R.drawable.ic_history_status_unchecked);

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(data, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvBill;
        TextView tvDate;
        TextView tvMoney;
        ImageView imgStatus;
        LinearLayout layout;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            tvBill = (TextView) itemView.findViewById(R.id.tvBill);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvMoney = (TextView) itemView.findViewById(R.id.tvMoney);
            imgStatus = (ImageView) itemView.findViewById(R.id.imgStatus);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_row_history);
        }
    }
}
