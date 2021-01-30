package com.gaurav.android_gisfy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gaurav.android_gisfy.adapter.UserAdapter;
import com.gaurav.android_gisfy.database.DatabaseClient;
import com.gaurav.android_gisfy.database.User;

import java.util.List;

public class UserDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tView_Nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);


        recyclerView = findViewById(R.id.recyclerView);
        tView_Nodata = findViewById(R.id.tView_Nodata);

        getTasks();

    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<User>> {

            @Override
            protected List<User> doInBackground(Void... voids) {
                List<User> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .myDao()
                        .getUsers();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<User> tasks) {
                super.onPostExecute(tasks);
                UserAdapter userAdapter = new UserAdapter(UserDetails.this, tasks);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(userAdapter);

                if (tasks.isEmpty()) {
                    tView_Nodata.setVisibility(View.VISIBLE);
                }
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

}