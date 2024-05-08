package com.adminlive.preetyadminpanel.ui.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.databinding.DashbordMainItemBinding;
import com.adminlive.preetyadminpanel.ui.home.models.TopOptionsModel;

import java.util.List;

public class TopOptionsAdapter extends RecyclerView.Adapter<TopOptionsAdapter.ViewHolder> {
    private static final String TAG = "TopOptionsAdapter";
    private List<TopOptionsModel> dataList;
    private Context context;

    public TopOptionsAdapter(Context context, List<TopOptionsModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using ViewBinding
        DashbordMainItemBinding binding = DashbordMainItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TopOptionsModel item = dataList.get(position);
        // Bind data to views using ViewBinding
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private DashbordMainItemBinding binding;

        public ViewHolder(@NonNull DashbordMainItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TopOptionsModel item) {
          try{
              // Bind data to views
              binding.tvCount.setText(item.getCount());
              binding.tvTitle.setText(item.getTitle());
              binding.ivIcon.setImageResource(item.getIcon());
              binding.ivIcon.setColorFilter(ContextCompat.getColor(context, item.getColor()), android.graphics.PorterDuff.Mode.MULTIPLY);

              // Add more bindings as needed
          }catch (Exception e){
              Log.i(TAG, "bind: ");
          }
        }
    }
}
