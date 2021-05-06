package com.example.omdb.views

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.omdb.R
import com.example.omdb.inflate
import com.example.omdb.models.MovieModel
import com.squareup.picasso.Picasso

/**
 * Movie recycler adapter
 */
class MovieRecyclerAdapter(private val movieModels: ArrayList<MovieModel>) :
    RecyclerView.Adapter<MovieRecyclerAdapter.MovieHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val inflatedView = parent.inflate(R.layout.row_video_item, false)
        return MovieHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val itemPhoto = movieModels[position]
        holder.bindMovie(itemPhoto)
    }

    override fun getItemCount(): Int = movieModels.size

    /**
     * Movie holder
     */
    class MovieHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        /**
         * Movie
         */
        private var movieModel: MovieModel? = null


        init {
            view.setOnClickListener(this)
        }

        /**
         * Bind movie to view
         */
        fun bindMovie(movieModel: MovieModel) {
            this.movieModel = movieModel

            // Get views
            val title = view.findViewById<TextView>(R.id.movie_title)
            val year = view.findViewById<TextView>(R.id.movie_year)
            val director = view.findViewById<TextView>(R.id.movie_director)
            val rating = view.findViewById<TextView>(R.id.movie_rating)
            var thumbnail = view.findViewById<ImageView>(R.id.movie_image)

            // Set views
            title.text = movieModel.title
            year.text = movieModel.year
            director.text = movieModel.director
            rating.text = movieModel.imdbRating
            Picasso.get().load(movieModel.posterUrl).into(thumbnail)
        }

        override fun onClick(v: View) {
            val context = itemView.context
            val showPhotoIntent = Intent(context, MovieActivity::class.java)
            showPhotoIntent.putExtra(MOVIEKEY, movieModel)
            context.startActivity(showPhotoIntent)
        }

        companion object {
            private val MOVIEKEY = "MOVIE"
        }
    }

}