begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

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
name|ContextTestSupport
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
name|InvalidPayloadException
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
name|ProxyBuilder
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|BeanProxyNoBindingTest
specifier|public
class|class
name|BeanProxyNoBindingTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testBeanProxyStringReturnString ()
specifier|public
name|void
name|testBeanProxyStringReturnString
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e2
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
name|OrderService
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|,
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|service
operator|.
name|submitOrderStringReturnString
argument_list|(
literal|"<order type=\"book\">Camel in action</order>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<order id=\"123\">OK</order>"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e2
block|}
annotation|@
name|Test
DECL|method|testBeanProxyStringReturnDocument ()
specifier|public
name|void
name|testBeanProxyStringReturnDocument
parameter_list|()
throws|throws
name|Exception
block|{
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
name|OrderService
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|,
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
name|Document
name|reply
init|=
name|service
operator|.
name|submitOrderStringReturnDocument
argument_list|(
literal|"<order type=\"book\">Camel in action</order>"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|reply
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<order id=\"123\">OK</order>"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanProxyDocumentReturnString ()
specifier|public
name|void
name|testBeanProxyDocumentReturnString
parameter_list|()
throws|throws
name|Exception
block|{
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
name|OrderService
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|,
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
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
literal|"<order type=\"book\">Camel in action</order>"
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|service
operator|.
name|submitOrderDocumentReturnString
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<order id=\"123\">OK</order>"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanProxyDocumentReturnDocument ()
specifier|public
name|void
name|testBeanProxyDocumentReturnDocument
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e3
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
name|OrderService
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|,
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
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
literal|"<order type=\"book\">Camel in action</order>"
argument_list|)
decl_stmt|;
name|Document
name|reply
init|=
name|service
operator|.
name|submitOrderDocumentReturnDocument
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|reply
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<order id=\"123\">OK</order>"
argument_list|,
name|s
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e3
block|}
annotation|@
name|Test
DECL|method|testBeanProxyFailure ()
specifier|public
name|void
name|testBeanProxyFailure
parameter_list|()
throws|throws
name|Exception
block|{
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
name|OrderService
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|,
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|service
operator|.
name|submitOrderStringReturnString
argument_list|(
literal|"<order type=\"beer\">Carlsberg</order>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<order>FAIL</order>"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanProxyFailureNotXMLBody ()
specifier|public
name|void
name|testBeanProxyFailureNotXMLBody
parameter_list|()
throws|throws
name|Exception
block|{
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
name|OrderService
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|,
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|service
operator|.
name|submitOrderStringReturnString
argument_list|(
literal|"Hello World"
argument_list|)
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
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testBeanProxyVoidReturnType ()
specifier|public
name|void
name|testBeanProxyVoidReturnType
parameter_list|()
throws|throws
name|Exception
block|{
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
name|OrderService
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|,
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
name|service
operator|.
name|doNothing
argument_list|(
literal|"<order>ping</order>"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanProxyFailureInvalidReturnType ()
specifier|public
name|void
name|testBeanProxyFailureInvalidReturnType
parameter_list|()
throws|throws
name|Exception
block|{
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
name|OrderService
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|,
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|service
operator|.
name|invalidReturnType
argument_list|(
literal|"<order type=\"beer\">Carlsberg</order>"
argument_list|)
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
comment|// expected
name|InvalidPayloadException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|InvalidPayloadException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|cause
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testBeanProxyCallAnotherBean ()
specifier|public
name|void
name|testBeanProxyCallAnotherBean
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:bean"
argument_list|)
decl_stmt|;
name|OrderService
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|,
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|service
operator|.
name|submitOrderStringReturnString
argument_list|(
literal|"World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
comment|// START SNIPPET: e4
annotation|@
name|Test
DECL|method|testProxyBuilderProxyCallAnotherBean ()
specifier|public
name|void
name|testProxyBuilderProxyCallAnotherBean
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use ProxyBuilder to easily create the proxy
name|OrderService
name|service
init|=
operator|new
name|ProxyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|endpoint
argument_list|(
literal|"direct:bean"
argument_list|)
operator|.
name|binding
argument_list|(
literal|false
argument_list|)
operator|.
name|build
argument_list|(
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|service
operator|.
name|submitOrderStringReturnString
argument_list|(
literal|"World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
comment|// END SNIPPET: e4
annotation|@
name|Test
DECL|method|testBeanProxyCallAnotherBeanWithNoArgs ()
specifier|public
name|void
name|testBeanProxyCallAnotherBeanWithNoArgs
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:bean"
argument_list|)
decl_stmt|;
name|OrderService
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|,
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|service
operator|.
name|doAbsolutelyNothing
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hi nobody"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProxyBuilderProxyCallAnotherBeanWithNoArgs ()
specifier|public
name|void
name|testProxyBuilderProxyCallAnotherBeanWithNoArgs
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:bean"
argument_list|)
decl_stmt|;
name|OrderService
name|service
init|=
operator|new
name|ProxyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|endpoint
argument_list|(
name|endpoint
argument_list|)
operator|.
name|binding
argument_list|(
literal|false
argument_list|)
operator|.
name|build
argument_list|(
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|service
operator|.
name|doAbsolutelyNothing
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hi nobody"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanProxyVoidAsInOut ()
specifier|public
name|void
name|testBeanProxyVoidAsInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:delay"
argument_list|)
decl_stmt|;
comment|// will by default let all exchanges be InOut
name|OrderService
name|service
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|,
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:delay"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|service
operator|.
name|doNothing
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:delay"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProxyBuilderVoidAsInOut ()
specifier|public
name|void
name|testProxyBuilderVoidAsInOut
parameter_list|()
throws|throws
name|Exception
block|{
comment|// will by default let all exchanges be InOut
name|OrderService
name|service
init|=
operator|new
name|ProxyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|endpoint
argument_list|(
literal|"seda:delay"
argument_list|)
operator|.
name|binding
argument_list|(
literal|false
argument_list|)
operator|.
name|build
argument_list|(
name|OrderService
operator|.
name|class
argument_list|)
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:delay"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|service
operator|.
name|doNothing
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:delay"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|xpath
argument_list|(
literal|"/order/@type = 'book'"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:book"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:other"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:book"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"<order id=\"123\">OK</order>"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:other"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"<order>FAIL</order>"
argument_list|)
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
name|from
argument_list|(
literal|"direct:bean"
argument_list|)
operator|.
name|bean
argument_list|(
name|MyFooBean
operator|.
name|class
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:delay"
argument_list|)
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:delay"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyFooBean
specifier|public
specifier|static
class|class
name|MyFooBean
block|{
DECL|method|hello (String name)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
literal|"Hello "
operator|+
name|name
return|;
block|}
else|else
block|{
return|return
literal|"Hi nobody"
return|;
block|}
block|}
block|}
block|}
end_class

end_unit
