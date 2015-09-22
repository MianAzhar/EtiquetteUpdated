package com.EA.Scenario.etiquette.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.adapters.IntroPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroductionFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private ImageButton getStartedButton;

    private ImageView dot1;
    private ImageView dot2;
    private ImageView dot3;

    public IntroductionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_introduction, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        dot1 = (ImageView)getActivity().findViewById(R.id.dot1);
        dot2 = (ImageView)getActivity().findViewById(R.id.dot2);
        dot3 = (ImageView)getActivity().findViewById(R.id.dot3);

        getStartedButton = (ImageButton)getActivity().findViewById(R.id.getStartedButton);

        getStartedButton.setOnClickListener(this);

        ViewPager defaultViewpager = (ViewPager) getActivity().findViewById(R.id.viewpager_default);

        IntroPagerAdapter defaultPagerAdapter = new IntroPagerAdapter(getChildFragmentManager());
        defaultViewpager.setAdapter(defaultPagerAdapter);
        //defaultViewpager.setClipToPadding(false);
        //defaultViewpager.setPadding(50, 0, 50, 0);



        defaultViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        dot1.setImageResource(R.drawable.rec_fill);
                        dot2.setImageResource(R.drawable.empty);
                        dot3.setImageResource(R.drawable.empty);

                        break;

                    case 1:
                        dot1.setImageResource(R.drawable.empty);
                        dot2.setImageResource(R.drawable.rec_fill);
                        dot3.setImageResource(R.drawable.empty);
                        break;

                    case 2:
                        dot1.setImageResource(R.drawable.empty);
                        dot2.setImageResource(R.drawable.empty);
                        dot3.setImageResource(R.drawable.rec_fill);
                        ImageButton b = (ImageButton)getActivity().findViewById(R.id.getStartedButton);
                        b.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.getStartedButton)
        {
            SignInFragment newFrag = new SignInFragment();
            android.support.v4.app.FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, "SignInFragment").commit();
        }
    }


}
