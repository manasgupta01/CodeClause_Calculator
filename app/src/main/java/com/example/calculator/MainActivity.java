package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Stack;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView displayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayTextView = findViewById(R.id.displayTextView);

        // Set onClickListener to all buttons

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonSubtract).setOnClickListener(this);
        findViewById(R.id.buttonMultiply).setOnClickListener(this);
        findViewById(R.id.buttonDivide).setOnClickListener(this);
        findViewById(R.id.buttonDecimal).setOnClickListener(this);
        findViewById(R.id.buttonClear).setOnClickListener(this);
        findViewById(R.id.buttonEquals).setOnClickListener(this);

        findViewById(R.id.buttonCloseParenthesis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = displayTextView.getText().toString();
                if(str.length()!=0) {
                    str = str.substring(0, str.length() - 1);
// Now set this Text to your edit text
                    displayTextView.setText(str);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        // Get the current text in the displayTextView
        String currentText = displayTextView.getText().toString();

        // Get the button that was clicked
        Button button = (Button) v;

        // Get the text on the button
        String buttonText = button.getText().toString();

        switch (v.getId()) {
            case R.id.buttonClear:
                // Clear the displayTextView
                displayTextView.setText("");
                break;
            case R.id.buttonEquals:
                int n = currentText.length();
                if(currentText.length()==0){
                    Toast.makeText(getApplicationContext(),"type something",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (currentText.substring(currentText.length() - 1).equals("/") || currentText.substring(currentText.length() - 1).equals("*") || currentText.substring(currentText.length() - 1).equals("+") || currentText.substring(currentText.length() - 1).equals("-") || currentText.substring(currentText.length() - 1).equals(".")) {
                        Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                    } else {
                        // Evaluate the expression using BODMAS rule
                        String expression = displayTextView.getText().toString();
                        String result = String.valueOf(evaluateExpression(expression));
                        displayTextView.setText(result);
                    }
                }
                break;
            default:
                // Append the button text to the displayTextView
                displayTextView.setText(currentText + buttonText);
                break;
        }
    }

    private static boolean isDigit(char c) {
        return (c >= '0' && c <= '9') || c == '.';
    }

    private String evaluateExpression(String expression) {

        Vector<String> vec = new Vector<>();
        String k ="";
        for(int i=0;i<expression.length();i++){
            Log.i("real", String.valueOf(expression.charAt(i)));
            if(isDigit(expression.charAt(i))){
                k=k+expression.charAt(i);
                if(i==expression.length()-1){
                    vec.add(k);
                }
            }
            else{
                    vec.add(k);
                    k = "";
                k=k+expression.charAt(i);
                vec.add(k);
                k="";
            }

        }


        //Log.i("check",evaluatePostfix(infixToPostfix(vec)).toString());

        return evaluatePostfix(infixToPostfix(vec)).toString();

    }

    static int Prec(String ch)
    {
        switch (ch.charAt(0)) {
            case '+':
            case '-':
                return 1;

            case '*':
            case '/':
                return 2;

            case '^':
                return 3;
        }
        return -1;
    }

    static Double evaluatePostfix(Vector<String> exp)
    {
        // Create a stack
        Stack<Double> stack = new Stack<>();

        // Scan all characters one by one
        for (int i = 0; i < exp.size(); i++) {
            String c = exp.get(i);

            // If the scanned character is an operand
            // (number here), push it to the stack.
            if ((c.length()==1 && onlyDigits(c,c.length()))||c.length()!=1) {
                double d = Double.parseDouble(c);
                stack.push(d);
            }

                //  If the scanned character is an operator, pop
                //  two elements from stack apply the operator
            else {
                Double val1 = stack.pop();
                Double val2 = stack.pop();

                switch (c.charAt(0)) {
                    case '+':
                        stack.push(val2 + val1);
                        break;
                    case '-':
                        stack.push(val2 - val1);
                        break;
                    case '/':
                        stack.push(val2 / val1);
                        break;
                    case '*':
                        stack.push(val2 * val1);
                        break;
                }
            }
        }
        return stack.pop();
    }


    Vector<String>  infixToPostfix(Vector<String> exp)
    {
        // Initializing empty String for result
        Vector<String> result = new Vector<>();

        // Initializing empty stack
        Deque<String> stack
                = new ArrayDeque<String>();

        for (int i = 0; i < exp.size(); ++i) {
            String c = exp.get(i);

            // If the scanned character is an
            // operand, add it to output.
            if ((c.length()==1 && onlyDigits(c,c.length()))||c.length()!=1) {
                result.add(c);
               //  Log.i("final00",result.toString());
            }



                // If the scanned character is an '(',
                // push it to the stack.
//            else if (c.charAt(0) == '(') {
//                stack.push(c);
//            }
//
//                // If the scanned character is an ')',
//                // pop and output from the stack
//                // until an '(' is encountered.
//            else if (c.charAt(0) == ')') {
//
//                while (!stack.isEmpty() && stack.peek().charAt(0) != '(') {
//                    if(!Objects.equals(stack.peek(), "")) {
//                        result.add(stack.peek());
//                    }
//                    stack.pop();
//                }
//
//                stack.pop();
//            }

            // An operator is encountered
            else
            {
                while (!stack.isEmpty() && Prec(c) <= Prec(stack.peek())) {
                    if(stack.peek()!="") {
                        result.add(stack.peek());
                    }
                    stack.pop();
                }
                stack.push(c);
            }
        }
     //   Log.i("final00",result.toString());
        //Log.i("final11",stack.toString());

        // Pop all the operators from the stack
        while (!stack.isEmpty()) {
            if (stack.peek().charAt(0) == '('){
                Toast.makeText(MainActivity.this,"invalid input",Toast.LENGTH_SHORT).show();
            }

            result.add(stack.peek());
            stack.pop();
        }

     //   Log.i("final",result.toString());
        return result;
    }
    public static boolean onlyDigits(String str, int n)
    {

        // Traverse the string from
        // start to end
        for (int i = 0; i < n; i++) {

            // Check if the specified
            // character is a not digit
            // then return false,
            // else return false
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        // If we reach here that means all
        // the characters were digits,
        // so we return true
        return true;
    }
}