package com.example.galleryapp.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;

import java.util.List;

public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.LabelViewHolder> {

    public List<LabelModel> labellist;

    public LabelAdapter(List<LabelModel> labellist){this.labellist=labellist;}

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_label,parent,false);
        return new LabelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelAdapter.LabelViewHolder holder, int position) {
        LabelModel label = labellist.get(position);
        holder.tvLabel.setText(label.getLabel());
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> label.setChecked(isChecked)));
    }

    @Override
    public int getItemCount() {
        return labellist.size();
    }

    public class LabelViewHolder extends RecyclerView.ViewHolder{
        TextView tvLabel;
        CheckBox checkBox;
        public LabelViewHolder(@NonNull View itemView){super(itemView);
        tvLabel = itemView.findViewById(R.id.tvLabel);
        checkBox=itemView.findViewById(R.id.checkBox);
        }
    }

    public void setLabels(List<LabelModel> labellist){
        this.labellist = labellist;
        notifyDataSetChanged();
    }


}
