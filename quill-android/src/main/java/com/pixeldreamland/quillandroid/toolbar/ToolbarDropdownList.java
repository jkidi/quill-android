package com.pixeldreamland.quillandroid.toolbar;

import android.content.Context;
import android.widget.Spinner;
import com.pixeldreamland.quillandroid.Format;
import com.pixeldreamland.quillandroid.Util;

/**
 * ToolbarDropdownList
 *
 * @author jkidi(Jakub Kidacki)
 */
public abstract class ToolbarDropdownList extends Spinner implements ToolbarElement {
   private Format format;
   private Object value;
   private Object[] whitelistValues;
   protected OnValueChangedListener onValueChangedListener;

   public ToolbarDropdownList(Context context) {
      super(context);
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
      boolean changed = !Util.equals(this.value, value);

      this.value = value;

      if(changed) {
         super.setSelection(whitelistIndexOf(value));
      }

      if(emitEvent && changed) {
         onValueChangedListener.onValueChanged(this, value);
      }
   }

   @Override
   public void setWhitelistValues(Object[] whitelistValues) {
      this.whitelistValues = whitelistValues;
   }

   @Override
   public Object[] getWhitelistValues() {
      return whitelistValues;
   }

   @Override
   public abstract void clear(boolean emitEvent);

   @Override
   public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
      this.onValueChangedListener = onValueChangedListener;
   }

   @Override
   public void setSelection(int position) {
      super.setSelection(position);
      setValue(whitelistValues[position], true);
   }
}
