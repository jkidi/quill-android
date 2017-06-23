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

import android.graphics.Color;

/** A default renderer for the Toolbar
 * @author jkidi(Jakub Kidacki)
 */
public class DefaultToolbarRenderer implements ToolbarRenderer {
   @Override
   public int getNormalState(Format type) {
      if(type == Format.BOLD) {
         return R.drawable.ic_format_bold_white_24dp;
      }
      else if(type == Format.ITALIC) {
         return R.drawable.ic_format_italic_white_24dp;
      }
      else if(type == Format.UNDERLINE) {
         return R.drawable.ic_format_underlined_white_24dp;
      }
      else if(type == Format.STRIKE) {
         return R.drawable.ic_strikethrough_s_white_24dp;
      }

      return 0;
   }

   @Override
   public int getCheckedState(Format type) {
      return 0;
   }

   @Override
   public int getNormalColorFilter(Format type) {
      return -1;
   }

   @Override
   public int getCheckedColorFilter(Format type) {
      return Color.BLUE;
   }
}
