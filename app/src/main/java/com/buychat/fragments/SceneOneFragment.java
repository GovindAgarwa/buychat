package com.buychat.fragments;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buychat.R;
import com.buychat.databinding.FragmentSceneOneBinding;

import java.util.ArrayList;


/**
 * Created by Eoin on 8/8/16.
 */
public class SceneOneFragment extends BaseSceneFragment {

    private FragmentSceneOneBinding binding;


    public static SceneOneFragment newInstance(int position) {
        SceneOneFragment scene = new SceneOneFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
//        args.putString(KEY_TEXT,s);
        scene.setArguments(args);

        return scene;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.sharedImage.setImageResource(getArguments().getInt(KEY_POSITION));
//        binding.textPager.setText(getArguments().getString(KEY_TEXT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scene_one, container, false);
        setRootPositionTag(binding.root);
        return binding.getRoot();
    }


    @Override
    public void enterScene(@Nullable ImageView sharedElement, float position) {

    }

    @Override
    public void centerScene(@Nullable ImageView sharedElement) {

    }

    @Override
    public void exitScene(@Nullable ImageView sharedElement, float position) {

    }

    @Override
    public void notInScene() {

    }
}
