package com.larvaesoft.kamaau.model

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Simple FrameLayout container extension that applies the FooterBarBehavior
 * as the default.
 */
@CoordinatorLayout.DefaultBehavior(FooterBarBehavior::class)
class FooterBarLayout : FrameLayout {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
}
