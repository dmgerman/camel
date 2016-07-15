begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.transformer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|transformer
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
name|Message
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
name|converter
operator|.
name|stream
operator|.
name|OutputStreamBuilder
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
name|model
operator|.
name|DataFormatDefinition
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
name|model
operator|.
name|transformer
operator|.
name|DataFormatTransformerDefinition
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
name|DataType
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
name|Transformer
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
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/transformer.html">Transformer</a>  * leverages DataFormat to perform transformation.  */
end_comment

begin_class
DECL|class|DataFormatTransformer
specifier|public
class|class
name|DataFormatTransformer
extends|extends
name|Transformer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DataFormatTransformer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dataFormatRef
specifier|private
name|String
name|dataFormatRef
decl_stmt|;
DECL|field|dataFormatType
specifier|private
name|DataFormatDefinition
name|dataFormatType
decl_stmt|;
DECL|field|dataFormat
specifier|private
name|DataFormat
name|dataFormat
decl_stmt|;
DECL|field|transformerString
specifier|private
name|String
name|transformerString
decl_stmt|;
DECL|method|DataFormatTransformer (CamelContext context)
specifier|public
name|DataFormatTransformer
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Perform data transformation with specified from/to type using DataFormat.      * @param message message to apply transformation      * @param from 'from' data type      * @param to 'to' data type      */
annotation|@
name|Override
DECL|method|transform (Message message, DataType from, DataType to)
specifier|public
name|void
name|transform
parameter_list|(
name|Message
name|message
parameter_list|,
name|DataType
name|from
parameter_list|,
name|DataType
name|to
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|CamelContext
name|context
init|=
name|exchange
operator|.
name|getContext
argument_list|()
decl_stmt|;
comment|// Unmarshaling into Java Object
if|if
condition|(
operator|(
name|to
operator|==
literal|null
operator|||
name|to
operator|.
name|isJavaType
argument_list|()
operator|)
operator|&&
operator|(
name|from
operator|.
name|equals
argument_list|(
name|getFrom
argument_list|()
argument_list|)
operator|||
name|from
operator|.
name|getModel
argument_list|()
operator|.
name|equals
argument_list|(
name|getModel
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|DataFormat
name|dataFormat
init|=
name|getDataFormat
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Unmarshaling with '{}'"
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
name|Object
name|answer
init|=
name|dataFormat
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
name|message
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|to
operator|!=
literal|null
operator|&&
name|to
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|toClass
init|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|to
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|toClass
operator|.
name|isAssignableFrom
argument_list|(
name|answer
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Converting to '{}'"
argument_list|,
name|toClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|toClass
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
name|message
operator|.
name|setBody
argument_list|(
name|answer
argument_list|)
expr_stmt|;
comment|// Marshaling from Java Object
block|}
elseif|else
if|if
condition|(
operator|(
name|from
operator|==
literal|null
operator|||
name|from
operator|.
name|isJavaType
argument_list|()
operator|)
operator|&&
operator|(
name|to
operator|.
name|equals
argument_list|(
name|getTo
argument_list|()
argument_list|)
operator|||
name|to
operator|.
name|getModel
argument_list|()
operator|.
name|equals
argument_list|(
name|getModel
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|Object
name|input
init|=
name|message
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|from
operator|!=
literal|null
operator|&&
name|from
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|fromClass
init|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|from
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|fromClass
operator|.
name|isAssignableFrom
argument_list|(
name|input
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Converting to '{}'"
argument_list|,
name|fromClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|input
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|fromClass
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
block|}
name|OutputStreamBuilder
name|osb
init|=
name|OutputStreamBuilder
operator|.
name|withExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|DataFormat
name|dataFormat
init|=
name|getDataFormat
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Marshaling with '{}'"
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
name|dataFormat
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|,
name|message
operator|.
name|getBody
argument_list|()
argument_list|,
name|osb
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|osb
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported transformation: from='"
operator|+
name|from
operator|+
literal|", to='"
operator|+
name|to
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
comment|/**      * A bit dirty hack to create DataFormat instance, as it requires a RouteContext anyway.      */
DECL|method|getDataFormat (Exchange exchange)
specifier|private
name|DataFormat
name|getDataFormat
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|dataFormat
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|dataFormat
operator|=
name|DataFormatDefinition
operator|.
name|getDataFormat
argument_list|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getRouteContext
argument_list|()
argument_list|,
name|this
operator|.
name|dataFormatType
argument_list|,
name|this
operator|.
name|dataFormatRef
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|dataFormat
operator|!=
literal|null
operator|&&
operator|!
name|getCamelContext
argument_list|()
operator|.
name|hasService
argument_list|(
name|this
operator|.
name|dataFormat
argument_list|)
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|addService
argument_list|(
name|this
operator|.
name|dataFormat
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|this
operator|.
name|dataFormat
return|;
block|}
comment|/**      * Set DataFormat ref.      * @param ref DataFormat ref      * @return this DataFormatTransformer instance      */
DECL|method|setDataFormatRef (String ref)
specifier|public
name|DataFormatTransformer
name|setDataFormatRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|dataFormatRef
operator|=
name|ref
expr_stmt|;
name|this
operator|.
name|transformerString
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set DataFormatDefinition.      * @param dataFormatType DataFormatDefinition      * @return this DataFormatTransformer instance      */
DECL|method|setDataFormatType (DataFormatDefinition dataFormatType)
specifier|public
name|DataFormatTransformer
name|setDataFormatType
parameter_list|(
name|DataFormatDefinition
name|dataFormatType
parameter_list|)
block|{
name|this
operator|.
name|dataFormatType
operator|=
name|dataFormatType
expr_stmt|;
name|this
operator|.
name|transformerString
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|transformerString
operator|==
literal|null
condition|)
block|{
name|transformerString
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"DataFormatTransformer[scheme='%s', from='%s', to='%s', ref='%s', type='%s']"
argument_list|,
name|getModel
argument_list|()
argument_list|,
name|getFrom
argument_list|()
argument_list|,
name|getTo
argument_list|()
argument_list|,
name|dataFormatRef
argument_list|,
name|dataFormatType
argument_list|)
expr_stmt|;
block|}
return|return
name|transformerString
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// no-op
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|this
operator|.
name|dataFormat
argument_list|)
expr_stmt|;
name|getCamelContext
argument_list|()
operator|.
name|removeService
argument_list|(
name|this
operator|.
name|dataFormat
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

