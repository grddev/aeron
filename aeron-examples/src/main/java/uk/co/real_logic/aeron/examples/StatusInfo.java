/*
 * Copyright 2014 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.real_logic.aeron.examples;

import uk.co.real_logic.aeron.common.CommonContext;
import uk.co.real_logic.aeron.common.IoUtil;
import uk.co.real_logic.aeron.common.concurrent.AtomicBuffer;
import uk.co.real_logic.aeron.common.status.CountersManager;

import java.io.File;
import java.nio.MappedByteBuffer;

/**
 * App to print out status counters and labels
 */
public class StatusInfo
{
    public static void main(final String[] args)
    {
        final File labelsFile = new File(System.getProperty(CommonContext.COUNTERS_DIR_PROP_NAME,
                CommonContext.COUNTERS_DIR_PROP_DEFAULT), CommonContext.LABELS_FILE);
        final File valuesFile = new File(System.getProperty(CommonContext.COUNTERS_DIR_PROP_NAME,
                CommonContext.COUNTERS_DIR_PROP_DEFAULT), CommonContext.VALUES_FILE);

        System.out.println("Labels file " + labelsFile);
        System.out.println("Values file " + valuesFile);

        try
        {
            final MappedByteBuffer labelsByteBuffer = IoUtil.mapExistingFile(labelsFile, "labels");
            final MappedByteBuffer valuesByteBuffer = IoUtil.mapExistingFile(valuesFile, "values");

            final AtomicBuffer valuesBuffer = new AtomicBuffer(valuesByteBuffer);

            final CountersManager manager = new CountersManager(new AtomicBuffer(labelsByteBuffer), valuesBuffer);

            while (true)
            {
                final double timestamp = (double)System.nanoTime() / (double)(1000*1000*1000);

                manager.forEachLabel(
                    (id, label) ->
                    {
                        final int offset = CountersManager.counterOffset(id);
                        final long value = valuesBuffer.getLongVolatile(offset);

                        System.out.println(String.format("[%f] %d[%d]: <<%s>> = %d", timestamp, id, offset, label, value));
                    });

                Thread.sleep(1000);
            }
        }
        catch (final Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
