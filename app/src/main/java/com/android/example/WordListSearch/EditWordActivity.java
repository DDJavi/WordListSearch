/*
 * Copyright (C) 2016 Google Inc.
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

package com.android.example.WordListSearch;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;


/**
 * Activity to edit an existing or create a new word.
 */
public class EditWordActivity extends AppCompatActivity {



    private static final int noID = -99;
    private static final String noWord = "";

    private EditText mEditWordView;

    public static final String EXTRA_REPLY = "com.example.android.WordListSearch.REPLY";

    int mId = MainActivity.addWord;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        mEditWordView = (EditText) findViewById(R.id.edit_word);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt(WordListAdapter.IDextra, noID);
            String word = extras.getString(WordListAdapter.wordExtra, noWord);
            if (id != noID && word != noWord) {
                mId = id;
                mEditWordView.setText(word);
            }
        }
    }

   
    public void returnReply(View view) {
        String word = ((EditText) findViewById(R.id.edit_word)).getText().toString();

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, word);
        replyIntent.putExtra(WordListAdapter.IDextra, mId);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}

