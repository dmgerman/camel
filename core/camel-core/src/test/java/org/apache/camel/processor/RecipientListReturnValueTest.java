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
name|impl
operator|.
name|JndiRegistry
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

begin_comment
comment|/**  * Tests to ensure a consistent return value when using the different ways of  * configuring the RecipientList pattern  */
end_comment

begin_class
DECL|class|RecipientListReturnValueTest
specifier|public
class|class
name|RecipientListReturnValueTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
operator|new
name|MyBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testRecipientListWithRecipientList ()
specifier|public
name|void
name|testRecipientListWithRecipientList
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestRecipientList
argument_list|(
literal|"direct:recipientList"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRecipientListWithBeanRef ()
specifier|public
name|void
name|testRecipientListWithBeanRef
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestRecipientList
argument_list|(
literal|"direct:beanRef"
argument_list|)
expr_stmt|;
block|}
DECL|method|doTestRecipientList (String uri)
specifier|private
name|void
name|doTestRecipientList
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|a
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
decl_stmt|;
name|a
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello a"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|b
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
decl_stmt|;
name|b
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello b"
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|uri
argument_list|,
literal|"Hello "
operator|+
name|uri
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello b"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
literal|"direct:beanRef"
argument_list|)
operator|.
name|bean
argument_list|(
literal|"myBean"
argument_list|,
literal|"route"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:recipientList"
argument_list|)
operator|.
name|recipientList
argument_list|()
operator|.
name|method
argument_list|(
literal|"myBean"
argument_list|,
literal|"recipientList"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Hello a"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Hello b"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyBean
specifier|public
class|class
name|MyBean
block|{
annotation|@
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RecipientList
DECL|method|route ()
specifier|public
name|String
index|[]
name|route
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"direct:a"
block|,
literal|"direct:b"
block|}
return|;
block|}
DECL|method|recipientList ()
specifier|public
name|String
index|[]
name|recipientList
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"direct:a"
block|,
literal|"direct:b"
block|}
return|;
block|}
block|}
block|}
end_class

end_unit
