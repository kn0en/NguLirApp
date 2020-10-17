package com.kn0en.ngulirapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kn0en.ngulirapp.DetailRestoActivity;
import com.kn0en.ngulirapp.R;
import com.kn0en.ngulirapp.functions.FilterHelper;
import com.kn0en.ngulirapp.functions.Utils;
import com.kn0en.ngulirapp.models.Restaurant;

import java.util.List;
import java.util.Locale;

import static com.kn0en.ngulirapp.functions.Utils.searchString;

@SuppressWarnings("ALL")
public class RestoAdapter extends RecyclerView.Adapter<RestoAdapter.ViewHolder> implements Filterable {

    private final Context context;
    public List<Restaurant> listRestaurant;
    private List<Restaurant> filterList;
    private FilterHelper filterHelper;


    public RestoAdapter(Context mContext, List<Restaurant> listRestaurant) {
        this.context = mContext;
        this.listRestaurant = listRestaurant;
        this.filterList = listRestaurant;
        TypedValue mTypedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground,
                mTypedValue, true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get current scientist
        final Restaurant s = listRestaurant.get(position);

        //bind data to widgets
        holder.nameTxt.setText(s.getNameResto());
        holder.mAddressTxt.setText(s.getAddress());
//        Picasso.get().load(s.getImageRestoUrl())
//                .placeholder(R.drawable.ic_launcher_background)
//                .into(holder.mImageRestoUri);

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
        }

        //get name and galaxy
        String name = s.getNameResto().toLowerCase(Locale.getDefault());
        String address = s.getAddress().toLowerCase(Locale.getDefault());

        //highlight name text while searching
        if (name.contains(searchString) && !(searchString.isEmpty())) {
            int startPos = name.indexOf(searchString);
            int endPos = startPos + searchString.length();

            Spannable spanString = Spannable.Factory.getInstance().
                    newSpannable(holder.nameTxt.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.nameTxt.setText(spanString);
        } else {
            //Utils.show(ctx, "Search string empty");
        }

        //highligh galaxy text while searching
        if (address.contains(searchString) && !(searchString.isEmpty())) {

            int startPos = address.indexOf(searchString);
            int endPos = startPos + searchString.length();

            Spannable spanString = Spannable.Factory.getInstance().
                    newSpannable(holder.mAddressTxt.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.BLUE), startPos, endPos,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.mAddressTxt.setText(spanString);
        }

        //open detailactivity when clicked
        holder.setItemClickListener(pos -> Utils.sendRestaurantToActivity(context, s,
                DetailRestoActivity.class));
    }

    @Override
    public int getItemCount() {
        return listRestaurant.size();
    }

    @Override
    public Filter getFilter() {
        if (filterHelper == null) {
            filterHelper = FilterHelper.newInstance(filterList, this);
        }
        return filterHelper;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private final TextView nameTxt;
        private final ImageView mImageRestoUri;
        private final TextView mAddressTxt;
        private OnItemClickListener itemClickListener;

        ViewHolder(View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameResto);
            mImageRestoUri = itemView.findViewById(R.id.imageRsto);
            mAddressTxt = itemView.findViewById(R.id.addess);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.itemClickListener.onClick(this.getLayoutPosition());
        }

        void setItemClickListener(OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }
}
