begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.support
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
name|support
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
name|ActiveMQConnectionFactory
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
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
DECL|class|SjmsConnectionTestSupport
specifier|public
specifier|abstract
class|class
name|SjmsConnectionTestSupport
block|{
static|static
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.activemq.default.directory.prefix"
argument_list|,
literal|"target/activemq/"
argument_list|)
expr_stmt|;
block|}
DECL|field|VM_BROKER_CONNECT_STRING
specifier|public
specifier|static
specifier|final
name|String
name|VM_BROKER_CONNECT_STRING
init|=
literal|"vm://broker"
decl_stmt|;
DECL|field|TCP_BROKER_CONNECT_STRING
specifier|public
specifier|static
specifier|final
name|String
name|TCP_BROKER_CONNECT_STRING
init|=
literal|"tcp://localhost:61616"
decl_stmt|;
DECL|field|logger
specifier|protected
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|vmTestConnectionFactory
specifier|private
name|ActiveMQConnectionFactory
name|vmTestConnectionFactory
decl_stmt|;
DECL|field|testConnectionFactory
specifier|private
name|ActiveMQConnectionFactory
name|testConnectionFactory
decl_stmt|;
DECL|field|brokerService
specifier|private
name|BrokerService
name|brokerService
decl_stmt|;
DECL|field|persistenceEnabled
specifier|private
name|boolean
name|persistenceEnabled
decl_stmt|;
DECL|method|getConnectionUri ()
specifier|public
specifier|abstract
name|String
name|getConnectionUri
parameter_list|()
function_decl|;
annotation|@
name|BeforeClass
DECL|method|setUpBeforeClass ()
specifier|public
specifier|static
name|void
name|setUpBeforeClass
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|AfterClass
DECL|method|tearDownAfterClass ()
specifier|public
specifier|static
name|void
name|tearDownAfterClass
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|getConnectionUri
argument_list|()
argument_list|)
operator|||
name|getConnectionUri
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"vm"
argument_list|)
condition|)
block|{
name|vmTestConnectionFactory
operator|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
name|VM_BROKER_CONNECT_STRING
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|createBroker
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|After
DECL|method|teardown ()
specifier|public
name|void
name|teardown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|vmTestConnectionFactory
operator|!=
literal|null
condition|)
block|{
name|vmTestConnectionFactory
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|testConnectionFactory
operator|!=
literal|null
condition|)
block|{
name|testConnectionFactory
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|brokerService
operator|!=
literal|null
condition|)
block|{
name|destroyBroker
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createTestConnectionFactory (String uri)
specifier|public
name|ActiveMQConnectionFactory
name|createTestConnectionFactory
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|ActiveMQConnectionFactory
name|cf
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|cf
operator|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
name|VM_BROKER_CONNECT_STRING
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cf
operator|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
return|return
name|cf
return|;
block|}
DECL|method|createBroker ()
specifier|protected
name|void
name|createBroker
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|connectString
init|=
name|getConnectionUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|connectString
argument_list|)
condition|)
block|{
name|connectString
operator|=
name|TCP_BROKER_CONNECT_STRING
expr_stmt|;
block|}
name|brokerService
operator|=
operator|new
name|BrokerService
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|setPersistent
argument_list|(
name|isPersistenceEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|addConnector
argument_list|(
name|connectString
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|start
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|waitUntilStarted
argument_list|()
expr_stmt|;
block|}
DECL|method|destroyBroker ()
specifier|protected
name|void
name|destroyBroker
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|brokerService
operator|!=
literal|null
condition|)
block|{
name|brokerService
operator|.
name|stop
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|waitUntilStopped
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|setTestConnectionFactory ( ActiveMQConnectionFactory testConnectionFactory)
specifier|public
name|void
name|setTestConnectionFactory
parameter_list|(
name|ActiveMQConnectionFactory
name|testConnectionFactory
parameter_list|)
block|{
name|this
operator|.
name|testConnectionFactory
operator|=
name|testConnectionFactory
expr_stmt|;
block|}
DECL|method|getTestConnectionFactory ()
specifier|public
name|ActiveMQConnectionFactory
name|getTestConnectionFactory
parameter_list|()
block|{
return|return
name|testConnectionFactory
return|;
block|}
DECL|method|setPersistenceEnabled (boolean persistenceEnabled)
specifier|public
name|void
name|setPersistenceEnabled
parameter_list|(
name|boolean
name|persistenceEnabled
parameter_list|)
block|{
name|this
operator|.
name|persistenceEnabled
operator|=
name|persistenceEnabled
expr_stmt|;
block|}
DECL|method|isPersistenceEnabled ()
specifier|public
name|boolean
name|isPersistenceEnabled
parameter_list|()
block|{
return|return
name|persistenceEnabled
return|;
block|}
block|}
end_class

end_unit

