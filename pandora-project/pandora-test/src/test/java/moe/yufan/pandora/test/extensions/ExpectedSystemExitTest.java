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

import moe.yufan.pandora.test.extensions.ExpectedSystemExit.ExitCapture;
import moe.yufan.pandora.test.metas.StaticTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author せいうはん
 */
@StaticTest
@SuppressWarnings({"squid:S1192", "squid:S00100"})
@ExpectedSystemExit
class ExpectedSystemExitTest {

    @Test
    @DisplayName("A test for expecting a System.exit call")
    void systemExit(ExitCapture capture) {
        capture.expectSystemExit();
        System.exit(-1);
    }

    @Test
    @DisplayName("A test for expecting a System.exit call with status code assertion")
    void systemExitWithStatus(ExitCapture capture) {
        capture.expectSystemExitWithStatus(100);
        System.exit(100);
    }
}
