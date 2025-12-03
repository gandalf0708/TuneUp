import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

// 1. ייבוא של קלאס ה-Binding שנוצר אוטומטית עבור ה-Layout של ההרשמה
import com.example.tuneup.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    // 2. הגדרת משתנה עבור אובייקט ה-Binding
    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 3. "ניפוח" (Inflate) של ה-Layout באמצעות View Binding
        binding = ActivitySignupBinding.inflate(getLayoutInflater());

        // 4. הגדרת התצוגה הראשית לשורש (root) של ה-Layout המנופח
        setContentView(binding.getRoot());

        // 5. הגדרת מאזין ללחיצה על כפתור ההרשמה
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignup();
            }
        });

        // 6. הגדרת מאזין לחזרה למסך ההתחברות
        binding.tvLoginPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // סוגר את מסך ההרשמה הנוכחי וחוזר למסך הקודם (מסך ההתחברות)
                finish();
            }
        });
    }

    /**
     * פונקציה המטפלת בלוגיקת ההרשמה
     */
    private void handleSignup() {
        // הגישה לטקסט מהשדות מתבצעת דרך אובייקט ה-binding
        String fullName = binding.etFullName.getText().toString().trim();
        String email = binding.etEmailSignup.getText().toString().trim();
        String password = binding.etPasswordSignup.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        // בדיקה שכל השדות מלאים
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "אנא מלא את כל השדות.", Toast.LENGTH_SHORT).show();
            return;
        }

        // בדיקה שהסיסמאות תואמות
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "הסיסמאות אינן תואמות.", Toast.LENGTH_SHORT).show();
            return;
        }

        // אם כל הבדיקות עברו, הצג הודעת הצלחה (או בצע את לוגיקת ההרשמה מול שרת)
        Toast.makeText(this, "מנסה להירשם עם אימייל: " + email, Toast.LENGTH_LONG).show();

        // --- כאן יבוא קוד ההרשמה. לאחר הרשמה מוצלחת, אפשר לעבור למסך הראשי או להתחברות ---
        // finish(); // חזרה למסך ההתחברות כדי שהמשתמש יתחבר
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // שחרור ה-binding כדי למנוע דליפות זיכרון
        binding = null;
    }
}
