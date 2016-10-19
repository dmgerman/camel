begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.smpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|smpp
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Component
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
name|Consumer
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
name|jsmpp
operator|.
name|bean
operator|.
name|AlertNotification
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|DataSm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|DeliverSm
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

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createMock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|isA
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * JUnit test class for<code>org.apache.camel.component.smpp.SmppEndpoint</code>  *   * @version   */
end_comment

begin_class
DECL|class|SmppEndpointTest
specifier|public
class|class
name|SmppEndpointTest
block|{
DECL|field|endpoint
specifier|private
name|SmppEndpoint
name|endpoint
decl_stmt|;
DECL|field|configuration
specifier|private
name|SmppConfiguration
name|configuration
decl_stmt|;
DECL|field|component
specifier|private
name|Component
name|component
decl_stmt|;
DECL|field|binding
specifier|private
name|SmppBinding
name|binding
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|configuration
operator|=
name|createMock
argument_list|(
name|SmppConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|component
operator|=
name|createMock
argument_list|(
name|Component
operator|.
name|class
argument_list|)
expr_stmt|;
name|binding
operator|=
name|createMock
argument_list|(
name|SmppBinding
operator|.
name|class
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|component
operator|.
name|createConfiguration
argument_list|(
literal|"smpp://smppclient@localhost:2775"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|component
operator|.
name|getCamelContext
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|component
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|SmppEndpoint
argument_list|(
literal|"smpp://smppclient@localhost:2775"
argument_list|,
name|component
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isLenientPropertiesShouldReturnTrue ()
specifier|public
name|void
name|isLenientPropertiesShouldReturnTrue
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|isLenientProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isSingletonShouldReturnTrue ()
specifier|public
name|void
name|isSingletonShouldReturnTrue
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|isSingleton
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointUriShouldReturnTheEndpointUri ()
specifier|public
name|void
name|createEndpointUriShouldReturnTheEndpointUri
parameter_list|()
block|{
name|expect
argument_list|(
name|configuration
operator|.
name|getUsingSSL
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configuration
operator|.
name|getSystemId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"smppclient"
argument_list|)
operator|.
name|times
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2775
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"smpp://smppclient@localhost:2775"
argument_list|,
name|endpoint
operator|.
name|createEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointUriWithoutUserShouldReturnTheEndpointUri ()
specifier|public
name|void
name|createEndpointUriWithoutUserShouldReturnTheEndpointUri
parameter_list|()
block|{
name|expect
argument_list|(
name|configuration
operator|.
name|getUsingSSL
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configuration
operator|.
name|getSystemId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2775
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"smpp://localhost:2775"
argument_list|,
name|endpoint
operator|.
name|createEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createConsumerShouldReturnASmppConsumer ()
specifier|public
name|void
name|createConsumerShouldReturnASmppConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|Processor
name|processor
init|=
name|createMock
argument_list|(
name|Processor
operator|.
name|class
argument_list|)
decl_stmt|;
name|replay
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|Consumer
name|consumer
init|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|consumer
operator|instanceof
name|SmppConsumer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createProducerShouldReturnASmppProducer ()
specifier|public
name|void
name|createProducerShouldReturnASmppProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|producer
operator|instanceof
name|SmppProducer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createOnAcceptAlertNotificationExchange ()
specifier|public
name|void
name|createOnAcceptAlertNotificationExchange
parameter_list|()
block|{
name|AlertNotification
name|alertNotification
init|=
name|createMock
argument_list|(
name|AlertNotification
operator|.
name|class
argument_list|)
decl_stmt|;
name|SmppMessage
name|message
init|=
name|createMock
argument_list|(
name|SmppMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|binding
operator|.
name|createSmppMessage
argument_list|(
name|alertNotification
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|isA
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|alertNotification
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnAcceptAlertNotificationExchange
argument_list|(
name|alertNotification
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|alertNotification
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|binding
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|message
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createOnAcceptAlertNotificationExchangeWithExchangePattern ()
specifier|public
name|void
name|createOnAcceptAlertNotificationExchangeWithExchangePattern
parameter_list|()
block|{
name|AlertNotification
name|alertNotification
init|=
name|createMock
argument_list|(
name|AlertNotification
operator|.
name|class
argument_list|)
decl_stmt|;
name|SmppMessage
name|message
init|=
name|createMock
argument_list|(
name|SmppMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|binding
operator|.
name|createSmppMessage
argument_list|(
name|alertNotification
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|isA
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|alertNotification
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnAcceptAlertNotificationExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|alertNotification
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|alertNotification
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|binding
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|message
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createOnAcceptDeliverSmExchange ()
specifier|public
name|void
name|createOnAcceptDeliverSmExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|DeliverSm
name|deliverSm
init|=
name|createMock
argument_list|(
name|DeliverSm
operator|.
name|class
argument_list|)
decl_stmt|;
name|SmppMessage
name|message
init|=
name|createMock
argument_list|(
name|SmppMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|binding
operator|.
name|createSmppMessage
argument_list|(
name|deliverSm
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|isA
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|deliverSm
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnAcceptDeliverSmExchange
argument_list|(
name|deliverSm
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|deliverSm
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|binding
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|message
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createOnAcceptDeliverSmWithExchangePattern ()
specifier|public
name|void
name|createOnAcceptDeliverSmWithExchangePattern
parameter_list|()
throws|throws
name|Exception
block|{
name|DeliverSm
name|deliverSm
init|=
name|createMock
argument_list|(
name|DeliverSm
operator|.
name|class
argument_list|)
decl_stmt|;
name|SmppMessage
name|message
init|=
name|createMock
argument_list|(
name|SmppMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|binding
operator|.
name|createSmppMessage
argument_list|(
name|deliverSm
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|isA
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|deliverSm
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnAcceptDeliverSmExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|deliverSm
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|deliverSm
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|binding
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|message
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createOnAcceptDataSm ()
specifier|public
name|void
name|createOnAcceptDataSm
parameter_list|()
throws|throws
name|Exception
block|{
name|DataSm
name|dataSm
init|=
name|createMock
argument_list|(
name|DataSm
operator|.
name|class
argument_list|)
decl_stmt|;
name|SmppMessage
name|message
init|=
name|createMock
argument_list|(
name|SmppMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|binding
operator|.
name|createSmppMessage
argument_list|(
name|eq
argument_list|(
name|dataSm
argument_list|)
argument_list|,
name|isA
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|isA
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|dataSm
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnAcceptDataSm
argument_list|(
name|dataSm
argument_list|,
literal|"1"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|dataSm
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|binding
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|message
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createOnAcceptDataSmWithExchangePattern ()
specifier|public
name|void
name|createOnAcceptDataSmWithExchangePattern
parameter_list|()
throws|throws
name|Exception
block|{
name|DataSm
name|dataSm
init|=
name|createMock
argument_list|(
name|DataSm
operator|.
name|class
argument_list|)
decl_stmt|;
name|SmppMessage
name|message
init|=
name|createMock
argument_list|(
name|SmppMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|binding
operator|.
name|createSmppMessage
argument_list|(
name|eq
argument_list|(
name|dataSm
argument_list|)
argument_list|,
name|isA
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|isA
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|dataSm
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createOnAcceptDataSm
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|dataSm
argument_list|,
literal|"1"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|dataSm
argument_list|,
name|binding
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|binding
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|message
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getConnectionStringShouldReturnTheConnectionString ()
specifier|public
name|void
name|getConnectionStringShouldReturnTheConnectionString
parameter_list|()
block|{
name|expect
argument_list|(
name|configuration
operator|.
name|getUsingSSL
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configuration
operator|.
name|getSystemId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"smppclient"
argument_list|)
operator|.
name|times
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2775
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"smpp://smppclient@localhost:2775"
argument_list|,
name|endpoint
operator|.
name|getConnectionString
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getConnectionStringWithoutUserShouldReturnTheConnectionString ()
specifier|public
name|void
name|getConnectionStringWithoutUserShouldReturnTheConnectionString
parameter_list|()
block|{
name|expect
argument_list|(
name|configuration
operator|.
name|getUsingSSL
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configuration
operator|.
name|getSystemId
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2775
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"smpp://localhost:2775"
argument_list|,
name|endpoint
operator|.
name|getConnectionString
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getConfigurationShouldReturnTheSetValue ()
specifier|public
name|void
name|getConfigurationShouldReturnTheSetValue
parameter_list|()
block|{
name|assertSame
argument_list|(
name|configuration
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

