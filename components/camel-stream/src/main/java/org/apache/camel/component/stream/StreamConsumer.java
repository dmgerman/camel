begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stream
package|;
end_package

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
name|InputStreamReader
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
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|Processor
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
name|impl
operator|.
name|DefaultConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Consumer that can read from any stream  */
end_comment

begin_class
DECL|class|StreamConsumer
specifier|public
class|class
name|StreamConsumer
extends|extends
name|DefaultConsumer
argument_list|<
name|StreamExchange
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|StreamConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|TYPES
specifier|private
specifier|static
specifier|final
name|String
name|TYPES
init|=
literal|"in"
decl_stmt|;
DECL|field|INVALID_URI
specifier|private
specifier|static
specifier|final
name|String
name|INVALID_URI
init|=
literal|"Invalid uri, valid form: 'stream:{"
operator|+
name|TYPES
operator|+
literal|"}'"
decl_stmt|;
DECL|field|TYPES_LIST
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|TYPES_LIST
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|TYPES
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|inputStream
specifier|protected
name|InputStream
name|inputStream
init|=
name|System
operator|.
name|in
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|Endpoint
argument_list|<
name|StreamExchange
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
DECL|field|file
specifier|private
name|String
name|file
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
DECL|method|StreamConsumer (Endpoint<StreamExchange> endpoint, Processor processor, String uri, Map<String, String> parameters)
specifier|public
name|StreamConsumer
parameter_list|(
name|Endpoint
argument_list|<
name|StreamExchange
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|uri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|file
operator|=
name|parameters
operator|.
name|get
argument_list|(
literal|"file"
argument_list|)
expr_stmt|;
name|url
operator|=
name|parameters
operator|.
name|get
argument_list|(
literal|"url"
argument_list|)
expr_stmt|;
comment|// must remove the options this component supports
name|parameters
operator|.
name|remove
argument_list|(
literal|"file"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|remove
argument_list|(
literal|"url"
argument_list|)
expr_stmt|;
name|validateUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
literal|"in"
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|inputStream
operator|=
name|System
operator|.
name|in
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|inputStream
operator|=
name|resolveStreamFromFile
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"url"
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|inputStream
operator|=
name|resolveStreamFromUrl
argument_list|()
expr_stmt|;
block|}
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
try|try
block|{
while|while
condition|(
operator|(
name|line
operator|=
name|br
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|consume
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StreamComponentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StreamComponentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|br
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|inputStream
operator|!=
literal|null
condition|)
block|{
name|inputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|consume (Object o)
specifier|public
name|void
name|consume
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|StreamMessage
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|resolveStreamFromUrl ()
specifier|private
name|InputStream
name|resolveStreamFromUrl
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|u
init|=
name|url
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|u
argument_list|)
decl_stmt|;
name|URLConnection
name|c
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
return|return
name|c
operator|.
name|getInputStream
argument_list|()
return|;
block|}
DECL|method|resolveStreamFromFile ()
specifier|private
name|InputStream
name|resolveStreamFromFile
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|fileName
init|=
name|file
operator|!=
literal|null
condition|?
name|file
operator|.
name|trim
argument_list|()
else|:
literal|"_file"
decl_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"About to read from file: "
operator|+
name|f
argument_list|)
expr_stmt|;
block|}
name|f
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
return|return
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
return|;
block|}
DECL|method|validateUri (String uri)
specifier|private
name|void
name|validateUri
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|s
init|=
name|uri
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|INVALID_URI
argument_list|)
throw|;
block|}
name|String
index|[]
name|t
init|=
name|s
index|[
literal|1
index|]
operator|.
name|split
argument_list|(
literal|"\\?"
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|length
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|INVALID_URI
argument_list|)
throw|;
block|}
name|this
operator|.
name|uri
operator|=
name|t
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|TYPES_LIST
operator|.
name|contains
argument_list|(
name|this
operator|.
name|uri
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|INVALID_URI
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

