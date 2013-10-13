begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stomp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stomp
package|;
end_package

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
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|BlockingConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Stomp
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|codec
operator|.
name|StompFrame
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
import|import static
name|org
operator|.
name|fusesource
operator|.
name|hawtbuf
operator|.
name|UTF8Buffer
operator|.
name|utf8
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Constants
operator|.
name|DESTINATION
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Constants
operator|.
name|MESSAGE_ID
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Constants
operator|.
name|SEND
import|;
end_import

begin_class
DECL|class|StompConsumerTest
specifier|public
class|class
name|StompConsumerTest
extends|extends
name|StompBaseTest
block|{
annotation|@
name|Test
DECL|method|testConsume ()
specifier|public
name|void
name|testConsume
parameter_list|()
throws|throws
name|Exception
block|{
name|Stomp
name|stomp
init|=
operator|new
name|Stomp
argument_list|(
literal|"tcp://localhost:"
operator|+
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|BlockingConnection
name|producerConnection
init|=
name|stomp
operator|.
name|connectBlocking
argument_list|()
decl_stmt|;
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
name|expectedMinimumMessageCount
argument_list|(
name|numberOfMessages
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numberOfMessages
condition|;
name|i
operator|++
control|)
block|{
name|StompFrame
name|frame
init|=
operator|new
name|StompFrame
argument_list|(
name|SEND
argument_list|)
decl_stmt|;
name|frame
operator|.
name|addHeader
argument_list|(
name|DESTINATION
argument_list|,
name|StompFrame
operator|.
name|encodeHeader
argument_list|(
literal|"/queue/test"
argument_list|)
argument_list|)
expr_stmt|;
name|frame
operator|.
name|addHeader
argument_list|(
name|MESSAGE_ID
argument_list|,
name|StompFrame
operator|.
name|encodeHeader
argument_list|(
literal|"msg:"
operator|+
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|frame
operator|.
name|content
argument_list|(
name|utf8
argument_list|(
literal|"Important Message "
operator|+
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|producerConnection
operator|.
name|send
argument_list|(
name|frame
argument_list|)
expr_stmt|;
block|}
name|mock
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"stomp:queue:test"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|convertToString
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
block|}
end_class

end_unit

