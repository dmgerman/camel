begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nagios
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nagios
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
name|ExecutorService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|core
operator|.
name|INagiosPassiveCheckSender
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|core
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|core
operator|.
name|MessagePayload
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|core
operator|.
name|NonBlockingNagiosPassiveCheckSender
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
name|impl
operator|.
name|DefaultProducer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nagios
operator|.
name|NagiosConstants
operator|.
name|HOST_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nagios
operator|.
name|NagiosConstants
operator|.
name|LEVEL
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nagios
operator|.
name|NagiosConstants
operator|.
name|SERVICE_NAME
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|NagiosProducer
specifier|public
class|class
name|NagiosProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|sender
specifier|private
specifier|final
name|INagiosPassiveCheckSender
name|sender
decl_stmt|;
DECL|method|NagiosProducer (NagiosEndpoint endpoint, INagiosPassiveCheckSender sender)
specifier|public
name|NagiosProducer
parameter_list|(
name|NagiosEndpoint
name|endpoint
parameter_list|,
name|INagiosPassiveCheckSender
name|sender
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|sender
operator|=
name|sender
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
name|String
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Level
name|level
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|LEVEL
argument_list|,
name|Level
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|level
operator|==
literal|null
condition|)
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|LEVEL
argument_list|,
name|Level
operator|.
name|OK
operator|.
name|name
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|level
operator|=
name|Level
operator|.
name|valueOf
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|String
name|serviceName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SERVICE_NAME
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|hostName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HOST_NAME
argument_list|,
literal|"localhost"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|MessagePayload
name|payload
init|=
operator|new
name|MessagePayload
argument_list|(
name|hostName
argument_list|,
name|level
operator|.
name|ordinal
argument_list|()
argument_list|,
name|serviceName
argument_list|,
name|message
argument_list|)
decl_stmt|;
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
literal|"Sending notification to Nagios: "
operator|+
name|payload
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sender
operator|.
name|send
argument_list|(
name|payload
argument_list|)
expr_stmt|;
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
literal|"Sending notification done"
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
comment|// if non blocking then set a executor service on it
if|if
condition|(
name|sender
operator|instanceof
name|NonBlockingNagiosPassiveCheckSender
condition|)
block|{
name|NonBlockingNagiosPassiveCheckSender
name|nonBlocking
init|=
operator|(
name|NonBlockingNagiosPassiveCheckSender
operator|)
name|sender
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|nonBlocking
operator|.
name|setExecutor
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
comment|// if non blocking then shutdown executor
if|if
condition|(
name|sender
operator|instanceof
name|NonBlockingNagiosPassiveCheckSender
condition|)
block|{
operator|(
operator|(
name|NonBlockingNagiosPassiveCheckSender
operator|)
name|sender
operator|)
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

