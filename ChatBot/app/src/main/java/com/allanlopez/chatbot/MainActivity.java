package com.allanlopez.chatbot;

import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends Activity implements AIListener {

    private EditText editText;
    private TextView textView;
    private Button button;

    private AIService aiService;
    private AIDataService aiDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AIConfiguration config = new AIConfiguration("0f4146091f08451fb8e8465a1471d4ae",
                AIConfiguration.SupportedLanguages.Spanish, AIConfiguration.RecognitionEngine.System);

        aiDataService = new AIDataService(config);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendRequestTask(aiDataService).execute(editText.getText().toString());
            }
        });
    }

    public class SendRequestTask extends AsyncTask<String, String, AIResponse>{
        private  AIDataService aiDataService;

        public SendRequestTask(AIDataService aiDataService) {
            this.aiDataService = aiDataService;
        }

        @Override
        protected AIResponse doInBackground(String... strings) {
            AIRequest aiRequest = new AIRequest();
            AIResponse aiResponse = null;
            try {
                aiRequest.setQuery(strings[0]);
                aiResponse = aiDataService.request(aiRequest);
            } catch (AIServiceException e) {
                e.printStackTrace();
            }
            return aiResponse;
        }

        @Override
        protected void onPostExecute(AIResponse aiResponse) {
            super.onPostExecute(aiResponse);
            Result result = aiResponse.getResult();
            textView.append("You: " + result.getResolvedQuery() + "\r\n" );
            textView.append("Bot: " + result.getFulfillment().getSpeech()+"\r\n");
        }
    }

    @Override
    public void onResult(AIResponse result) {

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
