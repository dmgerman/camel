begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jacksonxml.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jacksonxml
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|dataformat
operator|.
name|xml
operator|.
name|XmlMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jacksonxml
operator|.
name|JacksonXMLConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|TypeConverterRegistry
import|;
end_import

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|JacksonXMLTypeConverters
specifier|public
specifier|final
class|class
name|JacksonXMLTypeConverters
block|{
DECL|field|defaultMapper
specifier|private
specifier|final
name|XmlMapper
name|defaultMapper
init|=
operator|new
name|XmlMapper
argument_list|()
decl_stmt|;
DECL|field|init
specifier|private
name|boolean
name|init
decl_stmt|;
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
DECL|method|JacksonXMLTypeConverters ()
specifier|public
name|JacksonXMLTypeConverters
parameter_list|()
block|{     }
annotation|@
name|Converter
argument_list|(
name|fallback
operator|=
literal|true
argument_list|)
DECL|method|convertTo (Class<T> type, Exchange exchange, Object value, TypeConverterRegistry registry)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|,
name|TypeConverterRegistry
name|registry
parameter_list|)
block|{
comment|// only do this if enabled
if|if
condition|(
operator|!
name|init
operator|&&
name|exchange
operator|!=
literal|null
condition|)
block|{
comment|// init to see if this is enabled
name|String
name|text
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|get
argument_list|(
name|JacksonXMLConstants
operator|.
name|ENABLE_XML_TYPE_CONVERTER
argument_list|)
decl_stmt|;
name|enabled
operator|=
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|init
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|enabled
operator|==
literal|null
operator|||
operator|!
name|enabled
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|isNotPojoType
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|exchange
operator|!=
literal|null
operator|&&
name|value
operator|instanceof
name|Map
condition|)
block|{
name|ObjectMapper
name|mapper
init|=
name|resolveObjectMapper
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapper
operator|.
name|canSerialize
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|mapper
operator|.
name|convertValue
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
return|;
block|}
block|}
comment|// Just return null to let other fallback converter to do the job
return|return
literal|null
return|;
block|}
DECL|method|isNotPojoType (Class<?> type)
specifier|private
specifier|static
name|boolean
name|isNotPojoType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|boolean
name|isString
init|=
name|String
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|boolean
name|isNumber
init|=
name|Number
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|int
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|long
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|short
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|char
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|float
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|double
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
name|isString
operator|||
name|isNumber
return|;
block|}
DECL|method|resolveObjectMapper (Registry registry)
specifier|private
name|ObjectMapper
name|resolveObjectMapper
parameter_list|(
name|Registry
name|registry
parameter_list|)
block|{
name|Set
argument_list|<
name|XmlMapper
argument_list|>
name|mappers
init|=
name|registry
operator|.
name|findByType
argument_list|(
name|XmlMapper
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|mappers
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|mappers
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
return|return
name|defaultMapper
return|;
block|}
block|}
end_class

end_unit

