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
package moe.yufan.pandora.test.log;

import moe.yufan.pandora.test.extensions.CamelCastDisplayNameGenerator;
import moe.yufan.pandora.test.metas.StaticTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author せいうはん
 */
@StaticTest
@SuppressWarnings({"squid:S1192", "squid:S00100"})
@DisplayNameGeneration(CamelCastDisplayNameGenerator.class)
class LogbackListAppenderTest {

    @Test
    void normal() {
        String testString1 = "Hello";
        String testString2 = "World";
        LogbackListAppender appender = new LogbackListAppender();
        appender.addToLogger(LogbackListAppenderTest.class);

        // null
        assertThat(appender.getFirstLog()).isNull();
        assertThat(appender.getLastLog()).isNull();
        assertThat(appender.getFirstMessage()).isNull();
        assertThat(appender.getFirstMessage()).isNull();

        Logger logger = LoggerFactory.getLogger(LogbackListAppenderTest.class);
        logger.warn(testString1);
        logger.warn(testString2);

        // getFirstLog/getLastLog
        assertThat(appender.getFirstLog().getMessage()).isEqualTo(testString1);
        assertThat(appender.getLastLog().getMessage()).isEqualTo(testString2);

        assertThat(appender.getFirstMessage()).isEqualTo(testString1);
        assertThat(appender.getLastMessage()).isEqualTo(testString2);

        // getAllLogs
        assertThat(appender.getLogsCount()).isEqualTo(2);
        assertThat(appender.getAllLogs()).hasSize(2);
        assertThat(appender.getAllLogs().get(1).getMessage()).isEqualTo(testString2);

        // clearLogs
        appender.clearLogs();
        assertThat(appender.getFirstLog()).isNull();
        assertThat(appender.getLastLog()).isNull();
    }

    @Test
    void addAndRemoveAppender() {
        String testString = "Hello";
        Logger logger = LoggerFactory.getLogger(LogbackListAppenderTest.class);
        LogbackListAppender appender = new LogbackListAppender();
        // class
        appender.addToLogger(LogbackListAppenderTest.class);
        logger.warn(testString);
        assertThat(appender.getFirstLog()).isNotNull();

        appender.clearLogs();
        appender.removeFromLogger(LogbackListAppenderTest.class);
        logger.warn(testString);
        assertThat(appender.getFirstLog()).isNull();

        // name
        appender.clearLogs();
        appender.addToLogger("moe.yufan.pandora.test.log");
        logger.warn(testString);
        assertThat(appender.getFirstLog()).isNotNull();

        appender.clearLogs();
        appender.removeFromLogger("moe.yufan.pandora.test.log");
        logger.warn(testString);
        assertThat(appender.getFirstLog()).isNull();
    }
}
