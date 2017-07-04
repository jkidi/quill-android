package com.pixeldreamland.quillandroid.toolbar.defaults;

import android.content.Context;
import com.pixeldreamland.quillandroid.Format;
import com.pixeldreamland.quillandroid.toolbar.ToolbarToggleButton;

/** Default Italic Button
 * @author jkidi(Jakub Kidacki)
 */
public class ItalicButton extends ToolbarToggleButton {
   public ItalicButton(Context context) {
      super(context);
      setFormat(Format.ITALIC);
      setWhitelistValues(new Object[] {true, false});
   }

   @Override
   public boolean isChecked() {
      return getValue() != null && getValue().equals(true);
   }

   @Override
   public void clear(boolean emitEvent) {
      setValue(false, emitEvent);
   }

   @Override
   public void toggle(boolean emitEvent) {
      if(isChecked()) {
         setValue(false, emitEvent);
      }
      else {
         setValue(true, emitEvent);
      }
   }
}
