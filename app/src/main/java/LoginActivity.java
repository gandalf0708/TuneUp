import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

// 1. ייבוא של קלאס ה-Binding שנוצר אוטומטית עבור ה-Layout שלך
import com.example.tuneup.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    // 2. הגדרת משתנה יחיד עבור אובייקט ה-Binding
    // הוא יכיל את כל הרכיבים מקובץ ה-XML בצורה בטוחה
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 3. "ניפוח" (Inflate) של ה-Layout באמצעות View Binding
        // הפעולה הזו יוצרת את כל אובייקטי ה-View ומחזיקה אותם במשתנה binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        // 4. הגדרת התצוגה הראשית לשורש (root) של ה-Layout המנופח
        setContentView(binding.getRoot());

        // 5. הגדרת מאזין ללחיצה על כפתור ההתחברות
        // הגישה לרכיבים מתבצעת דרך אובייקט ה-binding, ללא צורך ב-findViewById
        // שמות המזהים הופכים אוטומטית ל-camelCase (למשל, btn_login -> btnLogin)
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // 6. הגדרת מאזין ללחיצה על טקסט "הירשם כאן"
        binding.tvSignupPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // יוצר Intent חדש למעבר ל-SignupActivity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * פונקציה המטפלת בלוגיקת ההתחברות
     */
    private void handleLogin() {
        // הגישה לטקסט מהשדות מתבצעת גם כן דרך אובייקט ה-binding
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "אנא הזן אימייל וסיסמה.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "מנסה להתחבר כ: " + email, Toast.LENGTH_LONG).show();

        // --- כאן יבוא קוד אימות המשתמש. לאחר אימות מוצלח: ---
        // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // startActivity(intent);
        // finish(); // סוגר את מסך ההתחברות כדי שהמשתמש לא יוכל לחזור אליו
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // חשוב לשחרר את ה-binding כדי למנוע דליפות זיכרון כשהמסך נהרס
        binding = null;
    }
}

