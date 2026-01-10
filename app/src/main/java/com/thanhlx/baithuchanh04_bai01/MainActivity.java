package com.thanhlx.baithuchanh04_bai01;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.thanhlx.model.Contact;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private ListView lsvDanhBa;
    ArrayAdapter<Contact> adapter;
    private String DATABASE_NAME = "Contacs.db";
    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;

    public static Contact selectedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnSave), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        processCopy();

        addVies();

        hienThiDanhBa();

        addEvents();
    }

    private void addEvents() {
        lsvDanhBa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedContact = adapter.getItem(position);
            }
        });

        lsvDanhBa.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedContact = adapter.getItem(position);
                //false: Sau khi thực hiện lệnh gán dữ liệu, hệ thống sẽ coi như sự kiện này chưa kết thúc.
                // Điều này cho phép Android tiếp tục hiển thị các menu phụ (như Context Menu - menu nổi lên để chọn Xóa/Sửa) nếu bạn có cài đặt.
                return false;
            }
        });
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
//        database.close();
    }

    private void addVies() {
        lsvDanhBa = (ListView) findViewById(R.id.lsvDanhBa);
        //android.R.layout.simple_list_item_1: Đây là một file giao diện có sẵn của hệ thống Android.
        // Nó định nghĩa rằng mỗi dòng trong danh sách sẽ chỉ là một dòng chữ đơn giản (TextView).
        adapter = new ArrayAdapter<Contact>(MainActivity.this, android.R.layout.simple_list_item_1);
        lsvDanhBa.setAdapter(adapter);

        registerForContextMenu(lsvDanhBa);
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

    //them menu option

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Lệnh này thực hiện việc "bơm" (inflate) các mục menu từ file XML vào thanh tiêu đề.
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

    //lua chon option nao cua menu option

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnuThem){
            //Tạo một "bản tin" (Intent) để thông báo cho hệ thống rằng bạn muốn đi từ màn hình hiện tại (MainActivity.this)
            // sang màn hình mới (ManHinhThem.class).
            Intent intent = new Intent(MainActivity.this, ManHinhThem.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mnuChinhSua){
            if(selectedContact != null){
                Intent intent = new Intent(MainActivity.this, ManHinhChinhSua.class);
                startActivity(intent);
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Gọi lại hàm hiển thị để cập nhật dữ liệu mới nhất từ SQLite
        hienThiDanhBa();
    }
}