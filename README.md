# Syntax-View-Android
Beautiful Android Syntax View with line counter it will automatically highlight the code,you can design your own IDE using this view

# Example
Colors can be modified as you want

![Screenshot](http://cryptobrewery.net/scn/4.png)

# How to add to your app 

add this to your dependencies in build.gradle file
```
implementation 'com.github.Badranh:Syntax-View-Android:0.1.2'
```
Add it in your root build.gradle at the end of repositories:

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

# USAGE
<b>1st way:</b>

- Step 1: 
  add this to your activity_main.xml or any other activity you want:
```
 <net.cryptobrewery.syntaxview.SyntaxView
    android:id="@+id/syn"
    android:layout_width="match_parent"
     android:layout_height="match_parent">
 </net.cryptobrewery.syntaxview.SyntaxView>
```
- Step 2:
  add this to your java code
```
//declaration
SyntaxView syntax_view = findViewById(R.id.syn);

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

```
<b>2nd way:</b>
```
//this way will set default methods
SyntaxView syntax_view = new SyntaxView(this);
setContentView(syntax_view);
```
<b>3rd way:</b>
```
//other colors can be set manually if needed using set methods like setAnnotationsColor
SyntaxView syntax_view = new SyntaxView(this,"#2b2b2b","#cc7832","#4a85a3","#cc7832","#6a8759");
setContentView(syntax_view);
```

# Set On TextChangeListener
```
   //on text change listener
        syntax_view.code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	    		//do whatever you want
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //on text change listener
		//do whatever you want
                Log.d("tester",charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
		//do whatever you want
            }
        });
```
# Changing Font
```
Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "yourFont.ttf");
syntax_view.setFont(tf);
```
# Code Paranthesis Validation Method
User can check if there is an error with the paranthesis in his code , the check will be perfomed for once only when called
```
syntax_view.checkMyCode();
```
# Changes:
+6/25/2018:
- Uploaded Syntax View
- Syntax Highlighting
- Line Number Counter
- Color Flexibility to meet user requirements

+6/26/2018:
 - added new method setFont() to change font of the code if you wish to
 - changed the default font to a new classic font
 - disabled autocorrect (By: MohamedElidrissi)
 - removed unused attribute (By: MohamedElidrissi)
 
 +6/30/2018
 - Added Auto Indentation
 - Added CheckMyCode Method To Check Code Paranthesis Validity
 - Changed the old font
 - Added a new method to change the Font
# Contribute
Next update: 
- User will have the ability to choose a language like " C,Java,Python" So we can do a faster UI and Highlighting(feel free to implement this update if you are able to do so)
- You can contribute to this project for missing programming key words so we can include
them in the next update.

License : MIT
