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
name|DateTimeException
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
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormatter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormatterBuilder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormatter
operator|.
name|ISO_LOCAL_DATE_TIME
import|;
end_import

begin_comment
comment|/**  * Utility class for working with DateTime fields.  */
end_comment

begin_class
DECL|class|DateTimeUtils
specifier|public
specifier|abstract
class|class
name|DateTimeUtils
block|{
DECL|field|BAD_TZ_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|BAD_TZ_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[+-][0-9]{4}+$"
argument_list|)
decl_stmt|;
DECL|field|ISO_8601_FORMATTER
specifier|private
specifier|static
specifier|final
name|DateTimeFormatter
name|ISO_8601_FORMATTER
init|=
operator|new
name|DateTimeFormatterBuilder
argument_list|()
operator|.
name|parseCaseInsensitive
argument_list|()
operator|.
name|append
argument_list|(
name|ISO_LOCAL_DATE_TIME
argument_list|)
operator|.
name|appendPattern
argument_list|(
literal|"[.S][,S]"
argument_list|)
operator|.
name|appendOffsetId
argument_list|()
operator|.
name|toFormatter
argument_list|()
decl_stmt|;
DECL|method|formatDateTime (ZonedDateTime dateTime)
specifier|public
specifier|static
name|String
name|formatDateTime
parameter_list|(
name|ZonedDateTime
name|dateTime
parameter_list|)
throws|throws
name|DateTimeException
block|{
return|return
name|ISO_8601_FORMATTER
operator|.
name|format
argument_list|(
name|dateTime
argument_list|)
return|;
block|}
DECL|method|parseDateTime (String dateTimeStr)
specifier|public
specifier|static
name|ZonedDateTime
name|parseDateTime
parameter_list|(
name|String
name|dateTimeStr
parameter_list|)
throws|throws
name|DateTimeParseException
block|{
return|return
name|ISO_8601_FORMATTER
operator|.
name|parse
argument_list|(
name|normalizeDateTime
argument_list|(
name|dateTimeStr
argument_list|)
argument_list|,
name|ZonedDateTime
operator|::
name|from
argument_list|)
return|;
block|}
DECL|method|normalizeDateTime (String dateTimeAsString)
specifier|private
specifier|static
name|String
name|normalizeDateTime
parameter_list|(
name|String
name|dateTimeAsString
parameter_list|)
block|{
if|if
condition|(
name|BAD_TZ_PATTERN
operator|.
name|matcher
argument_list|(
name|dateTimeAsString
argument_list|)
operator|.
name|find
argument_list|()
condition|)
block|{
name|int
name|splitAt
init|=
name|dateTimeAsString
operator|.
name|length
argument_list|()
operator|-
literal|2
decl_stmt|;
name|dateTimeAsString
operator|=
name|dateTimeAsString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|splitAt
argument_list|)
operator|+
literal|":"
operator|+
name|dateTimeAsString
operator|.
name|substring
argument_list|(
name|splitAt
argument_list|)
expr_stmt|;
block|}
return|return
name|dateTimeAsString
return|;
block|}
block|}
end_class

end_unit

