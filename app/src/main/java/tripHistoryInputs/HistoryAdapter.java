package tripHistoryInputs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ucsc121.carboncars.R;

public class HistoryAdapter extends RecyclerView.Adapter<tripViewHolder> {
    private List<tripDetailObject> itemList;
    private Context context;
    private static final String TAG = "adapter debug";

    public HistoryAdapter(List<tripDetailObject> itemList,Context context){
        this.itemList = itemList;
        this.context = context;
    }







    @NonNull
    @Override
    public tripViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ride_history,viewGroup,false);
        RecyclerView.LayoutParams cc = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(cc);
        tripViewHolder cc2 = new tripViewHolder(layoutView);
        return cc2;
    }

    @Override
    public void onBindViewHolder(@NonNull tripViewHolder holder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.rideID.setText(itemList.get(i).getRideID());
        holder.dateID.setText(itemList.get(i).getDateID());
        holder.distanceID.setText(itemList.get(i).getDistanceID());
        holder.co2ID.setText(itemList.get(i).getCo2ID());



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
