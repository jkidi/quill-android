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

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Editor
 * @author jkidi(Jakub Kidacki)
 */
public class Editor extends WebView {
   private static final String SETUP_HTML = "file:///android_asset/editor.html";

   private List<SelectionChangeListener> selectionChangeListenerList;
   private List<TextChangeListener> textChangeListenerList;
   private boolean initialized;

   private int index;
   private int selectionLength;

   public Editor(Context context) {
      this(context, null);
   }

   public Editor(Context context, AttributeSet attrs) {
      this(context, attrs, android.R.attr.webViewStyle);
   }

   @SuppressLint("SetJavaScriptEnabled")
   public Editor(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);

      selectionChangeListenerList = new ArrayList<>();
      textChangeListenerList = new ArrayList<>();
      initialized = false;

      setVerticalScrollBarEnabled(false);
      setHorizontalScrollBarEnabled(false);
      getSettings().setJavaScriptEnabled(true);
      setWebChromeClient(new WebChromeClient());
      setWebViewClient(new EditorWebViewClient());
      addJavascriptInterface(new WebAppInterface(context), "Android");

      loadUrl(SETUP_HTML);


      addSelectionChangeListener(new SelectionChangeListener() {
         @Override
         public void onSelectionChange(int index, int length, int oldIndex, int oldLength, String source) {
            Editor.this.index = index;
            Editor.this.selectionLength = length;
         }
      });

//    applyAttributes(context, attrs);
   }

   public void exec(final String trigger, final ValueCallback<String> resultCallback) {
      if(initialized) {
         post(new Runnable() {
            @Override
            public void run() {
               if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                  evaluateJavascript(trigger, resultCallback);
               }
               else {
                  // TODO: add callback logic for version < KITKAT
                  loadUrl("javascript:" + trigger);
               }
            }
         });
      }
      else {
         postDelayed(new Runnable() {
            @Override
            public void run() {
               exec(trigger, resultCallback);
            }
         }, 100);
      }
   }

   public void deleteText(int index, int length, ValueCallback<JSONObject> resultCallback) {
      deleteText(index, length, "api", resultCallback);
   }

   public void deleteText(int index, int length, String source, final ValueCallback<JSONObject> resultCallback) {
      exec("deleteText(" + Util.getJavascriptArgs(index, length, source) + ");",
         new ParseJSResultCallback<>(resultCallback, JSONObject.class));
   }

   public void getContents(ValueCallback<JSONObject> resultCallback) {
      getContents(-1, resultCallback);
   }

   public void getContents(int index, ValueCallback<JSONObject> resultCallback) {
      getContents(index, -1, resultCallback);
   }

   public void getContents(int index, int length, final ValueCallback<JSONObject> resultCallback) {
      String trigger;

      if(index < 0) {
         if(length < 0) {
            trigger = "quill.getContents();";
         }
         else {
            trigger = "quill.getContents(" + Util.getJavascriptArgs(0, length) + ");";
         }
      }
      else if(length < 0) {
         if(index < 0) {
            trigger = "quill.getContents();";
         }
         else {
            trigger = "quill.getContents(" + Util.getJavascriptArgs(index) + ");";
         }
      }
      else {
         trigger = "quill.getContents(" + Util.getJavascriptArgs(index, length) + ");";
      }

      exec(trigger, new ParseJSResultCallback<>(resultCallback, JSONObject.class));
   }

   public void getLength(final ValueCallback<Number> resultCallback) {
      exec("getLength();", new ParseJSResultCallback<>(resultCallback, Number.class));
   }

   public void getText(ValueCallback<String> resultCallback) {
      getText(-1, resultCallback);
   }

   public void getText(int index, ValueCallback<String> resultCallback) {
      getText(index, -1, resultCallback);
   }

   public void getText(int index, int length, final ValueCallback<String> resultCallback) {
      String trigger;

      if(index < 0) {
         if(length < 0) {
            trigger = "quill.getText();";
         }
         else {
            trigger = "quill.getText(" + Util.getJavascriptArgs(0, length) + ");";
         }
      }
      else if(length < 0) {
         if(index < 0) {
            trigger = "quill.getText();";
         }
         else {
            trigger = "quill.getText(" + Util.getJavascriptArgs(index) + ");";
         }
      }
      else {
         trigger = "quill.getText(" + Util.getJavascriptArgs(index, length) + ");";
      }

      exec(trigger, new ParseJSResultCallback<>(resultCallback, String.class));
   }

   public void insertEmbed(int index, String type, String value, ValueCallback<JSONObject> resultCallback) {
      insertEmbed(index, type, value, "api", resultCallback);
   }

   public void insertEmbed(int index, String type, String value, String source, final ValueCallback<JSONObject> resultCallback) {
      exec("quill.insertEmbed(" + Util.getJavascriptArgs(index, type, value, source) + ");",
         new ParseJSResultCallback<>(resultCallback, JSONObject.class));
   }

   public void insertText(int index, String text, ValueCallback<JSONObject> resultCallback) {
      insertText(index, text, "api", resultCallback);
   }

   public void insertText(int index, String text, String source, ValueCallback<JSONObject> resultCallback) {
      insertText(index, text, new FormatSet(), source, resultCallback);
   }

   public void insertText(int index, String text, FormatSet formatSet, ValueCallback<JSONObject> resultCallback) {
      insertText(index, text, formatSet, "api", resultCallback);
   }

   public void insertText(int index, String text, FormatSet formatSet, String source, final ValueCallback<JSONObject> resultCallback) {
      exec("quill.insertText(" + Util.getJavascriptArgs(index, text, formatSet, source) + ");",
         new ParseJSResultCallback<>(resultCallback, JSONObject.class));
   }

   public void setContents(JSONObject delta, ValueCallback<JSONObject> resultCallback) {
      setContents(delta, "api", resultCallback);
   }

   public void setContents(JSONObject delta, String source, final ValueCallback<JSONObject> resultCallback) {
      exec("quill.setContents(" + Util.getJavascriptArgs(delta, source) + ");",
         new ParseJSResultCallback<>(resultCallback, JSONObject.class));
   }

   public void setText(String text, ValueCallback<JSONObject> resultCallback) {
      setText(text, "api", resultCallback);
   }

   public void setText(String text, String source, final ValueCallback<JSONObject> resultCallback) {
      exec("quill.setText(" + Util.getJavascriptArgs(text, source) + ");",
         new ParseJSResultCallback<>(resultCallback, JSONObject.class));
   }

   public void updateContents(JSONObject delta, ValueCallback<JSONObject> resultCallback) {
      updateContents(delta, "api", resultCallback);
   }

   public void updateContents(JSONObject delta, String source, final ValueCallback<JSONObject> resultCallback) {
      exec("quill.updateContents(" + Util.getJavascriptArgs(delta, source) + ");",
         new ParseJSResultCallback<>(resultCallback, JSONObject.class));
   }

   public void format(Format format, Object value, ValueCallback<JSONObject> resultCallback) {
      format(format, value, "api", resultCallback);
   }

   public void format(Format format, Object value, String source, ValueCallback<JSONObject> resultCallback) {
      exec("quill.format(" + Util.getJavascriptArgs(format, value, source) + ");",
         new ParseJSResultCallback<>(resultCallback, JSONObject.class));
   }

   public void formatLine(int index, int length, ValueCallback<JSONObject> resultCallback) {
      formatLine(index, length, "api", resultCallback);
   }

   public void formatLine(int index, int length, String source, ValueCallback<JSONObject> resultCallback) {
      formatLine(index, length, new FormatSet(), source, resultCallback);
   }

   public void formatLine(int index, int length, FormatSet formatSet, ValueCallback<JSONObject> resultCallback) {
      formatLine(index, length, formatSet, "api", resultCallback);
   }

   public void formatLine(int index, int length, FormatSet formatSet, String source, ValueCallback<JSONObject> resultCallback) {
      exec("quill.formatLine(" + Util.getJavascriptArgs(index, length, formatSet, source) + ");",
         new ParseJSResultCallback<>(resultCallback, JSONObject.class));
   }

   public void formatText(int index, int length, ValueCallback<JSONObject> resultCallback) {
      formatText(index, length, "api", resultCallback);
   }

   public void formatText(int index, int length, String source, ValueCallback<JSONObject> resultCallback) {
      formatText(index, length, new FormatSet(), source, resultCallback);
   }

   public void formatText(int index, int length, FormatSet formatSet, ValueCallback<JSONObject> resultCallback) {
      formatText(index, length, formatSet, "api", resultCallback);
   }

   public void formatText(int index, int length, FormatSet formatSet, String source, ValueCallback<JSONObject> resultCallback) {
      exec("quill.formatText(" + Util.getJavascriptArgs(index, length, formatSet, source) + ");",
         new ParseJSResultCallback<>(resultCallback, JSONObject.class));
   }

   public void getFormat(ValueCallback<FormatSet> resultCallback) {
      exec("quill.getFormat();", new ParseJSResultCallback<>(resultCallback, FormatSet.class));
   }

   public void getFormat(int index, int length, ValueCallback<FormatSet> resultCallback) {
      exec("quill.getFormat(" + Util.getJavascriptArgs(index, length) + ");",
         new ParseJSResultCallback<>(resultCallback, FormatSet.class));
   }

   public void removeFormat(int index, int length, ValueCallback<JSONObject> resultCallback) {
      removeFormat(index, length, "api", resultCallback);
   }

   public void removeFormat(int index, int length, String source, ValueCallback<JSONObject> resultCallback) {
      exec("quill.removeFormat(" + Util.getJavascriptArgs(index, length, source) + ");",
         new ParseJSResultCallback<>(resultCallback, JSONObject.class));
   }

   public void registerFonts(String[] fonts) {
      exec("registerFonts(" + Util.toJavascriptArg(fonts) + ");", null);
   }

   public void addSelectionChangeListener(SelectionChangeListener selectionChangeListener) {
      this.selectionChangeListenerList.add(selectionChangeListener);
   }

   public void removeSelectionChangeListener(SelectionChangeListener selectionChangeListener) {
      selectionChangeListenerList.remove(selectionChangeListener);
   }

   public void addTextChangeListener(TextChangeListener textChangeListener) {
      this.textChangeListenerList.add(textChangeListener);
   }

   public void removeTextChangeListener(TextChangeListener textChangeListener) {
      textChangeListenerList.remove(textChangeListener);
   }

   public int getIndex() {
      return index;
   }

   public int getSelectionLength() {
      return selectionLength;
   }

   public interface SelectionChangeListener {
      void onSelectionChange(int index, int length, int oldIndex, int oldLength, String source);
   }

   public interface TextChangeListener {
      void onTextChange(JSONObject delta, JSONObject oldDelta, String source);
   }

   private static class ParseJSResultCallback<T> implements ValueCallback<String> {
      private ValueCallback<T> resultCallback;
      private Class<T> type;

      public ParseJSResultCallback(ValueCallback<T> resultCallback, Class<T> type) {
         this.resultCallback = resultCallback;
         this.type = type;
      }

      @Override
      public void onReceiveValue(String value) {
         if(resultCallback != null) {
            resultCallback.onReceiveValue((T) Util.parseJSResultValue(value, type));
         }
      }
   }

   private class WebAppInterface implements SelectionChangeListener, TextChangeListener {
      private Context context;

      public WebAppInterface(Context context) {
         this.context = context;
      }

      @JavascriptInterface
      @Override
      public void onSelectionChange(int index, int length, int oldIndex, int oldLength, String source) {
         for(SelectionChangeListener listener : selectionChangeListenerList) {
            listener.onSelectionChange(index, length, oldIndex, oldLength, source);
         }
      }

      @JavascriptInterface
      @Override
      public void onTextChange(JSONObject delta, JSONObject oldDelta, String source) {
         for(TextChangeListener listener : textChangeListenerList) {
            listener.onTextChange(delta, oldDelta, source);
         }
      }
   }

   private class EditorWebViewClient extends WebViewClient {
      @Override
      public void onPageFinished(WebView view, String url) {
         initialized = url.equalsIgnoreCase(SETUP_HTML);
      }
   }
}
