/*

 Copyright (C) 2015 - 2017 Aman Mehara

 This file is part of Programming!

 Programming! is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Programming! is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Foobar. If not, see <http://www.gnu.org/licenses/>.

 */

package com.amanmehara.programming.android.activities.enumeration;

import com.amanmehara.programming.android.activities.BaseActivity;
import com.amanmehara.programming.android.activities.ConnectionActivity;
import com.amanmehara.programming.android.activities.DetailActivity;
import com.amanmehara.programming.android.activities.GithubOAuthActivity;
import com.amanmehara.programming.android.activities.LanguageActivity;
import com.amanmehara.programming.android.activities.MainActivity;
import com.amanmehara.programming.android.activities.ProgramActivity;

/**
 * Created by @amanmehara on 20-06-2017.
 */
public enum Activity {

    CONNECTION(ConnectionActivity.class),
    DETAIL(DetailActivity.class),
    GITHUB(GithubOAuthActivity.class),
    LANGUAGE(LanguageActivity.class),
    MAIN(MainActivity.class),
    PROGRAM(ProgramActivity.class);

    private final Class<? extends BaseActivity> clazz;

    Activity(Class<? extends BaseActivity> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends BaseActivity> getClazz() {
        return clazz;
    }

}
