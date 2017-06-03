package com.lib.lapp.location;

import android.os.AsyncTask;

import com.lib.lapp.views.activity.LocationService;

/**
 * @author wxx
 * @Date 2017/5/31 21:09
 * @Description
 */

public class Klocation {

    public void start() {
        new LocalizationAlgorithmTask().execute();
    }

    private class LocalizationAlgorithmTask extends
            AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            LocationService.locationService.uiUpdate();
        }
    }
}
