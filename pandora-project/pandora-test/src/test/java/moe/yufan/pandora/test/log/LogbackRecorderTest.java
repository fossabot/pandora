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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;

/**
 * @author せいうはん
 */
@StaticTest
@SuppressWarnings({"squid:S1192", "squid:S00100"})
@DisplayNameGeneration(CamelCastDisplayNameGenerator.class)
class LogbackRecorderTest {

    private static final String[] TEST_MESSAGES = {"error", "warn", "info", "debug", "trace"};
    private static final Object[] TEST_ARGUMENTS = {null, true, 1, 2D, 3F};

    private final Logger log = LoggerFactory.getLogger(LogbackRecorderTest.class);
    private final Marker marker = MarkerFactory.getMarker(log.getName());

    private final Exception exception = new RuntimeException("Eek");

    private LogbackRecorder recorder = LogbackRecorder.forLogger(log);

    @BeforeEach
    void setup() {
        recorder.reset();
    }

    @Test
    void testTrace() {
        recorder.capture("TRACE");

        write();

        List<LogbackRecorder.Event> events = recorder.play();
        assertThat(events).hasSize(5);

        recordEventAssertion(events);

        recorder.release();
    }

    @Test
    void testTraceWithException() {
        recorder.capture("TRACE");

        writeWithException();

        List<LogbackRecorder.Event> events = recorder.play();
        assertThat(events).hasSize(5);

        recordedExceptionAssertion(events);

        recorder.release();
    }

    @Test
    void testDebug() {
        recorder.capture("DEBUG");

        write();

        List<LogbackRecorder.Event> events = recorder.play();
        assertThat(events).hasSize(4);

        recordEventAssertion(events);

        recorder.release();
    }

    @Test
    void testDebugWithException() {
        recorder.capture("DEBUG");

        writeWithException();

        List<LogbackRecorder.Event> events = recorder.play();
        assertThat(events).hasSize(4);

        recordedExceptionAssertion(events);

        recorder.release();
    }

    @Test
    void testInfo() {
        recorder.capture("INFO");

        write();

        List<LogbackRecorder.Event> events = recorder.play();
        assertThat(events).hasSize(3);

        recordEventAssertion(events);

        recorder.release();
    }

    @Test
    void testInfoWithException() {
        recorder.capture("INFO");

        writeWithException();

        List<LogbackRecorder.Event> events = recorder.play();
        assertThat(events).hasSize(3);

        recordedExceptionAssertion(events);

        recorder.release();
    }

    @Test
    void testWarn() {
        recorder.capture("WARN");

        write();

        List<LogbackRecorder.Event> events = recorder.play();
        assertThat(events).hasSize(2);

        recordEventAssertion(events);

        recorder.release();
    }

    @Test
    void testWarnWithException() {
        recorder.capture("WARN");

        writeWithException();

        List<LogbackRecorder.Event> events = recorder.play();
        assertThat(events).hasSize(2);

        recordedExceptionAssertion(events);

        recorder.release();
    }

    @Test
    void testError() {
        recorder.capture("ERROR");

        write();

        List<LogbackRecorder.Event> events = recorder.play();
        assertThat(events).hasSize(1);

        LogbackRecorder.Event event = events.get(0);
        assertThat(event.getMarker()).isEqualTo(marker);
        assertThat(event.getLevel()).isEqualTo(TEST_MESSAGES[0].toUpperCase());
        assertThat(event.getMessage()).startsWith(TEST_MESSAGES[0]);
        assertThat(event.getArguments()).hasSize(1);
        assertThat(event.getArguments()[0]).isEqualTo(TEST_ARGUMENTS[0]);
        assertThat(event.getThrown()).isNull();

        recorder.release();
    }

    @Test
    void testErrorWithException() {
        recorder.capture("ERROR");

        writeWithException();

        List<LogbackRecorder.Event> events = recorder.play();
        assertThat(events).hasSize(1);

        LogbackRecorder.Event event = events.get(0);
        assertThat(event.getMarker()).isEqualTo(marker);
        assertThat(event.getLevel()).isEqualTo(TEST_MESSAGES[0].toUpperCase());
        assertThat(event.getMessage()).startsWith(TEST_MESSAGES[0]);
        assertThat(event.getArguments()).hasSize(1);
        assertThat(event.getArguments()[0]).isEqualTo(TEST_ARGUMENTS[0]);
        assertThat(event.getThrown()).isEqualTo(exception.toString());

        recorder.release();
    }

