package com.buychat.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buychat.activities.ChatMainActivity;
import com.buychat.R;
import com.buychat.activities.MainCategoryDetailActivity;
import com.buychat.activities.ProfileActivity;
import com.buychat.adapter.ChatListAdapter;
import com.buychat.adapter.FashionAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.AddToList;
import com.buychat.extras.Constants;
import com.buychat.extras.CustomClickListner;
import com.buychat.extras.Keys;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.listners.RecyclerItemClickListener;
import com.buychat.pojos.CategoriesPojos;
import com.buychat.pojos.Chat;
import com.buychat.pojos.ProductPojos;
import com.buychat.pojos.ShopsPojo;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.chats.Status;
import com.buychat.utils.chats.UserType;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class ChatView extends Fragment implements Callback<JsonObject>,SwipeRefreshLayout.OnRefreshListener,CustomClickListner,RecyclerItemClickListener.OnItemClickListener {
    public static ChatView newInstance() {
        //   position = pos;
        //    arraylistCats = categoriesPojoses;
        //     mainposition = positions;
        Bundle args = new Bundle();
        ChatView fragment = new ChatView();
        fragment.setArguments(args);
        return fragment;
    }
    private ProgressDialog dialog;
    private Unbinder unbinder;
    RecyclerView recyclerView;
    @BindView(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener scrolllistner;
    ChatListAdapter adapter;
    int position;
    int mainposition;
    ArrayList<Chat> arrayList;


    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    CallSecondPage mcCallSecondPage;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mcCallSecondPage = (CallSecondPage) activity;
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
        initializeRefreshView(view);
        ApiCall();
    }


    private void ApiCall(){
//        WebRequests service1 = BuyChat.initializeRetrofit().create(WebRequests.class);
//        Call<JsonObject> call1 = service1.chat_list(Parse.getAccessTokenWithLimitOffset(Constants.DEFAULT_INT,Constants.INT_TEN));
//        call1.enqueue(this);
        mSwipeRefreshLayout.setRefreshing(false);
        arrayList = new ArrayList<>();
        adapter = new ChatListAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            arrayList.addAll(BuyChat.dbHelper.getAllChat());
            adapter.notifyDataSetChanged();


    }


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        mSwipeRefreshLayout.setRefreshing(false);
        arrayList = new ArrayList<>();
        if(response.code() == Constants.SUCCESS) {

        }else {
            BuyChat.showAToast(Constants.Some_Went_Wrong);
        }

//
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void ScrollApiCall(int current_page) {
        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call = service.chat_list(Parse.getAccessTokenWithLimitOffset(
                Constants.INT_TEN*current_page+Constants.INT_ONE,Constants.INT_TEN));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ArrayList<ShopsPojo> array = new ArrayList<>();
                if(response.code() == Constants.SUCCESS) {
                    if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                        array = Parse.parseChat(response.body().toString());
                    }
                }
//                if(array.size() != 0) {
//                    arrayList.addAll(array);
//                    adapter.notifyDataSetChanged();
//                }else{
//                    adapter.enableFooter(false);
//                    adapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }





    @Override
    public void onRefresh() {

        ApiCall();
    }

    private void initializeRefreshView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        TypedValue typed_value = new TypedValue();
        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typed_value, true);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));
        if(!mSwipeRefreshLayout.isEnabled())
            mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_views);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));

    }


    @Override
    public void imageClickListner(int position, View view) {

    }

    @Override
    public void itemClickListner(int position, View view) {
        BuyChat.saveToPreferences(getActivity(),Keys.merchant_id,arrayList.get(position).getMerchant_id());
        BuyChat.saveToPreferences(getActivity(),Keys.business_name,arrayList.get(position).getBusiness_name());
        BuyChat.saveToPreferences(getActivity(),Keys.merchant_image,arrayList.get(position).getMerchant_image());
        BuyChat.saveToPreferences(getActivity(),Keys.from,arrayList.get(position).getFrom());
        BuyChat.dbHelper.UpdateUnReadChat((arrayList.get(position).getMerchant_id()));
        startActivityForResult(new Intent(getActivity(),ChatMainActivity.class),Constants.DEFAULT_INT);
        getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void setData(JSONObject data){
        try{
            final Chat message = new Chat();
            message.setMessageStatus(Status.SENT);
            message.setImage(data.getString(Keys.image));
            message.setMessageText(data.getString(Keys.message));
            message.setFrom(data.getString(Keys.from));
            message.setUserType(UserType.SELF);
            message.setMessageTime(new Date().getTime());
            message.setMerchant_id(data.getString(Keys.merchant_id));
            message.setBusiness_name(data.getString(Keys.buisness_name));
            message.setMerchant_image(data.getString(Keys.merchant_image));
            message.setFlag("1");
            message.setCount(0);
            BuyChat.dbHelper.InsertChat(message);
            notifyData();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void notifyData(){
        ArrayList<Chat> arrayLists = new ArrayList<>();
        arrayLists = BuyChat.dbHelper.getAllChat();
        int message_count=0;
        int merchant_count=0;
        for(int i=0;i<arrayLists.size();i++){
            if(arrayLists.get(i).getFlag().equals("1")) {
                merchant_count++;
            }
            for(int j=0;j<BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayLists.get(i).getMerchant_id()).size();j++){
                if(BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayLists.get(i).getMerchant_id()).get(j).getFlag().equals("1")){
                    BuyChat.dbHelper.UpdateCountChat(BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayLists.get(i).getMerchant_id()).get(j).getMerchant_id(),(j+1));
                }
            }
        }

        arrayList.clear();

            recyclerView.setVisibility(View.VISIBLE);
            arrayList.addAll(BuyChat.dbHelper.getAllChat());
            adapter.notifyDataSetChanged();


    }

    @Override
    public void onItemClick(View view, int position) {
        if(position == Constants.DEFAULT_INT){
              mcCallSecondPage.setPage();
        }else {


            BuyChat.saveToPreferences(getActivity(),Keys.merchant_id,arrayList.get(position - 1).getMerchant_id());
            BuyChat.saveToPreferences(getActivity(),Keys.business_name,arrayList.get(position - 1).getBusiness_name());
            BuyChat.saveToPreferences(getActivity(),Keys.merchant_image,arrayList.get(position - 1).getMerchant_image());

            BuyChat.saveToPreferences(getActivity(),Keys.from,arrayList.get(position - 1).getFrom());


            BuyChat.saveToPreferences(getActivity(),Keys.chatlist,Constants.False);
            startActivityForResult(new Intent(getActivity(), ChatMainActivity.class), Constants.DEFAULT_INT);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

   public interface CallSecondPage{
        void setPage();
    }
}
