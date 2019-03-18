begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|Ordered
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|TestContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractTestExecutionListener
import|;
end_import

begin_comment
comment|/**  * Helper for {@link CamelSpringTestContextLoader} that sets the test class state  * in {@link CamelSpringTestHelper} almost immediately before the loader initializes  * the Spring context.  *<p/>  * Implemented as a listener as the state can be set on a {@code ThreadLocal} and we are pretty sure  * that the same thread will be used to initialize the Spring context.  */
end_comment

begin_class
DECL|class|CamelSpringTestContextLoaderTestExecutionListener
specifier|public
class|class
name|CamelSpringTestContextLoaderTestExecutionListener
extends|extends
name|AbstractTestExecutionListener
block|{
comment|/**      * The default implementation returns {@link org.springframework.core.Ordered#LOWEST_PRECEDENCE},      * thereby ensuring that custom listeners are ordered after default      * listeners supplied by the framework. Can be overridden by subclasses      * as necessary.      */
annotation|@
name|Override
DECL|method|getOrder ()
specifier|public
name|int
name|getOrder
parameter_list|()
block|{
comment|//set Camel first
return|return
name|Ordered
operator|.
name|HIGHEST_PRECEDENCE
return|;
block|}
annotation|@
name|Override
DECL|method|prepareTestInstance (TestContext testContext)
specifier|public
name|void
name|prepareTestInstance
parameter_list|(
name|TestContext
name|testContext
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelSpringTestHelper
operator|.
name|setTestClass
argument_list|(
name|testContext
operator|.
name|getTestClass
argument_list|()
argument_list|)
expr_stmt|;
name|CamelSpringTestHelper
operator|.
name|setTestContext
argument_list|(
name|testContext
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

