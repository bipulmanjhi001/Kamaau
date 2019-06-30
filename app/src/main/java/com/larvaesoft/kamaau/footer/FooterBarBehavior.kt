package com.larvaesoft.kamaau.model

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View

/**
 * Simple layout behavior that will track the state of the AppBarLayout
 * and match its offset for a corresponding footer.
 */
class FooterBarBehavior : CoordinatorLayout.Behavior<FooterBarLayout> {
    //Required to instantiate as a default behavior
    constructor() {}

    //Required to attach behavior via XML
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    //This is called to determine which views this behavior depends on
    override fun layoutDependsOn(parent: CoordinatorLayout?,
                                 child: FooterBarLayout?,
                                 dependency: View?): Boolean {
        //We are watching changes in the AppBarLayout
        return dependency is AppBarLayout
    }

    //This is called for each change to a dependent view
    override fun onDependentViewChanged(parent: CoordinatorLayout?,
                                        child: FooterBarLayout?,
                                        dependency: View?): Boolean {
        val offset = -dependency!!.top
        child!!.translationY = offset.toFloat()
        return true
    }
}