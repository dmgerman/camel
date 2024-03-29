begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cbor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cbor
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
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonProcessingException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|dataformat
operator|.
name|cbor
operator|.
name|CBORFactory
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CBORObjectListSplitTest
specifier|public
class|class
name|CBORObjectListSplitTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testCBOR ()
specifier|public
name|void
name|testCBOR
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|JsonProcessingException
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessagesMatches
argument_list|(
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|DummyObject
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|DummyObject
name|d1
init|=
operator|new
name|DummyObject
argument_list|()
decl_stmt|;
name|d1
operator|.
name|setDummy
argument_list|(
literal|"value1"
argument_list|)
expr_stmt|;
name|DummyObject
name|d2
init|=
operator|new
name|DummyObject
argument_list|()
decl_stmt|;
name|d2
operator|.
name|setDummy
argument_list|(
literal|"value2"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DummyObject
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|d1
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|d2
argument_list|)
expr_stmt|;
name|CBORFactory
name|factory
init|=
operator|new
name|CBORFactory
argument_list|()
decl_stmt|;
name|ObjectMapper
name|objectMapper
init|=
operator|new
name|ObjectMapper
argument_list|(
name|factory
argument_list|)
decl_stmt|;
name|byte
index|[]
name|payload
init|=
name|objectMapper
operator|.
name|writeValueAsBytes
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|payload
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
comment|// you can specify the pojo class type for unmarshal the jason file
name|CBORDataFormat
name|format
init|=
operator|new
name|CBORDataFormat
argument_list|()
decl_stmt|;
name|format
operator|.
name|useList
argument_list|()
expr_stmt|;
name|format
operator|.
name|setUnmarshalType
argument_list|(
name|DummyObject
operator|.
name|class
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|format
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|DummyObject
specifier|public
specifier|static
class|class
name|DummyObject
block|{
DECL|field|dummy
specifier|private
name|String
name|dummy
decl_stmt|;
DECL|method|DummyObject ()
specifier|public
name|DummyObject
parameter_list|()
block|{         }
DECL|method|getDummy ()
specifier|public
name|String
name|getDummy
parameter_list|()
block|{
return|return
name|dummy
return|;
block|}
DECL|method|setDummy (String dummy)
specifier|public
name|void
name|setDummy
parameter_list|(
name|String
name|dummy
parameter_list|)
block|{
name|this
operator|.
name|dummy
operator|=
name|dummy
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

