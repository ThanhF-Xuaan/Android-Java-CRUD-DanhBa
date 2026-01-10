package com.thanhlx.baithuchanh04_bai01;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManHinhChinhSua extends AppCompatActivity {
    EditText txtId, txtName, txtPhoneNumber;

    Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man_hinh_chinh_sua);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addViews();
    }

    private void addViews() {
        txtId = (EditText) findViewById(R.id.txtId);
        txtName = (EditText) findViewById(R.id.txtName);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);

        btnSave = (Button) findViewById(R.id.btSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        //dien thong tin cua ban ghi da chon tu man hinh chinh sang man hinh chinh sua
        txtId.setText(MainActivity.selectedContact.getId() + "");
        txtName.setText(MainActivity.selectedContact.getName());
        txtPhoneNumber.setText(MainActivity.selectedContact.getPhone_number());

        txtId.setEnabled(false);
    }

    public void huyChinhSuaDanhBa(View view) {
        finish();
    }

    public void luuDanhBaChinhSua(View view) {
        ContentValues values = new ContentValues();
        values.put("name", txtName.getText().toString());
        values.put("phone_number", txtPhoneNumber.getText().toString());

        int kq = MainActivity.database.update(
                "contact",
                values,
                "id=?",
                new String[]{txtId.getText().toString()});
        if(kq > 0){
            Toast.makeText(ManHinhChinhSua.this, "Update contact successfully!", Toast.LENGTH_LONG);
            finish();
        }
        else{
            Toast.makeText(ManHinhChinhSua.this, "Edit contact failed!", Toast.LENGTH_LONG);
        }
    }
}