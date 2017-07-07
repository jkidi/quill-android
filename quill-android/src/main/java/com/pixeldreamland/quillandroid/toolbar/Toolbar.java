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
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.pixeldreamland.quillandroid.*;
import com.pixeldreamland.quillandroid.toolbar.defaults.*;

import java.lang.reflect.Constructor;
import java.util.*;

/** Toolbar
 * @author jkidi(Jakub Kidacki)
 */
public class Toolbar extends LinearLayout implements ToolbarElement.OnValueChangedListener {
   public static final ToolbarFormat[] ALL_TYPES = new ToolbarFormat[]{
      new ToolbarFormat(Format.BOLD),
      new ToolbarFormat(Format.ITALIC),
      new ToolbarFormat(Format.UNDERLINE),
      new ToolbarFormat(Format.STRIKE),
      new ToolbarFormat(Format.FONT, new Object[] { "monospace", "sans-serif", "sans-serif-condensed", "sans-serif-medium"})
      // TODO add the rest
   };

   private HorizontalScrollView scrollView;
   private LinearLayout containerLayout;

   private Map<Format, Class<? extends ToolbarElement>> formatClassMap;
   private ToolbarFormat[] toolbarFormats;
   private Map<Format, List<ToolbarElement>> toolbarElementMap;

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
      toolbarFormats = ALL_TYPES;

      formatClassMap = new HashMap<>();
      formatClassMap.put(Format.BOLD, BoldButton.class);
      formatClassMap.put(Format.ITALIC, ItalicButton.class);
      formatClassMap.put(Format.UNDERLINE, UnderlineButton.class);
      formatClassMap.put(Format.STRIKE, StrikeButton.class);
      formatClassMap.put(Format.FONT, FontDropdownList.class);

      initElements();
   }

   private void initElements() {
      containerLayout.removeAllViews();
      toolbarElementMap = new HashMap<>();

      for(ToolbarFormat toolbarFormat : toolbarFormats) {
         Class<? extends ToolbarElement> cls = formatClassMap.get(toolbarFormat.getFormat());
         ToolbarElement element;

         try {
            Constructor<? extends ToolbarElement> constructor = cls.getConstructor(Context.class);
            element = constructor.newInstance(getContext());
            element.setFormat(toolbarFormat.getFormat());
            element.setOnValueChangedListener(this);

            if(toolbarFormat.getWhitelistValues() != null) {
               element.setWhitelistValues(toolbarFormat.getWhitelistValues());
            }

            containerLayout.addView((View) element);

            List<ToolbarElement> elementList = toolbarElementMap.get(toolbarFormat.getFormat());

            if(elementList != null) {
               elementList.add(element);
            }
            else {
               elementList = new ArrayList<>();
               elementList.add(element);
               toolbarElementMap.put(toolbarFormat.getFormat(), elementList);
            }
         }
         catch(Exception e) {
            e.printStackTrace();
         }
      }
   }

   private void resetButtons() {
      for(List<ToolbarElement> elementList : toolbarElementMap.values()) {
         for(ToolbarElement element : elementList) {
            element.clear(false);
         }
      }
   }

   @Override
   public void onValueChanged(ToolbarElement toolbarElement, Object value) {
      editor.format(toolbarElement.getFormat(), value, null);
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
                     List<ToolbarElement> elementList = toolbarElementMap.get(format);
                     Object value = formatSet.getValue(format);

                     for(ToolbarElement element : elementList) {
                        if(element.whitelistContains(value)) {
                           element.setValue(value, true);
                        }
                     }
                  }
               }
            });
         }
      });

      // register fonts
      List<ToolbarElement> fontElements = toolbarElementMap.get(Format.FONT);
      Set<String> fonts = new LinkedHashSet<>();

      for(ToolbarElement element : fontElements) {
         for(Object value : element.getWhitelistValues()) {
            fonts.add((String) value);
         }
      }

      if(fonts.size() > 0) {
         editor.registerFonts(fonts.toArray(new String[fonts.size()]));
      }
   }

   public static class ToolbarFormat {
      private Format format;
      private Object[] whitelistValues;

      public ToolbarFormat(Format format) {
         this.format = format;
      }

      public ToolbarFormat(Format format, Object[] whitelistValues) {
         this.format = format;
         this.whitelistValues = whitelistValues;
      }

      public Format getFormat() {
         return format;
      }

      public void setFormat(Format format) {
         this.format = format;
      }

      public Object[] getWhitelistValues() {
         return whitelistValues;
      }

      public void setWhitelistValues(Object[] whitelistValues) {
         this.whitelistValues = whitelistValues;
      }
   }
}
