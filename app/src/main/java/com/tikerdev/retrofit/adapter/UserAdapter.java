package com.tikerdev.retrofit.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tikerdev.retrofit.BR;
import com.tikerdev.retrofit.R;
import com.tikerdev.retrofit.models.User;
import com.tikerdev.retrofit.models.UserList;

/**
 * Created by Error404 on 29/11/2559.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public interface OnItemLongClickListener {
        void onLongClicked(int position);
    }

    public interface OnItemClickListener {
        void onClicked(int position);
    }

    private UserList userList;
    private OnItemLongClickListener itemLongClickListener;
    private OnItemClickListener itemClickListener;

    public void setUserList(UserList userList) {
        this.userList = userList;
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.user_item, parent, false);
        return new ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        holder.onBind(userList.getUser().get(position));
    }

    @Override
    public int getItemCount() {
        if (userList == null) {
            return 0;
        }
        if (userList.getUser() == null) {
            return 0;
        }
        return userList.getUser().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener, View.OnClickListener {

        private ViewDataBinding mViewDataBinding;


        public ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            mViewDataBinding = viewDataBinding;
            viewDataBinding.getRoot().setOnClickListener(this);
            viewDataBinding.getRoot().setOnLongClickListener(this);
        }

        public void onBind(User user) {
            mViewDataBinding.setVariable(BR.user, user);
            mViewDataBinding.executePendingBindings();
        }

        @Override
        public boolean onLongClick(View view) {
            itemLongClickListener.onLongClicked(getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClicked(getAdapterPosition());
        }
    }
}