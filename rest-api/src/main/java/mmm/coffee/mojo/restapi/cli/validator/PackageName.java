/*
 * Copyright 2020 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmm.coffee.mojo.restapi.cli.validator;

// Source from:  https://stackoverflow.com/questions/13557195/how-to-check-if-string-is-a-valid-class-identifier/13557237

import java.util.Arrays;

public enum PackageName {

    SIMPLE, QUALIFIED, INVALID;

    public static final PackageName check(String name) {
        PackageName ret = PackageName.INVALID;
        int[] codePoint;
        int index = 0, dotex = -1;
        boolean needStart = true;
        escape: {
            if(name == null || name.isEmpty()) break escape;
            if(name.codePointAt(0) == '.') break escape;
            codePoint = name.codePoints().toArray();
            while (index <= codePoint.length) {
                if(index == codePoint.length) {
                    if(codePoint[index - 1] == '.'){
                        ret = PackageName.INVALID; break escape;}
                    int start = dotex + 1;
                    int end = index;
                    start = name.offsetByCodePoints(0, start);
                    end = name.offsetByCodePoints(0, end);
                    String test = name.substring(start, end);
                    if(!(Arrays.binarySearch(reserved, test) < 0)){
                        ret = PackageName.INVALID; break escape;}
                    if(!(ret == PackageName.QUALIFIED)) ret = PackageName.SIMPLE;
                    break escape;
                }
                if(codePoint[index] == '.') {
                    if(codePoint[index - 1] == '.'){
                        ret = PackageName.INVALID;  break escape;}
                    else {
                        needStart = true;
                        int start = dotex + 1;
                        int end = index;
                        start = name.offsetByCodePoints(0, start);
                        end = name.offsetByCodePoints(0, end);
                        String test = name.substring(start, end);
                        if(!(Arrays.binarySearch(reserved, test) < 0)) break escape;
                        dotex = index;
                        ret = PackageName.QUALIFIED;
                    }
                } else if(Character.isJavaIdentifierStart(codePoint[index])) {
                    if(needStart) needStart = false;
                } else if((!Character.isJavaIdentifierPart(codePoint[index]))){
                    ret = PackageName.INVALID; break escape;
                }
                index++;
            }
        }
        return ret;
    }

    private static final String[] reserved;

    static {
        reserved = new String[] { "abstract", "assert", "boolean", "break", "byte",
                "case", "catch", "char", "class", "const", "continue", "default", "do",
                "double", "else", "enum", "extends", "false", "final", "finally",
                "float", "for", "if", "goto", "implements", "import", "instanceof",
                "int", "interface", "long", "native", "new", "null", "package",
                "private", "protected", "public", "return", "short", "static",
                "strictfp", "super", "switch", "synchronized", "this", "throw",
                "throws", "transient", "true", "try", "void", "volatile", "while" };
    }
}