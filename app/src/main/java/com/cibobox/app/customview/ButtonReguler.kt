package com.eisuchi.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.Button


@SuppressLint("AppCompatCustomView")
class ButtonReguler : Button {
    constructor(context: Context) : super(context) {
        val face = Typeface.createFromAsset(context.assets, "font/Metropolis-Medium.otf")
        this.typeface = face
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val face = Typeface.createFromAsset(context.assets, "font/Metropolis-Medium.otf")
        this.typeface = face
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        val face = Typeface.createFromAsset(context.assets, "font/Metropolis-Medium.otf")
        this.typeface = face
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}

