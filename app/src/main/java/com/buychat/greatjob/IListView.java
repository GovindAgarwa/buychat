package com.buychat.greatjob;

import com.buychat.pojos.City;

import java.util.ArrayList;

/**
 * Created by Hos_Logicbox on 18/07/16.
 */
public interface IListView {
    void onError(String message);
    void onSuccess(ArrayList<City> dealsPojoArrayList);
    void onScrollSuccess(ArrayList<City> dealsPojoArrayList);
    void internetIssue();
    void showProgress();
    void hideProgress();
}
