package com.buychat.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buychat.R;
import com.buychat.activities.CategoryListActivity;
import com.buychat.activities.CheckoutOptionsActivity;
import com.buychat.activities.OrderHistroyDetailsActivity;
import com.buychat.adapter.CheckOutAdapter;
import com.buychat.adapter.ItemsAdapter;
import com.buychat.api.Parse;
import com.buychat.app.BuyChat;
import com.buychat.extras.Communicate;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.listners.RecyclerItemClickListener;
import com.buychat.pojos.ProductPojos;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.AlertDialogManager;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by Snyxius Technologies on 8/16/2016.
 */
public class ItemsFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener ,SwipeRefreshLayout.OnRefreshListener {

    private ProgressDialog dialog;
    private Unbinder unbinder;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<ProductPojos> cartPOJOs;
    ItemsAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    int count = 0;
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    String string;
    Communicate communicate;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicate = (Communicate) activity;
    }

    static int positions;
    public static ItemsFragment newInstance(int position) {
        positions = position;
        Bundle args = new Bundle();
        ItemsFragment fragment = new ItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRecyclerView(view);
    }

    private void initializeRecyclerView(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_views);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        TypedValue typed_value = new TypedValue();
        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typed_value, true);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));
        if(!mSwipeRefreshLayout.isEnabled())
            mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        cartPOJOs = new ArrayList<>();
        String name = Constants.DEFAULT_STRING;
        if(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).size() != 0) {
            for (int i = 0; i < BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).size(); i++) {

                name += BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).get(i).getProduct_name()
                        + "(" + BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).get(i).getQuantity() + ") ";
            }
        }
        string = BuyChat.readFromPreferences(getActivity(),Keys.histroy_data,Constants.DEFAULT_STRING);
        count = BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).size();
        System.out.println(count);
        adapter = new ItemsAdapter(getActivity(), Parse.parseHistory(string),count,BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()),name) ;
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        if(position == 0 && BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).size() >= 1) {
            if (DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Movie_Tickets_CategoryId) ||
                    DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Hotels_CategoryId)) {
                if (BuyChat.isNetworkAvailable()) {
                    Log.d("str", BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).get(position).getProduct_image());
                    new ImageBitmap().execute(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).get(position).getProduct_image());

                } else {
                    BuyChat.showAToast(Constants.Internet);
                }
            } else {
                startActivityForResult(new Intent(getActivity(), CheckoutOptionsActivity.class)
                        .putExtra(Keys.position, positions), Constants.DEFAULT_INT);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }else{
            if(count >= Constants.INT_ONE){
                position = position-1;
            }
            DataSingleton.getInstance().setCartArray(Parse.parseHistory(string),position);
            System.out.println("Package Value : "+DataSingleton.getInstance().getCartArray().getPackage_value());
            System.out.println("Delivery Charge : "+DataSingleton.getInstance().getCartArray().getDelivery_charge());
            BuyChat.saveToPreferences(getActivity(),Keys.package_value,DataSingleton.getInstance().getCartArray().getPackage_value());
            BuyChat.saveToPreferences(getActivity(),Keys.delivery_charge,DataSingleton.getInstance().getCartArray().getDelivery_charge());
            startActivity(new Intent(getActivity(),OrderHistroyDetailsActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
        }
    }

    @Override
    public void onRefresh() {
            mSwipeRefreshLayout.setRefreshing(false);
    }



    private  class ImageBitmap extends AsyncTask<String,Void,Bitmap> {

        public ImageBitmap() {
        }
        ProgressDialog dialog =  new ProgressDialog(getActivity(),R.style.MyTheme);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AlertDialogManager.showDialog(dialog);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return BuyChat.getBitmapFromURL(params[Constants.DEFAULT_INT]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmaps) {
            super.onPostExecute(bitmaps);
                AlertDialogManager.dismissDialog(dialog);
            if(bitmaps != null){
                int nh = (int) ( bitmaps.getHeight() * (256.0 / bitmaps.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(bitmaps, 256, nh, true);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                scaled.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String str = Base64.encodeToString(byteArray, Base64.DEFAULT);
                BuyChat.dbHelper.UpdateProductImage(DataSingleton.getInstance().getData().getId(),str);
            }
            communicate.callChatFragment();
        }
    }


}
