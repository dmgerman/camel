begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ConsumerTemplate
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
name|spring
operator|.
name|SpringRunWithTestSupport
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

begin_class
annotation|@
name|ContextConfiguration
DECL|class|ConsumerTemplateAlreadyExistTest
specifier|public
class|class
name|ConsumerTemplateAlreadyExistTest
extends|extends
name|SpringRunWithTestSupport
block|{
annotation|@
name|Autowired
DECL|field|template
specifier|private
name|ConsumerTemplate
name|template
decl_stmt|;
annotation|@
name|Autowired
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Test
DECL|method|testHasExistingTemplate ()
specifier|public
name|void
name|testHasExistingTemplate
parameter_list|()
block|{
name|assertNotNull
argument_list|(
literal|"Should have injected a consumer template"
argument_list|,
name|template
argument_list|)
expr_stmt|;
name|ConsumerTemplate
name|lookup
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"myConsumerTemplate"
argument_list|,
name|ConsumerTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should lookup consumer template"
argument_list|,
name|lookup
argument_list|)
expr_stmt|;
name|ConsumerTemplate
name|lookup2
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"consumerTemplate"
argument_list|,
name|ConsumerTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Should not be able to lookup consumer template"
argument_list|,
name|lookup2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testShouldBeSingleton ()
specifier|public
name|void
name|testShouldBeSingleton
parameter_list|()
block|{
name|ConsumerTemplate
name|lookup
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"myConsumerTemplate"
argument_list|,
name|ConsumerTemplate
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
name|ConsumerTemplate
name|lookup2
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"myConsumerTemplate"
argument_list|,
name|ConsumerTemplate
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
name|assertSame
argument_list|(
literal|"Should be same instances (singleton)"
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

