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
import com.pixeldreamland.quillandroid.toolbar.defaults.BoldButton;
import com.pixeldreamland.quillandroid.toolbar.defaults.ItalicButton;
import com.pixeldreamland.quillandroid.toolbar.defaults.StrikeButton;
import com.pixeldreamland.quillandroid.toolbar.defaults.UnderlineButton;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/** Toolbar
 * @author jkidi(Jakub Kidacki)
 */
public class Toolbar extends LinearLayout implements ToolbarElement.OnValueChangedListener {
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

   private Map<Format, Class<? extends ToolbarElement>> formatClassMap;
   private Format[] formats;
   private Map<Format, ToolbarElement> toolbarElementMap;

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

      formatClassMap = new HashMap<>();
      formatClassMap.put(Format.BOLD, BoldButton.class);
      formatClassMap.put(Format.ITALIC, ItalicButton.class);
      formatClassMap.put(Format.UNDERLINE, UnderlineButton.class);
      formatClassMap.put(Format.STRIKE, StrikeButton.class);

      initElements();
   }

   public ToolbarRenderer getRenderer() {
      return renderer;
   }

   public void setRenderer(ToolbarRenderer renderer) {
      this.renderer = renderer;
   }

   private void initElements() {
      containerLayout.removeAllViews();
      toolbarElementMap = new HashMap<>();

      for(Format format : formats) {
         Class<? extends ToolbarElement> cls = formatClassMap.get(format);
         ToolbarElement element;

         try {
            Constructor<? extends ToolbarElement> constructor = cls.getConstructor(Context.class);
            element = constructor.newInstance(getContext());
            element.setFormat(format);
            element.setOnValueChangedListener(this);

            // TODO: Get rid of the whole renderer thing
            if(element instanceof ToolbarToggleButton) {
               ToolbarToggleButton toggleButton = (ToolbarToggleButton) element;
               toggleButton.setNormalState(renderer.getNormalState(format));
               toggleButton.setCheckedState(renderer.getCheckedState(format));
               toggleButton.setNormalColorFilter(renderer.getNormalColorFilter(format));
               toggleButton.setCheckedColorFilter(renderer.getCheckedColorFilter(format));
            }

            containerLayout.addView((View) element);
            toolbarElementMap.put(format, element);
         }
         catch(Exception e) {
            e.printStackTrace();
         }
      }
   }

   private void resetButtons() {
      for(ToolbarElement button : toolbarElementMap.values()) {
         button.clear(false);
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
                     toolbarElementMap.get(format).setValue(formatSet.getValue(format), true);
                  }
               }
            });
         }
      });
   }
}
