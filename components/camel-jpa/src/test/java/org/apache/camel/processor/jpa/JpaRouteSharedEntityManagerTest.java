begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|jpa
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|Exchange
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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|examples
operator|.
name|SendEmail
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
name|SpringRouteBuilder
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assume
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
name|expression
operator|.
name|BeanFactoryResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|expression
operator|.
name|Expression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|expression
operator|.
name|spel
operator|.
name|standard
operator|.
name|SpelExpressionParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|expression
operator|.
name|spel
operator|.
name|support
operator|.
name|StandardEvaluationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|LocalEntityManagerFactoryBean
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_class
DECL|class|JpaRouteSharedEntityManagerTest
specifier|public
class|class
name|JpaRouteSharedEntityManagerTest
extends|extends
name|AbstractJpaTest
block|{
DECL|field|SELECT_ALL_STRING
specifier|protected
specifier|static
specifier|final
name|String
name|SELECT_ALL_STRING
init|=
literal|"select x from "
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" x"
decl_stmt|;
DECL|field|latch
specifier|private
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
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
comment|// Don't run on Hibernate
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
literal|"org.hibernate.Hibernate"
argument_list|)
operator|==
literal|null
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteJpaShared ()
specifier|public
name|void
name|testRouteJpaShared
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|int
name|countStart
init|=
name|getBrokerCount
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
literal|"brokerCount"
argument_list|,
name|countStart
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:startShared"
argument_list|,
operator|new
name|SendEmail
argument_list|(
literal|"one@somewhere.org"
argument_list|)
argument_list|)
expr_stmt|;
comment|// start route
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"jpaShared"
argument_list|)
expr_stmt|;
comment|// not the cleanest way to check the number of open connections
name|int
name|countEnd
init|=
name|getBrokerCount
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
literal|"brokerCount"
argument_list|,
name|countEnd
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|getBrokerCount ()
specifier|private
name|int
name|getBrokerCount
parameter_list|()
block|{
name|LocalEntityManagerFactoryBean
name|entityManagerFactory
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"&entityManagerFactory"
argument_list|,
name|LocalEntityManagerFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//uses Spring EL so we don't need to reference the classes
name|StandardEvaluationContext
name|context
init|=
operator|new
name|StandardEvaluationContext
argument_list|(
name|entityManagerFactory
argument_list|)
decl_stmt|;
name|context
operator|.
name|setBeanResolver
argument_list|(
operator|new
name|BeanFactoryResolver
argument_list|(
name|applicationContext
argument_list|)
argument_list|)
expr_stmt|;
name|SpelExpressionParser
name|parser
init|=
operator|new
name|SpelExpressionParser
argument_list|()
decl_stmt|;
name|Expression
name|expression
init|=
name|parser
operator|.
name|parseExpression
argument_list|(
literal|"nativeEntityManagerFactory.brokerFactory.openBrokers"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|brokers
init|=
name|expression
operator|.
name|getValue
argument_list|(
name|context
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|brokers
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testRouteJpaNotShared ()
specifier|public
name|void
name|testRouteJpaNotShared
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:startNotshared"
argument_list|,
operator|new
name|SendEmail
argument_list|(
literal|"one@somewhere.org"
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|countStart
init|=
name|getBrokerCount
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
literal|"brokerCount"
argument_list|,
name|countStart
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
comment|// start route
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"jpaOwn"
argument_list|)
expr_stmt|;
comment|// not the cleanest way to check the number of open connections
name|int
name|countEnd
init|=
name|getBrokerCount
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
literal|"brokerCount"
argument_list|,
name|countEnd
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|SpringRouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:startNotshared"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jpa://"
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:startShared"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jpa://"
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?sharedEntityManager=true&joinTransaction=false"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jpa://"
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?sharedEntityManager=true&joinTransaction=false"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"jpaShared"
argument_list|)
operator|.
name|autoStartup
argument_list|(
literal|false
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|LatchProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jpa://"
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?sharedEntityManager=false"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"jpaOwn"
argument_list|)
operator|.
name|autoStartup
argument_list|(
literal|false
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|LatchProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|routeXml ()
specifier|protected
name|String
name|routeXml
parameter_list|()
block|{
return|return
literal|"org/apache/camel/processor/jpa/springJpaRouteTest.xml"
return|;
block|}
annotation|@
name|Override
DECL|method|selectAllString ()
specifier|protected
name|String
name|selectAllString
parameter_list|()
block|{
return|return
name|SELECT_ALL_STRING
return|;
block|}
DECL|class|LatchProcessor
specifier|private
class|class
name|LatchProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|latch
operator|.
name|await
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

