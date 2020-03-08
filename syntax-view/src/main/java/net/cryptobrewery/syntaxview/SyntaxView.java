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
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxView extends ScrollView {
    private EditText code;
    private int oldLength;
    private int newLength;
    private String language = "JAVA";
    private final SyntaxHighlighter  keywords = new SyntaxHighlighter(
            Pattern.compile(
                    "\\b(include|package|transient|strictfp|void|char|short|int|long|double|float|const|static|volatile|byte|boolean|class|interface|native|private|protected|public|final|abstract|synchronized|enum|instanceof|assert|if|else|switch|case|default|break|goto|return|for|while|do|continue|new|throw|throws|try|catch|finally|this|extends|implements|import|true|false|null)\\b"));
    private final SyntaxHighlighter annotations = new SyntaxHighlighter(
            Pattern.compile(
                    "@Override|@Callsuper|@Nullable|@Suppress|@SuppressLint|super"));
    private final SyntaxHighlighter numbers = new SyntaxHighlighter(
            Pattern.compile("(\\b(\\d*[.]?\\d+)\\b)")
    );
    private final SyntaxHighlighter special = new SyntaxHighlighter(
            Pattern.compile("[;#]")
    );
    private final SyntaxHighlighter printStatments = new SyntaxHighlighter(
            Pattern.compile("\"(.+?)\"")
    );
    private final SyntaxHighlighter attributes = new SyntaxHighlighter(
            Pattern.compile("")
    );
    private final SyntaxHighlighter[] schemes = {keywords, numbers, special, printStatments, annotations, attributes};
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
        setColor(keywords,keywordsColor);
        setColor(numbers,NumberColor);
        setColor(special,specialCharColors);
        setColor(printStatments,printStatmentsColor);
        setColor(annotations, "#80FFEC");
        setColor(attributes, "#80FFEC");
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
                StringBuilder linesText = new StringBuilder();
                for (int i = 1; i <= numb_of_line; i++) {
                    linesText.append(i).append("\n");
                }
                rows.setText(linesText.toString());
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
                    if (oldLength < newLength && (lastDiff == ':' || lastDiff == '{')) {

                        int position = code.getSelectionStart();
                        code.getText().insert(position, "\n ");
                        position = code.getSelectionStart();
                        code.setSelection(position);
                    }
                }
                removeSpans(s, ForegroundColorSpan.class);
                for (SyntaxHighlighter scheme : schemes) {
                    for (Matcher m = scheme.getPattern().matcher(s); m.find(); ) {
                        s.setSpan(new ForegroundColorSpan(scheme.getColor()),
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
    //check string's type is color format
    private void checkColor(String color)throws Error{
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
        setColor(keywords,color);
    }

    public void setNumbersColor(String color) {
        setColor(numbers,color);

    }

    public void setSpecialCharsColor(String color) {
        setColor(special,color);

    }

    public void setCodeTextColor(String color) {
        checkColor(color);
        code.setTextColor(Color.parseColor(color));
    }

    public void setAnnotationsColor(String color) {
        setColor(annotations,color);

    }

    public void setPrintStatmentsColor(String color) {
        setColor(printStatments,color);

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
        for (char c : valid) {
            if (c == '{' || c == '(') {
                stackCheck.push((c));
            }
            if (c == '}' || c == ')') {
                if (stackCheck.empty()) {
                    Toast.makeText(getContext(), "Your Code Has Invalid Parenthesis", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (!matchPair((char) stackCheck.peek(), c)) {
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
        else return c1 == '[' && c2 == ']';
    }

    public void checkMyCode(){checkValidity(code.getText());}

    private char getLastDifference(String a,String b){
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

    public EditText getCode() {
        return code;
    }

    private void setColor(SyntaxHighlighter type,String color){
        checkColor(color);
        type.setColor(color);
    }

    public void setLanguage(String newLang)
    {
        String upperNewLang = newLang.toUpperCase();

        if(upperNewLang.compareTo(language) != 0) {
            language = upperNewLang;

            switch (language)
            {
                case "JAVA":
                    keywords.setPattern(
                            Pattern.compile(
                                    "\\b(include|package|transient|strictfp|void|char|short|int|long|double|float|const|static|volatile|byte|boolean|class|interface|native|private|protected|public|final|abstract|synchronized|enum|instanceof|assert|if|else|switch|case|default|break|goto|return|for|while|do|continue|new|throw|throws|try|catch|finally|this|extends|implements|import|true|false|null)\\b")
                    );
                    annotations.setPattern(
                            Pattern.compile(
                                    "@Override|@Callsuper|@Nullable|@Suppress|@SuppressLint|super|@Deprecated|@SuppressWarnings")
                    );
                    break;
                case "PYTHON":
                    // Python keywords refer to https://www.programiz.com/python-programming/keywords-identifier
                    keywords.setPattern(
                            Pattern.compile("\\b(class|if|else|break|return|for|while|continue|try|except|finally|import|True|False|None|del|and|as|assert|def|elif|from|global|in|is|lambda|nonlocal|not|or|pass|raise|yield|with)\\b")
                    );
                    annotations.setPattern(Pattern.compile(""));
                    break;
                case "C":
                    // C keywords refer to https://www.sitesbay.com/cpp/cpp-keywords
                    keywords.setPattern(
                            Pattern.compile("\\b(include|void|char|short|int|long|double|float|const|static|volatile|enum|if|else|switch|case|default|break|goto|return|for|while|do|continue|NULL|auto|extern|register|signed|sizeof|struct|typedef|union|unsigned)\\b")
                    );
                    annotations.setPattern(Pattern.compile(""));
                    break;
                case "C++":
                case "CPP":
                    // C keywords refer to https://www.sitesbay.com/cpp/cpp-keywords
                    keywords.setPattern(
                            Pattern.compile("\\b(include|void|char|short|int|long|double|float|const|static|volatile|enum|if|else|switch|case|default|break|goto|return|for|while|do|continue|NULL|auto|extern|register|signed|sizeof|struct|typedef|union|unsigned|bool|class|private|protected|public|new|throw|try|catch|this|true|false|asm|const_cast|delete|using|dynamic_cast|explicit|friend|inline|mutable|typeid|virtual|namespace|operator|typename|wchar_t|reinterpret_case|static_cast|template)\\b")
                    );
                    annotations.setPattern(Pattern.compile(""));
                    break;
                case "JAVASCRIPT":
                    // javaScript keywords refer to https://www.w3schools.in/javascript-tutorial/keywords/
                    keywords.setPattern(
                            Pattern.compile("\\b(package|transient|void|char|short|int|long|double|float|static|volatile|byte|boolean|interface|native|private|protected|public|final|abstract|synchronized|instanceof|if|else|switch|case|default|break|goto|return|for|while|do|continue|new|throw|throws|try|catch|finally|this|implements|true|false|null|var|const|delete|function|in|yield|eval|arguments|debugger|let|typeof|with)\\b")
                    );
                    annotations.setPattern(Pattern.compile(""));
                    break;
                case "HTML":
                    // html keywords refer to https://www.w3schools.com/tags/ref_byfunc.asp
                    keywords.setPattern(
                            Pattern.compile("\\b(!DOCTYPE|!doctype|html|head|title|body|h1|h2|h3|h4|h5|h6|p|br|hr|acronym|abbr|address|b|bdi|bdo|big|blockquote|center|cite|code|del|dfn|em|font|i|ins|kbd|mark|meter|pre|progress|q|rp|rt|ruby|s|samp|small|strike|strong|sub|sup|template|time|tt|u|var|wbr|form|input|textarea|button|select|optgroup|option|label|fieldset|legend|datalist|output|frame|frameset|noframes|iframe|img|map|area|canvas|figcaption|figure|picture|svg|audio|source|track|video|a|link|nav|ul|ol|li|dir|dl|dt|dd|table|caption|th|tr|td|thead|tbody|tfoot|col|colgroup|style|div|span|header|footer|main|section|article|aside|details|dialog|summary|data|head|meta|base|basefont|script|noscript|applet|embed|object|param)\\b")
                    );
                    annotations.setPattern(Pattern.compile(""));
                    attributes.setPattern(
                            Pattern.compile(" (.+?)=|<!(.+?)>")
                    );
                    break;
                case "CSS":
                    // css keywords refer to https://www.w3schools.com/tags/ref_byfunc.asp
                    keywords.setPattern(
                            Pattern.compile("\\b(html|head|title|body|h1|h2|h3|h4|h5|h6|p|br|hr|acronym|abbr|address|b|bdi|bdo|big|blockquote|center|cite|code|del|dfn|em|font|i|ins|kbd|mark|meter|pre|progress|q|rp|rt|ruby|s|samp|small|strike|strong|sub|sup|template|time|tt|u|var|wbr|form|input|textarea|button|select|optgroup|option|label|fieldset|legend|datalist|output|frame|frameset|noframes|iframe|img|map|area|canvas|figcaption|figure|picture|svg|audio|source|track|video|a|link|nav|ul|ol|li|dir|dl|dt|dd|table|caption|th|tr|td|thead|tbody|tfoot|col|colgroup|style|div|span|header|footer|main|section|article|aside|details|dialog|summary|data|head|meta|base|basefont|script|noscript|applet|embed|object|param)\\b")
                    );
                    annotations.setPattern(Pattern.compile(""));
                    attributes.setPattern(
                            Pattern.compile(" (.+?):")
                    );
                    break;
                default:
                    break;
            }
        }
    }

    public static String[] getSupportLanguage()
    {
        return new String[]{"Java", "C", "C++", "Python", "JavaScript", "HTML", "CSS"};
    }


}

