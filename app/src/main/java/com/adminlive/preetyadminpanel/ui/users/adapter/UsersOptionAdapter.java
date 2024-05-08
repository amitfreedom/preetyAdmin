package com.adminlive.preetyadminpanel.ui.users.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adminlive.preetyadminpanel.databinding.ListUserOptionsBinding;
import com.adminlive.preetyadminpanel.ui.users.models.OptionModels;

import java.util.List;

public class UsersOptionAdapter extends RecyclerView.Adapter<UsersOptionAdapter.ViewHolder> {
    private static final String TAG = "UsersOption";
    private List<OptionModels> dataList;
    private Context context;

    public Select select;

    public interface Select {
        void OnPress(String title);
    }

    public UsersOptionAdapter(Context context, List<OptionModels> dataList,Select select) {
        this.context = context;
        this.dataList = dataList;
        this.select=select;
    }

    @NonNull
    @Override
    public UsersOptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using ViewBinding
        ListUserOptionsBinding binding = ListUserOptionsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UsersOptionAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersOptionAdapter.ViewHolder holder, int position) {
        OptionModels item = dataList.get(position);
        // Bind data to views using ViewBinding
        holder.bind(item);

        holder.itemView.setOnClickListener(view -> {
            select.OnPress(item.getTitle());
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListUserOptionsBinding binding;

        public ViewHolder(@NonNull ListUserOptionsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OptionModels item) {
            try{
                // Bind data to views
                binding.tvTitle.setText(item.getTitle());
                binding.ivIcon.setImageResource(item.getIcon());
//                binding.ivIcon.setColorFilter(ContextCompat.getColor(context, item.getColor()), android.graphics.PorterDuff.Mode.MULTIPLY);

                // Add more bindings as needed
            }catch (Exception e){
                Log.i(TAG, "bind: ");
            }
        }
    }
}