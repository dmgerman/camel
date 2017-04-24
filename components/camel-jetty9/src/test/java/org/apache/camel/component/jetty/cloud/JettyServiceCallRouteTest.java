begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
operator|.
name|cloud
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
name|ResolveEndpointFailedException
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
name|RoutesBuilder
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
name|RuntimeCamelException
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|JettyServiceCallRouteTest
specifier|public
class|class
name|JettyServiceCallRouteTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testCustomSchema ()
specifier|public
name|void
name|testCustomSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"8081"
argument_list|,
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:custom"
argument_list|,
literal|"hello"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"8082"
argument_list|,
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:custom"
argument_list|,
literal|"hello"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultSchema ()
specifier|public
name|void
name|testDefaultSchema
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"8081"
argument_list|,
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:default"
argument_list|,
literal|"hello"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ResolveEndpointFailedException
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
literal|"direct:custom"
argument_list|)
operator|.
name|serviceCall
argument_list|()
operator|.
name|name
argument_list|(
literal|"myService"
argument_list|)
operator|.
name|component
argument_list|(
literal|"jetty"
argument_list|)
operator|.
name|staticServiceDiscovery
argument_list|()
operator|.
name|servers
argument_list|(
literal|"myService@localhost:8081"
argument_list|)
operator|.
name|servers
argument_list|(
literal|"myService@localhost:8082"
argument_list|)
operator|.
name|endParent
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:default"
argument_list|)
operator|.
name|serviceCall
argument_list|()
operator|.
name|name
argument_list|(
literal|"myService"
argument_list|)
operator|.
name|staticServiceDiscovery
argument_list|()
operator|.
name|servers
argument_list|(
literal|"myService@localhost:8081"
argument_list|)
operator|.
name|servers
argument_list|(
literal|"myService@localhost:8082"
argument_list|)
operator|.
name|endParent
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:8081"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"8081"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:8082"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"8082"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

