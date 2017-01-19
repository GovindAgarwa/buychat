package com.buychat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.buychat.app.BuyChat;
import com.buychat.extras.Keys;
import com.buychat.pojos.Address;
import com.buychat.pojos.Chat;
import com.buychat.pojos.ProductPojos;
import com.buychat.pojos.ShopPojo;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.chats.Status;
import com.buychat.utils.chats.UserType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Snyxius Technologies on 8/16/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private final String CART_TABLE_NAME = "cart";

    private final String CHAT_TABLE_NAME = "chat";

    private final String ADDRESS_TABLE_NAME = "address";

    public DataBaseHelper(Context context) {
        super(context, "BuyChat.db", null, 1);
        onCreate(getWritableDatabase());

    }




    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE IF NOT EXISTS "
                + CART_TABLE_NAME
                + " (id INTEGER PRIMARY KEY,product_id TEXT," +
                "merchant_id TEXT,product_name TEXT,product_description TEXT," +
                "product_short_description TEXT,product_sku TEXT,product_price TEXT," +
                "product_image TEXT," +
                "product_categories TEXT,product_quantity TEXT,offer_price TEXT)";
        database.execSQL(query);


        query = "CREATE TABLE IF NOT EXISTS "
                + CHAT_TABLE_NAME
                + " (id INTEGER PRIMARY KEY," +
                "merchant_id TEXT,messageStatus TEXT,messageText TEXT," +
                "userType TEXT,image TEXT,messageTime TEXT," +
                "business_name TEXT," +
                "flag TEXT,count TEXT,merchant_image TEXT,my_site TEXT)";
        database.execSQL(query);


        query = "CREATE TABLE IF NOT EXISTS "
                + ADDRESS_TABLE_NAME
                + " (id INTEGER PRIMARY KEY," +
                "access_token TEXT,locality TEXT,flat_no_floor_name TEXT," +
                "landmark TEXT,tag_address TEXT)";
        database.execSQL(query);

    }


    public void InsertAddress(Address cartPOJO) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("access_token", cartPOJO.getAccess_token());

        values.put("locality", String.valueOf(cartPOJO.getLocality()));

        values.put("flat_no_floor_name", cartPOJO.getFlat_no_floor_name());

        values.put("landmark",cartPOJO.getLandmark());

        values.put("tag_address", cartPOJO.getTag_address());

        database.insert(ADDRESS_TABLE_NAME, null, values);

    }

    public void UpdateAddress(String id,Address cartPOJO) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("access_token", cartPOJO.getAccess_token());

        values.put("locality", String.valueOf(cartPOJO.getLocality()));

        values.put("flat_no_floor_name", cartPOJO.getFlat_no_floor_name());

        values.put("landmark",String.valueOf(cartPOJO.getLandmark()));

        values.put("tag_address", cartPOJO.getTag_address());

        database.update(ADDRESS_TABLE_NAME, values,
                " id ='" + id + "' "
                , null);

    }

    public ArrayList<Address> getAddressById(String id) {
        ArrayList<Address> cartList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ADDRESS_TABLE_NAME
                + " WHERE  id = '" + id
                + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Address cartPOJO = new Address();
                cartPOJO.setId(cursor.getString(0));
                cartPOJO.setAccess_token(cursor.getString(1));
                cartPOJO.setLocality(cursor.getString(2));
                cartPOJO.setFlat_no_floor_name(cursor.getString(3));
                cartPOJO.setLandmark(cursor.getString(4));
                cartPOJO.setTag_address(cursor.getString(5));
                cartList.add(cartPOJO);
            } while (cursor.moveToNext());
        }
        // close inserting data from database
        db.close();
        // return contact list
        return cartList;
    }

    public ArrayList<Address> getALLAddress() {
        ArrayList<Address> cartList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + ADDRESS_TABLE_NAME
                + " ORDER BY id DESC "
                + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Address cartPOJO = new Address();
                cartPOJO.setId(cursor.getString(0));
                cartPOJO.setAccess_token(cursor.getString(1));
                cartPOJO.setLocality(cursor.getString(2));
                cartPOJO.setFlat_no_floor_name(cursor.getString(3));
                cartPOJO.setLandmark(cursor.getString(4));
                cartPOJO.setTag_address(cursor.getString(5));
                cartList.add(cartPOJO);
            } while (cursor.moveToNext());
        }
        // close inserting data from database
        db.close();
        // return contact list
        return cartList;
    }

    public void InsertChat(Chat cartPOJO) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchant_id", cartPOJO.getMerchant_id());

        values.put("messageStatus", String.valueOf(cartPOJO.getMessageStatus()));

        values.put("messageText", cartPOJO.getMessageText());

        values.put("userType",String.valueOf(cartPOJO.getUserType()));

        values.put("image", cartPOJO.getImage());

        values.put("messageTime", cartPOJO.getMessageTime());

        values.put("business_name",cartPOJO.getBusiness_name());

        values.put("flag", cartPOJO.getFlag());

        values.put("count",cartPOJO.getCount());

        values.put("merchant_image",cartPOJO.getMerchant_image());

        values.put("my_site",cartPOJO.getFrom());
        database.insert(CHAT_TABLE_NAME, null, values);

    }


    public void UpdateUnReadChat(String merchant_id) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("flag","0");

        values.put("count",0);

        database.update(CHAT_TABLE_NAME, values,
                " merchant_id ='" + merchant_id + "' "
                , null);

    }

    public void UpdateCountChat(String merchant_id,int count) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put("count",count);

        database.update(CHAT_TABLE_NAME, values,
                " merchant_id ='" + merchant_id + "' "
                , null);

    }

    public void UpdateProductImage(String merchant_id,String image) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put("product_image",image);

        database.update(CART_TABLE_NAME, values,
                " merchant_id ='" + merchant_id + "' "
                , null);

    }

    public ArrayList<Chat> getChatByMerchant_id(String merchant_id) {
        ArrayList<Chat> cartList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + CHAT_TABLE_NAME
                + " WHERE  merchant_id = '" + merchant_id
                + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Chat cartPOJO = new Chat();
                cartPOJO.setMerchant_id(cursor.getString(1));
                if(String.valueOf(cursor.getString(2)).equals(String.valueOf(Status.SENT))){
                    cartPOJO.setMessageStatus(Status.SENT);
                }else{
                    cartPOJO.setMessageStatus(Status.DELIVERED);
                }
                cartPOJO.setMessageText(cursor.getString(3));
                if(String.valueOf(cursor.getString(4)).equals(String.valueOf(UserType.OTHER))){
                    cartPOJO.setUserType(UserType.OTHER);
                }else{
                    cartPOJO.setUserType(UserType.SELF);
                }
                cartPOJO.setImage((cursor.getString(5)));
                cartPOJO.setMessageTime(Long.valueOf(cursor.getString(6)));
                cartPOJO.setBusiness_name((cursor.getString(7)));
                cartPOJO.setFlag(cursor.getString(8));
                cartPOJO.setCount(Integer.valueOf(cursor.getString(9)));
                cartPOJO.setMerchant_image(cursor.getString(10));
                cartPOJO.setFrom(cursor.getString(11));
                // Adding contact to list
                cartList.add(cartPOJO);
            } while (cursor.moveToNext());
        }
        // close inserting data from database
        db.close();
        // return contact list
        return cartList;
    }

    public ArrayList<Chat> getChatByMerchant_idOrderBy(String merchant_id) {
        ArrayList<Chat> cartList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + CHAT_TABLE_NAME
                + " WHERE  merchant_id = '" + merchant_id
                + "' ORDER BY id DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Chat cartPOJO = new Chat();
                cartPOJO.setMerchant_id(cursor.getString(1));
                if(String.valueOf(cursor.getString(2)).equals(String.valueOf(Status.SENT))){
                    cartPOJO.setMessageStatus(Status.SENT);
                }else{
                    cartPOJO.setMessageStatus(Status.DELIVERED);
                }
                cartPOJO.setMessageText(cursor.getString(3));
                if(String.valueOf(cursor.getString(4)).equals(String.valueOf(UserType.OTHER))){
                    cartPOJO.setUserType(UserType.OTHER);
                }else{
                    cartPOJO.setUserType(UserType.SELF);
                }
                cartPOJO.setImage((cursor.getString(5)));
                cartPOJO.setMessageTime(Long.valueOf(cursor.getString(6)));
                cartPOJO.setBusiness_name((cursor.getString(7)));
                cartPOJO.setFlag(cursor.getString(8));
                cartPOJO.setCount(Integer.valueOf(cursor.getString(9)));
                cartPOJO.setMerchant_image(cursor.getString(10));
                cartPOJO.setFrom(cursor.getString(11));
                // Adding contact to list
                cartList.add(cartPOJO);
            } while (cursor.moveToNext());
        }
        // close inserting data from database
        db.close();
        // return contact list
        return cartList;
    }

    public ArrayList<Chat> getAllChat() {
        ArrayList<Chat> cartList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + CHAT_TABLE_NAME
                + " GROUP BY merchant_id ORDER BY id DESC"
                + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Chat cartPOJO = new Chat();
                cartPOJO.setMerchant_id(cursor.getString(1));
                if(String.valueOf(cursor.getString(2)).equals(Status.SENT)){
                    cartPOJO.setMessageStatus(Status.SENT);
                }else{
                    cartPOJO.setMessageStatus(Status.DELIVERED);
                }
                cartPOJO.setMessageText(cursor.getString(3));
                if(String.valueOf(cursor.getString(4)).equals(UserType.OTHER)){
                    cartPOJO.setUserType(UserType.OTHER);
                }else{
                    cartPOJO.setUserType(UserType.SELF);
                }
                cartPOJO.setImage((cursor.getString(5)));
                cartPOJO.setMessageTime(Long.valueOf(cursor.getString(6)));
                cartPOJO.setBusiness_name((cursor.getString(7)));
                cartPOJO.setFlag(cursor.getString(8));
                cartPOJO.setCount(Integer.valueOf(cursor.getString(9)));
                cartPOJO.setMerchant_image(cursor.getString(10));
                cartPOJO.setFrom(cursor.getString(11));
                // Adding contact to list
                cartList.add(cartPOJO);
            } while (cursor.moveToNext());
        }
        // close inserting data from database
        db.close();
        // return contact list
        return cartList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        String query;
        query = "DROP TABLE IF EXISTS " + CART_TABLE_NAME;
        database.execSQL(query);
        query = "DROP TABLE IF EXISTS " + CHAT_TABLE_NAME;
        database.execSQL(query);
        query = "DROP TABLE IF EXISTS " + ADDRESS_TABLE_NAME;
        database.execSQL(query);
        onCreate(database);
    }

    public void DeleteAddressRecords() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(ADDRESS_TABLE_NAME, null, null);
    }

    public void DeleteCartRecords() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(CART_TABLE_NAME, null, null);
    }

    public void DeleteChatRecords() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(CHAT_TABLE_NAME, null, null);
    }


    public void InsertFeeds(ProductPojos cartPOJO) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("product_id", cartPOJO.getId());

        values.put("merchant_id", cartPOJO.getMerchant_id());

        values.put("product_name", cartPOJO.getProduct_name());

        values.put("product_description",cartPOJO.getProduct_description());

        values.put("product_short_description", cartPOJO.getProduct_name());

        values.put("product_sku", cartPOJO.getProduct_description());

        values.put("product_price",cartPOJO.getProduct_price());

        values.put("product_image", cartPOJO.getProduct_image());

        values.put("product_categories",cartPOJO.getProduct_categories());

        values.put("product_quantity",cartPOJO.getQuantity());

        values.put("offer_price",cartPOJO.getOfferprice());

        database.insert(CART_TABLE_NAME, null, values);

    }


    public void UpdateFeeds(ProductPojos cartPOJO) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("product_id", cartPOJO.getId());

        values.put("merchant_id", cartPOJO.getMerchant_id());

        values.put("product_name", cartPOJO.getProduct_name());

        values.put("product_description",cartPOJO.getProduct_description());

        values.put("product_short_description", cartPOJO.getProduct_name());

        values.put("product_sku", cartPOJO.getProduct_description());

        values.put("product_price",cartPOJO.getProduct_price());

        values.put("product_image", cartPOJO.getProduct_image());

        values.put("product_categories",cartPOJO.getProduct_categories());

        values.put("product_quantity",cartPOJO.getQuantity());
        values.put("offer_price",cartPOJO.getOfferprice());
        database.update(CART_TABLE_NAME, values,
                " product_id ='" + cartPOJO.getId() + "' "
                , null);
    }

    public void DeleteItems(String product_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + CART_TABLE_NAME
                + " where product_id = '" + product_id
                + "'";

        Log.d("query", deleteQuery);
        database.execSQL(deleteQuery);

    }
    public void DeleteMerchant(String merchant_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + CART_TABLE_NAME
                + " where merchant_id = '" + merchant_id
                + "'";

        Log.d("query", deleteQuery);
        database.execSQL(deleteQuery);

    }
    public void DeleteAddress(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + ADDRESS_TABLE_NAME
                + " where id = '" + id
                + "'";

        Log.d("query", deleteQuery);
        database.execSQL(deleteQuery);

    }
    public ArrayList<ProductPojos> getCartProduct(String merchant_id) {
        ArrayList<ProductPojos> cartList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + CART_TABLE_NAME
                + " WHERE  merchant_id = '" + merchant_id
                + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProductPojos cartPOJO = new ProductPojos();
                cartPOJO.setId(cursor.getString(1));
                cartPOJO.setMerchant_id(cursor.getString(2));
                cartPOJO.setProduct_name(cursor.getString(3));
                cartPOJO.setProduct_description((cursor.getString(4)));
                cartPOJO.setProduct_short_description((cursor.getString(5)));
                cartPOJO.setProduct_sku((cursor.getString(6)));
                cartPOJO.setProduct_price((cursor.getString(7)));
                cartPOJO.setProduct_image((cursor.getString(8)));
                cartPOJO.setProduct_categories((cursor.getString(9)));
                cartPOJO.setQuantity(Integer.valueOf(cursor.getString(10)));
                cartPOJO.setOfferprice(cursor.getString(11));
                // Adding contact to list
                cartList.add(cartPOJO);
            } while (cursor.moveToNext());
        }
        // close inserting data from database
        db.close();
        // return contact list
        return cartList;
    }

    public ArrayList<ProductPojos> getCartProductByProductId(String merchant_id,String product_id) {
        ArrayList<ProductPojos> cartList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + CART_TABLE_NAME
                + " WHERE  merchant_id = '" + merchant_id +"'"
                + " AND product_id = '"+product_id +"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProductPojos cartPOJO = new ProductPojos();
                cartPOJO.setId(cursor.getString(1));
                cartPOJO.setMerchant_id(cursor.getString(2));
                cartPOJO.setProduct_name(cursor.getString(3));
                cartPOJO.setProduct_description((cursor.getString(4)));
                cartPOJO.setProduct_short_description((cursor.getString(5)));
                cartPOJO.setProduct_sku((cursor.getString(6)));
                cartPOJO.setProduct_price((cursor.getString(7)));
                cartPOJO.setProduct_image((cursor.getString(8)));
                cartPOJO.setProduct_categories((cursor.getString(9)));
                cartPOJO.setQuantity(Integer.valueOf(cursor.getString(10)));
                cartPOJO.setOfferprice(cursor.getString(11));
                // Adding contact to list
                cartList.add(cartPOJO);
            } while (cursor.moveToNext());
        }
        // close inserting data from database
        db.close();
        // return contact list
        return cartList;
    }
//    // Getting All Contacts
//    public List<ShopPojo> getAllCarts() {
//        List<ShopPojo> cartList = new ArrayList<ShopPojo>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + CART_TABLE_NAME + " ORDER BY id";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                ShopPojo cartPOJO = new ShopPojo();
//                cartPOJO.setShop_id(Integer.parseInt(cursor.getString(1)));
//                cartPOJO.setCategory_id(Integer.parseInt(cursor.getString(2)));
//                cartPOJO.setProduct_id(Integer.parseInt(cursor.getString(3)));
//                cartPOJO.setProduct_image((cursor.getString(4)));
//                cartPOJO.setProduct_name((cursor.getString(5)));
//                cartPOJO.setProduct_description((cursor.getString(6)));
//                cartPOJO.setProduct_price((cursor.getString(7)));
//                cartPOJO.setProuduct_quantity((cursor.getString(8)));
//                // Adding contact to list
//                cartList.add(cartPOJO);
//            } while (cursor.moveToNext());
//        }
//        // close inserting data from database
//        db.close();
//        // return contact list
//        return cartList;
//    }


}