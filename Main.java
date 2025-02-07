package com.jawaly;

import java.util.Arrays;
import java.util.List;

class Main {
    public static void main(String[] args) {
        try {
            // إنشاء نسخة من الكلاينت
            SMS4JawalyClient client = new SMS4JawalyClient(
                "your-api-key",
                "your-api-secret"
            );

            // اختبار جلب الرصيد
            System.out.println("Testing balance check...");
            String balance = client.getBalance();
            System.out.println("Balance Response: " + balance);

            // اختبار جلب أسماء المرسلين
            System.out.println("\nTesting sender names...");
            String senders = client.getSenderNames();
            System.out.println("Sender Names Response: " + senders);

            // اختبار إرسال رسالة متعددة
            List<String> numbers = Arrays.asList(
                "966500000001",
                "966500000002",
                "966500000003",
                "966500000004",
                "966500000005"
            );
            String message = "Test Message";
            String sender = "4jawaly";
            
            System.out.println("\nTesting parallel SMS sending...");
            List<String> responses = client.sendSMS(message, numbers, sender);
            System.out.println("Send SMS Responses:");
            for (int i = 0; i < responses.size(); i++) {
                System.out.println("Chunk " + (i+1) + ": " + responses.get(i));
            }

            // تنظيف الموارد
            client.shutdown();

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
