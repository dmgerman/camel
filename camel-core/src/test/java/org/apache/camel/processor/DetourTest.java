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

begin_class
DECL|class|DetourTest
specifier|public
class|class
name|DetourTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|BODY
specifier|private
specifier|static
specifier|final
name|String
name|BODY
init|=
literal|"<order custId=\"123\"/>"
decl_stmt|;
DECL|field|controlBean
specifier|private
name|ControlBean
name|controlBean
decl_stmt|;
DECL|method|testDetourSet ()
specifier|public
name|void
name|testDetourSet
parameter_list|()
throws|throws
name|Exception
block|{
name|controlBean
operator|.
name|setDetour
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|equals
argument_list|(
name|BODY
argument_list|)
expr_stmt|;
name|MockEndpoint
name|detourEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:detour"
argument_list|)
decl_stmt|;
name|detourEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|detourEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|equals
argument_list|(
name|BODY
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|BODY
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testDetourNotSet ()
specifier|public
name|void
name|testDetourNotSet
parameter_list|()
throws|throws
name|Exception
block|{
name|controlBean
operator|.
name|setDetour
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|equals
argument_list|(
name|BODY
argument_list|)
expr_stmt|;
name|MockEndpoint
name|detourEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:detour"
argument_list|)
decl_stmt|;
name|detourEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|BODY
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|controlBean
operator|=
operator|new
name|ControlBean
argument_list|()
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"controlBean"
argument_list|,
name|controlBean
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
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
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|method
argument_list|(
literal|"controlBean"
argument_list|,
literal|"isDetour"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:detour"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
DECL|class|ControlBean
specifier|public
specifier|final
class|class
name|ControlBean
block|{
DECL|field|detour
specifier|private
name|boolean
name|detour
decl_stmt|;
DECL|method|setDetour (boolean detour)
specifier|public
name|void
name|setDetour
parameter_list|(
name|boolean
name|detour
parameter_list|)
block|{
name|this
operator|.
name|detour
operator|=
name|detour
expr_stmt|;
block|}
DECL|method|isDetour ()
specifier|public
name|boolean
name|isDetour
parameter_list|()
block|{
return|return
name|detour
return|;
block|}
block|}
block|}
end_class

end_unit

