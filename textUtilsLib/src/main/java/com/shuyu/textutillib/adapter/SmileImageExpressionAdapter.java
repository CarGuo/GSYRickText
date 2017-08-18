/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
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
package com.shuyu.textutillib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.shuyu.textutillib.R;
import com.shuyu.textutillib.SmileUtils;

import java.util.List;


public class SmileImageExpressionAdapter extends ArrayAdapter<String> {

    public SmileImageExpressionAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.rich_smile_image_row_expression, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_expression);

        String filename = getItem(position);
        if ("delete_expression".equals(filename)) {
            int resId = getContext().getResources().getIdentifier(filename, "drawable", getContext().getPackageName());
            imageView.setImageResource(resId);
        } else {
            int resId = SmileUtils.getRedId(filename);
            imageView.setImageResource(resId);
        }
        return convertView;
    }

}
