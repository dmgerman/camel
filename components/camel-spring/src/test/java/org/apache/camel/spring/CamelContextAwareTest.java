begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|Map
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
name|engine
operator|.
name|DefaultConsumerTemplate
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
name|engine
operator|.
name|DefaultProducerTemplate
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
name|AbstractXmlApplicationContext
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

begin_class
DECL|class|CamelContextAwareTest
specifier|public
class|class
name|CamelContextAwareTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|bean1
specifier|protected
name|CamelContextAwareBean
name|bean1
decl_stmt|;
annotation|@
name|Test
DECL|method|testInjectionPoints ()
specifier|public
name|void
name|testInjectionPoints
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
literal|"No CamelContext injected!"
argument_list|,
name|bean1
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|globalOptions
init|=
name|bean1
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getGlobalOptions
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The global options reference should not be null"
argument_list|,
name|globalOptions
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No global options injected"
argument_list|,
name|globalOptions
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should get the value of org.apache.camel.test"
argument_list|,
name|globalOptions
operator|.
name|get
argument_list|(
literal|"org.apache.camel.test"
argument_list|)
argument_list|,
literal|"this is a test first"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCamelTemplates ()
specifier|public
name|void
name|testCamelTemplates
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultProducerTemplate
name|producer1
init|=
name|getMandatoryBean
argument_list|(
name|DefaultProducerTemplate
operator|.
name|class
argument_list|,
literal|"producer1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Inject a wrong camel context"
argument_list|,
name|producer1
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"camel1"
argument_list|)
expr_stmt|;
name|DefaultProducerTemplate
name|producer2
init|=
name|getMandatoryBean
argument_list|(
name|DefaultProducerTemplate
operator|.
name|class
argument_list|,
literal|"producer2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Inject a wrong camel context"
argument_list|,
name|producer2
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"camel2"
argument_list|)
expr_stmt|;
name|DefaultConsumerTemplate
name|consumer
init|=
name|getMandatoryBean
argument_list|(
name|DefaultConsumerTemplate
operator|.
name|class
argument_list|,
literal|"consumer"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Inject a wrong camel context"
argument_list|,
name|consumer
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"camel2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|bean1
operator|=
name|getMandatoryBean
argument_list|(
name|CamelContextAwareBean
operator|.
name|class
argument_list|,
literal|"bean1"
argument_list|)
expr_stmt|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/camelContextAwareBean.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

