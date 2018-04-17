begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gson
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

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
name|google
operator|.
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
DECL|class|GsonMarshalListTest
specifier|public
class|class
name|GsonMarshalListTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testMarshalAndUnmarshalPojo ()
specifier|public
name|void
name|testMarshalAndUnmarshalPojo
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|TestPojo
argument_list|>
name|inList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|TestPojo
name|in1
init|=
operator|new
name|TestPojo
argument_list|()
decl_stmt|;
name|in1
operator|.
name|setName
argument_list|(
literal|"Camel1"
argument_list|)
expr_stmt|;
name|TestPojo
name|in2
init|=
operator|new
name|TestPojo
argument_list|()
decl_stmt|;
name|in2
operator|.
name|setName
argument_list|(
literal|"Camel2"
argument_list|)
expr_stmt|;
name|inList
operator|.
name|add
argument_list|(
name|in1
argument_list|)
expr_stmt|;
name|inList
operator|.
name|add
argument_list|(
name|in2
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:reversePojo"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|List
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|inList
argument_list|)
expr_stmt|;
name|String
name|marshalled
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:inPojo"
argument_list|,
name|inList
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"[{\"name\":\"Camel1\"},{\"name\":\"Camel2\"}]"
argument_list|,
name|marshalled
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:backPojo"
argument_list|,
name|marshalled
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
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
name|GsonDataFormat
name|formatPojo
init|=
operator|new
name|GsonDataFormat
argument_list|()
decl_stmt|;
name|Type
name|genericType
init|=
operator|new
name|TypeToken
argument_list|<
name|List
argument_list|<
name|TestPojo
argument_list|>
argument_list|>
argument_list|()
block|{ }
operator|.
name|getType
argument_list|()
decl_stmt|;
name|formatPojo
operator|.
name|setUnmarshalGenericType
argument_list|(
name|genericType
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:inPojo"
argument_list|)
operator|.
name|marshal
argument_list|(
name|formatPojo
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:backPojo"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|formatPojo
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reversePojo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

