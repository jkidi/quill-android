package com.pixeldreamland.quillandroid.toolbar;

import com.pixeldreamland.quillandroid.Format;

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
}
