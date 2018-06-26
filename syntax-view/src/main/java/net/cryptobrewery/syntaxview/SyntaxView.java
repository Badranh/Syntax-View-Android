package net.cryptobrewery.syntaxview;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TOSHIBA on 6/25/2018.
 */

public class SyntaxView extends RelativeLayout  {

    private SyntaxHighlighter keywords = new SyntaxHighlighter(
            Pattern.compile(
                    "\\b(include|package|transient|strictfp|void|char|short|int|long|double|float|const|static|volatile|byte|boolean|class|interface|native|private|protected|public|final|abstract|synchronized|enum|instanceof|assert|if|else|switch|case|default|break|goto|return|for|while|do|continue|new|throw|throws|try|catch|finally|this|extends|implements|import|true|false|null)\\b"));
    private SyntaxHighlighter annotations = new SyntaxHighlighter(
            Pattern.compile(
                    "@Override|@Callsuper|@Nullable|@Suppress|@SuppressLint|super"));
    private SyntaxHighlighter numbers = new SyntaxHighlighter(
            Pattern.compile("(\\b(\\d*[.]?\\d+)\\b)")
    );
    private SyntaxHighlighter special = new SyntaxHighlighter(
            Pattern.compile(";|#")
    );

    private SyntaxHighlighter printStatments = new SyntaxHighlighter(
            Pattern.compile("\"(.+?)\"")
    );

    private SyntaxHighlighter[] schemes = { keywords, numbers,special,printStatments,annotations };


    public MaterialEditText code;
    private TextView rows;

    public SyntaxView(Context context) {
        super(context);
        initialize(context,"#2b2b2b","#cc7832","#4a85a3","#cc7832","#6a8759");

    }

    public SyntaxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context,"#2b2b2b","#cc7832","#4a85a3","#cc7832","#6a8759");
    }

    public SyntaxView(Context context,String BackgroundColor,String keywordsColor,String NumberColor,String specialCharColors,String printStatmentsColor) {
        super(context);
        initialize(context,BackgroundColor,keywordsColor,NumberColor,specialCharColors,printStatmentsColor);
    }

     private void initialize(Context context, String BackgroundColor, final String keywordsColor, final String NumberColor, final String specialCharColors, final String printStatmentsColor){
       // default/constructor color are set here

        setKeywordsColor(keywordsColor);
        setNumbersColor(NumberColor);
        setSpecialCharsColor(specialCharColors);
        setPrintStatmentsColor(printStatmentsColor);
        //inflate and get the helper views
        inflate(context, R.layout.syntaxview, this);
        code = findViewById(R.id.code);
        rows = findViewById(R.id.rows);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "progfont.ttf");
        code.setTypeface(tf);
        code.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		code.setSingleLine(false);
		code.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
		code.setBackgroundColor(Color.parseColor(BackgroundColor));
         rows.setTypeface(tf);
        code.addTextChangedListener(new TextWatcher() {


             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }
                //increment the rows view by 1 when the user moves to next line
             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 int numb_of_line = code.getLineCount();
                 String linesText = "";
                 for(int i = 1 ; i <= numb_of_line ; i++){
                     linesText = linesText + i + "\n";
                 }
                 rows.setText(linesText);
             //    printChange(s);

             }
            //remove old highlighting and set new highlighting
             @Override
             public void afterTextChanged(final Editable s) {
                 removeSpans(s, ForegroundColorSpan.class);
                 for (SyntaxHighlighter scheme : schemes) {
                     for(Matcher m = scheme.pattern.matcher(s); m.find();) {
                         s.setSpan(new ForegroundColorSpan(scheme.color),
                                 m.start(),
                                 m.end(),
                                 Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                     }

                 }


             }
            //remove old highlighting
             void removeSpans(Editable e, Class<? extends CharacterStyle> type) {
                 CharacterStyle[] spans = e.getSpans(0, e.length(), type);
                 for (CharacterStyle span : spans) {
                     e.removeSpan(span);
                 }

             }


         });

    }

