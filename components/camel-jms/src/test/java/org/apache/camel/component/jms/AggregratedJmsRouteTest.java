begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
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
name|ContextTestSupport
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
name|processor
operator|.
name|BatchProcessor
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
name|aggregate
operator|.
name|AggregationStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|JmsComponent
operator|.
name|jmsComponentClientAcknowledge
import|;
end_import

begin_class
DECL|class|AggregratedJmsRouteTest
specifier|public
class|class
name|AggregratedJmsRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|AggregratedJmsRouteTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|timeOutEndpointUri
specifier|private
name|String
name|timeOutEndpointUri
init|=
literal|"jms:queue:test.a"
decl_stmt|;
DECL|field|multicastEndpointUri
specifier|private
name|String
name|multicastEndpointUri
init|=
literal|"jms:queue:multicast"
decl_stmt|;
comment|/*      * negative receive wait timeout for jms is blocking so timeout during processing does not hang      */
DECL|method|testJmsBatchTimeoutExpiryWithAggregrationDelay ()
specifier|public
name|void
name|testJmsBatchTimeoutExpiryWithAggregrationDelay
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|setSleepForEmptyTest
argument_list|(
literal|3
operator|*
name|BatchProcessor
operator|.
name|DEFAULT_BATCH_TIMEOUT
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|String
name|body
init|=
literal|"message:"
operator|+
name|i
decl_stmt|;
name|sendExchange
argument_list|(
name|timeOutEndpointUri
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testJmsMulticastAndAggregration ()
specifier|public
name|void
name|testJmsMulticastAndAggregration
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:reply"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|String
name|body
init|=
literal|"message:"
operator|+
name|i
decl_stmt|;
name|sendExchange
argument_list|(
name|multicastEndpointUri
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|8000
argument_list|)
expr_stmt|;
block|}
DECL|method|sendExchange (String uri, final Object expectedBody)
specifier|protected
name|void
name|sendExchange
parameter_list|(
name|String
name|uri
parameter_list|,
specifier|final
name|Object
name|expectedBody
parameter_list|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
name|expectedBody
argument_list|,
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://localhost?broker.persistent=false"
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"jms"
argument_list|,
name|jmsComponentClientAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|timeOutEndpointUri
argument_list|)
operator|.
name|to
argument_list|(
literal|"jms:queue:test.b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jms:queue:test.b"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"cheese"
argument_list|)
argument_list|,
operator|new
name|AggregationStrategy
argument_list|()
block|{
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|2
operator|*
name|BatchProcessor
operator|.
name|DEFAULT_BATCH_TIMEOUT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"aggregration delay sleep inturrepted"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"aggregration delay sleep inturrepted"
argument_list|)
expr_stmt|;
block|}
return|return
name|newExchange
return|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|multicastEndpointUri
argument_list|)
operator|.
name|to
argument_list|(
literal|"jms:queue:point1"
argument_list|,
literal|"jms:queue:point2"
argument_list|,
literal|"jms:queue:point3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jms:queue:point1"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"jms:queue:reply"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jms:queue:point2"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"jms:queue:reply"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jms:queue:point3"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"jms:queue:reply"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jms:queue:reply"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"cheese"
argument_list|)
argument_list|,
operator|new
name|AggregationStrategy
argument_list|()
block|{
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
name|Exchange
name|copy
init|=
name|newExchange
operator|.
name|copy
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"try to aggregating the message "
argument_list|)
expr_stmt|;
name|Integer
name|old
init|=
operator|(
name|Integer
operator|)
name|oldExchange
operator|.
name|getProperty
argument_list|(
literal|"aggregated"
argument_list|)
decl_stmt|;
if|if
condition|(
name|old
operator|==
literal|null
condition|)
block|{
name|old
operator|=
literal|1
expr_stmt|;
block|}
name|Exchange
name|result
init|=
name|copy
decl_stmt|;
name|result
operator|.
name|setProperty
argument_list|(
literal|"aggregated"
argument_list|,
name|old
operator|+
literal|1
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
argument_list|)
operator|.
name|completedPredicate
argument_list|(
name|header
argument_list|(
literal|"aggregated"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:reply"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyProcessor
specifier|private
class|class
name|MyProcessor
implements|implements
name|Processor
block|{
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
name|LOG
operator|.
name|info
argument_list|(
literal|"get the exchange here "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

