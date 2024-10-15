package com.example.duan1;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ActivityMainBinding;
import com.example.duan1.model.Account;
import com.example.duan1.model.Contract;
import com.example.duan1.model.Invoice;
import com.example.duan1.model.Member;
import com.example.duan1.model.Room;
import com.example.duan1.model.RoomType;
import com.example.duan1.model.Service;
import com.example.duan1.model.ServiceDetail;
import com.example.duan1.model.SessionAccount;
import com.example.duan1.model.Utility;
import com.example.duan1.model.UtilityDetail;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DbMotel db;
    ActivityResultLauncher<String[]> permissionRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SessionAccount sessionManage = new SessionAccount(this);
        sessionManage.dropAccount();
        db = DbMotel.getInstance(this);
        //Insert data
        try {
            if(db.accountDao().getAll().size() == 0) {
                //1. insert account
                db.accountDao().insert(new Account("admin", "admin", "App quản lý phòng trọ", "0123456789", "Chủ trọ", "file:///storage/emulated/0/Pictures/avatar3.jpg"));


                //2. insert utility
                db.utilityDao().insert(new Utility(1, "cần đặt cọc", ""));
                db.utilityDao().insert(new Utility(2, "giờ giấc tự do", ""));
                db.utilityDao().insert(new Utility(3, "có camera an ninh", ""));
                db.utilityDao().insert(new Utility(4, "được nấu ăn", ""));
                db.utilityDao().insert(new Utility(5, "có quạt", ""));
                db.utilityDao().insert(new Utility(6, "có giường", ""));
                db.utilityDao().insert(new Utility(7, "có tủ lạnh", ""));
                db.utilityDao().insert(new Utility(8, "có tivi", ""));
                db.utilityDao().insert(new Utility(9, "có wifi", ""));
                db.utilityDao().insert(new Utility(10, "có bãi để xe", ""));
                db.utilityDao().insert(new Utility(11, "có máy giặt", ""));
                db.utilityDao().insert(new Utility(12, "có điều hòa", ""));

                //3. Service
                db.serviceDao().insert(new Service(1, "bình nước", 15000, "file:///storage/emulated/0/Pictures/binh_nuoc.png"));

                //4. RoomType
                db.roomTypeDao().insert(new RoomType(1, "Loại 1", 1500000, 10, 3500, 20000));
                db.roomTypeDao().insert(new RoomType(2, "Loại 2", 2000000, 10, 3500, 20000));
                db.roomTypeDao().insert(new RoomType(3, "Loại 3", 2500000, 10, 4000, 25000));
                db.roomTypeDao().insert(new RoomType(4, "Loại 4", 3000000, 10, 4000, 25000));
                db.roomTypeDao().insert(new RoomType(5, "Loại 5", 3500000, 10, 3500, 25000));

                //5. UtilityDetail
                db.utilityDetailDao().insert(new UtilityDetail(1, 1));
                db.utilityDetailDao().insert(new UtilityDetail(1, 2));
                db.utilityDetailDao().insert(new UtilityDetail(1, 3));
                db.utilityDetailDao().insert(new UtilityDetail(1, 4));

                db.utilityDetailDao().insert(new UtilityDetail(2, 5));
                db.utilityDetailDao().insert(new UtilityDetail(2, 6));
                db.utilityDetailDao().insert(new UtilityDetail(2, 7));
                db.utilityDetailDao().insert(new UtilityDetail(2, 8));

                db.utilityDetailDao().insert(new UtilityDetail(3, 9));
                db.utilityDetailDao().insert(new UtilityDetail(3, 10));
                db.utilityDetailDao().insert(new UtilityDetail(3, 11));
                db.utilityDetailDao().insert(new UtilityDetail(3, 1));

                db.utilityDetailDao().insert(new UtilityDetail(4, 2));
                db.utilityDetailDao().insert(new UtilityDetail(4, 3));
                db.utilityDetailDao().insert(new UtilityDetail(4, 4));
                db.utilityDetailDao().insert(new UtilityDetail(4, 5));

                db.utilityDetailDao().insert(new UtilityDetail(5, 6));
                db.utilityDetailDao().insert(new UtilityDetail(5, 7));
                db.utilityDetailDao().insert(new UtilityDetail(5, 8));
                db.utilityDetailDao().insert(new UtilityDetail(5, 9));
                //6. Room
                db.roomDao().insert(new Room("P101", 1, "- Có sẵn nội thất cơ bản: Giường, Tủ, Tivi, Máy lạnh\n" +
                    "\n" +
                    "- Có chỗ nấu ăn\n" +
                    "\n" +
                    "- Có hầm để xe miễn phí\n" +
                    "\n" +
                    "- Có thang máy.\n" +
                    "\n" +
                    "- Điện: 4k/kg, nước 20k/khối\n" +
                    "\n" +
                    "- Internet free siêu mạnh.\n" +
                    "\n" +
                    "- Có sẵn máy giặt, tủ lạnh.", "", 1));
                db.roomDao().insert(new Room("P102", 1, "NHÀ VỆ SINH RIÊNG TỪNG PHÒNG\n" +
                    "\n" +
                    "✅ Giờ giấc tự do, không ở chung chủ\n" +
                    "\n" +
                    ". Địa chỉ: 72 Lê Thị Riêng, phường bến thành, Quận 1\n" +
                    "\n" +
                    ". An ninh, an toàn: + Trang bị hệ thống báo động khi quên đóng cửa nhà + Cửa vào nhà kiểm soát bằng vân tay + 12 camera an ninh 24/24 toàn bộ nhà + Wifi tốc độ cao modem 5G riêng mỗi phòng\n" +
                    "\n" +
                    "+ Gía thuê: Trọn gói 1,4 triệu/người (không bao gồm xe máy)\n" +
                    "\n" +
                    "+ Hợp đồng 06 tháng. Đặt cọc 2 triệu.", "", 2));
                db.roomDao().insert(new Room("P103", 1, "Cho thuê 2 phòng, mỗi phòng 25m\n" +
                    "\n" +
                    "Địa chỉ 16/55/16 Nguyễn thiện Thuật, p2, quận 3\n" +
                    "\n" +
                    "Toilet riêng. Dùng chung bếp và máy giặt\n" +
                    "\n" +
                    "Giá 5 triệu thương lượng\n" +
                    "\n" +
                    "LH 0768287986\n" +
                    "\n" +
                    "Vị trí trên bản đồ\n" +
                    "16/55/16 Đường Nguyễn Thiện Thuật, Phường 2, Quận 3, Hồ Chí Minh", "", 1));
                db.roomDao().insert(new Room("P104", 1, "Cho thuê 2 phòng, mỗi phòng 25m\n" +
                    "\n" +
                    "Địa chỉ 16/55/16 Nguyễn thiện Thuật, p2, quận 3\n" +
                    "\n" +
                    "Toilet riêng. Dùng chung bếp và máy giặt\n" +
                    "\n" +
                    "Giá 5 triệu thương lượng\n" +
                    "\n" +
                    "LH 0768287986\n" +
                    "\n" +
                    "Vị trí trên bản đồ\n" +
                    "16/55/16 Đường Nguyễn Thiện Thuật, Phường 2, Quận 3, Hồ Chí Minh", "", 1));
                db.roomDao().insert(new Room("P201", 2, "Cho thuê 2 phòng, mỗi phòng 25m\n" +
                    "\n" +
                    "Địa chỉ 16/55/16 Nguyễn thiện Thuật, p2, quận 3\n" +
                    "\n" +
                    "Toilet riêng. Dùng chung bếp và máy giặt\n" +
                    "\n" +
                    "Giá 5 triệu thương lượng\n" +
                    "\n" +
                    "LH 0768287986\n" +
                    "\n" +
                    "Vị trí trên bản đồ\n" +
                    "16/55/16 Đường Nguyễn Thiện Thuật, Phường 2, Quận 3, Hồ Chí Minh", "", 3));
                db.roomDao().insert(new Room("P202", 2, "Cho thuê 2 phòng, mỗi phòng 25m\n" +
                    "\n" +
                    "Địa chỉ 16/55/16 Nguyễn thiện Thuật, p2, quận 3\n" +
                    "\n" +
                    "Toilet riêng. Dùng chung bếp và máy giặt\n" +
                    "\n" +
                    "Giá 5 triệu thương lượng\n" +
                    "\n" +
                    "LH 0768287986\n" +
                    "\n" +
                    "Vị trí trên bản đồ\n" +
                    "16/55/16 Đường Nguyễn Thiện Thuật, Phường 2, Quận 3, Hồ Chí Minh", "", 4));
                db.roomDao().insert(new Room("P203", 2, "Cho thuê 2 phòng, mỗi phòng 25m\n" +
                    "\n" +
                    "Địa chỉ 16/55/16 Nguyễn thiện Thuật, p2, quận 3\n" +
                    "\n" +
                    "Toilet riêng. Dùng chung bếp và máy giặt\n" +
                    "\n" +
                    "Giá 5 triệu thương lượng\n" +
                    "\n" +
                    "LH 0768287986\n" +
                    "\n" +
                    "Vị trí trên bản đồ\n" +
                    "16/55/16 Đường Nguyễn Thiện Thuật, Phường 2, Quận 3, Hồ Chí Minh", "", 2));
                db.roomDao().insert(new Room("P204", 2, "Cho thuê 2 phòng, mỗi phòng 25m\n" +
                    "\n" +
                    "Địa chỉ 16/55/16 Nguyễn thiện Thuật, p2, quận 3\n" +
                    "\n" +
                    "Toilet riêng. Dùng chung bếp và máy giặt\n" +
                    "\n" +
                    "Giá 5 triệu thương lượng\n" +
                    "\n" +
                    "LH 0768287986\n" +
                    "\n" +
                    "Vị trí trên bản đồ\n" +
                    "16/55/16 Đường Nguyễn Thiện Thuật, Phường 2, Quận 3, Hồ Chí Minh", "", 2));
                db.roomDao().insert(new Room("P301", 3, "Cho thuê 2 phòng, mỗi phòng 25m\n" +
                    "\n" +
                    "Địa chỉ 16/55/16 Nguyễn thiện Thuật, p2, quận 3\n" +
                    "\n" +
                    "Toilet riêng. Dùng chung bếp và máy giặt\n" +
                    "\n" +
                    "Giá 5 triệu thương lượng\n" +
                    "\n" +
                    "LH 0768287986\n" +
                    "\n" +
                    "Vị trí trên bản đồ\n" +
                    "16/55/16 Đường Nguyễn Thiện Thuật, Phường 2, Quận 3, Hồ Chí Minh", "", 5));
                db.roomDao().insert(new Room("P302", 3, "Cho thuê 2 phòng, mỗi phòng 25m\n" +
                    "\n" +
                    "Địa chỉ 16/55/16 Nguyễn thiện Thuật, p2, quận 3\n" +
                    "\n" +
                    "Toilet riêng. Dùng chung bếp và máy giặt\n" +
                    "\n" +
                    "Giá 5 triệu thương lượng\n" +
                    "\n" +
                    "LH 0768287986\n" +
                    "\n" +
                    "Vị trí trên bản đồ\n" +
                    "16/55/16 Đường Nguyễn Thiện Thuật, Phường 2, Quận 3, Hồ Chí Minh", "", 3));
                db.roomDao().insert(new Room("P303", 3, "Cho thuê 2 phòng, mỗi phòng 25m\n" +
                    "\n" +
                    "Địa chỉ 16/55/16 Nguyễn thiện Thuật, p2, quận 3\n" +
                    "\n" +
                    "Toilet riêng. Dùng chung bếp và máy giặt\n" +
                    "\n" +
                    "Giá 5 triệu thương lượng\n" +
                    "\n" +
                    "LH 0768287986\n" +
                    "\n" +
                    "Vị trí trên bản đồ\n" +
                    "16/55/16 Đường Nguyễn Thiện Thuật, Phường 2, Quận 3, Hồ Chí Minh", "", 4));
                db.roomDao().insert(new Room("P304", 3, "Cho thuê 2 phòng, mỗi phòng 25m\n" +
                    "\n" +
                    "Địa chỉ 16/55/16 Nguyễn thiện Thuật, p2, quận 3\n" +
                    "\n" +
                    "Toilet riêng. Dùng chung bếp và máy giặt\n" +
                    "\n" +
                    "Giá 5 triệu thương lượng\n" +
                    "\n" +
                    "LH 0768287986\n" +
                    "\n" +
                    "Vị trí trên bản đồ\n" +
                    "16/55/16 Đường Nguyễn Thiện Thuật, Phường 2, Quận 3, Hồ Chí Minh", "", 4));

                //7. Contract
            }
        }catch (Exception e){
            Toast.makeText(this, "Loi insert du lieu vao database!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        requestPermissions();
    }

    private void requestPermissions() {
        TedPermission.Builder builderTed = TedPermission.create();
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Chú ý");
                builder.setMessage("Bạn cần cấp quyền thì mới sử dụng được ứng dụng");
                builder.setNegativeButton("Cấp quyến", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    builderTed.check();
                });
                builder.setPositiveButton("Thoát", (dialogInterface, i) -> System.exit(0));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
        builderTed.setPermissionListener(permissionlistener)
        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        .check();
    }

}
