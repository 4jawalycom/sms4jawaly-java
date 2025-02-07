# SMS4Jawaly Java SDK

## 🇬🇧 English

Java library for sending SMS messages through the 4jawaly SMS Gateway with support for parallel sending and batch processing.

### Features
- Parallel SMS sending with automatic batch processing using Java ExecutorService
- Support for large number of recipients (automatically split into chunks)
- Comprehensive error handling and reporting
- Balance checking and sender names management
- Built with Java's modern HttpClient for efficient HTTP requests

### Installation
Add this dependency to your project's `pom.xml`:
```xml
<dependency>
    <groupId>com.jawaly</groupId>
    <artifactId>sms4jawaly</artifactId>
    <version>1.0.4</version>
</dependency>
```

### Usage
```java
import com.jawaly.SMS4JawalyClient;
import java.util.Arrays;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        try {
            // Initialize client
            SMS4JawalyClient client = new SMS4JawalyClient(
                "your-api-key",
                "your-api-secret"
            );

            // Send to multiple numbers (automatically handled in parallel)
            List<String> numbers = Arrays.asList(
                "966500000000",
                "966500000001"
            );
            List<String> results = client.sendSMS(
                "Test message from 4jawaly!",
                numbers,
                "YOUR_SENDER_NAME"
            );

            // Check results
            System.out.println("Results for each chunk: " + results);

            // Check balance
            String balance = client.getBalance();
            System.out.println("Balance: " + balance);

            // Get sender names
            String senders = client.getSenderNames();
            System.out.println("Senders: " + senders);

            // Clean up resources
            client.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## 🇸🇦 عربي

مكتبة جافا لإرسال الرسائل النصية القصيرة عبر بوابة فور جوالي للرسائل مع دعم الإرسال المتوازي ومعالجة الدفعات.

### المميزات
- إرسال الرسائل بشكل متوازي مع معالجة تلقائية للدفعات باستخدام ExecutorService
- دعم لعدد كبير من المستلمين (يتم تقسيمهم تلقائياً إلى مجموعات)
- معالجة شاملة للأخطاء والتقارير
- التحقق من الرصيد وإدارة أسماء المرسلين
- مبني باستخدام HttpClient الحديث في جافا للطلبات الفعالة

### التثبيت
أضف هذا الاعتماد إلى ملف `pom.xml` الخاص بمشروعك:
```xml
<dependency>
    <groupId>com.jawaly</groupId>
    <artifactId>sms4jawaly</artifactId>
    <version>1.0.4</version>
</dependency>
```

### الاستخدام
```java
import com.jawaly.SMS4JawalyClient;
import java.util.Arrays;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        try {
            // تهيئة العميل
            SMS4JawalyClient client = new SMS4JawalyClient(
                "your-api-key",
                "your-api-secret"
            );

            // إرسال إلى عدة أرقام (يتم معالجتها بالتوازي تلقائياً)
            List<String> numbers = Arrays.asList(
                "966500000000",
                "966500000001"
            );
            List<String> results = client.sendSMS(
                "رسالة تجريبية من فور جوالي!",
                numbers,
                "YOUR_SENDER_NAME"
            );

            // التحقق من النتائج
            System.out.println("نتائج كل دفعة: " + results);

            // التحقق من الرصيد
            String balance = client.getBalance();
            System.out.println("الرصيد: " + balance);

            // الحصول على أسماء المرسلين
            String senders = client.getSenderNames();
            System.out.println("المرسلين: " + senders);

            // تنظيف الموارد
            client.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Maven Central Repository

You can view the project on Maven Central [here](https://mvnrepository.com/artifact/com.jawaly/sms4jawaly).
