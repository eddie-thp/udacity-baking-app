package org.ethp.udacitybakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class IngredientsListRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListRemoteViewsFactory(getApplicationContext());
    }

}
