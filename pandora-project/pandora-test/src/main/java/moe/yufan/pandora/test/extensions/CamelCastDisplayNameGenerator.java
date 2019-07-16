/*
 * Copyright (c) 2019 Artistian and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package moe.yufan.pandora.test.extensions;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetterOrDigit;
import static java.lang.Character.isUpperCase;

/**
 * Change the camelcase or underscore method name into a readable display name
 *
 * @author せいうはん
 * @version 1.0.0, 2019-07-16 21:00
 * @since 1.0.0, 2019-07-16 21:00
 */
public class CamelCastDisplayNameGenerator implements DisplayNameGenerator {

    private static final DisplayNameGenerator INTERNAL = new Standard();

    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return INTERNAL.generateDisplayNameForClass(testClass);
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return INTERNAL.generateDisplayNameForNestedClass(nestedClass);
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        String methodName = testMethod == null ? "" : removeExtraClassTag(testMethod.getName());
        return camelToText(methodName).replaceAll("_", " ");
    }

    /**
     * This method was copy from https://github.com/krasa/StringManipulation
     */
    @SuppressWarnings("squid:S3776")
    private static String camelToText(String text) {
        StringBuilder builder = new StringBuilder();
        char lastChar = ' ';
        for (char c : text.toCharArray()) {
            char nc = c;

            if (isUpperCase(nc) && !isUpperCase(lastChar)) {
                if (lastChar != ' ' && isLetterOrDigit(lastChar)) {
                    builder.append(" ");
                }
                nc = Character.toLowerCase(c);
            } else if (isDigit(lastChar) && !isDigit(c)) {
                if (lastChar != ' ') {
                    builder.append(" ");
                }
                nc = Character.toLowerCase(c);
            }

            if (lastChar != ' ' || c != ' ') {
                builder.append(nc);
            }
            lastChar = c;
        }
        return builder.toString();
    }

    /**
     * Kotlin based test sometime would have some extra module name be appended to the test method name.
     * We just need drop them for clean display name.
     */
    private String removeExtraClassTag(String name) {
        String nonNullName = name == null ? "" : name;
        if (nonNullName.contains("$")) {
            return nonNullName.substring(0, nonNullName.indexOf('$'));
        } else {
            return nonNullName;
        }
    }
}
