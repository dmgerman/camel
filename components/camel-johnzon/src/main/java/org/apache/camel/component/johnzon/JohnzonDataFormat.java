begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.johnzon
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|johnzon
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|CamelContext
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
name|CamelContextAware
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
name|spi
operator|.
name|DataFormat
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
name|DataFormatName
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
name|annotations
operator|.
name|Dataformat
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|johnzon
operator|.
name|mapper
operator|.
name|Mapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|johnzon
operator|.
name|mapper
operator|.
name|MapperBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|johnzon
operator|.
name|mapper
operator|.
name|reflection
operator|.
name|JohnzonParameterizedType
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> ({@link DataFormat})  * using<a href="http://johnzon.apache.org/">Johnzon</a> to marshal to and from JSON.  */
end_comment

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"json-johnzon"
argument_list|)
DECL|class|JohnzonDataFormat
specifier|public
class|class
name|JohnzonDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
implements|,
name|CamelContextAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|objectMapper
specifier|private
name|Mapper
name|objectMapper
decl_stmt|;
DECL|field|unmarshalType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
decl_stmt|;
DECL|field|parameterizedType
specifier|private
name|JohnzonParameterizedType
name|parameterizedType
decl_stmt|;
DECL|field|attributeOrder
specifier|private
name|Comparator
argument_list|<
name|String
argument_list|>
name|attributeOrder
decl_stmt|;
DECL|field|pretty
specifier|private
name|boolean
name|pretty
decl_stmt|;
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
DECL|field|skipEmptyArray
specifier|private
name|boolean
name|skipEmptyArray
decl_stmt|;
DECL|field|skipNull
specifier|private
name|boolean
name|skipNull
decl_stmt|;
DECL|method|JohnzonDataFormat ()
specifier|public
name|JohnzonDataFormat
parameter_list|()
block|{
name|this
argument_list|(
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use the default Johnzon {@link Mapper} and with a custom      * unmarshal type      *      * @param unmarshalType the custom unmarshal type      */
DECL|method|JohnzonDataFormat (Class<?> unmarshalType)
specifier|public
name|JohnzonDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|unmarshalType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use the default Johnzon {@link Mapper} and with a custom      * unmarshal type      *      * @param unmarshalType the custom unmarshal type      */
DECL|method|JohnzonDataFormat (JohnzonParameterizedType parameterizedType)
specifier|public
name|JohnzonDataFormat
parameter_list|(
name|JohnzonParameterizedType
name|parameterizedType
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|parameterizedType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use a custom Johnzon mapper and unmarshal type      *      * @param mapper        the custom mapper      * @param unmarshalType the custom unmarshal type      */
DECL|method|JohnzonDataFormat (Mapper mapper, Class<?> unmarshalType)
specifier|public
name|JohnzonDataFormat
parameter_list|(
name|Mapper
name|mapper
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|this
operator|.
name|objectMapper
operator|=
name|mapper
expr_stmt|;
name|this
operator|.
name|unmarshalType
operator|=
name|unmarshalType
expr_stmt|;
block|}
comment|/**      * Use a custom Johnzon mapper and unmarshal type      *      * @param mapper        the custom mapper      * @param parameterizedType the JohnzonParameterizedType type      */
DECL|method|JohnzonDataFormat (Mapper mapper, JohnzonParameterizedType parameterizedType)
specifier|public
name|JohnzonDataFormat
parameter_list|(
name|Mapper
name|mapper
parameter_list|,
name|JohnzonParameterizedType
name|parameterizedType
parameter_list|)
block|{
name|this
operator|.
name|objectMapper
operator|=
name|mapper
expr_stmt|;
name|this
operator|.
name|parameterizedType
operator|=
name|parameterizedType
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"json-johnzon"
return|;
block|}
DECL|method|getObjectMapper ()
specifier|public
name|Mapper
name|getObjectMapper
parameter_list|()
block|{
return|return
name|objectMapper
return|;
block|}
DECL|method|setObjectMapper (Mapper objectMapper)
specifier|public
name|void
name|setObjectMapper
parameter_list|(
name|Mapper
name|objectMapper
parameter_list|)
block|{
name|this
operator|.
name|objectMapper
operator|=
name|objectMapper
expr_stmt|;
block|}
DECL|method|getUnmarshalType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getUnmarshalType
parameter_list|()
block|{
return|return
name|unmarshalType
return|;
block|}
DECL|method|setUnmarshalType (Class<?> unmarshalType)
specifier|public
name|void
name|setUnmarshalType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|this
operator|.
name|unmarshalType
operator|=
name|unmarshalType
expr_stmt|;
block|}
DECL|method|getParameterizedType ()
specifier|public
name|JohnzonParameterizedType
name|getParameterizedType
parameter_list|()
block|{
return|return
name|parameterizedType
return|;
block|}
DECL|method|setParameterizedType (JohnzonParameterizedType parameterizedType)
specifier|public
name|void
name|setParameterizedType
parameter_list|(
name|JohnzonParameterizedType
name|parameterizedType
parameter_list|)
block|{
name|this
operator|.
name|parameterizedType
operator|=
name|parameterizedType
expr_stmt|;
block|}
DECL|method|isPretty ()
specifier|public
name|boolean
name|isPretty
parameter_list|()
block|{
return|return
name|pretty
return|;
block|}
DECL|method|setPretty (boolean pretty)
specifier|public
name|void
name|setPretty
parameter_list|(
name|boolean
name|pretty
parameter_list|)
block|{
name|this
operator|.
name|pretty
operator|=
name|pretty
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
DECL|method|isSkipEmptyArray ()
specifier|public
name|boolean
name|isSkipEmptyArray
parameter_list|()
block|{
return|return
name|skipEmptyArray
return|;
block|}
DECL|method|setSkipEmptyArray (boolean skipEmptyArray)
specifier|public
name|void
name|setSkipEmptyArray
parameter_list|(
name|boolean
name|skipEmptyArray
parameter_list|)
block|{
name|this
operator|.
name|skipEmptyArray
operator|=
name|skipEmptyArray
expr_stmt|;
block|}
DECL|method|isSkipNull ()
specifier|public
name|boolean
name|isSkipNull
parameter_list|()
block|{
return|return
name|skipNull
return|;
block|}
DECL|method|setSkipNull (boolean skipNull)
specifier|public
name|void
name|setSkipNull
parameter_list|(
name|boolean
name|skipNull
parameter_list|)
block|{
name|this
operator|.
name|skipNull
operator|=
name|skipNull
expr_stmt|;
block|}
DECL|method|getAttributeOrder ()
specifier|public
name|Comparator
argument_list|<
name|String
argument_list|>
name|getAttributeOrder
parameter_list|()
block|{
return|return
name|attributeOrder
return|;
block|}
DECL|method|setAttributeOrder (Comparator<String> attributeOrder)
specifier|public
name|void
name|setAttributeOrder
parameter_list|(
name|Comparator
argument_list|<
name|String
argument_list|>
name|attributeOrder
parameter_list|)
block|{
name|this
operator|.
name|attributeOrder
operator|=
name|attributeOrder
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|objectMapper
operator|.
name|writeObject
argument_list|(
name|graph
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
comment|// is there a header with the unmarshal type?
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|unmarshalType
decl_stmt|;
name|String
name|type
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JohnzonConstants
operator|.
name|UNMARSHAL_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|clazz
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameterizedType
operator|!=
literal|null
condition|)
block|{
return|return
name|this
operator|.
name|objectMapper
operator|.
name|readCollection
argument_list|(
name|stream
argument_list|,
name|parameterizedType
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|this
operator|.
name|objectMapper
operator|.
name|readObject
argument_list|(
name|stream
argument_list|,
name|clazz
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|objectMapper
operator|==
literal|null
condition|)
block|{
name|MapperBuilder
name|builder
init|=
operator|new
name|MapperBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setPretty
argument_list|(
name|pretty
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setSkipNull
argument_list|(
name|skipNull
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setSkipEmptyArray
argument_list|(
name|skipEmptyArray
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
name|builder
operator|.
name|setEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|attributeOrder
argument_list|)
condition|)
block|{
name|builder
operator|.
name|setAttributeOrder
argument_list|(
name|attributeOrder
argument_list|)
expr_stmt|;
block|}
name|objectMapper
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

