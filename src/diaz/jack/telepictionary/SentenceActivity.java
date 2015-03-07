package diaz.jack.telepictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class SentenceActivity extends ActionBarActivity implements OnClickListener {
	Button nextBtn;
	int numPlayers;
	int turnNum;
	String gameName;
	File f;
	Bitmap bitmap;
	ImageView img;
	EditText et;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sentence);
		
		numPlayers = 0;
		turnNum = 0;
		gameName = "";
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			numPlayers = extras.getInt("NUM_PLAYERS")-1;
			turnNum = extras.getInt("TURN_NUM")+1;
			gameName = extras.getString("GAME_NAME");
		}		


		nextBtn = (Button)findViewById(R.id.next_btn);
		nextBtn.setOnClickListener(this);
		
		

		f = new File(Environment.getExternalStorageDirectory(), gameName+"/"+(turnNum-1)+".jpg");
		bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

		img = (ImageView)findViewById(R.id.imageView1);

		Drawable d = new BitmapDrawable(getResources(),bitmap);
		img.setImageDrawable(d);
		
		et = (EditText)findViewById(R.id.editText);
	}

	public void onClick(View view){
		//respond to clicks  
		if(view.getId()==R.id.next_btn){
			try {
	            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/"+gameName+"/"+turnNum+".txt");
	            myFile.createNewFile();
	            FileOutputStream fOut = new FileOutputStream(myFile);
	            OutputStreamWriter myOutWriter = 
	                                    new OutputStreamWriter(fOut);
	            myOutWriter.append(et.getText());
	            myOutWriter.close();
	            fOut.close();
				if(numPlayers != 0){
					Intent draw = new Intent(getApplicationContext(), DrawActivity.class);
					draw.putExtra("NUM_PLAYERS", numPlayers);
					draw.putExtra("GAME_NAME", gameName);
					draw.putExtra("TURN_NUM", turnNum);
					startActivity(draw);
				}else{
					Intent list = new Intent(getApplicationContext(), ListViewActivity.class);
					list.putExtra("GAME_NAME", gameName);
					list.putExtra("TURN_NUM", turnNum);
					startActivity(list);
				}
	        } catch (Exception e) {
	            Toast.makeText(getBaseContext(), e.getMessage(),
	                    Toast.LENGTH_SHORT).show();
	        }

		}
	}
}
