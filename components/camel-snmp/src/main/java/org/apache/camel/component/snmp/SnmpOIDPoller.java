begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|List
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
name|support
operator|.
name|ScheduledPollConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|CommunityTarget
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
name|ScopedPDU
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
name|Target
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
name|UserTarget
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|event
operator|.
name|ResponseEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|event
operator|.
name|ResponseListener
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
name|MPv3
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
name|SnmpConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|security
operator|.
name|AuthMD5
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|security
operator|.
name|AuthSHA
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|security
operator|.
name|Priv3DES
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|security
operator|.
name|PrivAES128
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|security
operator|.
name|PrivAES192
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|security
operator|.
name|PrivAES256
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|security
operator|.
name|PrivDES
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|security
operator|.
name|SecurityModels
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|security
operator|.
name|SecurityProtocols
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|security
operator|.
name|USM
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|security
operator|.
name|UsmUser
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
name|OID
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
name|OctetString
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
name|VariableBinding
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

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|util
operator|.
name|DefaultPDUFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|util
operator|.
name|TreeEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|util
operator|.
name|TreeUtils
import|;
end_import

begin_class
DECL|class|SnmpOIDPoller
specifier|public
class|class
name|SnmpOIDPoller
extends|extends
name|ScheduledPollConsumer
implements|implements
name|ResponseListener
block|{
DECL|field|targetAddress
specifier|private
name|Address
name|targetAddress
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
DECL|field|snmp
specifier|private
name|Snmp
name|snmp
decl_stmt|;
DECL|field|target
specifier|private
name|Target
name|target
decl_stmt|;
DECL|field|pdu
specifier|private
name|PDU
name|pdu
decl_stmt|;
DECL|field|endpoint
specifier|private
name|SnmpEndpoint
name|endpoint
decl_stmt|;
DECL|method|SnmpOIDPoller (SnmpEndpoint endpoint, Processor processor)
specifier|public
name|SnmpOIDPoller
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
name|this
operator|.
name|targetAddress
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
argument_list|()
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
argument_list|()
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
name|this
operator|.
name|transport
argument_list|)
expr_stmt|;
if|if
condition|(
name|SnmpConstants
operator|.
name|version3
operator|==
name|endpoint
operator|.
name|getSnmpVersion
argument_list|()
condition|)
block|{
name|UserTarget
name|userTarget
init|=
operator|new
name|UserTarget
argument_list|()
decl_stmt|;
name|userTarget
operator|.
name|setSecurityLevel
argument_list|(
name|endpoint
operator|.
name|getSecurityLevel
argument_list|()
argument_list|)
expr_stmt|;
name|userTarget
operator|.
name|setSecurityName
argument_list|(
name|convertToOctetString
argument_list|(
name|endpoint
operator|.
name|getSecurityName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|userTarget
operator|.
name|setAddress
argument_list|(
name|targetAddress
argument_list|)
expr_stmt|;
name|userTarget
operator|.
name|setRetries
argument_list|(
name|endpoint
operator|.
name|getRetries
argument_list|()
argument_list|)
expr_stmt|;
name|userTarget
operator|.
name|setTimeout
argument_list|(
name|endpoint
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|userTarget
operator|.
name|setVersion
argument_list|(
name|endpoint
operator|.
name|getSnmpVersion
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|userTarget
expr_stmt|;
name|USM
name|usm
init|=
operator|new
name|USM
argument_list|(
name|SecurityProtocols
operator|.
name|getInstance
argument_list|()
argument_list|,
operator|new
name|OctetString
argument_list|(
name|MPv3
operator|.
name|createLocalEngineID
argument_list|()
argument_list|)
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|SecurityModels
operator|.
name|getInstance
argument_list|()
operator|.
name|addSecurityModel
argument_list|(
name|usm
argument_list|)
expr_stmt|;
name|OID
name|authProtocol
init|=
name|convertAuthenticationProtocol
argument_list|(
name|endpoint
operator|.
name|getAuthenticationProtocol
argument_list|()
argument_list|)
decl_stmt|;
name|OctetString
name|authPwd
init|=
name|convertToOctetString
argument_list|(
name|endpoint
operator|.
name|getAuthenticationPassphrase
argument_list|()
argument_list|)
decl_stmt|;
name|OID
name|privProtocol
init|=
name|convertPrivacyProtocol
argument_list|(
name|endpoint
operator|.
name|getPrivacyProtocol
argument_list|()
argument_list|)
decl_stmt|;
name|OctetString
name|privPwd
init|=
name|convertToOctetString
argument_list|(
name|endpoint
operator|.
name|getPrivacyPassphrase
argument_list|()
argument_list|)
decl_stmt|;
name|UsmUser
name|user
init|=
operator|new
name|UsmUser
argument_list|(
name|convertToOctetString
argument_list|(
name|endpoint
operator|.
name|getSecurityName
argument_list|()
argument_list|)
argument_list|,
name|authProtocol
argument_list|,
name|authPwd
argument_list|,
name|privProtocol
argument_list|,
name|privPwd
argument_list|)
decl_stmt|;
name|usm
operator|.
name|addUser
argument_list|(
name|convertToOctetString
argument_list|(
name|endpoint
operator|.
name|getSecurityName
argument_list|()
argument_list|)
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|ScopedPDU
name|scopedPDU
init|=
operator|new
name|ScopedPDU
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getSnmpContextEngineId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scopedPDU
operator|.
name|setContextEngineID
argument_list|(
operator|new
name|OctetString
argument_list|(
name|endpoint
operator|.
name|getSnmpContextEngineId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getSnmpContextName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scopedPDU
operator|.
name|setContextName
argument_list|(
operator|new
name|OctetString
argument_list|(
name|endpoint
operator|.
name|getSnmpContextName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|pdu
operator|=
name|scopedPDU
expr_stmt|;
block|}
else|else
block|{
name|CommunityTarget
name|communityTarget
init|=
operator|new
name|CommunityTarget
argument_list|()
decl_stmt|;
name|communityTarget
operator|.
name|setCommunity
argument_list|(
name|convertToOctetString
argument_list|(
name|endpoint
operator|.
name|getSnmpCommunity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|communityTarget
operator|.
name|setAddress
argument_list|(
name|targetAddress
argument_list|)
expr_stmt|;
name|communityTarget
operator|.
name|setRetries
argument_list|(
name|endpoint
operator|.
name|getRetries
argument_list|()
argument_list|)
expr_stmt|;
name|communityTarget
operator|.
name|setTimeout
argument_list|(
name|endpoint
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|communityTarget
operator|.
name|setVersion
argument_list|(
name|endpoint
operator|.
name|getSnmpVersion
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|communityTarget
expr_stmt|;
name|this
operator|.
name|pdu
operator|=
operator|new
name|PDU
argument_list|()
expr_stmt|;
block|}
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
literal|"Starting OID poller on {} using {} protocol"
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
literal|"Started OID poller on {} using {} protocol"
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
name|log
operator|.
name|info
argument_list|(
literal|"Stopping OID poller on {}"
argument_list|,
name|targetAddress
argument_list|)
expr_stmt|;
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
literal|"Stopped OID poller on {}"
argument_list|,
name|targetAddress
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|pdu
operator|.
name|clear
argument_list|()
expr_stmt|;
name|int
name|type
init|=
name|this
operator|.
name|getPduType
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|pdu
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isTreeList
argument_list|()
condition|)
block|{
comment|// prepare the request items
for|for
control|(
name|OID
name|oid
range|:
name|this
operator|.
name|endpoint
operator|.
name|getOids
argument_list|()
control|)
block|{
name|this
operator|.
name|pdu
operator|.
name|add
argument_list|(
operator|new
name|VariableBinding
argument_list|(
name|oid
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|TreeUtils
name|treeUtils
init|=
operator|new
name|TreeUtils
argument_list|(
name|snmp
argument_list|,
operator|new
name|DefaultPDUFactory
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|OID
name|oid
range|:
name|this
operator|.
name|endpoint
operator|.
name|getOids
argument_list|()
control|)
block|{
name|List
name|events
init|=
name|treeUtils
operator|.
name|getSubtree
argument_list|(
name|target
argument_list|,
operator|new
name|OID
argument_list|(
name|oid
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|eventObj
range|:
name|events
control|)
block|{
name|TreeEvent
name|event
init|=
operator|(
name|TreeEvent
operator|)
name|eventObj
decl_stmt|;
if|if
condition|(
name|event
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Event is null"
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|event
operator|.
name|isError
argument_list|()
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error in event: {}"
argument_list|,
name|event
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|VariableBinding
index|[]
name|varBindings
init|=
name|event
operator|.
name|getVariableBindings
argument_list|()
decl_stmt|;
if|if
condition|(
name|varBindings
operator|==
literal|null
operator|||
name|varBindings
operator|.
name|length
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|VariableBinding
name|varBinding
range|:
name|varBindings
control|)
block|{
if|if
condition|(
name|varBinding
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|this
operator|.
name|pdu
operator|.
name|add
argument_list|(
name|varBinding
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// send the request
name|snmp
operator|.
name|send
argument_list|(
name|pdu
argument_list|,
name|target
argument_list|,
literal|null
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|onResponse (ResponseEvent event)
specifier|public
name|void
name|onResponse
parameter_list|(
name|ResponseEvent
name|event
parameter_list|)
block|{
comment|// Always cancel async request when response has been received
comment|// otherwise a memory leak is created! Not canceling a request
comment|// immediately can be useful when sending a request to a broadcast address.
operator|(
operator|(
name|Snmp
operator|)
name|event
operator|.
name|getSource
argument_list|()
operator|)
operator|.
name|cancel
argument_list|(
name|event
operator|.
name|getRequest
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// check for valid response
if|if
condition|(
name|event
operator|.
name|getRequest
argument_list|()
operator|==
literal|null
operator|||
name|event
operator|.
name|getResponse
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// ignore null requests/responses
name|log
operator|.
name|debug
argument_list|(
literal|"Received invalid SNMP event. Request: "
operator|+
name|event
operator|.
name|getRequest
argument_list|()
operator|+
literal|" / Response: "
operator|+
name|event
operator|.
name|getResponse
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|PDU
name|pdu
init|=
name|event
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|processPDU
argument_list|(
name|pdu
argument_list|)
expr_stmt|;
block|}
comment|/**      * processes the pdu message      *       * @param pdu the pdu      */
DECL|method|processPDU (PDU pdu)
specifier|public
name|void
name|processPDU
parameter_list|(
name|PDU
name|pdu
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
literal|"Received response event for {} : {}"
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
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** * @return Returns the target.      */
DECL|method|getTarget ()
specifier|public
name|Target
name|getTarget
parameter_list|()
block|{
return|return
name|this
operator|.
name|target
return|;
block|}
comment|/**      * @param target The target to set.      */
DECL|method|setTarget (Target target)
specifier|public
name|void
name|setTarget
parameter_list|(
name|Target
name|target
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
DECL|method|convertToOctetString (String value)
specifier|private
name|OctetString
name|convertToOctetString
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|OctetString
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|convertAuthenticationProtocol (String authenticationProtocol)
specifier|private
name|OID
name|convertAuthenticationProtocol
parameter_list|(
name|String
name|authenticationProtocol
parameter_list|)
block|{
if|if
condition|(
name|authenticationProtocol
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
literal|"MD5"
operator|.
name|equals
argument_list|(
name|authenticationProtocol
argument_list|)
condition|)
block|{
return|return
name|AuthMD5
operator|.
name|ID
return|;
block|}
elseif|else
if|if
condition|(
literal|"SHA1"
operator|.
name|equals
argument_list|(
name|authenticationProtocol
argument_list|)
condition|)
block|{
return|return
name|AuthSHA
operator|.
name|ID
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown authentication protocol: "
operator|+
name|authenticationProtocol
argument_list|)
throw|;
block|}
block|}
DECL|method|convertPrivacyProtocol (String privacyProtocol)
specifier|private
name|OID
name|convertPrivacyProtocol
parameter_list|(
name|String
name|privacyProtocol
parameter_list|)
block|{
if|if
condition|(
name|privacyProtocol
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
literal|"DES"
operator|.
name|equals
argument_list|(
name|privacyProtocol
argument_list|)
condition|)
block|{
return|return
name|PrivDES
operator|.
name|ID
return|;
block|}
elseif|else
if|if
condition|(
literal|"TRIDES"
operator|.
name|equals
argument_list|(
name|privacyProtocol
argument_list|)
condition|)
block|{
return|return
name|Priv3DES
operator|.
name|ID
return|;
block|}
elseif|else
if|if
condition|(
literal|"AES128"
operator|.
name|equals
argument_list|(
name|privacyProtocol
argument_list|)
condition|)
block|{
return|return
name|PrivAES128
operator|.
name|ID
return|;
block|}
elseif|else
if|if
condition|(
literal|"AES192"
operator|.
name|equals
argument_list|(
name|privacyProtocol
argument_list|)
condition|)
block|{
return|return
name|PrivAES192
operator|.
name|ID
return|;
block|}
elseif|else
if|if
condition|(
literal|"AES256"
operator|.
name|equals
argument_list|(
name|privacyProtocol
argument_list|)
condition|)
block|{
return|return
name|PrivAES256
operator|.
name|ID
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown privacy protocol: "
operator|+
name|privacyProtocol
argument_list|)
throw|;
block|}
block|}
DECL|method|getPduType (SnmpActionType type)
specifier|private
name|int
name|getPduType
parameter_list|(
name|SnmpActionType
name|type
parameter_list|)
block|{
if|if
condition|(
name|SnmpActionType
operator|.
name|GET_NEXT
operator|==
name|type
condition|)
block|{
return|return
name|PDU
operator|.
name|GETNEXT
return|;
block|}
else|else
block|{
return|return
name|PDU
operator|.
name|GET
return|;
block|}
block|}
block|}
end_class

end_unit

