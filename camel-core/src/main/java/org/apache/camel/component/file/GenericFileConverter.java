begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

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
name|FileInputStream
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
name|Serializable
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
name|spi
operator|.
name|TypeConverterRegistry
import|;
end_import

begin_comment
comment|/**  * A set of converter methods for working with generic file types  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|GenericFileConverter
specifier|public
specifier|final
class|class
name|GenericFileConverter
block|{
DECL|method|GenericFileConverter ()
specifier|private
name|GenericFileConverter
parameter_list|()
block|{
comment|// Helper Class
block|}
annotation|@
name|FallbackConverter
DECL|method|convertTo (Class<?> type, Exchange exchange, Object value, TypeConverterRegistry registry)
specifier|public
specifier|static
name|Object
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|?
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
comment|// use a fallback type converter so we can convert the embedded body if the value is GenericFile
if|if
condition|(
name|GenericFile
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
name|GenericFile
argument_list|<
name|?
argument_list|>
name|file
init|=
operator|(
name|GenericFile
argument_list|<
name|?
argument_list|>
operator|)
name|value
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|from
init|=
name|file
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
decl_stmt|;
comment|// maybe from is already the type we want
if|if
condition|(
name|from
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|file
operator|.
name|getBody
argument_list|()
return|;
block|}
comment|// no then try to lookup a type converter
name|TypeConverter
name|tc
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|type
argument_list|,
name|from
argument_list|)
decl_stmt|;
if|if
condition|(
name|tc
operator|!=
literal|null
condition|)
block|{
name|Object
name|body
init|=
name|file
operator|.
name|getBody
argument_list|()
decl_stmt|;
return|return
name|tc
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|genericFileToInputStream (GenericFile<?> file, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|genericFileToInputStream
parameter_list|(
name|GenericFile
argument_list|<
name|?
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
comment|// use a file input stream if its a java.io.File
if|if
condition|(
name|file
operator|.
name|getFile
argument_list|()
operator|instanceof
name|java
operator|.
name|io
operator|.
name|File
condition|)
block|{
return|return
operator|new
name|FileInputStream
argument_list|(
operator|(
name|File
operator|)
name|file
operator|.
name|getFile
argument_list|()
argument_list|)
return|;
block|}
comment|// otherwise ensure the body is loaded as we want the input stream of the body
name|file
operator|.
name|getBinding
argument_list|()
operator|.
name|loadContent
argument_list|(
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|file
operator|.
name|getBody
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
comment|// should revert to fallback converter if we don't have an exchange
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|genericFileToString (GenericFile<?> file, Exchange exchange)
specifier|public
specifier|static
name|String
name|genericFileToString
parameter_list|(
name|GenericFile
argument_list|<
name|?
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
comment|// ensure the body is loaded as we do not want a toString of java.io.File handle returned, but the file content
name|file
operator|.
name|getBinding
argument_list|()
operator|.
name|loadContent
argument_list|(
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|file
operator|.
name|getBody
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
comment|// should revert to fallback converter if we don't have an exchange
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|genericFileToSerializable (GenericFile<?> file, Exchange exchange)
specifier|public
specifier|static
name|Serializable
name|genericFileToSerializable
parameter_list|(
name|GenericFile
argument_list|<
name|?
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
comment|// ensure the body is loaded as we do not want a java.io.File handle returned, but the file content
name|file
operator|.
name|getBinding
argument_list|()
operator|.
name|loadContent
argument_list|(
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Serializable
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|file
operator|.
name|getBody
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
comment|// should revert to fallback converter if we don't have an exchange
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

