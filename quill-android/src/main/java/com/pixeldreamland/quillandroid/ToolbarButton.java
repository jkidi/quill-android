package com.pixeldreamland.quillandroid;

/** ToolbarButton interface
 * @author jkidi(Jakub Kidacki)
 */
public interface ToolbarButton {
   Format getFormat();

   void setFormat(Format format);

   Object getValue();

   void setValue(Object value);

   void setOnValueChangedListener(OnValueChangedListener onValueChangedListener);

   interface OnValueChangedListener {
      void onValueChanged(ToolbarButton toolbarButton, Object value);
   }
}
