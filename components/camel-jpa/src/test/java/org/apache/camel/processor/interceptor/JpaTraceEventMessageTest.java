begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
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
name|processor
operator|.
name|interceptor
operator|.
name|jpa
operator|.
name|JpaTraceEventMessage
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JpaTraceEventMessageTest
specifier|public
class|class
name|JpaTraceEventMessageTest
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
name|JpaTraceEventMessage
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
name|Test
DECL|method|testSendTraceMessage ()
specifier|public
name|void
name|testSendTraceMessage
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
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEntityInDB
argument_list|()
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
literal|"org/apache/camel/processor/interceptor/springJpaTraveEvent.xml"
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
name|Tracer
name|tracer
init|=
operator|new
name|Tracer
argument_list|()
decl_stmt|;
name|tracer
operator|.
name|setDestinationUri
argument_list|(
literal|"jpa://"
operator|+
name|JpaTraceEventMessage
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?persistenceUnit=trace"
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setUseJpa
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|addInterceptStrategy
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
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
DECL|method|assertEntityInDB ()
specifier|private
name|void
name|assertEntityInDB
parameter_list|()
throws|throws
name|Exception
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
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JpaTraceEventMessage
name|db
init|=
operator|(
name|JpaTraceEventMessage
operator|)
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|db
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|db
operator|.
name|getFromEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock://result"
argument_list|,
name|db
operator|.
name|getToNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|db
operator|.
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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

