begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|File
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
name|ObjectInput
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutput
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
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|StreamCache
import|;
end_import

begin_comment
comment|/**  * Optimised {@link IOConverter}  */
end_comment

begin_class
DECL|class|IOConverterOptimised
specifier|public
specifier|final
class|class
name|IOConverterOptimised
block|{
DECL|method|IOConverterOptimised ()
specifier|private
name|IOConverterOptimised
parameter_list|()
block|{     }
comment|// CHECKSTYLE:OFF
DECL|method|convertTo (final Class<?> type, final Exchange exchange, final Object value)
specifier|public
specifier|static
name|Object
name|convertTo
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
name|fromType
init|=
name|value
operator|.
name|getClass
argument_list|()
decl_stmt|;
comment|// if the value is StreamCache then ensure its readable before doing conversions
comment|// by resetting it (this is also what StreamCachingAdvice does)
if|if
condition|(
name|value
operator|instanceof
name|StreamCache
condition|)
block|{
operator|(
operator|(
name|StreamCache
operator|)
name|value
operator|)
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|==
name|InputStream
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|fromType
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toInputStream
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|URL
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toInputStream
argument_list|(
operator|(
name|URL
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|File
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toInputStream
argument_list|(
operator|(
name|File
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|byte
index|[]
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toInputStream
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|ByteArrayOutputStream
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toInputStream
argument_list|(
operator|(
name|ByteArrayOutputStream
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|BufferedReader
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toInputStream
argument_list|(
operator|(
name|BufferedReader
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|StringBuilder
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toInputStream
argument_list|(
operator|(
name|StringBuilder
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|type
operator|==
name|Reader
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|fromType
operator|==
name|File
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toReader
argument_list|(
operator|(
name|File
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toReader
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|InputStream
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|fromType
argument_list|)
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toReader
argument_list|(
operator|(
name|InputStream
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|type
operator|==
name|File
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|fromType
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toFile
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|type
operator|==
name|OutputStream
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|fromType
operator|==
name|File
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toOutputStream
argument_list|(
operator|(
name|File
operator|)
name|value
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|type
operator|==
name|Writer
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|fromType
operator|==
name|File
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toWriter
argument_list|(
operator|(
name|File
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|OutputStream
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|fromType
argument_list|)
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toWriter
argument_list|(
operator|(
name|OutputStream
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|type
operator|==
name|String
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|fromType
operator|==
name|byte
index|[]
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toString
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|File
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toString
argument_list|(
operator|(
name|File
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|URL
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toString
argument_list|(
operator|(
name|URL
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|BufferedReader
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toString
argument_list|(
operator|(
name|BufferedReader
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Reader
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|fromType
argument_list|)
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toString
argument_list|(
operator|(
name|Reader
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|InputStream
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|fromType
argument_list|)
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toString
argument_list|(
operator|(
name|InputStream
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|ByteArrayOutputStream
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toString
argument_list|(
operator|(
name|ByteArrayOutputStream
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|type
operator|==
name|byte
index|[]
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|fromType
operator|==
name|BufferedReader
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toByteArray
argument_list|(
operator|(
name|BufferedReader
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|Reader
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|fromType
argument_list|)
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toByteArray
argument_list|(
operator|(
name|Reader
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|File
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toByteArray
argument_list|(
operator|(
name|File
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toByteArray
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|ByteArrayOutputStream
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toByteArray
argument_list|(
operator|(
name|ByteArrayOutputStream
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|InputStream
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|fromType
argument_list|)
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toBytes
argument_list|(
operator|(
name|InputStream
operator|)
name|value
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|type
operator|==
name|ObjectInput
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|fromType
operator|==
name|InputStream
operator|.
name|class
operator|||
name|fromType
operator|==
name|BufferedInputStream
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toObjectInput
argument_list|(
operator|(
name|InputStream
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|type
operator|==
name|ObjectOutput
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|fromType
operator|==
name|OutputStream
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toObjectOutput
argument_list|(
operator|(
name|OutputStream
operator|)
name|value
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|type
operator|==
name|Properties
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|fromType
operator|==
name|File
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toProperties
argument_list|(
operator|(
name|File
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|InputStream
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toProperties
argument_list|(
operator|(
name|InputStream
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|fromType
operator|==
name|Reader
operator|.
name|class
condition|)
block|{
return|return
name|IOConverter
operator|.
name|toProperties
argument_list|(
operator|(
name|Reader
operator|)
name|value
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// no optimised type converter found
return|return
literal|null
return|;
block|}
comment|// CHECKSTYLE:ON
block|}
end_class

end_unit

