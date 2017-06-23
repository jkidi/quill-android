package com.pixeldreamland.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.pixeldreamland.quillandroid.Editor;
import com.pixeldreamland.quillandroid.Toolbar;

public class MainActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      Editor editor = (Editor) findViewById(R.id.editor);
      toolbar.setEditor(editor);
   }
}
