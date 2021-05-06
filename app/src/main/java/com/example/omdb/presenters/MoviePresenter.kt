package com.example.omdb.presenters

import com.example.omdb.models.MovieModel
import com.example.omdb.views.MainActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


/**
 * Movie presenter
 */
class MoviePresenter(listeningActivity: MainActivity) {

    /**
     * Base OMDb URL
     */
    private val URL: String = "http://www.omdbapi.com/?apikey=675e668a&"


    /**
     * Identity parameter
     */
    private val IDENTITYPARAMETER: String = "t="


    /**
     * Search parameter
     */
    private val SEARCH_PARAMETER: String = "s="


    /**
     * Page parameter
     */
    private val PAGE_PARAMETER: String = "&page="


    /**
     * First Page
     */
    private val FIRST_PAGE = 1


    /**
     * Page number
     */
    private var page = FIRST_PAGE


    /**
     * Activity
     */
    private var activity: MainActivity = listeningActivity


    /**
     * List of Movies
     */
    private var movieModelList: ArrayList<MovieModel> = ArrayList()

    /**
     * List to check whether movie has arrived
     */
    private var arrivedMovies: ArrayList<String> = ArrayList()


    /**
     * List of Movies IDs
     */
    private var moveIDlist: ArrayList<String> = ArrayList()


    /**
     * Current Query
     */
    private var currentQuery: String = ""


    /**
     * Http Client
     */
    private val client = OkHttpClient()


    /**
     * Search movies
     */
    fun searchMovies(query: String) {
        var searchQuery = query

        // Go to next page in search or start new search
        if (query.equals("") || query.equals(currentQuery)) {
            searchQuery = currentQuery
        } else {
            startNewSearch(searchQuery)
        }


        // Create request
        val request = Request.Builder()
            .url(URL + SEARCH_PARAMETER + searchQuery + PAGE_PARAMETER + page)
            .build()

        // Set to next page
        page += 1

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity.showMovieNotFound()
            }

            override fun onResponse(call: Call, response: Response) {
                try {

                    var stringResponse = response.body()!!.string()

                    // Create json object
                    val jsonMovieObject = JSONObject(stringResponse)

                    // Create json array
                    var searchTag = "Search"
                    var titleTag = "Title"
                    var jsonMovieArray: JSONArray = jsonMovieObject.getJSONArray(searchTag)
                    var size: Int = jsonMovieArray.length()

                    // New list of movie titles
                    var newMoviesTitles: ArrayList<String> = ArrayList()

                    // Get Movie IDs from the Search function
                    for (i in 0 until size) {
                        var jsonMovie: JSONObject = jsonMovieArray.getJSONObject(i)
                        var movieID = jsonMovie.getString(titleTag)
                        searchSingleMovie(movieID)
                        if (movieID !in moveIDlist) {
                            moveIDlist.add(movieID)
                            newMoviesTitles.add(movieID)
                        }
                    }

                    // Get movies based on ID's because using the search function yielded movies with not a lot of extra info (no director and summary etc)
                    var IDsize: Int = newMoviesTitles.size
                    for (i in 0 until IDsize) {
                        runBlocking {
                            launch {
                                searchSingleMovie(newMoviesTitles.get(i))
                            }
                        }
                    }

                    activity.refreshScreen(movieModelList)
                } catch (e: Exception) {
                    activity.showMovieNotFound()
                }

            }
        })
    }

    /**
     * Start new Search
     */
    fun startNewSearch(searchQuery: String) {
        currentQuery = searchQuery
        movieModelList = ArrayList()
        moveIDlist = ArrayList()
        arrivedMovies = ArrayList()
        page = 1
    }

    /**
     * Search single movie
     */
    fun searchSingleMovie(movieTitle: String) {
        try {
            val request = Request.Builder()
                .url(URL + IDENTITYPARAMETER + movieTitle)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    activity.showMovieNotFound()
                }

                override fun onResponse(call: Call, response: Response) {
                    var stringResponse = response.body()!!.string()
                    var jsonMovie = JSONObject(stringResponse)
                    var movie = MovieModel(jsonMovie)

                    // I ran into an issue where I did a request once and I got the same response twice which resulted in double entries.
                    // I couldn't find a more elegant way to solve this (as all those solutions gave a concurrent modification exception
                    // So I solved it by adding another list which holds the moves that have already arrived, which seems to have solved the problem
                    if (movie.isTitleInitialized() && movie.title !in arrivedMovies) {
                        movieModelList.add(movie)
                        arrivedMovies.add(movie.title)
                    }
                }
            })
        } catch (e: Exception) {
            activity.showMovieNotFound()
        }
    }
}