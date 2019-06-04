package tripHistoryInputs;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ucsc121.carboncars.R;

public class tripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView rideID;
    public TextView dateID;
    public TextView distanceID;
    public TextView co2ID;
    public tripViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        rideID = (TextView) itemView.findViewById(R.id.rideID);
        dateID = (TextView) itemView.findViewById(R.id.dateID);
        distanceID = (TextView) itemView.findViewById(R.id.distanceID);
        co2ID = (TextView) itemView.findViewById(R.id.co2ID);
    }

    @Override
    public void onClick(View v) {

    }
}
