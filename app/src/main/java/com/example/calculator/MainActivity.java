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
                Log.i("curr",currentText.toString());
                if(currentText.length()==0){
                    Toast.makeText(getApplicationContext(),"type something",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (checkOperators(currentText)||currentText.substring(currentText.length() - 1).equals("/") || currentText.substring(currentText.length() - 1).equals("*") || currentText.substring(currentText.length() - 1).equals("+") || currentText.substring(currentText.length() - 1).equals("-") || currentText.substring(currentText.length() - 1).equals(".")) {
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
    public static boolean checkOperators(String str) {
        Log.i("string",str.toString());
        if (str.length() < 2) {
            return false;
        }
        char firstChar = str.charAt(0);
        if (isOperator(firstChar)) {
            return true;
        }
        for (int i = 0; i < str.length()-1; i++) {
            if (isOperator(str.charAt(i)) && isOperator(str.charAt(i+1))|| str.charAt(i)=='.'&&str.charAt(i+1)=='.') {
                return true;
            }
        }
        return false;
    }
    private static boolean isOperator(char c) {
        return c == '+' ||c=='-'||  c == '*' || c == '/';
    }

    private static boolean isDigit(char c) {
        return (c >= '0' && c <= '9') || c == '.';
    }

    private String evaluateExpression(String expression) {
        Vector<String> vec = new Vector<>();
        StringBuilder currentNumber = new StringBuilder();
        boolean decimalEntered = false; // Flag to track if a decimal point has already been entered

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (isDigit(c)) {
                if (c == '.') {
                    if (decimalEntered) {
                        // Display error message or throw an exception for multiple decimal points
                        showToast("Invalid input: Multiple decimal points in a number");
                        return ""; // Or handle the error in an appropriate way
                    }
                    decimalEntered = true;
                }
                currentNumber.append(c);
            } else if (isOperator(c)) {
                if (currentNumber.length() > 0) {
                    vec.add(currentNumber.toString());
                    currentNumber.setLength(0);
                    decimalEntered = false; // Reset the decimal flag for the next number
                }
                vec.add(String.valueOf(c));
            }
        }

        if (currentNumber.length() > 0) {
            vec.add(currentNumber.toString());
        }

        return evaluatePostfix(infixToPostfix(vec)).toString();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

    static Double evaluatePostfix(Vector<String> exp) {
        // Create a stack
        Stack<Double> stack = new Stack<>();

        // Scan all characters one by one
        for (int i = 0; i < exp.size(); i++) {
            String c = exp.get(i);

            // If the scanned character is a valid number, push it to the stack
            if (isNumber(c)) {
                double d = Double.parseDouble(c);
                stack.push(d);
            } else {
                // If the scanned character is an operator, pop two elements from the stack and apply the operator
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid postfix expression");
                }
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
                    default:
                        throw new IllegalArgumentException("Invalid operator: " + c);
                }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression");
        }

        return stack.pop();
    }

    static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
    public static boolean onlyDigits(String str, int n) {
        // Check if the string is empty or null
        if (str == null || str.isEmpty()) {
            return false;
        }

        // Count the number of decimal points
        int decimalCount = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '.') {
                decimalCount++;
                if (decimalCount > 1) {
                    return false; // More than one decimal point found
                }
            } else if (!Character.isDigit(c)) {
                return false; // Found a non-digit character
            }
        }

        // Check if the string length exceeds the limit
        return str.length() <= n;
    }

}