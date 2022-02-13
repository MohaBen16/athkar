package mohaben.com.adhkar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mohaben.com.adhan.R;

public class MainActivity extends AppCompatActivity {

    private static final int[] BOTONS = {
            R.id.almasaaButton, R.id.alsabahButton, R.id.alsalatButton,
            R.id.almasjedButton, R.id.alnawmButton, R.id.wakeButton,
    };

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        for(int id : BOTONS) {
            final Button myButton = (Button)findViewById(id);
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, Read.class);
                    i.putExtra("time",getResources().getResourceEntryName(myButton.getId()));
                    startActivity(i);
                }
            });
        }
    }
}
