begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.lumberjack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|lumberjack
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
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
name|component
operator|.
name|lumberjack
operator|.
name|io
operator|.
name|LumberjackMessageProcessor
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
name|lumberjack
operator|.
name|io
operator|.
name|LumberjackServer
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
name|util
operator|.
name|concurrent
operator|.
name|CamelThreadFactory
import|;
end_import

begin_class
DECL|class|LumberjackConsumer
specifier|public
class|class
name|LumberjackConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|lumberjackServer
specifier|private
specifier|final
name|LumberjackServer
name|lumberjackServer
decl_stmt|;
DECL|method|LumberjackConsumer (LumberjackEndpoint endpoint, Processor processor, String host, int port, SSLContext sslContext)
specifier|public
name|LumberjackConsumer
parameter_list|(
name|LumberjackEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|SSLContext
name|sslContext
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|lumberjackServer
operator|=
operator|new
name|LumberjackServer
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|sslContext
argument_list|,
name|getThreadFactory
argument_list|()
argument_list|,
name|this
operator|::
name|onMessageReceived
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
name|lumberjackServer
operator|.
name|start
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
name|lumberjackServer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doResume
argument_list|()
expr_stmt|;
name|lumberjackServer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
name|lumberjackServer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|doSuspend
argument_list|()
expr_stmt|;
block|}
DECL|method|getThreadFactory ()
specifier|private
name|ThreadFactory
name|getThreadFactory
parameter_list|()
block|{
name|String
name|threadNamePattern
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|getThreadNamePattern
argument_list|()
decl_stmt|;
return|return
operator|new
name|CamelThreadFactory
argument_list|(
name|threadNamePattern
argument_list|,
literal|"LumberjackNettyExecutor"
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|onMessageReceived (Object payload, LumberjackMessageProcessor.Callback callback)
specifier|private
name|void
name|onMessageReceived
parameter_list|(
name|Object
name|payload
parameter_list|,
name|LumberjackMessageProcessor
operator|.
name|Callback
name|callback
parameter_list|)
block|{
comment|// Create the exchange
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
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
name|payload
argument_list|)
expr_stmt|;
comment|// Process the exchange
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
name|callback
operator|.
name|onComplete
argument_list|(
operator|!
name|exchange
operator|.
name|isFailed
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

