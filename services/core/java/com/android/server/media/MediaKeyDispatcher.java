/*
 * Copyright 2020 The Android Open Source Project
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
 * limitations under the License.
 */

package com.android.server.media;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.media.session.MediaSession;
import android.view.KeyEvent;

/**
 * Provides a way to customize behavior for media key events.
 */
public interface MediaKeyDispatcher {
    /**
     * Implement this to customize the logic for which MediaSession should consume which key event.
     *
     * @param keyEvent a non-null KeyEvent whose key code is one of the supported media buttons.
     * @param asSystemService {@code true} if the event came from the system service via hardware
     *         devices. {@code false} if the event came from the app process through key injection.
     * @return a {@link MediaSession.Token} instance that should consume the given key event.
     */
    @Nullable
    MediaSession.Token getSessionForKeyEvent(@NonNull KeyEvent keyEvent,
            boolean asSystemService);
}
