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
name|javax
operator|.
name|management
operator|.
name|MBeanServer
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
name|w3c
operator|.
name|dom
operator|.
name|Document
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
comment|/**  * @version  */
end_comment

begin_class
DECL|class|ManagedRouteDumpStatsAsXmlCustomDomainTest
specifier|public
class|class
name|ManagedRouteDumpStatsAsXmlCustomDomainTest
extends|extends
name|ManagementTestSupport
block|{
DECL|field|CUSTOM_DOMAIN_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CUSTOM_DOMAIN_NAME
init|=
literal|"custom"
decl_stmt|;
DECL|method|testPerformanceCounterStats ()
specifier|public
name|void
name|testPerformanceCounterStats
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
comment|// get the stats for the route
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
name|CUSTOM_DOMAIN_NAME
operator|+
literal|":context=camel-1,type=routes,name=\"foo\""
argument_list|)
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|String
name|xml
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"dumpRouteStatsAsXml"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|false
block|,
literal|true
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"boolean"
block|,
literal|"boolean"
block|}
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
name|xml
argument_list|)
expr_stmt|;
comment|// should be valid XML
name|Document
name|doc
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|xml
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|int
name|processors
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getElementsByTagName
argument_list|(
literal|"processorStat"
argument_list|)
operator|.
name|getLength
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|processors
argument_list|)
expr_stmt|;
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
comment|//              System.setProperty("org.apache.camel.jmx.mbeanObjectDomainName", CUSTOM_DOMAIN_NAME);
comment|//              Or
name|getContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|setMBeanObjectDomainName
argument_list|(
name|CUSTOM_DOMAIN_NAME
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|id
argument_list|(
literal|"to-log"
argument_list|)
operator|.
name|delay
argument_list|(
literal|100
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|id
argument_list|(
literal|"to-mock"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

