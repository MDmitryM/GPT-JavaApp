package com.gptjava.gptjava.chatgptcontrollers;

import org.springframework.scheduling.annotation.Async;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.concurrent.CompletableFuture;

public interface ChatGptService {
    @POST("/v1/chat/completions")
    @Headers("Content-Type: application/json")
    @Async
    CompletableFuture<ChatGptResponse> getResponse(@Header("Authorization:") String authToken, @Body ChatGptRequest request);
}