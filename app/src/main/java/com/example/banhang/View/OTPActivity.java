package com.example.banhang.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.banhang.R;

public class OTPActivity extends AppCompatActivity {
    EditText edtOTP1,edtOTP2,edtOTP3,edtOTP4 ;
    ProgressBar progressBar;
    TextView tvSoDienThoai,tvGuiLaiOTP;
    Button btnVerify;

    private boolean guiLai = false;

    private  int thoiGianGuiLai = 60;
    private  int selectedETPosition = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        AnhXa();
        SharedPreferences sharedPreferences = this.getSharedPreferences("ThongTinNguoiDung", Context.MODE_PRIVATE);
        String soDienThoai = sharedPreferences.getString("soDienThoai", "");
        tvSoDienThoai.setText(soDienThoai);

        edtOTP1.addTextChangedListener(textWatcher);
        edtOTP2.addTextChangedListener(textWatcher);
        edtOTP3.addTextChangedListener(textWatcher);
        edtOTP4.addTextChangedListener(textWatcher);


        //Bắt đầu gửi lại OTP

        batDauThoiGianGuiLai();

        tvGuiLaiOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(guiLai){
                    batDauThoiGianGuiLai();
                }
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kiemTraOTP = edtOTP1.getText().toString()+edtOTP2.getText().toString()+edtOTP3.getText().toString()+edtOTP4.getText().toString()+edtOTP1.getText().toString();
                if(kiemTraOTP.length() == 4){
                    // Gọi phương thức xác nhận OTP ở đây
                    xacNhanOTP(kiemTraOTP);
                }
            }
        });

    }

    private void xacNhanOTP(String kiemTraOTP) {
    }

    private  void showKeyboard(EditText otp){
        otp.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otp,InputMethodManager.SHOW_IMPLICIT);

    }
    void batDauThoiGianGuiLai(){
        guiLai = false;
        tvGuiLaiOTP.setTextColor(Color.BLACK);
        new CountDownTimer(thoiGianGuiLai * 1000,1000){

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                tvGuiLaiOTP.setText("Gửi Lại OTP ("+(millisUntilFinished / 1000)+")");
            }

            @Override
            public void onFinish() {
                guiLai = true;
                tvGuiLaiOTP.setText("Gửi lại OTP");
                tvGuiLaiOTP.setTextColor(getResources().getColor(R.color.grey));
            }
        }.start();
    }
    void AnhXa(){
        edtOTP1 = findViewById(R.id.edtOTP1);
        edtOTP2 = findViewById(R.id.edtOTP2);
        edtOTP3 = findViewById(R.id.edtOTP3);
        edtOTP4 = findViewById(R.id.edtOTP4);
        tvSoDienThoai = findViewById(R.id.tvSoDienTHoai_otp);
        progressBar = findViewById(R.id.progressbar_Verify);
        tvGuiLaiOTP = findViewById(R.id.tvGuilaiOTP);
        btnVerify = findViewById(R.id.btnXacNhan_Verify);
    }
    private  final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() > 0 ){
                if(selectedETPosition == 0){
                    selectedETPosition = 1;
                    showKeyboard(edtOTP2);
                } else if (selectedETPosition == 1) {
                    selectedETPosition = 2;
                    showKeyboard(edtOTP3);
                } else if (selectedETPosition == 2) {
                    selectedETPosition = 3;
                    showKeyboard(edtOTP4);
                }
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DEL){
            if(selectedETPosition == 3){
                selectedETPosition = 2;
                showKeyboard(edtOTP3);
            } else if (selectedETPosition == 2) {
                selectedETPosition = 1;
                showKeyboard(edtOTP2);
            } else if (selectedETPosition == 1) {
                selectedETPosition = 0;
                showKeyboard(edtOTP1);
            }
            return  true;
        }
        else {
            return super.onKeyUp(keyCode, event);

        }
    }
}
