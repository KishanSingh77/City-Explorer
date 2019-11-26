package com.group9.inclass12;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.group9.inclass12.AutoCompletePredictions;
import com.group9.inclass12.R;

import java.util.ArrayList;

public class PredictionsAdapter   extends RecyclerView.Adapter<PredictionsAdapter.ViewHolder>{

    ArrayList<AutoCompletePredictions> autoCompletePredictionsArrayList ;

    public PredictionsAdapter(ArrayList<AutoCompletePredictions> mData  ) {
        this.autoCompletePredictionsArrayList = mData;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.predictions, parent, false);

        ViewHolder  viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AutoCompletePredictions autoCompletePredictions = autoCompletePredictionsArrayList.get(position);

        holder.tv_description.setText(autoCompletePredictions.description+"");

        holder.autoCompletePredictions = autoCompletePredictions;
    }

    @Override
    public int getItemCount() {
        return autoCompletePredictionsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_description;
        AutoCompletePredictions autoCompletePredictions ;

        public ViewHolder(@NonNull View itemView  ) {
            super(itemView);

            tv_description =  itemView.findViewById(R.id.textView_description);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Demo" , autoCompletePredictions+""   );
                }
            });
        }
    }

}

