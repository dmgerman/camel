begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|websocket
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
name|Endpoint
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
name|eclipse
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|ServletContextHandler
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
name|servlet
operator|.
name|ServletHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|ArgumentCaptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|InOrder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|runners
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|inOrder
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|times
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|WebsocketComponentTest
specifier|public
class|class
name|WebsocketComponentTest
block|{
DECL|field|PATH_ONE
specifier|private
specifier|static
specifier|final
name|String
name|PATH_ONE
init|=
literal|"foo"
decl_stmt|;
DECL|field|PATH_TWO
specifier|private
specifier|static
specifier|final
name|String
name|PATH_TWO
init|=
literal|"bar"
decl_stmt|;
DECL|field|PATH_SPEC_ONE
specifier|private
specifier|static
specifier|final
name|String
name|PATH_SPEC_ONE
init|=
literal|"/"
operator|+
name|PATH_ONE
operator|+
literal|"/*"
decl_stmt|;
DECL|field|PATH_SPEC_TWO
specifier|private
specifier|static
specifier|final
name|String
name|PATH_SPEC_TWO
init|=
literal|"/"
operator|+
name|PATH_TWO
operator|+
literal|"/*"
decl_stmt|;
annotation|@
name|Mock
DECL|field|consumer
specifier|private
name|WebsocketConsumer
name|consumer
decl_stmt|;
annotation|@
name|Mock
DECL|field|sync
specifier|private
name|NodeSynchronization
name|sync
decl_stmt|;
annotation|@
name|Mock
DECL|field|servlet
specifier|private
name|WebsocketComponentServlet
name|servlet
decl_stmt|;
annotation|@
name|Mock
DECL|field|servlets
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|WebsocketComponentServlet
argument_list|>
name|servlets
decl_stmt|;
annotation|@
name|Mock
DECL|field|handler
specifier|private
name|ServletContextHandler
name|handler
decl_stmt|;
annotation|@
name|Mock
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|component
specifier|private
name|WebsocketComponent
name|component
decl_stmt|;
comment|/**      * @throws Exception      */
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|=
operator|new
name|WebsocketComponent
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#createContext()} .      */
annotation|@
name|Test
DECL|method|testCreateContext ()
specifier|public
name|void
name|testCreateContext
parameter_list|()
block|{
name|ServletContextHandler
name|handler
init|=
name|component
operator|.
name|createContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#createServer(org.eclipse.jetty.servlet.ServletContextHandler, String, int, String)} .      */
annotation|@
name|Test
DECL|method|testCreateServerWithoutStaticContent ()
specifier|public
name|void
name|testCreateServerWithoutStaticContent
parameter_list|()
block|{
name|ServletContextHandler
name|handler
init|=
name|component
operator|.
name|createContext
argument_list|()
decl_stmt|;
name|Server
name|server
init|=
name|component
operator|.
name|createServer
argument_list|(
name|handler
argument_list|,
literal|"localhost"
argument_list|,
literal|1988
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|server
operator|.
name|getConnectors
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|server
operator|.
name|getConnectors
argument_list|()
index|[
literal|0
index|]
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1988
argument_list|,
name|server
operator|.
name|getConnectors
argument_list|()
index|[
literal|0
index|]
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|server
operator|.
name|getConnectors
argument_list|()
index|[
literal|0
index|]
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|handler
argument_list|,
name|server
operator|.
name|getHandler
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|server
operator|.
name|getHandlers
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|handler
argument_list|,
name|server
operator|.
name|getHandlers
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|handler
operator|.
name|getContextPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|handler
operator|.
name|getSessionHandler
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|handler
operator|.
name|getResourceBase
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|handler
operator|.
name|getServletHandler
argument_list|()
operator|.
name|getHolderEntry
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#createServer(org.eclipse.jetty.servlet.ServletContextHandler, String, int, String)} .      */
annotation|@
name|Test
DECL|method|testCreateServerWithStaticContent ()
specifier|public
name|void
name|testCreateServerWithStaticContent
parameter_list|()
block|{
name|ServletContextHandler
name|handler
init|=
name|component
operator|.
name|createContext
argument_list|()
decl_stmt|;
name|Server
name|server
init|=
name|component
operator|.
name|createServer
argument_list|(
name|handler
argument_list|,
literal|"localhost"
argument_list|,
literal|1988
argument_list|,
literal|"public/"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|server
operator|.
name|getConnectors
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|server
operator|.
name|getConnectors
argument_list|()
index|[
literal|0
index|]
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1988
argument_list|,
name|server
operator|.
name|getConnectors
argument_list|()
index|[
literal|0
index|]
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|server
operator|.
name|getConnectors
argument_list|()
index|[
literal|0
index|]
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|handler
argument_list|,
name|server
operator|.
name|getHandler
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|server
operator|.
name|getHandlers
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|handler
argument_list|,
name|server
operator|.
name|getHandlers
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|handler
operator|.
name|getContextPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|handler
operator|.
name|getSessionHandler
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|handler
operator|.
name|getResourceBase
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|handler
operator|.
name|getResourceBase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"public"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|handler
operator|.
name|getServletHandler
argument_list|()
operator|.
name|getHolderEntry
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#createEndpoint(String, String, java.util.Map)} .      */
annotation|@
name|Test
DECL|method|testCreateEndpoint ()
specifier|public
name|void
name|testCreateEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|Endpoint
name|e1
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"websocket://foo"
argument_list|,
literal|"foo"
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|Endpoint
name|e2
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"websocket://foo"
argument_list|,
literal|"foo"
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|Endpoint
name|e3
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"websocket://bar"
argument_list|,
literal|"bar"
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|e1
argument_list|,
name|e3
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|e2
argument_list|,
name|e3
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#setServletConsumer(WebsocketComponentServlet, WebsocketConsumer)} .      */
annotation|@
name|Test
DECL|method|testSetServletConsumer ()
specifier|public
name|void
name|testSetServletConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|servlet
operator|.
name|getConsumer
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|InOrder
name|inOrder
init|=
name|inOrder
argument_list|(
name|servlet
argument_list|,
name|consumer
argument_list|,
name|sync
argument_list|)
decl_stmt|;
name|component
operator|.
name|setServletConsumer
argument_list|(
name|servlet
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// null&& null
name|inOrder
operator|.
name|verify
argument_list|(
name|servlet
argument_list|,
name|times
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|setConsumer
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|component
operator|.
name|setServletConsumer
argument_list|(
name|servlet
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
comment|// null&& not null
name|inOrder
operator|.
name|verify
argument_list|(
name|servlet
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|setConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|component
operator|.
name|setServletConsumer
argument_list|(
name|servlet
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// not null&& null
name|inOrder
operator|.
name|verify
argument_list|(
name|servlet
argument_list|,
name|times
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|setConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|component
operator|.
name|setServletConsumer
argument_list|(
name|servlet
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
comment|// not null&& not null
name|inOrder
operator|.
name|verify
argument_list|(
name|servlet
argument_list|,
name|times
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|setConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#createServlet(WebsocketStore, String, java.util.Map, ServletContextHandler)} .      */
annotation|@
name|Test
DECL|method|testCreateServlet ()
specifier|public
name|void
name|testCreateServlet
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|createServlet
argument_list|(
name|sync
argument_list|,
name|PATH_SPEC_ONE
argument_list|,
name|servlets
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|InOrder
name|inOrder
init|=
name|inOrder
argument_list|(
name|servlet
argument_list|,
name|consumer
argument_list|,
name|sync
argument_list|,
name|servlets
argument_list|,
name|handler
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|WebsocketComponentServlet
argument_list|>
name|servletCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|WebsocketComponentServlet
operator|.
name|class
argument_list|)
decl_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|servlets
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|eq
argument_list|(
name|PATH_SPEC_ONE
argument_list|)
argument_list|,
name|servletCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|ServletHolder
argument_list|>
name|holderCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|ServletHolder
operator|.
name|class
argument_list|)
decl_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|handler
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|addServlet
argument_list|(
name|holderCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|eq
argument_list|(
name|PATH_SPEC_ONE
argument_list|)
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|servletCaptor
operator|.
name|getValue
argument_list|()
argument_list|,
name|holderCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getServlet
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#createPathSpec(String)} .      */
annotation|@
name|Test
DECL|method|testCreatePathSpec ()
specifier|public
name|void
name|testCreatePathSpec
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|PATH_SPEC_ONE
argument_list|,
name|component
operator|.
name|createPathSpec
argument_list|(
name|PATH_ONE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PATH_SPEC_TWO
argument_list|,
name|component
operator|.
name|createPathSpec
argument_list|(
name|PATH_TWO
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#addServlet(WebsocketStore, WebsocketConsumer, String)} .      */
annotation|@
name|Test
DECL|method|testAddServletProducersOnly ()
specifier|public
name|void
name|testAddServletProducersOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|component
operator|.
name|setPort
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|component
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|WebsocketComponentServlet
name|s1
init|=
name|component
operator|.
name|addServlet
argument_list|(
name|sync
argument_list|,
literal|null
argument_list|,
name|PATH_ONE
argument_list|)
decl_stmt|;
name|WebsocketComponentServlet
name|s2
init|=
name|component
operator|.
name|addServlet
argument_list|(
name|sync
argument_list|,
literal|null
argument_list|,
name|PATH_TWO
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|s1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|s2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|s1
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|s2
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|component
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#addServlet(WebsocketStore, WebsocketConsumer, String)} .      */
annotation|@
name|Test
DECL|method|testAddServletConsumersOnly ()
specifier|public
name|void
name|testAddServletConsumersOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|component
operator|.
name|setPort
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|component
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|WebsocketComponentServlet
name|s1
init|=
name|component
operator|.
name|addServlet
argument_list|(
name|sync
argument_list|,
name|consumer
argument_list|,
name|PATH_ONE
argument_list|)
decl_stmt|;
name|WebsocketComponentServlet
name|s2
init|=
name|component
operator|.
name|addServlet
argument_list|(
name|sync
argument_list|,
name|consumer
argument_list|,
name|PATH_TWO
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|s1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|s2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|consumer
argument_list|,
name|s1
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|consumer
argument_list|,
name|s2
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|component
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#addServlet(WebsocketStore, WebsocketConsumer, String)} .      */
annotation|@
name|Test
DECL|method|testAddServletProducerAndConsumer ()
specifier|public
name|void
name|testAddServletProducerAndConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|component
operator|.
name|setPort
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|component
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|WebsocketComponentServlet
name|s1
init|=
name|component
operator|.
name|addServlet
argument_list|(
name|sync
argument_list|,
literal|null
argument_list|,
name|PATH_ONE
argument_list|)
decl_stmt|;
name|WebsocketComponentServlet
name|s2
init|=
name|component
operator|.
name|addServlet
argument_list|(
name|sync
argument_list|,
name|consumer
argument_list|,
name|PATH_ONE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|s1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|s2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|consumer
argument_list|,
name|s1
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|component
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketComponent#addServlet(WebsocketStore, WebsocketConsumer, String)} .      */
annotation|@
name|Test
DECL|method|testAddServletConsumerAndProducer ()
specifier|public
name|void
name|testAddServletConsumerAndProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|component
operator|.
name|setPort
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|component
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|WebsocketComponentServlet
name|s1
init|=
name|component
operator|.
name|addServlet
argument_list|(
name|sync
argument_list|,
name|consumer
argument_list|,
name|PATH_ONE
argument_list|)
decl_stmt|;
name|WebsocketComponentServlet
name|s2
init|=
name|component
operator|.
name|addServlet
argument_list|(
name|sync
argument_list|,
literal|null
argument_list|,
name|PATH_ONE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|s1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|s2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|consumer
argument_list|,
name|s1
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|component
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

