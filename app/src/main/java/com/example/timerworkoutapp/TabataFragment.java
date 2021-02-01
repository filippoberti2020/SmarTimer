package com.example.timerworkoutapp;

import android.content.Intent;
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

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

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

    private static NumberPicker setsPicker;//selettore round  di lavoro
    private static NumberPicker minutiWorkPicker; //selettore minuti di lavoro
    private static NumberPicker secondiWorkPicker;//selettore secondi di lavoro
    private static NumberPicker minutiRestPicker;//selettore minuti di riposo
    private static NumberPicker secondiRestPicker;//selettore secondi di riposo
    private static Button BottoneIniziaPaus;//bottono inizio pausa

    private long TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA=5000;//tempo di pausa/preparazione prima di partire con l'allenamento
    private static TextView SetsTxt;//text view dove mostro i round
    private static TextView timeTxt;//text view con scritta WORK TIME
    private static TextView showTime;//text view dove mostro il tempo
    private static TextView messageTxt;//text view dove mostro i messaggi per l'utente
    private static TextView ScrittaRoundTxt;//text view con scritta ROUND
    private static TextView ScrittaRoundBassaTxt;//text view in basso a destracon scritta ROUND
    private static MediaPlayer sound1;
    private static MediaPlayer sound2;
    private static MediaPlayer sound;
    private  long aus1,aus2;
    private ConstraintLayout mConstraintLayout;
    private static TextView txtMinuti;//text view con scritta MINUTI
    private static TextView txtSecondi;//text view con scritta SECONDI
    private static TextView restTxt;//text view del messaggio REST TIME
    public static long  TEMPO_DI_INIZIO_IN_MILLISECONDI=0 ;
    public static long TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS=0;
    private static CircularProgressBar circularProgressBar1,circularProgressBar2;// progress bar 1 per il tempoo la due per il contorno delle reps
    public static int reps=0;//variabile dove salvo il numero delle reps/round


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
        txtMinuti=(TextView)rootView.findViewById(R.id.textViewMinutiTabata);
        txtSecondi=(TextView)rootView.findViewById(R.id.textViewMinutiTabata2);
        circularProgressBar1 = rootView.findViewById(R.id.progress_circular);
        circularProgressBar2= rootView.findViewById(R.id.progress_circular2);
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
        ScrittaRoundTxt= rootView.findViewById(R.id.textViewSets);//textview scritta round
        ScrittaRoundBassaTxt= rootView.findViewById(R.id.textViewRoundText);//textview scritta round usata in basso a destra
        SetsTxt= rootView.findViewById(R.id.textViewRound);
        timeTxt= rootView.findViewById(R.id.textViewWorkTime);
        showTime= rootView.findViewById(R.id.textViewShowTempo);
        messageTxt= rootView.findViewById(R.id.textViewMessageGuide);
        restTxt= rootView.findViewById(R.id.textViewRest);
        sound1=MediaPlayer.create(getActivity(),R.raw.suonoprefine);
        sound=MediaPlayer.create(getActivity(),  R.raw.suonoprefine);
        sound2= MediaPlayer.create(getActivity(),  R.raw.suonofine);
        aus1= 0;
        aus2= 0 ;

        BottoneIniziaPaus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                TEMPO_DI_INIZIO_IN_MILLISECONDI=minutiWorkPicker.getValue()*60000+secondiWorkPicker.getValue()*1000; //salvo il tempo di work che vuole fare
                TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS  = minutiRestPicker.getValue()*60000+secondiRestPicker.getValue()*1000;                           //salvo il tempo di riposo che vuole fare
                reps=setsPicker.getValue();   //salvo le reps che vuole fare
                } catch (Exception e) {  }
                if(TEMPO_DI_INIZIO_IN_MILLISECONDI!=0 &&TEMPO_DI_INIZIO_IN_MILLISECONDI_REPS!=0 && reps>0)//se ha impostati i dati corretamente
                {
                    if(messageTxt.getText()=="FINE") //SE SCHIACCIA SU RESETTA
                    {
                        TEMPO_RIMASTO_IN_MILLISECONDI_PAUSA=5000;
                        SetsTxt.setText("ROUND");
                        messageTxt.setText("FINE");
                        BottoneIniziaPaus.setText("INIZIA");
                        txtMinuti.setVisibility(View.VISIBLE);
                        txtSecondi.setVisibility(View.VISIBLE);
                        minutiWorkPicker.setVisibility(View.VISIBLE);
                        secondiWorkPicker.setVisibility(View.VISIBLE);
                        minutiRestPicker.setVisibility(View.VISIBLE);
                        secondiRestPicker.setVisibility(View.VISIBLE);
                       setsPicker.setVisibility(View.VISIBLE);
                        timeTxt.setText("WORK TIME");
                        messageTxt.setText("REST TIME");
                    }
                    else//altrimenti devo fare partire il timer
                        getActivity().startService(new Intent(getActivity(), TabataService.class));
                }//se i dati non sono validi lo faccio notare all'utente
                else
                    Toast.makeText(getActivity(), "Dati non validi!",
                            Toast.LENGTH_LONG).show();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    protected static void AggiornaTestoWork(String n){//aggiorno il tempo corrente
        showTime.setTextColor(Color.YELLOW);
        showTime.setText(n);
    }
    protected static void AggiornaTestoRest(String n){//aggiorno il tempo corrente
        showTime.setTextColor(Color.BLUE);
        showTime.setText(n);
    }
    public static void Suona(){
        sound1.start();
    }//riproduco il suono
    public static void Suona2(){
        sound.start();
    }//riproduco il suono
    public static void SuonaFine(){
        sound2.start();
    }//riproduco il suono
    protected static void AggiornaReps(String a){//aggiorno il numero di round/reps mancanti
        SetsTxt.setText(a);
    }
    protected static void MessaggioWork(){//messaggio di info all'utente
        messageTxt.setText("WORK");
    }
    protected static void MessaggioPausa(){//messaggio di info all'utente
        messageTxt.setText("PREPARATI");
    }
    protected static void MessaggioRest(){//messaggio di info all'utente
        messageTxt.setText("RECUPERO");
    }
    protected static void Fine(){//quando finisce il timer
        SetsTxt.setText("0");
        messageTxt.setText("ALLENAMENTO FINITO");
        BottoneIniziaPaus.setText("RESETTA");
        BottoneIniziaPaus.setVisibility(View.VISIBLE);
        circularProgressBar1.setVisibility(View.INVISIBLE);
        circularProgressBar2.setVisibility(View.INVISIBLE);
        ScrittaRoundBassaTxt.setVisibility(View.INVISIBLE);
        SetsTxt.setVisibility(View.INVISIBLE);
    }

    //--------------------------------------------------------
    public static void AggiornaTestoPausa2(String n){//mostro il tempo di pre-inizio rimasto

        showTime.setTextColor(Color.WHITE);
        showTime.setText(n);
    }
    public static int getMinutiWorkPicker(){//ritorno il numero di minuti di lavoro selezionati

        return minutiWorkPicker.getValue();
    }
    public static int getSecondiWorkPicker(){//ritorno il numero di secondi di lavoro selezionati

        return secondiWorkPicker.getValue();
    }
    public static int getMinutiRestPicker(){//ritorno il numero di minuti di riposo selezionati

        return minutiRestPicker.getValue();
    }
    public static int getSecondiRestPicker(){//ritorno il numero di secondi di riposo selezionati

        return secondiRestPicker.getValue();
    }
    public static int getReps(){//ritorno il numero di round/reps selezionati

        return setsPicker.getValue();
    }
    public static String getMessageTxt(){//ritorno il messaggio di info che ce al momento

        return messageTxt.getText()+"";
    }
    public static void setAll2(){//quando viene premuto il pulsante resetta
        SetsTxt.setVisibility(View.INVISIBLE);
        messageTxt.setText("FINE");
        BottoneIniziaPaus.setText("INIZIA");
        txtMinuti.setVisibility(View.VISIBLE);
        txtSecondi.setVisibility(View.VISIBLE);
        minutiWorkPicker.setVisibility(View.VISIBLE);
        secondiWorkPicker.setVisibility(View.VISIBLE);
        minutiRestPicker.setVisibility(View.VISIBLE);
        secondiRestPicker.setVisibility(View.VISIBLE);
        setsPicker.setVisibility(View.VISIBLE);
        restTxt.setVisibility(View.VISIBLE);
        ScrittaRoundTxt.setVisibility(View.VISIBLE);
        showTime.setVisibility(View.INVISIBLE);
        messageTxt.setVisibility(View.INVISIBLE);
        timeTxt.setText("WORK TIME");
        messageTxt.setText("REST TIME");
        circularProgressBar2.setVisibility(View.INVISIBLE);
        ScrittaRoundBassaTxt.setVisibility(View.INVISIBLE);
    }
    public static void setAll(){//funzione che chiamo all'inizio del timer
        timeTxt.setVisibility(View.INVISIBLE);
        setsPicker.setVisibility(View.INVISIBLE);
        minutiWorkPicker.setVisibility(View.INVISIBLE);
        secondiWorkPicker.setVisibility(View.INVISIBLE);
        minutiRestPicker.setVisibility(View.INVISIBLE);
        secondiRestPicker.setVisibility(View.INVISIBLE);
        BottoneIniziaPaus.setVisibility(View.INVISIBLE);
        txtMinuti.setVisibility(View.INVISIBLE);
        txtSecondi.setVisibility(View.INVISIBLE);
        showTime.setVisibility(View.VISIBLE);
        restTxt.setVisibility(View.INVISIBLE);
        ScrittaRoundTxt.setVisibility(View.INVISIBLE);
        circularProgressBar2.setVisibility(View.VISIBLE);
        ScrittaRoundBassaTxt.setVisibility(View.VISIBLE);
        SetsTxt.setVisibility(View.VISIBLE);

    }
    public static void setInizioProgressBar(long millisecondiTotali){//preparo e mostro la progress bar
        circularProgressBar1.setProgressMax(millisecondiTotali/1000);
        circularProgressBar1.setVisibility(View.VISIBLE);
    }
    public static void aggiornaProgressBarPreInizio(int n){//mostro il progresso della progress bar
        circularProgressBar1.setProgressBarColor(Color.WHITE);
        long durata=1000; //durata animazione progress bar
        circularProgressBar1.setProgressWithAnimation(n, durata);
    }
    public static void aggiornaProgressBarWork(int n){//mostro il progresso della progress bar
        circularProgressBar1.setProgressBarColor(Color.YELLOW);
        long durata=1000; //durata animazione progress bar
        circularProgressBar1.setProgressWithAnimation(n, durata);
    }
    public static void aggiornaProgressBarRest(int n){//mostro il progresso della progress bar
        circularProgressBar1.setProgressBarColor(Color.BLUE);
        long durata=1000; //durata animazione progress bar
        circularProgressBar1.setProgressWithAnimation(n, durata);
    }
    public static void azzeraProgressBar(){//reset della progress bar
        long durata=0; //durata animazione progress bar
        circularProgressBar1.setProgressWithAnimation(0, durata);
    }

}