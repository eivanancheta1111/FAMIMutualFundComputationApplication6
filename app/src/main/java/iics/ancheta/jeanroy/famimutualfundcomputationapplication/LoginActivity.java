package iics.ancheta.jeanroy.famimutualfundcomputationapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button login, register;
    private EditText etEmail, etPass;
    private DbHelper db;
    private Session session;

    //Declaration TextInputLayout
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DbHelper(this);

        login = (Button)findViewById(R.id.buttonLogin);
        etEmail = (EditText)findViewById(R.id.editTextEmail);
        etPass = (EditText)findViewById(R.id.editTextPassword);
        initCreateAccountTextView();
        initViews();
        login.setOnClickListener(this);

        session = new Session(this);
        if(session.loggedin()){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonLogin:
                login();
                break;
            default:

        }
    }

    private void login(){
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();

        if (validate()) {
            //Get values from EditText fields
            String Email = etEmail.getText().toString();
            String Password = etPass.getText().toString();

            //Authenticate user
            User currentUser = db.Authenticate(new User(null, null, Email, Password));

            //Check Authentication is successful or not
            if (currentUser != null) {
                //Snackbar.make(buttonLogin, "Successfully Logged in!", Snackbar.LENGTH_LONG).show();
                Cursor res = db.getUsername(Email);
                session.setLoggedin(true);
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){
                    buffer.append("" + res.getString(0));
                }
                String StoredUsername = buffer.toString();


                //User Logged in Successfully Launch You home screen activity

                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("TransferredUsername",  StoredUsername);
                startActivity(intent);
                finish();


            } else {

                //User Logged in Failed
                Snackbar.make(login, "Failed to log in , please try again", Snackbar.LENGTH_LONG).show();

            }
        }
    }


    //this method used to set Create account TextView text and click event( maltipal colors
    // for TextView yet not supported in Xml so i have done it programmatically)
    private void initCreateAccountTextView() {
        TextView textViewCreateAccount = (TextView) findViewById(R.id.textViewCreateAccount);
        textViewCreateAccount.setText(fromHtml("<font color='#ffffff'>I don't have account yet. </font><font color='#0c0099'>create one</font>"));
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etPass = (EditText) findViewById(R.id.editTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        login = (Button) findViewById(R.id.buttonLogin);

    }

    //This method is for handling fromHtml method deprecation
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    //This method is used to validate input given by user
    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String Email = etEmail.getText().toString();
        String Password = etPass.getText().toString();

        //Handling validation for Email field
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            textInputLayoutEmail.setError("Please enter valid email!");
        } else {
            valid = true;
            textInputLayoutEmail.setError(null);
        }

        //Handling validation for Password field
        if (Password.isEmpty()) {
            valid = false;
            textInputLayoutPassword.setError("Please enter valid password!");
        } else {
            if (Password.length() > 5) {
                valid = true;
                textInputLayoutPassword.setError(null);
            } else {
                valid = false;
                textInputLayoutPassword.setError("Password is to short!");
            }
        }

        return valid;
    }


    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