    @Test
    void testOff() {
        recorder.capture("OFF");

        write();

        List<LogbackRecorder.Event> events = recorder.play();
        assertThat(events).isEmpty();

        recorder.release();
    }

    @Test
    void testOffWithException() {
        recorder.capture("OFF");

        writeWithException();

        List<LogbackRecorder.Event> events = recorder.play();
        assertThat(events).isEmpty();

        recorder.release();
    }

    @Test
    void testLogbackException() {
        Throwable caught = catchThrowable(() -> {
            LogbackRecorder.forLogger(mock(Logger.class));
        });
        assertThat(caught).isInstanceOf(IllegalArgumentException.class);
        assertThat(caught).hasMessage(LogbackRecorder.LOGBACK_EXCEPTION_MESSAGE);
    }

    @Test
    void testCaptureException() {
        recorder.capture("ALL");
        Throwable caught = catchThrowable(() -> {
            recorder.capture("ALL");
        });
        assertThat(caught).isInstanceOf(IllegalStateException.class);
        assertThat(caught).hasMessage(LogbackRecorder.CAPTURE_EXCEPTION_MESSAGE);
        recorder.release();
    }

    @Test
    void testReleaseException() {
        Throwable caught = catchThrowable(() -> {
            recorder.release();
        });
        assertThat(caught).isInstanceOf(IllegalStateException.class);
        assertThat(caught).hasMessage(LogbackRecorder.RELEASE_EXCEPTION_MESSAGE);
    }

    @Test
    void testClear() {
        recorder.capture("TRACE");

        write();

        recorder.release();

        assertThat(recorder.play()).hasSize(5);
        recorder.reset();
        assertThat(recorder.play()).isEmpty();
    }

    private void recordedExceptionAssertion(List<LogbackRecorder.Event> events) {
        for (int i = 0; i < events.size(); i++) {
            LogbackRecorder.Event event = events.get(i);
            assertThat(event.getMarker()).isEqualTo(marker);
            assertThat(event.getLevel()).isEqualTo(TEST_MESSAGES[i].toUpperCase());
            assertThat(event.getMessage()).startsWith(TEST_MESSAGES[i]);
            assertThat(event.getArguments()).hasSize(1);
            assertThat(event.getArguments()[0]).isEqualTo(TEST_ARGUMENTS[i]);
            assertThat(event.getThrown()).isEqualTo(exception.toString());
        }
    }

    private void recordEventAssertion(List<LogbackRecorder.Event> events) {
        for (int i = 0; i < events.size(); i++) {
            LogbackRecorder.Event event = events.get(i);
            assertThat(event.getMarker()).isEqualTo(marker);
            assertThat(event.getLevel()).isEqualTo(TEST_MESSAGES[i].toUpperCase());
            assertThat(event.getMessage()).startsWith(TEST_MESSAGES[i]);
            assertThat(event.getArguments()).hasSize(1);
            assertThat(event.getArguments()[0]).isEqualTo(TEST_ARGUMENTS[i]);
            assertThat(event.getThrown()).isNull();
        }
    }

    private void write() {
        log.error(marker, TEST_MESSAGES[0] + " {}", TEST_ARGUMENTS[0]);
        log.warn(marker, TEST_MESSAGES[1] + " {}", TEST_ARGUMENTS[1]);
        log.info(marker, TEST_MESSAGES[2] + " {}", TEST_ARGUMENTS[2]);
        log.debug(marker, TEST_MESSAGES[3] + " {}", TEST_ARGUMENTS[3]);
        log.trace(marker, TEST_MESSAGES[4] + " {}", TEST_ARGUMENTS[4]);
    }

    private void writeWithException() {
        log.error(marker, TEST_MESSAGES[0] + " {}", TEST_ARGUMENTS[0], exception);
        log.warn(marker, TEST_MESSAGES[1] + " {}", TEST_ARGUMENTS[1], exception);
        log.info(marker, TEST_MESSAGES[2] + " {}", TEST_ARGUMENTS[2], exception);
        log.debug(marker, TEST_MESSAGES[3] + " {}", TEST_ARGUMENTS[3], exception);
        log.trace(marker, TEST_MESSAGES[4] + " {}", TEST_ARGUMENTS[4], exception);
    }
}
