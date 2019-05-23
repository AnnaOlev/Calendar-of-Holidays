package com.example.coursework;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class CountryChoiceActivity extends AppCompatActivity {

    CountriesDatabase countriesDatabase = new CountriesDatabase(this);
    HolidaysDatabase holidaysDatabase = new HolidaysDatabase(this);
    RecyclerView mRecyclerView;
    ArrayList<Country> mCountries = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_choice);

        mRecyclerView = findViewById(R.id.countryRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));

        if (countriesDatabase.getData() == 0){
            new GetCountriesTask().execute();
        }
        else {
            ArrayList<Country> mCountries = countriesDatabase.getAllCountries();
            mRecyclerView.setAdapter(new CountryAdapter(mCountries, this));
        }
    }

    private class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryHolder> {
        private ArrayList<Country> mListCountries = new ArrayList<>();
        private Context context;


        CountryAdapter(List<Country> countries, Context context) {
            mListCountries.addAll(countries);
            this.context = context;
        }

        @Override
        public CountryHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_of_list_check, viewGroup, false );
            return new CountryHolder(view);
        }

        @Override
        public void onBindViewHolder(CountryHolder itemHolder, int position) {
            Country country = mListCountries.get(position);
            itemHolder.bindListItem(country);
        }

        @Override
        public int getItemCount() {
            return mListCountries == null ? 0 : mListCountries.size();
        }

        private class CountryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView mInfoTextView;
            CheckBox mCheckBox;

            CountryHolder(View view) {
                super(view);

                itemView.setOnClickListener(this);
                mInfoTextView = view.findViewById(R.id.infoTextCountry);
                mCheckBox = view.findViewById(R.id.checkBox);
                view.setOnClickListener(this);

                mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    SQLiteDatabase db = countriesDatabase.getWritableDatabase();
                    if (isChecked) {
                        mListCountries.get(getAdapterPosition()).setIfAdded("yes");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("name", mListCountries.get(getAdapterPosition()).getName());
                        contentValues.put("code", mListCountries.get(getAdapterPosition()).getCode());
                        contentValues.put("added", "yes");
                        db.update("countries_table", contentValues, "id = ?", new String[]
                                {mListCountries.get(getAdapterPosition()).getId()});
                    }
                    else {
                        mListCountries.get(getAdapterPosition()).setIfAdded("no");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("name", mListCountries.get(getAdapterPosition()).getName());
                        contentValues.put("code", mListCountries.get(getAdapterPosition()).getCode());
                        contentValues.put("added", mListCountries.get(getAdapterPosition()).getIfAdded());
                        db.update("countries_table", contentValues, "id = ?", new String[]
                                {mListCountries.get(getAdapterPosition()).getId()});
                        holidaysDatabase.deleteEntry(mListCountries.get(getAdapterPosition()).getCode());
                    }
                });
            }

            void bindListItem(Country country) {
                mInfoTextView.setText(country.getName());
                if (country.getIfAdded().equals("yes")) {
                    mCheckBox.setChecked(true);
                } else {
                    mCheckBox.setChecked(false);
                }
            }

            @Override
            public void onClick (View view){
            }
        }
    }

    void parceCountryList(){

        String name;
        String code;
        StringBuilder html = new StringBuilder();

        try {
            java.net.CookieManager cm = new java.net.CookieManager();
            java.net.CookieHandler.setDefault(cm);
            URL url = new URL("https://date.nager.at/Home/Countries");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                html.append(line);
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Received HTML: " + html);

        Matcher matches = Pattern.compile(Pattern.quote("<td><a href=\"/PublicHoliday/Country/") + "(\\w{2})" +
                Pattern.quote("\">") + "(\\w*)" + Pattern.quote("</a>")).matcher(html);

        while (matches.find()){
            code = matches.group(1);
            name = matches.group(2);
            Log.i(TAG, "Received country: " + name);
            Log.i(TAG, "Received code: " + code);
            countriesDatabase.insertCountry(name, code, "no");
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetCountriesTask extends AsyncTask<Void,Void,List<Country>> {
        @Override
        protected List<Country> doInBackground(Void... params) {

            parceCountryList();
            mCountries = countriesDatabase.getAllCountries();

            return mCountries;
        }

        @Override
        protected void onPostExecute(List<Country> countries) {

            mRecyclerView.setAdapter(new CountryAdapter(mCountries, getBaseContext()));
        }
    }
}
