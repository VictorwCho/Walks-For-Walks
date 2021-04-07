package com.bcit.walksforwalks;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

/*
Custom adapter to handle the recycler view
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context mContext;
    private final List<User> users;

//    private OnAdapterItemListener onAdapterItemListener;
//
//    public void setAdapterItemListener(OnAdapterItemListener onAdapterItemListener) {
//        this.onAdapterItemListener = onAdapterItemListener;
//    }

    public UserAdapter(Context context, List<User> users) {
        this.users = users;
        this.mContext = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_row_card, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.getFullName());
        holder.petName.setText(user.getPetName());
        holder.petBreed.setText(user.getPetBreed());
        holder.phone.setText(user.getPhone());
        Picasso.with(mContext)
                .load(user.getProfilePic())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.profilePic);

//        deleteButton.setOnClickListener(v -> onAdapterItemListener.onClick(user));
        ///April 5:20
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MessageActivity.class);
                String name = user.getFullName();
                String phone = user.getPhone();
                String email = user.getEmail();
                String pic = user.getProfilePic();
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.putExtra("photo", pic);
                v.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), Ratings.class);
                v.getContext().startActivity(intent);
                return true;
            }
        });


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
        ImageView profilePic;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tV_recycler_name);
            petName = itemView.findViewById(R.id.tv_recycler_pet);
            petBreed = itemView.findViewById(R.id.tv_recycler_breed);
            phone = itemView.findViewById(R.id.tv_recycler_phone);
            profilePic = itemView.findViewById(R.id.image_recyler_profile);
        }
    }
}
