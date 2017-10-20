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

public enum Language {

    C("C"),
    CPP("C++"),
    CSHARP("C#"),
    FSHARP("F#"),
    GROOVY("Groovy"),
    JAVA("Java"),
    JAVASCRIPT("JavaScript"),
    PHP("PHP"),
    PYTHON("Python"),
    SCALA("Scala"),
    SQL("SQL");

    private final String display;

    Language(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

}