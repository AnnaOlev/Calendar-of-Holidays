package com.example.coursework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class TodayHolidaysActivity extends AppCompatActivity {

    ArrayList<Holiday> mHolidays = new ArrayList<>(); // основной массив праздников
    ArrayList<Country> mCountries = new ArrayList<>();
    String date; // текущая дата
    RecyclerView mRecyclerView;
    Context context;
    HolidaysDatabase holidaysDatabase = new HolidaysDatabase(this);
    CountriesDatabase countriesDatabase = new CountriesDatabase(this);

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_holidays);

        date = getIntent().getStringExtra("clickedDate");
        context = this;

        mRecyclerView = findViewById(R.id.holiday_recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getBaseContext(), DividerItemDecoration.HORIZONTAL);
        mRecyclerView.addItemDecoration(itemDecor); // добавление разделителя

        mCountries = countriesDatabase.getAllCountries();
        mHolidays = holidaysDatabase.getAllHolidays();

        //if (holidaysDatabase.getData() == 0)
            new GetHolidaysTask().execute();
    }

    private class HolidayHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mInfoTextView;

        HolidayHolder(View view) {
            super(view);

            itemView.setOnClickListener(this);
            mInfoTextView = view.findViewById(R.id.infoText);
        }

        void bindListItem(Holiday holiday) {
            mInfoTextView.setText(holiday.toString());
        }

        @Override
        public void onClick (View view){
            Bundle bundle = new Bundle();
            EventToFavDialog eventToFavDialog = new EventToFavDialog();
            bundle.putString("EVENT_DATA", mInfoTextView.getText().toString());
            eventToFavDialog.setArguments(bundle);
            eventToFavDialog.show(getSupportFragmentManager(), "dialog");
            Intent intent = new Intent();
            intent.putExtra("message_return", mInfoTextView.getText().toString());
            setResult(RESULT_OK, intent);
        }
    }

    private class HolidayAdapter extends RecyclerView.Adapter<TodayHolidaysActivity.HolidayHolder> {
        private ArrayList<Holiday> mListHolidays = new ArrayList<>(); // тоже список праздников, но для адаптера
        private Context context;

        HolidayAdapter(List<Holiday> holidays, Context context) {
            for (int i = 0; i < holidays.size(); i++){
                if (holidays.get(i).getDate().equals(date)){ // добавляем если соответствует дата
                    Holiday holiday = holidays.get(i);
                    mListHolidays.add(holiday);
                    Log.i(TAG, "This holiday was added: " + holidays.get(i).toString()); // лень по точкам, вот вам лог
                }
            }
            // в этот массив сунули только нужные праздники
            this.context = context;
        }

        @Override
        public TodayHolidaysActivity.HolidayHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_of_list, viewGroup, false );
            return new TodayHolidaysActivity.HolidayHolder(view);
        }

        @Override
        public void onBindViewHolder(TodayHolidaysActivity.HolidayHolder itemHolder, int position) {
            Holiday holiday = mListHolidays.get(position);
            itemHolder.bindListItem(holiday);
        }

        @Override
        public int getItemCount() {
            return mListHolidays == null ? 0 : mListHolidays.size();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetHolidaysTask extends AsyncTask<Void,Void,List<Holiday>> {
        @Override
        protected List<Holiday> doInBackground(Void... params) {
            /*if (check){
                return new HolidayGetter().fetchItems(newCountries);
            } else*/
            return new HolidayGetter().fetchItems(mCountries, context);
        }

        @Override
        protected void onPostExecute(List<Holiday> holidays) {

            mHolidays.addAll(holidays);

            for (int i = 0; i < holidays.size(); i++) {
                Holiday holiday = holidays.get(i);
                holidaysDatabase.insertHoliday(holiday.getDate(), holiday.getName(),
                            holiday.getLocalName(), holiday.getCountryCode());
            }
            mRecyclerView.setAdapter(new HolidayAdapter(mHolidays, getBaseContext()));
        }
    }
}
