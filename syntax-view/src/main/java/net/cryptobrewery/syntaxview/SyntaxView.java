package net.cryptobrewery.syntaxview;


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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxView extends RelativeLayout {
    private MaterialEditText code;
    int oldLength, newLength;
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
            Pattern.compile("[;#]")
    );
    private SyntaxHighlighter printStatments = new SyntaxHighlighter(
            Pattern.compile("\"(.+?)\"")
    );
    private SyntaxHighlighter[] schemes = {keywords, numbers, special, printStatments, annotations};
    private TextView rows;
    private boolean autoIndent=false;

    public SyntaxView(Context context) {
        super(context);
        initialize(context, "#2b2b2b", "#cc7832", "#4a85a3", "#cc7832", "#6a8759");

    }

    public SyntaxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, "#2b2b2b", "#cc7832", "#4a85a3", "#cc7832", "#6a8759");
    }

    public SyntaxView(Context context, String BackgroundColor, String keywordsColor, String NumberColor, String specialCharColors, String printStatmentsColor) {
        super(context);
        initialize(context, BackgroundColor, keywordsColor, NumberColor, specialCharColors, printStatmentsColor);
    }

    private void initialize(Context context, String BackgroundColor, final String keywordsColor, final String NumberColor, final String specialCharColors, final String printStatmentsColor) {



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
        ////test
        ///test
        code.setTypeface(tf);
        code.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        code.setSingleLine(false);
        code.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        code.setBackgroundColor(Color.parseColor(BackgroundColor));
        rows.setTypeface(tf);
        code.setSelection(code.getText().length());
        code.addTextChangedListener(new TextWatcher() {
        String temp1,temp2;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldLength = s.length();
                temp1 = s.toString();
            }

            //increment the rows view by 1 when the user moves to next line
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("tester",s.toString());

                int numb_of_line = code.getLineCount();
                String linesText = "";
                for (int i = 1; i <= numb_of_line; i++) {
                    linesText = linesText + i + "\n";
                }
                rows.setText(linesText);
            }
            //remove old highlighting and set new highlighting
            @Override
            public void afterTextChanged(final Editable s) {
                temp2 = s.toString();

                newLength = s.length();
                //user can choose if he want to validate his code or not  this function will check if parenthesis is matched
                //AUTO INDENT NEW FEATURE
                // old and new length are implemented to know if user is deleting or not/if we didn't put this the user will not be able to delete his code
                //if(deleting) then allow him and don't do anything
                //if(!deleting) and there is { at the end of the text -> auto indent
                //

                if(autoIndent) {
                    char lastDiff = getLastDifference(temp2, temp1);
                    if (oldLength < newLength && (lastDiff == ';' || lastDiff == '{')) {

                        int position = code.getSelectionStart();
                        code.getText().insert(position, "\n ");
                        position = code.getSelectionStart();
                        code.setSelection(position);
                    }
                }
                removeSpans(s, ForegroundColorSpan.class);
                for (SyntaxHighlighter scheme : schemes) {
                    for (Matcher m = scheme.pattern.matcher(s); m.find(); ) {
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
    //check string's type is color
    public void checkColor(String color)throws Error{
        color = color.trim();

        if (!color.contains("#")) {
            throw new Error("SyntaxView Error : Invalid Color");
        }
        if (TextUtils.isEmpty(color)) {
            throw new Error("SyntaxView Error : Empty Color String");
        }
        if (color.length() != 7) {
            throw new Error("SyntaxView Error : Unknown Color");
        }
    }

    //the user will be able to change color of the view as he wishes
    public void setBgColor(String color) {
        checkColor(color);
        code.setBackgroundColor(Color.parseColor(color));
    }

    public void setKeywordsColor(String color) {
        checkColor(color);
        keywords.setColor(color);
    }

    public void setNumbersColor(String color) {
        checkColor(color);
        numbers.setColor(color);
    }

    public void setSpecialCharsColor(String color) {
        checkColor(color);
        special.setColor(color);
    }

    public void setCodeTextColor(String color) {
        checkColor(color);
        code.setTextColor(Color.parseColor(color));
    }

    public void setAnnotationsColor(String color) {
        checkColor(color);
        annotations.setColor(color);
    }

    public void setPrintStatmentsColor(String color) {
        checkColor(color);
        printStatments.setColor(color);
    }

    public void setRowNumbersColor(String color) {
        checkColor(color);
        rows.setTextColor(Color.parseColor(color));
    }

    public void setRowNumbersBgColor(String color) {
        checkColor(color);
        rows.setBackgroundColor(Color.parseColor(color));
    }

    public void setFont(Typeface tf) {
        code.setTypeface(tf);
        rows.setTypeface(tf);
    }

    private void checkValidity(Editable s) {
        String s1 = s.toString();
        Stack stackCheck = new Stack();
        char[] valid = s1.toCharArray();
        for (int i = 0; i < valid.length; i++) {
            if (valid[i] == '{' || valid[i] == '(') {
                stackCheck.push((valid[i]));
            }
            if (valid[i] == '}' || valid[i] == ')') {
                if (stackCheck.empty()) {
                    Toast.makeText(getContext(), "Your Code Has Invalid Parenthesis", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (!matchPair((char) stackCheck.peek(), valid[i])) {
                        Toast.makeText(getContext(), "Your Code Has Invalid Parenthesis", Toast.LENGTH_LONG).show();
                        return;
                    }
                    stackCheck.pop();
                }
            }
        }
        if(stackCheck.size() ==1 ){
            Toast.makeText(getContext(), "Unmatched Parenthesis", Toast.LENGTH_LONG).show();
        }

    }

    private boolean matchPair(char c1, char c2) {
        if (c1 == '(' && c2 == ')')
            return true;
        else if (c1 == '{' && c2 == '}')
            return true;
        else if (c1 == '[' && c2 == ']')
            return true;
        else
            return false;
    }

    public void checkMyCode(){checkValidity(code.getText());}

    public char getLastDifference(String a,String b){
        //a is new
        char [] c1 = a.toCharArray();
        //b is old
        char [] c2 = b.toCharArray();

        if(b.length() > a.length())
            return '.';
        if(c1.length ==0 || c2.length == 0)
            return '.';
        if(c1[c1.length-1] != c2[c2.length-1]){
            return c1[c1.length-1];
        }
        for( int i = 0 ; i < b.length() ; i++){
            if(c1[i]!=c2[i]){
                return c1[i];
            }
        }
        return '.';
    }

    public void setAutoIndent(boolean val){
        this.autoIndent = val;
    }

    public MaterialEditText getCode() {
        return code;
    }
}

