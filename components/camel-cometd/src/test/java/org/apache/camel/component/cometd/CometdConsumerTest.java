begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cometd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cometd
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|component
operator|.
name|cometd
operator|.
name|CometdConsumer
operator|.
name|ConsumerService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|MarkedReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|server
operator|.
name|LocalSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|server
operator|.
name|ServerChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|server
operator|.
name|ServerSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|server
operator|.
name|BayeuxServerImpl
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
name|util
operator|.
name|log
operator|.
name|Logger
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
name|ArgumentMatchers
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
name|junit
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
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|CometdConsumerTest
specifier|public
class|class
name|CometdConsumerTest
block|{
DECL|field|USER_NAME
specifier|private
specifier|static
specifier|final
name|String
name|USER_NAME
init|=
literal|"userName"
decl_stmt|;
DECL|field|testObj
specifier|private
name|CometdConsumer
name|testObj
decl_stmt|;
annotation|@
name|Mock
DECL|field|endpoint
specifier|private
name|CometdEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Mock
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
annotation|@
name|Mock
DECL|field|bayeuxServerImpl
specifier|private
name|BayeuxServerImpl
name|bayeuxServerImpl
decl_stmt|;
annotation|@
name|Mock
DECL|field|localSession
specifier|private
name|LocalSession
name|localSession
decl_stmt|;
annotation|@
name|Mock
DECL|field|logger
specifier|private
name|Logger
name|logger
decl_stmt|;
annotation|@
name|Mock
DECL|field|serverChannel
specifier|private
name|ServerChannel
name|serverChannel
decl_stmt|;
annotation|@
name|Mock
DECL|field|remote
specifier|private
name|ServerSession
name|remote
decl_stmt|;
annotation|@
name|Mock
DECL|field|markedReferenceServerChannel
specifier|private
name|MarkedReference
argument_list|<
name|ServerChannel
argument_list|>
name|markedReferenceServerChannel
decl_stmt|;
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
block|{
name|when
argument_list|(
name|bayeuxServerImpl
operator|.
name|newLocalSession
argument_list|(
name|ArgumentMatchers
operator|.
name|isNull
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|localSession
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|bayeuxServerImpl
operator|.
name|createChannelIfAbsent
argument_list|(
name|ArgumentMatchers
operator|.
name|isNull
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|markedReferenceServerChannel
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|markedReferenceServerChannel
operator|.
name|getReference
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|serverChannel
argument_list|)
expr_stmt|;
name|testObj
operator|=
operator|new
name|CometdConsumer
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|testObj
operator|.
name|setBayeux
argument_list|(
name|bayeuxServerImpl
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|attributeNames
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|attributeKey
init|=
name|USER_NAME
decl_stmt|;
name|attributeNames
operator|.
name|add
argument_list|(
name|attributeKey
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStartDoesntCreateMultipleServices ()
specifier|public
name|void
name|testStartDoesntCreateMultipleServices
parameter_list|()
throws|throws
name|Exception
block|{
comment|// setup
name|testObj
operator|.
name|start
argument_list|()
expr_stmt|;
name|ConsumerService
name|expectedService
init|=
name|testObj
operator|.
name|getConsumerService
argument_list|()
decl_stmt|;
name|testObj
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// act
name|ConsumerService
name|result
init|=
name|testObj
operator|.
name|getConsumerService
argument_list|()
decl_stmt|;
comment|// assert
name|assertEquals
argument_list|(
name|expectedService
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

