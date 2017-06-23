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

import android.util.JsonReader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

/** Utility methods
 * @author jkidi(Jakub Kidacki)
 */
public class Util {
   public static Object parseJSResultValue(String value, Class<?> type) {
      if(type == JSONObject.class) {
         return parseToJSONObject(value);
      }
      else if(type == FormatSet.class) {
         return parseToFormatSet(value);
      }
      else if(type == String.class) {
         return parseToString(value);
      }
      else if(type == Integer.class) {
         return parseToInteger(value);
      }

      return null;
   }

   public static JSONObject parseToJSONObject(String str) {
      JSONObject jsonObject = null;

      try {
         jsonObject = new JSONObject(str);
      }
      catch(JSONException e) {
         e.printStackTrace();
      }

      return jsonObject;
   }

   public static FormatSet parseToFormatSet(String str) {
      JSONObject jsonObject = parseToJSONObject(str);
      FormatSet formatSet = new FormatSet();

      try {
         if(jsonObject != null) {
            Iterator<String> iter = jsonObject.keys();

            while(iter.hasNext()) {
               String key = iter.next();
               Object value = jsonObject.get(key);

               for(Format format : Format.values()) {
                  if(format.getName().equalsIgnoreCase(key)) {
                     formatSet.add(format, value);
                     break;
                  }
               }
            }
         }
      }
      catch(JSONException e) {
         e.printStackTrace();
      }

      return formatSet;
   }

   public static String parseToString(String str) {
      JsonReader jsonReader = new JsonReader(new StringReader(str));
      jsonReader.setLenient(true);

      try {
         return jsonReader.nextString();
      }
      catch(IOException e) {
         e.printStackTrace();
      }

      return "";
   }

   public static Integer parseToInteger(String str) {
      Integer value = null;

      try {
         value = Integer.parseInt(str);
      }
      catch(NumberFormatException e) {
         e.printStackTrace();
      }

      return value;
   }

   public static String toJavascriptArg(Object value) {
      if(value == null) {
         return "null";
      }

      if(value instanceof String) {
         return "'" + value + "'";
      }
      else if(value instanceof Format) {
         return "'" + ((Format) value).getName() + "'";
      }
      else {
         return value.toString();
      }
   }

   public static String getJavascriptArgs(Object ... values) {
      String args = "";

      for(int i = 0; i < values.length; i++) {
         args += toJavascriptArg(values[i]);

         if(i != values.length - 1) {
            args += ",";
         }
      }

      return args;
   }
}
