package com.jawaly;

import java.util.Base64;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SMS4JawalyClient {
    private final String apiKey;
    private final String apiSecret;
    private final String baseUrl = "https://api-sms.4jawaly.com/api/v1";
    private final HttpClient httpClient;
    private final ExecutorService executor;

    public SMS4JawalyClient(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.httpClient = HttpClient.newHttpClient();
        this.executor = Executors.newFixedThreadPool(5); // 5 threads like Python version
    }

    private String getAuthToken() {
        String auth = apiKey + ":" + apiSecret;
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }

    private List<List<String>> chunkNumbers(List<String> numbers, int chunkSize) {
        List<List<String>> chunks = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i += chunkSize) {
            chunks.add(numbers.subList(i, Math.min(numbers.size(), i + chunkSize)));
        }
        return chunks;
    }

    private Future<String> sendChunk(String message, List<String> numbers, String sender) {
        return executor.submit(() -> {
            String jsonBody = String.format(
                "{\"messages\":[{\"text\":\"%s\",\"numbers\":%s,\"sender\":\"%s\"}]}",
                message,
                numbers.toString(),
                sender
            );

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/account/area/sms/send"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + getAuthToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        });
    }

    public List<String> sendSMS(String message, List<String> numbers, String sender) throws Exception {
        // تحديد حجم الشريحة بناءً على العدد الإجمالي
        int chunkSize;
        if (numbers.size() <= 5) {
            chunkSize = numbers.size();
        } else if (numbers.size() <= 100) {
            chunkSize = numbers.size() / 5 + (numbers.size() % 5 == 0 ? 0 : 1);
        } else {
            chunkSize = 100;
        }

        // تقسيم الأرقام إلى شرائح
        List<List<String>> chunks = chunkNumbers(numbers, chunkSize);
        
        // إرسال الشرائح بالتوازي
        List<Future<String>> futures = new ArrayList<>();
        for (List<String> chunk : chunks) {
            futures.add(sendChunk(message, chunk, sender));
        }

        // تجميع النتائج
        List<String> results = new ArrayList<>();
        for (Future<String> future : futures) {
            results.add(future.get()); // انتظار النتائج
        }

        return results;
    }

    public String getBalance() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/account/area/me/packages"))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", "Basic " + getAuthToken())
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getSenderNames() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/account/area/senders"))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", "Basic " + getAuthToken())
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // تنظيف الموارد عند الانتهاء
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
