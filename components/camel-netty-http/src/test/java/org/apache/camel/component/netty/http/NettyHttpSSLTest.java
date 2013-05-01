begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|impl
operator|.
name|JndiRegistry
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
name|After
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
DECL|class|NettyHttpSSLTest
specifier|public
class|class
name|NettyHttpSSLTest
extends|extends
name|BaseNettyTest
block|{
DECL|field|NULL_VALUE_MARKER
specifier|private
specifier|static
specifier|final
name|String
name|NULL_VALUE_MARKER
init|=
name|CamelTestSupport
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
decl_stmt|;
DECL|field|originalValues
specifier|protected
name|Properties
name|originalValues
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ensure jsse clients can validate the self signed dummy localhost cert,
comment|// use the server keystore as the trust store for these tests
name|URL
name|trustStoreUrl
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"jsse/localhost.ks"
argument_list|)
decl_stmt|;
name|setSystemProp
argument_list|(
literal|"javax.net.ssl.trustStore"
argument_list|,
name|trustStoreUrl
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|restoreSystemProperties
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|setSystemProp (String key, String value)
specifier|protected
name|void
name|setSystemProp
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|String
name|originalValue
init|=
name|System
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|originalValues
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|originalValue
operator|!=
literal|null
condition|?
name|originalValue
else|:
name|NULL_VALUE_MARKER
argument_list|)
expr_stmt|;
block|}
DECL|method|restoreSystemProperties ()
specifier|protected
name|void
name|restoreSystemProperties
parameter_list|()
block|{
for|for
control|(
name|Object
name|key
range|:
name|originalValues
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|originalValues
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|NULL_VALUE_MARKER
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|setProperty
argument_list|(
operator|(
name|String
operator|)
name|key
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"password"
argument_list|,
literal|"changeit"
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"ksf"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/jsse/localhost.ks"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"tsf"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/jsse/localhost.ks"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
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
DECL|method|testSSLInOutWithNettyConsumer ()
specifier|public
name|void
name|testSSLInOutWithNettyConsumer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ibm jdks dont have sun security algorithms
if|if
condition|(
name|isJavaVendor
argument_list|(
literal|"ibm"
argument_list|)
condition|)
block|{
return|return;
block|}
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"netty-http:http://localhost:{{port}}?ssl=true&passphrase=#password&keyStoreFile=#ksf&trustStoreFile=#tsf"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"https://localhost:{{port}}/foo"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

