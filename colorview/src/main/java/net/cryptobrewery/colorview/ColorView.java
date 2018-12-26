package net.cryptobrewery.colorview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ColorView extends RelativeLayout {
private RecyclerView colorRecyclerView;
private Context ctx;


    public ColorView(Context context) {
        super(context);
        this.ctx = context;
        init();
    }

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {
        inflate(ctx,R.layout.colorview,this);
        colorRecyclerView = findViewById(R.id.color_rec_view);
        LinearLayoutManager llm = new LinearLayoutManager(ctx);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        colorRecyclerView.setLayoutManager(llm);
        ColorAdapter adapter = new ColorAdapter(new String[]{"#99aafc","#831949","#941920"});
        colorRecyclerView.setAdapter(adapter);
        colorRecyclerView.hasFixedSize();
        colorRecyclerView.forceLayout();
    }
}
