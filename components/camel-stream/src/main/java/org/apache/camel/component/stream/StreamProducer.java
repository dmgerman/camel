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
name|BufferedWriter
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
name|FileOutputStream
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
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
name|converter
operator|.
name|ObjectConverter
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
name|DefaultProducer
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

begin_class
DECL|class|StreamProducer
specifier|public
class|class
name|StreamProducer
extends|extends
name|DefaultProducer
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
name|StreamProducer
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
literal|"in,out,err,file,url,header"
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
DECL|field|outputStream
specifier|protected
name|OutputStream
name|outputStream
init|=
name|System
operator|.
name|out
decl_stmt|;
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
decl_stmt|;
DECL|field|delay
specifier|private
name|String
name|delay
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
DECL|field|file
specifier|private
name|String
name|file
decl_stmt|;
DECL|method|StreamProducer (Endpoint<StreamExchange> endpoint, String uri, Map<String, String> parameters)
specifier|public
name|StreamProducer
parameter_list|(
name|Endpoint
argument_list|<
name|StreamExchange
argument_list|>
name|endpoint
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
name|delay
operator|=
name|parameters
operator|.
name|get
argument_list|(
literal|"delay"
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
name|file
operator|=
name|parameters
operator|.
name|get
argument_list|(
literal|"file"
argument_list|)
expr_stmt|;
comment|// must remove the parameters this component support
name|parameters
operator|.
name|remove
argument_list|(
literal|"delay"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|remove
argument_list|(
literal|"url"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|remove
argument_list|(
literal|"file"
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
name|outputStream
operator|!=
literal|null
condition|)
block|{
name|outputStream
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
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|delay
operator|!=
literal|null
condition|)
block|{
name|long
name|ms
init|=
name|ObjectConverter
operator|.
name|toLong
argument_list|(
name|delay
argument_list|)
decl_stmt|;
name|delay
argument_list|(
name|ms
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"out"
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|outputStream
operator|=
name|System
operator|.
name|out
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"err"
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|outputStream
operator|=
name|System
operator|.
name|err
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
name|outputStream
operator|=
name|resolveStreamFromFile
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"header"
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|outputStream
operator|=
name|resolveStreamFromHeader
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"stream"
argument_list|)
argument_list|)
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
name|outputStream
operator|=
name|resolveStreamFromUrl
argument_list|()
expr_stmt|;
block|}
name|writeToStream
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|resolveStreamFromUrl ()
specifier|private
name|OutputStream
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
name|getOutputStream
argument_list|()
return|;
block|}
DECL|method|resolveStreamFromFile ()
specifier|private
name|OutputStream
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
literal|"About to write to file: "
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
name|FileOutputStream
argument_list|(
name|f
argument_list|)
return|;
block|}
DECL|method|resolveStreamFromHeader (Object o)
specifier|private
name|OutputStream
name|resolveStreamFromHeader
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|StreamComponentException
block|{
if|if
condition|(
name|o
operator|!=
literal|null
operator|&&
name|o
operator|instanceof
name|OutputStream
condition|)
block|{
return|return
operator|(
name|OutputStream
operator|)
name|o
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|StreamComponentException
argument_list|(
literal|"Expected OutputStream in header('stream'), found: "
operator|+
name|o
argument_list|)
throw|;
block|}
block|}
DECL|method|delay (long ms)
specifier|private
name|void
name|delay
parameter_list|(
name|long
name|ms
parameter_list|)
throws|throws
name|InterruptedException
block|{
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
literal|"Delaying "
operator|+
name|ms
operator|+
literal|" millis"
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
name|ms
argument_list|)
expr_stmt|;
block|}
DECL|method|writeToStream (Exchange exchange)
specifier|private
name|void
name|writeToStream
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
name|body
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
literal|"Writing "
operator|+
name|body
operator|+
literal|" to "
operator|+
name|outputStream
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|String
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"in text buffered mode"
argument_list|)
expr_stmt|;
name|BufferedWriter
name|bw
init|=
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|outputStream
argument_list|)
argument_list|)
decl_stmt|;
name|bw
operator|.
name|write
argument_list|(
operator|(
name|String
operator|)
name|body
argument_list|)
expr_stmt|;
name|bw
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|bw
operator|.
name|flush
argument_list|()
expr_stmt|;
name|bw
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"in binary stream mode"
argument_list|)
expr_stmt|;
name|outputStream
operator|.
name|write
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|body
argument_list|)
expr_stmt|;
block|}
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

