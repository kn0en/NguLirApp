package com.kn0en.ngulirapp.functions;

import android.annotation.SuppressLint;
import android.widget.Filter;

import com.kn0en.ngulirapp.adapter.RestoAdapter;
import com.kn0en.ngulirapp.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class FilterHelper extends Filter {

    static List<Restaurant> currentList;
    @SuppressLint("StaticFieldLeak")
    static RestoAdapter adapter;

    public static FilterHelper newInstance(List<Restaurant> currentList, RestoAdapter adapter) {
        FilterHelper.adapter = adapter;
        FilterHelper.currentList = currentList;
        return new FilterHelper();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if (constraint != null && constraint.length() > 0)
            if (constraint.length() > 0) {
                //CHANGE TO UPPER
                constraint = constraint.toString().toUpperCase();

                //HOLD FILTERS WE FIND
                ArrayList<Restaurant> foundFilters = new ArrayList<>();

                String name, address;

                //ITERATE CURRENT LIST
                for (int i = 0; i < currentList.size(); i++) {
                    name = currentList.get(i).getNameResto();
                    address = currentList.get(i).getAddress();

                    //SEARCH
                    if (address.toUpperCase().contains(constraint)) {
                        foundFilters.add(currentList.get(i));
                    } else if (name.toUpperCase().contains(constraint)) {
                        foundFilters.add(currentList.get(i));
                    }
                }

                //SET RESULTS TO FILTER LIST
                filterResults.count = foundFilters.size();
                filterResults.values = foundFilters;
            } else {
                //NO ITEM FOUND.LIST REMAINS INTACT
                filterResults.count = currentList.size();
                filterResults.values = currentList;
            }

        //RETURN RESULTS
        return filterResults;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.listRestaurant = (ArrayList<Restaurant>) results.values;
        adapter.notifyDataSetChanged();
    }
}
