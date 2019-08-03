begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.vertx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|vertx
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|VertxRequestReplyTest
specifier|public
class|class
name|VertxRequestReplyTest
extends|extends
name|VertxBaseTestSupport
block|{
DECL|field|startUri
specifier|protected
name|String
name|startUri
init|=
literal|"direct:start"
decl_stmt|;
DECL|field|middleUri
specifier|protected
name|String
name|middleUri
init|=
literal|"vertx:foo.middle"
decl_stmt|;
DECL|field|resultUri
specifier|protected
name|String
name|resultUri
init|=
literal|"mock:result"
decl_stmt|;
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
DECL|field|body1
specifier|protected
name|String
name|body1
init|=
literal|"Camel"
decl_stmt|;
DECL|field|body2
specifier|protected
name|String
name|body2
init|=
literal|"World"
decl_stmt|;
annotation|@
name|Test
DECL|method|testVertxMessages ()
specifier|public
name|void
name|testVertxMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|resultUri
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Bye Camel"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|startUri
argument_list|,
name|body1
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|out2
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|startUri
argument_list|,
name|body2
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Camel"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out2
argument_list|)
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|startUri
argument_list|)
operator|.
name|to
argument_list|(
name|middleUri
argument_list|)
operator|.
name|to
argument_list|(
name|resultUri
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|middleUri
argument_list|)
operator|.
name|transform
argument_list|(
name|simple
argument_list|(
literal|"Bye ${body}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

