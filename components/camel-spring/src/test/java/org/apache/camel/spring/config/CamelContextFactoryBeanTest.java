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
name|Endpoint
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
name|Processor
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
name|Route
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
name|EventDrivenConsumerRoute
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
name|SpringCamelContext
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
name|xml
operator|.
name|XmlBeanDefinitionReader
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
name|GenericApplicationContext
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
name|io
operator|.
name|ClassPathResource
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelContextFactoryBeanTest
specifier|public
class|class
name|CamelContextFactoryBeanTest
extends|extends
name|XmlConfigTestSupport
block|{
DECL|method|testClassPathRouteLoading ()
specifier|public
name|void
name|testClassPathRouteLoading
parameter_list|()
throws|throws
name|Exception
block|{
name|ApplicationContext
name|applicationContext
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/camelContextFactoryBean.xml"
argument_list|)
decl_stmt|;
name|CamelContext
name|context
init|=
operator|(
name|CamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"camel"
argument_list|)
decl_stmt|;
name|assertValidContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|testClassPathRouteLoadingUsingNamespaces ()
specifier|public
name|void
name|testClassPathRouteLoadingUsingNamespaces
parameter_list|()
throws|throws
name|Exception
block|{
name|ApplicationContext
name|applicationContext
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/camelContextFactoryBean.xml"
argument_list|)
decl_stmt|;
name|CamelContext
name|context
init|=
operator|(
name|CamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"camel3"
argument_list|)
decl_stmt|;
name|assertValidContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|testGenericApplicationContextUsingNamespaces ()
specifier|public
name|void
name|testGenericApplicationContextUsingNamespaces
parameter_list|()
throws|throws
name|Exception
block|{
name|GenericApplicationContext
name|applicationContext
init|=
operator|new
name|GenericApplicationContext
argument_list|()
decl_stmt|;
name|XmlBeanDefinitionReader
name|xmlReader
init|=
operator|new
name|XmlBeanDefinitionReader
argument_list|(
name|applicationContext
argument_list|)
decl_stmt|;
name|xmlReader
operator|.
name|loadBeanDefinitions
argument_list|(
operator|new
name|ClassPathResource
argument_list|(
literal|"org/apache/camel/spring/camelContextFactoryBean.xml"
argument_list|)
argument_list|)
expr_stmt|;
comment|// lets refresh to inject the applicationContext into beans
name|applicationContext
operator|.
name|refresh
argument_list|()
expr_stmt|;
name|CamelContext
name|context
init|=
operator|(
name|CamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"camel3"
argument_list|)
decl_stmt|;
name|assertValidContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|testXMLRouteLoading ()
specifier|public
name|void
name|testXMLRouteLoading
parameter_list|()
throws|throws
name|Exception
block|{
name|ApplicationContext
name|applicationContext
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/camelContextFactoryBean.xml"
argument_list|)
decl_stmt|;
name|CamelContext
name|context
init|=
operator|(
name|CamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"camel2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No context found!"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|context
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found routes: "
operator|+
name|routes
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have found some routes"
argument_list|,
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"One Route should be found"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|Endpoint
name|key
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|EventDrivenConsumerRoute
name|consumerRoute
init|=
name|assertIsInstanceOf
argument_list|(
name|EventDrivenConsumerRoute
operator|.
name|class
argument_list|,
name|route
argument_list|)
decl_stmt|;
name|Processor
name|processor
init|=
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|assertEndpointUri
argument_list|(
name|key
argument_list|,
literal|"seda://test.c"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testRouteBuilderRef ()
specifier|public
name|void
name|testRouteBuilderRef
parameter_list|()
throws|throws
name|Exception
block|{
name|ApplicationContext
name|applicationContext
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/camelContextRouteBuilderRef.xml"
argument_list|)
decl_stmt|;
name|CamelContext
name|context
init|=
operator|(
name|CamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"camel5"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No context found!"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|assertValidContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|testShouldStartContext ()
specifier|public
name|void
name|testShouldStartContext
parameter_list|()
throws|throws
name|Exception
block|{
name|ApplicationContext
name|applicationContext
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/camelContextFactoryBean.xml"
argument_list|)
decl_stmt|;
name|SpringCamelContext
name|context
init|=
operator|(
name|SpringCamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"camel4"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No context found!"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"The context should not start yet"
argument_list|,
name|context
operator|.
name|getShouldStartContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There should have not route"
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|context
operator|=
operator|(
name|SpringCamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"camel3"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The context should started"
argument_list|,
name|context
operator|.
name|getShouldStartContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There should have one route"
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

