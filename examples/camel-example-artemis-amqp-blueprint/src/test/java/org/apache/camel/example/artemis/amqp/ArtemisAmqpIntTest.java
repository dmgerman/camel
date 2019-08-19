begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.artemis.amqp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|artemis
operator|.
name|amqp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
operator|.
name|CamelBlueprintTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|ArtemisAmqpIntTest
specifier|public
class|class
name|ArtemisAmqpIntTest
extends|extends
name|CamelBlueprintTestSupport
block|{
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"/OSGI-INF/blueprint/camel-context.xml,"
comment|//We add the embedded Artemis Broker
operator|+
literal|"/OSGI-INF/blueprint/embedded-broker.xml"
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|useOverridePropertiesWithConfigAdmin (Dictionary props)
specifier|protected
name|String
name|useOverridePropertiesWithConfigAdmin
parameter_list|(
name|Dictionary
name|props
parameter_list|)
block|{
comment|//obtain an available port
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
if|if
condition|(
name|port
operator|!=
literal|9090
condition|)
block|{
comment|//override the Netty port to use
name|props
operator|.
name|put
argument_list|(
literal|"netty.port"
argument_list|,
literal|""
operator|+
name|port
argument_list|)
expr_stmt|;
comment|//return the PID of the config-admin we are using in the blueprint xml file
return|return
literal|"my-placeholders"
return|;
block|}
else|else
block|{
comment|// no update needed
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Test
DECL|method|testEmbeddedBroker ()
specifier|public
name|void
name|testEmbeddedBroker
parameter_list|()
throws|throws
name|Exception
block|{
comment|//trigger
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty-http:http://localhost:{{netty.port}}/message"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//response validation
name|assertEquals
argument_list|(
literal|"not expected"
argument_list|,
literal|"Hello from Camel's AMQP example"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

