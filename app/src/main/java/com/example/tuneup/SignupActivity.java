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

// 1. ייבוא של קלאס ה-Binding שנוצר אוטומטית עבור ה-Layout של ההרשמה
import com.example.tuneup.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button signUpBtn      ;
    EditText etEmailSignup;
    TextView tvLoginPrompt;
    EditText etFullName;
    EditText etPasswordSignup;
    EditText etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 4. הגדרת התצוגה הראשית לשורש (root) של ה-Layout המנופח
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        etFullName= findViewById(R.id.etFullName);
        etEmailSignup= findViewById(R.id.etEmailSignup);
        etPasswordSignup= findViewById(R.id.etPasswordSignup);
        etConfirmPassword= findViewById(R.id.etConfirmPassword);
        tvLoginPrompt= findViewById(R.id.tvLoginPrompt);
        tvLoginPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SignupActivity", "tvLoginPrompt clicked");
                //Intent to SignupActivity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            });
        signUpBtn = findViewById(R.id.btnSignup);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignup();
            }
        });
    }
    private void goToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * פונקציה המטפלת בלוגיקת ההרשמה
     */
    private void handleSignup() {
        // הגישה לטקסט מהשדות-
        String fullName = etFullName.getText().toString().trim();
        String email =etEmailSignup.getText().toString().trim();
        String password = etPasswordSignup.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // בדיקה שכל השדות מלאים
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "אנא מלא את כל השדות.", Toast.LENGTH_SHORT).show();
            return;
        }

        // בדיקה שהסיסמאות תוא מות
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "הסיסמאות אינן תואמות.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isValidEmail = isValidEmail(email);
        if (!isValidEmail) {
            Toast.makeText(this, "אימייל לא תקין.", Toast.LENGTH_SHORT).show();
            return;
        }

        signUpUser(email,password);
    }

    private boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void signUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // הצלחה: המשתמש נוסף למערכת
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "הרשמה הצליחה!", Toast.LENGTH_SHORT).show();
                        // כאן אפשר לעבור למסך הבית
                        goToHomeActivity();
                    } else {
                        // כישלון: הצגת הודעה למשתמש (למשל, פורמט אימייל לא תקין או סיסמה קצרה מדי)
                        Toast.makeText(this, "שגיאה: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
