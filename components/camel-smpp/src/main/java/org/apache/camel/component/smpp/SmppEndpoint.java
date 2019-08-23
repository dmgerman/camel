begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|support
operator|.
name|DefaultEndpoint
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

begin_comment
comment|/**  * To send and receive SMS using a SMSC (Short Message Service Center).  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.2.0"
argument_list|,
name|scheme
operator|=
literal|"smpp,smpps"
argument_list|,
name|title
operator|=
literal|"SMPP"
argument_list|,
name|syntax
operator|=
literal|"smpp:host:port"
argument_list|,
name|label
operator|=
literal|"mobile"
argument_list|,
name|lenientProperties
operator|=
literal|true
argument_list|)
DECL|class|SmppEndpoint
specifier|public
class|class
name|SmppEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|binding
specifier|private
name|SmppBinding
name|binding
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|SmppConfiguration
name|configuration
decl_stmt|;
DECL|method|SmppEndpoint (String endpointUri, Component component, SmppConfiguration configuration)
specifier|public
name|SmppEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|SmppConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
name|getConnectionString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|SmppConsumer
name|answer
init|=
operator|new
name|SmppConsumer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SmppProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
comment|/**      * Create a new exchange for communicating with this endpoint from a SMSC      *      * @param alertNotification the received message from the SMSC      * @return a new exchange      */
DECL|method|createOnAcceptAlertNotificationExchange (AlertNotification alertNotification)
specifier|public
name|Exchange
name|createOnAcceptAlertNotificationExchange
parameter_list|(
name|AlertNotification
name|alertNotification
parameter_list|)
block|{
return|return
name|createOnAcceptAlertNotificationExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|,
name|alertNotification
argument_list|)
return|;
block|}
comment|/**      * Create a new exchange for communicating with this endpoint from a SMSC      * with the specified {@link ExchangePattern} such as whether its going      * to be an {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut} exchange      *      * @param exchangePattern the message exchange pattern for the exchange      * @param alertNotification the received message from the SMSC      * @return a new exchange      */
DECL|method|createOnAcceptAlertNotificationExchange (ExchangePattern exchangePattern, AlertNotification alertNotification)
specifier|public
name|Exchange
name|createOnAcceptAlertNotificationExchange
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|,
name|AlertNotification
name|alertNotification
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|exchangePattern
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|,
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|getBinding
argument_list|()
operator|.
name|createSmppMessage
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|alertNotification
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|/**      * Create a new exchange for communicating with this endpoint from a SMSC      *      * @param deliverSm the received message from the SMSC      * @return a new exchange      */
DECL|method|createOnAcceptDeliverSmExchange (DeliverSm deliverSm)
specifier|public
name|Exchange
name|createOnAcceptDeliverSmExchange
parameter_list|(
name|DeliverSm
name|deliverSm
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createOnAcceptDeliverSmExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|,
name|deliverSm
argument_list|)
return|;
block|}
comment|/**      * Create a new exchange for communicating with this endpoint from a SMSC      * with the specified {@link ExchangePattern} such as whether its going      * to be an {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut} exchange      *      * @param exchangePattern the message exchange pattern for the exchange      * @param deliverSm the received message from the SMSC      * @return a new exchange      */
DECL|method|createOnAcceptDeliverSmExchange (ExchangePattern exchangePattern, DeliverSm deliverSm)
specifier|public
name|Exchange
name|createOnAcceptDeliverSmExchange
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|,
name|DeliverSm
name|deliverSm
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|exchangePattern
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|,
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|getBinding
argument_list|()
operator|.
name|createSmppMessage
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|deliverSm
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|/**      * Create a new exchange for communicating with this endpoint from a SMSC      *      * @param dataSm the received message from the SMSC      * @param smppMessageId the smpp message id which will be used in the response      * @return a new exchange      */
DECL|method|createOnAcceptDataSm (DataSm dataSm, String smppMessageId)
specifier|public
name|Exchange
name|createOnAcceptDataSm
parameter_list|(
name|DataSm
name|dataSm
parameter_list|,
name|String
name|smppMessageId
parameter_list|)
block|{
return|return
name|createOnAcceptDataSm
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|,
name|dataSm
argument_list|,
name|smppMessageId
argument_list|)
return|;
block|}
comment|/**      * Create a new exchange for communicating with this endpoint from a SMSC      * with the specified {@link ExchangePattern} such as whether its going      * to be an {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut} exchange      *      * @param exchangePattern the message exchange pattern for the exchange      * @param dataSm the received message from the SMSC      * @param smppMessageId the smpp message id which will be used in the response      * @return a new exchange      */
DECL|method|createOnAcceptDataSm (ExchangePattern exchangePattern, DataSm dataSm, String smppMessageId)
specifier|public
name|Exchange
name|createOnAcceptDataSm
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|,
name|DataSm
name|dataSm
parameter_list|,
name|String
name|smppMessageId
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|exchangePattern
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|,
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|getBinding
argument_list|()
operator|.
name|createSmppMessage
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|dataSm
argument_list|,
name|smppMessageId
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|/**      * Returns the connection string for the current connection which has the form:      * smpp://<user>@<host>:<port>      *       * @return the connection string      */
DECL|method|getConnectionString ()
specifier|public
name|String
name|getConnectionString
parameter_list|()
block|{
return|return
operator|(
name|configuration
operator|.
name|isUsingSSL
argument_list|()
condition|?
literal|"smpps://"
else|:
literal|"smpp://"
operator|)
operator|+
operator|(
name|getConfiguration
argument_list|()
operator|.
name|getSystemId
argument_list|()
operator|!=
literal|null
condition|?
name|getConfiguration
argument_list|()
operator|.
name|getSystemId
argument_list|()
operator|+
literal|"@"
else|:
literal|""
operator|)
operator|+
name|getConfiguration
argument_list|()
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
return|;
block|}
comment|/**      * Returns the smpp configuration      *       * @return the configuration      */
DECL|method|getConfiguration ()
specifier|public
name|SmppConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getBinding ()
specifier|public
name|SmppBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|SmppBinding
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
DECL|method|setBinding (SmppBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|SmppBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
block|}
end_class

end_unit

