package com.bcit.walksforwalks;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
Custom adapter to handle the recycler view
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users;

    private OnAdapterItemListener onAdapterItemListener;

    public void setAdapterItemListener(OnAdapterItemListener onAdapterItemListener) {
        this.onAdapterItemListener = onAdapterItemListener;
    }

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View lootView = inflater.inflate(R.layout.recycler_row_card, parent, false);
        return new UserViewHolder(lootView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        TextView phone = holder.phone;
        TextView name = holder.name;
        TextView petName = holder.petName;
        TextView petBreed = holder.petBreed;
//        ImageView profilePic = holder.profilePic;

        User user = users.get(position);
        name.setText(user.getFullName());
        petName.setText(user.getPetName());
        petBreed.setText(user.getPetBreed());
        phone.setText(user.getPhone());
//        containerImageView.setImageResource(user.image());

//        deleteButton.setOnClickListener(v -> onAdapterItemListener.onClick(user));

    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    /*
    Custom ViewHolder to handle my recycler view
     */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView petName;
        TextView petBreed;
        TextView phone;
//        ImageView profilePic;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tV_recycler_name);
            petName = itemView.findViewById(R.id.tv_recycler_pet);
            petBreed = itemView.findViewById(R.id.tv_recycler_breed);
            phone = itemView.findViewById(R.id.tv_recycler_phone);
//            profilePic = itemView.findViewById(R.id.image_recyler_profile);

        }
    }
}
