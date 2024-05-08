package com.adminlive.preetyadminpanel.ui.agency.adapter;

import static com.adminlive.preetyadminpanel.Constant.USER_PLACEHOLDER_PATH;
import static com.adminlive.preetyadminpanel.ui.utils.GlobalFunctions.capitalizeText;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adminlive.preetyadminpanel.databinding.ItemAgencyListBinding;
import com.adminlive.preetyadminpanel.ui.agency.modal.AgencyCreateModel;
import com.adminlive.preetyadminpanel.ui.home.models.UserDetailsModel;
import com.adminlive.preetyadminpanel.ui.utils.UserImageUtils;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CreatedAgencyAdapter extends RecyclerView.Adapter<CreatedAgencyAdapter.ViewHolder> {
    private static final String TAG = "CreatedAgencyAdapter";
    private List<AgencyCreateModel> dataList;
    private Context context;

    public CreatedAgencyAdapter(Context context, List<AgencyCreateModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CreatedAgencyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using ViewBinding
        ItemAgencyListBinding binding = ItemAgencyListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CreatedAgencyAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CreatedAgencyAdapter.ViewHolder holder, int position) {
        AgencyCreateModel item = dataList.get(position);
        // Bind data to views using ViewBinding
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAgencyListBinding binding;

        public ViewHolder(@NonNull ItemAgencyListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(AgencyCreateModel item) {
            try{
                Date date = new Date(item.getJoinDate());

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
                String formattedDate = sdf.format(date);
                // Bind data to views
                binding.tvName.setText(capitalizeText(item.getUsername()));
                binding.tvAgencyCode.setText("Code : "+item.getAgencyCode());
                binding.tvDate.setText("Joining : "+formattedDate);
                if (!Objects.equals(item.getImage(), "")) {
                    Glide.with(context).load(item.getImage()).into(binding.ivProfileImage);
                }else {
                    Bitmap userImage = UserImageUtils.createUserImage(item.getUsername(), 50);
                    Glide.with(context).load(userImage).into(binding.ivProfileImage);
                }

                // Add more bindings as needed
            }catch (Exception e){
                Log.i(TAG, "bind: ");
            }
        }
    }
}

