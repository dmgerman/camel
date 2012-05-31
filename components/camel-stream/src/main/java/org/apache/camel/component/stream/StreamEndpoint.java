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

begin_class
DECL|class|StreamEndpoint
specifier|public
class|class
name|StreamEndpoint
extends|extends
name|DefaultEndpoint
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
name|StreamEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
DECL|field|fileName
specifier|private
name|String
name|fileName
decl_stmt|;
DECL|field|scanStream
specifier|private
name|boolean
name|scanStream
decl_stmt|;
DECL|field|retry
specifier|private
name|boolean
name|retry
decl_stmt|;
DECL|field|scanStreamDelay
specifier|private
name|long
name|scanStreamDelay
decl_stmt|;
DECL|field|delay
specifier|private
name|long
name|delay
decl_stmt|;
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
DECL|field|promptMessage
specifier|private
name|String
name|promptMessage
decl_stmt|;
DECL|field|promptDelay
specifier|private
name|long
name|promptDelay
decl_stmt|;
DECL|field|initialPromptDelay
specifier|private
name|long
name|initialPromptDelay
init|=
literal|2000
decl_stmt|;
DECL|field|groupLines
specifier|private
name|int
name|groupLines
decl_stmt|;
DECL|field|autoCloseCount
specifier|private
name|int
name|autoCloseCount
decl_stmt|;
DECL|field|charset
specifier|private
name|Charset
name|charset
decl_stmt|;
DECL|field|groupStrategy
specifier|private
name|GroupStrategy
name|groupStrategy
init|=
operator|new
name|DefaultGroupStrategy
argument_list|()
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
annotation|@
name|Deprecated
DECL|method|StreamEndpoint (String endpointUri)
specifier|public
name|StreamEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
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
return|return
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
comment|// Properties
comment|//-------------------------------------------------------------------------
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
name|LOG
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

