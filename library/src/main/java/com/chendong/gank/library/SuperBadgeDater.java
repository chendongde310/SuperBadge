package com.chendong.gank.library;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/1 - 19:00
 * 注释：角标管数据员
 */
public class SuperBadgeDater {

    private static final SuperBadgeDater Instance = new SuperBadgeDater();
    Map<String, SuperBadgeHelper> map = new HashMap<>();

    private SuperBadgeDater() {
    }

    public static SuperBadgeDater getInstance() {
        return Instance;
    }

    public void addBadge(SuperBadgeHelper superBadge) {
        map.put(superBadge.getTag(), superBadge);
    }

    private void findChildBadge(SuperBadgeHelper superBadge) {
        for (SuperBadgeHelper sbh : map.values()) {
            if (sbh.getTag().equals(superBadge.getTag())) {

            }
        }


    }



    /**
     * @param tag 标记
     */
    public SuperBadgeHelper getBadge(String tag) {
        return map.get(tag);
    }


}
