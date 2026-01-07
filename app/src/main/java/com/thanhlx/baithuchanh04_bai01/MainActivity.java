package com.thanhlx.baithuchanh04_bai01;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.thanhlx.model.Contact;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lsvDanhBa;
    ArrayAdapter<Contact> adapter;
    private String DATABASE_NAME = "Contacs.db";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        processCopy();

        addVies();

        hienThiDanhBa();
    }

    private void hienThiDanhBa() {
        //ket noi voi csdl
        //MODE_PRIVATE: Chế độ này đảm bảo rằng tệp cơ sở dữ liệu chỉ có thể được truy cập bởi ứng dụng của bạn (tăng tính bảo mật).
        //null: Bo tao cursor mac dinh cua he thong
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        //tao dau doc de doc du lieu trong bang
        //null: Khong co tham so loc
        Cursor cursor = database.rawQuery("SELECT * FROM contact", null);

        //doc du lieu va hien thi len giao dien
        //xóa sạch dữ liệu cũ đang hiển thị trên ListView
        adapter.clear();

        while (cursor.moveToNext()){
            //doc du lieu trong co so du lieu
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String phone_number = cursor.getString(2);
            Log.d("DEBUG_DB", "Số điện thoại vừa đọc: " + phone_number);
            byte[] avt = cursor.getBlob(3);

            //tao object Contact
            Contact c = new Contact(id, name, phone_number, avt);

            //gan doi tuong vao adapter;
            adapter.add(c);
        }

        cursor.close();
        database.close();
    }

    private void addVies() {
        lsvDanhBa = (ListView) findViewById(R.id.lsvDanhBa);
        //android.R.layout.simple_list_item_1: Đây là một file giao diện có sẵn của hệ thống Android.
        // Nó định nghĩa rằng mỗi dòng trong danh sách sẽ chỉ là một dòng chữ đơn giản (TextView).
        adapter = new ArrayAdapter<Contact>(MainActivity.this, android.R.layout.simple_list_item_1);
        lsvDanhBa.setAdapter(adapter);
    }

    //ham thuc hien copy vao dien thoai
    private void processCopy() {
        try {
            File db_file = getDatabasePath(DATABASE_NAME);
            //neu khong ton tai thi copy
            if(!db_file.exists()){
                copyDatabaseFromAssets();
//                Toast: Là một lớp (class) trong thư viện Android dùng để tạo ra các thông báo nhỏ,
//                nổi lên trên cùng của giao diện và tự động biến mất sau một khoảng thời gian ngắn mà không cần người dùng tương tác.
//                .makeText(...): Đây là một phương thức tĩnh (static method) dùng để khởi tạo đối tượng Toast với các thiết lập cụ thể.
//                MainActivity.this (Context): Đây là tham số về "ngữ cảnh". Android cần biết thông báo này hiển thị ở đâu (Activity nào).
//                Toast.LENGTH_LONG (Duration): Thời gian hiển thị. Android cung cấp 2 hằng số mặc định:
                //LENGTH_LONG: Hiển thị khoảng 3.5 giây.
                //LENGTH_SHORT: Hiển thị khoảng 2 giây.
                Toast.makeText(MainActivity.this, "Copy database file to mobile successfully"
                , Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex){
            //hien thi len man hinh
            Toast.makeText(MainActivity.this, "Error: " + ex.toString(), Toast.LENGTH_LONG).show();
            Log.e("Error", ex.toString());
        }
    }

    //ham lay duong dan file database
    private String getDatabasePath(){
        //getApplicationInfo().dataDir: Đây là phương thức trả về đường dẫn đến thư mục dữ liệu gốc của ứng dụng trên thiết bị.
        //Thông thường nó sẽ có dạng: /data/user/0/tên.gói.ứng.dụng/ (ví dụ: /data/user/0/com.example.myproject/).
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    //ham copy csdl tu assets
    private void copyDatabaseFromAssets(){
        try {
            InputStream myInput = getAssets().open(DATABASE_NAME);
            String outFileName = getDatabasePath();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!f.exists()){
                f.mkdir();
            }

            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while((length = myInput.read(buffer)) > 0){
                myOutput.write(buffer, 0, length); //Do du lieu tu buffer vao Internal Storage. Bat dau ghi tu 0 den het length.
            }

            myOutput.flush(); //Ép tất cả dữ liệu còn sót lại trong bộ đệm phải ghi hết xuống đĩa.
            myOutput.close();
            myInput.close();
        }
        catch (Exception ex){
            //ex.toString(): Tên loại lỗi + Thông điệp chi tiết.
            Log.e("Error: ", ex.toString());
        }
    }
}