begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|Producer
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ManagedUnregisterProducerTest
specifier|public
class|class
name|ManagedUnregisterProducerTest
extends|extends
name|ManagementTestSupport
block|{
DECL|method|testUnregisterProducer ()
specifier|public
name|void
name|testUnregisterProducer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// send a message so the managed producer is started
comment|// do this "manually" to avoid camel managing the direct:start producer as well
comment|// this makes the unit test easier as we only have 1 managed producer = mock:result
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:start"
argument_list|)
decl_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
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
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// TODO: producers are not managed due they can lead to memory leak CAMEL-2484
comment|//        MBeanServer mbeanServer = getMBeanServer();
comment|//        Set<ObjectName> set = mbeanServer.queryNames(new ObjectName("*:type=producers,*"), null);
comment|//        assertEquals(0, set.size());
comment|//        ObjectName on = set.iterator().next();
comment|//        assertTrue("Should be registered", mbeanServer.isRegistered(on));
comment|//        String uri = (String) mbeanServer.getAttribute(on, "EndpointUri");
comment|//        assertEquals("mock://result", uri);
comment|// TODO: populating route id on producers is not implemented yet
comment|//        String routeId = (String) mbeanServer.getAttribute(on, "RouteId");
comment|//        assertEquals("route1", routeId);
comment|//        Boolean singleton = (Boolean) mbeanServer.getAttribute(on, "Singleton");
comment|//        assertEquals(Boolean.TRUE, singleton);
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|//        assertFalse("Should no longer be registered", mbeanServer.isRegistered(on));
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

