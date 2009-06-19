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
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|Map
import|;
end_import

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
name|TypeConverter
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
name|JmsConstants
operator|.
name|JMS_MESSAGE_TYPE
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsMessageTypeTest
specifier|public
class|class
name|JmsMessageTypeTest
extends|extends
name|CamelTestSupport
block|{
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
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addTypeConverter
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|MyFooBean
operator|.
name|class
argument_list|,
operator|new
name|MyFooBean
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addTypeConverter
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|MyFooBean
operator|.
name|class
argument_list|,
operator|new
name|MyFooBean
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addTypeConverter
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|MyFooBean
operator|.
name|class
argument_list|,
operator|new
name|MyFooBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
annotation|@
name|Test
DECL|method|testHeaderTextType ()
specifier|public
name|void
name|testHeaderTextType
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// we use Text type then it should be a String
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:foo"
argument_list|,
operator|new
name|MyFooBean
argument_list|(
literal|"World"
argument_list|)
argument_list|,
name|JMS_MESSAGE_TYPE
argument_list|,
literal|"Text"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertTextType ()
specifier|public
name|void
name|testConvertTextType
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// we use Text type then it should be a String
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// we send an object and fore it to use Text type
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:text"
argument_list|,
operator|new
name|MyFooBean
argument_list|(
literal|"World"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTextType ()
specifier|public
name|void
name|testTextType
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// we use Text type then it should be a String
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// we send an object and fore it to use Text type
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:text"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHeaderBytesType ()
specifier|public
name|void
name|testHeaderBytesType
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
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:foo"
argument_list|,
operator|new
name|MyFooBean
argument_list|(
literal|"World"
argument_list|)
argument_list|,
name|JMS_MESSAGE_TYPE
argument_list|,
literal|"Bytes"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertBytesType ()
specifier|public
name|void
name|testConvertBytesType
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
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
comment|// we send an object and fore it to use Bytes type
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bytes"
argument_list|,
operator|new
name|MyFooBean
argument_list|(
literal|"World"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBytesType ()
specifier|public
name|void
name|testBytesType
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
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
comment|// we send an object and fore it to use Text type
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bytes"
argument_list|,
literal|"Bye World"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHeaderMapType ()
specifier|public
name|void
name|testHeaderMapType
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
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:foo"
argument_list|,
operator|new
name|MyFooBean
argument_list|(
literal|"Claus"
argument_list|)
argument_list|,
name|JMS_MESSAGE_TYPE
argument_list|,
literal|"Map"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|mock
operator|.
name|getExchanges
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
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertMapType ()
specifier|public
name|void
name|testConvertMapType
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
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:map"
argument_list|,
operator|new
name|MyFooBean
argument_list|(
literal|"Claus"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|mock
operator|.
name|getExchanges
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
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMapType ()
specifier|public
name|void
name|testMapType
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
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|)
expr_stmt|;
name|Map
name|body
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"Claus"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:map"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|mock
operator|.
name|getExchanges
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
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHeaderObjectType ()
specifier|public
name|void
name|testHeaderObjectType
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
comment|// we use Text type then it should be a String
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|MyFooBean
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// we send an object and fore it to use Text type
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:foo"
argument_list|,
operator|new
name|MyFooBean
argument_list|(
literal|"James"
argument_list|)
argument_list|,
name|JMS_MESSAGE_TYPE
argument_list|,
literal|"Object"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|mock
operator|.
name|getExchanges
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
name|getBody
argument_list|(
name|MyFooBean
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testObjectType ()
specifier|public
name|void
name|testObjectType
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
comment|// we use Text type then it should be a String
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|MyFooBean
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// we send an object and fore it to use Text type
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:object"
argument_list|,
operator|new
name|MyFooBean
argument_list|(
literal|"James"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|mock
operator|.
name|getExchanges
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
name|getBody
argument_list|(
name|MyFooBean
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
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
literal|"direct:text"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jms:queue:foo?jmsMessageType=Text"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bytes"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jms:queue:foo?jmsMessageType=Bytes"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:map"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jms:queue:foo?jmsMessageType=Map"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:object"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jms:queue:foo?jmsMessageType=Object"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jms:queue:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jms:queue:foo"
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
DECL|class|MyFooBean
specifier|public
specifier|static
specifier|final
class|class
name|MyFooBean
implements|implements
name|TypeConverter
implements|,
name|Serializable
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|method|MyFooBean ()
specifier|private
name|MyFooBean
parameter_list|()
block|{         }
DECL|method|MyFooBean (String name)
specifier|private
name|MyFooBean
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|convertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|String
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
call|(
name|T
call|)
argument_list|(
literal|"Hello "
operator|+
operator|(
operator|(
name|MyFooBean
operator|)
name|value
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
condition|)
block|{
return|return
call|(
name|T
call|)
argument_list|(
literal|"Bye "
operator|+
operator|(
operator|(
name|MyFooBean
operator|)
name|value
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getBytes
argument_list|()
return|;
block|}
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|Map
operator|.
name|class
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
operator|(
operator|(
name|MyFooBean
operator|)
name|value
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
name|T
operator|)
name|map
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|convertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|mandatoryConvertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|mandatoryConvertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

