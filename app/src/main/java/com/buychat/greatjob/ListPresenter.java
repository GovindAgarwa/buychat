package com.buychat.greatjob;

import com.buychat.pojos.City;

import java.util.ArrayList;

/**
 * Created by Hos_Logicbox on 18/07/16.
 */
public class ListPresenter implements IListPresenter,OnListFinishedListner {

    IListView view;
    AsyncListInteraction interaction;
    ListPresenter(IListView view){
        this.view = view;
        interaction = new AsyncListInteraction();
    }


    @Override
    public void onError(String message) {
            view.onError(message);
    }

    @Override
    public void onSuccess(ArrayList<City> dealsPojoArrayList) {
        view.onSuccess(dealsPojoArrayList);
    }

    @Override
    public void onScrollSuccess(ArrayList<City> dealsPojoArrayList) {
        view.onScrollSuccess(dealsPojoArrayList);
    }

    @Override
    public void internetIssue() {
        view.internetIssue();
    }

    @Override
    public void getCityData(int limit, int offset) {
            interaction.fetchCityData(this,limit,offset);
    }
}
