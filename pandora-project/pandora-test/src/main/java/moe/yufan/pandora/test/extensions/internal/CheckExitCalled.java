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
package moe.yufan.pandora.test.extensions.internal;

/**
 * The exception when meeting check exit issues.
 *
 * @author せいうはん
 * @version 1.0.0, 2019-07-16 21:00
 * @since 1.0.0, 2019-07-16 21:00
 */
public class CheckExitCalled extends SecurityException {
    private static final long serialVersionUID = 159678654L;

    private final Integer status;

    public CheckExitCalled(int status) {
        super("Tried to exit with status " + status + ".");
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
