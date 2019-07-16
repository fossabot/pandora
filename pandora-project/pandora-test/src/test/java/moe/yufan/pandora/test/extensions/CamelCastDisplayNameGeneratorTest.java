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

import moe.yufan.pandora.test.metas.StaticTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author せいうはん
 */
@StaticTest
@SuppressWarnings({"squid:S1192", "squid:S00100"})
@DisplayNameGeneration(CamelCastDisplayNameGenerator.class)
class CamelCastDisplayNameGeneratorTest {

    @Test
    void camelCaseCouldBeConvertToA_MoreBetterDisplayName(TestInfo testInfo) {
        assertEquals(
            "camel case could be convert to a more better display name",
            testInfo.getDisplayName()
        );
    }

    @Test
    void underscore_could_be_transform_to_display_name(TestInfo testInfo) {
        assertEquals(
            "underscore could be transform to display name",
            testInfo.getDisplayName()
        );
    }

    @Test
    void anything_after_dollar_in_method_name_would_be_removed$thisWouldBeRemove(TestInfo testInfo) {
        assertEquals(
            "anything after dollar in method name would be removed",
            testInfo.getDisplayName()
        );
    }

    @Test
    void number1ShouldAlsoBeSupported2(TestInfo testInfo) {
        assertEquals(
            "number1 should also be supported2",
            testInfo.getDisplayName()
        );
    }
}
