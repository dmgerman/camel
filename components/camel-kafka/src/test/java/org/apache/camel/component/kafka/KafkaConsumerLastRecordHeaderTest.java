begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
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
name|Properties
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
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|ProducerConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|ProducerRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_class
DECL|class|KafkaConsumerLastRecordHeaderTest
specifier|public
class|class
name|KafkaConsumerLastRecordHeaderTest
extends|extends
name|BaseEmbeddedKafkaTest
block|{
DECL|field|TOPIC
specifier|private
specifier|static
specifier|final
name|String
name|TOPIC
init|=
literal|"last-record"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
DECL|field|producer
specifier|private
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|KafkaProducer
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|producer
decl_stmt|;
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
block|{
name|Properties
name|props
init|=
name|getDefaultProperties
argument_list|()
decl_stmt|;
name|producer
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|KafkaProducer
argument_list|<>
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|after ()
specifier|public
name|void
name|after
parameter_list|()
block|{
if|if
condition|(
name|producer
operator|!=
literal|null
condition|)
block|{
name|producer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * When consuming data with autoCommitEnable=false      * Then the LAST_RECORD_BEFORE_COMMIT header must be always defined      * And it should be true only for the last one      */
annotation|@
name|Test
DECL|method|shouldStartFromBeginningWithEmptyOffsetRepository ()
specifier|public
name|void
name|shouldStartFromBeginningWithEmptyOffsetRepository
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"message-0"
argument_list|,
literal|"message-1"
argument_list|,
literal|"message-2"
argument_list|,
literal|"message-3"
argument_list|,
literal|"message-4"
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|producer
operator|.
name|send
argument_list|(
operator|new
name|ProducerRecord
argument_list|<>
argument_list|(
name|TOPIC
argument_list|,
literal|"1"
argument_list|,
literal|"message-"
operator|+
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|assertIsSatisfied
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|result
operator|.
name|getExchanges
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
name|exchanges
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Boolean
name|header
init|=
name|exchanges
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|LAST_RECORD_BEFORE_COMMIT
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Header not set for #"
operator|+
name|i
argument_list|,
name|header
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Header invalid for #"
operator|+
name|i
argument_list|,
name|header
argument_list|,
name|i
operator|==
name|exchanges
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"kafka:"
operator|+
name|TOPIC
operator|+
literal|"?groupId=A&autoOffsetReset=earliest&autoCommitEnable=false"
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

