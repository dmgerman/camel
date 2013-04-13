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
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|concurrent
operator|.
name|CopyOnWriteArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|camel
operator|.
name|impl
operator|.
name|DefaultMessage
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
comment|/**  * Consumer that can read from streams  */
end_comment

begin_class
DECL|class|StreamConsumer
specifier|public
class|class
name|StreamConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|Runnable
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
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
literal|"in,file,url"
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
DECL|field|executor
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|inputStream
specifier|private
specifier|volatile
name|InputStream
name|inputStream
init|=
name|System
operator|.
name|in
decl_stmt|;
DECL|field|inputStreamToClose
specifier|private
specifier|volatile
name|InputStream
name|inputStreamToClose
decl_stmt|;
DECL|field|endpoint
specifier|private
name|StreamEndpoint
name|endpoint
decl_stmt|;
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
DECL|field|initialPromptDone
specifier|private
name|boolean
name|initialPromptDone
decl_stmt|;
DECL|field|lines
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|lines
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|StreamConsumer (StreamEndpoint endpoint, Processor processor, String uri)
specifier|public
name|StreamConsumer
parameter_list|(
name|StreamEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|uri
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
name|initializeStream
argument_list|()
expr_stmt|;
name|executor
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getGroupLines
argument_list|()
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option groupLines must be 0 or positive number, was "
operator|+
name|endpoint
operator|.
name|getGroupLines
argument_list|()
argument_list|)
throw|;
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
name|executor
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|executor
operator|=
literal|null
expr_stmt|;
block|}
name|lines
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// do not close regular inputStream as it may be System.in etc.
name|IOHelper
operator|.
name|close
argument_list|(
name|inputStreamToClose
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|readFromStream
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// we are closing down so ignore
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|initializeStream ()
specifier|private
name|BufferedReader
name|initializeStream
parameter_list|()
throws|throws
name|Exception
block|{
comment|// close old stream, before obtaining a new stream
name|IOHelper
operator|.
name|close
argument_list|(
name|inputStreamToClose
argument_list|)
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
name|inputStreamToClose
operator|=
literal|null
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
name|inputStreamToClose
operator|=
name|inputStream
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
name|inputStreamToClose
operator|=
name|inputStream
expr_stmt|;
block|}
name|Charset
name|charset
init|=
name|endpoint
operator|.
name|getCharset
argument_list|()
decl_stmt|;
return|return
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|,
name|charset
argument_list|)
argument_list|)
return|;
block|}
DECL|method|readFromStream ()
specifier|private
name|void
name|readFromStream
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|line
decl_stmt|;
name|BufferedReader
name|br
init|=
name|initializeStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isScanStream
argument_list|()
condition|)
block|{
comment|// repeat scanning from stream
while|while
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
name|line
operator|=
name|br
operator|.
name|readLine
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Read line: {}"
argument_list|,
name|line
argument_list|)
expr_stmt|;
name|boolean
name|eos
init|=
name|line
operator|==
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|eos
operator|&&
name|isRunAllowed
argument_list|()
condition|)
block|{
name|processLine
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|eos
operator|&&
name|isRunAllowed
argument_list|()
operator|&&
name|endpoint
operator|.
name|isRetry
argument_list|()
condition|)
block|{
comment|//try and re-open stream
name|br
operator|=
name|initializeStream
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|endpoint
operator|.
name|getScanStreamDelay
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
else|else
block|{
comment|// regular read stream once until end of stream
name|boolean
name|eos
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|eos
operator|&&
name|isRunAllowed
argument_list|()
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|getPromptMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|doPromptMessage
argument_list|()
expr_stmt|;
block|}
name|line
operator|=
name|br
operator|.
name|readLine
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Read line: {}"
argument_list|,
name|line
argument_list|)
expr_stmt|;
name|eos
operator|=
name|line
operator|==
literal|null
expr_stmt|;
if|if
condition|(
operator|!
name|eos
operator|&&
name|isRunAllowed
argument_list|()
condition|)
block|{
name|processLine
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// important: do not close the reader as it will close the standard system.in etc.
block|}
comment|/**      * Strategy method for processing the line      */
DECL|method|processLine (String line)
specifier|protected
specifier|synchronized
name|void
name|processLine
parameter_list|(
name|String
name|line
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|endpoint
operator|.
name|getGroupLines
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// remember line
name|lines
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
comment|// should we flush lines?
if|if
condition|(
name|lines
operator|.
name|size
argument_list|()
operator|>=
name|endpoint
operator|.
name|getGroupLines
argument_list|()
condition|)
block|{
comment|// spit out lines
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
comment|// create message with the lines
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|copy
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|lines
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|endpoint
operator|.
name|getGroupStrategy
argument_list|()
operator|.
name|groupLines
argument_list|(
name|copy
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|msg
argument_list|)
expr_stmt|;
comment|// clear lines
name|lines
operator|.
name|clear
argument_list|()
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
block|}
else|else
block|{
comment|// single line
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|msg
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
block|}
comment|/**      * Strategy method for prompting the prompt message      */
DECL|method|doPromptMessage ()
specifier|protected
name|void
name|doPromptMessage
parameter_list|()
block|{
name|long
name|delay
init|=
literal|0
decl_stmt|;
if|if
condition|(
operator|!
name|initialPromptDone
operator|&&
name|endpoint
operator|.
name|getInitialPromptDelay
argument_list|()
operator|>
literal|0
condition|)
block|{
name|initialPromptDone
operator|=
literal|true
expr_stmt|;
name|delay
operator|=
name|endpoint
operator|.
name|getInitialPromptDelay
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getPromptDelay
argument_list|()
operator|>
literal|0
condition|)
block|{
name|delay
operator|=
name|endpoint
operator|.
name|getPromptDelay
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|delay
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|inputStream
operator|==
name|System
operator|.
name|in
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|endpoint
operator|.
name|getPromptMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|endpoint
operator|.
name|getUrl
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|u
argument_list|,
literal|"url"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"About to read from url: {}"
argument_list|,
name|u
argument_list|)
expr_stmt|;
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
name|endpoint
operator|.
name|getFileName
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|fileName
argument_list|,
literal|"fileName"
argument_list|)
expr_stmt|;
name|FileInputStream
name|fileStream
decl_stmt|;
name|File
name|file
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
literal|"File to be scanned : {}, path : {}"
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|file
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|fileStream
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|INVALID_URI
argument_list|)
throw|;
block|}
return|return
name|fileStream
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
name|IllegalArgumentException
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
name|this
operator|.
name|uri
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
condition|)
block|{
name|this
operator|.
name|uri
operator|=
name|this
operator|.
name|uri
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
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

