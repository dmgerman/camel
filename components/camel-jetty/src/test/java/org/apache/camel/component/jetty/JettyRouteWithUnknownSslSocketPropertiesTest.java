begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Server
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
DECL|class|JettyRouteWithUnknownSslSocketPropertiesTest
specifier|public
class|class
name|JettyRouteWithUnknownSslSocketPropertiesTest
extends|extends
name|BaseJettyTest
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testUnknownProperty ()
specifier|public
name|void
name|testUnknownProperty
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|Server
operator|.
name|getVersion
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"8"
argument_list|)
condition|)
block|{
comment|// SocketConnector props do not work for jetty 9
return|return;
block|}
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// define socket connector properties
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"acceptors"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"statsOn"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"soLingerTime"
argument_list|,
literal|"5000"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"doesNotExist"
argument_list|,
literal|2000
argument_list|)
expr_stmt|;
name|JettyHttpComponent
name|jetty
init|=
name|getContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"jetty"
argument_list|,
name|JettyHttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|jetty
operator|.
name|setSslSocketConnectorProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:https://localhost:{{port}}/myapp/myservice"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IllegalArgumentException
name|iae
init|=
name|assertIsInstanceOf
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Actual message: "
operator|+
name|iae
operator|.
name|getMessage
argument_list|()
argument_list|,
name|iae
operator|.
name|getMessage
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"Unknown parameters=[{doesNotExist=2000}]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

