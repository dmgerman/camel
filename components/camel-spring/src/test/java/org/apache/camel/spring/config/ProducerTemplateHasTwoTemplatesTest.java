begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|config
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
name|CamelContext
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
name|ProducerTemplate
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
name|annotation
operator|.
name|Autowired
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
name|ContextConfiguration
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
name|junit38
operator|.
name|AbstractJUnit38SpringContextTests
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|ProducerTemplateHasTwoTemplatesTest
specifier|public
class|class
name|ProducerTemplateHasTwoTemplatesTest
extends|extends
name|AbstractJUnit38SpringContextTests
block|{
annotation|@
name|Autowired
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|method|testHasTwoTemplates ()
specifier|public
name|void
name|testHasTwoTemplates
parameter_list|()
block|{
name|ProducerTemplate
name|lookup
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"myTemplate"
argument_list|,
name|ProducerTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should lookup producer template"
argument_list|,
name|lookup
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|lookup2
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"myOtherTemplate"
argument_list|,
name|ProducerTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should lookup producer template"
argument_list|,
name|lookup2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
literal|"Should not be same"
argument_list|,
name|lookup
argument_list|,
name|lookup2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

