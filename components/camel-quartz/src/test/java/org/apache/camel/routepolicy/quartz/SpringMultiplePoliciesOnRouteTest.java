begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.routepolicy.quartz
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|routepolicy
operator|.
name|quartz
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
name|test
operator|.
name|CamelSpringTestSupport
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
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SpringMultiplePoliciesOnRouteTest
specifier|public
class|class
name|SpringMultiplePoliciesOnRouteTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"seda:foo?concurrentConsumers=20"
decl_stmt|;
DECL|field|size
specifier|private
name|int
name|size
init|=
literal|100
decl_stmt|;
annotation|@
name|Test
DECL|method|testMultiplePoliciesOnRoute ()
specifier|public
name|void
name|testMultiplePoliciesOnRoute
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we use seda which are not persistent and hence can loose a message
comment|// when we get graceful shutdown support we can prevent this
name|getMockEndpoint
argument_list|(
literal|"mock:success"
argument_list|)
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|size
operator|-
literal|10
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|url
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.camel.test.CamelSpringTestSupport#createApplicationContext()      */
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/routepolicy/quartz/MultiplePolicies.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

