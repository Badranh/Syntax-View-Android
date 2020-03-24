package net.cryptobrewery.syntaxview;

import android.graphics.Color;

import java.util.regex.Pattern;

/**
 * Created by TOSHIBA on 6/25/2018.
 */

 class SyntaxHighlighter {

      private Pattern pattern;
      private int color;

    SyntaxHighlighter(Pattern pattern) {
        this.pattern = pattern;
    }

    void setColor(String color){
        this.color = Color.parseColor(color);
    }

    public int getColor() {
        return color;
    }

    Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern)
    {
        this.pattern = pattern;
    }
}
