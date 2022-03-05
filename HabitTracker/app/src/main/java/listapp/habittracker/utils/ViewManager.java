package listapp.habittracker.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class ViewManager {

    public static String getString (TextView view){
        return view.getText().toString();
    }

    public static void changeWarnFlag(TextView view, String warn){
        if(warn!=null){
            view.setText(warn);
            view.setVisibility(TextView.VISIBLE);
        }
        else{
            view.setVisibility(TextView.INVISIBLE);
        }
    }

    public static abstract class newTextWatcher{
        TextWatcher textWatcher;

        public newTextWatcher() {
            textWatcher = warnTextWatcher();
        }

        public TextWatcher warnTextWatcher(){
            return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    doOnTextChange(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };
        }

        public abstract void doOnTextChange(String s);

        public TextWatcher getTextWatcher() {
            return textWatcher;
        }
    }





}
