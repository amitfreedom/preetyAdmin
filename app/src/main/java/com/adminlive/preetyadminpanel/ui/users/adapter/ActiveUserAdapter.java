package com.adminlive.preetyadminpanel.ui.users.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adminlive.preetyadminpanel.Constant;
import com.adminlive.preetyadminpanel.databinding.ListActiveUsersBinding;
import com.adminlive.preetyadminpanel.ui.users.models.LiveUser;
import com.adminlive.preetyadminpanel.ui.utils.FirestoreAdapter;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class ActiveUserAdapter extends FirestoreAdapter<ActiveUserAdapter.ViewHolder> {

    public interface OnActiveUserSelectedListener {

        void onActiveUserSelected(DocumentSnapshot user);

    }

    private ActiveUserAdapter.OnActiveUserSelectedListener mListener;


    public ActiveUserAdapter(Query query, ActiveUserAdapter.OnActiveUserSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ActiveUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActiveUserAdapter.ViewHolder(ListActiveUsersBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ActiveUserAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ListActiveUsersBinding binding;

        public ViewHolder(ListActiveUsersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final DocumentSnapshot snapshot,
                         final ActiveUserAdapter.OnActiveUserSelectedListener listener) {

            LiveUser restaurant = snapshot.toObject(LiveUser.class);

            assert restaurant != null;
            if (Objects.equals(restaurant.getLiveType(), "0")){
                binding.liveType.setText("Video Live");
            }

            if (Objects.equals(restaurant.getLiveType(), "1")){
                binding.liveType.setText("Audio Party");
            }

            if (Objects.equals(restaurant.getPhoto(), "")){
                // Load image
                Glide.with(binding.ivProfileImage.getContext())
                        .load(Constant.USER_PLACEHOLDER_PATH)
                        .into(binding.ivProfileImage);
            }else {
                // Load image
                Glide.with(binding.ivProfileImage.getContext())
                        .load(restaurant.getPhoto())
                        .into(binding.ivProfileImage);
            }


            binding.tvName.setText(restaurant.getUsername());
            binding.tvUid.setText("ID : "+ restaurant.getUid());

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onActiveUserSelected(snapshot);
                    }
                }
            });
        }

    }
}

