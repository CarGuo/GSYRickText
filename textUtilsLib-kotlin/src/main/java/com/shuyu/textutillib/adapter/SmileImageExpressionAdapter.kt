/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shuyu.textutillib.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

import com.shuyu.textutillib.R
import com.shuyu.textutillib.SmileUtils


open class SmileImageExpressionAdapter(context: Context, textViewResourceId: Int, objects: List<String>) : ArrayAdapter<String>(context, textViewResourceId, objects) {


    override fun getView(position: Int, convertViewT: View?, parent: ViewGroup): View {
        var convertView = convertViewT
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.rich_smile_image_row_expression, null)
        }

        val imageView = convertView!!.findViewById(R.id.iv_expression) as ImageView

        val filename = getItem(position)
        if ("delete_expression" == filename) {
            val resId = context.resources.getIdentifier(filename, "drawable", context.packageName)
            imageView.setImageResource(resId)
        } else {
            val resId = SmileUtils.getRedId(filename)
            imageView.setImageResource(resId)
        }
        return convertView
    }

}
