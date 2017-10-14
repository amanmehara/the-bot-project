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
 along with Programming!. If not, see <http://www.gnu.org/licenses/>.

 */

package com.amanmehara.programming.android.common;

/**
 * Created by @amanmehara on 29-06-2017.
 */
public enum Type {

    FILE("file"),
    DIRECTORY("dir");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
