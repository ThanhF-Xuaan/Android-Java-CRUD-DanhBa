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

public class ManHinhThem extends AppCompatActivity {
    EditText txtId, txtName, txtPhoneNumber;

    Button btnSave, btnDelete, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man_hinh_them);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnSave), (v, insets) -> {
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
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnCancel = (Button) findViewById(R.id.btnCancel);
    }

    public void luuDanhBa(View view) {
        //buoc 1: lay du lieu tren form
        int id = Integer.parseInt(txtId.getText().toString());
        String name = txtName.getText().toString();
        String phoneNumber = txtPhoneNumber.getText().toString();

        //buoc 2: tao value de luu tru doi tuong
        //ContentValues: Đây là một lớp đặc biệt của Android dùng để lưu trữ dữ liệu theo cặp Key-Value (Khóa - Giá trị).
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("phone_number", phoneNumber);

        //buoc 3: them danh ba vao co so du lieu
        //Tham số thứ 2 (nullColumnHack): Khi để null, Android sẽ không cho phép chèn một dòng hoàn toàn trống vào bảng.
        long kq = MainActivity.database.insert("contact", null, values);
        if(kq > 0){
            Toast.makeText(ManHinhThem.this, "Them thanh cong", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(ManHinhThem.this, "Them khong thanh cong", Toast.LENGTH_LONG).show();
        }
    }

    public void xoaDuLieuNhap(View view) {
        txtId.setText("");
        txtId.setHint("Enter Id");

        txtName.setText("");
        txtName.setHint("Enter Name");

        txtPhoneNumber.setText("");
        txtPhoneNumber.setHint("Enter Phone Number");

        txtId.requestFocus();
    }

    public void huyThemDanhBa(View view) {
        finish();
    }
}