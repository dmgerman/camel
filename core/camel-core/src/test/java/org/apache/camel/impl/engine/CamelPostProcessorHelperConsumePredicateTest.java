begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CamelPostProcessorHelperConsumePredicateTest
specifier|public
class|class
name|CamelPostProcessorHelperConsumePredicateTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testConsumePredicate ()
specifier|public
name|void
name|testConsumePredicate
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
literal|"low"
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
name|method
operator|=
name|my
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"high"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|getMockEndpoint
argument_list|(
literal|"mock:low"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"17"
argument_list|,
literal|"89"
argument_list|,
literal|"39"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:high"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"219"
argument_list|,
literal|"112"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"17"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"219"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"89"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"112"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"39"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumePredicateDrop ()
specifier|public
name|void
name|testConsumePredicateDrop
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
literal|"low"
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
name|method
operator|=
name|my
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"high"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|getMockEndpoint
argument_list|(
literal|"mock:low"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"17"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:high"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"112"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"17"
argument_list|)
expr_stmt|;
comment|// should be dropped as it does not match any predicates
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"-1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"112"
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
annotation|@
name|Consume
argument_list|(
name|value
operator|=
literal|"direct:foo"
argument_list|,
name|predicate
operator|=
literal|"${body}>= 0&& ${body}< 100"
argument_list|)
DECL|method|low (String body)
specifier|public
name|void
name|low
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:low"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Consume
argument_list|(
name|value
operator|=
literal|"direct:foo"
argument_list|,
name|predicate
operator|=
literal|"${body}>= 100"
argument_list|)
DECL|method|high (String body)
specifier|public
name|void
name|high
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:high"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
