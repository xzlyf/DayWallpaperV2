package com.xz.daywallpaper.presenter;

import com.xz.daywallpaper.base.BaseActivity;
import com.xz.daywallpaper.model.Model;

public class Presenter {
    private Model model;
    private BaseActivity view;

    public Presenter(BaseActivity view) {
        this.view = view;
        model = new Model();
        view.dismissLoading();
    }

}
