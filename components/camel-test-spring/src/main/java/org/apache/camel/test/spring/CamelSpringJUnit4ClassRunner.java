begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|InitializationError
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|OrderComparator
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
name|TestContextManager
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
name|TestExecutionListener
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
name|junit4
operator|.
name|SpringJUnit4ClassRunner
import|;
end_import

begin_comment
comment|/**  * An implementation bringing the functionality of {@link org.apache.camel.test.spring.CamelSpringTestSupport} to  * Spring Test based test cases.  This approach allows developers to implement tests  * for their Spring based applications/routes using the typical Spring Test conventions  * for test development.  */
end_comment

begin_class
DECL|class|CamelSpringJUnit4ClassRunner
specifier|public
class|class
name|CamelSpringJUnit4ClassRunner
extends|extends
name|SpringJUnit4ClassRunner
block|{
DECL|method|CamelSpringJUnit4ClassRunner (Class<?> clazz)
specifier|public
name|CamelSpringJUnit4ClassRunner
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|InitializationError
block|{
name|super
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the specialized manager instance that provides tight integration between Camel testing      * features and Spring.      *      * @return a new instance of {@link CamelTestContextManager}.      */
annotation|@
name|Override
DECL|method|createTestContextManager (Class<?> clazz)
specifier|protected
name|TestContextManager
name|createTestContextManager
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
operator|new
name|CamelTestContextManager
argument_list|(
name|clazz
argument_list|)
return|;
block|}
comment|/**      * An implementation providing additional integration between Spring Test and Camel      * testing features.      */
DECL|class|CamelTestContextManager
specifier|public
specifier|static
specifier|final
class|class
name|CamelTestContextManager
extends|extends
name|TestContextManager
block|{
DECL|method|CamelTestContextManager (Class<?> testClass)
specifier|public
name|CamelTestContextManager
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
block|{
name|super
argument_list|(
name|testClass
argument_list|)
expr_stmt|;
comment|// inject Camel first, and then disable jmx and add the stop-watch
name|List
argument_list|<
name|TestExecutionListener
argument_list|>
name|list
init|=
name|getTestExecutionListeners
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|CamelSpringTestContextLoaderTestExecutionListener
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|DisableJmxTestExecutionListener
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|StopWatchTestExecutionListener
argument_list|()
argument_list|)
expr_stmt|;
name|OrderComparator
operator|.
name|sort
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|registerTestExecutionListeners
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

