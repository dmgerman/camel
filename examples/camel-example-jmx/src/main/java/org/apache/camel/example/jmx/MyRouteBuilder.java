begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|jmx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

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
comment|/**  * A simple example router demonstrating the camel-jmx component.  */
end_comment

begin_class
DECL|class|MyRouteBuilder
specifier|public
class|class
name|MyRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|bean
specifier|private
name|SimpleBean
name|bean
decl_stmt|;
DECL|field|server
specifier|private
name|MBeanServer
name|server
decl_stmt|;
DECL|method|MyRouteBuilder ()
specifier|public
name|MyRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
expr_stmt|;
name|bean
operator|=
operator|new
name|SimpleBean
argument_list|()
expr_stmt|;
comment|// START SNIPPET: e2
name|server
operator|.
name|registerMBean
argument_list|(
name|bean
argument_list|,
operator|new
name|ObjectName
argument_list|(
literal|"jmxExample"
argument_list|,
literal|"name"
argument_list|,
literal|"simpleBean"
argument_list|)
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e2
block|}
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"jmx:platform?objectDomain=jmxExample&key.name=simpleBean"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:jmxEvent"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
name|from
argument_list|(
literal|"timer:foo?period=6000"
argument_list|)
operator|.
name|bean
argument_list|(
name|bean
argument_list|,
literal|"tick"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

