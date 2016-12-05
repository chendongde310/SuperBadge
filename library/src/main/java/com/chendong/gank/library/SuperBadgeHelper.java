package com.chendong.gank.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：陈东  —  www.renwey.com
 * 日期：2016/12/1 - 18:01
 * 注释：角标助手，通过添加标签对
 * 不应该在拥有下级节点的控件上进行数字操作
 */
public class SuperBadgeHelper implements Serializable, Cloneable  {
    private String tag; //标签
    private View view; //寄生控件
    private int num; //计数
    private List<SuperBadgeHelper> paterBadge = new ArrayList<>();//关联的上级节点
    private List<SuperBadgeHelper> childBadge = new ArrayList<>();//关联的下级节点
    private Activity context; //控件所在页面
    private BadgeManger badge;//红点管理器
    private boolean show; //是否显示数字
    private OnNumCallback onNumCallback ;


    private SuperBadgeHelper(Activity context, View view, String tag, int num, boolean show) {
        if (SuperBadgeDater.getInstance().getBadge(context,tag) != null) {
            throw new IllegalArgumentException(tag + "标记已经被其他控件注册");
        }
        if (context == null) {
            throw new NullPointerException("context not is null ");
        }
        if (num < 0) {
            throw new IllegalArgumentException("初始化角标数字不能小于0");
        }
        if (tag == null) {
            throw new IllegalArgumentException("tag 不能为空");
        }

        this.tag = tag;
        this.view = view;
        this.num = num;
        this.context = context;
        this.show = show;

        badge = new BadgeManger(context);
        badge.setTargetView(view);
        paterAddNum(num);
    }

    public static SuperBadgeHelper init(Activity context, View view, String tag) {
        return init(context, view, tag, 0, true);
    }

    public static SuperBadgeHelper init(Activity context, View view, String tag, int num) {
        return init(context, view, tag, num, true);
    }



    public static SuperBadgeHelper init(Activity context, View view, String tag, boolean show) {
        return init(context, view, tag, 0, show);
    }



    public static SuperBadgeHelper getSBHelper(Context context ,String tag) {
        SuperBadgeHelper superBadge = SuperBadgeDater.getInstance().getBadge(context,tag);
        if (superBadge == null) {
            throw new NullPointerException("没有找到标记为[" + tag + "]的控件");
        }
        return superBadge;
    }



    /**
     * @param context 当前Avtivity
     * @param view    绑定角标view
     * @param tag     用于绑定的唯一标记
     * @param num     角标数字
     * @param show    是否显示数字
     * @return SuperBadgeHelper
     */
    public static SuperBadgeHelper init(Activity context, View view, String tag, int num, boolean show) {
        SuperBadgeHelper superBadge = SuperBadgeDater.getInstance().getBadge(context,tag);
        if (superBadge != null) {
            superBadge.setView(view);
            superBadge.setContext(context);
            superBadge.setShowBadge(show);
            superBadge.getBadge().setTargetView(view);
            if (superBadge.isShow()) {
                superBadge.getBadge().setBadgeCount(superBadge.getNum());
            }
            return superBadge;
        } else {
            return new SuperBadgeHelper(context, view, tag, num, show);
        }
    }

    @Deprecated
    public void setOnNumCallback(OnNumCallback onNumCallback) {
        this.onNumCallback = onNumCallback;
    }

    /**
     * 设置角标半径
     * @param dipRadius 半径
     */
    public void setDipRadius(int dipRadius) {
        badge.setBackground(dipRadius, Color.parseColor("#d3321b"));
    }

    /**
     * 设置角标颜色
     * @param badgeColor 颜色
     */
    public void setBadgeColor(int badgeColor) {
        badge.setBackground(9, badgeColor);
    }

    /**
     *
     * @return
     */
    public boolean isShow() {
        return show;
    }


    private BadgeManger getBadge() {
        return badge;
    }

    /**
     * 控件添加数字
     *
     * @param i 添加数字大小
     */
    public void addNum(int i) {
        if (childBadge.size() >= 1) {
            throw new IllegalArgumentException("该控件不是根节点数据控件（该控件包含下级控件），无法完成添加操作");
        }
        paterAddNum(i);
    }


    private void paterAddNum(int i) {
        if (i < 0) {
           // throw new IllegalArgumentException("添加数字不能小于0");
        }else {
            this.num = this.num + i;
            if (show) {
                badge.setBadgeCount(num);
            }
            SuperBadgeDater.getInstance().addBadge(this);
            //传递变化到上级控件
            for (SuperBadgeHelper bean : paterBadge) {
                if (bean != null) {
                    bean.paterAddNum(i);
                }
            }
        }
    }


    public Activity getContext() {
        return context;
    }

    private void setContext(Activity context) {
        this.context = context;
    }

    /**
     * 读取所有消息，减去所有数字
     */
    public void read() {
        chlidLessNum(getNum());
    }

    public String getTag() {
        return tag;
    }

    private void setTag(String tag) {
        this.tag = tag;
    }

    public View getView() {
        return view;
    }

    private void setView(View view) {
        this.view = view;
    }

    public int getNum() {
        return num;
    }

    /**
     * @param i 减少数字
     */
    private void chlidLessNum(int i) {
        if (i > 0) {
            if (show) {
                badge.setBadgeCount(getNum() - i);
            }
            this.num = num - i;
            SuperBadgeDater.getInstance().addBadge(this);
            changeBadge(i);
        }
    }

    /**
     * 减少
     *
     * @param i 减少数字
     */
    public void lessNum(int i) {
        if (childBadge.size() >= 1) {
            throw new IllegalArgumentException("该控件不是根节点数据控件（包含下级控件），无法完成减少操作");
        }
        chlidLessNum(i);
    }


    /**
     * 根据父级控件tag绑定父级控件
     *
     * @param tag 父级控件的Tag
     */
    public void bindView(String tag) {

        for (SuperBadgeHelper pater : paterBadge) {
            if (pater.getTag().equals(tag)) {
                //  throw new IllegalArgumentException("不能重复添加相同控件");
                return;
            }
        }

        SuperBadgeHelper paterBadgeHelper = SuperBadgeDater.getInstance().getBadge(context,tag);
        if (paterBadgeHelper != null) {
            paterBadge.add(paterBadgeHelper); //添加本级父控件
            paterBadgeHelper.addChild(this);//添加到父级子控件
        } else {
            throw new NullPointerException("没有找到标记为[" + tag + "]的控件");
        }
    }


    public void bindView(SuperBadgeHelper pater) {
        bindView(pater.getTag());
    }


    private void addChild(SuperBadgeHelper superBadgeHelper) {
        if (superBadgeHelper != null) {
            childBadge.add(superBadgeHelper);
            paterAddNum(superBadgeHelper.getNum());
        }
    }


    /**
     * 传递关联的View
     *
     * @param num 减少的数字
     */
    private void changeBadge(int num) {

        if (num > 0) {
            //传递变化到上级控件
            for (SuperBadgeHelper bean : paterBadge) {
                if (bean != null && bean.getNum() != 0) {
                    bean.chlidLessNum(num);
                }
            }
        }

        //清空下级控件数字
        if (getNum() == 0) {
            if (childBadge.size() > 0) {
                for (SuperBadgeHelper bean : childBadge) {
                    if (bean != null) {
                        bean.read();
                    }
                }

            }
        }
    }

    /**
     * 是否显示角标
     *
     * @param b
     */
    public void setShowBadge(boolean b) {
        this.show = b;
    }




    public interface OnNumCallback {
        int lodingNum();//加载数字方法
    }


}
