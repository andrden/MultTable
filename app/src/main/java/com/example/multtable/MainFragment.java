package com.example.multtable;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by denny on 6/30/15.
 */
public class MainFragment extends Fragment {

    //final int MAX = 8; // 12
    int[] digitBtnIds = {R.id.b0, R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6, R.id.b7, R.id.b8, R.id.b9,};

    Button bstop;
    View rootView;
    ProgressBar progressBar;
    ProgressBar progressOk;
    TextView numberTxt;
    TextView statusTxt;


    Model model;

    void updateView(){
        if( model.stop ){
            bstop.setEnabled(true);
            for( int digitBtnId : digitBtnIds ){
                rootView.findViewById(digitBtnId).setEnabled(false);
            }
            numberTxt.setText("The END");
        }else {
            numberTxt.setText(model.numberTxt);
            statusTxt.setText(model.statusTxt());
        }
        progressBar.setProgress(model.taskNo);
        progressOk.setProgress(model.okNo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        if( model==null ){
            model = new Model(PreferenceManager.getDefaultSharedPreferences(getActivity()));
            progressBar.incrementProgressBy(1);
        }

        numberTxt = (TextView) rootView.findViewById(R.id.numberTxt);
        statusTxt = (TextView) rootView.findViewById(R.id.statusTxt);
        bstop = (Button)rootView.findViewById(R.id.bstop);

        progressOk = (ProgressBar)rootView.findViewById(R.id.progressOk);
        progressBar.setMax(20);
        progressOk.setMax(20);

        updateView();

        for (int i = 0; i <= 9; i++) {
            final Button b = ((Button) rootView.findViewById(digitBtnIds[i]));
            b.setText("" + i);
            b.setTextSize(numberTxt.getTextSize() / 3);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.nextLetter(b.getText().charAt(0));
                    updateView();
                }
            });
        }

        return rootView;
    }
}
