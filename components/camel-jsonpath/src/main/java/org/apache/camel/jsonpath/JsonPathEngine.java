begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jsonpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jsonpath
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
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|Configuration
operator|.
name|Defaults
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|JsonPath
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|internal
operator|.
name|DefaultsImpl
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
name|InvalidPayloadException
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
name|file
operator|.
name|GenericFile
import|;
end_import

begin_class
DECL|class|JsonPathEngine
specifier|public
class|class
name|JsonPathEngine
block|{
DECL|field|path
specifier|private
specifier|final
name|JsonPath
name|path
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|Configuration
name|configuration
decl_stmt|;
DECL|method|JsonPathEngine (String expression)
specifier|public
name|JsonPathEngine
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|this
argument_list|(
name|expression
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|JsonPathEngine (String expression, boolean suppressExceptions, Option[] options)
specifier|public
name|JsonPathEngine
parameter_list|(
name|String
name|expression
parameter_list|,
name|boolean
name|suppressExceptions
parameter_list|,
name|Option
index|[]
name|options
parameter_list|)
block|{
name|Defaults
name|defaults
init|=
name|DefaultsImpl
operator|.
name|INSTANCE
decl_stmt|;
if|if
condition|(
name|options
operator|!=
literal|null
condition|)
block|{
name|Configuration
operator|.
name|ConfigurationBuilder
name|builder
init|=
name|Configuration
operator|.
name|builder
argument_list|()
operator|.
name|jsonProvider
argument_list|(
name|defaults
operator|.
name|jsonProvider
argument_list|()
argument_list|)
operator|.
name|options
argument_list|(
name|options
argument_list|)
decl_stmt|;
if|if
condition|(
name|suppressExceptions
condition|)
block|{
name|builder
operator|.
name|options
argument_list|(
name|Option
operator|.
name|SUPPRESS_EXCEPTIONS
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|configuration
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Configuration
operator|.
name|ConfigurationBuilder
name|builder
init|=
name|Configuration
operator|.
name|builder
argument_list|()
operator|.
name|jsonProvider
argument_list|(
name|defaults
operator|.
name|jsonProvider
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|suppressExceptions
condition|)
block|{
name|builder
operator|.
name|options
argument_list|(
name|Option
operator|.
name|SUPPRESS_EXCEPTIONS
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|configuration
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|path
operator|=
name|JsonPath
operator|.
name|compile
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|read (Exchange exchange)
specifier|public
name|Object
name|read
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidPayloadException
block|{
name|Object
name|json
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
name|json
operator|instanceof
name|GenericFile
condition|)
block|{
name|GenericFile
argument_list|<
name|?
argument_list|>
name|genericFile
init|=
operator|(
name|GenericFile
argument_list|<
name|?
argument_list|>
operator|)
name|json
decl_stmt|;
if|if
condition|(
name|genericFile
operator|.
name|getCharset
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// special treatment for generic file with charset
name|InputStream
name|inputStream
init|=
operator|new
name|FileInputStream
argument_list|(
operator|(
name|File
operator|)
name|genericFile
operator|.
name|getFile
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|path
operator|.
name|read
argument_list|(
name|inputStream
argument_list|,
name|genericFile
operator|.
name|getCharset
argument_list|()
argument_list|,
name|configuration
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|json
operator|instanceof
name|String
condition|)
block|{
name|String
name|str
init|=
operator|(
name|String
operator|)
name|json
decl_stmt|;
return|return
name|path
operator|.
name|read
argument_list|(
name|str
argument_list|,
name|configuration
argument_list|)
return|;
block|}
else|else
block|{
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|jsonEncoding
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JsonPathConstants
operator|.
name|HEADER_JSON_ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|jsonEncoding
operator|!=
literal|null
condition|)
block|{
comment|// json encoding specified in header
return|return
name|path
operator|.
name|read
argument_list|(
name|is
argument_list|,
name|jsonEncoding
argument_list|,
name|configuration
argument_list|)
return|;
block|}
else|else
block|{
comment|// no json encoding specified --> assume json encoding is unicode and determine the specific unicode encoding according to RFC-4627
comment|// this is a temporary solution, it can be removed as soon as jsonpath offers the encoding detection
name|JsonStream
name|jsonStream
init|=
operator|new
name|JsonStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
return|return
name|path
operator|.
name|read
argument_list|(
name|jsonStream
argument_list|,
name|jsonStream
operator|.
name|getEncoding
argument_list|()
operator|.
name|name
argument_list|()
argument_list|,
name|configuration
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

