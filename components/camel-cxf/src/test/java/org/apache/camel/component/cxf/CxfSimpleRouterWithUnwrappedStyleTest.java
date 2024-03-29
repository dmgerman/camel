begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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
name|cxf
operator|.
name|frontend
operator|.
name|ClientFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientProxyFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ServerFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
annotation|@
name|Ignore
argument_list|(
literal|"As the refelection can't tell the paramenter name from SEI without annonation, "
operator|+
literal|"CXF cannot send a meaningful request for unwrapped message."
operator|+
literal|" We need to use the annontated SEI for testing"
argument_list|)
DECL|class|CxfSimpleRouterWithUnwrappedStyleTest
specifier|public
class|class
name|CxfSimpleRouterWithUnwrappedStyleTest
extends|extends
name|CxfSimpleRouterTest
block|{
DECL|field|routerEndpointURI
specifier|private
name|String
name|routerEndpointURI
init|=
literal|"cxf://"
operator|+
name|getRouterAddress
argument_list|()
operator|+
literal|"?"
operator|+
name|SERVICE_CLASS
operator|+
literal|"&wrappedStyle=false"
decl_stmt|;
DECL|field|serviceEndpointURI
specifier|private
name|String
name|serviceEndpointURI
init|=
literal|"cxf://"
operator|+
name|getServiceAddress
argument_list|()
operator|+
literal|"?"
operator|+
name|SERVICE_CLASS
operator|+
literal|"&wrappedStyle=false"
decl_stmt|;
annotation|@
name|Override
DECL|method|configureFactory (ServerFactoryBean svrBean)
specifier|protected
name|void
name|configureFactory
parameter_list|(
name|ServerFactoryBean
name|svrBean
parameter_list|)
block|{
name|svrBean
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setWrapped
argument_list|(
literal|false
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
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|routerEndpointURI
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:org.apache.camel?level=DEBUG"
argument_list|)
operator|.
name|to
argument_list|(
name|serviceEndpointURI
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|getCXFClient ()
specifier|protected
name|HelloService
name|getCXFClient
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientProxyFactoryBean
name|proxyFactory
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|ClientFactoryBean
name|clientBean
init|=
name|proxyFactory
operator|.
name|getClientFactoryBean
argument_list|()
decl_stmt|;
name|clientBean
operator|.
name|setAddress
argument_list|(
name|getRouterAddress
argument_list|()
argument_list|)
expr_stmt|;
name|clientBean
operator|.
name|setServiceClass
argument_list|(
name|HelloService
operator|.
name|class
argument_list|)
expr_stmt|;
name|clientBean
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setWrapped
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|HelloService
name|client
init|=
operator|(
name|HelloService
operator|)
name|proxyFactory
operator|.
name|create
argument_list|()
decl_stmt|;
return|return
name|client
return|;
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testOnwayInvocation ()
specifier|public
name|void
name|testOnwayInvocation
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ignore the invocation without parameter, as the document-literal doesn't support the invocation without parameter.
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testInvokingServiceFromCXFClient ()
specifier|public
name|void
name|testInvokingServiceFromCXFClient
parameter_list|()
throws|throws
name|Exception
block|{
name|HelloService
name|client
init|=
name|getCXFClient
argument_list|()
decl_stmt|;
name|Boolean
name|result
init|=
name|client
operator|.
name|echoBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"we should get the right answer from router"
argument_list|,
literal|true
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// The below invocation is failed with CXF 2.6.1 as the request are all start with<arg0>
name|String
name|str
init|=
name|client
operator|.
name|echo
argument_list|(
literal|"hello world"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"we should get the right answer from router"
argument_list|,
literal|"echo hello world"
argument_list|,
name|str
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

