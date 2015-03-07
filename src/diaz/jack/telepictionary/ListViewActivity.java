package diaz.jack.telepictionary;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.support.v7.app.ActionBarActivity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewActivity extends ListActivity implements OnClickListener {
	Bitmap originalBitmap;
	int turnNum;
	String gameName;
	Button menuBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		turnNum = 0;
		gameName = "";

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			turnNum = extras.getInt("TURN_NUM");
			gameName = extras.getString("GAME_NAME");
		}

		menuBtn = (Button)findViewById(R.id.menu_btn);
		menuBtn.setOnClickListener(this);
		
		ArrayList<Tuple> arrayList = new ArrayList<Tuple>();

		for(int i = 1; i < turnNum+1; i = i + 2){

			// text
			File sdcard = Environment.getExternalStorageDirectory();

			//Get the text file
			File file = new File(sdcard,"/"+gameName+"/"+i+".txt");

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
				Toast.makeText(ListViewActivity.this, "=[", Toast.LENGTH_LONG).show();
			}

			// image
			File f = new File(Environment.getExternalStorageDirectory(), "/"+gameName+"/"+(i+1)+".jpg");
			Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

			arrayList.add(new Tuple<String, Bitmap>(text.toString(), bitmap));

		}
		CustomArrayAdapter adapter = new CustomArrayAdapter(this, arrayList);

		setListAdapter(adapter);

	}

	private class CustomArrayAdapter extends ArrayAdapter<Tuple> {
		private final Context context;
		private final ArrayList<Tuple> arrayList;
		public CustomArrayAdapter(Context context, ArrayList<Tuple> objects) {
			super(context, R.layout.row_layout, objects);
			this.context = context;
			this.arrayList = objects;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.row_layout, parent, false);

			TextView textView1 = (TextView)rowView.findViewById(R.id.textView);
			ImageView imageView1 = (ImageView)rowView.findViewById(R.id.imageView1);

			Tuple object = arrayList.get(position);
			textView1.setText((String)object.getX());
			Drawable d = new BitmapDrawable(getResources(),(Bitmap)object.getY());
			imageView1.setImageDrawable(d);

			return rowView;
		}
	}

	public void onClick(View view){
		//respond to clicks  
		if(view.getId()==R.id.menu_btn){
			Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
			startActivity(menu);
		}
	}
}
