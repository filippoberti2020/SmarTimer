package com.example.timerworkoutapp;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabataFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  View rootView;

    private NumberPicker setsPicker;
    private NumberPicker minutiWorkPicker;
    private NumberPicker secondiWorkPicker;
    private NumberPicker minutiRestPicker;
    private NumberPicker secondiRestPicker;
    private Button BottoneIniziaPaus;

    private long millisecondiReps;
    private long TEMPO_RIMASTO_IN_MILLISECONDI_REPS;
    private long TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA=5000;
    private TextView SetsTxt;
    private TextView timeTxt;
    private TextView messageTxt;
    protected Button Exit;
    private MediaPlayer sound1,sound2,sound;
    private  long aus1,aus2;
    private ConstraintLayout mConstraintLayout;


    public static long  TEMPO_DI_INIZIO_IN_MILLISECONDI=0 ;
    public static long TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS=0;
    private long millisecondiReps2;
    private long TEMPO_RIMASTO_IN_MILLISECONDI_REPS2;

    public static int reps=0;


    public TabataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabataFragment newInstance(String param1, String param2) {
        TabataFragment fragment = new TabataFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         rootView = inflater.inflate(R.layout.fragment_tabata, container, false);
        mConstraintLayout = (ConstraintLayout)rootView.findViewById(R.id.gang);
        setsPicker=(NumberPicker)rootView.findViewById(R.id.SetsPicker);
        minutiWorkPicker=(NumberPicker)rootView.findViewById(R.id.MinutiWorkPicker);
        secondiWorkPicker=(NumberPicker)rootView.findViewById(R.id.SecondiWorkPicker);
        minutiRestPicker=(NumberPicker)rootView.findViewById(R.id.MinutiRestPicker);
        secondiRestPicker=(NumberPicker)rootView.findViewById(R.id.SecondiRestPicker);
        BottoneIniziaPaus=(Button)rootView.findViewById(R.id.buttonIniziaTabata);

        setsPicker.setMaxValue(30);
        setsPicker.setMinValue(0);
        setsPicker.setValue(0);

        minutiWorkPicker.setMaxValue(60);
        minutiWorkPicker.setMinValue(0);
        minutiWorkPicker.setValue(0);

        secondiWorkPicker.setMaxValue(60);
        secondiWorkPicker.setMinValue(0);
        secondiWorkPicker.setValue(0);

        minutiRestPicker.setMaxValue(60);
        minutiRestPicker.setMinValue(0);
        minutiRestPicker.setValue(0);

        secondiRestPicker.setMaxValue(60);
        secondiRestPicker.setMinValue(0);
        secondiRestPicker.setValue(0);

        SetsTxt= rootView.findViewById(R.id.textViewSets);
        timeTxt= rootView.findViewById(R.id.textViewWorkTime);
        messageTxt= rootView.findViewById(R.id.textViewRest);
        sound1=MediaPlayer.create(getActivity(),R.raw.suonoprefine);
        sound=MediaPlayer.create(getActivity(),  R.raw.suonoprefine);
        sound2= MediaPlayer.create(getActivity(),  R.raw.suonofine);
        aus1= 0;
        aus2= 0 ;

        BottoneIniziaPaus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                TEMPO_DI_INIZIO_IN_MILLISECONDI=minutiWorkPicker.getValue()*60000+secondiWorkPicker.getValue()*1000; //salvo il tempo di work che vuole fare
                TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS  = minutiRestPicker.getValue()*60000+secondiRestPicker.getValue()*1000;                           //salvo il tempo di riposo che vuole fare
                reps=setsPicker.getValue();   //salvo le reps che vuole fare

                } catch (Exception e) {  }
                if(TEMPO_DI_INIZIO_IN_MILLISECONDI!=0 &&TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS!=0 && reps>0)
                {
                    IniziaTimer();
                }

                else
                    Toast.makeText(getActivity(), "Dati non validi!",
                            Toast.LENGTH_LONG).show();

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }
    private void IniziaTimer () {
        setsPicker.setVisibility(View.INVISIBLE);
        minutiWorkPicker.setVisibility(View.INVISIBLE);
        secondiWorkPicker.setVisibility(View.INVISIBLE);
        minutiRestPicker.setVisibility(View.INVISIBLE);
        secondiRestPicker.setVisibility(View.INVISIBLE);
        AggiornaReps();
        MessaggioPausa();
        SetOrangeBackground();
        CountDownTimer CountDownTimer = new CountDownTimer(TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA, 1000) {
            @Override
            public void onTick(long l) {
                TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA = l;
                AggiornaTestoPausa();
                if(TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA/1000==2)
                    Suona();
                if(TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA/1000==1)
                    Suona2();
            }

            @Override
            public void onFinish() {

                SuonaFine();
                Main();
            }
        }.start();

    }
    private void Main(){
        Timer1();
    }
    private void Timer1(){
        SetGreenBackground();
        MessaggioWork();
        AggiornaReps();
        CountDownTimer CountDownTimer = new CountDownTimer(TEMPO_DI_INIZIO_IN_MILLISECONDI, 1000) {
            @Override
            public void onTick(long l) {
                aus1 = l;
                AggiornaTestoWork();
                if (aus1 / 1000 == 2)
                    Suona();
                if (aus1 / 1000 == 1)
                    Suona2();
            }

            @Override
            public void onFinish() {

                SuonaFine();
                Timer2();
            }
        }.start();
    }

    private void Timer2(){

        if(reps!=1) {
            SetRedBackground();
            MessaggioRest();
            CountDownTimer CountDownTimer = new CountDownTimer(TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS, 1000) {
                @Override
                public void onTick(long l) {
                    aus2 = l;
                    AggiornaTestoRest();
                    if (aus2 / 1000 == 2)
                        Suona();
                    if (aus2 / 1000 == 1)
                        Suona2();
                }

                @Override
                public void onFinish() {

                    SuonaFine();
                    aus1 = TEMPO_DI_INIZIO_IN_MILLISECONDI;
                    aus2 = TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS;
                    reps--;
                    if (reps != 0)
                        Main();
                    else
                        SetDefaultBackground();
                }
            }.start();
        }
        else
            Fine();
    }

    private void AggiornaTestoWork(){
        int minuti = (int) (aus1/ 1000) / 60;
        int secondi = (int) (aus1/ 1000) % 60;
        String TempoRimastoFormattato = String.format(Locale.getDefault(), "%02d:%02d", minuti, secondi);
        timeTxt.setText(TempoRimastoFormattato);
    }
    private void AggiornaTestoRest(){
        int minuti = (int) (aus2 / 1000) / 60;
        int secondi = (int) (aus2 / 1000) % 60;
        String TempoRimastoFormattato = String.format(Locale.getDefault(), "%02d:%02d", minuti, secondi);
        timeTxt.setText(TempoRimastoFormattato);
    }
    private void AggiornaTestoPausa(){
        int minuti = (int) (TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA / 1000) / 60;
        int secondi = (int) (TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA / 1000) % 60;
        String TempoRimastoFormattato = String.format(Locale.getDefault(), "%02d:%02d", minuti, secondi);
        timeTxt.setText(TempoRimastoFormattato);
    }
    private void Suona(){
        sound1.start();
    }
    private void Suona2(){
        sound.start();
    }
    private void SuonaFine(){
        sound2.start();
    }
    private void AggiornaReps(){
        String a = Integer.toString(reps);
        SetsTxt.setText(a);
    }
    private void SetDefaultBackground(){
        mConstraintLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tabas));
    }
    private void SetOrangeBackground(){
        mConstraintLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.arancione));
        MessaggioPausa();
    }
    private void SetGreenBackground(){
        mConstraintLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
    }
    private void SetRedBackground(){
        mConstraintLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.rosso));
    }
    private void SetFinishBackground(){
        mConstraintLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.azzurro));
    }



    private void MessaggioWork(){
        messageTxt.setText("WORK");
    }
    private void MessaggioPausa(){
        messageTxt.setText("PREPARATI");
    }
    private void MessaggioRest(){
        messageTxt.setText("RECUPERO");
    }
    private void Fine(){
        SetsTxt.setText("0");
        SetFinishBackground();// cambio sfondo per far vedere che ha finito
        messageTxt.setText("FINE");
        BottoneIniziaPaus.setText("RESETTA");
    }

    private void BacktoHome(){
    //    Intent intent =new Intent(this,Activity2.class);
  //      startActivity(intent);
    }


/*
    private void AggiornaReps(){RepsText.setText(reps+"");}
    private void Home () {
        int minuti = 0;
        int secondi =0;
        int minuti2 = 0;
        int secondi2 =0;
        String TempoRimastoFormattato = String.format(Locale.getDefault(), "%02d:%02d", minuti, secondi);
        String TempoRimastoFormattato2 = String.format(Locale.getDefault(), "%02d:%02d", minuti2, secondi2);
        TempoWorkText.setText(TempoRimastoFormattato);
        TempoRestText.setText(TempoRimastoFormattato2);
    }
    private void ResettaTimer () {
        int minuti = 0;
        int secondi =0;
        int minuti2 = 0;
        int secondi2 =0;
        String TempoRimastoFormattato = String.format(Locale.getDefault(), "%02d:%02d", minuti, secondi);
        String TempoRimastoFormattato2 = String.format(Locale.getDefault(), "%02d:%02d", minuti2, secondi2);
        TempoWorkText.setText(TempoRimastoFormattato);
        TempoRestText.setText(TempoRimastoFormattato2);
    }*/
}