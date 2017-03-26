package tonydarko.mykhai.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import java.util.Arrays;

import tonydarko.mykhai.R;
import tonydarko.mykhai.Utils.Constant;
import tonydarko.mykhai.ui.fragments.DisciplineFragment;
import tonydarko.mykhai.ui.fragments.ExtraBallFragment;
import tonydarko.mykhai.ui.fragments.OnlineVoteFragment;
import tonydarko.mykhai.ui.fragments.RatingFragment;
import tonydarko.mykhai.ui.fragments.SchedullerFragment;
import tonydarko.mykhai.ui.fragments.SettingsFragment;
import tonydarko.mykhai.ui.fragments.StudentBallsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences setting;
    private Toolbar toolbar;
    Boolean noOrYes;
    TextView tv1, tv2, nameHead;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noOrYes = Constant.getNoOrYes();
        setActionBar();
        setDrawerLayout();
        tv1 = (TextView) findViewById(R.id.tv1);
        View headerView = navigationView.getHeaderView(0);
        nameHead = (TextView) headerView.findViewById(R.id.header_username);

        if (noOrYes) {
            tv1.setText(Constant.getInfo());
            String[] str = Constant.getInfo().split(",");
            String[] str2 = str[1].split("!");
            System.out.println(Arrays.toString(str2));
            nameHead.setText(str2[0]);
        } else {
            tv1.setText("Для перегляду приватної інформаціі необхідно здійснити вхід");
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_container);
                if (f != null) {
                    updateToolbarTitle(f);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            allertDialogFunc();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        switch (item.getItemId()) {
            case R.id.drawer_rating:
                if (noOrYes) {
                    RatingFragment ratingFragment = new RatingFragment();
                    tv1.setVisibility(View.INVISIBLE);
                    replaceFragment(ratingFragment);
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Необхідно увійти!", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
                break;
            case R.id.drawer_student_ball:
                if (noOrYes) {
                    StudentBallsFragment studentBallsFragment = new StudentBallsFragment();
                    tv1.setVisibility(View.INVISIBLE);
                    replaceFragment(studentBallsFragment);
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Необхідно увійти!", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
                break;
            case R.id.drawer_extra_ball:
                tv1.setVisibility(View.INVISIBLE);
                ExtraBallFragment extraBallFragment = new ExtraBallFragment();
                replaceFragment(extraBallFragment);
                break;
            case R.id.drawer_vibir:
                tv1.setVisibility(View.INVISIBLE);
                DisciplineFragment disciplineFragment = new DisciplineFragment();
                replaceFragment(disciplineFragment);
                break;
            case R.id.drawer_online_vote:
                tv1.setVisibility(View.INVISIBLE);
                OnlineVoteFragment onlineVoteFragment = new OnlineVoteFragment();
                replaceFragment(onlineVoteFragment);
                break;
            case R.id.drawer_scheduler:
                tv1.setVisibility(View.INVISIBLE);
                SchedullerFragment schedullerFragment = new SchedullerFragment();
                replaceFragment(schedullerFragment);
                break;
            case R.id.drawer_settings:
                tv1.setVisibility(View.INVISIBLE);
                SettingsFragment settingsFragment = new SettingsFragment();
                replaceFragment(settingsFragment);
                break;
            case R.id.drawer_exit:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Вийти?");

                alertDialog.setMessage("Ви дійсно бажаете вийти?");

                alertDialog.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Intent intent = new Intent(MainActivity.this, LogginActivity.class);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        setting = getSharedPreferences("LogPass", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = setting.edit();
                        editor.putString("Login", "");
                        editor.putString("Password", "");
                        System.out.println("Saved");
                        editor.apply();
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                });

                alertDialog.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
                break;
        }
        return true;
    }

    public void allertDialogFunc(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Вийти?");

        alertDialog.setMessage("Ви дійсно бажаете вийти?");

        alertDialog.setPositiveButton("Так", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        alertDialog.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        setTitle("Оцінки студента ХАІ");
    }


    private void replaceFragment(Fragment fragment) {
        String backStackName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStackName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStackName) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.setCustomAnimations( R.anim.right_in, R.anim.left_out );
            ft.replace(R.id.main_container, fragment, backStackName);
            ft.addToBackStack(backStackName);
            ft.commit();
        }
    }

    private void updateToolbarTitle(Fragment fragment) {
        String fragmentClassName = fragment.getClass().getName();

        if (fragmentClassName.equals(ExtraBallFragment.class.getName())) {
            setTitle("Додаткові бали");
            navigationView.setCheckedItem(R.id.drawer_extra_ball);
        } else if (fragmentClassName.equals(SchedullerFragment.class.getName())) {
            setTitle("Розклад занять за вибором");
            navigationView.setCheckedItem(R.id.drawer_scheduler);
        } else if (fragmentClassName.equals(RatingFragment.class.getName())) {
            setTitle("Рейтинг на стипендію");
            navigationView.setCheckedItem(R.id.drawer_rating);
        } else if (fragmentClassName.equals(StudentBallsFragment.class.getName())) {
            setTitle("Оцінки студента");
            navigationView.setCheckedItem(R.id.drawer_student_ball);
        } else if (fragmentClassName.equals(DisciplineFragment.class.getName())) {
            setTitle("Вибір дисципліни");
            navigationView.setCheckedItem(R.id.drawer_vibir);
        } else if (fragmentClassName.equals(OnlineVoteFragment.class.getName())) {
            setTitle("Онлайн вибір");
            navigationView.setCheckedItem(R.id.drawer_online_vote);
        } else if (fragmentClassName.equals(SettingsFragment.class.getName())) {
            setTitle("Налаштування");
            navigationView.setCheckedItem(R.id.drawer_settings);
        }
    }

}