package com.buychat.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.fragments.CategoryFragment;
import com.buychat.fragments.SubCategoryFragment;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.singleton.DataSingleton;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubCategoryListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener  {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private GridLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener scrolllistner;
    @BindView(R.id.checkout_button)
    RelativeLayout checkOutLayout;
    @BindViews({R.id.count,R.id.total_price})
    List<TextView> textViewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        ButterKnife.bind(this);
        initialize();

    }
    private void setCheckOutLayout(){
        int item = 0;
        float price = 0.0f;
        for (int i = 0; i < BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).size(); i++) {
            item += BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).get(i).getQuantity();
            price += (BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).get(i).getQuantity()
                    * Float.valueOf(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).get(i).getProduct_price()));
        }
        textViewList.get(Constants.DEFAULT_INT).setText(String.valueOf(item));
        textViewList.get(Constants.INT_ONE).setText(Constants.MONEY_ICON + String.valueOf(price));
        checkOutLayout.setVisibility(View.VISIBLE);
    }

    private void initialize() {
        setSupportActionBar(toolbar);
        setTitle(DataSingleton.getInstance().getCategoriesData().getSubcategory_name());
        getSupportActionBar().setSubtitle(DataSingleton.getInstance().getData().getBusiness_name());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Fragment fragment = new SubCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Keys.position,getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT));
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,fragment,Constants.SubCategoryFragment)
                .commit();

        if(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).isEmpty()) {
            checkOutLayout.setVisibility(View.GONE);
        }else {
            setCheckOutLayout();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.inside_menu, menu);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setOnQueryTextListener(this);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String query) {
        System.out.print("xx"+query);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if(fragment instanceof SubCategoryFragment){
            ((SubCategoryFragment) fragment).filter(query);
        }

        return true;

    }



    @OnClick({R.id.checkout_button}) void clickCheckOut(){
        startActivity(new Intent(getApplicationContext(), CheckoutOptionsActivity.class).putExtra(Keys.position,getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

}
