package com.gptjava.gptjava.chatgptcontrollers;

public class ChatGptRequest {
    private String prompt;

    public ChatGptRequest(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
