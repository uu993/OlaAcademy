package com.snail.olaxueyuan.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.snail.olaxueyuan.ui.circle.CircleFragment;
import com.snail.olaxueyuan.ui.course.CourseFragment;
import com.snail.olaxueyuan.ui.examination.ExamFragment;
import com.snail.olaxueyuan.ui.me.UserFragment;
import com.snail.olaxueyuan.ui.question.QuestionFragment;

/**
 * Created by tianxiaopeng on 15-1-2.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
//    private IndexFragment indexFragment;
//    private InformationFragment consultFragment;
//    private StoryFragment storyFragment;
//    private SearchFragment searchFragment;

    private QuestionFragment questionFragment;
    private ExamFragment examFragment;
    private CourseFragment courseFragment;
    private CircleFragment circleFragment;
    //    private UserBaseFragment userFragment;
    private UserFragment userFragment;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        construct();
    }

    private void construct() {
//        indexFragment = new IndexFragment();
//        consultFragment = new InformationFragment();
//        searchFragment = new SearchFragment();
//        storyFragment = new StoryFragment();

        questionFragment = new QuestionFragment();
        examFragment = new ExamFragment();
        courseFragment = new CourseFragment();
        circleFragment = new CircleFragment();
//        userFragment = new UserBaseFragment();
        userFragment = new UserFragment();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return questionFragment;
            case 1:
                return examFragment;
            case 2:
                return courseFragment;
            case 3:
                return circleFragment;
            case 4:
                return userFragment;
            default:
                return null;
        }
    }

}

