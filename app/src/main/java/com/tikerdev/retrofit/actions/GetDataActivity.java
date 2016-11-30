package com.tikerdev.retrofit.actions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tikerdev.retrofit.R;
import com.tikerdev.retrofit.adapter.UserAdapter;
import com.tikerdev.retrofit.models.User;
import com.tikerdev.retrofit.models.UserList;
import com.tikerdev.retrofit.service.HttpManager;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetDataActivity extends AppCompatActivity {

    private String TAG = GetDataActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private UserAdapter adapter;

    private UserList userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);
        init();
        loadUser();

    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UserAdapter();
        adapter.setUserList(userList);
        recyclerView.setAdapter(adapter);
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
                                   for (User user : userList.getUser()) {
                                       Log.d(TAG, "user : " + user.getUser() + " name : " + user.getName());
                                       adapter.setUserList(userList);
                                   }
                               }
                           }

                );
    }

}
