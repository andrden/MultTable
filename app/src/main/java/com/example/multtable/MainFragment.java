package com.example.multtable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by denny on 6/30/15.
 */
public class MainFragment extends Fragment {
    final int MAX = 7; // 12
    int[] digitBtnIds = {R.id.b0, R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6, R.id.b7, R.id.b8, R.id.b9,};

    boolean stop=false;
    Button bstop;
    View rootView;
    ProgressBar progressBar;
    TextView numberTxt;
    String res;
    Random rnd = new SecureRandom();

    Set<String> shown = new HashSet<>();

    void nextTask(){
        if( progressBar.getProgress()>=20 ){
            stop=true;
            bstop.setEnabled(true);
            for( int digitBtnId : digitBtnIds ){
                rootView.findViewById(digitBtnId).setEnabled(false);
            }
            numberTxt.setText("");
        }else {
            progressBar.incrementProgressBy(1);
            for(;;) {
                int v1 = 2 + rnd.nextInt(MAX - 1);
                int v2 = 2 + rnd.nextInt(MAX - 1);
                String key = Math.min(v1, v2) + "x" + Math.max(v1, v2);
                if( shown.add(key) ) {
                    numberTxt.setText(v1 + "x" + v2 + "=");
                    res = v1 + "x" + v2 + "=" + (v1 * v2);
                    break;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        numberTxt = (TextView) rootView.findViewById(R.id.numberTxt);
        bstop = (Button)rootView.findViewById(R.id.bstop);

        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        final ProgressBar progressOk = (ProgressBar)rootView.findViewById(R.id.progressOk);
        progressBar.setMax(20);
        progressOk.setMax(20);

        nextTask();

        for (int i = 0; i <= 9; i++) {
            final Button b = ((Button) rootView.findViewById(digitBtnIds[i]));
            b.setText("" + i);
            b.setTextSize(numberTxt.getTextSize()/3);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( !stop ) {
                        if (res.charAt(numberTxt.getText().length()) == b.getText().charAt(0)) {
                            numberTxt.append(b.getText());
                            if (numberTxt.getText().toString().equals(res)) {
                                progressOk.incrementProgressBy(1);
                                nextTask();
                            }
                        } else {
                            nextTask();
                        }
                    }else{
                        b.setEnabled(false); // can get here?
                    }
                }
            });
        }
        return rootView;
    }
}
