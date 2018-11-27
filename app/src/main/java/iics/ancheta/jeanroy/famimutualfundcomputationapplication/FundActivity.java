package iics.ancheta.jeanroy.famimutualfundcomputationapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FundActivity extends AppCompatActivity {
    private Session session;
    private Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund);

        session = new Session(this);
        /**if(!session.loggedin()){
            logout();
        }
        btnLogout = (Button)findViewById(R.id.signoutButton);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });*/
    }
    /**
    private void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(FundActivity.this,LoginActivity.class));
    }*/
}
