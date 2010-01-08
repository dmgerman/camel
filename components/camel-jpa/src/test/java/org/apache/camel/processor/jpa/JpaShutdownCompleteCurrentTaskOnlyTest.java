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
name|ShutdownRunningTask
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
name|SpringCamelContext
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|orm
operator|.
name|jpa
operator|.
name|JpaTemplate
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
name|JpaTransactionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|support
operator|.
name|TransactionTemplate
import|;
end_import

begin_comment
comment|/**  * Unit test for shutdown.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JpaShutdownCompleteCurrentTaskOnlyTest
specifier|public
class|class
name|JpaShutdownCompleteCurrentTaskOnlyTest
extends|extends
name|CamelTestSupport
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
DECL|field|applicationContext
specifier|protected
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|jpaTemplate
specifier|protected
name|JpaTemplate
name|jpaTemplate
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"jpa://"
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|SendEmail
argument_list|(
literal|"a@beer.org"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"jpa://"
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|SendEmail
argument_list|(
literal|"b@beer.org"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"jpa://"
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|SendEmail
argument_list|(
literal|"c@beer.org"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"jpa://"
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|SendEmail
argument_list|(
literal|"d@beer.org"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"jpa://"
operator|+
name|SendEmail
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|SendEmail
argument_list|(
literal|"e@beer.org"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCompleteCurrentTaskOnly ()
specifier|public
name|void
name|testCompleteCurrentTaskOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
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
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route1"
argument_list|)
comment|// let it complete only current task so we shutdown faster
operator|.
name|shutdownRunningTask
argument_list|(
name|ShutdownRunningTask
operator|.
name|CompleteCurrentTaskOnly
argument_list|)
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|bar
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
decl_stmt|;
name|bar
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|bar
operator|.
name|setResultWaitTime
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// shutdown during processing
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// should NOT route all 5
name|assertTrue
argument_list|(
literal|"Should NOT complete all messages, was: "
operator|+
name|bar
operator|.
name|getReceivedCounter
argument_list|()
argument_list|,
name|bar
operator|.
name|getReceivedCounter
argument_list|()
operator|<
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|applicationContext
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/processor/jpa/springJpaRouteTest.xml"
argument_list|)
expr_stmt|;
name|cleanupRepository
argument_list|()
expr_stmt|;
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|)
return|;
block|}
DECL|method|cleanupRepository ()
specifier|protected
name|void
name|cleanupRepository
parameter_list|()
block|{
name|jpaTemplate
operator|=
operator|(
name|JpaTemplate
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"jpaTemplate"
argument_list|,
name|JpaTemplate
operator|.
name|class
argument_list|)
expr_stmt|;
name|TransactionTemplate
name|transactionTemplate
init|=
operator|new
name|TransactionTemplate
argument_list|()
decl_stmt|;
name|transactionTemplate
operator|.
name|setTransactionManager
argument_list|(
operator|new
name|JpaTransactionManager
argument_list|(
name|jpaTemplate
operator|.
name|getEntityManagerFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|transactionTemplate
operator|.
name|setPropagationBehavior
argument_list|(
name|TransactionDefinition
operator|.
name|PROPAGATION_REQUIRED
argument_list|)
expr_stmt|;
name|transactionTemplate
operator|.
name|execute
argument_list|(
operator|new
name|TransactionCallback
argument_list|()
block|{
specifier|public
name|Object
name|doInTransaction
parameter_list|(
name|TransactionStatus
name|arg0
parameter_list|)
block|{
name|List
name|list
init|=
name|jpaTemplate
operator|.
name|find
argument_list|(
name|SELECT_ALL_STRING
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|item
range|:
name|list
control|)
block|{
name|jpaTemplate
operator|.
name|remove
argument_list|(
name|item
argument_list|)
expr_stmt|;
block|}
name|jpaTemplate
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

