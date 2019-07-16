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
package moe.yufan.pandora.test.metas;

import lombok.experimental.UtilityClass;

/**
 * A tag identity for grouping tests, used for every JUnit 5 based test in euphoria project
 *
 * @author せいうはん
 * @version 1.0.0, 2019-07-16 21:00
 * @see org.junit.jupiter.api.Tag
 * @see org.junit.jupiter.api.Tags
 * @since 1.0.0, 2019-07-16 21:00
 */
@UtilityClass
public final class TagMetas {

    /**
     * The test marked with this tag means that it doesn't need some dynamic environments, just execute it in IDE is also OK.
     * If it was marked on class, all the test in its test class should be executed separately.
     * <p>
     * A extra implicit {@code tokusei} in japanese aka. {@code feature} is that the test should be passed quickly.
     * Any test's execution time exceed 5 seconds shouldn't be marked with this identity.
     */
    public static final String STATIC = "static";

    /**
     * The test marked with this means it must be executed with some extra dynamic context.
     * Such as a embedded db or cache, a relative execution order of the tests, a outside database in test environment.
     * <p>
     * Dynamic tests execution speed could be kinda slow, because every test class need to ensure the environment.
     * Sometimes it would be hard to run it directly on a local IDE.
     * <p>
     * A ci platform like Jenkins or Bamboo would be the best man to verify these tests.
     */
    public static final String DYNAMIC = "dynamic";

    /**
     * The test marked with this means it should be run on a flink based environment.
     * Some predefined parameter should be provided before the test execution.
     * <p>
     * Flink could be a dynamic test or not, but make it separately could accomplish some business purpose easily.
     */
    public static final String INTEGRATION = "integration";
}
