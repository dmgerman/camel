begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.beanstalk.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|beanstalk
operator|.
name|integration
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|surftools
operator|.
name|BeanstalkClient
operator|.
name|Job
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
name|CamelExecutionException
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
name|EndpointInject
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
name|Produce
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
name|beanstalk
operator|.
name|Headers
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|DeleteProducerIntegrationTest
specifier|public
class|class
name|DeleteProducerIntegrationTest
extends|extends
name|BeanstalkCamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"direct:start"
argument_list|)
DECL|field|direct
specifier|protected
name|ProducerTemplate
name|direct
decl_stmt|;
annotation|@
name|Test
DECL|method|testDelete ()
specifier|public
name|void
name|testDelete
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|long
name|jobId
init|=
name|writer
operator|.
name|put
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|5
argument_list|,
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Valid Job Id"
argument_list|,
name|jobId
operator|>
literal|0
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|JOB_ID
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|RESULT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|direct
operator|.
name|sendBodyAndHeader
argument_list|(
literal|null
argument_list|,
name|Headers
operator|.
name|JOB_ID
argument_list|,
name|jobId
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
specifier|final
name|Long
name|messageJobId
init|=
name|resultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Headers
operator|.
name|JOB_ID
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Job ID in message"
argument_list|,
name|messageJobId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Message Job ID equals"
argument_list|,
name|jobId
argument_list|,
name|messageJobId
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Job
name|job
init|=
name|reader
operator|.
name|peek
argument_list|(
name|jobId
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Job has been deleted"
argument_list|,
name|job
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CamelExecutionException
operator|.
name|class
argument_list|)
DECL|method|testNoJobId ()
specifier|public
name|void
name|testNoJobId
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|direct
operator|.
name|sendBody
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertListSize
argument_list|(
literal|"Number of exceptions"
argument_list|,
name|resultEndpoint
operator|.
name|getFailures
argument_list|()
argument_list|,
literal|1
argument_list|)
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
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
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
literal|"beanstalk:"
operator|+
name|tubeName
operator|+
literal|"?command=delete"
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
block|}
end_class

end_unit

