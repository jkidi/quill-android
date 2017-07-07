package com.pixeldreamland.quillandroid.toolbar.defaults;

import android.content.Context;
import android.graphics.Color;
import com.pixeldreamland.quillandroid.Format;
import com.pixeldreamland.quillandroid.R;
import com.pixeldreamland.quillandroid.toolbar.ToolbarToggleButton;

/** Default Bold Button
 * @author jkidi(Jakub Kidacki)
 */
public class BoldButton extends ToolbarToggleButton {
   public BoldButton(Context context) {
      super(context);
      setFormat(Format.BOLD);
      setWhitelistValues(new Object[] {true, false});
      setNormalState(R.drawable.ic_format_bold_white_24dp);
      setCheckedColorFilter(Color.BLUE);
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
