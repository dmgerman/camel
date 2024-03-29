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
name|beanstalk
operator|.
name|Helper
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
DECL|class|ConsumerIntegrationTest
specifier|public
class|class
name|ConsumerIntegrationTest
extends|extends
name|BeanstalkCamelTestSupport
block|{
DECL|field|testMessage
specifier|final
name|String
name|testMessage
init|=
literal|"Hello, world!"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|result
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|Test
DECL|method|testReceive ()
specifier|public
name|void
name|testReceive
parameter_list|()
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
name|long
name|prio
init|=
literal|0
decl_stmt|;
name|int
name|ttr
init|=
literal|10
decl_stmt|;
specifier|final
name|long
name|jobId
init|=
name|writer
operator|.
name|put
argument_list|(
name|prio
argument_list|,
literal|0
argument_list|,
name|ttr
argument_list|,
name|Helper
operator|.
name|stringToBytes
argument_list|(
name|testMessage
argument_list|)
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedHeaderReceived
argument_list|(
name|Headers
operator|.
name|JOB_ID
argument_list|,
name|jobId
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|CREATED_TIMESTAMP
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|JOB_ID
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|jobId
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|PRIORITY
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|prio
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|TUBE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|tubeName
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|STATE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"reserved"
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|AGE
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|TIME_LEFT
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|TIMEOUTS
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|RELEASES
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|BURIES
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|KICKS
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|(
literal|500
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
literal|"beanstalk:"
operator|+
name|tubeName
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

