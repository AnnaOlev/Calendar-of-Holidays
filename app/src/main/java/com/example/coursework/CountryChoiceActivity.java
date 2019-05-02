package com.example.coursework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    RecyclerView mRecyclerView;
    ArrayList<Country> mCountries = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_choice);


        mRecyclerView = findViewById(R.id.countryRecycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));

        if (countriesDatabase.getData() == 0){
            new GetCountriesTask().execute();
        }
        else {
            ArrayList<Country> mCountries = countriesDatabase.getAllCountries();
            mRecyclerView.setAdapter(new CountryAdapter(mCountries, this));
        }
    }

    private class CountryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mInfoTextView;

        CountryHolder(View view) {
            super(view);

            itemView.setOnClickListener(this);
            mInfoTextView = view.findViewById(R.id.infoText);
        }

        void bindListItem(Country country) {
            mInfoTextView.setText(country.getName());
        }

        @Override
        public void onClick (View view){
            // если успею, тут будет вызов поиска инфы о празднике из википедии
        }
    }

    private class CountryAdapter extends RecyclerView.Adapter<CountryChoiceActivity.CountryHolder> {
        private ArrayList<Country> mListCountries = new ArrayList<>(); // тоже список праздников, но для адаптера
        private Context context;

        CountryAdapter(List<Country> countries, Context context) {
            mListCountries.addAll(countries);
            // в этот массив сунули только нужные праздники
            this.context = context;
        }

        @Override
        public CountryChoiceActivity.CountryHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_of_list, viewGroup, false );
            return new CountryChoiceActivity.CountryHolder(view);
        }

        @Override
        public void onBindViewHolder(CountryChoiceActivity.CountryHolder itemHolder, int position) {
            Country country = mListCountries.get(position);
            itemHolder.bindListItem(country);
        }

        @Override
        public int getItemCount() {
            return mListCountries == null ? 0 : mListCountries.size();
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
