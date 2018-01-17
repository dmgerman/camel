begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|List
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
name|Ordered
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
name|component
operator|.
name|file
operator|.
name|GenericFile
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
name|GenericFileConsumer
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
name|support
operator|.
name|SynchronizationAdapter
import|;
end_import

begin_comment
comment|/**  * Base class for remote file consumers.  */
end_comment

begin_class
DECL|class|RemoteFileConsumer
specifier|public
specifier|abstract
class|class
name|RemoteFileConsumer
parameter_list|<
name|T
parameter_list|>
extends|extends
name|GenericFileConsumer
argument_list|<
name|T
argument_list|>
block|{
DECL|field|loggedIn
specifier|protected
specifier|transient
name|boolean
name|loggedIn
decl_stmt|;
DECL|field|loggedInWarning
specifier|protected
specifier|transient
name|boolean
name|loggedInWarning
decl_stmt|;
DECL|method|RemoteFileConsumer (RemoteFileEndpoint<T> endpoint, Processor processor, RemoteFileOperations<T> operations)
specifier|public
name|RemoteFileConsumer
parameter_list|(
name|RemoteFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
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
name|processor
argument_list|,
name|operations
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPollStrategy
argument_list|(
operator|new
name|RemoteFilePollingConsumerPollStrategy
argument_list|()
argument_list|)
expr_stmt|;
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
DECL|method|prePollCheck ()
specifier|protected
name|boolean
name|prePollCheck
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"prePollCheck on "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|remoteServerInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getMaximumReconnectAttempts
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// only use recoverable if we are allowed any re-connect attempts
name|recoverableConnectIfNecessary
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|connectIfNecessary
argument_list|()
expr_stmt|;
block|}
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
comment|// login failed should we thrown exception
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isThrowExceptionOnConnectFailed
argument_list|()
condition|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|loggedIn
condition|)
block|{
name|String
name|message
init|=
literal|"Cannot connect/login to: "
operator|+
name|remoteServer
argument_list|()
operator|+
literal|". Will skip this poll."
decl_stmt|;
if|if
condition|(
operator|!
name|loggedInWarning
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|loggedInWarning
operator|=
literal|true
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
else|else
block|{
comment|// need to log the failed log again
name|loggedInWarning
operator|=
literal|false
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|postPollCheck (int polledMessages)
specifier|protected
name|void
name|postPollCheck
parameter_list|(
name|int
name|polledMessages
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"postPollCheck on "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|remoteServerInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// if we did not poll any messages, but are configured to disconnect then we need to do this now
comment|// as there is no exchanges to be routed that otherwise will disconnect from the last UoW
if|if
condition|(
name|polledMessages
operator|==
literal|0
condition|)
block|{
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
literal|"postPollCheck disconnect from: {}"
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
block|}
annotation|@
name|Override
DECL|method|processExchange (Exchange exchange)
specifier|protected
name|boolean
name|processExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// mark the exchange to be processed synchronously as the ftp client is not thread safe
comment|// and we must execute the callbacks in the same thread as this consumer
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|UNIT_OF_WORK_PROCESS_SYNC
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// defer disconnect til the UoW is complete - but only the last exchange from the batch should do that
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
literal|true
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
name|isDisconnect
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"postPollCheck disconnect from: {}"
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|disconnect
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|allowHandover
parameter_list|()
block|{
comment|// do not allow handover as we must execute the callbacks in the same thread as this consumer
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getOrder
parameter_list|()
block|{
comment|// we want to disconnect last
return|return
name|Ordered
operator|.
name|LOWEST
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Disconnect"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|processExchange
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isRetrieveFile ()
specifier|protected
name|boolean
name|isRetrieveFile
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|isDownload
argument_list|()
return|;
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|disconnect
argument_list|()
expr_stmt|;
block|}
DECL|method|disconnect ()
specifier|protected
name|void
name|disconnect
parameter_list|()
block|{
comment|// eager indicate we are no longer logged in
name|loggedIn
operator|=
literal|false
expr_stmt|;
comment|// disconnect
try|try
block|{
if|if
condition|(
name|getOperations
argument_list|()
operator|.
name|isConnected
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Disconnecting from: {}"
argument_list|,
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getOperations
argument_list|()
operator|.
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
literal|"Error occurred while disconnecting from "
operator|+
name|remoteServer
argument_list|()
operator|+
literal|" due: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception will be ignored."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|forceDisconnect ()
specifier|protected
name|void
name|forceDisconnect
parameter_list|()
block|{
comment|// eager indicate we are no longer logged in
name|loggedIn
operator|=
literal|false
expr_stmt|;
comment|// disconnect
try|try
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Force disconnecting from: {}"
argument_list|,
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getOperations
argument_list|()
operator|.
name|forceDisconnect
argument_list|()
expr_stmt|;
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
literal|"Error occurred while disconnecting from "
operator|+
name|remoteServer
argument_list|()
operator|+
literal|" due: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception will be ignored."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|recoverableConnectIfNecessary ()
specifier|protected
name|void
name|recoverableConnectIfNecessary
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|connectIfNecessary
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Could not connect to: "
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|". Will try to recover."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|loggedIn
operator|=
literal|false
expr_stmt|;
block|}
comment|// recover by re-creating operations which should most likely be able to recover
if|if
condition|(
operator|!
name|loggedIn
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Trying to recover connection to: {} with a fresh client."
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
comment|// we want to preserve last FTP activity listener when we set a new operations
if|if
condition|(
name|operations
operator|instanceof
name|FtpOperations
condition|)
block|{
name|FtpOperations
name|ftpOperations
init|=
operator|(
name|FtpOperations
operator|)
name|operations
decl_stmt|;
name|FtpClientActivityListener
name|listener
init|=
name|ftpOperations
operator|.
name|getClientActivityListener
argument_list|()
decl_stmt|;
name|setOperations
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|createRemoteFileOperations
argument_list|()
argument_list|)
expr_stmt|;
name|getOperations
argument_list|()
operator|.
name|setEndpoint
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|listener
operator|!=
literal|null
condition|)
block|{
name|ftpOperations
operator|=
operator|(
name|FtpOperations
operator|)
name|getOperations
argument_list|()
expr_stmt|;
name|ftpOperations
operator|.
name|setClientActivityListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|setOperations
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|createRemoteFileOperations
argument_list|()
argument_list|)
expr_stmt|;
name|getOperations
argument_list|()
operator|.
name|setEndpoint
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connectIfNecessary
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|connectIfNecessary ()
specifier|protected
name|void
name|connectIfNecessary
parameter_list|()
throws|throws
name|IOException
block|{
comment|// We need to send a noop first to check if the connection is still open
name|boolean
name|isConnected
init|=
literal|false
decl_stmt|;
try|try
block|{
name|isConnected
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
name|ex
parameter_list|)
block|{
comment|// here we just ignore the exception and try to reconnect
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Exception checking connection status: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|loggedIn
operator|||
operator|!
name|isConnected
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Not connected/logged in, connecting to: {}"
argument_list|,
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|loggedIn
operator|=
name|getOperations
argument_list|()
operator|.
name|connect
argument_list|(
operator|(
name|RemoteFileConfiguration
operator|)
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|loggedIn
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Connected and logged in to: "
operator|+
name|remoteServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Returns human readable server information for logging purpose      */
DECL|method|remoteServer ()
specifier|protected
name|String
name|remoteServer
parameter_list|()
block|{
return|return
operator|(
operator|(
name|RemoteFileEndpoint
argument_list|<
name|?
argument_list|>
operator|)
name|endpoint
operator|)
operator|.
name|remoteServerInformation
argument_list|()
return|;
block|}
comment|/**      * Executes doPollDirectory and on exception checks if it can be ignored by calling ignoreCannotRetrieveFile.      *      * @param absolutePath  the path of the directory to poll      * @param dirName       the name of the directory to poll      * @param fileList      current list of files gathered      * @param depth         the current depth of the directory      * @return whether or not to continue polling,<tt>false</tt> means the maxMessagesPerPoll limit has been hit      * @throws GenericFileOperationFailedException if the exception during doPollDirectory can not be ignored      */
DECL|method|doSafePollSubDirectory (String absolutePath, String dirName, List<GenericFile<T>> fileList, int depth)
specifier|protected
name|boolean
name|doSafePollSubDirectory
parameter_list|(
name|String
name|absolutePath
parameter_list|,
name|String
name|dirName
parameter_list|,
name|List
argument_list|<
name|GenericFile
argument_list|<
name|T
argument_list|>
argument_list|>
name|fileList
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
try|try
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Polling sub directory: {} from: {}"
argument_list|,
name|absolutePath
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
comment|//Try to poll the directory
return|return
name|doPollDirectory
argument_list|(
name|absolutePath
argument_list|,
name|dirName
argument_list|,
name|fileList
argument_list|,
name|depth
argument_list|)
return|;
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
literal|"Caught exception {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ignoreCannotRetrieveFile
argument_list|(
name|absolutePath
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Ignoring file error {} for {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|absolutePath
argument_list|)
expr_stmt|;
comment|//indicate no files in this directory to poll, continue with fileList
return|return
literal|true
return|;
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Not ignoring file error {} for {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|absolutePath
argument_list|)
expr_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|GenericFileOperationFailedException
condition|)
block|{
throw|throw
operator|(
name|GenericFileOperationFailedException
operator|)
name|e
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot poll sub-directory: "
operator|+
name|absolutePath
operator|+
literal|" from: "
operator|+
name|endpoint
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|/**      * Poll directory given by dirName or absolutePath      *      * @param absolutePath The path of the directory to poll      * @param dirName The name of the directory to poll      * @param fileList current list of files gathered      * @param depth the current depth of the directory      * @return whether or not to continue polling,<tt>false</tt> means the maxMessagesPerPoll limit has been hit      */
DECL|method|doPollDirectory (String absolutePath, String dirName, List<GenericFile<T>> fileList, int depth)
specifier|protected
specifier|abstract
name|boolean
name|doPollDirectory
parameter_list|(
name|String
name|absolutePath
parameter_list|,
name|String
name|dirName
parameter_list|,
name|List
argument_list|<
name|GenericFile
argument_list|<
name|T
argument_list|>
argument_list|>
name|fileList
parameter_list|,
name|int
name|depth
parameter_list|)
function_decl|;
block|}
end_class

end_unit

