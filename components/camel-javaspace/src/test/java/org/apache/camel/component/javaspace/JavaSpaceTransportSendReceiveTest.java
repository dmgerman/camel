begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.javaspace
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|javaspace
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
name|CountDownLatch
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
name|Endpoint
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
name|ExchangePattern
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
name|Message
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
name|Producer
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
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JavaSpaceTransportSendReceiveTest
specifier|public
class|class
name|JavaSpaceTransportSendReceiveTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|spring
specifier|private
name|ClassPathXmlApplicationContext
name|spring
decl_stmt|;
DECL|field|countLatch
specifier|private
name|CountDownLatch
name|countLatch
decl_stmt|;
annotation|@
name|Test
DECL|method|testJavaSpaceTransportSendReceive ()
specifier|public
name|void
name|testJavaSpaceTransportSendReceive
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|directEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:input"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|directEndpoint
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
literal|"DAVID"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|directEndpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|int
name|nummsg
init|=
literal|1
decl_stmt|;
name|countLatch
operator|=
operator|new
name|CountDownLatch
argument_list|(
name|nummsg
argument_list|)
expr_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
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
name|nummsg
condition|;
operator|++
name|i
control|)
block|{
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
name|countLatch
operator|.
name|await
argument_list|()
expr_stmt|;
name|long
name|stop
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"{} took {} milliseconds"
argument_list|,
name|getTestMethodName
argument_list|()
argument_list|,
name|stop
operator|-
name|start
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
name|spring
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/javaspace/spring.xml"
argument_list|)
expr_stmt|;
name|SpringCamelContext
name|ctx
init|=
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|spring
argument_list|)
decl_stmt|;
return|return
name|ctx
return|;
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:input"
argument_list|)
operator|.
name|to
argument_list|(
literal|"javaspace:jini://localhost?spaceName=mySpace"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"javaspace:jini://localhost?spaceName=mySpace&verb=take&concurrentConsumers=2&transactional=false"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exc
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|body
init|=
name|exc
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|new
name|String
argument_list|(
name|body
argument_list|)
operator|.
name|equals
argument_list|(
literal|"DAVID"
argument_list|)
argument_list|)
expr_stmt|;
name|countLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

