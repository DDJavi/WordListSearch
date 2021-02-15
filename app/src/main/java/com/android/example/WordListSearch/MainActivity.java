package com.android.example.WordListSearch;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public static final int editWord = 1;
    public static final int addWord = -1;

    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    private WordListOpenHelper mDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDB = new WordListOpenHelper(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new WordListAdapter(this, mDB);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EditWordActivity.class);
                startActivityForResult(intent, editWord);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == editWord) {
            if (resultCode == RESULT_OK) {
                String word = data.getStringExtra(EditWordActivity.EXTRA_REPLY);
                if (!TextUtils.isEmpty(word)) {
                    int id = data.getIntExtra(WordListAdapter.IDextra, -99);
                    if (id == addWord) {
                        mDB.insert(word);
                        mAdapter.notifyDataSetChanged();
                    } else if (id >= 0) {
                        mDB.update(id, word);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}