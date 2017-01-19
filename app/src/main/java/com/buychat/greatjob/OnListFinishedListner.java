package com.buychat.greatjob;

import com.buychat.pojos.City;

import java.util.ArrayList;

/**
 * Created by Hos_Logicbox on 16/07/16.
 */
public interface OnListFinishedListner {
    void onError(String message);
    void onSuccess(ArrayList<City> dealsPojoArrayList);
    void onScrollSuccess(ArrayList<City> dealsPojoArrayList);
    void internetIssue();
}
