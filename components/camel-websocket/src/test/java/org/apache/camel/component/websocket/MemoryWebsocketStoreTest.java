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
name|Collection
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
name|junit
operator|.
name|Assert
operator|.
name|fail
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
DECL|class|MemoryWebsocketStoreTest
specifier|public
class|class
name|MemoryWebsocketStoreTest
block|{
DECL|field|KEY_1
specifier|private
specifier|static
specifier|final
name|String
name|KEY_1
init|=
literal|"one"
decl_stmt|;
DECL|field|KEY_2
specifier|private
specifier|static
specifier|final
name|String
name|KEY_2
init|=
literal|"two"
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
DECL|field|websocket1
specifier|private
name|DefaultWebsocket
name|websocket1
init|=
operator|new
name|DefaultWebsocket
argument_list|(
name|sync
argument_list|,
literal|null
argument_list|,
name|consumer
argument_list|)
decl_stmt|;
annotation|@
name|Mock
DECL|field|websocket2
specifier|private
name|DefaultWebsocket
name|websocket2
init|=
operator|new
name|DefaultWebsocket
argument_list|(
name|sync
argument_list|,
literal|null
argument_list|,
name|consumer
argument_list|)
decl_stmt|;
DECL|field|store
specifier|private
name|MemoryWebsocketStore
name|store
decl_stmt|;
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
name|store
operator|=
operator|new
name|MemoryWebsocketStore
argument_list|()
expr_stmt|;
name|when
argument_list|(
name|websocket1
operator|.
name|getConnectionKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|KEY_1
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|websocket2
operator|.
name|getConnectionKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|KEY_2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAdd ()
specifier|public
name|void
name|testAdd
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|websocket1
operator|.
name|getConnectionKey
argument_list|()
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|websocket1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|websocket1
argument_list|,
name|store
operator|.
name|get
argument_list|(
name|KEY_1
argument_list|)
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|websocket2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|websocket2
argument_list|,
name|store
operator|.
name|get
argument_list|(
name|KEY_2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|NullPointerException
operator|.
name|class
argument_list|)
DECL|method|testAddNullValue ()
specifier|public
name|void
name|testAddNullValue
parameter_list|()
block|{
name|store
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveDefaultWebsocket ()
specifier|public
name|void
name|testRemoveDefaultWebsocket
parameter_list|()
block|{
comment|// first call of websocket1.getConnectionKey()
name|store
operator|.
name|add
argument_list|(
name|websocket1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|websocket1
argument_list|,
name|store
operator|.
name|get
argument_list|(
name|KEY_1
argument_list|)
argument_list|)
expr_stmt|;
comment|// second call of websocket1.getConnectionKey()
name|store
operator|.
name|remove
argument_list|(
name|websocket1
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|get
argument_list|(
name|KEY_1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveDefaultWebsocketKeyNotSet ()
specifier|public
name|void
name|testRemoveDefaultWebsocketKeyNotSet
parameter_list|()
block|{
comment|// first call of websocket1.getConnectionKey()
name|store
operator|.
name|add
argument_list|(
name|websocket1
argument_list|)
expr_stmt|;
comment|// overload getConnectionKey() after store.add() - otherwise npe
name|when
argument_list|(
name|websocket1
operator|.
name|getConnectionKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|websocket1
argument_list|,
name|store
operator|.
name|get
argument_list|(
name|KEY_1
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|store
operator|.
name|remove
argument_list|(
name|websocket1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Exception expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|NullPointerException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testRemoveNotExisting ()
specifier|public
name|void
name|testRemoveNotExisting
parameter_list|()
block|{
name|websocket1
operator|.
name|setConnectionKey
argument_list|(
name|KEY_1
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|websocket1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|websocket1
argument_list|,
name|store
operator|.
name|get
argument_list|(
name|KEY_1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|get
argument_list|(
name|KEY_2
argument_list|)
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|websocket2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|websocket1
argument_list|,
name|store
operator|.
name|get
argument_list|(
name|KEY_1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|get
argument_list|(
name|KEY_2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveString ()
specifier|public
name|void
name|testRemoveString
parameter_list|()
block|{
name|websocket1
operator|.
name|setConnectionKey
argument_list|(
name|KEY_1
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|websocket1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|websocket1
argument_list|,
name|store
operator|.
name|get
argument_list|(
name|KEY_1
argument_list|)
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|KEY_1
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|get
argument_list|(
name|KEY_1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveStringNotExisting ()
specifier|public
name|void
name|testRemoveStringNotExisting
parameter_list|()
block|{
name|websocket1
operator|.
name|setConnectionKey
argument_list|(
name|KEY_1
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|websocket1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|websocket1
argument_list|,
name|store
operator|.
name|get
argument_list|(
name|KEY_1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|get
argument_list|(
name|KEY_2
argument_list|)
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|KEY_2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|websocket1
argument_list|,
name|store
operator|.
name|get
argument_list|(
name|KEY_1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|get
argument_list|(
name|KEY_2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetString ()
specifier|public
name|void
name|testGetString
parameter_list|()
block|{
name|websocket1
operator|.
name|setConnectionKey
argument_list|(
name|KEY_1
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|websocket1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|websocket1
argument_list|,
name|store
operator|.
name|get
argument_list|(
name|KEY_1
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|get
argument_list|(
name|KEY_2
argument_list|)
argument_list|)
expr_stmt|;
name|websocket2
operator|.
name|setConnectionKey
argument_list|(
name|KEY_2
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|websocket2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|websocket1
argument_list|,
name|store
operator|.
name|get
argument_list|(
name|KEY_1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|websocket2
argument_list|,
name|store
operator|.
name|get
argument_list|(
name|KEY_2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetAll ()
specifier|public
name|void
name|testGetAll
parameter_list|()
block|{
name|Collection
argument_list|<
name|DefaultWebsocket
argument_list|>
name|sockets
init|=
name|store
operator|.
name|getAll
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|sockets
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|sockets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|websocket1
operator|.
name|setConnectionKey
argument_list|(
name|KEY_1
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|websocket1
argument_list|)
expr_stmt|;
name|sockets
operator|=
name|store
operator|.
name|getAll
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|sockets
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|sockets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sockets
operator|.
name|contains
argument_list|(
name|websocket1
argument_list|)
argument_list|)
expr_stmt|;
name|websocket2
operator|.
name|setConnectionKey
argument_list|(
name|KEY_2
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|websocket2
argument_list|)
expr_stmt|;
name|sockets
operator|=
name|store
operator|.
name|getAll
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|sockets
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|sockets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sockets
operator|.
name|contains
argument_list|(
name|websocket1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sockets
operator|.
name|contains
argument_list|(
name|websocket2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

