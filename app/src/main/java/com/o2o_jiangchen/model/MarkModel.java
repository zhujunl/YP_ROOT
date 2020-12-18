package com.o2o_jiangchen.model;

import ai.yunji.water.entity.Marker;

/**
 * Created by Administrator on 2020/5/12.
 */

public class MarkModel extends Marker {
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
