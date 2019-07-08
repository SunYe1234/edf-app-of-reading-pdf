package cn.xiaolong.pdfiumpdfviewer.loginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.xiaolong.pdfiumpdfviewer.MainActivity;
import cn.xiaolong.pdfiumpdfviewer.R;

public class DashboardActivity extends AppCompatActivity {

    String NameHolder;
    TextView Name;
    Button LogOUT ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

       Name = (TextView)findViewById(R.id.textView1);
        LogOUT = (Button)findViewById(R.id.button1);

        Intent intent = getIntent();

        // Receiving User Email Send By showDireActivity.
        NameHolder = intent.getStringExtra(MainActivity.userName);

        // Setting up received email to TextView.
        Name.setText(Name.getText().toString()+ NameHolder);

        // Adding click listener to Log Out button.
        LogOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Finishing current DashBoard activity on button click.
                finish();

                Toast.makeText(DashboardActivity.this,"Log Out Successfull", Toast.LENGTH_LONG).show();

            }
        });

    }
}