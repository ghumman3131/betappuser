package com.inception.betappuser;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by Mohit pal on 3/7/2018.
 */

public class betting_dialouge2 extends Dialog {
    public betting_dialouge2(@NonNull Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setContentView(R.layout.dialouge_betting);
    }
}
