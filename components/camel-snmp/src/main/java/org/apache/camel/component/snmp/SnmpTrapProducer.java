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
name|impl
operator|.
name|DefaultProducer
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
name|Integer32
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

begin_comment
comment|/**  * A snmp trap producer  */
end_comment

begin_class
DECL|class|SnmpTrapProducer
specifier|public
class|class
name|SnmpTrapProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
name|SnmpEndpoint
name|endpoint
decl_stmt|;
DECL|field|targetAddress
specifier|private
name|Address
name|targetAddress
decl_stmt|;
DECL|field|usm
specifier|private
name|USM
name|usm
decl_stmt|;
DECL|field|target
specifier|private
name|CommunityTarget
name|target
decl_stmt|;
DECL|method|SnmpTrapProducer (SnmpEndpoint endpoint)
specifier|public
name|SnmpTrapProducer
parameter_list|(
name|SnmpEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
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
name|log
operator|.
name|debug
argument_list|(
literal|"targetAddress: {}"
argument_list|,
name|targetAddress
argument_list|)
expr_stmt|;
name|this
operator|.
name|usm
operator|=
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
expr_stmt|;
name|SecurityModels
operator|.
name|getInstance
argument_list|()
operator|.
name|addSecurityModel
argument_list|(
name|this
operator|.
name|usm
argument_list|)
expr_stmt|;
comment|// setting up target
name|this
operator|.
name|target
operator|=
operator|new
name|CommunityTarget
argument_list|()
expr_stmt|;
name|this
operator|.
name|target
operator|.
name|setCommunity
argument_list|(
operator|new
name|OctetString
argument_list|(
name|endpoint
operator|.
name|getSnmpCommunity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|.
name|setAddress
argument_list|(
name|this
operator|.
name|targetAddress
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|.
name|setRetries
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getRetries
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|.
name|setTimeout
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|.
name|setVersion
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getSnmpVersion
argument_list|()
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
try|try
block|{
name|SecurityModels
operator|.
name|getInstance
argument_list|()
operator|.
name|removeSecurityModel
argument_list|(
operator|new
name|Integer32
argument_list|(
name|this
operator|.
name|usm
operator|.
name|getID
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|this
operator|.
name|targetAddress
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|usm
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|target
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// load connection data only if the endpoint is enabled
name|Snmp
name|snmp
init|=
literal|null
decl_stmt|;
name|TransportMapping
argument_list|<
name|?
extends|extends
name|Address
argument_list|>
name|transport
init|=
literal|null
decl_stmt|;
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Starting SNMP Trap producer on {}"
argument_list|,
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
name|this
operator|.
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
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
name|this
operator|.
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
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
name|this
operator|.
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
throw|;
block|}
name|snmp
operator|=
operator|new
name|Snmp
argument_list|(
name|transport
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"SnmpTrap: getting pdu from body"
argument_list|)
expr_stmt|;
name|PDU
name|trap
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|PDU
operator|.
name|class
argument_list|)
decl_stmt|;
name|trap
operator|.
name|setErrorIndex
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|trap
operator|.
name|setErrorStatus
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|trap
operator|.
name|setMaxRepetitions
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|endpoint
operator|.
name|getSnmpVersion
argument_list|()
operator|==
name|SnmpConstants
operator|.
name|version1
condition|)
block|{
name|trap
operator|.
name|setType
argument_list|(
name|PDU
operator|.
name|V1TRAP
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|trap
operator|.
name|setType
argument_list|(
name|PDU
operator|.
name|TRAP
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"SnmpTrap: sending"
argument_list|)
expr_stmt|;
name|snmp
operator|.
name|send
argument_list|(
name|trap
argument_list|,
name|this
operator|.
name|target
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"SnmpTrap: sent"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|transport
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{ }
try|try
block|{
name|snmp
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{ }
block|}
block|}
comment|//end process
block|}
end_class

end_unit

