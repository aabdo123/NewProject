package com.views;

/**
 * Created by sadra on 7/29/17.
 */


import android.view.View;

import com.models.NLevelItemModel;

public interface NLevelView {

    public View getView(NLevelItemModel item);
}