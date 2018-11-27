package iics.ancheta.jeanroy.famimutualfundcomputationapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btnLogout;
    private Session session;
    private TextView fundTablePage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(this);
        fundTablePage = (TextView) findViewById(R.id.tablePage);
        if(!session.loggedin()){
            logout();
        }

        Bundle bn = getIntent().getExtras();
        String Username = bn.getString("TransferredUsername");
        final TextView UsernameText = (TextView) findViewById(R.id.tvUsername);
        UsernameText .setText(String.valueOf(Username));

        btnLogout = (Button)findViewById(R.id.signoutButton);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        DisplayFundTable();

    }

    public void DisplayFundTable() {
        fundTablePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(MainActivity.this,FundActivity.class);
                startActivity(newActivity);


            }
        });



    }
    public void MapPage(View v) {
        Uri gmmIntentUri = Uri.parse("geo:14.5576402,121.0201884");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if(mapIntent.resolveActivity(getPackageManager()) !=  null) {
            startActivity(mapIntent);
        }


    }
    private void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
}
