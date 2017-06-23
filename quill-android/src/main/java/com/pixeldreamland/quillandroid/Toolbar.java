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

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.ValueCallback;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

/** Toolbar
 * @author jkidi(Jakub Kidacki)
 */
public class Toolbar extends LinearLayout implements ToolbarButton.OnValueChangedListener {
   public static final Format[] ALL_TYPES = new Format[] {
      Format.BOLD,
      Format.ITALIC,
      Format.UNDERLINE,
      Format.STRIKE
      // TODO add the rest
   };

   private HorizontalScrollView scrollView;
   private LinearLayout containerLayout;

   private ToolbarRenderer renderer;
   private Format[] formats;
   private Map<Format, ToggleToolbarButton> buttonMap;

   private Editor editor;

   public Toolbar(Context context) {
      super(context);
      init(null, 0);
   }

   public Toolbar(Context context, AttributeSet attrs) {
      super(context, attrs);
      init(attrs, 0);
   }

   public Toolbar(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      init(attrs, defStyle);
   }

   private void init(AttributeSet attrs, int defStyle) {
      inflate(getContext(), R.layout.toolbar_rich_editor, this);
      scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
      containerLayout = (LinearLayout) findViewById(R.id.containerLayout);
      setRenderer(new DefaultToolbarRenderer());
      formats = ALL_TYPES;
      initButtons();
   }

   public ToolbarRenderer getRenderer() {
      return renderer;
   }

   public void setRenderer(ToolbarRenderer renderer) {
      this.renderer = renderer;
   }

   private void initButtons() {
      containerLayout.removeAllViews();
      buttonMap = new HashMap<>();

      for(Format format : formats) {
         ToggleToolbarButton button = new ToggleToolbarButton(getContext());
         button.setFormat(format);
         button.setNormalState(renderer.getNormalState(format));
         button.setCheckedState(renderer.getCheckedState(format));
         button.setNormalColorFilter(renderer.getNormalColorFilter(format));
         button.setCheckedColorFilter(renderer.getCheckedColorFilter(format));
         button.setOnValueChangedListener(this);
         containerLayout.addView(button);
         buttonMap.put(format, button);
      }
   }

   private void resetButtons() {
      for(ToggleToolbarButton button : buttonMap.values()) {
         button.setChecked(false, false);
      }
   }

   @Override
   public void onValueChanged(ToolbarButton toolbarButton, Object value) {
      editor.format(toolbarButton.getFormat(), value, null);
   }

   public Editor getEditor() {
      return editor;
   }

   public void setEditor(final Editor editor) {
      this.editor = editor;
      editor.addSelectionChangeListener(new Editor.SelectionChangeListener() {
         @Override
         public void onSelectionChange(int index, int length, int oldIndex, int oldLength, String source) {
            editor.getFormat(new ValueCallback<FormatSet>() {
               @Override
               public void onReceiveValue(FormatSet formatSet) {
                  resetButtons();

                  for(Format format : formatSet.getFormats()) {
                     buttonMap.get(format).setChecked((Boolean) formatSet.getValue(format));
                  }
               }
            });
         }
      });
   }


}