//the user will be able to change color of the view as he wishes
    public void setBgColor(String color) throws Error {

         color = color.trim();

         if(!color.contains("#")){
             throw new Error("SyntaxView Error : Invalid Color");
         }
         if(TextUtils.isEmpty(color)){
             throw new Error("SyntaxView Error : Empty Color String");

         }
         if(color.length() != 7){
             throw new Error("SyntaxView Error : Unknown Color");

         }
         code.setBackgroundColor(Color.parseColor(color));
    }
    public void setKeywordsColor(String color) throws Error{
        color = color.trim();

        if(!color.contains("#")){
            throw new Error("SyntaxView Error : Invalid Color");
        }
        if(TextUtils.isEmpty(color)){
            throw new Error("SyntaxView Error : Empty Color String");

        }
        if(color.length() != 7){
           throw new Error("SyntaxView Error : Unknown Color");

        }
         keywords.setColor(color);
    }
    public void setNumbersColor(String color) throws Error{
        color = color.trim();

        if(!color.contains("#")){
            throw new Error("SyntaxView Error : Invalid Color");
        }
        if(TextUtils.isEmpty(color)){
            throw new Error("SyntaxView Error : Empty Color String");

        }
        if(color.length() != 7){
           throw new Error("SyntaxView Error : Unknown Color");

        }
        numbers.setColor(color);
    }
    public void setSpecialCharsColor(String color)throws Error{
        color = color.trim();

        if(!color.contains("#")){
            throw new Error("SyntaxView Error : Invalid Color");
        }
        if(TextUtils.isEmpty(color)){
            throw new Error("SyntaxView Error : Empty Color String");

        }
        if(color.length() != 7) {
            throw new Error("SyntaxView Error : Unknown Color");
        }
            special.setColor(color);
    }
    public void setCodeTextColor(String color) throws Error{
        color = color.trim();

        if(!color.contains("#")){
            throw new Error("SyntaxView Error : Invalid Color");
        }
        if(TextUtils.isEmpty(color)){
            throw new Error("SyntaxView Error : Empty Color String");

        }
        if(color.length() != 7) {
            throw new Error("SyntaxView Error : Unknown Color");
        }
code.setTextColor(Color.parseColor(color));
    }
    public void setAnnotationsColor(String color) throws Error{
        color = color.trim();

        if(!color.contains("#")){
            throw new Error("SyntaxView Error : Invalid Color");
        }
        if(TextUtils.isEmpty(color)){
            throw new Error("SyntaxView Error : Empty Color String");

        }
        if(color.length() != 7) {
            throw new Error("SyntaxView Error : Unknown Color");
        }
            annotations.setColor(color);

    }
    public void setPrintStatmentsColor(String color) throws Error{
        color = color.trim();

        if(!color.contains("#")){
            throw new Error("SyntaxView Error : Invalid Color");
        }
        if(TextUtils.isEmpty(color)){
            throw new Error("SyntaxView Error : Empty Color String");

        }
        if(color.length() != 7) {
            throw new Error("SyntaxView Error : Unknown Color");
        }
            printStatments.setColor(color);
    }
    public void setRowNumbersColor (String color) throws Error{
        color = color.trim();

        if(!color.contains("#")){
            throw new Error("SyntaxView Error : Invalid Color");
        }
        if(TextUtils.isEmpty(color)){
            throw new Error("SyntaxView Error : Empty Color String");

        }
        if(color.length() != 7) {
            throw new Error("SyntaxView Error : Unknown Color");
        }
         rows.setTextColor(Color.parseColor(color));
    }
    public void setRowNumbersBgColor (String color) throws Error{
        color = color.trim();

        if(!color.contains("#")){
            throw new Error("SyntaxView Error : Invalid Color");
        }
        if(TextUtils.isEmpty(color)){
            throw new Error("SyntaxView Error : Empty Color String");

        }
        if(color.length() != 7) {
            throw new Error("SyntaxView Error : Unknown Color");
        }
        rows.setBackgroundColor(Color.parseColor(color));
    }

    public void setFont(Typeface tf){
        code.setTypeface(tf);
    }

}
