begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|EndpointConsumerBuilder
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
name|EndpointProducerBuilder
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
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The snmp component gives you the ability to poll SNMP capable devices or  * receiving traps.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|SnmpEndpointBuilderFactory
specifier|public
interface|interface
name|SnmpEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the SNMP component.      */
DECL|interface|SnmpEndpointBuilder
specifier|public
specifier|static
interface|interface
name|SnmpEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSnmpEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSnmpEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Hostname of the SNMP enabled device.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|host (String host)
specifier|default
name|SnmpEndpointBuilder
name|host
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"host"
argument_list|,
name|host
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Port number of the SNMP enabled device.          * The option is a<code>java.lang.Integer</code> type.          * @group consumer          */
DECL|method|port (Integer port)
specifier|default
name|SnmpEndpointBuilder
name|port
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Port number of the SNMP enabled device.          * The option will be converted to a<code>java.lang.Integer</code>          * type.          * @group consumer          */
DECL|method|port (String port)
specifier|default
name|SnmpEndpointBuilder
name|port
parameter_list|(
name|String
name|port
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Defines which values you are interested in. Please have a look at the          * Wikipedia to get a better understanding. You may provide a single OID          * or a coma separated list of OIDs. Example:          * oids=1.3.6.1.2.1.1.3.0,1.3.6.1.2.1.25.3.2.1.5.1,1.3.6.1.2.1.25.3.5.1.1.1,1.3.6.1.2.1.43.5.1.1.11.1.          * The option is a<code>org.apache.camel.component.snmp.OIDList</code>          * type.          * @group consumer          */
DECL|method|oids (Object oids)
specifier|default
name|SnmpEndpointBuilder
name|oids
parameter_list|(
name|Object
name|oids
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"oids"
argument_list|,
name|oids
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Defines which values you are interested in. Please have a look at the          * Wikipedia to get a better understanding. You may provide a single OID          * or a coma separated list of OIDs. Example:          * oids=1.3.6.1.2.1.1.3.0,1.3.6.1.2.1.25.3.2.1.5.1,1.3.6.1.2.1.25.3.5.1.1.1,1.3.6.1.2.1.43.5.1.1.11.1.          * The option will be converted to a          *<code>org.apache.camel.component.snmp.OIDList</code> type.          * @group consumer          */
DECL|method|oids (String oids)
specifier|default
name|SnmpEndpointBuilder
name|oids
parameter_list|(
name|String
name|oids
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"oids"
argument_list|,
name|oids
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Here you can select which protocol to use. You can use either udp or          * tcp.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|protocol (String protocol)
specifier|default
name|SnmpEndpointBuilder
name|protocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"protocol"
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Defines how often a retry is made before canceling the request.          * The option is a<code>int</code> type.          * @group consumer          */
DECL|method|retries (int retries)
specifier|default
name|SnmpEndpointBuilder
name|retries
parameter_list|(
name|int
name|retries
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"retries"
argument_list|,
name|retries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Defines how often a retry is made before canceling the request.          * The option will be converted to a<code>int</code> type.          * @group consumer          */
DECL|method|retries (String retries)
specifier|default
name|SnmpEndpointBuilder
name|retries
parameter_list|(
name|String
name|retries
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"retries"
argument_list|,
name|retries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the community octet string for the snmp request.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|snmpCommunity (String snmpCommunity)
specifier|default
name|SnmpEndpointBuilder
name|snmpCommunity
parameter_list|(
name|String
name|snmpCommunity
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"snmpCommunity"
argument_list|,
name|snmpCommunity
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the context engine ID field of the scoped PDU.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|snmpContextEngineId ( String snmpContextEngineId)
specifier|default
name|SnmpEndpointBuilder
name|snmpContextEngineId
parameter_list|(
name|String
name|snmpContextEngineId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"snmpContextEngineId"
argument_list|,
name|snmpContextEngineId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the context name field of this scoped PDU.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|snmpContextName (String snmpContextName)
specifier|default
name|SnmpEndpointBuilder
name|snmpContextName
parameter_list|(
name|String
name|snmpContextName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"snmpContextName"
argument_list|,
name|snmpContextName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the snmp version for the request. The value 0 means SNMPv1, 1          * means SNMPv2c, and the value 3 means SNMPv3.          * The option is a<code>int</code> type.          * @group consumer          */
DECL|method|snmpVersion (int snmpVersion)
specifier|default
name|SnmpEndpointBuilder
name|snmpVersion
parameter_list|(
name|int
name|snmpVersion
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"snmpVersion"
argument_list|,
name|snmpVersion
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the snmp version for the request. The value 0 means SNMPv1, 1          * means SNMPv2c, and the value 3 means SNMPv3.          * The option will be converted to a<code>int</code> type.          * @group consumer          */
DECL|method|snmpVersion (String snmpVersion)
specifier|default
name|SnmpEndpointBuilder
name|snmpVersion
parameter_list|(
name|String
name|snmpVersion
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"snmpVersion"
argument_list|,
name|snmpVersion
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the timeout value for the request in millis.          * The option is a<code>int</code> type.          * @group consumer          */
DECL|method|timeout (int timeout)
specifier|default
name|SnmpEndpointBuilder
name|timeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the timeout value for the request in millis.          * The option will be converted to a<code>int</code> type.          * @group consumer          */
DECL|method|timeout (String timeout)
specifier|default
name|SnmpEndpointBuilder
name|timeout
parameter_list|(
name|String
name|timeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"timeout"
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Which operation to perform such as poll, trap, etc.          * The option is a          *<code>org.apache.camel.component.snmp.SnmpActionType</code> type.          * @group consumer          */
DECL|method|type (SnmpActionType type)
specifier|default
name|SnmpEndpointBuilder
name|type
parameter_list|(
name|SnmpActionType
name|type
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Which operation to perform such as poll, trap, etc.          * The option will be converted to a          *<code>org.apache.camel.component.snmp.SnmpActionType</code> type.          * @group consumer          */
DECL|method|type (String type)
specifier|default
name|SnmpEndpointBuilder
name|type
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The authentication passphrase. If not null, authenticationProtocol          * must also be not null. RFC3414 11.2 requires passphrases to have a          * minimum length of 8 bytes. If the length of authenticationPassphrase          * is less than 8 bytes an IllegalArgumentException is thrown.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|authenticationPassphrase ( String authenticationPassphrase)
specifier|default
name|SnmpEndpointBuilder
name|authenticationPassphrase
parameter_list|(
name|String
name|authenticationPassphrase
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"authenticationPassphrase"
argument_list|,
name|authenticationPassphrase
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Authentication protocol to use if security level is set to enable          * authentication The possible values are: MD5, SHA1.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|authenticationProtocol ( String authenticationProtocol)
specifier|default
name|SnmpEndpointBuilder
name|authenticationProtocol
parameter_list|(
name|String
name|authenticationProtocol
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"authenticationProtocol"
argument_list|,
name|authenticationProtocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The privacy passphrase. If not null, privacyProtocol must also be not          * null. RFC3414 11.2 requires passphrases to have a minimum length of 8          * bytes. If the length of authenticationPassphrase is less than 8 bytes          * an IllegalArgumentException is thrown.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|privacyPassphrase (String privacyPassphrase)
specifier|default
name|SnmpEndpointBuilder
name|privacyPassphrase
parameter_list|(
name|String
name|privacyPassphrase
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"privacyPassphrase"
argument_list|,
name|privacyPassphrase
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The privacy protocol ID to be associated with this user. If set to          * null, this user only supports unencrypted messages.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|privacyProtocol (String privacyProtocol)
specifier|default
name|SnmpEndpointBuilder
name|privacyProtocol
parameter_list|(
name|String
name|privacyProtocol
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"privacyProtocol"
argument_list|,
name|privacyProtocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the security level for this target. The supplied security level          * must be supported by the security model dependent information          * associated with the security name set for this target. The value 1          * means: No authentication and no encryption. Anyone can create and          * read messages with this security level The value 2 means:          * Authentication and no encryption. Only the one with the right          * authentication key can create messages with this security level, but          * anyone can read the contents of the message. The value 3 means:          * Authentication and encryption. Only the one with the right          * authentication key can create messages with this security level, and          * only the one with the right encryption/decryption key can read the          * contents of the message.          * The option is a<code>int</code> type.          * @group security          */
DECL|method|securityLevel (int securityLevel)
specifier|default
name|SnmpEndpointBuilder
name|securityLevel
parameter_list|(
name|int
name|securityLevel
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"securityLevel"
argument_list|,
name|securityLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the security level for this target. The supplied security level          * must be supported by the security model dependent information          * associated with the security name set for this target. The value 1          * means: No authentication and no encryption. Anyone can create and          * read messages with this security level The value 2 means:          * Authentication and no encryption. Only the one with the right          * authentication key can create messages with this security level, but          * anyone can read the contents of the message. The value 3 means:          * Authentication and encryption. Only the one with the right          * authentication key can create messages with this security level, and          * only the one with the right encryption/decryption key can read the          * contents of the message.          * The option will be converted to a<code>int</code> type.          * @group security          */
DECL|method|securityLevel (String securityLevel)
specifier|default
name|SnmpEndpointBuilder
name|securityLevel
parameter_list|(
name|String
name|securityLevel
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"securityLevel"
argument_list|,
name|securityLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the security name to be used with this target.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|securityName (String securityName)
specifier|default
name|SnmpEndpointBuilder
name|securityName
parameter_list|(
name|String
name|securityName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"securityName"
argument_list|,
name|securityName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the SNMP component.      */
DECL|interface|AdvancedSnmpEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedSnmpEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SnmpEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SnmpEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedSnmpEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedSnmpEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedSnmpEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedSnmpEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.snmp.SnmpActionType</code> enum.      */
DECL|enum|SnmpActionType
specifier|public
specifier|static
enum|enum
name|SnmpActionType
block|{
DECL|enumConstant|TRAP
DECL|enumConstant|POLL
DECL|enumConstant|GET_NEXT
name|TRAP
block|,
name|POLL
block|,
name|GET_NEXT
block|;     }
comment|/**      * The snmp component gives you the ability to poll SNMP capable devices or      * receiving traps. Creates a builder to build endpoints for the SNMP      * component.      */
DECL|method|snmp (String path)
specifier|default
name|SnmpEndpointBuilder
name|snmp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|SnmpEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|SnmpEndpointBuilder
implements|,
name|AdvancedSnmpEndpointBuilder
block|{
specifier|public
name|SnmpEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"snmp"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SnmpEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

