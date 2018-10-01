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
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|impl
operator|.
name|DefaultProducerTemplate
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
DECL|class|ProducerTemplateMixedAutoRegisterTwoCamelContextsTest
specifier|public
class|class
name|ProducerTemplateMixedAutoRegisterTwoCamelContextsTest
extends|extends
name|SpringRunWithTestSupport
block|{
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"camel1"
argument_list|)
DECL|field|context1
specifier|private
name|CamelContext
name|context1
decl_stmt|;
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"camel2"
argument_list|)
DECL|field|context2
specifier|private
name|CamelContext
name|context2
decl_stmt|;
annotation|@
name|Test
DECL|method|testHasTemplateCamel1 ()
specifier|public
name|void
name|testHasTemplateCamel1
parameter_list|()
block|{
name|DefaultProducerTemplate
name|lookup
init|=
name|context1
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"template1"
argument_list|,
name|DefaultProducerTemplate
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
name|assertEquals
argument_list|(
literal|"camel1"
argument_list|,
name|lookup
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHasTemplateCamel2 ()
specifier|public
name|void
name|testHasTemplateCamel2
parameter_list|()
block|{
name|DefaultProducerTemplate
name|lookup
init|=
name|context1
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"template2"
argument_list|,
name|DefaultProducerTemplate
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
name|assertEquals
argument_list|(
literal|"camel2"
argument_list|,
name|lookup
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHasNoConsumerTemplateCamel1 ()
specifier|public
name|void
name|testHasNoConsumerTemplateCamel1
parameter_list|()
block|{
name|ConsumerTemplate
name|lookup
init|=
name|context1
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
literal|"Should NOT lookup consumer template"
argument_list|,
name|lookup
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHasNoConsumerTemplateCamel2 ()
specifier|public
name|void
name|testHasNoConsumerTemplateCamel2
parameter_list|()
block|{
name|ConsumerTemplate
name|lookup
init|=
name|context2
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
literal|"Should NOT lookup consumer template"
argument_list|,
name|lookup
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

