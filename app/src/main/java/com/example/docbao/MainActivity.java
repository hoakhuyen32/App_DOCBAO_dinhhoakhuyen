package com.example.docbao;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvTieuDe;
    ArrayList<String> arrayTitle, arrayLink;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTieuDe = findViewById(R.id.listViewTD);
        arrayTitle = new ArrayList<>();
        arrayLink = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayTitle);
        lvTieuDe.setAdapter(adapter);

        new ReadRSS().execute("https://vnexpress.net/rss/kinh-doanh.rss");
        lvTieuDe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("linkTinTuc", arrayLink.get(position));
                startActivity(intent);
            }
        });
    }

    private class ReadRSS extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> titles = new ArrayList<>();
            arrayLink = new ArrayList<>();

            try {
                Document doc = Jsoup.connect(strings[0]).get();
                Elements items = doc.select("item");

                for (Element item : items) {
                    String title = item.select("title").first().text();
                    titles.add(title);

                    // Lấy link chi tiết của bài báo
                    String link = item.select("link").first().text();
                    arrayLink.add(link);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return titles;
        }
        @Override
        protected void onPostExecute(ArrayList<String> titles) {
            super.onPostExecute(titles);
            arrayTitle.clear();
            arrayTitle.addAll(titles);
            adapter.notifyDataSetChanged();
        }
    }
}
