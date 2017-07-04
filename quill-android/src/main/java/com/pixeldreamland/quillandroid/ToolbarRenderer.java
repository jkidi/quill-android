/**
 * Copyright (C) 2017 Pixel Dreamland LLC
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pixeldreamland.quillandroid;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

/** ToolbarRenderer interface
 * @author jkidi(Jakub Kidacki)
 */
// TODO: Get rid of the whole renderer thing
public interface ToolbarRenderer {
   @DrawableRes
   int getNormalState(Format format);
   @DrawableRes
   int getCheckedState(Format format);
   @ColorInt
   int getNormalColorFilter(Format format);
   @ColorInt
   int getCheckedColorFilter(Format format);
}
