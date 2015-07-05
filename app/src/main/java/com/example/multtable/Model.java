package com.example.multtable;

import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by denny on 7/1/15.
 */
class Model {
    SharedPreferences sharedPref;
    TextToSpeech tts;

    Random rnd = new SecureRandom();
    long tstart = System.currentTimeMillis();
    long tend = tstart;
    boolean stop = false;

    String res;
    String numberTxt;

    Set<String> shown = new HashSet<>();
    int taskNo = 0;
    int okNo = 0;
    boolean hadError;

    int getMax() {
        return Integer.parseInt(sharedPref.getString("pref_max", "11"));
    }

    public Model(SharedPreferences sharedPref, TextToSpeech tts) {
        this.sharedPref = sharedPref;
        this.tts = tts;
        nextTask();
    }

    void nextTask() {
        if( taskNo>=20 ){
            stop = true;
        }else {
            taskNo++;
            hadError=false;
            for (int i = 0; ; i++) {
                int v1 = 2 + rnd.nextInt(getMax() - 1);
                int v2 = 2 + rnd.nextInt(getMax() - 1);
                String key = Math.min(v1, v2) + "x" + Math.max(v1, v2);
                if (shown.add(key) || i > 100) {
                    res = v1 + "x" + v2 + "=" + (v1 * v2);
                    numberTxt = v1 + "x" + v2 + "=";
                    break;
                }
            }
        }
    }

    String statusTxt() {
        long sec = (tend - tstart) / 1000;
        return String.format("%02d:%02d, #%d", sec / 60, sec % 60, taskNo);
    }

    public void nextLetter(char c) {
        tend = System.currentTimeMillis();
        if (res.charAt(numberTxt.length()) == c) {
            numberTxt += c;
            if (numberTxt.equals(res)) {
                if( ! hadError ) {
                    okNo++;
                }
                nextTask();
            }
        } else {
//            String[] ers = {"oops","no","nope","nay","no way","ahem","blah","boo","jeez","my gosh","tut","what?"};
            String[] ers = {"oops","no","nope","nay","no way","ahem","jeez","my gosh"};
            tts.speak(ers[rnd.nextInt(ers.length)], TextToSpeech.QUEUE_FLUSH, null);
            hadError=true;
            //nextTask();
        }
    }
}
