begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.snmp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|snmp
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
name|support
operator|.
name|DefaultConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|CommandResponder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|CommandResponderEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|MessageException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|PDU
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|Snmp
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|TransportMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|mp
operator|.
name|StateReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|mp
operator|.
name|StatusInformation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|smi
operator|.
name|Address
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|smi
operator|.
name|GenericAddress
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|smi
operator|.
name|TcpAddress
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|smi
operator|.
name|UdpAddress
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|transport
operator|.
name|DefaultTcpTransportMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|transport
operator|.
name|DefaultUdpTransportMapping
import|;
end_import

begin_class
DECL|class|SnmpTrapConsumer
specifier|public
class|class
name|SnmpTrapConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|CommandResponder
block|{
DECL|field|endpoint
specifier|private
name|SnmpEndpoint
name|endpoint
decl_stmt|;
DECL|field|listenGenericAddress
specifier|private
name|Address
name|listenGenericAddress
decl_stmt|;
DECL|field|snmp
specifier|private
name|Snmp
name|snmp
decl_stmt|;
DECL|field|transport
specifier|private
name|TransportMapping
argument_list|<
name|?
extends|extends
name|Address
argument_list|>
name|transport
decl_stmt|;
DECL|method|SnmpTrapConsumer (SnmpEndpoint endpoint, Processor processor)
specifier|public
name|SnmpTrapConsumer
parameter_list|(
name|SnmpEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
comment|// load connection data only if the endpoint is enabled
if|if
condition|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Starting trap consumer on {}"
argument_list|,
name|this
operator|.
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|listenGenericAddress
operator|=
name|GenericAddress
operator|.
name|parse
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
comment|// either tcp or udp
if|if
condition|(
literal|"tcp"
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
name|this
operator|.
name|transport
operator|=
operator|new
name|DefaultTcpTransportMapping
argument_list|(
operator|(
name|TcpAddress
operator|)
name|this
operator|.
name|listenGenericAddress
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"udp"
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
name|this
operator|.
name|transport
operator|=
operator|new
name|DefaultUdpTransportMapping
argument_list|(
operator|(
name|UdpAddress
operator|)
name|this
operator|.
name|listenGenericAddress
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown protocol: "
operator|+
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
throw|;
block|}
name|this
operator|.
name|snmp
operator|=
operator|new
name|Snmp
argument_list|(
name|transport
argument_list|)
expr_stmt|;
name|this
operator|.
name|snmp
operator|.
name|addCommandResponder
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// listen to the transport
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Starting trap consumer on {} using {} protocol"
argument_list|,
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|transport
operator|.
name|listen
argument_list|()
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Started trap consumer on {} using {} protocol"
argument_list|,
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// stop listening to the transport
if|if
condition|(
name|this
operator|.
name|transport
operator|!=
literal|null
operator|&&
name|this
operator|.
name|transport
operator|.
name|isListening
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Stopping trap consumer on {}"
argument_list|,
name|this
operator|.
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|transport
operator|.
name|close
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Stopped trap consumer on {}"
argument_list|,
name|this
operator|.
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|processPdu (CommandResponderEvent event)
specifier|public
name|void
name|processPdu
parameter_list|(
name|CommandResponderEvent
name|event
parameter_list|)
block|{
name|PDU
name|pdu
init|=
name|event
operator|.
name|getPDU
argument_list|()
decl_stmt|;
comment|// check PDU not null
if|if
condition|(
name|pdu
operator|!=
literal|null
condition|)
block|{
comment|// check for INFORM
comment|// code take from the book "Essential SNMP"
if|if
condition|(
operator|(
name|pdu
operator|.
name|getType
argument_list|()
operator|!=
name|PDU
operator|.
name|TRAP
operator|)
operator|&&
operator|(
name|pdu
operator|.
name|getType
argument_list|()
operator|!=
name|PDU
operator|.
name|V1TRAP
operator|)
operator|&&
operator|(
name|pdu
operator|.
name|getType
argument_list|()
operator|!=
name|PDU
operator|.
name|REPORT
operator|)
operator|&&
operator|(
name|pdu
operator|.
name|getType
argument_list|()
operator|!=
name|PDU
operator|.
name|RESPONSE
operator|)
condition|)
block|{
comment|// first response the inform-message and then process the
comment|// message
name|pdu
operator|.
name|setErrorIndex
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|pdu
operator|.
name|setErrorStatus
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|pdu
operator|.
name|setType
argument_list|(
name|PDU
operator|.
name|RESPONSE
argument_list|)
expr_stmt|;
name|StatusInformation
name|statusInformation
init|=
operator|new
name|StatusInformation
argument_list|()
decl_stmt|;
name|StateReference
name|ref
init|=
name|event
operator|.
name|getStateReference
argument_list|()
decl_stmt|;
try|try
block|{
name|event
operator|.
name|getMessageDispatcher
argument_list|()
operator|.
name|returnResponsePdu
argument_list|(
name|event
operator|.
name|getMessageProcessingModel
argument_list|()
argument_list|,
name|event
operator|.
name|getSecurityModel
argument_list|()
argument_list|,
name|event
operator|.
name|getSecurityName
argument_list|()
argument_list|,
name|event
operator|.
name|getSecurityLevel
argument_list|()
argument_list|,
name|pdu
argument_list|,
name|event
operator|.
name|getMaxSizeResponsePDU
argument_list|()
argument_list|,
name|ref
argument_list|,
name|statusInformation
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"response to INFORM sent"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MessageException
name|ex
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
name|processPDU
argument_list|(
name|pdu
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Received invalid trap PDU"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processPDU (PDU pdu, CommandResponderEvent event)
specifier|public
name|void
name|processPDU
parameter_list|(
name|PDU
name|pdu
parameter_list|,
name|CommandResponderEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Received trap event for {} : {}"
argument_list|,
name|this
operator|.
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|,
name|pdu
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|pdu
argument_list|,
name|event
argument_list|)
decl_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

