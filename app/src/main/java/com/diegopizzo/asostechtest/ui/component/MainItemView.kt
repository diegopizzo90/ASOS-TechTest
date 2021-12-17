package com.diegopizzo.asostechtest.ui.component

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.diegopizzo.asostechtest.R
import com.diegopizzo.asostechtest.databinding.ComponentMainItemBinding

class MainItemView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = ComponentMainItemBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        if (!isInEditMode) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.MainItemView)

            setImageViewContent(
                binding.ivIcon,
                styledAttributes,
                R.styleable.MainItemView_icon_image
            )

            setTextContent(
                binding.tvLabelText1,
                styledAttributes,
                R.styleable.MainItemView_label_text_1
            )
            setTextContent(
                binding.tvLabelText2,
                styledAttributes,
                R.styleable.MainItemView_label_text_2
            )
            setTextContent(
                binding.tvLabelText3,
                styledAttributes,
                R.styleable.MainItemView_label_text_3
            )
            setTextContent(
                binding.tvLabelText4,
                styledAttributes,
                R.styleable.MainItemView_label_text_4
            )
            setTextContent(
                binding.tvLabelText5,
                styledAttributes,
                R.styleable.MainItemView_label_text_5
            )
            setTextContent(
                binding.tvLabelText6,
                styledAttributes,
                R.styleable.MainItemView_label_text_6
            )
            setTextContent(
                binding.tvLabelText7,
                styledAttributes,
                R.styleable.MainItemView_label_text_7
            )
            setTextContent(
                binding.tvLabelText8,
                styledAttributes,
                R.styleable.MainItemView_label_text_8
            )

            setImageViewContent(
                binding.ivIcon,
                styledAttributes,
                R.styleable.MainItemView_small_icon_image
            )

            styledAttributes.recycle()
        }
    }

    private fun setTextContent(
        textView: AppCompatTextView,
        styledAttributes: TypedArray,
        attributeValue: Int
    ) {
        val labelTextRes = styledAttributes.getResourceId(attributeValue, -1)
        textView.text = if (labelTextRes != -1) context.getString(labelTextRes) else ""
    }

    private fun setImageViewContent(
        imageView: ImageView,
        styledAttributes: TypedArray,
        attributeValue: Int
    ) {
        val imageRes = styledAttributes.getResourceId(attributeValue, -1)
        if (imageRes != -1) imageView.setImageResource(imageRes)
    }

    fun setItemContentView(
        iconImageUrl: String?,
        labelText1: String,
        labelText2: String,
        labelText3: String,
        labelText4: String,
        labelText5: String,
        labelText6: String,
        labelText7: String,
        labelText8: String,
        smallIconRes: Int?
    ) {
        binding.apply {
            Glide.with(this@MainItemView).load(iconImageUrl)
                .placeholder(R.drawable.ic_close_black)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(ivIcon)

            tvLabelText1.text = labelText1
            tvLabelText2.text = labelText2
            tvLabelText3.text = labelText3
            tvLabelText4.text = labelText4
            tvLabelText5.text = labelText5
            tvLabelText6.text = labelText6
            tvLabelText7.text = labelText7
            tvLabelText8.text = labelText8
            if (smallIconRes != null) {
                ivSmallIcon.setImageDrawable(AppCompatResources.getDrawable(context, smallIconRes))
            } else {
                ivSmallIcon.visibility = GONE
            }
        }
    }
}