package com.gptjava.gptjava;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.ResourceBundle;



public class MainController implements Initializable {

    ObservableList<String> CONVERSATIONS = FXCollections.observableArrayList();

    @FXML
    public Button sendButton;
    @FXML
    public void onSendButtonClicked() {
        String currentConversationText = conversationTextArea.getText();
        String userQuestion = questionTextField.getText();


        conversationTextArea.clear();
        conversationTextArea.setText(currentConversationText +"User said- \n"+questionTextField.getText()+"\n");
        questionTextField.clear();

        //String token = System.getenv("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");
        OpenAiService service = new OpenAiService("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");

        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Somebody once told me the world is gonna roll me")
                .model("ada")
                .echo(true)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);

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