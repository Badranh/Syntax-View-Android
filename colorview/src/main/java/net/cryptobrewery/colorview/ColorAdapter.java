package net.cryptobrewery.colorview;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Hasan Badran on 8/12/2018.
 */

public class ColorAdapter  extends RecyclerView.Adapter<ColorAdapter.ViewHolder>{
    private String[] colors;
    private onCardColorClick listener;
    private int height,width;
    public ColorAdapter(String[] colors) {
        this.colors = colors;
    }

    public ColorAdapter(String[] colors, onCardColorClick listener) {
        this.colors = colors;
        this.listener = listener;
    }

    public ColorAdapter(String[] colors, onCardColorClick listener, int height, int width) {
        this.colors = colors;
        this.listener = listener;
        this.height = height;
        this.width = width;
    }

    public ColorAdapter(String[] colors, int height, int width) {
        this.colors = colors;
        this.height = height;
        this.width = width;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item_box,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.colorCard.setCardBackgroundColor(Color.parseColor(colors[position]));
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private CardView colorCard;
        private ViewHolder(View itemView) {
            super(itemView);
            colorCard = itemView.findViewById(R.id.color_rec_view);
            if(listener !=null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.ClickCallBack();
                    }
                });
            }
        }
    }
}
