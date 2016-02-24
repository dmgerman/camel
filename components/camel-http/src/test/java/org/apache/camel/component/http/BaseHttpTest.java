begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|Message
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
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Handler
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
name|handler
operator|.
name|ContextHandler
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
name|handler
operator|.
name|ContextHandlerCollection
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
name|handler
operator|.
name|HandlerCollection
import|;
end_import

begin_comment
comment|/**  * Ported from {@link org.apache.camel.component.http4.BaseHttpTest}.  *  * @version   */
end_comment

begin_class
DECL|class|BaseHttpTest
specifier|public
specifier|abstract
class|class
name|BaseHttpTest
extends|extends
name|CamelTestSupport
block|{
DECL|method|assertExchange (Exchange exchange)
specifier|protected
name|void
name|assertExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|exchange
operator|.
name|hasOut
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|assertHeaders
argument_list|(
name|out
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertBody
argument_list|(
name|out
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertHeaders (Map<String, Object> headers)
specifier|protected
name|void
name|assertHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|HttpServletResponse
operator|.
name|SC_OK
argument_list|,
name|headers
operator|.
name|get
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"12"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"Content-Length"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have Content-Type header"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"Content-Type"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertBody (String body)
specifier|protected
name|void
name|assertBody
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|getExpectedContent
argument_list|()
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|getExpectedContent ()
specifier|protected
name|String
name|getExpectedContent
parameter_list|()
block|{
return|return
literal|"camel rocks!"
return|;
block|}
DECL|method|contextHandler (String context, Handler handler)
specifier|protected
name|ContextHandler
name|contextHandler
parameter_list|(
name|String
name|context
parameter_list|,
name|Handler
name|handler
parameter_list|)
block|{
name|ContextHandler
name|contextHandler
init|=
operator|new
name|ContextHandler
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|contextHandler
operator|.
name|setHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
return|return
name|contextHandler
return|;
block|}
DECL|method|handlers (Handler... handlers)
specifier|protected
name|HandlerCollection
name|handlers
parameter_list|(
name|Handler
modifier|...
name|handlers
parameter_list|)
block|{
name|HandlerCollection
name|collection
init|=
operator|new
name|ContextHandlerCollection
argument_list|()
decl_stmt|;
name|collection
operator|.
name|setHandlers
argument_list|(
name|handlers
argument_list|)
expr_stmt|;
return|return
name|collection
return|;
block|}
block|}
end_class

end_unit

