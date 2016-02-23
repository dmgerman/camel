begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
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
name|concurrent
operator|.
name|TimeUnit
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
name|TimeoutException
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|client
operator|.
name|promises
operator|.
name|ResponsePromise
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|requests
operator|.
name|EtcdKeyGetRequest
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|responses
operator|.
name|EtcdKeysResponse
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
DECL|class|EtcdWatchConsumer
specifier|public
class|class
name|EtcdWatchConsumer
extends|extends
name|AbstractEtcdConsumer
implements|implements
name|ResponsePromise
operator|.
name|IsSimplePromiseResponseHandler
argument_list|<
name|EtcdKeysResponse
argument_list|>
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EtcdWatchConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|EtcdWatchEndpoint
name|endpoint
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|EtcdConfiguration
name|configuration
decl_stmt|;
DECL|field|defaultPath
specifier|private
specifier|final
name|String
name|defaultPath
decl_stmt|;
DECL|method|EtcdWatchConsumer (EtcdWatchEndpoint endpoint, Processor processor, EtcdConfiguration configuration, EtcdNamespace namespace, String path)
specifier|public
name|EtcdWatchConsumer
parameter_list|(
name|EtcdWatchEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|EtcdConfiguration
name|configuration
parameter_list|,
name|EtcdNamespace
name|namespace
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|,
name|namespace
argument_list|,
name|path
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
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|defaultPath
operator|=
name|endpoint
operator|.
name|getRemainingPath
argument_list|(
name|path
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
name|watch
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onResponse (ResponsePromise<EtcdKeysResponse> promise)
specifier|public
name|void
name|onResponse
parameter_list|(
name|ResponsePromise
argument_list|<
name|EtcdKeysResponse
argument_list|>
name|promise
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
block|{
name|EtcdKeysResponse
name|response
init|=
name|promise
operator|.
name|get
argument_list|()
decl_stmt|;
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
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EtcdConstants
operator|.
name|ETCD_PATH
argument_list|,
name|response
operator|.
name|node
operator|.
name|key
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
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
name|watch
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Timeout watching for "
operator|+
name|defaultPath
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isSendEmptyExchangeOnTimeout
argument_list|()
condition|)
block|{
try|try
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
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EtcdConstants
operator|.
name|ETCD_TIMEOUT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EtcdConstants
operator|.
name|ETCD_PATH
argument_list|,
name|defaultPath
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
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
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|watch ()
specifier|private
name|void
name|watch
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
return|return;
block|}
name|EtcdKeyGetRequest
name|request
init|=
name|getClient
argument_list|()
operator|.
name|get
argument_list|(
name|defaultPath
argument_list|)
operator|.
name|waitForChange
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isRecursive
argument_list|()
condition|)
block|{
name|request
operator|.
name|recursive
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getTimeout
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|timeout
argument_list|(
name|configuration
operator|.
name|getTimeout
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|request
operator|.
name|send
argument_list|()
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

