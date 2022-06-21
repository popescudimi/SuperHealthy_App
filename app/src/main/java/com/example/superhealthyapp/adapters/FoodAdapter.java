/*
https://gist.github.com/piyush-malaviya/db29b921d8c91ac3901dfbbd2578debd
*/
package com.example.superhealthyapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;

import androidx.recyclerview.widget.RecyclerView;

import com.example.superhealthyapp.R;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;



public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> implements Filterable {
    private List<ApplicationViewModel> foodList;
    private List<ApplicationViewModel> filteredFoodList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    ArrayList<ApplicationViewModel> mArrayList;

    public FoodAdapter(Context context, List<ApplicationViewModel> foodList, OnItemClickListener onItemClickListener){
        this.context = context;
        this.foodList = foodList;
        this.filteredFoodList = foodList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_main,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int i) {
     //   viewHolder.textViewFoodGrams.setText(filteredFoodList.get(i).getFoodGrams());
        viewHolder.textViewFoodName.setText(filteredFoodList.get(i).getFoodName());
     //   viewHolder.textViewFoodDescription.setText(filteredFoodList.get(i).getFoodDescription());
        viewHolder.textViewFoodGrams.setText(MessageFormat.format("Grams : {0}", filteredFoodList.get(i).getFoodGrams()));
        viewHolder.textViewFoodCalories.setText(MessageFormat.format("Calories : {0}", filteredFoodList.get(i).getCaloriesNumber()));
       viewHolder.textViewFoodType.setText(MessageFormat.format("Calorie Type : {0}", filteredFoodList.get(i).getTypeCalorie()));
        Picasso.with(context)
                .load(filteredFoodList.get(i).getFoodDownload())
                .placeholder(R.drawable.loading_icon)
                .into(viewHolder.imageViewFoodURL);
        viewHolder.bind(filteredFoodList.get(i), onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return filteredFoodList.size();
    }

    public void setDataArrayList(ArrayList<ApplicationViewModel> mArrayList) {
        this.mArrayList = mArrayList;
    }

    public ApplicationViewModel getListItem(int position) {
        if (position > foodList.size()) {
            return null;
        }
        return foodList.get(position);
    }

    public ArrayList<ApplicationViewModel> getList()
    {
        return mArrayList;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

            //    if (TextUtils.isEmpty(charSequence)) {
            //        foodList.addAll(filteredFoodList);
               //     foodList.clear();
          //      }
                String query = charSequence.toString();
                List<ApplicationViewModel> filtered = new ArrayList<>();
                if (query.isEmpty()) {
                    filtered.addAll(foodList);
                } else {
                    for (ApplicationViewModel food : foodList) {
                        if (food.getFoodName().toLowerCase().contains(query.toLowerCase())) {
                            filtered.add(food);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.count = filtered.size();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
               if (foodList != null) {
                    Handler handler;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (foodList != null) {
                                // handling exception
                            }
                        }
                    }, 100);
                    filteredFoodList = (ArrayList<ApplicationViewModel>) results.values;
                    notifyDataSetChanged();
              }
            }


        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFoodName,textViewFoodDescription,textViewFoodCalories, textViewFoodGrams, textViewFoodType;
        ImageView imageViewFoodURL;
        ViewHolder( View itemView) {
            super(itemView);
            textViewFoodName = itemView.findViewById(R.id.textViewFoodName);
            textViewFoodCalories = itemView.findViewById(R.id.textViewFoodCalory);
            textViewFoodType = itemView.findViewById(R.id.textViewFoodType);
         //    textViewFoodDescription = itemView.findViewById(R.id.textViewFoodDescription);
            textViewFoodGrams = itemView.findViewById(R.id.textViewFoodGrams);
            imageViewFoodURL = itemView.findViewById(R.id.imageViewFoodURL);

        }

        void bind(final ApplicationViewModel food, final OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(food);
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(ApplicationViewModel food);
    }
}