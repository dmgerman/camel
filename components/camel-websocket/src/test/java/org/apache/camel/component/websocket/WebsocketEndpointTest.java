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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Producer
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
name|Matchers
operator|.
name|isNull
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
DECL|class|WebsocketEndpointTest
specifier|public
class|class
name|WebsocketEndpointTest
block|{
DECL|field|REMAINING
specifier|private
specifier|static
specifier|final
name|String
name|REMAINING
init|=
literal|"foo/bar"
decl_stmt|;
DECL|field|URI
specifier|private
specifier|static
specifier|final
name|String
name|URI
init|=
literal|"websocket://"
operator|+
name|REMAINING
decl_stmt|;
annotation|@
name|Mock
DECL|field|component
specifier|private
name|WebsocketComponent
name|component
decl_stmt|;
annotation|@
name|Mock
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|field|websocketEndpoint
specifier|private
name|WebsocketEndpoint
name|websocketEndpoint
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
name|websocketEndpoint
operator|=
operator|new
name|WebsocketEndpoint
argument_list|(
name|component
argument_list|,
name|URI
argument_list|,
name|REMAINING
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketEndpoint#createConsumer(org.apache.camel.Processor)} .      */
annotation|@
name|Test
DECL|method|testCreateConsumer ()
specifier|public
name|void
name|testCreateConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|Consumer
name|consumer
init|=
name|websocketEndpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WebsocketConsumer
operator|.
name|class
argument_list|,
name|consumer
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|InOrder
name|inOrder
init|=
name|inOrder
argument_list|(
name|component
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|NodeSynchronization
argument_list|>
name|synchronizationCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|NodeSynchronization
operator|.
name|class
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|WebsocketConsumer
argument_list|>
name|consumerCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|WebsocketConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|component
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|addServlet
argument_list|(
name|synchronizationCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|consumerCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|eq
argument_list|(
name|REMAINING
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
name|DefaultNodeSynchronization
operator|.
name|class
argument_list|,
name|synchronizationCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|consumer
argument_list|,
name|consumerCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketEndpoint#createProducer()} .      */
annotation|@
name|Test
DECL|method|testCreateProducer ()
specifier|public
name|void
name|testCreateProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Producer
name|producer
init|=
name|websocketEndpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WebsocketProducer
operator|.
name|class
argument_list|,
name|producer
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|InOrder
name|inOrder
init|=
name|inOrder
argument_list|(
name|component
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|NodeSynchronization
argument_list|>
name|synchronizationCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|NodeSynchronization
operator|.
name|class
argument_list|)
decl_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|component
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|addServlet
argument_list|(
name|synchronizationCaptor
operator|.
name|capture
argument_list|()
argument_list|,
operator|(
name|WebsocketConsumer
operator|)
name|isNull
argument_list|()
argument_list|,
name|eq
argument_list|(
name|REMAINING
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
name|DefaultNodeSynchronization
operator|.
name|class
argument_list|,
name|synchronizationCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test method for {@link org.apache.camel.component.websocket.WebsocketEndpoint#isSingleton()} .      */
annotation|@
name|Test
DECL|method|testIsSingleton ()
specifier|public
name|void
name|testIsSingleton
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|websocketEndpoint
operator|.
name|isSingleton
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

