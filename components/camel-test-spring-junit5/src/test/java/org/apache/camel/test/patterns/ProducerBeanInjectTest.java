begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.patterns
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|patterns
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertNotNull
import|;
end_import

begin_class
DECL|class|ProducerBeanInjectTest
specifier|public
class|class
name|ProducerBeanInjectTest
block|{
annotation|@
name|Test
DECL|method|checkProducerBeanInjection ()
specifier|public
name|void
name|checkProducerBeanInjection
parameter_list|()
block|{
name|AbstractApplicationContext
name|applicationContext
init|=
name|createApplicationContext
argument_list|()
decl_stmt|;
name|MyProduceBean
name|bean
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"myProduceBean"
argument_list|,
name|MyProduceBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bean
operator|.
name|getProducerTemplate
argument_list|()
argument_list|,
literal|"The producerTemplate should not be null."
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel rocks!"
argument_list|,
name|bean
operator|.
name|getProducerTemplate
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"Camel"
argument_list|)
argument_list|,
literal|"Get a wrong response"
argument_list|)
expr_stmt|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/test/patterns/ProduceBeanInjectTest.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

