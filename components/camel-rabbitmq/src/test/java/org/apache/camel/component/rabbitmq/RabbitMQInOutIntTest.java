begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|List
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
name|rabbitmq
operator|.
name|testbeans
operator|.
name|TestNonSerializableObject
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
name|rabbitmq
operator|.
name|testbeans
operator|.
name|TestPartiallySerializableObject
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
name|rabbitmq
operator|.
name|testbeans
operator|.
name|TestSerializableObject
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
name|impl
operator|.
name|JndiRegistry
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
DECL|class|RabbitMQInOutIntTest
specifier|public
class|class
name|RabbitMQInOutIntTest
extends|extends
name|AbstractRabbitMQIntTest
block|{
DECL|field|ROUTING_KEY
specifier|public
specifier|static
specifier|final
name|String
name|ROUTING_KEY
init|=
literal|"rk5"
decl_stmt|;
DECL|field|TIMEOUT_MS
specifier|public
specifier|static
specifier|final
name|long
name|TIMEOUT_MS
init|=
literal|2000
decl_stmt|;
DECL|field|EXCHANGE
specifier|private
specifier|static
specifier|final
name|String
name|EXCHANGE
init|=
literal|"ex5"
decl_stmt|;
DECL|field|EXCHANGE_NO_ACK
specifier|private
specifier|static
specifier|final
name|String
name|EXCHANGE_NO_ACK
init|=
literal|"ex5.noAutoAck"
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:rabbitMQ"
argument_list|)
DECL|field|directProducer
specifier|protected
name|ProducerTemplate
name|directProducer
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"rabbitmq:localhost:5672/"
operator|+
name|EXCHANGE
operator|+
literal|"?threadPoolSize=1&exchangeType=direct&username=cameltest&password=cameltest"
operator|+
literal|"&autoAck=true&queue=q4&routingKey="
operator|+
name|ROUTING_KEY
operator|+
literal|"&transferException=true&requestTimeout="
operator|+
name|TIMEOUT_MS
argument_list|)
DECL|field|rabbitMQEndpoint
specifier|private
name|Endpoint
name|rabbitMQEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"rabbitmq:localhost:5672/"
operator|+
name|EXCHANGE_NO_ACK
operator|+
literal|"?threadPoolSize=1&exchangeType=direct&username=cameltest&password=cameltest"
operator|+
literal|"&autoAck=false&autoDelete=false&durable=false&queue=q5&routingKey="
operator|+
name|ROUTING_KEY
operator|+
literal|"&transferException=true&requestTimeout="
operator|+
name|TIMEOUT_MS
operator|+
literal|"&args=#args"
argument_list|)
DECL|field|noAutoAckEndpoint
specifier|private
name|Endpoint
name|noAutoAckEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|private
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|args
operator|.
name|put
argument_list|(
literal|"queue.x-expires"
argument_list|,
literal|60000
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"args"
argument_list|,
name|args
argument_list|)
expr_stmt|;
return|return
name|jndi
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
literal|"direct:rabbitMQ"
argument_list|)
operator|.
name|id
argument_list|(
literal|"producingRoute"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"routeHeader"
argument_list|,
name|simple
argument_list|(
literal|"routeHeader"
argument_list|)
argument_list|)
operator|.
name|inOut
argument_list|(
name|rabbitMQEndpoint
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|rabbitMQEndpoint
argument_list|)
operator|.
name|id
argument_list|(
literal|"consumingRoute"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Receiving message"
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
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|TestSerializableObject
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|TestSerializableObject
name|foo
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|TestSerializableObject
operator|.
name|class
argument_list|)
decl_stmt|;
name|foo
operator|.
name|setDescription
argument_list|(
literal|"foobar"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|TestPartiallySerializableObject
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|TestPartiallySerializableObject
name|foo
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|TestPartiallySerializableObject
operator|.
name|class
argument_list|)
decl_stmt|;
name|foo
operator|.
name|setNonSerializableObject
argument_list|(
operator|new
name|TestNonSerializableObject
argument_list|()
argument_list|)
expr_stmt|;
name|foo
operator|.
name|setDescription
argument_list|(
literal|"foobar"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"header"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"String"
argument_list|)
argument_list|,
literal|"String"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"routeHeader"
argument_list|)
argument_list|,
literal|"routeHeader"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"Exception"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Boom"
argument_list|)
throw|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"TimeOut"
argument_list|)
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|TIMEOUT_MS
operator|*
literal|2
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|+
literal|" response"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:rabbitMQNoAutoAck"
argument_list|)
operator|.
name|id
argument_list|(
literal|"producingRouteNoAutoAck"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"routeHeader"
argument_list|,
name|simple
argument_list|(
literal|"routeHeader"
argument_list|)
argument_list|)
operator|.
name|inOut
argument_list|(
name|noAutoAckEndpoint
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|noAutoAckEndpoint
argument_list|)
operator|.
name|id
argument_list|(
literal|"consumingRouteNoAutoAck"
argument_list|)
operator|.
name|to
argument_list|(
name|resultEndpoint
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"test exception"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|inOutRaceConditionTest1 ()
specifier|public
name|void
name|inOutRaceConditionTest1
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|String
name|reply
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:rabbitMQ"
argument_list|,
literal|"test1"
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|EXCHANGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test1 response"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|inOutRaceConditionTest2 ()
specifier|public
name|void
name|inOutRaceConditionTest2
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|String
name|reply
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:rabbitMQ"
argument_list|,
literal|"test2"
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|EXCHANGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test2 response"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|headerTest ()
specifier|public
name|void
name|headerTest
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|TestSerializableObject
name|testObject
init|=
operator|new
name|TestSerializableObject
argument_list|()
decl_stmt|;
name|testObject
operator|.
name|setName
argument_list|(
literal|"header"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"String"
argument_list|,
literal|"String"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Boolean"
argument_list|,
operator|new
name|Boolean
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
comment|// This will blow up the connection if not removed before sending the message
name|headers
operator|.
name|put
argument_list|(
literal|"TestObject1"
argument_list|,
name|testObject
argument_list|)
expr_stmt|;
comment|// This will blow up the connection if not removed before sending the message
name|headers
operator|.
name|put
argument_list|(
literal|"class"
argument_list|,
name|testObject
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
comment|// This will mess up de-serialization if not removed before sending the message
name|headers
operator|.
name|put
argument_list|(
literal|"CamelSerialize"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// populate a map and an arrayList
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|tmpMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|tmpList
init|=
operator|new
name|ArrayList
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
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|String
name|name
init|=
literal|"header"
operator|+
name|i
decl_stmt|;
name|tmpList
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|tmpMap
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|// This will blow up the connection if not removed before sending the message
name|headers
operator|.
name|put
argument_list|(
literal|"arrayList"
argument_list|,
name|tmpList
argument_list|)
expr_stmt|;
comment|// This will blow up the connection if not removed before sending the message
name|headers
operator|.
name|put
argument_list|(
literal|"map"
argument_list|,
name|tmpMap
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:rabbitMQ"
argument_list|,
literal|"header"
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"header response"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|serializeTest ()
specifier|public
name|void
name|serializeTest
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|TestSerializableObject
name|foo
init|=
operator|new
name|TestSerializableObject
argument_list|()
decl_stmt|;
name|foo
operator|.
name|setName
argument_list|(
literal|"foobar"
argument_list|)
expr_stmt|;
name|TestSerializableObject
name|reply
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:rabbitMQ"
argument_list|,
name|foo
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|EXCHANGE
argument_list|,
name|TestSerializableObject
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foobar"
argument_list|,
name|reply
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foobar"
argument_list|,
name|reply
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|partiallySerializeTest ()
specifier|public
name|void
name|partiallySerializeTest
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|TestPartiallySerializableObject
name|foo
init|=
operator|new
name|TestPartiallySerializableObject
argument_list|()
decl_stmt|;
name|foo
operator|.
name|setName
argument_list|(
literal|"foobar"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:rabbitMQ"
argument_list|,
name|foo
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|EXCHANGE
argument_list|,
name|TestPartiallySerializableObject
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
comment|// expected
block|}
comment|// Make sure we didn't crash the one Consumer thread
name|String
name|reply2
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:rabbitMQ"
argument_list|,
literal|"partiallySerializeTest1"
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|EXCHANGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"partiallySerializeTest1 response"
argument_list|,
name|reply2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSerializableObject ()
specifier|public
name|void
name|testSerializableObject
parameter_list|()
throws|throws
name|IOException
block|{
name|TestSerializableObject
name|foo
init|=
operator|new
name|TestSerializableObject
argument_list|()
decl_stmt|;
name|foo
operator|.
name|setName
argument_list|(
literal|"foobar"
argument_list|)
expr_stmt|;
name|byte
index|[]
name|body
init|=
literal|null
decl_stmt|;
try|try
init|(
name|ByteArrayOutputStream
name|b
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|;
name|ObjectOutputStream
name|o
operator|=
operator|new
name|ObjectOutputStream
argument_list|(
name|b
argument_list|)
init|;
init|)
block|{
name|o
operator|.
name|writeObject
argument_list|(
name|foo
argument_list|)
expr_stmt|;
name|body
operator|=
name|b
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
name|TestSerializableObject
name|newFoo
init|=
literal|null
decl_stmt|;
try|try
init|(
name|InputStream
name|b
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|body
argument_list|)
init|;
name|ObjectInputStream
name|o
operator|=
operator|new
name|ObjectInputStream
argument_list|(
name|b
argument_list|)
init|;
init|)
block|{
name|newFoo
operator|=
operator|(
name|TestSerializableObject
operator|)
name|o
operator|.
name|readObject
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ClassNotFoundException
name|e
parameter_list|)
block|{         }
name|assertEquals
argument_list|(
name|foo
operator|.
name|getName
argument_list|()
argument_list|,
name|newFoo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|inOutExceptionTest ()
specifier|public
name|void
name|inOutExceptionTest
parameter_list|()
block|{
try|try
block|{
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:rabbitMQ"
argument_list|,
literal|"Exception"
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|EXCHANGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"This should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|,
name|IllegalArgumentException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"This should have caught CamelExecutionException"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|inOutTimeOutTest ()
specifier|public
name|void
name|inOutTimeOutTest
parameter_list|()
block|{
try|try
block|{
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:rabbitMQ"
argument_list|,
literal|"TimeOut"
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|EXCHANGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"This should have thrown a timeOut exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
comment|// expected
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"This should have caught CamelExecutionException"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|inOutNullTest ()
specifier|public
name|void
name|inOutNullTest
parameter_list|()
block|{
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:rabbitMQ"
argument_list|,
literal|null
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|EXCHANGE
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|messageAckOnExceptionWhereNoAutoAckTest ()
specifier|public
name|void
name|messageAckOnExceptionWhereNoAutoAckTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|EXCHANGE_NO_ACK
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|RabbitMQConstants
operator|.
name|ROUTING_KEY
argument_list|,
name|ROUTING_KEY
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:rabbitMQNoAutoAck"
argument_list|,
literal|"testMessage"
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"This should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalStateException
operator|)
condition|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|//On restarting the camel context, if the message was not acknowledged the message would be reprocessed
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

