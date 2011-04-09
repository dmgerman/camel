begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
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

begin_comment
comment|/**  * A helper for unit testing with Apache ActiveMQ as embedded JMS broker.  *  * @version   */
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
name|String
name|url
init|=
literal|"vm://test-broker-"
operator|+
name|id
operator|+
literal|"?broker.persistent=true&broker.useJmx=false"
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

