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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_comment
comment|/**  * A unit test to verify mbean registration of multi-instances of a processor  *  * @version   */
end_comment

begin_class
DECL|class|MultiInstanceProcessorTest
specifier|public
class|class
name|MultiInstanceProcessorTest
extends|extends
name|JmxInstrumentationUsingDefaultsTest
block|{
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
comment|// simulate a little processing time
name|Thread
operator|.
name|sleep
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * It retrieves a mbean for each "to" processor instance in the query ":type=processor"      */
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testMBeansRegistered ()
specifier|public
name|void
name|testMBeansRegistered
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
name|assertDefaultDomain
argument_list|()
expr_stmt|;
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:end"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=endpoints,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 2 endpoints: "
operator|+
name|s
argument_list|,
literal|2
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=context,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 1 context: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=processors,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 3 processor: "
operator|+
name|s
argument_list|,
literal|3
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=routes,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 1 route: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testCounters ()
specifier|public
name|void
name|testCounters
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
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:end"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<hello>world!</hello>"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|verifyCounter
argument_list|(
name|mbsc
argument_list|,
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=routes,*"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

