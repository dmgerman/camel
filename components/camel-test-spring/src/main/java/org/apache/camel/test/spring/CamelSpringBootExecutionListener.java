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
name|spring
operator|.
name|SpringCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|ConfigurableApplicationContext
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

begin_class
DECL|class|CamelSpringBootExecutionListener
specifier|public
class|class
name|CamelSpringBootExecutionListener
extends|extends
name|AbstractTestExecutionListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelSpringBootExecutionListener
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|LOG
operator|.
name|info
argument_list|(
literal|"@RunWith(CamelSpringBootRunner.class) preparing: {}"
argument_list|,
name|testContext
operator|.
name|getTestClass
argument_list|()
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
init|=
name|testContext
operator|.
name|getTestClass
argument_list|()
decl_stmt|;
comment|// we are customizing the Camel context with
comment|// CamelAnnotationsHandler so we do not want to start it
comment|// automatically, which would happen when SpringCamelContext
comment|// is added to Spring ApplicationContext, so we set the flag
comment|// not to start it just yet
name|SpringCamelContext
operator|.
name|setNoStart
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"skipStartingCamelContext"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|ConfigurableApplicationContext
name|context
init|=
operator|(
name|ConfigurableApplicationContext
operator|)
name|testContext
operator|.
name|getApplicationContext
argument_list|()
decl_stmt|;
comment|// Post CamelContext(s) instantiation but pre CamelContext(s) start setup
name|CamelAnnotationsHandler
operator|.
name|handleProvidesBreakpoint
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
name|CamelAnnotationsHandler
operator|.
name|handleShutdownTimeout
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
name|CamelAnnotationsHandler
operator|.
name|handleMockEndpoints
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
name|CamelAnnotationsHandler
operator|.
name|handleMockEndpointsAndSkip
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
name|CamelAnnotationsHandler
operator|.
name|handleUseOverridePropertiesWithPropertiesComponent
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"skipStartingCamelContext"
argument_list|)
expr_stmt|;
name|SpringCamelContext
operator|.
name|setNoStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTestMethod (TestContext testContext)
specifier|public
name|void
name|beforeTestMethod
parameter_list|(
name|TestContext
name|testContext
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"@RunWith(CamelSpringBootRunner.class) before: {}.{}"
argument_list|,
name|testContext
operator|.
name|getTestClass
argument_list|()
argument_list|,
name|testContext
operator|.
name|getTestMethod
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
init|=
name|testContext
operator|.
name|getTestClass
argument_list|()
decl_stmt|;
name|String
name|testName
init|=
name|testContext
operator|.
name|getTestMethod
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|ConfigurableApplicationContext
name|context
init|=
operator|(
name|ConfigurableApplicationContext
operator|)
name|testContext
operator|.
name|getApplicationContext
argument_list|()
decl_stmt|;
comment|// mark Camel to be startable again and start Camel
name|System
operator|.
name|clearProperty
argument_list|(
literal|"skipStartingCamelContext"
argument_list|)
expr_stmt|;
comment|// route coverage need to know the test method
name|CamelAnnotationsHandler
operator|.
name|handleRouteCoverage
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|,
name|s
lambda|->
name|testName
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Initialized CamelSpringBootRunner now ready to start CamelContext"
argument_list|)
expr_stmt|;
name|CamelAnnotationsHandler
operator|.
name|handleCamelContextStartup
argument_list|(
name|context
argument_list|,
name|testClass
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

