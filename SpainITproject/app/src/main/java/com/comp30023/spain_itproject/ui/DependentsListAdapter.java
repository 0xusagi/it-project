package com.comp30023.spain_itproject.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DependentUser;

import java.util.ArrayList;

public class DependentsListAdapter extends RecyclerView.Adapter<DependentsListAdapter.DependentViewHolder> {

    private ArrayList<String> dependentsName;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class DependentViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView dependentView;
        public DependentViewHolder(TextView v) {
            super(v);
            dependentView = v;
        }
    }

    // Constructor
    public DependentsListAdapter(ArrayList<DependentUser> dependentUsers) {
        // Given the list of dependents store just the dependents name in the arrayList dependents
        // name
        dependentsName = new ArrayList<>();
        for (DependentUser dependent : dependentUsers) {
            dependentsName.add(dependent.getName());
        }
    }

    // Create new views
    @NonNull
    @Override
    public DependentsListAdapter.DependentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        TextView dependentView = (TextView) LayoutInflater.from(parent.getContext()).
                inflate(R.layout.carer_home_dependent_text_view, parent, false);

        DependentViewHolder viewHolder = new DependentViewHolder(dependentView);

        return viewHolder;
    }

    // Replace contents of a view
    @Override
    public void onBindViewHolder(@NonNull DependentViewHolder holder, int position) {
        holder.dependentView.setText(dependentsName.get(position));
    }

    @Override
    public int getItemCount() {
        return dependentsName.size();
    }
}
