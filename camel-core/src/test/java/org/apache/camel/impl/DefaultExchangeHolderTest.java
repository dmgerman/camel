begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|List
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DefaultExchangeHolderTest
specifier|public
class|class
name|DefaultExchangeHolderTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|method|testMarshal ()
specifier|public
name|void
name|testMarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultExchangeHolder
name|holder
init|=
name|createHolder
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|holder
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|holder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNoProperties ()
specifier|public
name|void
name|testNoProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultExchangeHolder
name|holder
init|=
name|createHolder
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|holder
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|DefaultExchangeHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
name|holder
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnmarshal ()
specifier|public
name|void
name|testUnmarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|id
operator|=
literal|null
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|DefaultExchangeHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
name|createHolder
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hi Camel"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelFoo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|444
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|555
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"CamelBar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSkipNonSerializableData ()
specifier|public
name|void
name|testSkipNonSerializableData
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Foo"
argument_list|,
operator|new
name|MyFoo
argument_list|(
literal|"Tiger"
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Bar"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|DefaultExchangeHolder
name|holder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|DefaultExchangeHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
name|holder
argument_list|)
expr_stmt|;
comment|// the non serializable header should be skipped
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testSkipNonSerializableDataFromList ()
specifier|public
name|void
name|testSkipNonSerializableDataFromList
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use a mixed list, the MyFoo is not serializable so the entire list should be skipped
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"I am okay"
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|MyFoo
argument_list|(
literal|"Tiger"
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Foo"
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Bar"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|DefaultExchangeHolder
name|holder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|DefaultExchangeHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
name|holder
argument_list|)
expr_stmt|;
comment|// the non serializable header should be skipped
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testSkipNonSerializableDataFromMap ()
specifier|public
name|void
name|testSkipNonSerializableDataFromMap
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use a mixed Map, the MyFoo is not serializable so the entire map should be skipped
name|Map
name|map
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
literal|"I am okay"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"B"
argument_list|,
operator|new
name|MyFoo
argument_list|(
literal|"Tiger"
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Foo"
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Bar"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|DefaultExchangeHolder
name|holder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|DefaultExchangeHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
name|holder
argument_list|)
expr_stmt|;
comment|// the non serializable header should be skipped
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createHolder (boolean includeProperties)
specifier|private
name|DefaultExchangeHolder
name|createHolder
parameter_list|(
name|boolean
name|includeProperties
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|id
operator|=
name|exchange
operator|.
name|getExchangeId
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelFoo"
argument_list|,
literal|"Hi Camel"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"bar"
argument_list|,
literal|444
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"CamelBar"
argument_list|,
literal|555
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
return|return
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|,
name|includeProperties
argument_list|)
return|;
block|}
DECL|class|MyFoo
specifier|private
specifier|final
class|class
name|MyFoo
block|{
DECL|field|foo
specifier|private
name|String
name|foo
decl_stmt|;
DECL|method|MyFoo (String foo)
specifier|private
name|MyFoo
parameter_list|(
name|String
name|foo
parameter_list|)
block|{
name|this
operator|.
name|foo
operator|=
name|foo
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|getFoo ()
specifier|public
name|String
name|getFoo
parameter_list|()
block|{
return|return
name|foo
return|;
block|}
block|}
block|}
end_class

end_unit

