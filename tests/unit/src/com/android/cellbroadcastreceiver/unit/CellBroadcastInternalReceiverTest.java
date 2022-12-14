/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.android.cellbroadcastreceiver.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.UserManager;

import com.android.cellbroadcastreceiver.CellBroadcastInternalReceiver;
import com.android.cellbroadcastreceiver.CellBroadcastReceiver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CellBroadcastInternalReceiverTest extends CellBroadcastTest {

    @Mock Intent mIntent;
    @Mock UserManager mUserManager;

    private Configuration mConfiguration = new Configuration();
    private CellBroadcastInternalReceiver mReceiver;

    @Before
    public void setUp() throws Exception {
        super.setUp(this.getClass().getSimpleName());
        MockitoAnnotations.initMocks(this);
        doReturn(mConfiguration).when(mResources).getConfiguration();
        mReceiver = spy(new CellBroadcastInternalReceiver());
        doReturn(mContext).when(mContext).getApplicationContext();
        doReturn(mUserManager).when(mContext).getSystemService(Context.USER_SERVICE);
        doReturn(false).when(mUserManager).isSystemUser();
    }

    @Test
    public void testOnReceive_actionMarkAsRead() {
        doReturn(CellBroadcastReceiver.ACTION_MARK_AS_READ).when(mIntent).getAction();
        doNothing().when(mReceiver).getCellBroadcastTask(nullable(Context.class), anyLong());
        mReceiver.onReceive(mContext, mIntent);
        verify(mIntent).getLongExtra(CellBroadcastReceiver.EXTRA_DELIVERY_TIME, -1);
        verify(mReceiver).getCellBroadcastTask(nullable(Context.class), anyLong());
    }

    @Test
    public void testOnReceive_cellbroadcastStartConfigAction() {
        doReturn(CellBroadcastReceiver.CELLBROADCAST_START_CONFIG_ACTION).when(mIntent).getAction();
        mReceiver.onReceive(mContext, mIntent);

        verify(mReceiver).startConfigServiceToEnableChannels(any());
    }

}
