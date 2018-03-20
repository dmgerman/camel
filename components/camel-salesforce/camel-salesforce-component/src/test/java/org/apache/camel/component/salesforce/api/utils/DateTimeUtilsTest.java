begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|OffsetTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneId
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneOffset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|DateTimeUtilsTest
specifier|public
class|class
name|DateTimeUtilsTest
block|{
annotation|@
name|Test
DECL|method|testFormatDateTime ()
specifier|public
name|void
name|testFormatDateTime
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"1991-12-10T12:13:14.007+01:00"
argument_list|,
name|DateTimeUtils
operator|.
name|formatDateTime
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|1991
argument_list|,
literal|12
argument_list|,
literal|10
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|7000000
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"UTC+01:00:21"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1991-12-10T12:13:14.007Z"
argument_list|,
name|DateTimeUtils
operator|.
name|formatDateTime
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|1991
argument_list|,
literal|12
argument_list|,
literal|10
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|7000000
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"UTC"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1700-01-01T01:13:14.007+00:19"
argument_list|,
name|DateTimeUtils
operator|.
name|formatDateTime
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|1700
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|7000000
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"UTC+00:19:21"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1700-02-03T02:13:14.007Z"
argument_list|,
name|DateTimeUtils
operator|.
name|formatDateTime
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|1700
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|2
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|7000000
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"UTC"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseDateTime ()
specifier|public
name|void
name|testParseDateTime
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|1991
argument_list|,
literal|12
argument_list|,
literal|10
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|7000000
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"+01:00"
argument_list|)
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseDateTime
argument_list|(
literal|"1991-12-10T12:13:14.007+01:00"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|1991
argument_list|,
literal|12
argument_list|,
literal|10
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|0
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"Z"
argument_list|)
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseDateTime
argument_list|(
literal|"1991-12-10T12:13:14+00:00"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|1991
argument_list|,
literal|12
argument_list|,
literal|10
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|0
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"Z"
argument_list|)
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseDateTime
argument_list|(
literal|"1991-12-10T12:13:14.000+00:00"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|1991
argument_list|,
literal|12
argument_list|,
literal|10
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|0
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"Z"
argument_list|)
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseDateTime
argument_list|(
literal|"1991-12-10T12:13:14+0000"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|1991
argument_list|,
literal|12
argument_list|,
literal|10
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|0
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"Z"
argument_list|)
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseDateTime
argument_list|(
literal|"1991-12-10T12:13:14.000+0000"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|1991
argument_list|,
literal|12
argument_list|,
literal|10
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|7000000
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"Z"
argument_list|)
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseDateTime
argument_list|(
literal|"1991-12-10T12:13:14.007Z"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|1700
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|7000000
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"+00:19"
argument_list|)
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseDateTime
argument_list|(
literal|"1700-01-01T01:13:14.007+00:19"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|1700
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|2
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|7000000
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"Z"
argument_list|)
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseDateTime
argument_list|(
literal|"1700-02-03T02:13:14.007Z"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFormatDate ()
specifier|public
name|void
name|testFormatDate
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"1991-12-10"
argument_list|,
name|DateTimeUtils
operator|.
name|formatDate
argument_list|(
name|LocalDate
operator|.
name|of
argument_list|(
literal|1991
argument_list|,
literal|12
argument_list|,
literal|10
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2100-12-10"
argument_list|,
name|DateTimeUtils
operator|.
name|formatDate
argument_list|(
name|LocalDate
operator|.
name|of
argument_list|(
literal|2100
argument_list|,
literal|12
argument_list|,
literal|10
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1700-01-01"
argument_list|,
name|DateTimeUtils
operator|.
name|formatDate
argument_list|(
name|LocalDate
operator|.
name|of
argument_list|(
literal|1700
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseDate ()
specifier|public
name|void
name|testParseDate
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|LocalDate
operator|.
name|of
argument_list|(
literal|1700
argument_list|,
literal|01
argument_list|,
literal|01
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseDate
argument_list|(
literal|"1700-01-01"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LocalDate
operator|.
name|of
argument_list|(
literal|2100
argument_list|,
literal|12
argument_list|,
literal|10
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseDate
argument_list|(
literal|"2100-12-10"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LocalDate
operator|.
name|of
argument_list|(
literal|1700
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseDate
argument_list|(
literal|"1700-01-01"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFormatTime ()
specifier|public
name|void
name|testFormatTime
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"12:13:14.007"
argument_list|,
name|DateTimeUtils
operator|.
name|formatTime
argument_list|(
name|OffsetTime
operator|.
name|of
argument_list|(
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|7000000
argument_list|,
name|ZoneOffset
operator|.
name|UTC
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"01:00:00"
argument_list|,
name|DateTimeUtils
operator|.
name|formatTime
argument_list|(
name|OffsetTime
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|ZoneOffset
operator|.
name|UTC
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"00:00:00"
argument_list|,
name|DateTimeUtils
operator|.
name|formatTime
argument_list|(
name|OffsetTime
operator|.
name|of
argument_list|(
name|LocalTime
operator|.
name|MIDNIGHT
argument_list|,
name|ZoneOffset
operator|.
name|UTC
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseTime ()
specifier|public
name|void
name|testParseTime
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|OffsetTime
operator|.
name|of
argument_list|(
name|LocalTime
operator|.
name|MIDNIGHT
argument_list|,
name|ZoneOffset
operator|.
name|UTC
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseTime
argument_list|(
literal|"00:00:00.000"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|OffsetTime
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|100
argument_list|,
name|ZoneOffset
operator|.
name|UTC
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseTime
argument_list|(
literal|"01:00:00.0000001"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|OffsetTime
operator|.
name|of
argument_list|(
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|7000000
argument_list|,
name|ZoneOffset
operator|.
name|UTC
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseTime
argument_list|(
literal|"12:13:14.007"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|OffsetTime
operator|.
name|of
argument_list|(
literal|12
argument_list|,
literal|13
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|ZoneOffset
operator|.
name|UTC
argument_list|)
argument_list|,
name|DateTimeUtils
operator|.
name|parseTime
argument_list|(
literal|"12:13"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

