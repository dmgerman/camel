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
name|RouteBuilder
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
operator|.
name|xpath
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|class|BeanProxyTest
specifier|public
class|class
name|BeanProxyTest
extends|extends
name|ContextTestSupport
block|{
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
comment|// TODO: Does not pass on JDK6
DECL|method|disabledtestBeanProxyFailureNullBody ()
specifier|public
name|void
name|disabledtestBeanProxyFailureNullBody
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
literal|null
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
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

