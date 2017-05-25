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
name|Arrays
import|;
end_import

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
name|impl
operator|.
name|DefaultCamelContext
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
name|ServerMessage
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
name|assertTrue
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
DECL|class|CometBindingTest
specifier|public
class|class
name|CometBindingTest
block|{
DECL|field|FOO
specifier|private
specifier|static
specifier|final
name|Object
name|FOO
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
DECL|field|THIRTY_FOUR
specifier|private
specifier|static
specifier|final
name|Long
name|THIRTY_FOUR
init|=
name|Long
operator|.
name|valueOf
argument_list|(
literal|34L
argument_list|)
decl_stmt|;
DECL|field|TWO_POINT_ONE
specifier|private
specifier|static
specifier|final
name|Double
name|TWO_POINT_ONE
init|=
name|Double
operator|.
name|valueOf
argument_list|(
literal|2.1
argument_list|)
decl_stmt|;
DECL|field|EIGHT
specifier|private
specifier|static
specifier|final
name|Integer
name|EIGHT
init|=
operator|new
name|Integer
argument_list|(
literal|8
argument_list|)
decl_stmt|;
DECL|field|HELLO
specifier|private
specifier|static
specifier|final
name|String
name|HELLO
init|=
literal|"hello"
decl_stmt|;
DECL|field|FOO_ATTR_NAME
specifier|private
specifier|static
specifier|final
name|String
name|FOO_ATTR_NAME
init|=
literal|"foo"
decl_stmt|;
DECL|field|LONG_ATTR_NAME
specifier|private
specifier|static
specifier|final
name|String
name|LONG_ATTR_NAME
init|=
literal|"long"
decl_stmt|;
DECL|field|DOUBLE_ATTR_NAME
specifier|private
specifier|static
specifier|final
name|String
name|DOUBLE_ATTR_NAME
init|=
literal|"double"
decl_stmt|;
DECL|field|INTEGER_ATTR_NAME
specifier|private
specifier|static
specifier|final
name|String
name|INTEGER_ATTR_NAME
init|=
literal|"integer"
decl_stmt|;
DECL|field|STRING_ATTR_NAME
specifier|private
specifier|static
specifier|final
name|String
name|STRING_ATTR_NAME
init|=
literal|"string"
decl_stmt|;
DECL|field|BOOLEAN_ATT_NAME
specifier|private
specifier|static
specifier|final
name|String
name|BOOLEAN_ATT_NAME
init|=
literal|"boolean"
decl_stmt|;
DECL|field|testObj
specifier|private
name|CometdBinding
name|testObj
decl_stmt|;
annotation|@
name|Mock
DECL|field|bayeux
specifier|private
name|BayeuxServerImpl
name|bayeux
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
DECL|field|cometdMessage
specifier|private
name|ServerMessage
name|cometdMessage
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
block|{
name|testObj
operator|=
operator|new
name|CometdBinding
argument_list|(
name|bayeux
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
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|STRING_ATTR_NAME
argument_list|,
name|INTEGER_ATTR_NAME
argument_list|,
name|LONG_ATTR_NAME
argument_list|,
name|DOUBLE_ATTR_NAME
argument_list|,
name|FOO_ATTR_NAME
argument_list|,
name|BOOLEAN_ATT_NAME
argument_list|)
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|remote
operator|.
name|getAttributeNames
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|attributeNames
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|remote
operator|.
name|getAttribute
argument_list|(
name|STRING_ATTR_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|HELLO
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|remote
operator|.
name|getAttribute
argument_list|(
name|INTEGER_ATTR_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|EIGHT
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|remote
operator|.
name|getAttribute
argument_list|(
name|LONG_ATTR_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|THIRTY_FOUR
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|remote
operator|.
name|getAttribute
argument_list|(
name|DOUBLE_ATTR_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|TWO_POINT_ONE
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|remote
operator|.
name|getAttribute
argument_list|(
name|FOO_ATTR_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|FOO
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|remote
operator|.
name|getAttribute
argument_list|(
name|BOOLEAN_ATT_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBindingTransfersSessionAttributtes ()
specifier|public
name|void
name|testBindingTransfersSessionAttributtes
parameter_list|()
block|{
comment|// setup
name|testObj
operator|=
operator|new
name|CometdBinding
argument_list|(
name|bayeux
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// act
name|Message
name|result
init|=
name|testObj
operator|.
name|createCamelMessage
argument_list|(
name|camelContext
argument_list|,
name|remote
argument_list|,
name|cometdMessage
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// assert
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|result
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HELLO
argument_list|,
name|result
operator|.
name|getHeader
argument_list|(
name|STRING_ATTR_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EIGHT
argument_list|,
name|result
operator|.
name|getHeader
argument_list|(
name|INTEGER_ATTR_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|THIRTY_FOUR
argument_list|,
name|result
operator|.
name|getHeader
argument_list|(
name|LONG_ATTR_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TWO_POINT_ONE
argument_list|,
name|result
operator|.
name|getHeader
argument_list|(
name|DOUBLE_ATTR_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
operator|.
name|getHeader
argument_list|(
name|FOO_ATTR_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
name|Boolean
operator|)
name|result
operator|.
name|getHeader
argument_list|(
name|BOOLEAN_ATT_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBindingHonorsFlagForSessionAttributtes ()
specifier|public
name|void
name|testBindingHonorsFlagForSessionAttributtes
parameter_list|()
block|{
comment|// act
name|Message
name|result
init|=
name|testObj
operator|.
name|createCamelMessage
argument_list|(
name|camelContext
argument_list|,
name|remote
argument_list|,
name|cometdMessage
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// assert
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
operator|.
name|getHeader
argument_list|(
name|STRING_ATTR_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
operator|.
name|getHeader
argument_list|(
name|INTEGER_ATTR_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
operator|.
name|getHeader
argument_list|(
name|LONG_ATTR_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
operator|.
name|getHeader
argument_list|(
name|FOO_ATTR_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
operator|.
name|getHeader
argument_list|(
name|DOUBLE_ATTR_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
operator|.
name|getHeader
argument_list|(
name|BOOLEAN_ATT_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubscriptionHeadersPassed ()
specifier|public
name|void
name|testSubscriptionHeadersPassed
parameter_list|()
block|{
comment|// setup
name|String
name|expectedSubscriptionInfo
init|=
literal|"subscriptionInfo"
decl_stmt|;
name|when
argument_list|(
name|cometdMessage
operator|.
name|get
argument_list|(
name|CometdBinding
operator|.
name|COMETD_SUBSCRIPTION_HEADER_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|expectedSubscriptionInfo
argument_list|)
expr_stmt|;
comment|// act
name|Message
name|result
init|=
name|testObj
operator|.
name|createCamelMessage
argument_list|(
name|camelContext
argument_list|,
name|remote
argument_list|,
name|cometdMessage
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// assert
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedSubscriptionInfo
argument_list|,
name|result
operator|.
name|getHeader
argument_list|(
name|CometdBinding
operator|.
name|COMETD_SUBSCRIPTION_HEADER_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

