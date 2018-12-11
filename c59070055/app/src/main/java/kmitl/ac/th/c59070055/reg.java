package kmitl.ac.th.c59070055;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class reg extends Fragment {
    private static final String TAG = "REGISTER";

    private SQLiteDatabase myDB;
    private Account account;
    private ProgressDialog kLoading;

    private EditText kUsername;
    private EditText kPassword;
    private EditText kFullname;
    private EditText kage;
    private Button kSignup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_reg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myDB = getActivity().openOrCreateDatabase("my.db", Context.MODE_PRIVATE, null);
        account = Account.getAccountInstance();
        kLoading = new ProgressDialog(getActivity());
        registerFragment();
        initseva();
    }
    private void registerFragment(){
        Log.d(TAG, "Get Elements");
        kUsername = getView().findViewById(R.id.register_user);
        kPassword = getView().findViewById(R.id.register_password);
        kFullname = getView().findViewById(R.id.register_name);
        kage = getView().findViewById(R.id.register_age);
        kSignup = getView().findViewById(R.id.register_register_btn);
    }
    private void initseva() {
        kSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Signup : clicked");
                kLoading.setMessage("กำลังสมัครสมาชิก...");
                kLoading.setCanceledOnTouchOutside(false);
                kLoading.setCancelable(false);
                kLoading.show();

                String username = kUsername.getText().toString();
                String password = kPassword.getText().toString();
                String fullname = kFullname.getText().toString();
                String kage1 = kage.getText().toString();
                int age = Integer.parseInt(kage1);

                if (username.isEmpty() || password.isEmpty() || fullname.isEmpty() || kage1.isEmpty()) {
                    Log.d(TAG, "field is empty");
                    Toast.makeText(getActivity(), "กรุณาใส่ข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
                    kLoading.dismiss();
                } else if (password.length() < 6) {
                    Log.d(TAG, "password more than 6");
                    Toast.makeText(getActivity(), "รหัสผ่านต้องมากกว่า 6 ตัวอักษร", Toast.LENGTH_SHORT).show();
                    kLoading.dismiss();
                } else if (username.length() < 6 || username.length() > 12) {
                    Log.d(TAG, "password more than 6");
                    Toast.makeText(getActivity(), "userต้องมากกว่า 6 และน้อยกว่า 12 ตัวอักษร", Toast.LENGTH_SHORT).show();
                    kLoading.dismiss();
                } else if (age < 10 || age > 80) {
                    Log.d(TAG, "Both passwords must be the same");
                    Toast.makeText(getActivity(), "อายุคุณต้องอยู่ระหว่าง 10 - 80 ปี", Toast.LENGTH_SHORT).show();
                    kLoading.dismiss();
                } else {
                    Cursor cursor = myDB.rawQuery("select * from account where username = '" + username + "'", null);
                    if (cursor.getCount() != 1) {
                        ContentValues registerAccount = new ContentValues();
                        registerAccount.put("username", username);
                        registerAccount.put("password", password);

                        Log.d(TAG, "Insert new account");
                        myDB.insert("account", null, registerAccount);
                        account.setUsername(username);
                        account.setPassword(password);

                        Cursor cursorPrimaryid = myDB.rawQuery("select * from account where username = '" + username + "'", null);
                        while (cursorPrimaryid.moveToNext()) {
                            ContentValues registerMy = new ContentValues();
                            registerMy.put("fullname", fullname);
                            registerMy.put("age", kage1);
                            registerMy.put("account_id", cursorPrimaryid.getInt(0));
                            myDB.insert("my", null, registerMy);
                            account.setPrimaryid(cursorPrimaryid.getInt(0));
                            account.setFullname(fullname);
                            account.setAge(kage1);
                        }
                        cursorPrimaryid.close();
                        cursor.close();
                        Toast.makeText(getActivity(), "สมัครสมาชิกเรียบร้อย", Toast.LENGTH_LONG).show();
                        kLoading.dismiss();

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new login()).commit();
                    } else {
                        Toast.makeText(getActivity(), "username นี้มีผู้ใช้อื่นใช้แล้ว", Toast.LENGTH_LONG).show();
                        cursor.close();
                        kLoading.dismiss();
                    }
                }

            }
        });
    }
}
