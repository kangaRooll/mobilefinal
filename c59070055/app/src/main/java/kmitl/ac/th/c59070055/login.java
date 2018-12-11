package kmitl.ac.th.c59070055;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

public class login extends Fragment {

    private static final String TAG = "LOGIN";
    private static final String isLogin = "loginStatus";
    private SharedPreferences loginCheck;
    private SharedPreferences.Editor loginChange;

    private SQLiteDatabase myDB;
    private Account account;

    private EditText zUsername;
    private EditText zPassword;
    private Button zSignin;
    private TextView zSignup;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        account = Account.getAccountInstance();
        myDB = getActivity().openOrCreateDatabase("my.db", Context.MODE_PRIVATE, null);
        loginFragmentElements();
        initSignup();
        initSignin();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lay_login, container, false);
    }
    private void loginFragmentElements() {
        Log.d(TAG, "Get Elements");
        zUsername = getView().findViewById(R.id.login_user_id);
        zPassword = getView().findViewById(R.id.login_password);
        zSignin = getView().findViewById(R.id.login_login_btn);
        zSignup = getView().findViewById(R.id.login_register_new_account_text);
    }
    private void initSignup() {
        zSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Signup : clicked");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new reg()).addToBackStack(null).commit();
            }
        });
    }

    private void initSignin() {
        zSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = zUsername.getText().toString();
                String password = zPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "กรุณาใส่ข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor loginCursorCheck = myDB.rawQuery("select * from account where username = '" + username + "' and password = '" + password + "'", null);
                    if (loginCursorCheck.getCount() > 0) {
                        loginCursorCheck.move(1);
                        account.setPrimaryid(loginCursorCheck.getInt(0));
                        account.setUsername(loginCursorCheck.getString(1));
                        account.setPassword(loginCursorCheck.getString(2));

                        Cursor myCheck = myDB.rawQuery("select * from my where account_id = '" + account.getPrimaryid() + "'", null);
                        if (myCheck.getCount() > 0) {
                            myCheck.move(1);
                            account.setFullname(myCheck.getString(1));
                            account.setAge(myCheck.getString(2));

                            myCheck.close();
                            loginCursorCheck.close();

                            loginCheck = getActivity().getSharedPreferences(isLogin, Context.MODE_PRIVATE);
                            loginChange = loginCheck.edit();
                            loginChange.putString("accountId", String.valueOf(account.getPrimaryid()));
                            loginChange.putBoolean("isLogin", true);
                            loginChange.commit();

                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new WelcomeFragment()).commit();
                        }
                    } else {
                        Toast.makeText(getActivity(), "ไม่พบข้อมูลผู้ใช้", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
