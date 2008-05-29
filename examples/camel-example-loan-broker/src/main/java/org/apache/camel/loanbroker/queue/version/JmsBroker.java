begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.loanbroker.queue.version
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|loanbroker
operator|.
name|queue
operator|.
name|version
package|;
end_package

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

begin_class
DECL|class|JmsBroker
specifier|public
specifier|final
class|class
name|JmsBroker
block|{
DECL|field|jmsBrokerThread
name|JMSEmbeddedBroker
name|jmsBrokerThread
decl_stmt|;
DECL|field|jmsBrokerUrl
name|String
name|jmsBrokerUrl
init|=
literal|"tcp://localhost:61616"
decl_stmt|;
DECL|field|activeMQStorageDir
name|String
name|activeMQStorageDir
decl_stmt|;
DECL|method|JmsBroker ()
specifier|public
name|JmsBroker
parameter_list|()
block|{     }
DECL|method|JmsBroker (String url)
specifier|public
name|JmsBroker
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|jmsBrokerUrl
operator|=
name|url
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|jmsBrokerThread
operator|=
operator|new
name|JMSEmbeddedBroker
argument_list|(
name|jmsBrokerUrl
argument_list|)
expr_stmt|;
name|jmsBrokerThread
operator|.
name|startBroker
argument_list|()
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|jmsBrokerThread
operator|.
name|shutdownBroker
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|jmsBrokerThread
operator|!=
literal|null
condition|)
block|{
name|jmsBrokerThread
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
name|jmsBrokerThread
operator|=
literal|null
expr_stmt|;
block|}
DECL|class|JMSEmbeddedBroker
class|class
name|JMSEmbeddedBroker
extends|extends
name|Thread
block|{
DECL|field|shutdownBroker
name|boolean
name|shutdownBroker
decl_stmt|;
DECL|field|brokerUrl
specifier|final
name|String
name|brokerUrl
decl_stmt|;
DECL|field|exception
name|Exception
name|exception
decl_stmt|;
DECL|method|JMSEmbeddedBroker (String url)
specifier|public
name|JMSEmbeddedBroker
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|brokerUrl
operator|=
name|url
expr_stmt|;
block|}
DECL|method|startBroker ()
specifier|public
name|void
name|startBroker
parameter_list|()
throws|throws
name|Exception
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|super
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|wait
argument_list|()
expr_stmt|;
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exception
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
comment|//ContainerWapper container;
name|BrokerService
name|broker
init|=
operator|new
name|BrokerService
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|broker
operator|.
name|setPersistenceAdapter
argument_list|(
operator|new
name|MemoryPersistenceAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setTmpDataDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"./target"
argument_list|)
argument_list|)
expr_stmt|;
name|broker
operator|.
name|addConnector
argument_list|(
name|brokerUrl
argument_list|)
expr_stmt|;
name|broker
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|notifyAll
argument_list|()
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
while|while
condition|(
operator|!
name|shutdownBroker
condition|)
block|{
name|wait
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
block|}
name|broker
operator|.
name|stop
argument_list|()
expr_stmt|;
name|broker
operator|=
literal|null
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exception
operator|=
name|e
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

