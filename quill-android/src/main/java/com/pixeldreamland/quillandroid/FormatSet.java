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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/** FormatSet
 * @author jkidi(Jakub Kidacki)
 */
public class FormatSet {
   private Map<Format, Object> formats;

   public FormatSet() {
      formats = new HashMap<>();
   }

   public FormatSet add(Format format, Object value) {
      formats.put(format, value);
      return this;
   }

   public Collection<Format> getFormats() {
      return formats.keySet();
   }

   public Object getValue(Format format) {
      return formats.get(format);
   }

   @Override
   public String toString() {
      JSONObject jsonObject = new JSONObject();

      for(Format format : formats.keySet()) {
         try {
            jsonObject.put(format.getName(), formats.get(format));
         }
         catch(JSONException e) {
            e.printStackTrace();
         }
      }

      return jsonObject.toString();
   }
}
