package com.buychat.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.extras.AddressReload;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.fragments.AddAddressFragment;
import com.buychat.fragments.AddressListFragment;
import com.buychat.pojos.Address;
import com.buychat.singleton.DataSingleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class  DeliveryAddressListActivity extends AppCompatActivity implements AddressReload{

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        initialize();
        setupWindowAnimations();
        setupLayout();

    }

    private void initialize() {
        setSupportActionBar(toolbar);
        setTitle(DataSingleton.getInstance().getData().getBusiness_name());
        getSupportActionBar().setSubtitle("Share Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }



    @Override
    public void onBackPressed() {
        System.out.println("WORKING");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.sample2_content);
        if(fragment instanceof AddressListFragment){
            System.out.println("LIST");
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }else{
            System.out.println("EDIT<ADD");
            getSupportFragmentManager().popBackStack();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }
//    @OnClick(R.id.add_an_address)
//    public void addAnAddress(){
//
//    }

    private void setupWindowAnimations() {
        // We are not interested in defining a new Enter Transition. Instead we change default transition duration
        if (Build.VERSION.SDK_INT >= 21 ) {
            getWindow().getEnterTransition().setDuration(getResources().getInteger(R.integer.anim_duration_long));
        }
    }

    private void setupLayout() {
        // Transition for fragment1
        AddressListFragment addressListFragment=new AddressListFragment();
        Bundle args = new Bundle();
        args.putInt(Keys.position,getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT));
        addressListFragment.setArguments(args);
        if (Build.VERSION.SDK_INT >= 21 ) {
            Slide slideTransition = new Slide(Gravity.BOTTOM);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            // Create fragment and define some of it transitions
            addressListFragment.setReenterTransition(slideTransition);
            addressListFragment.setExitTransition(slideTransition);
            addressListFragment.setSharedElementEnterTransition(new ChangeBounds());
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sample2_content, addressListFragment, Constants.AddressListFragment)
                .commit();
    }


    @Override
    public void onAddAddress() {
       setupLayout();
    }
}
