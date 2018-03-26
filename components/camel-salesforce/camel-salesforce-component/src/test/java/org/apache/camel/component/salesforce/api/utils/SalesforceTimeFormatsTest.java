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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
import|;
end_import

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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|String
operator|.
name|format
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonProcessingException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|JavaType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|type
operator|.
name|TypeFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|XStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|annotations
operator|.
name|XStreamAlias
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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|SalesforceTimeFormatsTest
specifier|public
class|class
name|SalesforceTimeFormatsTest
block|{
annotation|@
name|XStreamAlias
argument_list|(
literal|"Dto"
argument_list|)
DECL|class|DateTransferObject
specifier|public
specifier|static
class|class
name|DateTransferObject
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|value
specifier|private
name|T
name|value
decl_stmt|;
DECL|method|DateTransferObject ()
specifier|public
name|DateTransferObject
parameter_list|()
block|{         }
DECL|method|DateTransferObject (final T value)
specifier|public
name|DateTransferObject
parameter_list|(
specifier|final
name|T
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (final Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
specifier|final
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|DateTransferObject
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|DateTransferObject
argument_list|<
name|?
argument_list|>
name|dto
init|=
operator|(
name|DateTransferObject
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|value
argument_list|,
name|dto
operator|.
name|value
argument_list|)
return|;
block|}
DECL|method|getValue ()
specifier|public
name|T
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|value
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|setValue (final T value)
specifier|public
name|void
name|setValue
parameter_list|(
specifier|final
name|T
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
DECL|field|JSON_FMT
specifier|private
specifier|static
specifier|final
name|String
name|JSON_FMT
init|=
literal|"{\"value\":\"%s\"}"
decl_stmt|;
DECL|field|XML_FMT
specifier|private
specifier|static
specifier|final
name|String
name|XML_FMT
init|=
literal|"<Dto><value>%s</value></Dto>"
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
literal|0
argument_list|)
DECL|field|dto
specifier|public
name|DateTransferObject
argument_list|<
name|?
argument_list|>
name|dto
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
literal|1
argument_list|)
DECL|field|json
specifier|public
name|String
name|json
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
literal|3
argument_list|)
DECL|field|parameterType
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|parameterType
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
literal|2
argument_list|)
DECL|field|xml
specifier|public
name|String
name|xml
decl_stmt|;
DECL|field|objectMapper
specifier|private
specifier|final
name|ObjectMapper
name|objectMapper
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
DECL|field|xStream
specifier|private
specifier|final
name|XStream
name|xStream
init|=
name|XStreamUtils
operator|.
name|createXStream
argument_list|(
name|DateTransferObject
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldDeserializeJson ()
specifier|public
name|void
name|shouldDeserializeJson
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|JavaType
name|javaType
init|=
name|TypeFactory
operator|.
name|defaultInstance
argument_list|()
operator|.
name|constructParametricType
argument_list|(
name|DateTransferObject
operator|.
name|class
argument_list|,
name|parameterType
argument_list|)
decl_stmt|;
specifier|final
name|DateTransferObject
argument_list|<
name|?
argument_list|>
name|deserialized
init|=
name|objectMapper
operator|.
name|readerFor
argument_list|(
name|javaType
argument_list|)
operator|.
name|readValue
argument_list|(
name|json
argument_list|)
decl_stmt|;
name|assertDeserializationResult
argument_list|(
name|deserialized
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldDeserializeXml ()
specifier|public
name|void
name|shouldDeserializeXml
parameter_list|()
block|{
name|xStream
operator|.
name|addDefaultImplementation
argument_list|(
name|parameterType
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
specifier|final
name|DateTransferObject
argument_list|<
name|?
argument_list|>
name|deserialized
init|=
operator|(
name|DateTransferObject
argument_list|<
name|?
argument_list|>
operator|)
name|xStream
operator|.
name|fromXML
argument_list|(
name|xml
argument_list|)
decl_stmt|;
name|assertDeserializationResult
argument_list|(
name|deserialized
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSerializeJson ()
specifier|public
name|void
name|shouldSerializeJson
parameter_list|()
throws|throws
name|JsonProcessingException
block|{
name|assertThat
argument_list|(
name|objectMapper
operator|.
name|writeValueAsString
argument_list|(
name|dto
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|json
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSerializeXml ()
specifier|public
name|void
name|shouldSerializeXml
parameter_list|()
block|{
name|assertThat
argument_list|(
name|xStream
operator|.
name|toXML
argument_list|(
name|dto
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|xml
argument_list|)
expr_stmt|;
block|}
DECL|method|assertDeserializationResult (final DateTransferObject<?> deserialized)
specifier|private
name|void
name|assertDeserializationResult
parameter_list|(
specifier|final
name|DateTransferObject
argument_list|<
name|?
argument_list|>
name|deserialized
parameter_list|)
block|{
if|if
condition|(
name|dto
operator|.
name|value
operator|instanceof
name|ZonedDateTime
condition|)
block|{
comment|// Salesforce expresses time in UTC+offset (ISO-8601 , with this we
comment|// loose time zone information
specifier|final
name|ZonedDateTime
name|dtoValue
init|=
operator|(
name|ZonedDateTime
operator|)
name|dto
operator|.
name|value
decl_stmt|;
specifier|final
name|ZonedDateTime
name|deserializedValue
init|=
operator|(
name|ZonedDateTime
operator|)
name|deserialized
operator|.
name|value
decl_stmt|;
name|assertThat
argument_list|(
name|deserializedValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|dtoValue
operator|.
name|withFixedOffsetZone
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertThat
argument_list|(
name|deserialized
operator|.
name|value
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|dto
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Parameters
DECL|method|cases ()
specifier|public
specifier|static
name|Iterable
argument_list|<
name|Object
index|[]
argument_list|>
name|cases
parameter_list|()
block|{
specifier|final
name|LocalDate
name|localDate
init|=
name|LocalDate
operator|.
name|of
argument_list|(
literal|2007
argument_list|,
literal|03
argument_list|,
literal|19
argument_list|)
decl_stmt|;
specifier|final
name|ZonedDateTime
name|zonedDateTime
init|=
name|ZonedDateTime
operator|.
name|of
argument_list|(
name|localDate
operator|.
name|atTime
argument_list|(
literal|10
argument_list|,
literal|54
argument_list|,
literal|57
argument_list|)
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"Z"
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|Instant
name|instant
init|=
name|zonedDateTime
operator|.
name|toInstant
argument_list|()
decl_stmt|;
specifier|final
name|String
name|zone
init|=
name|DateTimeFormatter
operator|.
name|ofPattern
argument_list|(
literal|"XX"
argument_list|)
operator|.
name|format
argument_list|(
name|zonedDateTime
operator|.
name|withZoneSameLocal
argument_list|(
name|ZoneId
operator|.
name|systemDefault
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
comment|//
name|dto
argument_list|(
name|Date
operator|.
name|from
argument_list|(
name|instant
argument_list|)
argument_list|,
literal|"2007-03-19T10:54:57.000+0000"
argument_list|)
argument_list|,
comment|// 0
name|dto
argument_list|(
name|Date
operator|.
name|from
argument_list|(
name|localDate
operator|.
name|atStartOfDay
argument_list|()
operator|.
name|toInstant
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
argument_list|)
argument_list|,
literal|"2007-03-19T00:00:00.000+0000"
argument_list|)
argument_list|,
comment|// 1
name|dto
argument_list|(
name|localDate
argument_list|,
literal|"2007-03-19"
argument_list|)
argument_list|,
comment|// 2
name|dto
argument_list|(
name|zonedDateTime
operator|.
name|toLocalDateTime
argument_list|()
argument_list|,
literal|"2007-03-19T10:54:57.000"
operator|+
name|zone
argument_list|)
argument_list|,
comment|// 3
name|dto
argument_list|(
name|zonedDateTime
operator|.
name|toOffsetDateTime
argument_list|()
argument_list|,
literal|"2007-03-19T10:54:57.000Z"
argument_list|)
argument_list|,
comment|// 4
name|dto
argument_list|(
name|zonedDateTime
operator|.
name|toOffsetDateTime
argument_list|()
argument_list|,
literal|"2007-03-19T10:54:57.000Z"
argument_list|)
argument_list|,
comment|// 5
name|dto
argument_list|(
name|zonedDateTime
operator|.
name|toOffsetDateTime
argument_list|()
operator|.
name|withOffsetSameInstant
argument_list|(
name|ZoneOffset
operator|.
name|of
argument_list|(
literal|"-7"
argument_list|)
argument_list|)
argument_list|,
literal|"2007-03-19T03:54:57.000-0700"
argument_list|)
argument_list|,
comment|// 6
name|dto
argument_list|(
name|zonedDateTime
argument_list|,
literal|"2007-03-19T10:54:57.000Z"
argument_list|)
argument_list|,
comment|// 7
name|dto
argument_list|(
name|zonedDateTime
operator|.
name|withZoneSameInstant
argument_list|(
name|ZoneId
operator|.
name|of
argument_list|(
literal|"Asia/Kolkata"
argument_list|)
argument_list|)
argument_list|,
literal|"2007-03-19T16:24:57.000+0530"
argument_list|)
argument_list|,
comment|// 8
name|dto
argument_list|(
name|zonedDateTime
operator|.
name|withZoneSameInstant
argument_list|(
name|ZoneId
operator|.
name|of
argument_list|(
literal|"+3"
argument_list|)
argument_list|)
argument_list|,
literal|"2007-03-19T13:54:57.000+0300"
argument_list|)
argument_list|,
comment|// 9
name|dto
argument_list|(
name|instant
argument_list|,
name|instant
operator|.
name|atZone
argument_list|(
name|ZoneId
operator|.
name|systemDefault
argument_list|()
argument_list|)
operator|.
name|format
argument_list|(
name|DateTimeFormatter
operator|.
name|ofPattern
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ss.SSSXX"
argument_list|)
argument_list|)
argument_list|)
argument_list|,
comment|// 10
name|dto
argument_list|(
name|ZonedDateTime
operator|.
name|of
argument_list|(
literal|2018
argument_list|,
literal|03
argument_list|,
literal|22
argument_list|,
literal|9
argument_list|,
literal|58
argument_list|,
literal|8
argument_list|,
literal|5000000
argument_list|,
name|ZoneId
operator|.
name|of
argument_list|(
literal|"Z"
argument_list|)
argument_list|)
argument_list|,
literal|"2018-03-22T09:58:08.005Z"
argument_list|)
argument_list|,
comment|// 11
name|dto
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
literal|"00:00:00.000Z"
argument_list|)
argument_list|,
comment|// 12
name|dto
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
literal|"12:13:14.007Z"
argument_list|)
comment|// 13
argument_list|)
return|;
block|}
DECL|method|dto (final Object value, final String serialized)
specifier|private
specifier|static
name|Object
index|[]
name|dto
parameter_list|(
specifier|final
name|Object
name|value
parameter_list|,
specifier|final
name|String
name|serialized
parameter_list|)
block|{
specifier|final
name|DateTransferObject
argument_list|<
name|?
argument_list|>
name|dto
init|=
operator|new
name|DateTransferObject
argument_list|<>
argument_list|(
name|value
argument_list|)
decl_stmt|;
specifier|final
name|String
name|json
init|=
name|format
argument_list|(
name|JSON_FMT
argument_list|,
name|serialized
argument_list|)
decl_stmt|;
specifier|final
name|String
name|xml
init|=
name|format
argument_list|(
name|XML_FMT
argument_list|,
name|serialized
argument_list|)
decl_stmt|;
return|return
operator|new
name|Object
index|[]
block|{
name|dto
block|,
name|json
block|,
name|xml
block|,
name|value
operator|.
name|getClass
argument_list|()
block|}
return|;
block|}
block|}
end_class

end_unit

