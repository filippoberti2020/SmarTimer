package com.example.timerworkoutapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.skyfishjy.library.RippleBackground;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static NumberPicker orePicker; //selettore ore
    private static NumberPicker minutiPicker;//selettore minuti
    private static NumberPicker secondniPicker;//selettore secondi
    private static TextView txtviewOre;//testo scritta ORE
    private static TextView txtviewMinuti;//testo scritta MINUTI
    private static TextView txtviewSecondi;//testo scritta SECONDI

    //variabili per timer
    private  int  TEMPO_DI_INIZIO_IN_MILLISECONDI = 60000;
    private static TextView TempoText; //text view dove mostro il tempo che scorre
    private static Button BottoneIniziaPausa; //bottone di inizio/pausa
    private static Button BottoneResetta;//bottone resetta
    private int millisecondiTotali;

    private AudioManager mAudioManager; //varabile per gestire gli audio


    private static int  prog=0;//progresso progress bar
    private static MediaPlayer sound1; //suono fine
    private MediaPlayer sound2;//secondo suono fine
    private static RippleBackground rippleBackground=null; //offetto acqua
    private android.os.CountDownTimer CountDownTimer; //timer

    private long TEMPO_RIMASTO_IN_MILLISECONDI = TEMPO_DI_INIZIO_IN_MILLISECONDI=0;
    private static CircularProgressBar circularProgressBar; //progress bar
    private static long  durata=1000; //durata animazione progress bar

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance(String param1, String param2) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }
    @Override
    public View onCreateView(LayoutInflater inflater,@NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_timer, container, false);
        //-------------------------------------------------------------------//

        TempoText = rootView.findViewById(R.id.textViewTimer);
        BottoneIniziaPausa = rootView.findViewById(R.id.buttonInizia);
        BottoneResetta = rootView.findViewById(R.id.buttonReset);
        rippleBackground=(RippleBackground)rootView.findViewById(R.id.content);//cerchio animazione acqua

        sound1= MediaPlayer.create(getActivity(),  R.raw.zapsplat_bell_service_disk_ring_slightly_broken_resonate_multiple_18041);

        circularProgressBar = rootView.findViewById(R.id.progress_circular);

        txtviewOre = rootView.findViewById(R.id.textViewOre);
        txtviewMinuti = rootView.findViewById(R.id.textViewMinuti);
        txtviewSecondi= rootView.findViewById(R.id.textViewSecondi);
        //-------------------------------------------------------------------//

        orePicker=(NumberPicker)rootView.findViewById(R.id.OrePicker);
        minutiPicker=(NumberPicker)rootView.findViewById(R.id.MinutiPicker);
        secondniPicker=(NumberPicker)rootView.findViewById(R.id.SecondiPicker);

       //ore
        orePicker.setMaxValue(23);
        orePicker.setMinValue(0);
        orePicker.setValue(0);

        //minuti
        minutiPicker.setMaxValue(60);
        minutiPicker.setMinValue(0);
        minutiPicker.setValue(0);

        //secondi
        secondniPicker.setMaxValue(60);
        secondniPicker.setMinValue(0);
        secondniPicker.setValue(0);

        BottoneIniziaPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              try {//se è la prima volta che schiaccia avvio il servizio tramite exception perche il timer non e ancora stato inizializato
                    if (TimerService.TimerRunning == true)//se sta andando il timer devo fermarlo
                    {
                        getActivity().stopService(new Intent(getActivity(), TimerService.class));
                         PausaTimer();
                    }
                    else {
                        getActivity().startService(new Intent(getActivity(), TimerService.class));
                    }
              }catch (Exception e){getActivity().startService(new Intent(getActivity(), TimerService.class));}

            }
        });


        BottoneResetta.setOnClickListener(new View.OnClickListener() { //resetto tutto
            @Override
            public void onClick(View view) {
                ResettaTimer();
            }
        });
        AggioraTestoTimer();

        // Inflate the layout for this fragment
        return rootView;
    }

    private static void Suona(){
        sound1.start();
    } //riproduco il suono
    private void PausaTimer () { //fermo quello che stava andando
        rippleBackground.stopRippleAnimation();
        BottoneIniziaPausa.setText("RIPRENDI");
        BottoneResetta.setVisibility(View.VISIBLE);
    }
    private void ResettaTimer () { //resetto tutto come da schermata iniziale
        resetProgressBar();
        TEMPO_RIMASTO_IN_MILLISECONDI = TEMPO_DI_INIZIO_IN_MILLISECONDI=0;
        rippleBackground.stopRippleAnimation();
        AggioraTestoTimer();
        prog=0;
        BottoneResetta.setVisibility(View.INVISIBLE);
        TempoText.setVisibility(View.INVISIBLE);
        BottoneIniziaPausa.setVisibility(View.VISIBLE);
        BottoneIniziaPausa.setText("INIZIA");
        circularProgressBar.setVisibility(View.INVISIBLE);
        secondniPicker.setVisibility(View.VISIBLE);
        minutiPicker.setVisibility(View.VISIBLE);
        orePicker.setVisibility(View.VISIBLE);
        VisualizzaIntestazione();
    }

    private void AggioraTestoTimer () {
        int minuti = (int) (TEMPO_RIMASTO_IN_MILLISECONDI / 1000) / 60;
        int secondi = (int) (TEMPO_RIMASTO_IN_MILLISECONDI / 1000) % 60;
        String TempoRimastoFormattato = String.format(Locale.getDefault(), "%02d:%02d", minuti, secondi);
        TempoText.setText(TempoRimastoFormattato);
    }

    public void VisualizzaIntestazione(){ //riemto in vista la scritta ORE MINUTI E SECONDI
        txtviewOre.setVisibility(View.VISIBLE);
        txtviewMinuti.setVisibility(View.VISIBLE);
        txtviewSecondi.setVisibility(View.VISIBLE);
    }

    //--------------------------------------------------
    public static String getcurrentTime()//ritorno il tempo corrente
    {
        return TempoText.getText()+"";
    }
    public static void setTxtTempoVisibile(){ //imposto tutto per mostrare solo la progressbar e il tempo
        int millisecondiTotali=(orePicker.getValue()*3600000)+(minutiPicker.getValue()*60000)+(secondniPicker.getValue()*1000);
        TempoText.setVisibility(View.VISIBLE);
        circularProgressBar.setProgressMax(millisecondiTotali/1000);
        circularProgressBar.setVisibility(View.VISIBLE);
        secondniPicker.setVisibility(View.INVISIBLE);
        minutiPicker.setVisibility(View.INVISIBLE);
        orePicker.setVisibility(View.INVISIBLE);
        txtviewOre.setVisibility(View.INVISIBLE);
        txtviewMinuti.setVisibility(View.INVISIBLE);
        txtviewSecondi.setVisibility(View.INVISIBLE);
    }
    public static void onFinish(){// quando finisce il timer faccio riporodurre il suono e faccio partire l'animazione
        resetProgressBar();
        prog=0;//rimposto il punto di partenza della progress bar
        BottoneIniziaPausa.setText("INIZIA");
        BottoneIniziaPausa.setVisibility(View.INVISIBLE);
        BottoneResetta.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.INVISIBLE);
        rippleBackground.startRippleAnimation(); //faccio partire l'animazione waterdrop
        Suona();
    }
    public static void onInizio(){//se schiaccia su inizio metto il pulsante su pausa e nascondo il pulsante resetta
        BottoneIniziaPausa.setText("PAUSA");
        BottoneResetta.setVisibility(View.INVISIBLE);
    }
    public static void setText(String a){
        TempoText.setText(a);
    }//per impostare il tempo corrente
    public static int getOrePicker(){//per prendere le ore impostate
       return orePicker.getValue();
    }
    public static int getMinutiPicker(){//per prendere i minuti impostati
        return minutiPicker.getValue();
    }
    public static int getSecondiPicker(){//per prendere i secondi impostati
        return secondniPicker.getValue();
    }
    public static void messaggioInput(Context context){//messagio per input non valido
        Toast.makeText(context, "Devi prima impostare un timer!",
                Toast.LENGTH_LONG).show();
    }
    public static void aggiornaProgressBar(){//aggiorno il progresso della progress bar
        prog=prog+1;
        circularProgressBar.setProgressWithAnimation(prog, durata);
    }
    public static void resetProgressBar(){//resetto la progress bar a default
       long aus=0;
        circularProgressBar.setProgressWithAnimation(0, aus);
    }
}