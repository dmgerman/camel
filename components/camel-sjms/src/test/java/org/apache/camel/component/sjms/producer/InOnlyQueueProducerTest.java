begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|producer
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageConsumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TextMessage
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
name|component
operator|.
name|sjms
operator|.
name|jms
operator|.
name|JmsObjectFactory
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
name|sjms
operator|.
name|support
operator|.
name|JmsTestSupport
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
DECL|class|InOnlyQueueProducerTest
specifier|public
class|class
name|InOnlyQueueProducerTest
extends|extends
name|JmsTestSupport
block|{
DECL|field|TEST_DESTINATION_NAME
specifier|private
specifier|static
specifier|final
name|String
name|TEST_DESTINATION_NAME
init|=
literal|"sync.queue.producer.test"
decl_stmt|;
DECL|method|InOnlyQueueProducerTest ()
specifier|public
name|InOnlyQueueProducerTest
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyQueueProducer ()
specifier|public
name|void
name|testInOnlyQueueProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageConsumer
name|mc
init|=
name|JmsObjectFactory
operator|.
name|createQueueConsumer
argument_list|(
name|getSession
argument_list|()
argument_list|,
name|TEST_DESTINATION_NAME
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mc
argument_list|)
expr_stmt|;
specifier|final
name|String
name|expectedBody
init|=
literal|"Hello World!"
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
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|mc
operator|.
name|receive
argument_list|(
literal|5000
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|instanceof
name|TextMessage
argument_list|)
expr_stmt|;
name|TextMessage
name|tm
init|=
operator|(
name|TextMessage
operator|)
name|message
decl_stmt|;
name|String
name|text
init|=
name|tm
operator|.
name|getText
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:finish"
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * @see org.apache.camel.test.junit4.CamelTestSupport#createRouteBuilder()      *      * @return      * @throws Exception      */
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
literal|"sjms:queue:"
operator|+
name|TEST_DESTINATION_NAME
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:finish"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:test.log.1?showBody=true"
argument_list|,
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

