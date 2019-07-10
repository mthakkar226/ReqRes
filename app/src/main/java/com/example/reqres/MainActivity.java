package com.example.reqres;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.reqres.api.UserApi;
import com.example.reqres.api.UserInterface;
import com.example.reqres.model.data;
import com.example.reqres.model.User;
import com.example.reqres.utils.ScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;

    private static final String TAG = "MainActivity";

    CustomAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 4;
    private int currentPage = PAGE_START;

    private UserInterface userInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.tvPost);

        rv = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);

        adapter = new CustomAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new ScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreItems();
                    }
                },1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        userInterface = UserApi.getClient().create(UserInterface.class);

        loadFirstPage();

        createUser();
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        callUsersApi().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                List<data> datas = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(datas);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private List<data> fetchResults(Response<User> response) {
        User u = response.body();
        return u.getData();
    }

    private Call<User> callUsersApi() {
        return userInterface.getUsers(currentPage);
    }

    private void createUser(){
        data d = new data("John","software engineer");

        Call<data> call = userInterface.creatUser(d);

        call.enqueue(new Callback<data>() {
            @Override
            public void onResponse(Call<data> call, Response<data> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                data dataResponse = response.body();

                String content = "Post Response:" + "\n";
                content += "ID: " + dataResponse.getId() + "\n";
                content += "Name: " + dataResponse.getName() + "\n";
                content += "Job: " + dataResponse.getJob() + "\n";
                content += "created at: " + dataResponse.getCreatedAt() + "\n\n";

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<data> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}
