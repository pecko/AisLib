/* Copyright (c) 2011 Danish Maritime Authority.
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
package dk.dma.ais.message;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.proprietary.IProprietarySourceTag;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.proprietary.ProprietaryFactory;
import dk.dma.ais.sentence.SentenceLine;

public class ProprietaryTest {

    @Test
    public void pTest() {
        Assert.assertNotNull(ProprietaryFactory.parseTag(new SentenceLine("$PGHP,1,2010,6,11,11,46,11,874,276,0,,1,55*2C")));
    }
    
    @Test
    public void nonSourceTagTest() {
        Assert.assertNull(ProprietaryFactory.parseTag(new SentenceLine("$PGHP,27,1*3B")));
    }

    @Test
    public void ghTimeTest() {
        IProprietaryTag tag = ProprietaryFactory.parseTag(new SentenceLine("$PGHP,1,2010,6,11,11,46,11,874,276,0,,1,55*2C"));
        Assert.assertNotNull(tag);
        Assert.assertTrue(tag instanceof IProprietarySourceTag);
        IProprietarySourceTag sourceTag = (IProprietarySourceTag) tag;
        Date timestamp = sourceTag.getTimestamp();
        Assert.assertNotNull(timestamp);
        DateTime dt = new DateTime(timestamp, DateTimeZone.UTC);
        Assert.assertEquals(dt.getYear(), 2010);
        Assert.assertEquals(dt.getMonthOfYear(), 6);
        Assert.assertEquals(dt.getDayOfMonth(), 11);
        Assert.assertEquals(dt.getHourOfDay(), 11);
        Assert.assertEquals(dt.getMinuteOfHour(), 46);
        Assert.assertEquals(dt.getSecondOfMinute(), 11);
        Assert.assertEquals(dt.getMillisOfSecond(), 874);
    }

    @Test
    public void ghTest() {
        SentenceLine sl = new SentenceLine("$PGHP,1,2013,8,23,8,34,27,56,219,,2190066,1,10*22");
        Assert.assertTrue(sl.isChecksumMatch());
        IProprietaryTag tag = ProprietaryFactory.parseTag(sl);
        Assert.assertNotNull(tag);
        Assert.assertTrue(tag instanceof IProprietarySourceTag);
        IProprietarySourceTag sourceTag = (IProprietarySourceTag) tag;

        Date timestamp = sourceTag.getTimestamp();
        Assert.assertNotNull(timestamp);
        DateTime dt = new DateTime(timestamp, DateTimeZone.UTC);
        Assert.assertEquals(dt.getYear(), 2013);
        Assert.assertEquals(dt.getMonthOfYear(), 8);
        Assert.assertEquals(dt.getDayOfMonth(), 23);
        Assert.assertEquals(dt.getHourOfDay(), 8);
        Assert.assertEquals(dt.getMinuteOfHour(), 34);
        Assert.assertEquals(dt.getSecondOfMinute(), 27);
        Assert.assertEquals(dt.getMillisOfSecond(), 56);

        Assert.assertEquals(sourceTag.getBaseMmsi().intValue(), 2190066);
        Assert.assertEquals(sourceTag.getRegion(), "");
        Assert.assertEquals(sourceTag.getCountry().getThreeLetter(), "DNK");

    }

}
