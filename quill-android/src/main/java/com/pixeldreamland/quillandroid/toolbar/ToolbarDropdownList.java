package com.pixeldreamland.quillandroid.toolbar;

import android.content.Context;
import android.widget.ListView;
import com.pixeldreamland.quillandroid.Format;

/** ToolbarDropdownList
 * @author jkidi(Jakub Kidacki)
 */
public abstract class ToolbarDropdownList extends ListView implements ToolbarElement {
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
      this.value = value;

      if(emitEvent) {
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
}
