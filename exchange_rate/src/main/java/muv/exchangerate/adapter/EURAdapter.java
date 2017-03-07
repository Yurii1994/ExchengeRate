package muv.exchangerate.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import muv.exchangerate.R;
import muv.exchangerate.data.DataAllBank;

public class EURAdapter extends RecyclerView.Adapter<EURAdapter.PBViewHolder>
{

    private List<DataAllBank> data = new ArrayList<>();
    Context context;

    @Override
    public PBViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_usd_eur_rub, parent, false);
        return new PBViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PBViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class PBViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        TextView nameBank;
        TextView buyPrice;
        TextView salePrice;
        TextView date;

        public PBViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            date = (TextView)itemView.findViewById(R.id.date);
            nameBank = (TextView)itemView.findViewById(R.id.name_bank);
            buyPrice = (TextView)itemView.findViewById(R.id.price_buy);
            salePrice = (TextView)itemView.findViewById(R.id.price_sale);

        }
    }

    public EURAdapter(List<DataAllBank> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setData(List<DataAllBank> data) {
        this.data = data;
    }
}
