package com.example.timerworkoutapp;


import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

public class TimerService extends Service {
    private static final String TAG = "BertiService";
    private long TEMPO_DI_INIZIO_IN_MILLISECONDI,TEMPO_RIMASTO_IN_MILLISECONDI;
    public static boolean TimerRunning;//variabile che mi dice se il timer sta andando
    // indica se il servizio e’ attivo
    private boolean isRunning  = false;

    private int i;
    private CountDownTimer CountDownTimer;

    // costruttore
    public TimerService() {
    }

    @Override
    public void onCreate() {

    }

    // collegamento con altre APP non necessario
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;

        try {
            // salvo i secondi e minuti selezionati
            int minuti = TimerFragment.getMinutiPicker();
            int secondi = TimerFragment.getSecondiPicker();

            // salvo i secondi e minuti correnti che vengono mostrati
            String string2 = TimerFragment.getcurrentTime();
            String[] parts2 = string2.split(":");
            int minuti2 = Integer.parseInt(parts2[0]);
            int secondi2 = Integer.parseInt(parts2[1]);
            // salvo i secondi totali correnti che vengono mostrati
            int totSecondi2 = secondi2 + minuti2 * 60;

            if (totSecondi2 > 0)//se ho fatto pausa e quindi i secondi correnti mostrati sono diversi da zero imposto il timer a quei secondi rimasti
            {
                TEMPO_RIMASTO_IN_MILLISECONDI = (minuti2 * 60000) + (secondi2 * 1000);
            } else {//altrimenti imposto il timer ai secondi impostati/scelti
                TEMPO_DI_INIZIO_IN_MILLISECONDI = (TimerFragment.getOrePicker() * 3600000) + (TimerFragment.getMinutiPicker() * 60000) + (TimerFragment.getSecondiPicker() * 1000);
                TEMPO_RIMASTO_IN_MILLISECONDI = TEMPO_DI_INIZIO_IN_MILLISECONDI;
            }

            IniziaTimer();// faccio partire il timer
            TimerRunning = true;
        }catch (Exception e){}
        return Service.START_STICKY;
    }

    private void IniziaTimer () {
        if(TEMPO_RIMASTO_IN_MILLISECONDI==0)// se non ho impsotato un timer do un messaggio
        {
                TimerFragment.messaggioInput(getApplicationContext());
        }else {
            TimerFragment.setTxtTempoVisibile();
            CountDownTimer = new CountDownTimer(TEMPO_RIMASTO_IN_MILLISECONDI, 1000) {
                int prog=0;
                @Override
                public void onTick(long l) {
                    TEMPO_RIMASTO_IN_MILLISECONDI = l;
                    AggiornaTempo();
                    TimerFragment.aggiornaProgressBar();
                }

                @Override
                public void onFinish() {
                    TimerRunning = false;
                    TimerFragment.onFinish();
                }
            }.start();
            TimerRunning = true;
            TimerFragment.onInizio();
        }
    }

    private void AggiornaTempo(){
        int minuti = (int) (TEMPO_RIMASTO_IN_MILLISECONDI / 1000) / 60;
        int secondi = (int) (TEMPO_RIMASTO_IN_MILLISECONDI / 1000) % 60;
        String TempoRimastoFormattato = String.format(Locale.getDefault(), "%02d:%02d", minuti, secondi);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() { // the runnable object
            @Override
            public void run() {
                TimerFragment.setText(TempoRimastoFormattato);
            }
        });
    }

    @Override
    public void onDestroy() {
        CountDownTimer.cancel();
        TimerRunning = false;
    }
}
