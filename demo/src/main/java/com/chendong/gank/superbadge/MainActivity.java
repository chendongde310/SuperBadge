package com.chendong.gank.superbadge;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.chendong.gank.library.SuperBadgeHelper;

import static com.chendong.gank.library.SuperBadgeHelper.init;

/**
 * 作者：chendong  -  github.com/chendongde310
 * 日期：2016/12/2 - 11:25
 * 注释：
 * 更新说明:
 */
public class MainActivity extends Activity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private TextView text1;
    private TextView text2;
    private TextView text3;

    private PlaceholderFragment Fragment1;
    private PlaceholderFragment Fragment2;
    private PlaceholderFragment Fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        this.text3 = (TextView) findViewById(R.id.text3);
        this.text2 = (TextView) findViewById(R.id.text2);
        this.text1 = (TextView) findViewById(R.id.text1);


        setOnClick(text1, 0);
        setOnClick(text2, 1);
        setOnClick(text3, 2);


        Fragment1 = PlaceholderFragment.newInstance(1, init(this, text1, text1.getText().toString()).getTag());
        Fragment2 = PlaceholderFragment.newInstance(2, init(this, text2, text2.getText().toString()).getTag());
        Fragment3 = PlaceholderFragment.newInstance(3, init(this, text3, text3.getText().toString()).getTag());


        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                setColor(position);
                switch (position){
                    case 0:
                        text1.setBackgroundColor(getResources().getColor(R.color.main_menu_tab));
                        break;
                    case 1:
                        text2.setBackgroundColor(getResources().getColor(R.color.main_menu_tab));
                        break;
                    case 2:
                        text3.setBackgroundColor(getResources().getColor(R.color.main_menu_tab));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(1);
    }


    private void setOnClick(final View view, final int i) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(i);
                view.setBackgroundColor(getResources().getColor(R.color.main_menu_tab));
            }
        });
    }

    private void setColor(int i){
        mViewPager.setCurrentItem(i);
        text1.setBackgroundColor(getResources().getColor(R.color.white));
        text2.setBackgroundColor(getResources().getColor(R.color.white));
        text3.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String SUPER_BADGE_TAG = "super_badge_tag";

        private TextView textView;
        private SuperBadgeHelper tvsb;
        private Thread thread;
        private TextView rootbadge1;
        private TextView rootbadge2;
        private TextView rootbadge3;
        private SuperBadgeHelper rb1;
        private SuperBadgeHelper rb2;
        private SuperBadgeHelper rb3;

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber, String tag) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(SUPER_BADGE_TAG, tag);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            String sectionNumber = String.valueOf(getArguments().getInt(ARG_SECTION_NUMBER));
            String tag = getArguments().getString(SUPER_BADGE_TAG);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            this.rootbadge3 = (TextView) rootView.findViewById(R.id.root_badge_3);
            this.rootbadge2 = (TextView) rootView.findViewById(R.id.root_badge_2);
            this.rootbadge1 = (TextView) rootView.findViewById(R.id.root_badge_1);

            rootbadge1.setText(String.format("%s根节点1", tag));
            rootbadge2.setText(String.format("%s根节点2", tag));
            rootbadge3.setText(String.format("%s根节点3", tag));


            textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText("读取全部" + tag + "节点");
            tvsb = init(getActivity(), textView, "textView" + sectionNumber, false);
            tvsb.bindView(getArguments().getString(SUPER_BADGE_TAG));


            rb1 = SuperBadgeHelper.init(getActivity(), rootbadge1, "R.id.root_badge_1" + sectionNumber, 3);
            rb2 = SuperBadgeHelper.init(getActivity(), rootbadge2, "R.id.root_badge_2" + sectionNumber, 1);
            rb3 = SuperBadgeHelper.init(getActivity(), rootbadge3, "R.id.root_badge_3" + sectionNumber, 7);
            rb1.bindView(tvsb);
            rb2.bindView(tvsb);
            rb3.bindView(tvsb);

//            rb1.setBadgeColor(Color.parseColor("#d3321b"));
//            rb2.setBadgeColor(Color.parseColor("#d3321b"));
//            rb3.setBadgeColor(Color.parseColor("#d3321b"));


            setReadOnClick(textView, tvsb);
            setReadOnClick(rootbadge1, rb1);
            setReadOnClick(rootbadge2, rb2);
            setReadOnClick(rootbadge3, rb3);


            //模拟消息添加
            if (thread == null) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rb1.addNum((int) (Math.random() * 5));
                                    rb2.addNum((int) (Math.random() * 5));
                                    rb3.addNum((int) (Math.random() * 5));
                                }
                            });
                            try {
                                Thread.sleep((int) (Math.random() * 3000 + 10000));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                 thread.start();
            }


            return rootView;
        }


        private void setReadOnClick(View view, final SuperBadgeHelper superBadge) {

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    superBadge.read();
                }
            });
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return Fragment1;
                case 1:
                    return Fragment2;
                case 2:
                    return Fragment3;
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            return "页面" + position;
        }
    }


}
