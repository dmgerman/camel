begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
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
name|FallbackConverter
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
name|TypeConverter
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
name|cxf
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
name|TypeConverterRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageContentsList
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
comment|/**  * The<a href="http://camel.apache.org/type-converter.html">Type Converters</a>  * for CXF related types' converting .  *  * @version   */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|CxfConverter
specifier|public
specifier|final
class|class
name|CxfConverter
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
name|CxfConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|CxfConverter ()
specifier|private
name|CxfConverter
parameter_list|()
block|{
comment|// Helper class
block|}
annotation|@
name|Converter
DECL|method|toMessageContentsList (final Object[] array)
specifier|public
specifier|static
name|MessageContentsList
name|toMessageContentsList
parameter_list|(
specifier|final
name|Object
index|[]
name|array
parameter_list|)
block|{
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|MessageContentsList
argument_list|(
name|array
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|MessageContentsList
argument_list|()
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|toQName (String qname)
specifier|public
specifier|static
name|QName
name|toQName
parameter_list|(
name|String
name|qname
parameter_list|)
block|{
return|return
name|QName
operator|.
name|valueOf
argument_list|(
name|qname
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toArray (Object object)
specifier|public
specifier|static
name|Object
index|[]
name|toArray
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Collection
condition|)
block|{
return|return
operator|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|object
operator|)
operator|.
name|toArray
argument_list|()
return|;
block|}
else|else
block|{
name|Object
name|answer
index|[]
decl_stmt|;
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|Object
index|[
literal|0
index|]
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|Object
index|[
literal|1
index|]
expr_stmt|;
name|answer
index|[
literal|0
index|]
operator|=
name|object
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|soapMessageToString (final SOAPMessage soapMessage)
specifier|public
specifier|static
name|String
name|soapMessageToString
parameter_list|(
specifier|final
name|SOAPMessage
name|soapMessage
parameter_list|)
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|soapMessage
operator|.
name|writeTo
argument_list|(
name|baos
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Get the exception when converting the SOAPMessage into String, the exception is "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|baos
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toDataFormat (final String name)
specifier|public
specifier|static
name|DataFormat
name|toDataFormat
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|DataFormat
operator|.
name|valueOf
argument_list|(
name|name
operator|.
name|toUpperCase
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (Response response, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|Response
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|obj
init|=
name|response
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|InputStream
condition|)
block|{
comment|// short circuit the lookup
return|return
operator|(
name|InputStream
operator|)
name|obj
return|;
block|}
name|TypeConverterRegistry
name|registry
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverterRegistry
argument_list|()
decl_stmt|;
name|TypeConverter
name|tc
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|obj
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
return|return
name|tc
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|obj
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Use a fallback type converter so we can convert the embedded list element       * if the value is MessageContentsList.  The algorithm of this converter      * finds the first non-null list element from the list and applies conversion      * to the list element.      *       * @param type the desired type to be converted to      * @param exchange optional exchange which can be null      * @param value the object to be converted      * @param registry type converter registry      * @return the converted value of the desired type or null if no suitable converter found      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|FallbackConverter
DECL|method|convertTo (Class<T> type, Exchange exchange, Object value, TypeConverterRegistry registry)
specifier|public
specifier|static
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
comment|// CXF-WS MessageContentsList class
if|if
condition|(
name|MessageContentsList
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|MessageContentsList
name|list
init|=
operator|(
name|MessageContentsList
operator|)
name|value
decl_stmt|;
for|for
control|(
name|Object
name|embedded
range|:
name|list
control|)
block|{
if|if
condition|(
name|embedded
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|embedded
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|embedded
argument_list|)
return|;
block|}
else|else
block|{
name|TypeConverter
name|tc
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|type
argument_list|,
name|embedded
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
return|return
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|embedded
argument_list|)
return|;
block|}
block|}
block|}
block|}
comment|// return void to indicate its not possible to convert at this time
return|return
operator|(
name|T
operator|)
name|Void
operator|.
name|TYPE
return|;
block|}
comment|// CXF-RS Response class
if|if
condition|(
name|Response
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|Response
name|response
init|=
operator|(
name|Response
operator|)
name|value
decl_stmt|;
name|Object
name|entity
init|=
name|response
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|TypeConverter
name|tc
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|type
argument_list|,
name|entity
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
return|return
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|entity
argument_list|)
return|;
block|}
comment|// return void to indicate its not possible to convert at this time
return|return
operator|(
name|T
operator|)
name|Void
operator|.
name|TYPE
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

