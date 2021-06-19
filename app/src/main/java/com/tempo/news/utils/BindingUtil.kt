package com.tempo.news.utils

import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputLayout
import com.tempo.news.R
import com.tempo.news.utils.Extensions.afterTextChanged
import com.tempo.news.utils.Extensions.snackError
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object BindingUtil {
    @BindingAdapter("error")
    @JvmStatic
    fun error(view: View, error: Int) {
        if (error == 0) return
        when (view) {
            is EditText -> view.error =
                view.getContext().getString(error)
            is TextInputLayout -> {
                error(view, error)
            }
            else -> view.snackError(error)
        }
    }

    @BindingAdapter("error")
    @JvmStatic
    fun error(view: TextInputLayout, error: Int) {
        if (error == 0) return
        view.isErrorEnabled = true
        view.error = view.context.getString(error)
        if (view.tag != null && view.tag is TextWatcher) return  //watcher already set
        if (view.editText != null) view.editText!!.afterTextChanged {
            view.error = null
            view.isErrorEnabled = false
            view.tag = this
        }
    }


    @BindingAdapter("snack_error")
    @JvmStatic
    fun snackError(view: View, @StringRes error: Int) {
        if (error != 0)
            view.snackError(error)
    }

    @BindingAdapter("snack_error")
    @JvmStatic
    fun snackError(view: View, error: String?) {
        error?.let { view.snackError(error) }
    }

    @BindingAdapter("url_src")
    @JvmStatic
    fun url_src(view: ImageView, url: String?) {
        ImageLoader.loadImage(view, url)
    }

    @BindingAdapter("hasFixedSize")
    @JvmStatic
    fun hasFixedSize(view: RecyclerView, value: Boolean?) {
        value?.let { view.setHasFixedSize(it) }
    }

    @BindingAdapter("round_corners")
    @JvmStatic
    fun round_corners(imageView: ShapeableImageView, round_corners: Boolean) {
        if (!round_corners) return
        val radius = imageView.context.resources.getDimension(R.dimen.default_corner_radius)
        imageView.shapeAppearanceModel = imageView.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius)
            .build()

    }


    @BindingAdapter("firstChar")
    @JvmStatic
    fun firstChar(view: TextView, text: String?) {
        text?.let {
            view.text = text.toCharArray()[0].toString()
        }
    }

    @BindingAdapter("visibility")
    @JvmStatic
    fun visibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("formatDate")
    @JvmStatic
    //converts date in format such as "2018-02-02t065457-744z"
    fun formatDate(view: TextView, text: String?) {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val output = SimpleDateFormat("MMM dd yy")

        var d: Date? = null
        try {
            d = input.parse("2018-02-02T06:54:57.744Z")
            view.text = output.format(d)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    @BindingAdapter("enableCollapsing")
    @JvmStatic
    fun enableCollapsing(collapsingToolbarLayout: CollapsingToolbarLayout, enabled: Boolean?) {
        enabled?.let {
            val lp = collapsingToolbarLayout.layoutParams as AppBarLayout.LayoutParams
            if (it) {
                lp.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
            } else {
                lp.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
            }
            collapsingToolbarLayout.layoutParams = lp
        }
    }
}