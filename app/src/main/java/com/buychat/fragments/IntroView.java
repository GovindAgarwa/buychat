package com.buychat.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.buychat.BuildConfig;
import com.buychat.R;
import com.buychat.activities.HomeActivity;
import com.buychat.adapter.ScenePagerAdapter;
import com.buychat.adapter.SceneTransformer;
import com.buychat.adapter.TextPagerAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.databinding.IntroMainLayout2Binding;
import com.buychat.databinding.IntroMainLayoutBinding;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.greatjob.GreateJob;
import com.buychat.register.RegisterView;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.ParallaxPageTransformer;
import com.buychat.utils.ViewPageTransformer;
import com.buychat.verifynumber.VerifcationCodeView;
import com.buychat.viewpagertransformers.AccordionTransformer;
import com.buychat.viewpagertransformers.BackgroundToForegroundTransformer;
import com.buychat.viewpagertransformers.CubeInTransformer;
import com.buychat.viewpagertransformers.CubeOutTransformer;
import com.buychat.viewpagertransformers.DefaultTransformer;
import com.buychat.viewpagertransformers.DepthPageTransformer;
import com.buychat.viewpagertransformers.DrawFromBackTransformer;
import com.buychat.viewpagertransformers.FadeTranformer;
import com.buychat.viewpagertransformers.FlipHorizontalTransformer;
import com.buychat.viewpagertransformers.RotateDownTransformer;
import com.buychat.viewpagertransformers.StackTransformer;
import com.buychat.viewpagertransformers.TabletTransformer;
import com.buychat.viewpagertransformers.ZoomInTransformer;
import com.buychat.viewpagertransformers.ZoomOutSlideTransformer;
import com.buychat.viewpagertransformers.ZoomOutTranformer;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snyxius Technologies on 8/8/2016.
 */
public class IntroView extends Fragment {

    private IntroMainLayout2Binding binding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.intro_main_layout2, container, false);
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ArrayList<Integer> listImages=new ArrayList<>();
        listImages.add(R.drawable.intro_dummy);
        listImages.add(R.drawable.intro_two);
//        listImages.add(R.drawable.dummy_image);

//        ArrayList<String> listTexts=new ArrayList<>();
//        listTexts.add(getResources().getString(R.string.write_label));
//        listTexts.add(getResources().getString(R.string.write_label));
//        listTexts.add(getResources().getString(R.string.follow_label));

        setIntro(listImages);

    }





    public void setIntro(final ArrayList<Integer> listImage){

        SceneTransformer sceneTransformer = new SceneTransformer();
        ScenePagerAdapter scenePagerAdapter =
                new ScenePagerAdapter(getChildFragmentManager(), sceneTransformer,listImage);

        binding.tutorialPager.setAdapter(scenePagerAdapter);
        // set limit same as number of fragments
        binding.tutorialPager.setOffscreenPageLimit(2);
        binding.tutorialPager.setPageTransformer(true, new FadeTranformer());

        // AUTO SCROLL
        binding.tutorialPager.startAutoScroll(3000);
//        binding.textPager.startAutoScroll(3000);
        binding.tutorialPager.setAutoScrollDurationFactor(10);
//        binding.textPager.setAutoScrollDurationFactor(15);
        binding.tutorialPager.setStopScrollWhenTouch(true);
//        binding.textPager.setStopScrollWhenTouch(true);
//        binding.tutorialPager.setSwipeScrollDurationFactor(3000);
//        binding.textPager.setSwipeScrollDurationFactor(3000);
        // AUTO SCROLL

//        TextPagerAdapter textAdapter = new TextPagerAdapter(listText);
//        binding.textPager.setAdapter(textAdapter);

        binding.indicator.setViewPager(binding.tutorialPager);
        binding.indicator.setSnap(true);
//        binding.indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                // do nothing
//            }
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset,
//                                       int positionOffsetPixels) {
//                // translate up start button
//
//                if (position == listImage.size()-2) {
//                    binding.start.setVisibility(View.VISIBLE);
//                    binding.start.setTranslationY(binding.textPager.getBottom() * (1 - positionOffset));
//                    binding.indicator.setAlpha(1 - positionOffset);
//                }else if (position == listImage.size()-1) {
//                    binding.start.setVisibility(View.VISIBLE);
//                }else
//                    binding.start.setVisibility(View.GONE);
//            }
//        });

        // to control the two view pagers at once we put a layout above them that intercepts the touches
//        binding.tutorialPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                binding.tutorialPager.onTouchEvent(event);
//                binding.textPager.onTouchEvent(event);
//                return true;
//            }
//        });


        binding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyChat.saveToPreferences(getActivity(), Keys.intro_time,"DONE");
                settingView();
            }
        });
    }

    public void settingView() {
        if (Build.VERSION.SDK_INT >= 21 ) {
            Transition transitionSlideRight =
                    TransitionInflater.from(getActivity()).inflateTransition(R.transition.slide_right);
        }



        if(
                !BuyChat.readFromPreferences(getActivity(), Keys.mobile,Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING) &&
                BuyChat.readFromPreferences(getActivity(), Keys.approved_status,Constants.DEFAULT_STRING).equals(""+Constants.INT_ONE) &&
                !BuyChat.readFromPreferences(getActivity(), Keys.buychat_id,Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING) &&
                !BuyChat.readFromPreferences(getActivity(), Keys.city_name,Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING)){
            String city_id = "";
            for(int i=0;i< Parse.parseCity(DataSingleton.getInstance().getCityData()).size();i++){
                if( Parse.parseCity(DataSingleton.getInstance().getCityData()).get(i).getCity_name().equals(BuyChat.readFromPreferences(getActivity(),Keys.city_name,Constants.DEFAULT_STRING))){
                    city_id =  Parse.parseCity(DataSingleton.getInstance().getCityData()).get(i).getId();
                }
            }
            BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.city_id,city_id);
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            getActivity().finish();


        }else if(BuyChat.readFromPreferences(getActivity(), Keys.approved_status,Constants.DEFAULT_STRING).equals(""+Constants.INT_ONE) &&
                BuyChat.readFromPreferences(getActivity(), Keys.city_name,Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING)){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new GreateJob(), Constants.SelectCityView)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                    .commit();


        }else if(
                !BuyChat.readFromPreferences(getActivity(), Keys.mobile,Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING) &&
                BuyChat.readFromPreferences(getActivity(), Keys.approved_status,Constants.DEFAULT_STRING).equals(""+Constants.DEFAULT_INT)){

            Fragment fragment = new VerifcationCodeView();
            Bundle args = new Bundle();
            args.putString(Keys.data, "("+BuyChat.readFromPreferences(getActivity(),Keys.code,Constants.DEFAULT_STRING)+") "+BuyChat.readFromPreferences(getActivity(),Keys.mobile,Constants.DEFAULT_STRING));
            fragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, Constants.VerifcationCodeView)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                    .commit();

        }else if(BuyChat.readFromPreferences(getActivity(), Keys.mobile,Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING) &&
                !BuyChat.readFromPreferences(getActivity(), Keys.intro_time,Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING)){

            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RegisterView(), Constants.Register)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                    .commit();
        }   else{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new IntroView(), Constants.IntroView)
                    .addToBackStack(Constants.Splash)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                    .commit();
        }

    }



}

