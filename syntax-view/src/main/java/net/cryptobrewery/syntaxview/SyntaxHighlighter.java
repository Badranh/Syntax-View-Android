package net.cryptobrewery.syntaxview;

import android.graphics.Color;

import java.util.regex.Pattern;

/**
 * Created by TOSHIBA on 6/25/2018.
 */

 class SyntaxHighlighter {
    Pattern pattern;
      int color;

    public SyntaxHighlighter(Pattern pattern) {
        this.pattern = pattern;
    }

    public void setColor(String color){
        this.color = Color.parseColor(color);
    }
}
