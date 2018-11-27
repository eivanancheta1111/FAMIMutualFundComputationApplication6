package iics.ancheta.jeanroy.famimutualfundcomputationapplication;

import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {


    //Declaration EditTexts
    EditText editTextUserName;
    EditText editTextEmail;
    EditText editTextPassword;

    //Declaration TextInputLayout
    TextInputLayout textInputLayoutUserName;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;

    //Declaration Button
    Button buttonRegister, btnViewAll;

    //Declaration SqliteHelper
    DbHelper sqliteHelper;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sqliteHelper = new DbHelper(this);



        initTextViewLogin();
        initViews();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String UserName = editTextUserName.getText().toString();
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();

                    //Check in the database is there any user associated with  this email
                    if (!sqliteHelper.isEmailExists(Email)) {

                        //Email does not exist now add new user to database
                        sqliteHelper.addUser(new User(null, UserName, Email, Password));
                        Snackbar.make(buttonRegister, "User created successfully! Please Login ", Snackbar.LENGTH_LONG).show();

                    }else {

                        //Email exists with email input provided so show error user already exist
                        Snackbar.make(buttonRegister, "User already exists with same email ", Snackbar.LENGTH_LONG).show();
                    }


                }
            }
        });
        ViewAll();
    }

    //this method used to set Login TextView click event
    private void initTextViewLogin() {
        TextView textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutUserName = (TextInputLayout) findViewById(R.id.textInputLayoutUserName);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        btnViewAll= (Button) findViewById(R.id.bViewAll);

    }

    //This method is used to validate input given by user
    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String UserName = editTextUserName.getText().toString();
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        if(UserName.isEmpty() && Email.isEmpty() && Password.isEmpty() ){
            valid = false;
            textInputLayoutUserName.setError("Fill up the form");
            textInputLayoutEmail.setError("Fill up the form");
            textInputLayoutPassword.setError("Fill up the form");

        }
        else if(UserName.isEmpty() && Email.isEmpty() && !(Password.isEmpty())){
            valid = false;
            textInputLayoutUserName.setError("Fill up the form");
            textInputLayoutEmail.setError("Fill up the form");
            textInputLayoutPassword.setError(null);

        }
        else if(UserName.isEmpty() && !(Email.isEmpty()) && Password.isEmpty()){
            valid = false;
            textInputLayoutUserName.setError("Fill up the form");
            textInputLayoutEmail.setError(null);
            textInputLayoutPassword.setError("Fill up the form");

        }
        else if(UserName.isEmpty() && !(Email.isEmpty()) && !(Password.isEmpty())){
            valid = false;
            textInputLayoutUserName.setError("Please enter valid username");
            textInputLayoutEmail.setError(null);
            textInputLayoutPassword.setError(null);
        }
        else if(!(UserName.isEmpty()) && Email.isEmpty() && Password.isEmpty()){
            textInputLayoutUserName.setError(null);
            textInputLayoutEmail.setError("Fill up the form");
            textInputLayoutPassword.setError("Fill up the form");
        }
        else if(!(UserName.isEmpty()) && Email.isEmpty() && !(Password.isEmpty())){
            textInputLayoutUserName.setError(null);
            textInputLayoutEmail.setError("Please enter valid email address");
            textInputLayoutPassword.setError(null);
        }
        else if(!(UserName.isEmpty()) && !(Email.isEmpty()) && Password.isEmpty()){
            textInputLayoutUserName.setError(null);
            textInputLayoutEmail.setError(null);
            textInputLayoutPassword.setError("Please enter valid password");

        }
        //Complete Credentials
        else{
            //Email Address matches email
            //Password < 5
            //Username < 5
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                valid = false;
                textInputLayoutUserName.setError(null);
                textInputLayoutEmail.setError("Please enter valid email!");
                textInputLayoutPassword.setError(null);
            }
            else if(Password.length() < 5){
                valid = false;
                textInputLayoutUserName.setError(null);
                textInputLayoutEmail.setError(null);
                textInputLayoutPassword.setError("Password is too short");
            }
            else if(UserName.length() < 5){
                valid = false;
                textInputLayoutUserName.setError("Username is too short");
                textInputLayoutEmail.setError(null);
                textInputLayoutPassword.setError(null);
            }
            else{
                valid = true;
                textInputLayoutUserName.setError(null);
                textInputLayoutEmail.setError(null);
                textInputLayoutPassword.setError(null);

            }
        }


        return valid;
    }

    public void ViewAll(){
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = sqliteHelper.getAllData();
                if(res.getCount() == 0){
                    //show message
                    showMessage("Error", "Nothing Found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){
                    buffer.append("Id : " + res.getString(0) + "\n");
                    //buffer.append("Firstname : " + res.getString(1) + "\n");
                    //buffer.append("Lastname : " + res.getString(photo) + "\n");
                    buffer.append("Email : " + res.getString(2) + "\n");
                    buffer.append("Password : " + res.getString(3) + "\n\n");
                }

                //Show all data
                showMessage("Data", buffer.toString());
            }
        });
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
