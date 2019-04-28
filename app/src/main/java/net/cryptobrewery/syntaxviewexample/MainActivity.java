package net.cryptobrewery.syntaxviewexample;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import net.cryptobrewery.syntaxview.SyntaxView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.
                activity_main);
           final SyntaxView syntax_view = findViewById(R.id.syn);

        //this will set the color of Code Text background
        syntax_view.setBgColor("#2b2b2b");
        //this will set the color of strings between " "
        syntax_view.setPrintStatmentsColor("#6a8759");
        //this will set the default code text color other than programming keywords!
        syntax_view.setCodeTextColor("#ffffff");
        //this will set programming keywords color like String,int,for,etc...
        syntax_view.setKeywordsColor("#cc7832");
        //this will set the numbers color in code | ex: return 0; 0 will be colored
        syntax_view.setNumbersColor("#4a85a3");
        //this will set the line number view background color at left
        syntax_view.setRowNumbersBgColor("#2b2b2b");
        //this will set the color of numbers in the line number view at left
        syntax_view.setRowNumbersColor("#cc7832");
        //this will set color of Annotations like super,@Nullable,etc ....
        syntax_view.setAnnotationsColor("#1932F3");
        //this will set special characters color like ;
        syntax_view.setSpecialCharsColor("#cc7832");
        syntax_view.setAutoIndent(false);
        //you can change the font
        //code:
        // Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "yourFont.ttf");
        //syntax_view.setFont(tf);

        //code Validation parenthesis this method will check code once called for one time only
        syntax_view.checkMyCode();

        //on text change listener
        syntax_view.getCode().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //listen for live changes
                Log.d("tester",charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
