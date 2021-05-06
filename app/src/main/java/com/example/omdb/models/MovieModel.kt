package com.example.omdb.models

import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

/**
 * Movie model
 */
class MovieModel(movieJSON: JSONObject) : Serializable {

    /**
     * Movie title
     */
    lateinit var title: String

    /**
     * Movie year
     */
    lateinit var year: String

    /**
     * Movie director
     */
    lateinit var director: String

    /**
     * Movie details
     */
    lateinit var details: String

    /**
     * Movie cast
     */
    lateinit var cast: String

    /**
     * IMDB rating
     */
    lateinit var imdbRating : String

    /**
     * Movie Poster URL
     */
    lateinit var posterUrl: String

    init {
        try {
            title = movieJSON.getString(MOVIE_TITLE)
            year = movieJSON.getString(MOVIE_YEAR)
            director = movieJSON.getString(MOVIE_DIRECTOR)
            details = movieJSON.getString(MOVIE_DETAILS)
            posterUrl = movieJSON.getString(MOVIE_POSTER)
            cast = movieJSON.getString(MOVIE_CAST)
            imdbRating = movieJSON.getString(MOVIE_IMDB_RATING)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    /**
     * Check if title is initialized
     */
    fun isTitleInitialized(): Boolean {
        return ::title.isInitialized
    }


    companion object {
        private val MOVIE_TITLE = "Title"
        private val MOVIE_YEAR = "Year"
        private val MOVIE_DIRECTOR = "Director"
        private val MOVIE_DETAILS = "Plot"
        private val MOVIE_POSTER = "Poster"
        private val MOVIE_CAST = "Actors"
        private val MOVIE_IMDB_RATING = "imdbRating"
    }
}