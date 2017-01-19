package com.buychat.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.pojos.Image;
import com.buychat.utils.chats.AndroidUtilities;
import com.buychat.pojos.Chat;
import com.buychat.utils.chats.Emoji;
import com.buychat.utils.chats.Status;
import com.buychat.utils.chats.UserType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class ChatAdapter extends BaseAdapter {

    private ArrayList<Chat> chatMessages;
    private Context context;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("h:mm a", Locale.US);
    ImageLoader imageLoader;
    DisplayImageOptions options;

    private void initializeImageLoader(){
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo).build();

    }
    public ChatAdapter(ArrayList<Chat> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
        initializeImageLoader();
    }


    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        Chat message = chatMessages.get(position);
        ViewHolder1 holder1;
        ViewHolder2 holder2;

        if (message.getUserType() == UserType.SELF) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user1_item, null, false);
                holder1 = new ViewHolder1();


                holder1.messageTextView = (TextView) v.findViewById(R.id.textview_message);
                holder1.timeTextView = (TextView) v.findViewById(R.id.textview_time);
                holder1.image = (ImageView) v.findViewById(R.id.image);
                v.setTag(holder1);
            } else {
                v = convertView;
                holder1 = (ViewHolder1) v.getTag();

            }

            holder1.messageTextView.setText(Html.fromHtml(Emoji.replaceEmoji(message.getMessageText(),
                    holder1.messageTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16))
                    + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;"));
            holder1.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
            if(message.getImage() != null) {
                if (!message.getImage().equals(Constants.DEFAULT_STRING)) {
                    holder1.image.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(BuyChat.replace(message.getImage()),
                            holder1.image, options);

                } else {
                    holder1.image.setVisibility(View.GONE);
                }
            } else {
                holder1.image.setVisibility(View.GONE);
            }
        } else if (message.getUserType() == UserType.OTHER) {

            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user2_item, null, false);

                holder2 = new ViewHolder2();


                holder2.messageTextView = (TextView) v.findViewById(R.id.textview_message);
                holder2.timeTextView = (TextView) v.findViewById(R.id.textview_time);
                holder2.messageStatus = (ImageView) v.findViewById(R.id.user_reply_status);
                holder2.image = (ImageView) v.findViewById(R.id.image);
                v.setTag(holder2);

            } else {
                v = convertView;
                holder2 = (ViewHolder2) v.getTag();

            }

            holder2.messageTextView.setText(Html.fromHtml(Emoji.replaceEmoji(message.getMessageText(),
                    holder2.messageTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16))
                    + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;" +
                    "&#160;&#160;&#160;&#160;&#160;&#160;&#160;"));
            //holder2.messageTextView.setText(message.getMessageText());
            holder2.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
            if(message.getImage() != null ){
                if(!message.getImage().equals(Constants.DEFAULT_STRING)) {
                    holder2.image.setVisibility(View.VISIBLE);
                    holder2.image.setImageBitmap(BuyChat.base64ToBitmap(message.getImage()));
                }else{
                    holder2.image.setVisibility(View.GONE);
                }
            }else{
                holder2.image.setVisibility(View.GONE);
            }
            if (message.getMessageStatus() == Status.DELIVERED) {
                holder2.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.message_got_receipt_from_target));
            } else if (message.getMessageStatus() == Status.SENT) {
                holder2.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.message_got_receipt_from_server));

            }


        }


        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Chat message = chatMessages.get(position);
        return message.getUserType().ordinal();
    }

    private class ViewHolder1 {
        public TextView messageTextView;
        public TextView timeTextView;
        public ImageView image;

    }

    private class ViewHolder2 {
        public ImageView messageStatus;
        public TextView messageTextView;
        public TextView timeTextView;
        public ImageView image;

    }
}
