package com.pixeldreamland.quillandroid.toolbar.defaults;

import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import com.pixeldreamland.quillandroid.toolbar.ToolbarDropdownList;

import java.util.Arrays;

/** Default Font Dropdown List
 * @author jkidi(Jakub Kidacki)
 */
public class FontDropdownList extends ToolbarDropdownList {
   public FontDropdownList(Context context) {
      super(context);
      setBackgroundColor(Color.WHITE);
   }

   @Override
   public void clear(boolean emitEvent) {

   }

   @Override
   public void setWhitelistValues(Object[] whitelistValues) {
      super.setWhitelistValues(whitelistValues);

      ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
         Arrays.copyOf(whitelistValues, whitelistValues.length, String[].class));
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      setAdapter(adapter);
   }
}
