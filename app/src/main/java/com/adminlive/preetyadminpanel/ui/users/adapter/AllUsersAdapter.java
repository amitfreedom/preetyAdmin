package com.adminlive.preetyadminpanel.ui.users.adapter;

import static com.adminlive.preetyadminpanel.Constant.USER_PLACEHOLDER_PATH;
import static com.adminlive.preetyadminpanel.ui.utils.GlobalFunctions.capitalizeText;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adminlive.preetyadminpanel.databinding.ItemHostRequestBinding;
import com.adminlive.preetyadminpanel.databinding.ItemLatestMemberBinding;
import com.adminlive.preetyadminpanel.ui.home.models.UserDetailsModel;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolder> {
    private static final String TAG = "AllUsersAdapter";
    private List<UserDetailsModel> dataList;
    private Context context;

    public AllUsersAdapter(Context context, List<UserDetailsModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public AllUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using ViewBinding
        ItemHostRequestBinding binding = ItemHostRequestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AllUsersAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllUsersAdapter.ViewHolder holder, int position) {
        UserDetailsModel item = dataList.get(position);
        // Bind data to views using ViewBinding
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemHostRequestBinding binding;

        public ViewHolder(@NonNull ItemHostRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(UserDetailsModel item) {
            try{
//                Date date = new Date(item.getLoginTime());

//                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
//                String formattedDate = sdf.format(date);
                // Bind data to views
                binding.tvName.setText(capitalizeText(item.getUsername()));
                binding.tvDate.setText("ID:"+item.getUid());
                if (!Objects.equals(item.getImage(), "")) {
                    Glide.with(context).load(item.getImage()).into(binding.ivProfileImage);
                }else {
                    Glide.with(context).load(USER_PLACEHOLDER_PATH).into(binding.ivProfileImage);
                }

                // Add more bindings as needed
            }catch (Exception e){
                Log.i(TAG, "bind: ");
            }
        }
    }
}

