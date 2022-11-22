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
import com.example.duan1.model.Utility;
import com.example.duan1.model.UtilityDetail;

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
        db = DbMotel.getInstance(this);
        //Insert data
        try {
            if(db.accountDao().getAll().size() == 0){
                //1. insert account
                db.accountDao().insert(new Account("admin","admin","Nguyễn Văn A","0123456789","host",""));
                db.accountDao().insert(new Account("user1","user1","Nguyễn Văn B","0123456789","manager",""));
                db.accountDao().insert(new Account("user2","user2","Nguyễn Văn C","0123456789","manager",""));
                db.accountDao().insert(new Account("user3","user3","Nguyễn Văn D","0123456789","manager",""));
                db.accountDao().insert(new Account("user4","user4","Nguyễn Văn E","0123456789","manager",""));

                //2. insert utility
                db.utilityDao().insert(new Utility(1,"cần đặt cọc",""));
                db.utilityDao().insert(new Utility(2,"giờ giấc tự do",""));
                db.utilityDao().insert(new Utility(3,"có camera an ninh",""));
                db.utilityDao().insert(new Utility(4,"được nấu ăn",""));
                db.utilityDao().insert(new Utility(5,"có quạt",""));
                db.utilityDao().insert(new Utility(6,"có giường",""));
                db.utilityDao().insert(new Utility(7,"có tủ lạnh",""));
                db.utilityDao().insert(new Utility(8,"có tivi",""));
                db.utilityDao().insert(new Utility(9,"có wifi",""));
                db.utilityDao().insert(new Utility(10,"có bãi để xe",""));
                db.utilityDao().insert(new Utility(11,"có máy giặt",""));
                db.utilityDao().insert(new Utility(12,"có điều hòa",""));

                //3. Service
                db.serviceDao().insert(new Service(1,"bình nước",15000,"file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg"));
                db.serviceDao().insert(new Service(2,"mỳ tôm trứng",15000,"file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg"));
                db.serviceDao().insert(new Service(3,"bánh mỳ trứng",15000,"file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg"));
                db.serviceDao().insert(new Service(4,"bình gas",15000,"file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg"));
                db.serviceDao().insert(new Service(5,"mỳ xào",15000,"file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg"));

                //4. RoomType
                db.roomTypeDao().insert(new RoomType(1,"Loại 1", 1500000,10,"file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg", 3500,20000));
                db.roomTypeDao().insert(new RoomType(2,"Loại 2", 2000000,10,"file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg", 3500,20000));
                db.roomTypeDao().insert(new RoomType(3,"Loại 3", 2500000,10,"file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg", 4000,25000));
                db.roomTypeDao().insert(new RoomType(4,"Loại 4", 3000000,10,"file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg", 4000,25000));
                db.roomTypeDao().insert(new RoomType(5,"Loại 5", 3500000,10,"file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg", 3500,25000));

                //5. UtilityDetail
                db.utilityDetailDao().insert(new UtilityDetail(1,1));
                db.utilityDetailDao().insert(new UtilityDetail(1,2));
                db.utilityDetailDao().insert(new UtilityDetail(1,3));
                db.utilityDetailDao().insert(new UtilityDetail(1,4));

                db.utilityDetailDao().insert(new UtilityDetail(2,5));
                db.utilityDetailDao().insert(new UtilityDetail(2,6));
                db.utilityDetailDao().insert(new UtilityDetail(2,7));
                db.utilityDetailDao().insert(new UtilityDetail(2,8));

                db.utilityDetailDao().insert(new UtilityDetail(3,9));
                db.utilityDetailDao().insert(new UtilityDetail(3,10));
                db.utilityDetailDao().insert(new UtilityDetail(3,11));
                db.utilityDetailDao().insert(new UtilityDetail(3,1));

                db.utilityDetailDao().insert(new UtilityDetail(4,2));
                db.utilityDetailDao().insert(new UtilityDetail(4,3));
                db.utilityDetailDao().insert(new UtilityDetail(4,4));
                db.utilityDetailDao().insert(new UtilityDetail(4,5));

                db.utilityDetailDao().insert(new UtilityDetail(5,6));
                db.utilityDetailDao().insert(new UtilityDetail(5,7));
                db.utilityDetailDao().insert(new UtilityDetail(5,8));
                db.utilityDetailDao().insert(new UtilityDetail(5,9));

                //6. Room
                db.roomDao().insert(new Room("P101", 1,"Phòng sạch sẽ thoáng mát" , 1 ));
                db.roomDao().insert(new Room("P102", 1,"Phòng sạch sẽ thoáng mát" , 2 ));
                db.roomDao().insert(new Room("P103", 1,"Phòng sạch sẽ thoáng mát" , 1 ));
                db.roomDao().insert(new Room("P201", 2,"Phòng sạch sẽ thoáng mát" , 3 ));
                db.roomDao().insert(new Room("P202", 2,"Phòng sạch sẽ thoáng mát" , 4 ));
                db.roomDao().insert(new Room("P203", 2,"Phòng sạch sẽ thoáng mát" , 2 ));
                db.roomDao().insert(new Room("P301", 3,"Phòng sạch sẽ thoáng mát" , 5 ));
                db.roomDao().insert(new Room("P302", 3,"Phòng sạch sẽ thoáng mát" , 3 ));
                db.roomDao().insert(new Room("P303", 3,"Phòng sạch sẽ thoáng mát" , 4 ));

                //7. Contract
                db.contractDao().insert(new Contract(1, "2022-11-15", "2023-05-15", 1,"P101"));
                db.contractDao().insert(new Contract(2, "2021-11-15", "2022-05-15", 0,"P102"));
                db.contractDao().insert(new Contract(3, "2022-11-15", "2023-05-15", 1,"P201"));
                db.contractDao().insert(new Contract(4, "2022-11-15", "2023-05-15", 1,"P202"));
                db.contractDao().insert(new Contract(5, "2022-11-15", "2023-05-15", 1,"P301"));

                //8. Invoice
                db.invoiceDao().insert(new Invoice(1,"2022-11-15",0,0,0,0,"",0,0,0,1,"admin"));
                db.invoiceDao().insert(new Invoice(2,"2021-11-15",0,10,0,50,"",0,1,2375000,2,"user1"));
                db.invoiceDao().insert(new Invoice(3,"2022-11-15",0,0,0,0,"",0,0,0,3,"user2"));
                db.invoiceDao().insert(new Invoice(4,"2022-11-15",0,0,0,0,"",0,0,0,4,"user3"));
                db.invoiceDao().insert(new Invoice(5,"2022-11-15",0,0,0,0,"",0,0,0,5,"user4"));

                //9. Member
                db.memberDao().insert(new Member(1,"Nguyễn Văn A", "2000-01-01","024020001101","file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg","0123456789","Thái Bình",1));
                db.memberDao().insert(new Member(2,"Nguyễn Văn B", "2000-02-02","024020001102","file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg","0123456789","Bắc Cạn",2));
                db.memberDao().insert(new Member(3,"Nguyễn Văn C", "2000-03-04","024020001103","file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg","0123456789","Bắc Giang",3));
                db.memberDao().insert(new Member(4,"Nguyễn Văn D", "2000-04-03","024020001104","file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg","0123456789","Bắc Ninh",4));
                db.memberDao().insert(new Member(5,"Nguyễn Văn E", "2000-05-05","024020001105","file:///storage/emulated/0/DCIM/Camera/IMG_20221115_085810.jpg","0123456789","Hà Nội",5));

                //10.ServiceDetail
                db.serviceDetailDao().insert(new ServiceDetail(1,1,"2022-11-17",1));
                db.serviceDetailDao().insert(new ServiceDetail(2,2,"2022-11-17",2));
                db.serviceDetailDao().insert(new ServiceDetail(3,3,"2022-11-17",3));
                db.serviceDetailDao().insert(new ServiceDetail(4,4,"2022-11-17",4));
                db.serviceDetailDao().insert(new ServiceDetail(5,5,"2022-11-17",5));
            }
        }catch (Exception e){
            Toast.makeText(this, "Loi insert du lieu vao database!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        permissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // No location access granted.
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Chú ý");
                        builder.setMessage("Bạn cần cấp quyền thì mới sử dụng được ứng dụng");
                        builder.setNegativeButton("Cấp quyến", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            permissionRequest.launch(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
                        });
                        builder.setPositiveButton("Thoát", (dialogInterface, i) -> System.exit(0));
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return;
                    }
                    startActivity(new Intent(this,RoomManageActivity.class));
        });
        permissionRequest.launch(new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE});
    }

}