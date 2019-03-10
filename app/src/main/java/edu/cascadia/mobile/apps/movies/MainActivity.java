package edu.cascadia.mobile.apps.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import edu.cascadia.mobile.apps.movies.database.MovieDao;
import edu.cascadia.mobile.apps.movies.database.movieDatabase;
import edu.cascadia.mobile.apps.movies.model.MovieEntity;
import edu.cascadia.mobile.apps.movies.ui.MoviesAdapter;
import edu.cascadia.mobile.apps.movies.utilities.SampleData;
import edu.cascadia.mobile.apps.movies.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MoviesAdapter mMoviesAdapter;
    private movieDatabase mDatabase;
    private MainViewModel mViewModel;
    private List<MovieEntity> movieData  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);

        initRecyclerView();
        initViewModel();

        //Get Database
        mDatabase = movieDatabase.getInstance(this);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editMovie = new Intent("edu.cascadia.mobile.apps.movies.EditMovie");
                startActivity(editMovie);
            }
        });

    }

    private void initViewModel(){

        final Observer<List<MovieEntity>> movieObserver = new Observer<List<MovieEntity>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntity> movieEntities) {
                movieData.clear();
                movieData.addAll(movieEntities);
                if(mMoviesAdapter == null){
                    mRecyclerView.setAdapter(new MoviesAdapter(movieData,MainActivity.this));
                }
                else {
                    mMoviesAdapter.notifyDataSetChanged();
                }
            }
        };
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.mMovies.observe(this, movieObserver);
    }

    private void initRecyclerView(){
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addsampledata) {
            addSampleData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addSampleData(){
        mViewModel.addSampleData();
    }
}
