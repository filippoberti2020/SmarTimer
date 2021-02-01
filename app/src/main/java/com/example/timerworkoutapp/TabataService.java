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
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

public class TabataService extends Service {
    private  long aus1,aus2;//varibili che utilizza per vedere se manca un secondo o due
    private long TEMPO_DI_INIZIO_IN_MILLISECONDI,TEMPO_RIMASTO_IN_MILLISECONDI;
    public static long TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS=0;
    public static boolean TimerRunning;//indica se il timer sta andando
    private boolean pausa=false;
    public static int reps=0;//indica i round
    private long TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA=5000;//secondi che lascio prima di far partire il tutto
    int prog=0;//variabile che uso per impostare il progresso della progress bar

    private boolean isRunning  = false;// indica se il servizio e’ attivo
    private CountDownTimer CountDownTimer;

    public TabataService() {
    }

    @Override
    public void onCreate() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;


        try {
            TEMPO_DI_INIZIO_IN_MILLISECONDI=TabataFragment.getMinutiWorkPicker()*60000+TabataFragment.getSecondiWorkPicker()*1000; //salvo il tempo di work che vuole fare
            TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS  = TabataFragment.getMinutiRestPicker()*60000+TabataFragment.getSecondiRestPicker()*1000;                           //salvo il tempo di riposo che vuole fare
            reps=TabataFragment.getReps();   //salvo le reps che vuole fare

        } catch (Exception e) {  }
        if(TEMPO_DI_INIZIO_IN_MILLISECONDI!=0 &&TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS!=0 && reps>0)//se i dati sono validi
        {
            if(TabataFragment.getMessageTxt().equals("ALLENAMENTO FINITO")) //SE SCHIACCIA SU RESETTA
            {
                TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA=5000; //rimposto i secondi per preparasi prima di iniziare il timer
                TabataFragment.setAll2(); // respetto tutte le cose
            }
            else
                   IniziaTimer();
        }

        else{
            Toast.makeText(this, "Dati non validi!",
                    Toast.LENGTH_LONG).show();

    }

        return Service.START_STICKY;
    }

    private void IniziaTimer () {
      TabataFragment.setAll();
        TabataFragment.AggiornaReps(reps+"");
        TabataFragment.MessaggioPausa();
        TabataFragment.setInizioProgressBar(TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA);
        prog=0;// rimposto il progresso di partenza della progress bar a zero
        CountDownTimer CountDownTimer = new CountDownTimer(TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA, 1000) {
            @Override
            public void onTick(long l) {
                TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA = l;
                prog=prog+1;
                TabataFragment.aggiornaProgressBarPreInizio(prog);
                int minuti = (int) (TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA / 1000) / 60;
                int secondi = (int) (TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA / 1000) % 60;
                String TempoRimastoFormattato = String.format(Locale.getDefault(), "%02d:%02d", minuti, secondi);
                TabataFragment.AggiornaTestoPausa2(TempoRimastoFormattato);
                if(TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA/1000==2)
                    TabataFragment.Suona();
                if(TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA/1000==1)
                    TabataFragment.Suona2();
            }

            @Override
            public void onFinish() {

                TabataFragment.SuonaFine();
                TabataFragment.azzeraProgressBar();
                Main();
            }
        }.start();
    }

    private void Main(){
        Timer1();
    }
    private void Timer1(){
        TabataFragment.MessaggioWork();
        TabataFragment.AggiornaReps(reps+"");
        TabataFragment.setInizioProgressBar(TEMPO_DI_INIZIO_IN_MILLISECONDI);
        prog=0;// rimposto il progresso di partenza della progress bar a zero
        CountDownTimer CountDownTimer = new CountDownTimer(TEMPO_DI_INIZIO_IN_MILLISECONDI, 1000) {
            @Override
            public void onTick(long l) {
                aus1 = l;
                prog=prog+1;
                TabataFragment.aggiornaProgressBarWork(prog);
                int minuti = (int) (aus1/ 1000) / 60;
                int secondi = (int) (aus1/ 1000) % 60;
                String TempoRimastoFormattato = String.format(Locale.getDefault(), "%02d:%02d", minuti, secondi);
                TabataFragment.AggiornaTestoWork(TempoRimastoFormattato);
                if (aus1 / 1000 == 2)
                    TabataFragment.Suona();
                if (aus1 / 1000 == 1)
                    TabataFragment.Suona2();
            }

            @Override
            public void onFinish() {

                TabataFragment.SuonaFine();
                TabataFragment.azzeraProgressBar();
                Timer2();
            }
        }.start();
    }

    private void Timer2(){

        if(reps!=1) //se ci sono ancora round
        {
            TabataFragment.MessaggioRest();
            TabataFragment.setInizioProgressBar(TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS);
            prog=0;// rimposto il progresso di partenza della progress bar a zero
            CountDownTimer CountDownTimer = new CountDownTimer(TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS, 1000) {
                @Override
                public void onTick(long l) {
                    aus2 = l;
                    prog=prog+1;
                    TabataFragment.aggiornaProgressBarRest(prog);
                    int minuti = (int) (aus2 / 1000) / 60;
                    int secondi = (int) (aus2 / 1000) % 60;
                    String TempoRimastoFormattato = String.format(Locale.getDefault(), "%02d:%02d", minuti, secondi);
                    TabataFragment.AggiornaTestoRest(TempoRimastoFormattato);
                    if (aus2 / 1000 == 2)
                        TabataFragment.Suona();
                    if (aus2 / 1000 == 1)
                        TabataFragment.Suona2();
                }

                @Override
                public void onFinish() {

                    TabataFragment.SuonaFine();
                    aus1 = TEMPO_DI_INIZIO_IN_MILLISECONDI;
                    aus2 = TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS;
                    reps--;
                    TabataFragment.azzeraProgressBar();
                    if (reps != 0)//se ci sono ancora round faccio ricominciare
                        Main();
                }
            }.start();
        }
        else
            TabataFragment.Fine();
    }


    public static boolean siRunning(){
        return TimerRunning;
    }

    @Override
    public void onDestroy() {
        CountDownTimer.cancel();
        pausa=true;
        TimerRunning = false;
    }
}
