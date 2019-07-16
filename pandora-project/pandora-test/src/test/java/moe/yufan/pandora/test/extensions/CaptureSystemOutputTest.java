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

import moe.yufan.pandora.test.extensions.CaptureSystemOutput.OutputCapture;
import moe.yufan.pandora.test.metas.StaticTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * A sample class for displaying the detailed usage for {@link CaptureSystemOutput}
 *
 * @author せいうはん
 */
@StaticTest
@SuppressWarnings("squid:S106")
@CaptureSystemOutput
class CaptureSystemOutputTest {

    @Test
    @DisplayName("system out assert")
    void systemOutAssert(OutputCapture capture) {
        capture.expect(str -> assertThat(str).matches("^This is a sample out."));
        assertAll(() -> System.out.print("This is a sample out."));
    }

    @Test
    @DisplayName("system err assert")
    void systemErrAssert(OutputCapture capture) {
        capture.expect(str -> assertThat(str).matches("^This is a sample err."));
        assertAll(() -> System.err.print("This is a sample err."));
    }
}
