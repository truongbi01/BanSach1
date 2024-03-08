package com.example.banhang.View.fragment;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.banhang.R;

public class Rate extends AppCompatActivity {
    TextView titlerate, resultrate;
    Button btnfeedback;
    ImageView charPlace;
    RatingBar rateStarts;
    String answerValue;
    String soSao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rate);
       titlerate=findViewById(R.id.titlerate);
       resultrate=findViewById(R.id.resultrate);
       btnfeedback=findViewById(R.id.btnfeedback);
       charPlace=findViewById(R.id.charPlace);
       rateStarts=  findViewById(R.id.rateStarts);
        rateStarts.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                answerValue=String.valueOf((int) (rateStarts.getRating()));
                if(answerValue.equals("1")){
                    charPlace.setImageResource(R.drawable.rate1);
                    resultrate.setText("Bad");
                    soSao = "1";
                }
                else if(answerValue.equals("2")){
                    charPlace.setImageResource(R.drawable.rate2);
                    resultrate.setText("Not bad");
                }
                else if(answerValue.equals("3")){
                    charPlace.setImageResource(R.drawable.rate3);
                    resultrate.setText("Normal");
                }
                else if(answerValue.equals("4")){
                    charPlace.setImageResource(R.drawable.rate4);
                    resultrate.setText("Good job");
                }
                else if(answerValue.equals("5")){
                    charPlace.setImageResource(R.drawable.rate5);
                    resultrate.setText("Awesome");
                }
                else{
                    Toast.makeText(getApplicationContext(),"No point",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
