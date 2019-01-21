begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.asn1
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|asn1
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|IOException
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|IOHelper
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
name|bouncycastle
operator|.
name|asn1
operator|.
name|ASN1InputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|ASN1Primitive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openmuc
operator|.
name|jasn1
operator|.
name|ber
operator|.
name|ReverseByteArrayOutputStream
import|;
end_import

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"asn1"
argument_list|)
DECL|class|ASN1DataFormat
specifier|public
class|class
name|ASN1DataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
block|{
DECL|field|usingIterator
specifier|private
name|boolean
name|usingIterator
decl_stmt|;
DECL|field|clazzName
specifier|private
name|String
name|clazzName
decl_stmt|;
DECL|method|ASN1DataFormat ()
specifier|public
name|ASN1DataFormat
parameter_list|()
block|{
name|this
operator|.
name|usingIterator
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|ASN1DataFormat (String clazzName)
specifier|public
name|ASN1DataFormat
parameter_list|(
name|String
name|clazzName
parameter_list|)
block|{
name|this
operator|.
name|usingIterator
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|clazzName
operator|=
name|clazzName
expr_stmt|;
block|}
DECL|method|ASN1DataFormat (Class<?> clazz)
specifier|public
name|ASN1DataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|this
operator|.
name|usingIterator
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|clazzName
operator|=
name|clazz
operator|.
name|getName
argument_list|()
expr_stmt|;
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
literal|"asn1"
return|;
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
name|InputStream
name|berOut
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|usingIterator
condition|)
block|{
if|if
condition|(
name|clazzName
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
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
name|clazzName
argument_list|)
decl_stmt|;
name|encodeGenericTypeObject
argument_list|(
name|exchange
argument_list|,
name|clazz
argument_list|,
name|stream
argument_list|)
expr_stmt|;
return|return;
block|}
name|Object
name|record
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|record
operator|instanceof
name|ASN1Primitive
condition|)
block|{
name|ASN1Primitive
name|asn1Primitive
init|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|ASN1Primitive
operator|.
name|class
argument_list|,
name|record
argument_list|)
decl_stmt|;
name|berOut
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|asn1Primitive
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|record
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|berOut
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|record
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|byte
index|[]
name|byteInput
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|exchange
argument_list|,
name|graph
argument_list|)
decl_stmt|;
name|berOut
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|byteInput
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|IOHelper
operator|.
name|copy
argument_list|(
name|berOut
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|berOut
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|encodeGenericTypeObject (Exchange exchange, Class<?> clazz, OutputStream stream)
specifier|private
name|void
name|encodeGenericTypeObject
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|NoSuchMethodException
throws|,
name|SecurityException
throws|,
name|IllegalAccessException
throws|,
name|IllegalArgumentException
throws|,
name|InvocationTargetException
throws|,
name|IOException
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|paramOut
init|=
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
literal|1
index|]
decl_stmt|;
name|paramOut
index|[
literal|0
index|]
operator|=
name|OutputStream
operator|.
name|class
expr_stmt|;
name|ReverseByteArrayOutputStream
name|berOut
init|=
operator|new
name|ReverseByteArrayOutputStream
argument_list|(
name|IOHelper
operator|.
name|DEFAULT_BUFFER_SIZE
operator|/
literal|256
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Method
name|encodeMethod
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredMethod
argument_list|(
literal|"encode"
argument_list|,
name|paramOut
argument_list|)
decl_stmt|;
name|encodeMethod
operator|.
name|invoke
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|berOut
argument_list|)
expr_stmt|;
name|stream
operator|.
name|write
argument_list|(
name|berOut
operator|.
name|getArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
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
if|if
condition|(
name|usingIterator
condition|)
block|{
if|if
condition|(
name|clazzName
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
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
name|clazzName
argument_list|)
decl_stmt|;
name|ASN1GenericIterator
name|asn1GenericIterator
init|=
operator|new
name|ASN1GenericIterator
argument_list|(
name|clazz
argument_list|,
name|stream
argument_list|)
decl_stmt|;
return|return
name|asn1GenericIterator
return|;
block|}
name|ASN1MessageIterator
name|asn1MessageIterator
init|=
operator|new
name|ASN1MessageIterator
argument_list|(
name|exchange
argument_list|,
name|stream
argument_list|)
decl_stmt|;
return|return
name|asn1MessageIterator
return|;
block|}
else|else
block|{
name|ASN1Primitive
name|asn1Record
init|=
literal|null
decl_stmt|;
name|byte
index|[]
name|asn1Bytes
decl_stmt|;
try|try
init|(
name|ASN1InputStream
name|ais
init|=
operator|new
name|ASN1InputStream
argument_list|(
name|stream
argument_list|)
init|; ByteArrayOutputStream asn1Out = new ByteArrayOutputStream()
empty_stmt|;
block|)
block|{
while|while
condition|(
name|ais
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
name|asn1Record
operator|=
name|ais
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|asn1Out
operator|.
name|write
argument_list|(
name|asn1Record
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|asn1Bytes
operator|=
name|asn1Out
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
return|return
name|asn1Bytes
return|;
block|}
block|}
end_class

begin_function
DECL|method|isUsingIterator ()
specifier|public
name|boolean
name|isUsingIterator
parameter_list|()
block|{
return|return
name|usingIterator
return|;
block|}
end_function

begin_function
DECL|method|setUsingIterator (boolean usingIterator)
specifier|public
name|void
name|setUsingIterator
parameter_list|(
name|boolean
name|usingIterator
parameter_list|)
block|{
name|this
operator|.
name|usingIterator
operator|=
name|usingIterator
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getClazzName ()
specifier|public
name|String
name|getClazzName
parameter_list|()
block|{
return|return
name|clazzName
return|;
block|}
end_function

begin_function
DECL|method|setClazzName (String clazzName)
specifier|public
name|void
name|setClazzName
parameter_list|(
name|String
name|clazzName
parameter_list|)
block|{
name|this
operator|.
name|clazzName
operator|=
name|clazzName
expr_stmt|;
block|}
end_function

begin_function
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
comment|// no op
block|}
end_function

begin_function
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
comment|// no op
block|}
end_function

unit|}
end_unit

