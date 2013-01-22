begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.mock
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|mock
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
name|EndpointInject
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
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|InitializingBean
import|;
end_import

begin_comment
comment|/**  * An example bean which adds some expectations on some mock endpoints and then  * asserts that the expectactions are met.  *  * @version   */
end_comment

begin_comment
comment|// START SNIPPET: example
end_comment

begin_class
DECL|class|MyAssertions
specifier|public
class|class
name|MyAssertions
implements|implements
name|InitializingBean
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:matched"
argument_list|)
DECL|field|matched
specifier|private
name|MockEndpoint
name|matched
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:notMatched"
argument_list|)
DECL|field|notMatched
specifier|private
name|MockEndpoint
name|notMatched
decl_stmt|;
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets add some expectations
name|matched
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|notMatched
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|assertEndpointsValid ()
specifier|public
name|void
name|assertEndpointsValid
parameter_list|()
throws|throws
name|Exception
block|{
comment|// now lets perform some assertions that the test worked as we expect
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Should have a matched endpoint"
argument_list|,
name|matched
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"Should have a notMatched endpoint"
argument_list|,
name|notMatched
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|matched
argument_list|,
name|notMatched
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: example
end_comment

end_unit

