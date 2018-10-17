/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.android.server.backup.keyvalue;

import static com.google.common.truth.Truth.assertThat;

import android.platform.test.annotations.Presubmit;

import com.android.server.testing.FrameworkRobolectricTestRunner;
import com.android.server.testing.SystemLoaderPackages;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.IOException;

@RunWith(FrameworkRobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 26)
@SystemLoaderPackages({"com.android.server.backup"})
@Presubmit
public class AgentExceptionTest {
    @Test
    public void testTransitory_isTransitory() throws Exception {
        AgentException exception = AgentException.transitory();

        assertThat(exception.isTransitory()).isTrue();
    }

    @Test
    public void testTransitory_withCause() throws Exception {
        Exception cause = new IOException();

        AgentException exception = AgentException.transitory(cause);

        assertThat(exception.isTransitory()).isTrue();
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    public void testPermanent_isNotTransitory() throws Exception {
        AgentException exception = AgentException.permanent();

        assertThat(exception.isTransitory()).isFalse();
    }

    @Test
    public void testPermanent_withCause() throws Exception {
        Exception cause = new IOException();

        AgentException exception = AgentException.permanent(cause);

        assertThat(exception.isTransitory()).isFalse();
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}
