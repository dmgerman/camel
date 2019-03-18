begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|OffsetDateTime
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonGenerator
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
name|JsonSerializer
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
name|SerializerProvider
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
name|ser
operator|.
name|std
operator|.
name|StdSerializer
import|;
end_import

begin_class
DECL|class|OffsetDateTimeSerializer
specifier|final
class|class
name|OffsetDateTimeSerializer
extends|extends
name|StdSerializer
argument_list|<
name|OffsetDateTime
argument_list|>
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|JsonSerializer
argument_list|<
name|OffsetDateTime
argument_list|>
name|INSTANCE
init|=
operator|new
name|OffsetDateTimeSerializer
argument_list|()
decl_stmt|;
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|OffsetDateTimeSerializer ()
specifier|private
name|OffsetDateTimeSerializer
parameter_list|()
block|{
name|super
argument_list|(
name|OffsetDateTime
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|serialize (final OffsetDateTime value, final JsonGenerator gen, final SerializerProvider serializers)
specifier|public
name|void
name|serialize
parameter_list|(
specifier|final
name|OffsetDateTime
name|value
parameter_list|,
specifier|final
name|JsonGenerator
name|gen
parameter_list|,
specifier|final
name|SerializerProvider
name|serializers
parameter_list|)
throws|throws
name|IOException
throws|,
name|JsonProcessingException
block|{
specifier|final
name|ZonedDateTime
name|zonedDateTime
init|=
name|value
operator|.
name|toZonedDateTime
argument_list|()
decl_stmt|;
name|serializers
operator|.
name|defaultSerializeValue
argument_list|(
name|zonedDateTime
argument_list|,
name|gen
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

