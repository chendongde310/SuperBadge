package com.chendong.gank.library;

import android.content.Context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/1 - 19:00
 * 注释：角标管数据员
 */
public class SuperBadgeDater implements Serializable {

    private static final SuperBadgeDater Instance = new SuperBadgeDater();
    // public static final String FILE_NAME = "super_badge_dater";


    Map<String, SuperBadgeHelper> map = new HashMap<>();

    private SuperBadgeDater() {

    }

    public static SuperBadgeDater getInstance() {
        return Instance;
    }

    public void addBadge(SuperBadgeHelper superBadge) {
        map.put(superBadge.getTag(), superBadge);
        StringSerializable.saveSuperBadgeHelper(superBadge.getContext(), superBadge.getTag(), superBadge);
    }


    /**
     * @param tag 标记
     */
    public SuperBadgeHelper getBadge(Context context, String tag) {
        SuperBadgeHelper superBadgeHelper = map.get(tag);
        if (superBadgeHelper != null) {
            return map.get(tag);

        } else {
            return StringSerializable.readSuperBadgeHelper(context, tag);
        }
    }
}
