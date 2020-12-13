package com.technest.needfood.intro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.technest.needfood.BuildConfig;
import com.technest.needfood.R;
import com.technest.needfood.admin.DashboardAdminActivity;
import com.technest.needfood.dapur.DashboardDapurActivity;
import com.technest.needfood.driver.DashboardDriverActivity;
import com.technest.needfood.models.intro.LoginModel;
import com.technest.needfood.network.ApiClient;
import com.technest.needfood.network.ApiInterface;
import com.technest.needfood.network.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tab_indikator;
    private RelativeLayout content_login;
    private RelativeLayout rl_footer;
    private Animation fromBottom;
    private SliderAdapter sliderAdapter;

    private TextView btn_masuk;
    private TextInputEditText tie_username;
    private TextInputEditText tie_password;

    private int[] bg_footer = null;
    private int[] bg_intro = null;

    private ProgressDialog pd;

    private SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences = getApplicationContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        String id = sharedpreferences.getString("ID", "");
        String role = sharedpreferences.getString("ROLE", "");
        if (!id.isEmpty()) {
            if (!id.equals("")) {
                if (role.equals("admin")) {
                    startActivity(new Intent(this, DashboardAdminActivity.class));
                    finish();
                } else if (role.equals("kitchen")) {
                    startActivity(new Intent(this, DashboardDapurActivity.class));
                    finish();
                } else if (role.equals("driver")) {
                    startActivity(new Intent(this, DashboardDriverActivity.class));
                    finish();
                } else {

                }
            }
        }

        pd = new ProgressDialog(this);

        Context context = LoginActivity.this;
        ConnectionDetector ConnectionDetector = new ConnectionDetector(
                context.getApplicationContext());

        content_login = findViewById(R.id.content_login);
        rl_footer = findViewById(R.id.rl_footer);
        tab_indikator = findViewById(R.id.tab_indikator);

        tie_username = findViewById(R.id.tie_username);
        tie_password = findViewById(R.id.tie_password);
        btn_masuk = findViewById(R.id.btn_masuk);

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = tie_username.getText().toString();
                String pass = tie_password.getText().toString();

                pd.setMessage("Proses ... ");
                pd.setCancelable(true);
                pd.show();

                // check Koneksi
                if (ConnectionDetector.isInternetAvailble()) {
                    proccesLogin(user, pass);
                } else {
                    pd.hide();
                    actionNotConnection();
                }

            }
        });

        fromBottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        rl_footer.setAnimation(fromBottom);

        //Array background
        final int[] background_footer = {

                R.drawable.footer_1,
                R.drawable.footer_2,
                R.drawable.footer_3

        };
        bg_footer = background_footer;
        final int[] background_intro = {
                R.drawable.bg_login_1,
                R.drawable.bg_login_2,
                R.drawable.bg_login_3
        };
        bg_intro = background_intro;


        //fill list screen
        final List<SliderItem> mList = new ArrayList<>();
        mList.add(new SliderItem("Admin",
                "Mengatur manajemen keuanagn, mengonfirmasi pesanan dan memantau Anjay-anjay " +
                        "yang ada",
                R.drawable.ic_android_black_24dp));

        mList.add(new SliderItem("Dapur",
                "Memastikan kebutuhan bahan makanan alat dan barang yang ingin di gunakan" +
                        " dan yang di pesan oleh pemesan",
                R.drawable.ic_android_black_24dp));

        mList.add(new SliderItem("Driver", "Mengefisienkan waktu untuk mengantarkan " +
                "pesanan para pelanggan",
                R.drawable.ic_android_black_24dp));

        // setup view pager
        final ViewPager screenPager = findViewById(R.id.view_pager);
        sliderAdapter = new SliderAdapter(this, mList);
        screenPager.setAdapter(sliderAdapter);

        tab_indikator.setupWithViewPager(screenPager);

        //tablayout add change listener
        tab_indikator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        screenPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {

                if (position < (sliderAdapter.getCount() - 1) && position < (bg_footer.length - 1) && position < (bg_intro.length - 1)) {

                    content_login.setBackgroundResource(background_intro[position]);
                    rl_footer.setBackgroundResource(background_footer[position]);


                } else {
                    content_login.setBackgroundResource(background_intro[background_intro.length - 1]);
                    rl_footer.setBackgroundResource(background_footer[background_footer.length - 1]);

                }

                position++;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void actionNotConnection() {
        Snackbar.make(findViewById(android.R.id.content), "Koneksi Tidak Ada!", Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .setAction("Close", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                    }
                })
                .show();
    }

    private void proccesLogin(String user, String pass) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginModel> loginModelCall = apiInterface.setLogin("Bearer " + BuildConfig.TOKEN, user, pass);
        loginModelCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                pd.hide();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isSuccess()) {
                        String id = String.valueOf(response.body().getId());
                        String role = response.body().getRole();
                        Log.d("LOGIN", "Message : " + response.body().getMessage());
                        editor = sharedpreferences.edit();
                        editor.putString("ID", id);
                        editor.putString("ROLE", role);
                        editor.apply();

                        switch (role) {
                            case "admin":
                                masukAdmin();
                                break;
                            case "kitchen":
                                masukDapur();
                                break;
                            case "driver":
                                masukDriver();
                                break;
                        }

                    } else {
                        Log.d("LOGIN", "Message = " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                pd.hide();
                actionNotConnection();
            }
        });

    }

    private void masukAdmin() {
        pd.hide();
        Intent intent = new Intent(LoginActivity.this, DashboardAdminActivity.class);
        startActivity(intent);
        finish();

    }

    private void masukDriver() {
        pd.hide();
        Intent intent = new Intent(LoginActivity.this, DashboardDriverActivity.class);
        startActivity(intent);
        finish();

    }

    private void masukDapur() {
        pd.hide();
        Intent intent = new Intent(LoginActivity.this, DashboardDapurActivity.class);
        startActivity(intent);
        finish();

    }
}
