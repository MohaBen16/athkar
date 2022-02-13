package mohaben.com.adhkar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;

import mohaben.com.adhan.R;

public class Read extends AppCompatActivity {

    String fileName;
    ImageView iv_img ;
    Drawable drawable;
    public int total_tikrar ,total_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athkars);

        total_tikrar= 0;
        total_read=0;

        iv_img=findViewById(R.id.img);

        fileName = (String) getIntent().getExtras().getString("time");

        switch (fileName){
            case "almasaaButton":
                drawable = getResources().getDrawable(R.drawable.almasaa);
                fileName = "athkar_almasaa.xml";
            break;
            case "alsabahButton":
                drawable = getResources().getDrawable(R.drawable.alsabah);
                fileName = "athkar_alsabah.xml";
                break;
            case "alsalatButton":
                drawable = getResources().getDrawable(R.drawable.alsalat);
                fileName = "athkar_alsalat.xml";
                break;
            case "alnawmButton":
                drawable = getResources().getDrawable(R.drawable.alnawm);
                fileName = "athkar_alnawm.xml";
                break;
            case "almasjedButton":
                drawable = getResources().getDrawable(R.drawable.almasjed);
                fileName = "athkar_almasjed.xml";
                break;
            case "wakeButton":
                drawable = getResources().getDrawable(R.drawable.wake);
                fileName = "athkar_wake.xml";
                break;
            default:
        }

        iv_img.setBackground(drawable);

        parseXml(fileName);
    }

    public void parseXml(String file){

        XmlPullParserFactory parseFactory;

        try{
            parseFactory = XmlPullParserFactory.newInstance();

            XmlPullParser parser = parseFactory.newPullParser();
            InputStream is=getAssets().open(file);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(is,null);
            processParsing(parser);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void processParsing(XmlPullParser parser){

        ArrayList<Athkar> athkars = new ArrayList<>();

        try {
            int evenType = parser.getEventType();
            Athkar currentAthkar = null;

            while (evenType != XmlPullParser.END_DOCUMENT) {

                String eltName = null;

                switch (evenType) {
                    case XmlPullParser.START_TAG:
                        eltName = parser.getName();
                        if ("athkar".equals(eltName)) {
                            currentAthkar = new Athkar();
                            athkars.add(currentAthkar);
                        } else if (currentAthkar != null) {
                            if ("text".equals(eltName)) {
                                currentAthkar.text = parser.nextText();
                            } else if ("number".equals(eltName)) {
                                currentAthkar.number = parser.nextText();
                            }
                        }
                    break;
                }
                evenType = parser.next();
            }
            printAthkars(athkars);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void printAthkars(ArrayList<Athkar> athkars){

        LinearLayout.LayoutParams params_number = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params_text = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params_number.setMargins(20,0,20,20);
        params_text.setMargins(20,20,20,0);


        LinearLayout list = (LinearLayout)findViewById(R.id.list);

        for(Athkar athkar : athkars){

            final TextView textView = new TextView(this);
            final TextView textNumber = new TextView(this);

            textView.setText(""+athkar.text);
            textNumber.setText("الكترار: "+athkar.number);

            setViewProperties(textView,textNumber,list,params_text,params_number);

            total_tikrar += Integer.parseInt(athkar.number);
            total_read = total_tikrar;

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int tikrar = Integer.parseInt(getNummber(textNumber.getText().toString()));
                    total_tikrar --;

                    if(total_tikrar == 0 ){
                        Toast.makeText(getApplicationContext(), "Total Athkars Read: "+total_read, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Read.this, MainActivity.class);
                        startActivity(i);
                    }else{
                        if(tikrar == 1){
                            textView.setVisibility(View.GONE);
                            textNumber.setVisibility(View.GONE);
                        }else{
                            textNumber.setText("الكترار: "+((tikrar)-1));
                        }
                    }
                }
            });
        }
    }


    public void setViewProperties(TextView text,TextView number,LinearLayout linear, LinearLayout.LayoutParams p_text, LinearLayout.LayoutParams p_number){

        text.setBackground(getResources().getDrawable(R.drawable.textview_border));
        text.setPadding(30,30,30,30);
        text.setLayoutParams(p_text);
        text.setTextSize(20);

        number.setBackground(getResources().getDrawable(R.drawable.textnumber_border));
        number.setTextColor(getResources().getColor(R.color.colorWhite));
        number.setPadding(20,10,50,10);
        number.setLayoutParams(p_number);
        number.setTextSize(20);

        linear.addView(text);
        linear.addView(number);
    }

    public static String getNummber(String text){

        char[] text_div = text.toCharArray();

        String n ="";

        for (int i=0; i<text_div.length; i++){
            if(Character.isDigit(text_div[i])){
                n+=text_div[i];
            }
        }
        return n;
    }
}
