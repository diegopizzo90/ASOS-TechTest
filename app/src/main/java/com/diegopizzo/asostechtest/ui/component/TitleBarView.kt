package com.diegopizzo.asostechtest.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.res.getResourceIdOrThrow
import com.diegopizzo.asostechtest.R
import com.diegopizzo.asostechtest.databinding.ComponentTitleBarBinding
import com.google.android.material.card.MaterialCardView

class TitleBarView(context: Context, attrs: AttributeSet) : MaterialCardView(context, attrs) {

    private val binding = ComponentTitleBarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        if (!isInEditMode) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView)
            val titleRes = styledAttributes.getResourceIdOrThrow(R.styleable.TitleBarView_title_bar)
            binding.tvCardTitle.text = context.getText(titleRes)

            styledAttributes.recycle()
        }
    }

    fun setTitleBar(title: String) {
        binding.tvCardTitle.text = title
    }
}