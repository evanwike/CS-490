package com.vijaya.speechtotext;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView mVoiceInputTv, mNameView, mHintView;
    private ImageButton mSpeakBtn;
    TextToSpeech mTts;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVoiceInputTv = (TextView) findViewById(R.id.voiceInput);
        mNameView = (TextView) findViewById(R.id.nameView);
        mHintView = (TextView) findViewById(R.id.hintView);
        mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
        mTts = new TextToSpeech(this, this);
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, "Activity not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mVoiceInputTv.setText(result.get(0));
                respond(result.get(0));
            }
        }
    }

    @Override
    public void onInit(int status) {
        switch (status) {
            case TextToSpeech.SUCCESS: {
                int result = mTts.setLanguage(Locale.US);

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    Toast.makeText(this, "English (US) not supported", Toast.LENGTH_SHORT).show();
                else
                    mTts.speak("Hello", TextToSpeech.QUEUE_ADD, null);
                break;
            }
            case TextToSpeech.ERROR: {
                Toast.makeText(this, "TTS initialization failed", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void respond(String input) {
        mHintView.setText("Tap on mic to start");

        if (input.equals("hello")) {
            mTts.speak("What's your name?", TextToSpeech.QUEUE_FLUSH, null);
            mHintView.setText("Tap the mic and say \"My name is <name>\"");
        } else if (input.contains("my name is") || input.contains("my name's")) {
            setName(input.split(" ")[3]);
        } else if (input.contains("not feeling good")) {
            mTts.speak("I understand, what are your symptoms?", TextToSpeech.QUEUE_FLUSH, null);
        } else if (input.contains("thank you") || input.contains("thanks")) {
            mTts.speak(String.format("No problem, %s! Take care.", preferences.getString("name", "user")),
                    TextToSpeech.QUEUE_FLUSH,
                    null);
        } else if (input.contains("medicines")) {
            mTts.speak("I think you might have a fever... you should probably take some Aspirin.", TextToSpeech.QUEUE_FLUSH, null);
        } else if (input.contains("time")) {
            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY) == 0 ? 12 : now.get(Calendar.HOUR_OF_DAY);
            String min = now.get(Calendar.MINUTE) == 0 ? "o'clock" : String.format(Locale.US,"%d", now.get(Calendar.MINUTE));
            String half = "AM";
            if (hour > 11 && hour < 23) {
                hour = hour - 12;
                half = "PM";
            } else if (hour == 0) {
                hour = 12;
                half = "AM";
            }
            mTts.speak(String.format(Locale.US,"It's %d:%s %s", hour, min, half), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void setName(String name) {
        preferences = getSharedPreferences("prefs",0);
        editor = preferences.edit();
        editor.putString("name", name).apply();

        mNameView.setText(String.format("Hello, %s!", name));
        mNameView.setVisibility(View.VISIBLE);

        mTts.speak(String.format("Hello, %s!", name), TextToSpeech.QUEUE_ADD, null);
    }
}