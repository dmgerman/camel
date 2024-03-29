begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|model
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

begin_comment
comment|/**  * A serializer for {@link Instant} compatible with {@link UnixTimestampDeserializer}.  */
end_comment

begin_class
DECL|class|UnixTimestampSerializer
specifier|public
class|class
name|UnixTimestampSerializer
extends|extends
name|JsonSerializer
argument_list|<
name|Instant
argument_list|>
block|{
annotation|@
name|Override
DECL|method|serialize (Instant value, JsonGenerator gen, SerializerProvider serializers)
specifier|public
name|void
name|serialize
parameter_list|(
name|Instant
name|value
parameter_list|,
name|JsonGenerator
name|gen
parameter_list|,
name|SerializerProvider
name|serializers
parameter_list|)
throws|throws
name|IOException
block|{
name|gen
operator|.
name|writeNumber
argument_list|(
name|value
operator|.
name|getEpochSecond
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

