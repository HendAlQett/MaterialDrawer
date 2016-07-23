package adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hend.materialdrawer.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import helper.ItemTouchHelperAdapter;
import helper.ItemTouchHelperViewHolder;
import helper.OnStartDragListener;

/**
 * Created by hend on 7/12/16.
 */
public class ChefMenuRecyclerAdapter extends RecyclerView.Adapter<ChefMenuRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private final List<String> mItems = new ArrayList<>();
    OnStartDragListener mStartDragListener;
    Context context;

    public ChefMenuRecyclerAdapter(Context context, OnStartDragListener dragStartListener) {
        this.context = context;
        mStartDragListener = dragStartListener;
        mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chef_menu, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvMenu.setText(mItems.get(position));
        holder.ivOrder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    mStartDragListener.onStartDrag(holder);
                }
                return true;
            }
        });
        holder.rlData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.rlActions.getVisibility() == View.VISIBLE) {
                    Log.d("TAG", "Clicked " + position);
                    collapse(holder.rlActions);
                } else {
                    expand(holder.rlActions);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mItems, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mItems, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        public final TextView tvMenu;
        public final ImageView ivOrder;
        public final RelativeLayout rlActions;
        public final RelativeLayout rlData;


        public ViewHolder(View itemView) {
            super(itemView);
            tvMenu = (TextView) itemView.findViewById(R.id.tvMealName);
            ivOrder = (ImageView) itemView.findViewById(R.id.ivOrder);
            rlActions = (RelativeLayout) itemView.findViewById(R.id.rlActions);
            rlData = (RelativeLayout) itemView.findViewById(R.id.rlData);

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }

    public void expand(final View v) {
//        int width = v.getWidth();
        // Calculate ActionBar height
//        TypedValue tv = new TypedValue();
//        int itemHeight=200;
//        if (context.getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, tv, true))
//        {
//             itemHeight = TypedValue.complexToDimensionPixelSize(tv.data,context.getResources().getDisplayMetrics());
//        }
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetedHeight = v.getMeasuredHeight();
        if (v.isShown()) {
            collapse(v);
        } else {
            v.getLayoutParams().height = 0;
            v.setVisibility(View.VISIBLE);
            final int finalItemHeight = RelativeLayout.LayoutParams.WRAP_CONTENT;
//            Log.d("Height", "hi " + RelativeLayout.LayoutParams.WRAP_CONTENT);
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime,
                                                   Transformation t) {
                    v.getLayoutParams().height = interpolatedTime == 1 ? finalItemHeight
                            : (int) (targetedHeight * interpolatedTime);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            a.setDuration((int) (targetedHeight + 500));
            v.startAnimation(a);
        }

    }

    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight
                            - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (v.getLayoutParams().height + 500));
        v.startAnimation(a);
    }
}
