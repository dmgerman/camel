begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|pool
operator|.
name|PooledConnectionFactory
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
name|FileUtil
import|;
end_import

begin_comment
comment|/**  * A helper for unit testing with Apache ActiveMQ as embedded JMS broker.  */
end_comment

begin_class
DECL|class|CamelJmsTestHelper
specifier|public
specifier|final
class|class
name|CamelJmsTestHelper
block|{
DECL|field|counter
specifier|private
specifier|static
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|method|CamelJmsTestHelper ()
specifier|private
name|CamelJmsTestHelper
parameter_list|()
block|{     }
DECL|method|createConnectionFactory ()
specifier|public
specifier|static
name|ConnectionFactory
name|createConnectionFactory
parameter_list|()
block|{
return|return
name|createConnectionFactory
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|createConnectionFactory (String options)
specifier|public
specifier|static
name|ConnectionFactory
name|createConnectionFactory
parameter_list|(
name|String
name|options
parameter_list|)
block|{
comment|// using a unique broker name improves testing when running the entire test suite in the same JVM
name|int
name|id
init|=
name|counter
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
name|String
name|url
init|=
literal|"vm://test-broker-"
operator|+
name|id
operator|+
literal|"?broker.persistent=false&broker.useJmx=false"
decl_stmt|;
if|if
condition|(
name|options
operator|!=
literal|null
condition|)
block|{
name|url
operator|=
name|url
operator|+
literal|"&"
operator|+
name|options
expr_stmt|;
block|}
name|ActiveMQConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
name|url
argument_list|)
decl_stmt|;
comment|// optimize AMQ to be as fast as possible so unit testing is quicker
name|connectionFactory
operator|.
name|setCopyMessageOnSend
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|setOptimizeAcknowledge
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|setOptimizedMessageDispatch
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// When using asyncSend, producers will not be guaranteed to send in the order we
comment|// have in the tests (which may be confusing for queues) so we need this set to false.
comment|// Another way of guaranteeing order is to use persistent messages or transactions.
name|connectionFactory
operator|.
name|setUseAsyncSend
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|setAlwaysSessionAsync
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// use a pooled connection factory
name|PooledConnectionFactory
name|pooled
init|=
operator|new
name|PooledConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|pooled
operator|.
name|setMaxConnections
argument_list|(
literal|8
argument_list|)
expr_stmt|;
return|return
name|pooled
return|;
block|}
DECL|method|createPersistentConnectionFactory ()
specifier|public
specifier|static
name|ConnectionFactory
name|createPersistentConnectionFactory
parameter_list|()
block|{
return|return
name|createPersistentConnectionFactory
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|createPersistentConnectionFactory (String options)
specifier|public
specifier|static
name|ConnectionFactory
name|createPersistentConnectionFactory
parameter_list|(
name|String
name|options
parameter_list|)
block|{
comment|// using a unique broker name improves testing when running the entire test suite in the same JVM
name|int
name|id
init|=
name|counter
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
comment|// use an unique data directory in target
name|String
name|dir
init|=
literal|"target/activemq-data-"
operator|+
name|id
decl_stmt|;
comment|// remove dir so its empty on startup
name|FileUtil
operator|.
name|removeDir
argument_list|(
operator|new
name|File
argument_list|(
name|dir
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|url
init|=
literal|"vm://test-broker-"
operator|+
name|id
operator|+
literal|"?broker.persistent=true&broker.useJmx=false&broker.dataDirectory="
operator|+
name|dir
decl_stmt|;
if|if
condition|(
name|options
operator|!=
literal|null
condition|)
block|{
name|url
operator|=
name|url
operator|+
literal|"&"
operator|+
name|options
expr_stmt|;
block|}
name|ActiveMQConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
name|url
argument_list|)
decl_stmt|;
comment|// optimize AMQ to be as fast as possible so unit testing is quicker
name|connectionFactory
operator|.
name|setCopyMessageOnSend
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|setOptimizeAcknowledge
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|setOptimizedMessageDispatch
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|setUseAsyncSend
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|setAlwaysSessionAsync
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// use a pooled connection factory
name|PooledConnectionFactory
name|pooled
init|=
operator|new
name|PooledConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|pooled
operator|.
name|setMaxConnections
argument_list|(
literal|8
argument_list|)
expr_stmt|;
return|return
name|pooled
return|;
block|}
block|}
end_class

end_unit

