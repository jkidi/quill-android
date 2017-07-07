package com.pixeldreamland.quillandroid.toolbar;

import android.annotation.SuppressLint;
import com.pixeldreamland.quillandroid.Format;
import com.pixeldreamland.quillandroid.Util;

/** ToolbarButton interface
 * @author jkidi(Jakub Kidacki)
 */
public interface ToolbarElement {
   Format getFormat();

   void setFormat(Format format);

   Object getValue();

   void setValue(Object value, boolean emitEvent);

   void setWhitelistValues(Object[] whitelistValues);

   Object[] getWhitelistValues();
   
   void clear(boolean emitEvent);

   void setOnValueChangedListener(OnValueChangedListener onValueChangedListener);

   interface OnValueChangedListener {
      void onValueChanged(ToolbarElement toolbarElement, Object value);
   }

   @SuppressLint("NewApi")
   default boolean whitelistContains(Object value) {
      if(getWhitelistValues() == null) {
         return true;
      }

      return whitelistIndexOf(value) >= 0;
   }

   @SuppressLint("NewApi")
   default int whitelistIndexOf(Object value) {
      for(int i = 0; i < getWhitelistValues().length; i++) {
         if(Util.equals(getWhitelistValues()[i], value)) {
            return i;
         }
      }

      return -1;
   }
}
