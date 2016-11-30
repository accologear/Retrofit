package com.tikerdev.retrofit.actions;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.annotation.BoolRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tikerdev.retrofit.R;
import com.tikerdev.retrofit.databinding.ActivityPostDataBinding;
import com.tikerdev.retrofit.models.Response;
import com.tikerdev.retrofit.models.User;
import com.tikerdev.retrofit.models.UserList;
import com.tikerdev.retrofit.service.HttpManager;
import com.tikerdev.retrofit.adapter.UserAdapter;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PostDataActivity extends AppCompatActivity
        implements UserAdapter.OnItemLongClickListener, UserAdapter.OnItemClickListener {
    ActivityPostDataBinding binding;

    private String TAG = PostDataActivity.class.getSimpleName();


    private UserAdapter adapter;
    private UserList listUser = new UserList();


    private int idForUpdate = -1;
    private int positionForUpdate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_data);
        init();
        loadUser();
    }

    private void init() {

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UserAdapter();
        adapter.setUserList(listUser);

        binding.recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);
        adapter.setItemLongClickListener(this);

    }

    private void clearEditText() {
        binding.username.setText("");
        binding.fullname.setText("");
    }

    public void refresh(View view) {
        loadUser();
    }

    public void addData(View view) {
        postUser();
    }

    public void updateData(View view) {
        if (idForUpdate != -1) {
            updateUser();
        } else {
            Toast.makeText(this, "please select your item for update", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUser() {
        Observable<UserList> data = HttpManager.getInstance().getService().loadUserList();
        data.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserList userList) {
                        listUser = userList;
                        adapter.setUserList(userList);
                        Toast.makeText(PostDataActivity.this, "load success", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUser() {
        String user = binding.username.getText().toString();
        String name = binding.fullname.getText().toString();

        final Observable<User> update = HttpManager.getInstance().getService()
                .updateUser(idForUpdate, user, name);
        update.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {
                        idForUpdate = -1;
                        positionForUpdate = -1;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error : " + e.getMessage());
                    }

                    @Override
                    public void onNext(User user) {
                        if (user != null) {
                            listUser.getUser().set(positionForUpdate, user);
                            adapter.notifyItemChanged(positionForUpdate, user);
                            Toast.makeText(PostDataActivity.this, "update success", Toast.LENGTH_SHORT).show();
                        }
                        clearEditText();
                    }
                });
    }

    private void postUser() {

        if (binding.username.getText().length() == 0) {
            binding.username.setError("please enter your username ");
            return;
        }
        if (binding.fullname.getText().length() == 0) {
            binding.fullname.setError("please enter your full name ");
            return;
        }

        String user = binding.username.getText().toString();
        String name = binding.fullname.getText().toString();


        Observable<User> post = HttpManager.getInstance().getService().postUser(user, name);
        post.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error : " + e.getMessage());
                    }

                    @Override
                    public void onNext(User user) {
                        if (user != null) {
                            listUser.getUser().add(0, user);
                            adapter.setUserList(listUser);
                            adapter.notifyItemInserted(0);
                            binding.recyclerView.smoothScrollToPosition(0);
                            clearEditText();
                            Toast.makeText(PostDataActivity.this, "add success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void deleteUser(int userId, final int position) {
        Observable<Response> data = HttpManager.getInstance().getService()
                .deleteUser(userId);
        data.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error : " + e.getMessage());
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.isSuccess()) {
                            listUser.getUser().remove(position);
                            adapter.notifyItemRemoved(position);
                            Toast.makeText(PostDataActivity.this, "delete success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onLongClicked(int position) {
        User user = listUser.getUser().get(position);
        binding.username.setText(user.getUser());
        binding.fullname.setText(user.getName());
        positionForUpdate = position;
        idForUpdate = user.getId();
    }

    @Override
    public void onClicked(final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int userId = listUser.getUser().get(position).getId();
                deleteUser(userId, position);
            }
        });
        builder.setMessage("Confirm your delete item ?");
        Dialog dialog = builder.create();
        dialog.show();
    }
}
