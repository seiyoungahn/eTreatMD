package com.exercise.seiyoung.exercise_etreatmd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PatientActivity extends AppCompatActivity {
    static final int CAMERA_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        Patient patient = (Patient) getIntent().getSerializableExtra("patient");
        TextView textView = (TextView) findViewById(R.id.id_patient_text);
        textView.setText("Name: " + patient.name + "\nID: " + patient.id + "");
        Button photoButton = (Button) findViewById(R.id.id_patient_button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == CAMERA_REQUEST){
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ImageView imageView = (ImageView) findViewById(R.id.id_patient_imageview);
            imageView.setImageBitmap(image);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
