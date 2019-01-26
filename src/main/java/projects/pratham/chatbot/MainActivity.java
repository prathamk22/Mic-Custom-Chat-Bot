package projects.pratham.chatbot;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.hanks.htextview.scale.ScaleTextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static String QUERY = null;
    public static String SESSION_ID = "bee67580-d05c-47f6-8d64-a6218c3913e1";
    public static String DATA = null;

    public static String NAME = "";

    public static ScaleTextView data;
    public static ScaleTextView resolved;
    SpeechRecognizer speechRecognizer;
    public static RecognitionProgressView recognitionProgressView;
    Intent speechIntent;
    public static RelativeLayout relativeLayout;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing Views from Activity Main
        recognitionProgressView = (RecognitionProgressView) findViewById (R.id.recognition_view);
        relativeLayout = findViewById (R.id.relative);
        data = findViewById (R.id.data);
        resolved = findViewById (R.id.resolved);
        int[] colors = {
                ContextCompat.getColor(this, R.color.GoogleGreen),
                ContextCompat.getColor(this, R.color.GoogleBlue),
                ContextCompat.getColor(this, R.color.GoogleRed),
                ContextCompat.getColor(this, R.color.GoogleYellow),
                ContextCompat.getColor(this, R.color.GoogleGreen)
        };

        //Permission for Audio and Speech
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions (new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, 10);
        }

        //Setting up custom Recognizers
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer (this);
        speechRecognizer.setRecognitionListener (new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                recognitionProgressView.stop();
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                recognitionProgressView.stop ();
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        recognitionProgressView.setSpeechRecognizer (speechRecognizer);
        recognitionProgressView.setColors (colors);
        recognitionProgressView.play ();
        recognitionProgressView.setRecognitionListener (new RecognitionListener () {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> arrayList = bundle.getStringArrayList (SpeechRecognizer.RESULTS_RECOGNITION);
                if(arrayList!=null){
                    QUERY = arrayList.get (0);
                    Async async = new Async (MainActivity.this);
                    async.execute ();
                }else {
                    Toast.makeText (MainActivity.this, "Array List is null", Toast.LENGTH_SHORT).show ();
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        speechIntent = new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra (RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra (RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault ());
    }

    public void start(View v) {
        speechRecognizer.startListening (speechIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10&&resultCode==RESULT_OK){
            Toast.makeText(this, "Got Permissions", Toast.LENGTH_SHORT).show();
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions (new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, 10);
            }
        }
    }
}
