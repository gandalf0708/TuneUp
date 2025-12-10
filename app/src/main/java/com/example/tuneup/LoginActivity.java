package com.example.tuneup;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// 1. ייבוא של קלאס ה-Binding שנוצר אוטומטית עבור ה-Layout שלך
import com.example.tuneup.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    EditText userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // 5. הגדרת מאזין ללחיצה על כפתור ההתחברות
        // שמות המזהים הופכים אוטומטית ל-camelCase (למשל, btn_login -> btnLogin)
        loginBtn = findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LoginActivity", "login button clicked");
                handleLogin();
            }
        });

//        userEmail = findViewById(R.id.etEmail);
//
//        // 6. הגדרת מאזין ללחיצה על טקסט "הירשם כאן"
//        TextView signup = findViewById(R.id.tvSignupPrompt);
//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // יוצר Intent חדש למעבר ל-com.example.tuneup.SignupActivity
//                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    /**
     * פונקציה המטפלת בלוגיקת ההתחברות
     */
    private void handleLogin() {
        // הגישה לטקסט מהשדות מתבצעת גם כן דרך אובייקט ה-binding
//        String email = userEmail.getText().toString().trim();
//        String password = binding.etPassword.getText().toString().trim();

//        if (email.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "אנא הזן אימייל וסיסמה.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Toast.makeText(this, "מנסה להתחבר כ: " , Toast.LENGTH_LONG).show();

        // --- כאן יבוא קוד אימות המשתמש. לאחר אימות מוצלח: ---
        // Intent intent = new Intent(com.example.tuneup.LoginActivity.this, MainActivity.class);
        // startActivity(intent);
        // finish(); // סוגר את מסך ההתחברות כדי שהמשתמש לא יוכל לחזור אליו
    }

}

