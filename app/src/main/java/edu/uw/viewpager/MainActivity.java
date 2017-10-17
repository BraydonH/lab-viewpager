package edu.uw.viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements MovieListFragment.OnMovieSelectedListener,
    SearchFragment.OnSearchListener {

    private static final String TAG = "MainActivity";
    public static final String MOVIE_LIST_FRAGMENT_TAG = "MoviesListFragment";
    public static final String MOVIE_DETAIL_FRAGMENT_TAG = "DetailFragment";
    private ViewPager mViewPager;
    private SearchFragment mSearchFragment;
    private MovieListFragment mMovieListFragment;
    private DetailFragment mDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchFragment = SearchFragment.newInstance();

        mViewPager = (ViewPager) findViewById(R.id.container);
        MoviePagerAdapter adapter = new MoviePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

    }

    @Override
    public void onSearchSubmitted(String searchTerm) {
        mMovieListFragment  = MovieListFragment.newInstance(searchTerm);
        mViewPager.getAdapter().notifyDataSetChanged();
        mViewPager.setCurrentItem(1);
    }

    private class MoviePagerAdapter extends FragmentStatePagerAdapter {

        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position < 0 || position > 2) {
                throw new IllegalArgumentException("position must in range [0,2]. Given " + position);
            }
            if(position == 0) {
                return mSearchFragment;
            }
            if(position == 1) {
                return mMovieListFragment;
            }
            return mDetailFragment;
        }

        @Override
        public int getCount() {
            if(mMovieListFragment == null) {
                return 1;
            }else if(mDetailFragment == null) {
                return 2;
            }
            return 3;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    //respond to search button clicking
    public void handleSearchClick(View v){
        EditText text = (EditText)findViewById(R.id.txt_search);
        String searchTerm = text.getText().toString();

        MovieListFragment fragment = MovieListFragment.newInstance(searchTerm);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, MOVIE_LIST_FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onMovieSelected(Movie movie) {
        mDetailFragment = DetailFragment.newInstance(movie);
        mViewPager.getAdapter().notifyDataSetChanged();
        mViewPager.setCurrentItem(2);
    }

    @Override
    public void onBackPressed() {
        MoviePagerAdapter adapter = (MoviePagerAdapter) mViewPager.getAdapter();
        if(adapter.getCount() == 0) {
            super.onBackPressed();
        }else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }
}
