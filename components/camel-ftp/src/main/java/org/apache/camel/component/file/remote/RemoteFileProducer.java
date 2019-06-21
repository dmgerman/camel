begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
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
operator|.
name|remote
package|;
end_package

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
name|component
operator|.
name|file
operator|.
name|GenericFileOperationFailedException
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
name|GenericFileProducer
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
name|URISupport
import|;
end_import

begin_comment
comment|/**  * Generic remote file producer for all the FTP variations.  */
end_comment

begin_class
DECL|class|RemoteFileProducer
specifier|public
class|class
name|RemoteFileProducer
parameter_list|<
name|T
parameter_list|>
extends|extends
name|GenericFileProducer
argument_list|<
name|T
argument_list|>
block|{
DECL|field|loggedIn
specifier|private
name|boolean
name|loggedIn
decl_stmt|;
DECL|field|remoteFileProducerToString
specifier|private
specifier|transient
name|String
name|remoteFileProducerToString
decl_stmt|;
DECL|method|RemoteFileProducer (RemoteFileEndpoint<T> endpoint, RemoteFileOperations<T> operations)
specifier|protected
name|RemoteFileProducer
parameter_list|(
name|RemoteFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|RemoteFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|operations
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getFileSeparator ()
specifier|public
name|String
name|getFileSeparator
parameter_list|()
block|{
return|return
literal|"/"
return|;
block|}
annotation|@
name|Override
DECL|method|normalizePath (String name)
specifier|public
name|String
name|normalizePath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
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
comment|// store any existing file header which we want to keep and propagate
specifier|final
name|String
name|existing
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// create the target file name
name|String
name|target
init|=
name|createFileName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
try|try
block|{
name|processExchange
argument_list|(
name|exchange
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// remove the write file name header as we only want to use it once (by design)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|OVERRULE_FILE_NAME
argument_list|)
expr_stmt|;
comment|// and restore existing file name
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|existing
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getOperations ()
specifier|protected
name|RemoteFileOperations
argument_list|<
name|T
argument_list|>
name|getOperations
parameter_list|()
block|{
return|return
operator|(
name|RemoteFileOperations
argument_list|<
name|T
argument_list|>
operator|)
name|operations
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getEndpoint ()
specifier|public
name|RemoteFileEndpoint
argument_list|<
name|T
argument_list|>
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|RemoteFileEndpoint
argument_list|<
name|T
argument_list|>
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * The file could not be written. We need to disconnect from the remote server.      */
DECL|method|handleFailedWrite (Exchange exchange, Exception exception)
specifier|public
name|void
name|handleFailedWrite
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Exception
name|exception
parameter_list|)
throws|throws
name|Exception
block|{
name|loggedIn
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|isStopping
argument_list|()
operator|||
name|isStopped
argument_list|()
condition|)
block|{
comment|// if we are stopping then ignore any exception during a poll
name|log
operator|.
name|debug
argument_list|(
literal|"Exception occurred during stopping: {}"
argument_list|,
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Writing file failed with: {}"
argument_list|,
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|disconnect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore exception
name|log
operator|.
name|debug
argument_list|(
literal|"Ignored exception during disconnect: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// rethrow the original exception*/
throw|throw
name|exception
throw|;
block|}
block|}
DECL|method|disconnect ()
specifier|public
name|void
name|disconnect
parameter_list|()
throws|throws
name|GenericFileOperationFailedException
block|{
name|loggedIn
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|getOperations
argument_list|()
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Disconnecting from: {}"
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|getOperations
argument_list|()
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|preWriteCheck (Exchange exchange)
specifier|public
name|void
name|preWriteCheck
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// before writing send a noop to see if the connection is alive and works
name|boolean
name|noop
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|loggedIn
condition|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSendNoop
argument_list|()
condition|)
block|{
try|try
block|{
name|noop
operator|=
name|getOperations
argument_list|()
operator|.
name|sendNoop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore as we will try to recover connection
name|noop
operator|=
literal|false
expr_stmt|;
comment|// mark as not logged in, since the noop failed
name|loggedIn
operator|=
literal|false
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"preWriteCheck send noop success: {}"
argument_list|,
name|noop
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// okay send noop is disabled then we would regard the op as success
name|noop
operator|=
literal|true
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"preWriteCheck send noop disabled"
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if not alive then reconnect
if|if
condition|(
operator|!
name|noop
condition|)
block|{
try|try
block|{
name|connectIfNecessary
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|loggedIn
operator|=
literal|false
expr_stmt|;
comment|// must be logged in to be able to upload the file
throw|throw
name|e
throw|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|postWriteCheck (Exchange exchange)
specifier|public
name|void
name|postWriteCheck
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|boolean
name|isLast
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|,
literal|false
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|isLast
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|isDisconnectOnBatchComplete
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"postWriteCheck disconnect on batch complete from: {}"
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|disconnect
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isDisconnect
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"postWriteCheck disconnect from: {}"
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|disconnect
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|GenericFileOperationFailedException
name|e
parameter_list|)
block|{
comment|// ignore just log a warning
name|log
operator|.
name|warn
argument_list|(
literal|"Exception occurred during disconnecting from: "
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|" "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|log
operator|.
name|debug
argument_list|(
literal|"Starting"
argument_list|)
expr_stmt|;
comment|// do not connect when component starts, just wait until we process as we will
comment|// connect at that time if needed
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
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
try|try
block|{
name|disconnect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Exception occurred during disconnecting from: "
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|" "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|connectIfNecessary (Exchange exchange)
specifier|protected
name|void
name|connectIfNecessary
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
if|if
condition|(
operator|!
name|loggedIn
operator|||
operator|!
name|getOperations
argument_list|()
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Not already connected/logged in. Connecting to: {}"
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteFileConfiguration
name|config
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|loggedIn
operator|=
name|getOperations
argument_list|()
operator|.
name|connect
argument_list|(
name|config
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|loggedIn
condition|)
block|{
return|return;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Connected and logged in to: {}"
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
comment|// this producer is stateful because the remote file operations is not thread safe
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|remoteFileProducerToString
operator|==
literal|null
condition|)
block|{
name|remoteFileProducerToString
operator|=
literal|"RemoteFileProducer["
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|remoteFileProducerToString
return|;
block|}
block|}
end_class

end_unit

