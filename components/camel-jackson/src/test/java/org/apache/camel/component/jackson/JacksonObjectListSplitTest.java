begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jackson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jackson
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
DECL|class|JacksonObjectListSplitTest
specifier|public
class|class
name|JacksonObjectListSplitTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testJackson ()
specifier|public
name|void
name|testJackson
parameter_list|()
throws|throws
name|InterruptedException
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"[{\"dummy\": \"value1\"}, {\"dummy\": \"value2\"}]"
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
name|JacksonDataFormat
name|format
init|=
operator|new
name|JacksonDataFormat
argument_list|(
name|DummyObject
operator|.
name|class
argument_list|)
decl_stmt|;
name|format
operator|.
name|useList
argument_list|()
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

