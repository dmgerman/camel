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
name|OffsetTime
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
name|JsonParser
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
name|JsonToken
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
name|DeserializationContext
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
name|JsonDeserializer
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
name|JsonMappingException
import|;
end_import

begin_class
DECL|class|TimeDeserializer
specifier|public
class|class
name|TimeDeserializer
extends|extends
name|JsonDeserializer
argument_list|<
name|OffsetTime
argument_list|>
block|{
DECL|method|TimeDeserializer ()
specifier|public
name|TimeDeserializer
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|deserialize (JsonParser jsonParser, DeserializationContext deserializationContext)
specifier|public
name|OffsetTime
name|deserialize
parameter_list|(
name|JsonParser
name|jsonParser
parameter_list|,
name|DeserializationContext
name|deserializationContext
parameter_list|)
throws|throws
name|IOException
block|{
name|JsonToken
name|currentToken
init|=
name|jsonParser
operator|.
name|getCurrentToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentToken
operator|==
name|JsonToken
operator|.
name|VALUE_STRING
condition|)
block|{
return|return
name|DateTimeUtils
operator|.
name|parseTime
argument_list|(
name|jsonParser
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
throw|throw
name|JsonMappingException
operator|.
name|from
argument_list|(
name|deserializationContext
argument_list|,
literal|"Expected String value, got: "
operator|+
name|currentToken
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|handledType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|handledType
parameter_list|()
block|{
return|return
name|OffsetTime
operator|.
name|class
return|;
block|}
block|}
end_class

end_unit

