package com.example.shuyu.bind

import android.databinding.DataBindingComponent

/**
 * kotlin 需要提供DataBindingComponent来指定BindingAdapter
 * Created by guoshuyu on 2017/10/20.
 */
open class RichEditTextBindingsComponent : DataBindingComponent {

    override fun getCompanion(): RichEditTextBindings.Companion = RichEditTextBindings
}