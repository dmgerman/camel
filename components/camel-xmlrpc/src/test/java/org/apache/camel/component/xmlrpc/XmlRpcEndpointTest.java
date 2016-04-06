begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmlrpc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmlrpc
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
name|CamelContext
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|SimpleRegistry
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
name|spi
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmlrpc
operator|.
name|client
operator|.
name|XmlRpcClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmlrpc
operator|.
name|client
operator|.
name|XmlRpcClientConfigImpl
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
DECL|class|XmlRpcEndpointTest
specifier|public
class|class
name|XmlRpcEndpointTest
extends|extends
name|Assert
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|createRegistry
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|createRegistry ()
specifier|protected
name|Registry
name|createRegistry
parameter_list|()
block|{
name|SimpleRegistry
name|answer
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
comment|// Binding the client configurer
name|answer
operator|.
name|put
argument_list|(
literal|"myClientConfigurer"
argument_list|,
operator|new
name|MyClientConfigurer
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|// create the endpoint with parameters
annotation|@
name|Test
DECL|method|testEndpointSetting ()
specifier|public
name|void
name|testEndpointSetting
parameter_list|()
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|XmlRpcEndpoint
name|endpoint
init|=
operator|(
name|XmlRpcEndpoint
operator|)
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"xmlrpc:http://www.example.com?userAgent=myAgent&gzipCompressing=true&connectionTimeout=30&defaultMethodName=echo"
argument_list|)
decl_stmt|;
name|XmlRpcClientConfigImpl
name|clientConfig
init|=
name|endpoint
operator|.
name|getClientConfig
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong userAgent"
argument_list|,
literal|"myAgent"
argument_list|,
name|clientConfig
operator|.
name|getUserAgent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong gzipCompressing"
argument_list|,
literal|true
argument_list|,
name|clientConfig
operator|.
name|isGzipCompressing
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong connectionTimeout"
argument_list|,
literal|30
argument_list|,
name|clientConfig
operator|.
name|getConnectionTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong endpoint address"
argument_list|,
literal|"http://www.example.com"
argument_list|,
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a worng default method name"
argument_list|,
literal|"echo"
argument_list|,
name|endpoint
operator|.
name|getDefaultMethodName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testClientConfigurer ()
specifier|public
name|void
name|testClientConfigurer
parameter_list|()
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|XmlRpcEndpoint
name|endpoint
init|=
operator|(
name|XmlRpcEndpoint
operator|)
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"xmlrpc:http://www.example.com?clientConfigurer=#myClientConfigurer"
argument_list|)
decl_stmt|;
name|XmlRpcClient
name|client
init|=
name|endpoint
operator|.
name|createClient
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a worng maxThreads"
argument_list|,
literal|10
argument_list|,
name|client
operator|.
name|getMaxThreads
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong value of enabledForExtensions"
argument_list|,
literal|true
argument_list|,
operator|(
operator|(
name|XmlRpcClientConfigImpl
operator|)
name|client
operator|.
name|getClientConfig
argument_list|()
operator|)
operator|.
name|isEnabledForExtensions
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

