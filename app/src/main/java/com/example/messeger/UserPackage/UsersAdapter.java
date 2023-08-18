package com.example.messeger.UserPackage;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messeger.R;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersHolder> {

    private static final String TAG = "UsersAdapter_tag";

    private List<User> users = new ArrayList<>();
    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    private OnClickUserListener onClickUserListener;
    public void setOnClickUserListener(OnClickUserListener onClickUserListener) {
        this.onClickUserListener = onClickUserListener;
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_item,
                parent,
                false);
        return new UsersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersHolder holder, int position) {
        User user = users.get(position);

        holder.name.setText(String.format("%s %s %s", user.getName(), user.getLastname(), user.getAge()));
        Log.d(TAG, user.getName() + " " + user.getLastname());

        int bgResId;
        if(user.isOnline()) {
            bgResId = R.drawable.circle_green;
        } else {
            bgResId = R.drawable.circle_red;
        }
        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(), bgResId);
        holder.status.setBackground(background);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickUserListener != null) {
                    onClickUserListener.OnClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    interface OnClickUserListener {
        public void OnClick(User user);
    }

    static class UsersHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final View status;

        public UsersHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_item);
            status = itemView.findViewById(R.id.status_item);
        }
    }
}
