begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.postprocessor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|postprocessor
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|ApplicationContext
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
DECL|class|PlainSpringCustomPostProcessorOnRouteBuilderTest
specifier|public
class|class
name|PlainSpringCustomPostProcessorOnRouteBuilderTest
extends|extends
name|TestCase
block|{
DECL|method|testShouldProcessAnnotatedFields ()
specifier|public
name|void
name|testShouldProcessAnnotatedFields
parameter_list|()
block|{
name|ApplicationContext
name|context
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"classpath:/org/apache/camel/spring/postprocessor/plainSpringCustomPostProcessorOnRouteBuilderTest.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Context not created"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Post processor not registered"
argument_list|,
name|context
operator|.
name|getBeansOfType
argument_list|(
name|MagicAnnotationPostProcessor
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|TestPojo
name|pojo
init|=
name|context
operator|.
name|getBean
argument_list|(
literal|"testPojo"
argument_list|,
name|TestPojo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Test pojo not registered"
argument_list|,
name|pojo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Processor has not changed field value"
argument_list|,
literal|"Changed Value"
argument_list|,
name|pojo
operator|.
name|getTestValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

