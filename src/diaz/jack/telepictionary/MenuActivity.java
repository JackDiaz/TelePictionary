package diaz.jack.telepictionary;
import java.io.File;
import java.util.Random;

import diaz.jack.telepictionary.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class MenuActivity extends ActionBarActivity implements OnClickListener{
	Button startBtn, savedBtn;
	int numPlayers;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		startBtn = (Button)findViewById(R.id.start_btn);
		startBtn.setOnClickListener(this);

		savedBtn = (Button)findViewById(R.id.saved_btn);
		savedBtn.setOnClickListener(this);
		
		numPlayers = 0;
	}

	public void onClick(View view){
		//respond to clicks  
		if(view.getId()==R.id.start_btn){

			final AlertDialog.Builder gameNameAlert = new AlertDialog.Builder(this);
			gameNameAlert.setTitle("Please name this game.");
			final EditText strIn = new EditText(this);
			strIn.setInputType(InputType.TYPE_CLASS_TEXT);
			gameNameAlert.setView(strIn);
			gameNameAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String gameName = strIn.getText().toString().trim();
					File myDir=new File(Environment.getExternalStorageDirectory().getPath()+"/"+gameName);
					if (myDir.exists ()){
						Toast.makeText(getApplicationContext(), "That name already exists!", Toast.LENGTH_SHORT).show();
					}else{
						myDir.mkdirs();
						Intent sentence = new Intent(getApplicationContext(), SentenceActivity.class);
						sentence.putExtra("NUM_PLAYERS", numPlayers);
						sentence.putExtra("GAME_NAME", gameName);
						sentence.putExtra("TURN_NUM", 0);
						startActivity(sentence);
					}
				}
			});

			gameNameAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.cancel();
				}
			});
			final AlertDialog.Builder playerNumAlert = new AlertDialog.Builder(this);
			playerNumAlert.setTitle("How many players?");
			final EditText numIn = new EditText(this);
			numIn.setInputType(InputType.TYPE_CLASS_NUMBER);
			playerNumAlert.setView(numIn);
			playerNumAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = numIn.getText().toString().trim();
					numPlayers = Integer.parseInt(value);
					if(numPlayers < 3){
						Toast.makeText(getApplicationContext(), "There must be at least 3 players", Toast.LENGTH_SHORT).show();
					}else{
						gameNameAlert.show();
					}
				}
			});

			playerNumAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.cancel();
				}
			});
			playerNumAlert.show(); 

		}else if(view.getId()==R.id.saved_btn){
			Toast.makeText(MenuActivity.this, "Not yet implemented", Toast.LENGTH_LONG).show();
		}
	}

}
