package com.buychat.adapter;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.buychat.fragments.BaseSceneFragment;
import com.buychat.fragments.SceneOneFragment;

import java.util.ArrayList;


/**
 * Created by Eoin on 8/8/16.
 */
public class ScenePagerAdapter extends FragmentPagerAdapter {

    private SceneTransformer mSceneTransformer;
    ArrayList<Integer> listImage;
//    ArrayList<String> listText;

    public ScenePagerAdapter(@NonNull FragmentManager supportFragmentManager,
                             @NonNull SceneTransformer sceneTransformer, ArrayList<Integer> listImages) {
        super(supportFragmentManager);
        listImage=new ArrayList<>();
        listImage.addAll(listImages);
//        listText=new ArrayList<>();
//        listText.addAll(listTextz);
        mSceneTransformer = sceneTransformer;
    }

    @Override
    public Fragment getItem(int position) {
        BaseSceneFragment baseSceneFragment = null;

        baseSceneFragment = SceneOneFragment.newInstance(listImage.get(position));

//        switch (position) {
//            case 0:
//                baseSceneFragment = SceneOneFragment.newInstance(position);
//                break;
//            case 1:
//                baseSceneFragment = SceneTwoFragment.newInstance(position);
//                break;
//            case 2:
//                baseSceneFragment = SceneThreeFragment.newInstance(position);
//                break;
//        }

        mSceneTransformer.addSceneChangeListener(baseSceneFragment);
        return baseSceneFragment;
    }

    @Override
    public int getCount() {
        return listImage.size();
    }
}