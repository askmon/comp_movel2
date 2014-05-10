package usp.ime.movel.brickbreaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        Button play_button = (Button) findViewById(R.id.play_button);
        play_button.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.play_button:
			Intent i = new Intent(this, GameActivity.class);
			startActivity(i);
			break;
		}
	}
}