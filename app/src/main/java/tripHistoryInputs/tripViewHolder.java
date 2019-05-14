package tripHistoryInputs;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ucsc121.carboncars.R;

public class tripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView rideId;
    public tripViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        rideId = (TextView) itemView.findViewById(R.id.rideID);
    }

    @Override
    public void onClick(View v) {

    }
}
