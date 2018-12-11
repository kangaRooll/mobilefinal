package kmitl.ac.th.c59070055;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final String isLogin = "loginStatus";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase myDB = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS account (id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(200), password VARCHAR(200))");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS my (id INTEGER PRIMARY KEY AUTOINCREMENT, fullname VARCHAR(200), age VARCHAR(200), account_id INTEGER)");
        SharedPreferences loginCheck = MainActivity.this.getSharedPreferences(isLogin, Context.MODE_PRIVATE);
        Boolean isLogin = loginCheck.getBoolean("isLogin", false);
        if(isLogin) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new WelcomeFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_view, new login()).commit();
        }
    }
}
