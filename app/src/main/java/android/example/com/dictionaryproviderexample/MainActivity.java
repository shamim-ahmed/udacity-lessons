/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.example.com.dictionaryproviderexample;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.provider.UserDictionary.Words;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * This is the central activity for the Provider Dictionary Example App. The purpose of this app is
 * to show an example of accessing the {@link Words} list via its' Content Provider.
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ContentResolver which will send a message to the ContentProvider
        ContentResolver resolver = getContentResolver();

        // Get a Cursor containing all of the rows in the Words table

        StringBuilder resultBuilder = new StringBuilder();

        try (Cursor cursor = resolver.query(UserDictionary.Words.CONTENT_URI, null, null, null, null)) {
            int n = cursor.getCount();
            resultBuilder.append("Number of words : ").append(n).append("\n");

            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(Words._ID);
                int wordIndex = cursor.getColumnIndex(Words.WORD);
                int frequencyIndex = cursor.getColumnIndex(Words.FREQUENCY);

                int id = cursor.getInt(idIndex);
                String word = cursor.getString(wordIndex);
                int frequency = cursor.getInt(frequencyIndex);

                resultBuilder.append(id).append(" ").append(word).append(" ").append(frequency).append("\n");
            }
        }


        TextView textView = (TextView) findViewById(R.id.dictionary_text_view);
        textView.setText(resultBuilder.toString());
    }
}
