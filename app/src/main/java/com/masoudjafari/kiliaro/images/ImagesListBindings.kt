/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.masoudjafari.kiliaro.images

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.masoudjafari.kiliaro.ImagesAdapter
import com.masoudjafari.kiliaro.data.Image

/**
 * [BindingAdapter]s for the [Image]s list.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Image>?) {
    items?.let {
        (listView.adapter as ImagesAdapter).submitList(items)
    }
}
