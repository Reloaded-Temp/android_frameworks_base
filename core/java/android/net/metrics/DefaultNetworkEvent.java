/*
 * Copyright (C) 2016 The Android Open Source Project
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

package android.net.metrics;

import android.annotation.SystemApi;
import android.net.NetworkCapabilities;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * {@hide}
 */
@SystemApi
public final class DefaultNetworkEvent extends IpConnectivityEvent implements Parcelable {
    // The ID of the network that has become the new default or NETID_UNSET if none.
    public final int netId;
    // The list of transport types of the new default network, for example TRANSPORT_WIFI, as
    // defined in NetworkCapabilities.java.
    public final int[] transportTypes;
    // The ID of the network that was the default before or NETID_UNSET if none.
    public final int prevNetId;
    // Whether the previous network had IPv4/IPv6 connectivity.
    public final boolean prevIPv4;
    public final boolean prevIPv6;

    private DefaultNetworkEvent(int netId, int[] transportTypes,
                int prevNetId, boolean prevIPv4, boolean prevIPv6) {
        this.netId = netId;
        this.transportTypes = transportTypes;
        this.prevNetId = prevNetId;
        this.prevIPv4 = prevIPv4;
        this.prevIPv6 = prevIPv6;
    }

    private DefaultNetworkEvent(Parcel in) {
        this.netId = in.readInt();
        this.transportTypes = in.createIntArray();
        this.prevNetId = in.readInt();
        this.prevIPv4 = (in.readByte() > 0);
        this.prevIPv6 = (in.readByte() > 0);
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(netId);
        out.writeIntArray(transportTypes);
        out.writeInt(prevNetId);
        out.writeByte(prevIPv4 ? (byte) 1 : (byte) 0);
        out.writeByte(prevIPv6 ? (byte) 1 : (byte) 0);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
      return String.format("DefaultNetworkEvent(%d -> %d, %s, IPv4: %b, IPv6: %b)", prevNetId,
              netId, NetworkCapabilities.transportNamesOf(transportTypes), prevIPv4, prevIPv6);
    }

    public static final Parcelable.Creator<DefaultNetworkEvent> CREATOR
        = new Parcelable.Creator<DefaultNetworkEvent>() {
        public DefaultNetworkEvent createFromParcel(Parcel in) {
            return new DefaultNetworkEvent(in);
        }

        public DefaultNetworkEvent[] newArray(int size) {
            return new DefaultNetworkEvent[size];
        }
    };

    public static void logEvent(
            int netId, int[] transports, int prevNetId, boolean hadIPv4, boolean hadIPv6) {
        logEvent(new DefaultNetworkEvent(netId, transports, prevNetId, hadIPv4, hadIPv6));
    }
};
