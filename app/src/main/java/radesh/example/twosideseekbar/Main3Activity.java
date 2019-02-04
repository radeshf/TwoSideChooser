package radesh.example.twosideseekbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        EditText pws = findViewById(R.id.et);
        final CheckBox checkBoxLower = new CheckBox(this);
        final CheckBox checkBoxUpper = new CheckBox(this);
        final CheckBox checkBoxNumber = new CheckBox(this);

        pws.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkBoxLower.setChecked(hasLowerCase(charSequence.toString()));
                checkBoxUpper.setChecked(hasUpperCase(charSequence.toString()));
                checkBoxNumber.setChecked(hasNumber(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            private boolean hasLowerCase(String s){
                return !s.equals(s.toUpperCase());
            }

            private boolean hasUpperCase(String s){
                return !s.equals(s.toLowerCase());
            }

            private boolean hasNumber(String s){
                return s.matches(".*\\d+.*");
            }

        });
    }
}
