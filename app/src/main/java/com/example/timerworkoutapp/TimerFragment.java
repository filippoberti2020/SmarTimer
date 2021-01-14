package com.example.timerworkoutapp;

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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

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

    private NumberPicker orePicker;
    private NumberPicker minutiPicker;
    private NumberPicker secondniPicker;




    //variabili per timer
    private  int  TEMPO_DI_INIZIO_IN_MILLISECONDI = 60000;
    private TextView TempoText;
    private Button BottoneIniziaPausa;
    private Button BottoneResetta;
    private int millisecondiTotali;

    private AudioManager mAudioManager;

    private Button BottoneImpostaUnMinuto;
    private Button BottoneImpostaUnMinutoE20;
    private Button BottoneImpostaDueMinuti;


    private MediaPlayer sound1;
    private MediaPlayer sound2;
    private RippleBackground rippleBackground=null;
    private android.os.CountDownTimer CountDownTimer;
    private boolean TimerRunning;
    private long TEMPO_RIMASTO_IN_MILLISECONDI = TEMPO_DI_INIZIO_IN_MILLISECONDI=0;

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



        Log.d("debug berti","Millisecondi: \n"+millisecondiTotali);


        BottoneIniziaPausa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (TimerRunning)
                    PausaTimer();
                else
                {
                    if(TEMPO_RIMASTO_IN_MILLISECONDI>0)// se avevo fatto pausa e quindi ci sono ancora secondi
                    {
                        IniziaTimer();
                        Log.d("debug berti","Tempo risdyo in mil: \n"+millisecondiTotali);
                    }else //altrimetni riparto da zero
                    {
                        millisecondiTotali=(orePicker.getValue()*3600000)+(minutiPicker.getValue()*60000)+(secondniPicker.getValue()*1000);
                        ImpostaTempo(millisecondiTotali);
                        Log.d("debug berti","Millisecondi: \n"+millisecondiTotali);
                        IniziaTimer();
                    }






                }

            }
        });


        BottoneResetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResettaTimer();
            }
        });
        AggioraTestoTimer();




        // Inflate the layout for this fragment
        return rootView;
    }

    private void IniziaTimer () {
        if(TEMPO_RIMASTO_IN_MILLISECONDI==0)// se non ho impsotato un timer do un messaggio
        {
            Toast.makeText(getActivity(), "Devi prima impostare un timer!",
                    Toast.LENGTH_LONG).show();
        }else {
            TempoText.setVisibility(View.VISIBLE);
            rippleBackground.startRippleAnimation(); //faccio partire l'animazione waterdrop
            CountDownTimer = new CountDownTimer(TEMPO_RIMASTO_IN_MILLISECONDI, 1000) {
                @Override
                public void onTick(long l) {
                    TEMPO_RIMASTO_IN_MILLISECONDI = l;
                    AggioraTestoTimer();
                }

                @Override
                public void onFinish() {
                    TimerRunning = false;
                    BottoneIniziaPausa.setText("INIZIA");
                    BottoneIniziaPausa.setVisibility(View.INVISIBLE);
                    BottoneResetta.setVisibility(View.VISIBLE);
                    rippleBackground.stopRippleAnimation();
                    Suona();
                }
            }.start();
            TimerRunning = true;
            BottoneIniziaPausa.setText("PAUSA");
            BottoneResetta.setVisibility(View.INVISIBLE);
        }
    }


    private void Suona(){
        sound1.start();
    }
    private void PausaTimer () {
        CountDownTimer.cancel();
        TimerRunning = false;
        rippleBackground.stopRippleAnimation();
        BottoneIniziaPausa.setText("RIPRENDI");
        BottoneResetta.setVisibility(View.VISIBLE);
    }
    private void ResettaTimer () {
        TEMPO_RIMASTO_IN_MILLISECONDI = TEMPO_DI_INIZIO_IN_MILLISECONDI;
        AggioraTestoTimer();
        BottoneResetta.setVisibility(View.INVISIBLE);
        BottoneIniziaPausa.setVisibility(View.VISIBLE);
        BottoneIniziaPausa.setText("INIZIA");
    }
    private void ImpostaTempo(int n){TEMPO_DI_INIZIO_IN_MILLISECONDI=n;
        TEMPO_RIMASTO_IN_MILLISECONDI=n;}

    private void AggioraTestoTimer () {
        int minuti = (int) (TEMPO_RIMASTO_IN_MILLISECONDI / 1000) / 60;
        int secondi = (int) (TEMPO_RIMASTO_IN_MILLISECONDI / 1000) % 60;
        String TempoRimastoFormattato = String.format(Locale.getDefault(), "%02d:%02d", minuti, secondi);
        TempoText.setText(TempoRimastoFormattato);


    }
}