package com.example.tuneup; // וודא שזה תואם לחבילה שלך

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    // רכיבי ממשק המשתמש (UI)
    private EditText etStatus, etProjects, etLookingFor;
    private Button btnPost, btnGeminiEnhance;
    private ProgressBar pbLoading;
    private RecyclerView rvLobby;

    // הגדרות עבור Gemini API
    private static final String API_KEY = ""; // המפתח יוזרק אוטומטית בזמן ריצה
    private static final String GEMINI_MODEL = "gemini-2.5-flash-preview-09-2025";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // וודא ששם ה-Layout תואם לקובץ ה-XML שלך
        setContentView(R.layout.activity_home);

        // 1. קישור הרכיבים מה-XML לקוד ה-Java
        initViews();

        // 2. הגדרת מאזין לכפתור הפרסום הרגיל
        btnPost.setOnClickListener(v -> handlePostAction());

        // 3. הגדרת מאזין לכפתור השדרוג באמצעות AI ✨
        btnGeminiEnhance.setOnClickListener(v -> enhanceDescriptionWithAI());
    }

    private void initViews() {
        etStatus = findViewById(R.id.etStatus);
        etProjects = findViewById(R.id.etProjects);
        etLookingFor = findViewById(R.id.etLookingFor);
        btnPost = findViewById(R.id.btnPost);
        btnGeminiEnhance = findViewById(R.id.btnGeminiEnhance);
        pbLoading = findViewById(R.id.pbLoading);
        rvLobby = findViewById(R.id.rvLobby);

        // הגדרת הרשימה (RecyclerView)
        if (rvLobby != null) {
            rvLobby.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    /**
     * פונקציה לפרסום פוסט רגיל ללובי
     */
    private void handlePostAction() {
        String status = etStatus.getText().toString().trim();
        if (status.isEmpty()) {
            Toast.makeText(this, "נא להזין סטטוס לפני הפרסום", Toast.LENGTH_SHORT).show();
            return;
        }

        // כאן תוסיף לוגיקה לשמירה ב-Database (כמו Firebase)
        Toast.makeText(this, "הפוסט פורסם בהצלחה! 🎸", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    /**
     * פונקציה שפונה ל-Gemini API כדי לשדרג את ניסוח הפוסט
     */
    private void enhanceDescriptionWithAI() {
        String status = etStatus.getText().toString();
        String projects = etProjects.getText().toString();

        if (status.isEmpty() && projects.isEmpty()) {
            Toast.makeText(this, "כתוב משהו כדי שה-AI יוכל לעזור לך", Toast.LENGTH_SHORT).show();
            return;
        }

        // הצגת אנימציית טעינה
        pbLoading.setVisibility(View.VISIBLE);
        btnGeminiEnhance.setEnabled(false);

        // בניית הבקשה ל-AI
        String prompt = "You are a branding expert for musicians. Take the following raw info and rewrite it " +
                "to be professional, creative and engaging in Hebrew. " +
                "Status: " + status + ". Projects: " + projects + ". Keep it concise.";

        sendRequestToGemini(prompt);
    }

    private void sendRequestToGemini(String prompt) {
        executorService.execute(() -> {
            try {
                // יצירת החיבור ל-API
                URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/" + GEMINI_MODEL + ":generateContent?key=" + API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // יצירת גוף הבקשה ב-JSON
                JSONObject jsonBody = new JSONObject();
                JSONArray contents = new JSONArray();
                JSONObject parts = new JSONObject();
                parts.put("text", prompt);
                contents.put(new JSONObject().put("parts", new JSONArray().put(parts)));
                jsonBody.put("contents", contents);

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.toString().getBytes());
                os.flush();

                if (conn.getResponseCode() == 200) {
                    Scanner scanner = new Scanner(conn.getInputStream());
                    String response = scanner.useDelimiter("\\A").next();
                    scanner.close();

                    // חילוץ התשובה מה-JSON
                    JSONObject resultJson = new JSONObject(response);
                    String aiText = resultJson.getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");

                    // עדכון הממשק במסך הראשי
                    mainHandler.post(() -> {
                        etProjects.setText(aiText);
                        Toast.makeText(this, "הניסוח שודרג! ✨", Toast.LENGTH_SHORT).show();
                        hideLoading();
                    });
                } else {
                    mainHandler.post(() -> {
                        Toast.makeText(this, "שגיאה ב-AI. נסה שוב.", Toast.LENGTH_SHORT).show();
                        hideLoading();
                    });
                }
            } catch (Exception e) {
                mainHandler.post(() -> {
                    Toast.makeText(this, "תקלה בתקשורת", Toast.LENGTH_SHORT).show();
                    hideLoading();
                });
            }
        });
    }

    private void hideLoading() {
        pbLoading.setVisibility(View.GONE);
        btnGeminiEnhance.setEnabled(true);
    }

    private void clearFields() {
        etStatus.setText("");
        etProjects.setText("");
        etLookingFor.setText("");
    }
}