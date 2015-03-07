package diaz.jack.telepictionary;
//SLIDE UP~~~~~~~~~~~~~~~~~~~~~~~~~

/*
 * 	Toast.makeText(MainActivity.this, "Your Message", Toast.LENGTH_LONG).show();
 */
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View.OnClickListener;

public class DrawActivity extends ActionBarActivity implements OnClickListener{
	private DrawingView drawView;
	private float smallBrush, mediumBrush, largeBrush;
	private Button drawBtn, newBtn, saveBtn, colorBtn, nextBtn;
	private ImageButton currPaint;
	OnAmbilWarnaListener listener;
	int colorInt = 0xFF660000;
	int numPlayers;
	int turnNum;
	String gameName;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw);

		numPlayers = 0;
		turnNum = 0;
		gameName = "";

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			numPlayers = extras.getInt("NUM_PLAYERS")-1;
			turnNum = extras.getInt("TURN_NUM")+1;
			gameName = extras.getString("GAME_NAME");
		}

		drawView = (DrawingView)findViewById(R.id.drawing);
		drawView.setBrushSize(mediumBrush);

		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);

		drawBtn = (Button)findViewById(R.id.draw_btn);
		drawBtn.setOnClickListener(this);

		newBtn = (Button)findViewById(R.id.new_btn);
		newBtn.setOnClickListener(this);

		colorBtn = (Button)findViewById(R.id.color_btn);
		colorBtn.setOnClickListener(this);

		nextBtn = (Button)findViewById(R.id.next_btn);
		nextBtn.setOnClickListener(this);
		
		File sdcard = Environment.getExternalStorageDirectory();

		//Get the text file
		File file = new File(sdcard,"/"+gameName+"/"+(turnNum - 1)+".txt");

		//Read text from file
		StringBuilder text = new StringBuilder();

		try {
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;

		    while ((line = br.readLine()) != null) {
		        text.append(line);
		        text.append('\n');
		    }
		    br.close();
		}
		catch (IOException e) {
			Toast.makeText(DrawActivity.this, "~~~~~", Toast.LENGTH_LONG).show();
		}
		tv = (TextView)findViewById(R.id.tv);
		tv.setText(text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void paintClicked(View view){
		//use chosen color

		if(view!=currPaint){
			//update color
			ImageButton imgView = (ImageButton)view;
			//color = view.getTag().toString();
			//drawView.setColor(color);
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
			currPaint=(ImageButton)view;
		}
	}

	@Override
	public void onClick(View view){
		//respond to clicks  
		if(view.getId()==R.id.draw_btn){
			//draw button clicked
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Brush size:");
			brushDialog.setContentView(R.layout.brush_chooser);

			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(smallBrush);
					drawView.setLastBrushSize(smallBrush);
					brushDialog.dismiss();
				}
			});

			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(mediumBrush);
					drawView.setLastBrushSize(mediumBrush);
					brushDialog.dismiss();
				}
			});

			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(largeBrush);
					drawView.setLastBrushSize(largeBrush);
					brushDialog.dismiss();
				}
			});

			brushDialog.show();
		}else if(view.getId()==R.id.new_btn){
			//new button
			AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
			newDialog.setTitle("New drawing");
			newDialog.setMessage("Start new drawing? \nYou will lose the current drawing");
			newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					drawView.startNew();
					dialog.dismiss();
				}
			});
			newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					dialog.cancel();
				}
			});
			newDialog.show();
			/*	}else if(view.getId()==R.id.save_btn){
			//save drawing
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("Save drawing to device Gallery?");
			saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					//save drawing
					drawView.setDrawingCacheEnabled(true);
					String imgSaved = MediaStore.Images.Media.insertImage(
							getContentResolver(), drawView.getDrawingCache(),
							UUID.randomUUID().toString()+".png", "drawing");
					if(imgSaved!=null){
						Toast savedToast = Toast.makeText(getApplicationContext(), 
								"Drawing saved to Gallery!", Toast.LENGTH_SHORT);
						savedToast.show();
					}
					else{
						Toast unsavedToast = Toast.makeText(getApplicationContext(), 
								"Oops! Image could not be saved.", Toast.LENGTH_SHORT);
						unsavedToast.show();
					}
					drawView.destroyDrawingCache();
				}
			});
			saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					dialog.cancel();
				}
			});
			saveDialog.show();*/
		}else if(view.getId()==R.id.color_btn){

			AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, colorInt, new OnAmbilWarnaListener() {
				@Override
				public void onOk(AmbilWarnaDialog dialog, int color) {
					// color is the color selected by the user.
					drawView.setColor(color);
					colorInt = color;

				}

				@Override
				public void onCancel(AmbilWarnaDialog dialog) {
					// cancel was selected by the user
				}
			});

			dialog.show();


		}else if(view.getId()==R.id.next_btn){
			//save drawing
			File myDir=new File(Environment.getExternalStorageDirectory().getPath()+"/"+gameName);
			String fname = turnNum+".jpg";
			File file = new File (myDir, fname);
			if (file.exists ()){
				file.delete (); 
			}
			int width = drawView.getWidth();
		    int height = drawView.getHeight();

		    int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
		    int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

		    //Cause the view to re-layout
		    drawView.measure(measuredWidth, measuredHeight);
		    //drawView.layout(0, 0, drawView.getMeasuredWidth(), drawView.getMeasuredHeight());

		    //Create a bitmap backed Canvas to draw the view into
		    Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		    Canvas c = new Canvas(b);

		    //Now that the view is laid out and we have a canvas, ask the view to draw itself into the canvas
		    drawView.draw(c);
		    
			try {
				FileOutputStream out = new FileOutputStream(file);
				b.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
				if(numPlayers != 0){
					Intent sentence = new Intent(getApplicationContext(), SentenceActivity.class);
					sentence.putExtra("NUM_PLAYERS", numPlayers);
					sentence.putExtra("GAME_NAME", gameName);
					sentence.putExtra("TURN_NUM", turnNum);
					startActivity(sentence);
				}else{
					Intent list = new Intent(getApplicationContext(), ListViewActivity.class);
					list.putExtra("GAME_NAME", gameName);
					list.putExtra("TURN_NUM", turnNum);
					startActivity(list);
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Image could not be saved.", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

			drawView.destroyDrawingCache();



		}
	}



}
