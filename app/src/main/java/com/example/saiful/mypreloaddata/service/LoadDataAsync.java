package com.example.saiful.mypreloaddata.service;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.example.saiful.mypreloaddata.R;
import com.example.saiful.mypreloaddata.database.MahasiswaHelper;
import com.example.saiful.mypreloaddata.model.MahasiswaModel;
import com.example.saiful.mypreloaddata.pref.AppPreference;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class LoadDataAsync extends AsyncTask<Void, Integer, Boolean> {
    private final String TAG = LoadDataAsync.class.getSimpleName();
    private MahasiswaHelper mahasiswaHelper;
    private AppPreference appPreference;
    private WeakReference<LoadDataCallback> weakCallback;
    private WeakReference<Resources> weakResource;
    double progress;
    double maxProgress = 100;


    LoadDataAsync(MahasiswaHelper mahasiswaHelper, AppPreference preference, LoadDataCallback callback, Resources resources) {
        this.mahasiswaHelper = mahasiswaHelper;
        this.appPreference = preference;
        this.weakCallback = new WeakReference<>(callback);
        this.weakResource = new WeakReference<>(resources);

    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute");
        weakCallback.get().onPreload();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        weakCallback.get().onProgressUpdate(values[0]);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Boolean firstRun = appPreference.getFirstRun();
        if (firstRun) {
            ArrayList<MahasiswaModel> mahasiswaModels = preLoadRaw();

            mahasiswaHelper.open();

            progress = 30;

            publishProgress((int) progress);
            Double progressMaxInsert = 80.0;
            Double progressDiff = (progressMaxInsert - progress) / mahasiswaModels.size();

            boolean isInsertSucces;
            try {
                mahasiswaHelper.beginTransaction();
                for (MahasiswaModel model : mahasiswaModels) {
                    if (isCancelled()) {
                        break;
                    } else {

                        mahasiswaHelper.insert(model);
                        progress += progressDiff;
                        publishProgress((int) progress);
                    }

                }
                if (isCancelled()) {
                    isInsertSucces = false;
                    appPreference.setFirstRun(true);
                    weakCallback.get().onLoadCancel();
                } else {
                    mahasiswaHelper.setTransactionOnSucces();
                    isInsertSucces = true;
                    appPreference.setFirstRun(false);
                }
            } catch (Exception e) {
                Log.e(TAG, "doInBackground : Exception");
                isInsertSucces = false;
            } finally {
                mahasiswaHelper.endTransaction();
            }
            mahasiswaHelper.close();

            publishProgress((int) maxProgress);

            return isInsertSucces;
        } else {
            try {
                synchronized (this) {
                    this.wait(2000);
                    publishProgress(50);

                    this.wait(2000);
                    publishProgress((int) maxProgress);

                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            weakCallback.get().onLoadSucces();
        } else {
            weakCallback.get().onLoadFailed();
        }
    }

    private ArrayList<MahasiswaModel> preLoadRaw() {
        ArrayList<MahasiswaModel> mahasiswaModels = new ArrayList<>();
        String line;
        BufferedReader reader;
        try {
            Resources res = weakResource.get();
            InputStream raw_dict = res.openRawResource(R.raw.data_mahasiswa);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");

                MahasiswaModel mahasiswaModel;

                mahasiswaModel = new MahasiswaModel(splitstr[0], splitstr[1]);
                mahasiswaModels.add(mahasiswaModel);

            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mahasiswaModels;
    }
}
