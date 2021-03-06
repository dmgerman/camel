begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|HashMap
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|ExecutorService
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
name|Executors
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
name|Future
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|JpaProducerConcurrentTest
specifier|public
class|class
name|JpaProducerConcurrentTest
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
annotation|@
name|Test
DECL|method|testNoConcurrentProducers ()
specifier|public
name|void
name|testNoConcurrentProducers
parameter_list|()
throws|throws
name|Exception
block|{
name|doSendMessages
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConcurrentProducers ()
specifier|public
name|void
name|testConcurrentProducers
parameter_list|()
throws|throws
name|Exception
block|{
name|doSendMessages
argument_list|(
literal|10
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
DECL|method|doSendMessages (int files, int poolSize)
specifier|private
name|void
name|doSendMessages
parameter_list|(
name|int
name|files
parameter_list|,
name|int
name|poolSize
parameter_list|)
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|files
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|assertNoDuplicates
argument_list|(
name|body
argument_list|()
argument_list|)
expr_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|poolSize
argument_list|)
decl_stmt|;
comment|// we access the responses Map below only inside the main thread,
comment|// so no need for a thread-safe Map implementation
name|Map
argument_list|<
name|Integer
argument_list|,
name|Future
argument_list|<
name|SendEmail
argument_list|>
argument_list|>
name|responses
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|index
init|=
name|i
decl_stmt|;
name|Future
argument_list|<
name|SendEmail
argument_list|>
name|out
init|=
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|SendEmail
argument_list|>
argument_list|()
block|{
specifier|public
name|SendEmail
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|SendEmail
argument_list|(
literal|"user"
operator|+
name|index
operator|+
literal|"@somewhere.org"
argument_list|)
argument_list|,
name|SendEmail
operator|.
name|class
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|responses
operator|.
name|put
argument_list|(
name|index
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|files
argument_list|,
name|responses
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// get them so they are complete
for|for
control|(
name|Future
argument_list|<
name|SendEmail
argument_list|>
name|future
range|:
name|responses
operator|.
name|values
argument_list|()
control|)
block|{
name|SendEmail
name|sendEmail
init|=
name|future
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|sendEmail
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Persisted the SendEmail entity with the id {} and the address {}"
argument_list|,
name|sendEmail
operator|.
name|getId
argument_list|()
argument_list|,
name|sendEmail
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// assert in the database
name|assertEntityInDB
argument_list|(
name|files
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
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
literal|"direct:start"
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
literal|"?usePersist=true"
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
block|}
end_class

end_unit

