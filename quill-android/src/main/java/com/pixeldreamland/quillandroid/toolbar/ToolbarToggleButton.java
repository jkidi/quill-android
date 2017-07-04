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

package com.pixeldreamland.quillandroid.toolbar;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;
import com.pixeldreamland.quillandroid.Format;

/** Toggle button implementation that switches between two values
 * @author jkidi(Jakub Kidacki)
 */
public abstract class ToolbarToggleButton extends ImageButton implements ToolbarElement {
   protected Format format;
   protected Object value;
   protected Object[] whitelistValues;
   protected OnValueChangedListener onValueChangedListener;

   private @DrawableRes
   int normalState;

   private @DrawableRes
   int checkedState;
   private @ColorInt
   int normalColorFilter;

   private @ColorInt
   int checkedColorFilter;

   public ToolbarToggleButton(Context context) {
      super(context);
   }

   public ToolbarToggleButton(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   @Override
   public Format getFormat() {
      return format;
   }

   @Override
   public void setFormat(Format format) {
      this.format = format;
   }

   @Override
   public Object getValue() {
      return value;
   }

   @Override
   public void setValue(Object value, boolean emitEvent) {
      this.value = value;
      update();

      if(onValueChangedListener != null && emitEvent) {
         onValueChangedListener.onValueChanged(this, value);
      }
   }

   @Override
   public Object[] getWhitelistValues() {
      return whitelistValues;
   }

   @Override
   public void setWhitelistValues(Object[] whitelistValues) {
      this.whitelistValues = whitelistValues;
   }

   public int getNormalState() {
      return normalState;
   }

   public void setNormalState(int normalState) {
      this.normalState = normalState;
      update();
   }

   public int getCheckedState() {
      return checkedState;
   }

   public void setCheckedState(int checkedState) {
      this.checkedState = checkedState;
      update();
   }

   public int getNormalColorFilter() {
      return normalColorFilter;
   }

   public void setNormalColorFilter(int normalColorFilter) {
      this.normalColorFilter = normalColorFilter;
      update();
   }

   public int getCheckedColorFilter() {
      return checkedColorFilter;
   }

   public void setCheckedColorFilter(int checkedColorFilter) {
      this.checkedColorFilter = checkedColorFilter;
      update();
   }

   @Override
   public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
      this.onValueChangedListener = onValueChangedListener;
   }

   public abstract boolean isChecked();

   public abstract void clear(boolean emitEvent);

   public abstract void toggle(boolean emitEvent);

   private void update() {
      if(isChecked()) {
         if(checkedState != 0) {
            setImageResource(checkedState);
         }

         setColorFilter(checkedColorFilter);
      }
      else {
         setImageResource(normalState);
         setColorFilter(normalColorFilter);
      }
   }

   @Override
   public boolean onTouchEvent(MotionEvent event) {
      if(event.getAction() == MotionEvent.ACTION_UP) {
         this.toggle(true);
      }

      return super.onTouchEvent(event);
   }
}
