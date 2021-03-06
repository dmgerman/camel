begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.batch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|batch
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|broker
operator|.
name|BrokerService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|store
operator|.
name|memory
operator|.
name|MemoryPersistenceAdapter
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
name|test
operator|.
name|AvailablePortFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExternalResource
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
comment|/**  * JUnit Test aspect that creates an embedded ActiveMQ broker at the beginning of each test and shuts it down after.  */
end_comment

begin_class
DECL|class|EmbeddedActiveMQBroker
specifier|public
class|class
name|EmbeddedActiveMQBroker
extends|extends
name|ExternalResource
block|{
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EmbeddedActiveMQBroker
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|brokerId
specifier|private
specifier|final
name|String
name|brokerId
decl_stmt|;
DECL|field|brokerService
specifier|private
name|BrokerService
name|brokerService
decl_stmt|;
DECL|field|tcpConnectorUri
specifier|private
specifier|final
name|String
name|tcpConnectorUri
decl_stmt|;
DECL|method|EmbeddedActiveMQBroker (String brokerId)
specifier|public
name|EmbeddedActiveMQBroker
parameter_list|(
name|String
name|brokerId
parameter_list|)
block|{
if|if
condition|(
operator|(
name|brokerId
operator|==
literal|null
operator|)
operator|||
operator|(
name|brokerId
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"brokerId is empty"
argument_list|)
throw|;
block|}
name|this
operator|.
name|brokerId
operator|=
name|brokerId
expr_stmt|;
name|tcpConnectorUri
operator|=
literal|"tcp://localhost:"
operator|+
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
name|brokerService
operator|=
operator|new
name|BrokerService
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|setBrokerId
argument_list|(
name|brokerId
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|setPersistent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|setUseJmx
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|brokerService
operator|.
name|setPersistenceAdapter
argument_list|(
operator|new
name|MemoryPersistenceAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|addConnector
argument_list|(
name|tcpConnectorUri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Problem creating brokerService"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|before ()
specifier|protected
name|void
name|before
parameter_list|()
throws|throws
name|Throwable
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Starting embedded broker[{}] on {}"
argument_list|,
name|brokerId
argument_list|,
name|tcpConnectorUri
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|after ()
specifier|protected
name|void
name|after
parameter_list|()
block|{
try|try
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Stopping embedded broker[{}]"
argument_list|,
name|brokerId
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Exception shutting down broker service"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getTcpConnectorUri ()
specifier|public
name|String
name|getTcpConnectorUri
parameter_list|()
block|{
return|return
name|tcpConnectorUri
return|;
block|}
block|}
end_class

end_unit

