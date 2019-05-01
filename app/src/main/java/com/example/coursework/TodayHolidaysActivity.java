package com.example.coursework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
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
    String date; // текущая дата
    RecyclerView mRecyclerView;
    HolidaysDatabase holidaysDatabase = new HolidaysDatabase(this);

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_holidays);

        date = getIntent().getStringExtra("clickedDate");

        mRecyclerView = findViewById(R.id.holiday_recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getBaseContext(), DividerItemDecoration.HORIZONTAL);
        mRecyclerView.addItemDecoration(itemDecor); // добавление разделителя

        if (!holidaysDatabase.getData(date)) {
            new GetHolidaysTask().execute(); // вызов асинка для подгрузки праздников
        } else {
            mHolidays = holidaysDatabase.getAllHolidays();
            mRecyclerView.setAdapter(new HolidayAdapter(mHolidays, getBaseContext()));
        }
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
            // если успею, тут будет вызов поиска инфы о празднике из википедии
        }
    }

    private class HolidayAdapter extends RecyclerView.Adapter<TodayHolidaysActivity.HolidayHolder> {
        private ArrayList<Holiday> mListHolidays = new ArrayList<>(); // тоже список праздников, но для адаптера
        private Context context;

        HolidayAdapter(List<Holiday> holidays, Context context) {
            for (int i = 0; i < holidays.size(); i++){
                if (holidays.get(i).getDate().equals(date)){
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

            return new HolidayGetter().fetchItems();
        }

        @Override
        protected void onPostExecute(List<Holiday> holidays) {
            mHolidays.addAll(holidays); // наверное потом надо сделать тут бд? или не тут
            for (int i = 0; i < mHolidays.size(); i++) {
                Holiday holiday = mHolidays.get(i);
                holidaysDatabase.insertHoliday(holiday.getDate(), holiday.getName(), holiday.getLocalName(), holiday.getCountryCode());
            }
            mRecyclerView.setAdapter(new HolidayAdapter(mHolidays, getBaseContext()));
        }
    }
}


