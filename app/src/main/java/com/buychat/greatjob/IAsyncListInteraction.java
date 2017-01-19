package com.buychat.greatjob;

/**
 * Created by Hos_Logicbox on 16/07/16.
 */

public interface IAsyncListInteraction {

    void fetchCityData(OnListFinishedListner onListFinishedListner, int offset, int limit);

}
