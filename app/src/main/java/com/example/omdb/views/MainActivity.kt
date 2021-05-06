package com.example.omdb.views

import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omdb.R
import com.example.omdb.models.MovieModel
import com.example.omdb.presenters.MoviePresenter

import kotlin.collections.ArrayList

/**
 * Main activity
 */
class MainActivity : AppCompatActivity() {

    /**
     * Layout manager
     */
    private lateinit var linearLayoutManager: LinearLayoutManager

    /**
     * Movie Recycler Adapter
     */
    private lateinit var adapter: MovieRecyclerAdapter

    /**
     * Recycler view
     */
    private lateinit var recyclerView: RecyclerView

    /**
     * Movie requester
     */
    private lateinit var moviePresenter: MoviePresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        linearLayoutManager = LinearLayoutManager(this)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    moviePresenter.searchMovies("")
                }
            }
        })

        moviePresenter = MoviePresenter(this)

        createSearchView()
    }

    /**
     * Create search view
     */
    private fun createSearchView() {
        var searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!query.isNullOrBlank() && !query.isNullOrEmpty()) {
                    moviePresenter.searchMovies(query)

                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    /**
     * Refresh screen
     */
    fun refreshScreen(movieModelList: ArrayList<MovieModel>) {
        runOnUiThread {
            adapter = MovieRecyclerAdapter(movieModelList)
            recyclerView.adapter = adapter
            val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()

            if (movieModelList.size > 1) {
                adapter.notifyDataSetChanged()
            }

            // This is where I attempted to not only refresh the screen but also keep the same position, but this doesn't seem to work consistently
            recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            if (adapter.itemCount >= 10) recyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }

    /**
     * Show Movie not found
     */
    fun showMovieNotFound() {
        Looper.prepare()
        Toast.makeText(this@MainActivity, getString(R.string.move_not_found), Toast.LENGTH_LONG).show()
        Looper.loop()
    }
}




