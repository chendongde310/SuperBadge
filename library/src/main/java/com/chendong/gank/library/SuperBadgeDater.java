package com.chendong.gank.library;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 作者：chendongde310
 * Github:www.github.com/chendongde310
 * 日期：2016/12/5 - 15:37
 * 注释：角标数据管理
 * 更新内容：
 */

public class SuperBadgeDater implements Serializable {

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
    public SuperBadgeHelper getBadge(String tag) {
        return map.get(tag);
    }
}
