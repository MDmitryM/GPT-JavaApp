package com.gptjava.gptjava;

import com.google.gson.Gson;
import com.gptjava.gptjava.chatgptcontrollers.ChatGptRequest;
import com.gptjava.gptjava.chatgptcontrollers.ChatGptResponse;
import com.gptjava.gptjava.chatgptcontrollers.ChatGptService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



public class MainController implements Initializable {

    ObservableList<String> CONVERSATIONS = FXCollections.observableArrayList();

    @FXML
    public Button sendButton;
    @FXML
    public void onSendButtonClicked() {
        String currentConversationText = conversationTextArea.getText();
        String userQuestion = questionTextField.getText();

        conversationTextArea.clear();
        questionTextField.clear();

        Executor executor = Executors.newCachedThreadPool();

        ChatGptService chatGptService = new Retrofit.Builder()
                .baseUrl("https://api.openai.com")
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(executor)
                .build()
                .create(ChatGptService.class);

        CompletableFuture<ChatGptResponse> future = chatGptService.getResponse("Bearer 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",
                new ChatGptRequest(userQuestion));

        conversationTextArea.appendText("User said- \n" + userQuestion + "\n" + currentConversationText);

        future.thenAcceptAsync(response -> {
            Gson gson = new Gson();
            ChatGptResponse gptResponse = gson.fromJson(response.getResponse(), ChatGptResponse.class);

            Platform.runLater(() -> conversationTextArea.appendText("ChatGpt-\n" + gptResponse.getResponse() + "\n"));
        }, executor);

        //Platform.runLater(() ->
    }
    @FXML
    public Button saveButton;
    @FXML
    public void onSaveButtonClicked() throws IOException {
        String currentConversationText = conversationTextArea.getText();
        String[] lines = currentConversationText.split("\n");
        String secondLine = "";
        if (lines.length > 1) {
            secondLine = lines[1];
            secondLine = secondLine.replaceAll("[*,.?:]", "");
        }
        String fileName = secondLine+".txt";

        Path directory = Paths.get("Conversations");
        Path file = directory.resolve(fileName);
        Files.write(file, Collections.singleton(currentConversationText), StandardCharsets.UTF_8);

        if (!CONVERSATIONS.contains(fileName))
        {
        CONVERSATIONS.add(file.toFile().getName());
        conversationMenuListView.refresh();
        }
    }

    @FXML
    public Button newConversationButton;
    @FXML
    public void onNewConversationButtonClicked()
    {
        questionTextField.clear();
        conversationTextArea.clear();
    }
    @FXML
    public ListView conversationMenuListView;
    @FXML
    public TextField questionTextField;
    @FXML
    public TextArea conversationTextArea;

    @Override
     public void initialize(URL url, ResourceBundle resourceBundle)
    {
        sendButton.setDisable(true);
        saveButton.setDisable(true);

        File directory = new File("Conversations");
        File[] directoryFiles = directory.listFiles();


        for (File file : directoryFiles) {
            CONVERSATIONS.add(file.getName());
        }

        conversationMenuListView.setItems(CONVERSATIONS);

        questionTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue.isEmpty())
            {
                sendButton.setDisable(true);
            }
            else
            {
                sendButton.setDisable(false);
            }
        });

        conversationTextArea.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue.isEmpty())
            {
                saveButton.setDisable(true);
            }
            else
            {
                saveButton.setDisable(false);
            }
        });

    }
}