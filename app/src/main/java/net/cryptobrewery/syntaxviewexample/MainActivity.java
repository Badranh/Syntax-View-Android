package net.cryptobrewery.syntaxviewexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.cryptobrewery.syntaxview.SyntaxView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.
                activity_main);
           SyntaxView syntax_view = findViewById(R.id.syn);

            syntax_view.setBgColor("#ffffff");
            syntax_view.setPrintStatmentsColor("#931B42");
            syntax_view.setCodeTextColor("#000000");
            syntax_view.setRowNumbersBgColor("#ffffff");
            syntax_view.setRowNumbersColor("#000000");
    }
}
