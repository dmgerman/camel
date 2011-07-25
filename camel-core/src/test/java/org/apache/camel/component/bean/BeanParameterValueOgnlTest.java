begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|BeanParameterValueOgnlTest
specifier|public
class|class
name|BeanParameterValueOgnlTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testBeanParameterValue ()
specifier|public
name|void
name|testBeanParameterValue
parameter_list|()
throws|throws
name|Exception
block|{
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testBeanParameterValueBodyOgnl ()
specifier|public
name|void
name|testBeanParameterValueBodyOgnl
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Tony"
argument_list|)
expr_stmt|;
name|Animal
name|tiger
init|=
operator|new
name|Animal
argument_list|(
literal|"Tony"
argument_list|,
literal|13
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start2"
argument_list|,
name|tiger
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testBeanParameterValueHeaderOgnl ()
specifier|public
name|void
name|testBeanParameterValueHeaderOgnl
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Kong"
argument_list|)
expr_stmt|;
name|Animal
name|kong
init|=
operator|new
name|Animal
argument_list|(
literal|"Kong"
argument_list|,
literal|34
argument_list|)
decl_stmt|;
name|Animal
name|tiger
init|=
operator|new
name|Animal
argument_list|(
literal|"Tony"
argument_list|,
literal|13
argument_list|)
decl_stmt|;
name|tiger
operator|.
name|setFriend
argument_list|(
name|kong
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start3"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"animal"
argument_list|,
name|tiger
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
literal|"foo"
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:foo?method=bar(body,true)"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:foo?method=bar(${body.name}, true)"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:foo?method=bar(${header.animal?.friend.name}, true)"
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
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|bar (String body, boolean hello)
specifier|public
name|String
name|bar
parameter_list|(
name|String
name|body
parameter_list|,
name|boolean
name|hello
parameter_list|)
block|{
if|if
condition|(
name|hello
condition|)
block|{
return|return
literal|"Hello "
operator|+
name|body
return|;
block|}
else|else
block|{
return|return
name|body
return|;
block|}
block|}
DECL|method|echo (String body, int times)
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|body
parameter_list|,
name|int
name|times
parameter_list|)
block|{
if|if
condition|(
name|times
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|times
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
name|body
return|;
block|}
block|}
DECL|class|Animal
specifier|public
specifier|static
specifier|final
class|class
name|Animal
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|age
specifier|private
name|int
name|age
decl_stmt|;
DECL|field|friend
specifier|private
name|Animal
name|friend
decl_stmt|;
DECL|method|Animal (String name, int age)
specifier|private
name|Animal
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|age
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|age
operator|=
name|age
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
DECL|method|getAge ()
specifier|public
name|int
name|getAge
parameter_list|()
block|{
return|return
name|age
return|;
block|}
DECL|method|getFriend ()
specifier|public
name|Animal
name|getFriend
parameter_list|()
block|{
return|return
name|friend
return|;
block|}
DECL|method|setFriend (Animal friend)
specifier|public
name|void
name|setFriend
parameter_list|(
name|Animal
name|friend
parameter_list|)
block|{
name|this
operator|.
name|friend
operator|=
name|friend
expr_stmt|;
block|}
DECL|method|isDangerous ()
specifier|public
name|boolean
name|isDangerous
parameter_list|()
block|{
return|return
name|name
operator|.
name|contains
argument_list|(
literal|"Tiger"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
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

