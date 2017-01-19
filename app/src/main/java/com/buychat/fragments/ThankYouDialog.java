package com.buychat.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.activities.HomeActivity;
import com.buychat.api.Parse;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.pojos.Chat;
import com.buychat.pojos.ProductPojos;
import com.buychat.singleton.DataSingleton;
import com.buychat.singleton.SocketSingleton;
import com.buychat.utils.chats.Status;
import com.buychat.utils.chats.UserType;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class ThankYouDialog extends DialogFragment  {


    public ThankYouDialog() {
    }
    @BindView(R.id.order_id)
    TextView order_id;
    public static ThankYouDialog newInstance() {
        
        Bundle args = new Bundle();
        
        ThankYouDialog fragment = new ThankYouDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.dialog_thankyou, container,false);
        ButterKnife.bind(this,itemView);

        return itemView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        order_id.setText(Constants.Order_ID(getArguments().getString(Keys.order_id),getArguments().getString(Keys.total_amount)));
        String details = "";
        ArrayList<ProductPojos> arrayList = BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId());
        for(int i=0; i<arrayList.size();i++){
                details +=  arrayList.get(i).getProduct_name() +" - "+arrayList.get(i).getProduct_price()+" x "+ arrayList.get(i).getQuantity()+" <br>";
        }
        System.out.println("details "+details);
        System.out.println("payment_type "+getArguments().getString(Keys.payment_type));
        final Chat message = new Chat();
        message.setMessageStatus(Status.SENT);
        message.setImage(Constants.DEFAULT_STRING);
        String chat = "Your order with order id #"+getArguments().getString(Keys.order_id)+" for "+Constants.MONEY_ICON+getArguments().getString(Keys.total_amount)+" has been successfully placed."+
                "<br> Payment Mode : "+getArguments().getString(Keys.payment_type)+". <br> Product Details : <br> "+details + " Thankyou for using Buychat. ";
        message.setMessageText(chat);
        message.setUserType(UserType.SELF);
        message.setMessageTime(new Date().getTime());
        message.setMerchant_id(DataSingleton.getInstance().getData().getId());
        message.setBusiness_name(DataSingleton.getInstance().getData().getBusiness_name());
        message.setMerchant_image(DataSingleton.getInstance().getData().getBusiness_image());
        message.setFrom(Constants.MERCHANT);
        message.setFlag("1");
        message.setCount(0);
        BuyChat.dbHelper.InsertChat(message);
        SocketSingleton.get(getActivity()).getSocket().connect();
        SocketSingleton.get(getActivity()).getSocket().emit(Keys.chatMessageToMerchant, Parse.getMerchantIdAccessTokenAndMessage(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING),
                chat,""));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @OnClick(R.id.home)
    public void click(){
        BuyChat.dbHelper.DeleteMerchant(DataSingleton.getInstance().getData().getId());
        Intent intent = new Intent(getActivity(),
                HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


}
