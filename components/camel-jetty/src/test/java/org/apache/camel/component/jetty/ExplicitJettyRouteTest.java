begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|Processor
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
name|Connector
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
name|nio
operator|.
name|SelectChannelConnector
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

begin_comment
comment|/**  * Unit test for wiki demonstration.  */
end_comment

begin_class
DECL|class|ExplicitJettyRouteTest
specifier|public
class|class
name|ExplicitJettyRouteTest
extends|extends
name|BaseJettyTest
block|{
annotation|@
name|Test
DECL|method|testSendToJetty ()
specifier|public
name|void
name|testSendToJetty
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:{{port}}/myapp/myservice"
argument_list|,
literal|"bookid=123"
argument_list|)
decl_stmt|;
comment|// convert the response to a String
name|String
name|body
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
name|response
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<html><body>Book 123 is Camel in Action</body></html>"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|createSocketConnector ()
specifier|private
name|Connector
name|createSocketConnector
parameter_list|()
block|{
name|SelectChannelConnector
name|answer
init|=
operator|new
name|SelectChannelConnector
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setAcceptors
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setStatsOn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setSoLingerTime
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setPort
argument_list|(
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
comment|// create socket connectors for port 9080
name|Map
argument_list|<
name|Integer
argument_list|,
name|Connector
argument_list|>
name|connectors
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Connector
argument_list|>
argument_list|()
decl_stmt|;
name|connectors
operator|.
name|put
argument_list|(
name|getPort
argument_list|()
argument_list|,
name|createSocketConnector
argument_list|()
argument_list|)
expr_stmt|;
comment|// create jetty component
name|JettyHttpComponent
name|jetty
init|=
operator|new
name|JettyHttpComponent
argument_list|()
decl_stmt|;
comment|// add connectors
name|jetty
operator|.
name|setSocketConnectors
argument_list|(
name|connectors
argument_list|)
expr_stmt|;
comment|// add jetty to camel context
name|context
operator|.
name|addComponent
argument_list|(
literal|"jetty"
argument_list|,
name|jetty
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
name|from
argument_list|(
literal|"jetty:http://localhost:{{port}}/myapp/myservice"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyBookService
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyBookService
specifier|public
class|class
name|MyBookService
implements|implements
name|Processor
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// just get the body as a string
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// we have access to the HttpServletRequest here and we can grab it if we need it
name|HttpServletRequest
name|req
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|req
argument_list|)
expr_stmt|;
comment|// for unit testing
name|assertEquals
argument_list|(
literal|"bookid=123"
argument_list|,
name|body
argument_list|)
expr_stmt|;
comment|// send a html response
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<html><body>Book 123 is Camel in Action</body></html>"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

