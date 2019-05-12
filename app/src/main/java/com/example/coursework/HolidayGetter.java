package com.example.coursework;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

class HolidayGetter {

    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    List<Holiday> fetchItems(List<Country> countries, Context context) { // тут надо будет немного паработать над параметрами
        List<Holiday> holidays = new ArrayList<>();
        HolidaysDatabase holidaysDatabase = new HolidaysDatabase(context);

        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getIfAdded().equals("yes") && !holidaysDatabase.getDataByCountry(countries.get(i).getCode())){
                try {
                    String url = Uri.parse("https://date.nager.at/api/v2/PublicHolidays/2019/" + countries.get(i).getCode())
                            .buildUpon()
                            .appendQueryParameter("format", "json")
                            .appendQueryParameter("nojsoncallback", "1")
                            .appendQueryParameter("extras", "url_s")
                            .build().toString();
                    String jsonString = getUrlString(url);
                    Log.i(TAG, "Received JSON: " + jsonString);
                    JSONArray jsonBody = new JSONArray(jsonString);
                    parseItems(holidays, jsonBody);
                } catch (IOException ioe) {
                    Log.e(TAG, "Failed to fetch items", ioe);
                } catch (JSONException je) {
                    Log.e(TAG, "Failed to parse JSON", je);
                }
            }
            else if (countries.get(i).getIfAdded().equals("no") && holidaysDatabase.getDataByCountry(countries.get(i).getCode())){
                Log.e(TAG, "DELETE DELETE DELETE (getter)");
                holidaysDatabase.deleteEntry(countries.get(i).getCode());
            }
        }
        return holidays;
    }

    private void parseItems(List<Holiday> items, JSONArray jsonArray)
            throws JSONException {

        for (int i = 1; i < jsonArray.length(); i++) {
            JSONObject jsonObjectItem = jsonArray.getJSONObject(i);

            String date = jsonObjectItem.getString("date");
            String localName = jsonObjectItem.getString("localName");
            String name = jsonObjectItem.getString("name");
            String countryCode = jsonObjectItem.getString("countryCode");

            Holiday holiday = new Holiday(date, localName, name, countryCode, "no");
            items.add(holiday);
        }
    }
}