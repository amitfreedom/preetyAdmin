package com.adminlive.preetyadminpanel.agency.ui.home.adapter;

import static com.adminlive.preetyadminpanel.Constant.USER_PLACEHOLDER_PATH;
import static com.adminlive.preetyadminpanel.ui.utils.GlobalFunctions.capitalizeText;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adminlive.preetyadminpanel.databinding.ItemAgencyHostUserBinding;
import com.adminlive.preetyadminpanel.databinding.ItemHostRequestBinding;
import com.adminlive.preetyadminpanel.ui.host.modal.HostModal;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

public class AgencyHostUser extends RecyclerView.Adapter<AgencyHostUser.ViewHolder> {
    private static final String TAG = "AgencyHostUser";
    private List<HostModal> dataList;
    private Context context;

    private AgencyHostUser.Select select;

    public interface Select{
        void pressItem(HostModal hostModal);
    }

    public AgencyHostUser(Context context, List<HostModal> dataList, AgencyHostUser.Select select) {
        this.context = context;
        this.dataList = dataList;
        this.select = select;
    }

    @NonNull
    @Override
    public AgencyHostUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using ViewBinding
        ItemAgencyHostUserBinding binding = ItemAgencyHostUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AgencyHostUser.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AgencyHostUser.ViewHolder holder, int position) {
        HostModal item = dataList.get(position);
        // Bind data to views using ViewBinding
        holder.bind(item,position);

        holder.binding.textView.setOnClickListener(view -> {
            select.pressItem(item);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemAgencyHostUserBinding binding;

        public ViewHolder(@NonNull ItemAgencyHostUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(HostModal item,int position) {
            try{
//                String capitalizedText = item.getRealName().substring(0, 1).toUpperCase() + item.getRealName().substring(1);
                binding.tvSN.setText(String.valueOf(position+1));
                binding.tvName.setText(capitalizeText(item.getRealName()));
                binding.tvDate.setText("Code : "+item.getAgencyCode());
                binding.tvJoiningDate.setText("Joined : "+item.getJoiningDate());
                if (!Objects.equals(item.getPhoto(), "")) {
                    Glide.with(context).load(item.getPhoto()).into(binding.ivProfileImage);
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

