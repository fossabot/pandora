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
/**
 * <h1>Test Package</h1>
 * <p>
 * The tests were handled by JUnit 5. Because JUnit 5 could describe the test purpose and group the tests by using<br/>
 * {@code @DisplayName} and {@code @Tag}.
 * <p>
 * <h2>TagMetas</h2>
 * <p>
 * By using JUnit 5 we want to have a collect of common tags to make sure we could execute the tests we want.<br/>
 * {@code TagMeta} is a constance class for the tag identity to use in our test. It has there properties below:
 * <p>
 * <strong>static</strong>
 * <p>
 * The test marked with this tag means that it doesn&#39;t need some dynamic environments, just execute it in IDE is also OK.<br/>
 * If it was marked on class, all the test in its test class should be executed separately.
 * <p>
 * A extra implicit <strong>tokusei</strong>, in japanese, aka. <strong>feature</strong> is that the test should be passed quickly.<br/>
 * Any test&#39;s execution time exceed 5 seconds shouldn&#39;t be marked with this identity.
 * <p>
 * <strong>dynamic</strong>
 * <p>
 * The test marked with this means it must be executed with some extra dynamic context.<br/>
 * Such as a embedded db or cache, a relative execution order of the tests, a outside database in test environment.
 * <p>
 * Dynamic tests execution speed could be kinda slow, because every test class need to ensure the environment.<br/>
 * Sometimes it would be hard to run it directly on a local IDE.
 * <p>
 * A ci platform like Jenkins or Bamboo would be the best man to verify these tests.
 * <p>
 * <strong>integration</strong>
 * <p>
 * The test marked with this means it should be run in a production similar environment.<br/>
 * Some predefined parameter should be provided before the test execution.
 * <p>
 * Integration test could be a dynamic test or not, but make it separately for executing in maven {@code verify} goal.
 * <p>
 * <h2>Custom tag annotation</h2>
 * <p>
 * Since JUnit 5 support custom tag annotation, we define some tag annotation based on the {@code TagMetas} above.<br/>
 * They are {@code @DynamicTest} {@code @IntegrationTest} {@code @StaticTest}.
 * <p>
 * Any test in our project should be used with anyone of there tag annotations.
 *
 * @author せいうはん
 * @version 1.0.0, 2019-07-16 21:00
 * @since 1.0.0, 2019-07-16 21:00
 */
package moe.yufan.pandora.test;
