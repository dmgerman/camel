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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Consume
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|CamelPostProcessorHelperConsumePropertyTest
specifier|public
class|class
name|CamelPostProcessorHelperConsumePropertyTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testConsumePropertyExplicit ()
specifier|public
name|void
name|testConsumePropertyExplicit
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelPostProcessorHelper
name|helper
init|=
operator|new
name|CamelPostProcessorHelper
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyConsumeBean
name|my
init|=
operator|new
name|MyConsumeBean
argument_list|()
decl_stmt|;
name|my
operator|.
name|setFoo
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
name|Method
name|method
init|=
name|my
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"consumeSomething"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|helper
operator|.
name|consumerInjection
argument_list|(
name|method
argument_list|,
name|my
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
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
literal|"seda:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testConsumePropertyImplicit ()
specifier|public
name|void
name|testConsumePropertyImplicit
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelPostProcessorHelper
name|helper
init|=
operator|new
name|CamelPostProcessorHelper
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyConsumeBean
name|my
init|=
operator|new
name|MyConsumeBean
argument_list|()
decl_stmt|;
name|my
operator|.
name|setFoo
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
name|Method
name|method
init|=
name|my
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|helper
operator|.
name|consumerInjection
argument_list|(
name|method
argument_list|,
name|my
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
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
literal|"seda:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testConsumePropertyOnImplicit ()
specifier|public
name|void
name|testConsumePropertyOnImplicit
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelPostProcessorHelper
name|helper
init|=
operator|new
name|CamelPostProcessorHelper
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyConsumeBean
name|my
init|=
operator|new
name|MyConsumeBean
argument_list|()
decl_stmt|;
name|my
operator|.
name|setFoo
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
name|Method
name|method
init|=
name|my
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"onFoo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|helper
operator|.
name|consumerInjection
argument_list|(
name|method
argument_list|,
name|my
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
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
literal|"seda:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testConsumePropertyEndpointImplicit ()
specifier|public
name|void
name|testConsumePropertyEndpointImplicit
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelPostProcessorHelper
name|helper
init|=
operator|new
name|CamelPostProcessorHelper
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyConsumeBean
name|my
init|=
operator|new
name|MyConsumeBean
argument_list|()
decl_stmt|;
name|my
operator|.
name|setBarEndpoint
argument_list|(
literal|"seda:bar"
argument_list|)
expr_stmt|;
name|Method
name|method
init|=
name|my
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"bar"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|helper
operator|.
name|consumerInjection
argument_list|(
name|method
argument_list|,
name|my
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
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
literal|"seda:bar"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testConsumePropertyOnEndpointImplicit ()
specifier|public
name|void
name|testConsumePropertyOnEndpointImplicit
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelPostProcessorHelper
name|helper
init|=
operator|new
name|CamelPostProcessorHelper
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyConsumeBean
name|my
init|=
operator|new
name|MyConsumeBean
argument_list|()
decl_stmt|;
name|my
operator|.
name|setBarEndpoint
argument_list|(
literal|"seda:bar"
argument_list|)
expr_stmt|;
name|Method
name|method
init|=
name|my
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"onBar"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|helper
operator|.
name|consumerInjection
argument_list|(
name|method
argument_list|,
name|my
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
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
literal|"seda:bar"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|MyConsumeBean
specifier|public
class|class
name|MyConsumeBean
block|{
DECL|field|foo
specifier|private
name|String
name|foo
decl_stmt|;
DECL|field|barEndpoint
specifier|private
name|String
name|barEndpoint
decl_stmt|;
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
DECL|method|setFoo (String foo)
specifier|public
name|void
name|setFoo
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
DECL|method|getBarEndpoint ()
specifier|public
name|String
name|getBarEndpoint
parameter_list|()
block|{
return|return
name|barEndpoint
return|;
block|}
DECL|method|setBarEndpoint (String barEndpoint)
specifier|public
name|void
name|setBarEndpoint
parameter_list|(
name|String
name|barEndpoint
parameter_list|)
block|{
name|this
operator|.
name|barEndpoint
operator|=
name|barEndpoint
expr_stmt|;
block|}
annotation|@
name|Consume
argument_list|(
name|property
operator|=
literal|"foo"
argument_list|)
DECL|method|consumeSomething (String body)
specifier|public
name|void
name|consumeSomething
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:result"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Consume
argument_list|()
DECL|method|foo (String body)
specifier|public
name|void
name|foo
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:result"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Consume
argument_list|()
DECL|method|onFoo (String body)
specifier|public
name|void
name|onFoo
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:result"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Consume
argument_list|()
DECL|method|bar (String body)
specifier|public
name|void
name|bar
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:result"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Consume
argument_list|()
DECL|method|onBar (String body)
specifier|public
name|void
name|onBar
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:result"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

