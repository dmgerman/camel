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
name|Random
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

begin_comment
comment|/**  * A helper for unit testing with Apache ActiveMQ as embedded JMS broker.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelJmsTestHelper
specifier|public
specifier|final
class|class
name|CamelJmsTestHelper
block|{
DECL|field|ran
specifier|private
specifier|static
name|Random
name|ran
init|=
operator|new
name|Random
argument_list|()
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
comment|// using a unique broker name improves testing when running the entire test suite in the same JVM
name|int
name|id
init|=
name|ran
operator|.
name|nextInt
argument_list|(
literal|100000
argument_list|)
decl_stmt|;
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://broker-"
operator|+
name|id
operator|+
literal|"?broker.persistent=false&broker.useJmx=false"
argument_list|)
decl_stmt|;
return|return
name|connectionFactory
return|;
block|}
DECL|method|createPersistentConnectionFactory ()
specifier|public
specifier|static
name|ConnectionFactory
name|createPersistentConnectionFactory
parameter_list|()
block|{
comment|// using a unique broker name improves testing when running the entire test suite in the same JVM
name|int
name|id
init|=
name|ran
operator|.
name|nextInt
argument_list|(
literal|100000
argument_list|)
decl_stmt|;
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://broker-"
operator|+
name|id
operator|+
literal|"?broker.persistent=true&broker.useJmx=false"
argument_list|)
decl_stmt|;
return|return
name|connectionFactory
return|;
block|}
block|}
end_class

end_unit

