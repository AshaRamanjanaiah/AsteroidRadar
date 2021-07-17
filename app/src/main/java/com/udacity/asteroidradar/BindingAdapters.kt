package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.api.AsteroidImage
import com.udacity.asteroidradar.main.AsteroidApiStatus

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("imageUrl")
fun bingImageURL(imageView: ImageView, asteroidImage: AsteroidImage?) {
    val context = imageView.context
    if (asteroidImage != null && asteroidImage.media_type == "image") {
        Picasso.with(imageView.context)
            .load(asteroidImage.url)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .error(R.drawable.placeholder_picture_of_day)
            .into(imageView)

        imageView.contentDescription = String.format(context.getString(R.string.nasa_picture_of_day_content_description_format), asteroidImage.title)
    }
}

@BindingAdapter("imageText")
fun bindImageTitle(textView: TextView, title: String?) {
    val context = textView.context
    if (!title.isNullOrEmpty()) {
        textView.text = title
        textView.contentDescription = "$title image"
    } else {
        textView.text = textView.context.getString(R.string.image_of_the_day)
        textView.contentDescription = textView.context.getString(R.string.image_of_the_day)
    }
}

@BindingAdapter("asteroidApiStatus")
fun bindStatus(progressbar: ProgressBar, status: AsteroidApiStatus?) {
    when (status) {
        AsteroidApiStatus.LOADING -> {
            progressbar.visibility = View.VISIBLE
        }
        AsteroidApiStatus.ERROR -> {
            progressbar.visibility = View.GONE
        }
        AsteroidApiStatus.DONE -> {
            progressbar.visibility = View.GONE
        }
    }
}


