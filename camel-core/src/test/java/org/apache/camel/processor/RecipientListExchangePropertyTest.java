begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RecipientListExchangePropertyTest
specifier|public
class|class
name|RecipientListExchangePropertyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|myStuff
specifier|private
specifier|final
name|MyStuff
name|myStuff
init|=
operator|new
name|MyStuff
argument_list|(
literal|"Blah"
argument_list|)
decl_stmt|;
DECL|method|testExchangeProperty ()
specifier|public
name|void
name|testExchangeProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:x"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:x"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|property
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|myStuff
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|property
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|myStuff
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:z"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:z"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|property
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|myStuff
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|property
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|myStuff
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndProperty
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"foo"
argument_list|,
name|myStuff
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|MyStuff
name|stuff
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
literal|"foo"
argument_list|,
name|MyStuff
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"Should be same instance"
argument_list|,
name|myStuff
argument_list|,
name|stuff
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|recipientList
argument_list|(
name|constant
argument_list|(
literal|"mock:x,mock:y,mock:z"
argument_list|)
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
DECL|class|MyStuff
specifier|private
specifier|static
specifier|final
class|class
name|MyStuff
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|method|MyStuff (String name)
specifier|private
name|MyStuff
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
block|}
end_class

end_unit

