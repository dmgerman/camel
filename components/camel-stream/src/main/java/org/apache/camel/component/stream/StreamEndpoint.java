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
name|Component
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
name|Consumer
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
name|Producer
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
name|DefaultEndpoint
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
name|Metadata
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
import|;
end_import

begin_comment
comment|/**  * The stream: component provides access to the system-in, system-out and system-err streams as well as allowing streaming of file and URL.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.3.0"
argument_list|,
name|scheme
operator|=
literal|"stream"
argument_list|,
name|title
operator|=
literal|"Stream"
argument_list|,
name|syntax
operator|=
literal|"stream:kind"
argument_list|,
name|consumerClass
operator|=
name|StreamConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"file,system"
argument_list|)
DECL|class|StreamEndpoint
specifier|public
class|class
name|StreamEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|charset
specifier|private
specifier|transient
name|Charset
name|charset
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|enums
operator|=
literal|"in,out,err,header,file,url"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|kind
specifier|private
name|String
name|kind
decl_stmt|;
annotation|@
name|UriParam
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
annotation|@
name|UriParam
DECL|field|fileName
specifier|private
name|String
name|fileName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|scanStream
specifier|private
name|boolean
name|scanStream
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|retry
specifier|private
name|boolean
name|retry
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|fileWatcher
specifier|private
name|boolean
name|fileWatcher
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|closeOnDone
specifier|private
name|boolean
name|closeOnDone
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|scanStreamDelay
specifier|private
name|long
name|scanStreamDelay
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|delay
specifier|private
name|long
name|delay
decl_stmt|;
annotation|@
name|UriParam
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|promptMessage
specifier|private
name|String
name|promptMessage
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|promptDelay
specifier|private
name|long
name|promptDelay
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"2000"
argument_list|)
DECL|field|initialPromptDelay
specifier|private
name|long
name|initialPromptDelay
init|=
literal|2000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|groupLines
specifier|private
name|int
name|groupLines
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|autoCloseCount
specifier|private
name|int
name|autoCloseCount
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|groupStrategy
specifier|private
name|GroupStrategy
name|groupStrategy
init|=
operator|new
name|DefaultGroupStrategy
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|prefix
operator|=
literal|"httpHeaders."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|)
DECL|field|httpHeaders
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpHeaders
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|connectTimeout
specifier|private
name|int
name|connectTimeout
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|readTimeout
specifier|private
name|int
name|readTimeout
decl_stmt|;
DECL|method|StreamEndpoint (String endpointUri, Component component)
specifier|public
name|StreamEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|StreamConsumer
name|answer
init|=
operator|new
name|StreamConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isFileWatcher
argument_list|()
operator|&&
operator|!
literal|"file"
operator|.
name|equals
argument_list|(
name|getKind
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"File watcher is only possible if reading streams from files"
argument_list|)
throw|;
block|}
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|StreamProducer
argument_list|(
name|this
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createExchange (Object body, long index, boolean last)
specifier|protected
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|body
parameter_list|,
name|long
name|index
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|StreamConstants
operator|.
name|STREAM_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|StreamConstants
operator|.
name|STREAM_COMPLETE
argument_list|,
name|last
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getKind ()
specifier|public
name|String
name|getKind
parameter_list|()
block|{
return|return
name|kind
return|;
block|}
comment|/**      * Kind of stream to use such as System.in or System.out.      */
DECL|method|setKind (String kind)
specifier|public
name|void
name|setKind
parameter_list|(
name|String
name|kind
parameter_list|)
block|{
name|this
operator|.
name|kind
operator|=
name|kind
expr_stmt|;
block|}
DECL|method|getFileName ()
specifier|public
name|String
name|getFileName
parameter_list|()
block|{
return|return
name|fileName
return|;
block|}
comment|/**      * When using the stream:file URI format, this option specifies the filename to stream to/from.      */
DECL|method|setFileName (String fileName)
specifier|public
name|void
name|setFileName
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|this
operator|.
name|fileName
operator|=
name|fileName
expr_stmt|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
comment|/**      * When using the stream:url URI format, this option specifies the URL to stream to/from.      * The input/output stream will be opened using the JDK URLConnection facility.      */
DECL|method|setUrl (String url)
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getDelay ()
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
comment|/**      * Initial delay in milliseconds before producing the stream.      */
DECL|method|setDelay (long delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
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
comment|/**      * You can configure the encoding (is a charset name) to use text-based streams (for example, message body is a String object).      * If not provided, Camel uses the JVM default Charset.      */
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
DECL|method|getPromptMessage ()
specifier|public
name|String
name|getPromptMessage
parameter_list|()
block|{
return|return
name|promptMessage
return|;
block|}
comment|/**      * Message prompt to use when reading from stream:in; for example, you could set this to Enter a command:      */
DECL|method|setPromptMessage (String promptMessage)
specifier|public
name|void
name|setPromptMessage
parameter_list|(
name|String
name|promptMessage
parameter_list|)
block|{
name|this
operator|.
name|promptMessage
operator|=
name|promptMessage
expr_stmt|;
block|}
DECL|method|getPromptDelay ()
specifier|public
name|long
name|getPromptDelay
parameter_list|()
block|{
return|return
name|promptDelay
return|;
block|}
comment|/**      * Optional delay in milliseconds before showing the message prompt.      */
DECL|method|setPromptDelay (long promptDelay)
specifier|public
name|void
name|setPromptDelay
parameter_list|(
name|long
name|promptDelay
parameter_list|)
block|{
name|this
operator|.
name|promptDelay
operator|=
name|promptDelay
expr_stmt|;
block|}
DECL|method|getInitialPromptDelay ()
specifier|public
name|long
name|getInitialPromptDelay
parameter_list|()
block|{
return|return
name|initialPromptDelay
return|;
block|}
comment|/**      * Initial delay in milliseconds before showing the message prompt. This delay occurs only once.      * Can be used during system startup to avoid message prompts being written while other logging is done to the system out.      */
DECL|method|setInitialPromptDelay (long initialPromptDelay)
specifier|public
name|void
name|setInitialPromptDelay
parameter_list|(
name|long
name|initialPromptDelay
parameter_list|)
block|{
name|this
operator|.
name|initialPromptDelay
operator|=
name|initialPromptDelay
expr_stmt|;
block|}
DECL|method|isScanStream ()
specifier|public
name|boolean
name|isScanStream
parameter_list|()
block|{
return|return
name|scanStream
return|;
block|}
comment|/**      * To be used for continuously reading a stream such as the unix tail command.      */
DECL|method|setScanStream (boolean scanStream)
specifier|public
name|void
name|setScanStream
parameter_list|(
name|boolean
name|scanStream
parameter_list|)
block|{
name|this
operator|.
name|scanStream
operator|=
name|scanStream
expr_stmt|;
block|}
DECL|method|getGroupStrategy ()
specifier|public
name|GroupStrategy
name|getGroupStrategy
parameter_list|()
block|{
return|return
name|groupStrategy
return|;
block|}
comment|/**      * Allows to use a custom GroupStrategy to control how to group lines.      */
DECL|method|setGroupStrategy (GroupStrategy strategy)
specifier|public
name|void
name|setGroupStrategy
parameter_list|(
name|GroupStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|groupStrategy
operator|=
name|strategy
expr_stmt|;
block|}
DECL|method|isRetry ()
specifier|public
name|boolean
name|isRetry
parameter_list|()
block|{
return|return
name|retry
return|;
block|}
comment|/**      * Will retry opening the stream if it's overwritten, somewhat like tail --retry      *<p/>      * If reading from files then you should also enable the fileWatcher option, to make it work reliable.      */
DECL|method|setRetry (boolean retry)
specifier|public
name|void
name|setRetry
parameter_list|(
name|boolean
name|retry
parameter_list|)
block|{
name|this
operator|.
name|retry
operator|=
name|retry
expr_stmt|;
block|}
DECL|method|isFileWatcher ()
specifier|public
name|boolean
name|isFileWatcher
parameter_list|()
block|{
return|return
name|fileWatcher
return|;
block|}
comment|/**      * To use JVM file watcher to listen for file change events to support re-loading files that may be overwritten, somewhat like tail --retry      */
DECL|method|setFileWatcher (boolean fileWatcher)
specifier|public
name|void
name|setFileWatcher
parameter_list|(
name|boolean
name|fileWatcher
parameter_list|)
block|{
name|this
operator|.
name|fileWatcher
operator|=
name|fileWatcher
expr_stmt|;
block|}
DECL|method|isCloseOnDone ()
specifier|public
name|boolean
name|isCloseOnDone
parameter_list|()
block|{
return|return
name|closeOnDone
return|;
block|}
comment|/**      * This option is used in combination with Splitter and streaming to the same file.      * The idea is to keep the stream open and only close when the Splitter is done, to improve performance.      * Mind this requires that you only stream to the same file, and not 2 or more files.      */
DECL|method|setCloseOnDone (boolean closeOnDone)
specifier|public
name|void
name|setCloseOnDone
parameter_list|(
name|boolean
name|closeOnDone
parameter_list|)
block|{
name|this
operator|.
name|closeOnDone
operator|=
name|closeOnDone
expr_stmt|;
block|}
DECL|method|getScanStreamDelay ()
specifier|public
name|long
name|getScanStreamDelay
parameter_list|()
block|{
return|return
name|scanStreamDelay
return|;
block|}
comment|/**      * Delay in milliseconds between read attempts when using scanStream.      */
DECL|method|setScanStreamDelay (long scanStreamDelay)
specifier|public
name|void
name|setScanStreamDelay
parameter_list|(
name|long
name|scanStreamDelay
parameter_list|)
block|{
name|this
operator|.
name|scanStreamDelay
operator|=
name|scanStreamDelay
expr_stmt|;
block|}
DECL|method|getGroupLines ()
specifier|public
name|int
name|getGroupLines
parameter_list|()
block|{
return|return
name|groupLines
return|;
block|}
comment|/**      * To group X number of lines in the consumer.      * For example to group 10 lines and therefore only spit out an Exchange with 10 lines, instead of 1 Exchange per line.      */
DECL|method|setGroupLines (int groupLines)
specifier|public
name|void
name|setGroupLines
parameter_list|(
name|int
name|groupLines
parameter_list|)
block|{
name|this
operator|.
name|groupLines
operator|=
name|groupLines
expr_stmt|;
block|}
DECL|method|getAutoCloseCount ()
specifier|public
name|int
name|getAutoCloseCount
parameter_list|()
block|{
return|return
name|autoCloseCount
return|;
block|}
comment|/**      * Number of messages to process before closing stream on Producer side.      * Never close stream by default (only when Producer is stopped). If more messages are sent, the stream is reopened for another autoCloseCount batch.      */
DECL|method|setAutoCloseCount (int autoCloseCount)
specifier|public
name|void
name|setAutoCloseCount
parameter_list|(
name|int
name|autoCloseCount
parameter_list|)
block|{
name|this
operator|.
name|autoCloseCount
operator|=
name|autoCloseCount
expr_stmt|;
block|}
DECL|method|getCharset ()
specifier|public
name|Charset
name|getCharset
parameter_list|()
block|{
return|return
name|charset
return|;
block|}
DECL|method|getHttpHeaders ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHttpHeaders
parameter_list|()
block|{
return|return
name|httpHeaders
return|;
block|}
comment|/**      * Optional http headers to use in request when using HTTP URL.      */
DECL|method|setHttpHeaders (Map<String, Object> httpHeaders)
specifier|public
name|void
name|setHttpHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpHeaders
parameter_list|)
block|{
name|this
operator|.
name|httpHeaders
operator|=
name|httpHeaders
expr_stmt|;
block|}
DECL|method|getConnectTimeout ()
specifier|public
name|int
name|getConnectTimeout
parameter_list|()
block|{
return|return
name|connectTimeout
return|;
block|}
comment|/**      * Sets a specified timeout value, in milliseconds, to be used      * when opening a communications link to the resource referenced      * by this URLConnection.  If the timeout expires before the      * connection can be established, a      * java.net.SocketTimeoutException is raised. A timeout of zero is      * interpreted as an infinite timeout.      */
DECL|method|setConnectTimeout (int connectTimeout)
specifier|public
name|void
name|setConnectTimeout
parameter_list|(
name|int
name|connectTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectTimeout
operator|=
name|connectTimeout
expr_stmt|;
block|}
DECL|method|getReadTimeout ()
specifier|public
name|int
name|getReadTimeout
parameter_list|()
block|{
return|return
name|readTimeout
return|;
block|}
comment|/**      * Sets the read timeout to a specified timeout, in      * milliseconds. A non-zero value specifies the timeout when      * reading from Input stream when a connection is established to a      * resource. If the timeout expires before there is data available      * for read, a java.net.SocketTimeoutException is raised. A      * timeout of zero is interpreted as an infinite timeout.      */
DECL|method|setReadTimeout (int readTimeout)
specifier|public
name|void
name|setReadTimeout
parameter_list|(
name|int
name|readTimeout
parameter_list|)
block|{
name|this
operator|.
name|readTimeout
operator|=
name|readTimeout
expr_stmt|;
block|}
comment|// Implementations
comment|//-------------------------------------------------------------------------
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|charset
operator|=
name|loadCharset
argument_list|()
expr_stmt|;
block|}
DECL|method|loadCharset ()
name|Charset
name|loadCharset
parameter_list|()
block|{
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|encoding
operator|=
name|Charset
operator|.
name|defaultCharset
argument_list|()
operator|.
name|name
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"No encoding parameter using default charset: {}"
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Charset
operator|.
name|isSupported
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The encoding: "
operator|+
name|encoding
operator|+
literal|" is not supported"
argument_list|)
throw|;
block|}
return|return
name|Charset
operator|.
name|forName
argument_list|(
name|encoding
argument_list|)
return|;
block|}
block|}
end_class

end_unit

