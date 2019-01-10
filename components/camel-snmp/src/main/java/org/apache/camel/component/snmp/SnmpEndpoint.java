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
name|java
operator|.
name|net
operator|.
name|URI
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
name|support
operator|.
name|DefaultPollingEndpoint
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
name|Metadata
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
name|spi
operator|.
name|UriPath
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
name|PDU
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
name|SecurityLevel
import|;
end_import

begin_comment
comment|/**  * The snmp component gives you the ability to poll SNMP capable devices or receiving traps.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.1.0"
argument_list|,
name|scheme
operator|=
literal|"snmp"
argument_list|,
name|title
operator|=
literal|"SNMP"
argument_list|,
name|syntax
operator|=
literal|"snmp:host:port"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"monitoring"
argument_list|)
DECL|class|SnmpEndpoint
specifier|public
class|class
name|SnmpEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
DECL|field|DEFAULT_COMMUNITY
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_COMMUNITY
init|=
literal|"public"
decl_stmt|;
DECL|field|DEFAULT_SNMP_VERSION
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_SNMP_VERSION
init|=
name|SnmpConstants
operator|.
name|version1
decl_stmt|;
DECL|field|DEFAULT_SNMP_RETRIES
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_SNMP_RETRIES
init|=
literal|2
decl_stmt|;
DECL|field|DEFAULT_SNMP_TIMEOUT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_SNMP_TIMEOUT
init|=
literal|1500
decl_stmt|;
DECL|field|address
specifier|private
specifier|transient
name|String
name|address
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Hostname of the SNMP enabled device"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Port number of the SNMP enabled device"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"udp"
argument_list|,
name|enums
operator|=
literal|"tcp,udp"
argument_list|)
DECL|field|protocol
specifier|private
name|String
name|protocol
init|=
literal|"udp"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|DEFAULT_SNMP_RETRIES
argument_list|)
DECL|field|retries
specifier|private
name|int
name|retries
init|=
name|DEFAULT_SNMP_RETRIES
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|DEFAULT_SNMP_TIMEOUT
argument_list|)
DECL|field|timeout
specifier|private
name|int
name|timeout
init|=
name|DEFAULT_SNMP_TIMEOUT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|DEFAULT_SNMP_VERSION
argument_list|,
name|enums
operator|=
literal|"0,1,3"
argument_list|)
DECL|field|snmpVersion
specifier|private
name|int
name|snmpVersion
init|=
name|DEFAULT_SNMP_VERSION
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
name|DEFAULT_COMMUNITY
argument_list|)
DECL|field|snmpCommunity
specifier|private
name|String
name|snmpCommunity
init|=
name|DEFAULT_COMMUNITY
decl_stmt|;
annotation|@
name|UriParam
DECL|field|type
specifier|private
name|SnmpActionType
name|type
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"60000"
argument_list|)
DECL|field|delay
specifier|private
name|long
name|delay
init|=
literal|60000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|SecurityLevel
operator|.
name|AUTH_PRIV
argument_list|,
name|enums
operator|=
literal|"1,2,3"
argument_list|,
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|securityLevel
specifier|private
name|int
name|securityLevel
init|=
name|SecurityLevel
operator|.
name|AUTH_PRIV
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|securityName
specifier|private
name|String
name|securityName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"MD5,SHA1"
argument_list|,
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|authenticationProtocol
specifier|private
name|String
name|authenticationProtocol
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|authenticationPassphrase
specifier|private
name|String
name|authenticationPassphrase
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|privacyProtocol
specifier|private
name|String
name|privacyProtocol
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|privacyPassphrase
specifier|private
name|String
name|privacyPassphrase
decl_stmt|;
annotation|@
name|UriParam
DECL|field|snmpContextName
specifier|private
name|String
name|snmpContextName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|snmpContextEngineId
specifier|private
name|String
name|snmpContextEngineId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|oids
specifier|private
name|OIDList
name|oids
init|=
operator|new
name|OIDList
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|treeList
specifier|private
name|boolean
name|treeList
decl_stmt|;
comment|/**      * creates a snmp endpoint      *      * @param uri       the endpoint uri      * @param component the component      */
DECL|method|SnmpEndpoint (String uri, SnmpComponent component)
specifier|public
name|SnmpEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SnmpComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|this
operator|.
name|type
operator|==
name|SnmpActionType
operator|.
name|TRAP
condition|)
block|{
name|SnmpTrapConsumer
name|answer
init|=
operator|new
name|SnmpTrapConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
comment|// As the SnmpTrapConsumer is not a polling consumer we don't need to call the configureConsumer here.
return|return
name|answer
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|type
operator|==
name|SnmpActionType
operator|.
name|POLL
condition|)
block|{
name|SnmpOIDPoller
name|answer
init|=
operator|new
name|SnmpOIDPoller
argument_list|(
name|this
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
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The type '"
operator|+
name|this
operator|.
name|type
operator|+
literal|"' is not valid!"
argument_list|)
throw|;
block|}
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|type
operator|==
name|SnmpActionType
operator|.
name|TRAP
condition|)
block|{
return|return
operator|new
name|SnmpTrapProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|SnmpProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * creates an exchange for the given message      *      * @param pdu the pdu      * @return an exchange      */
DECL|method|createExchange (PDU pdu)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|PDU
name|pdu
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|SnmpMessage
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|pdu
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|/**      * creates an exchange for the given message      *      * @param pdu the pdu      * @param event a snmp4j CommandResponderEvent      * @return an exchange      */
DECL|method|createExchange (PDU pdu, CommandResponderEvent event)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|PDU
name|pdu
parameter_list|,
name|CommandResponderEvent
name|event
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|SnmpMessage
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|pdu
argument_list|,
name|event
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|/**      * creates and configures the endpoint      *      * @throws Exception if unable to setup connection      * @deprecated use {@link #start()} instead      */
annotation|@
name|Deprecated
DECL|method|initiate ()
specifier|public
name|void
name|initiate
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|getDelay ()
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
comment|/**      * Sets update rate in seconds      *      * @param updateEvery the update rate in seconds      */
annotation|@
name|Override
DECL|method|setDelay (long updateEvery)
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
name|updateEvery
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|updateEvery
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|SnmpActionType
name|getType
parameter_list|()
block|{
return|return
name|this
operator|.
name|type
return|;
block|}
comment|/**      * Which operation to perform such as poll, trap, etc.      */
DECL|method|setType (SnmpActionType type)
specifier|public
name|void
name|setType
parameter_list|(
name|SnmpActionType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getOids ()
specifier|public
name|OIDList
name|getOids
parameter_list|()
block|{
return|return
name|this
operator|.
name|oids
return|;
block|}
comment|/**      * Defines which values you are interested in. Please have a look at the Wikipedia to get a better understanding.      * You may provide a single OID or a coma separated list of OIDs.      * Example: oids="1.3.6.1.2.1.1.3.0,1.3.6.1.2.1.25.3.2.1.5.1,1.3.6.1.2.1.25.3.5.1.1.1,1.3.6.1.2.1.43.5.1.1.11.1"      */
DECL|method|setOids (OIDList oids)
specifier|public
name|void
name|setOids
parameter_list|(
name|OIDList
name|oids
parameter_list|)
block|{
name|this
operator|.
name|oids
operator|=
name|oids
expr_stmt|;
block|}
DECL|method|getAddress ()
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|this
operator|.
name|address
return|;
block|}
DECL|method|setAddress (String address)
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
DECL|method|getRetries ()
specifier|public
name|int
name|getRetries
parameter_list|()
block|{
return|return
name|this
operator|.
name|retries
return|;
block|}
comment|/**      * Defines how often a retry is made before canceling the request.      */
DECL|method|setRetries (int retries)
specifier|public
name|void
name|setRetries
parameter_list|(
name|int
name|retries
parameter_list|)
block|{
name|this
operator|.
name|retries
operator|=
name|retries
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|this
operator|.
name|timeout
return|;
block|}
comment|/**      * Sets the timeout value for the request in millis.      */
DECL|method|setTimeout (int timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getSnmpVersion ()
specifier|public
name|int
name|getSnmpVersion
parameter_list|()
block|{
return|return
name|this
operator|.
name|snmpVersion
return|;
block|}
comment|/**      * Sets the snmp version for the request.      *<p/>      * The value 0 means SNMPv1, 1 means SNMPv2c, and the value 3 means SNMPv3      */
DECL|method|setSnmpVersion (int snmpVersion)
specifier|public
name|void
name|setSnmpVersion
parameter_list|(
name|int
name|snmpVersion
parameter_list|)
block|{
name|this
operator|.
name|snmpVersion
operator|=
name|snmpVersion
expr_stmt|;
block|}
DECL|method|getSnmpCommunity ()
specifier|public
name|String
name|getSnmpCommunity
parameter_list|()
block|{
return|return
name|this
operator|.
name|snmpCommunity
return|;
block|}
comment|/**      * Sets the community octet string for the snmp request.      */
DECL|method|setSnmpCommunity (String snmpCommunity)
specifier|public
name|void
name|setSnmpCommunity
parameter_list|(
name|String
name|snmpCommunity
parameter_list|)
block|{
name|this
operator|.
name|snmpCommunity
operator|=
name|snmpCommunity
expr_stmt|;
block|}
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|this
operator|.
name|protocol
return|;
block|}
comment|/**      * Here you can select which protocol to use. You can use either udp or tcp.      */
DECL|method|setProtocol (String protocol)
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
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
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|host
init|=
name|uri
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|int
name|port
init|=
name|uri
operator|.
name|getPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|host
operator|==
literal|null
operator|||
name|host
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|<
literal|1
condition|)
block|{
name|host
operator|=
literal|"127.0.0.1"
expr_stmt|;
block|}
if|if
condition|(
name|port
operator|==
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|getType
argument_list|()
operator|==
name|SnmpActionType
operator|.
name|POLL
condition|)
block|{
name|port
operator|=
literal|161
expr_stmt|;
comment|// default snmp poll port
block|}
else|else
block|{
name|port
operator|=
literal|162
expr_stmt|;
comment|// default trap port
block|}
block|}
comment|// set the address
name|String
name|address
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%s:%s/%d"
argument_list|,
name|getProtocol
argument_list|()
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Using snmp address {}"
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
DECL|method|getSecurityLevel ()
specifier|public
name|int
name|getSecurityLevel
parameter_list|()
block|{
return|return
name|securityLevel
return|;
block|}
comment|/**      * Sets the security level for this target. The supplied security level must      * be supported by the security model dependent information associated with      * the security name set for this target.      *<p/>      * The value 1 means: No authentication and no encryption. Anyone can create and read messages with this security level      * The value 2 means: Authentication and no encryption. Only the one with the right authentication key can create      * messages with this security level, but anyone can read the contents of the message.      * The value 3 means: Authentication and encryption. Only the one with the right authentication key can create messages      * with this security level, and only the one with the right encryption/decryption key can read the contents of the message.      */
DECL|method|setSecurityLevel (int securityLevel)
specifier|public
name|void
name|setSecurityLevel
parameter_list|(
name|int
name|securityLevel
parameter_list|)
block|{
name|this
operator|.
name|securityLevel
operator|=
name|securityLevel
expr_stmt|;
block|}
DECL|method|getSecurityName ()
specifier|public
name|String
name|getSecurityName
parameter_list|()
block|{
return|return
name|securityName
return|;
block|}
comment|/**      * Sets the security name to be used with this target.      */
DECL|method|setSecurityName (String securityName)
specifier|public
name|void
name|setSecurityName
parameter_list|(
name|String
name|securityName
parameter_list|)
block|{
name|this
operator|.
name|securityName
operator|=
name|securityName
expr_stmt|;
block|}
DECL|method|getAuthenticationProtocol ()
specifier|public
name|String
name|getAuthenticationProtocol
parameter_list|()
block|{
return|return
name|authenticationProtocol
return|;
block|}
comment|/**      * Authentication protocol to use if security level is set to enable authentication      * The possible values are: MD5, SHA1      */
DECL|method|setAuthenticationProtocol (String authenticationProtocol)
specifier|public
name|void
name|setAuthenticationProtocol
parameter_list|(
name|String
name|authenticationProtocol
parameter_list|)
block|{
name|this
operator|.
name|authenticationProtocol
operator|=
name|authenticationProtocol
expr_stmt|;
block|}
DECL|method|getAuthenticationPassphrase ()
specifier|public
name|String
name|getAuthenticationPassphrase
parameter_list|()
block|{
return|return
name|authenticationPassphrase
return|;
block|}
comment|/**      * The authentication passphrase. If not<code>null</code>,<code>authenticationProtocol</code> must also be not      *<code>null</code>. RFC3414 11.2 requires passphrases to have a minimum length of 8 bytes.      * If the length of<code>authenticationPassphrase</code> is less than 8 bytes an<code>IllegalArgumentException</code> is thrown.      */
DECL|method|setAuthenticationPassphrase (String authenticationPassphrase)
specifier|public
name|void
name|setAuthenticationPassphrase
parameter_list|(
name|String
name|authenticationPassphrase
parameter_list|)
block|{
name|this
operator|.
name|authenticationPassphrase
operator|=
name|authenticationPassphrase
expr_stmt|;
block|}
DECL|method|getPrivacyProtocol ()
specifier|public
name|String
name|getPrivacyProtocol
parameter_list|()
block|{
return|return
name|privacyProtocol
return|;
block|}
comment|/**      * The privacy protocol ID to be associated with this user. If set to<code>null</code>, this user only supports unencrypted messages.      */
DECL|method|setPrivacyProtocol (String privacyProtocol)
specifier|public
name|void
name|setPrivacyProtocol
parameter_list|(
name|String
name|privacyProtocol
parameter_list|)
block|{
name|this
operator|.
name|privacyProtocol
operator|=
name|privacyProtocol
expr_stmt|;
block|}
DECL|method|getPrivacyPassphrase ()
specifier|public
name|String
name|getPrivacyPassphrase
parameter_list|()
block|{
return|return
name|privacyPassphrase
return|;
block|}
comment|/**      * The privacy passphrase. If not<code>null</code>,<code>privacyProtocol</code> must also be not<code>null</code>.      * RFC3414 11.2 requires passphrases to have a minimum length of 8 bytes. If the length of      *<code>authenticationPassphrase</code> is less than 8 bytes an<code>IllegalArgumentException</code> is thrown.      */
DECL|method|setPrivacyPassphrase (String privacyPassphrase)
specifier|public
name|void
name|setPrivacyPassphrase
parameter_list|(
name|String
name|privacyPassphrase
parameter_list|)
block|{
name|this
operator|.
name|privacyPassphrase
operator|=
name|privacyPassphrase
expr_stmt|;
block|}
DECL|method|getSnmpContextName ()
specifier|public
name|String
name|getSnmpContextName
parameter_list|()
block|{
return|return
name|snmpContextName
return|;
block|}
comment|/**      * Sets the context name field of this scoped PDU.      */
DECL|method|setSnmpContextName (String snmpContextName)
specifier|public
name|void
name|setSnmpContextName
parameter_list|(
name|String
name|snmpContextName
parameter_list|)
block|{
name|this
operator|.
name|snmpContextName
operator|=
name|snmpContextName
expr_stmt|;
block|}
DECL|method|getSnmpContextEngineId ()
specifier|public
name|String
name|getSnmpContextEngineId
parameter_list|()
block|{
return|return
name|snmpContextEngineId
return|;
block|}
comment|/**      * Sets the context engine ID field of the scoped PDU.      */
DECL|method|setSnmpContextEngineId (String snmpContextEngineId)
specifier|public
name|void
name|setSnmpContextEngineId
parameter_list|(
name|String
name|snmpContextEngineId
parameter_list|)
block|{
name|this
operator|.
name|snmpContextEngineId
operator|=
name|snmpContextEngineId
expr_stmt|;
block|}
DECL|method|isTreeList ()
specifier|public
name|boolean
name|isTreeList
parameter_list|()
block|{
return|return
name|treeList
return|;
block|}
comment|/**      * Sets the flag whether the scoped PDU will be displayed as the list      * if it has child elements in its tree      */
DECL|method|setTreeList (boolean treeList)
specifier|public
name|void
name|setTreeList
parameter_list|(
name|boolean
name|treeList
parameter_list|)
block|{
name|this
operator|.
name|treeList
operator|=
name|treeList
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// only show address to avoid user and password details to be shown
return|return
literal|"snmp://"
operator|+
name|address
return|;
block|}
block|}
end_class

end_unit

