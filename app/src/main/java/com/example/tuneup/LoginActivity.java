package com.example.tuneup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// 1. ייבוא של קלאס ה-Binding שנוצר אוטומטית עבור ה-Layout שלך
import com.example.tuneup.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button loginBtn;
    EditText etEmail;
   TextView tvSignupPrompt;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);


        // 5. הגדרת מאזין ללחיצה על כפתור ההתחברות
        // שמות המזהים הופכים אוטומטית ל-camelCase (למשל, btn_login -> btnLogin)
        mAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LoginActivity", "login button clicked");
                handleLogin();
            }
        });

        tvSignupPrompt = findViewById(R.id.tvSignupPrompt);
        tvSignupPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LoginActivity", "tvSignupPrompt clicked");
                //Intent to SignupActivity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
     private void goToHomeActivity() {
         Intent intent = new Intent(this, HomeActivity.class);
         startActivity(intent);
         finish();
     }



    /**
     * פונקציה המטפלת בלוגיקת ההתחברות
     */

    private void handleLogin() {
        String email =etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
       if (email.isEmpty() || password.isEmpty()) {
           Toast.makeText(this, "אנא הזן אימייל וסיסמה.", Toast.LENGTH_SHORT).show();
           return;
       }
        boolean isValidEmail = isValidEmail(email);
        if (!isValidEmail) {
            Toast.makeText(this, "אימייל לא תקין.", Toast.LENGTH_SHORT).show();
            return;
        }

        loginUser(email, password);

    }

    private boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

private void loginUser(String email, String password) {


    mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // התחברות הצליחה
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(this, "ברוך הבא!", Toast.LENGTH_SHORT).show();

                    // מעבר למסך הראשי
                    goToHomeActivity();
                } else {
                    // התחברות נכשלה (למשל: סיסמה שגויה או משתמש לא קיים)
                    Toast.makeText(this, "שגיאה: " + task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
}

}

