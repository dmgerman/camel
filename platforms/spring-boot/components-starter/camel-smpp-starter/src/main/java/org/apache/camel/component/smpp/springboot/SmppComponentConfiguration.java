begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.smpp.springboot
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
operator|.
name|springboot
package|;
end_package

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
name|component
operator|.
name|smpp
operator|.
name|SmppComponent
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
name|smpp
operator|.
name|SmppSplittingPolicy
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|SessionStateListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * To send and receive SMS using a SMSC (Short Message Service Center).  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.smpp"
argument_list|)
DECL|class|SmppComponentConfiguration
specifier|public
class|class
name|SmppComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * To use the shared SmppConfiguration as configuration.      */
DECL|field|configuration
specifier|private
name|SmppConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|SmppConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( SmppConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|SmppConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|SmppConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|SmppConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|smpp
operator|.
name|SmppConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * Hostname for the SMSC server to use.          */
DECL|field|host
specifier|private
name|String
name|host
init|=
literal|"localhost"
decl_stmt|;
comment|/**          * Port number for the SMSC server to use.          */
DECL|field|port
specifier|private
name|Integer
name|port
init|=
literal|2775
decl_stmt|;
comment|/**          * The system id (username) for connecting to SMSC server.          */
DECL|field|systemId
specifier|private
name|String
name|systemId
init|=
literal|"smppclient"
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**          * Defines the data coding according the SMPP 3.4 specification, section          * 5.2.19. Example data encodings are:          *<ul>          *<li>0: SMSC Default Alphabet</li>          *<li>3: Latin 1 (ISO-8859-1)</li>          *<li>4: Octet unspecified (8-bit binary)</li>          *<li>8: UCS2 (ISO/IEC-10646)</li>          *<li>13: Extended Kanji JIS(X 0212-1990)</li>          *</ul>          */
DECL|field|dataCoding
specifier|private
name|Byte
name|dataCoding
decl_stmt|;
comment|/**          * Defines encoding of data according the SMPP 3.4 specification,          * section 5.2.19.          *<ul>          *<li>0: SMSC Default Alphabet          *<li>4: 8 bit Alphabet</li>          *<li>8: UCS2 Alphabet</li></li>          *</ul>          */
DECL|field|alphabet
specifier|private
name|Byte
name|alphabet
decl_stmt|;
comment|/**          * Defines the encoding scheme of the short message user data. Only for          * SubmitSm, ReplaceSm and SubmitMulti.          */
DECL|field|encoding
specifier|private
name|String
name|encoding
init|=
literal|"ISO-8859-1"
decl_stmt|;
comment|/**          * Defines the interval in milliseconds between the confidence checks.          * The confidence check is used to test the communication path between          * an ESME and an SMSC.          */
DECL|field|enquireLinkTimer
specifier|private
name|Integer
name|enquireLinkTimer
init|=
literal|5000
decl_stmt|;
comment|/**          * Defines the maximum period of inactivity allowed after a transaction,          * after which an SMPP entity may assume that the session is no longer          * active. This timer may be active on either communicating SMPP entity          * (i.e. SMSC or ESME).          */
DECL|field|transactionTimer
specifier|private
name|Integer
name|transactionTimer
init|=
literal|10000
decl_stmt|;
comment|/**          * This parameter is used to categorize the type of ESME (External Short          * Message Entity) that is binding to the SMSC (max. 13 characters).          */
DECL|field|systemType
specifier|private
name|String
name|systemType
init|=
literal|"cp"
decl_stmt|;
comment|/**          * Is used to request an SMSC delivery receipt and/or SME originated          * acknowledgements. The following values are defined:          *<ul>          *<li>0: No SMSC delivery receipt requested.</li>          *<li>1: SMSC delivery receipt requested where final delivery outcome          * is success or failure.</li>          *<li>2: SMSC delivery receipt requested where the final delivery          * outcome is delivery failure.</li>          *</ul>          */
DECL|field|registeredDelivery
specifier|private
name|Byte
name|registeredDelivery
decl_stmt|;
comment|/**          * The service type parameter can be used to indicate the SMS          * Application service associated with the message. The following          * generic service_types are defined:          *<ul>          *<li>CMT: Cellular Messaging</li>          *<li>CPT: Cellular Paging</li>          *<li>VMN: Voice Mail Notification</li>          *<li>VMA: Voice Mail Alerting</li>          *<li>WAP: Wireless Application Protocol</li>          *<li>USSD: Unstructured Supplementary Services Data</li>          *</ul>          */
DECL|field|serviceType
specifier|private
name|String
name|serviceType
init|=
literal|"CMT"
decl_stmt|;
comment|/**          * Defines the type of number (TON) to be used in the SME originator          * address parameters. The following TON values are defined:          *<ul>          *<li>0: Unknown</li>          *<li>1: International</li>          *<li>2: National</li>          *<li>3: Network Specific</li>          *<li>4: Subscriber Number</li>          *<li>5: Alphanumeric</li>          *<li>6: Abbreviated</li>          *</ul>          */
DECL|field|sourceAddrTon
specifier|private
name|Byte
name|sourceAddrTon
decl_stmt|;
comment|/**          * Defines the type of number (TON) to be used in the SME destination          * address parameters. Only for SubmitSm, SubmitMulti, CancelSm and          * DataSm. The following TON values are defined:          *<ul>          *<li>0: Unknown</li>          *<li>1: International</li>          *<li>2: National</li>          *<li>3: Network Specific</li>          *<li>4: Subscriber Number</li>          *<li>5: Alphanumeric</li>          *<li>6: Abbreviated</li>          *</ul>          */
DECL|field|destAddrTon
specifier|private
name|Byte
name|destAddrTon
decl_stmt|;
comment|/**          * Defines the numeric plan indicator (NPI) to be used in the SME          * originator address parameters. The following NPI values are defined:          *<ul>          *<li>0: Unknown</li>          *<li>1: ISDN (E163/E164)</li>          *<li>2: Data (X.121)</li>          *<li>3: Telex (F.69)</li>          *<li>6: Land Mobile (E.212)</li>          *<li>8: National</li>          *<li>9: Private</li>          *<li>10: ERMES</li>          *<li>13: Internet (IP)</li>          *<li>18: WAP Client Id (to be defined by WAP Forum)</li>          *</ul>          */
DECL|field|sourceAddrNpi
specifier|private
name|Byte
name|sourceAddrNpi
decl_stmt|;
comment|/**          * Defines the type of number (TON) to be used in the SME destination          * address parameters. Only for SubmitSm, SubmitMulti, CancelSm and          * DataSm. The following NPI values are defined:          *<ul>          *<li>0: Unknown</li>          *<li>1: ISDN (E163/E164)</li>          *<li>2: Data (X.121)</li>          *<li>3: Telex (F.69)</li>          *<li>6: Land Mobile (E.212)</li>          *<li>8: National</li>          *<li>9: Private</li>          *<li>10: ERMES</li>          *<li>13: Internet (IP)</li>          *<li>18: WAP Client Id (to be defined by WAP Forum)</li>          *</ul>          */
DECL|field|destAddrNpi
specifier|private
name|Byte
name|destAddrNpi
decl_stmt|;
comment|/**          * The protocol id          */
DECL|field|protocolId
specifier|private
name|Byte
name|protocolId
decl_stmt|;
comment|/**          * Allows the originating SME to assign a priority level to the short          * message. Only for SubmitSm and SubmitMulti. Four Priority Levels are          * supported:          *<ul>          *<li>0: Level 0 (lowest) priority</li>          *<li>1: Level 1 priority</li>          *<li>2: Level 2 priority</li>          *<li>3: Level 3 (highest) priority</li>          *</ul>          */
DECL|field|priorityFlag
specifier|private
name|Byte
name|priorityFlag
decl_stmt|;
comment|/**          * Used to request the SMSC to replace a previously submitted message,          * that is still pending delivery. The SMSC will replace an existing          * message provided that the source address, destination address and          * service type match the same fields in the new message. The following          * replace if present flag values are defined:          *<ul>          *<li>0: Don't replace</li>          *<li>1: Replace</li>          *</ul>          */
DECL|field|replaceIfPresentFlag
specifier|private
name|Byte
name|replaceIfPresentFlag
decl_stmt|;
comment|/**          * Defines the address of SME (Short Message Entity) which originated          * this message.          */
DECL|field|sourceAddr
specifier|private
name|String
name|sourceAddr
init|=
literal|"1616"
decl_stmt|;
comment|/**          * Defines the destination SME address. For mobile terminated messages,          * this is the directory number of the recipient MS. Only for SubmitSm,          * SubmitMulti, CancelSm and DataSm.          */
DECL|field|destAddr
specifier|private
name|String
name|destAddr
init|=
literal|"1717"
decl_stmt|;
comment|/**          * Defines the type of number (TON) to be used in the SME. The following          * TON values are defined:          *<ul>          *<li>0: Unknown</li>          *<li>1: International</li>          *<li>2: National</li>          *<li>3: Network Specific</li>          *<li>4: Subscriber Number</li>          *<li>5: Alphanumeric</li>          *<li>6: Abbreviated</li>          *</ul>          */
DECL|field|typeOfNumber
specifier|private
name|Byte
name|typeOfNumber
decl_stmt|;
comment|/**          * Defines the numeric plan indicator (NPI) to be used in the SME. The          * following NPI values are defined:          *<ul>          *<li>0: Unknown</li>          *<li>1: ISDN (E163/E164)</li>          *<li>2: Data (X.121)</li>          *<li>3: Telex (F.69)</li>          *<li>6: Land Mobile (E.212)</li>          *<li>8: National</li>          *<li>9: Private</li>          *<li>10: ERMES</li>          *<li>13: Internet (IP)</li>          *<li>18: WAP Client Id (to be defined by WAP Forum)</li>          *</ul>          */
DECL|field|numberingPlanIndicator
specifier|private
name|Byte
name|numberingPlanIndicator
decl_stmt|;
comment|/**          * Whether using SSL with the smpps protocol          */
DECL|field|usingSSL
specifier|private
name|Boolean
name|usingSSL
init|=
literal|false
decl_stmt|;
comment|/**          * Defines the initial delay in milliseconds after the consumer/producer          * tries to reconnect to the SMSC, after the connection was lost.          */
DECL|field|initialReconnectDelay
specifier|private
name|Long
name|initialReconnectDelay
init|=
literal|5000L
decl_stmt|;
comment|/**          * Defines the interval in milliseconds between the reconnect attempts,          * if the connection to the SMSC was lost and the previous was not          * succeed.          */
DECL|field|reconnectDelay
specifier|private
name|Long
name|reconnectDelay
init|=
literal|5000L
decl_stmt|;
DECL|field|maxReconnect
specifier|private
name|Integer
name|maxReconnect
init|=
literal|2147483647
decl_stmt|;
comment|/**          * Sessions can be lazily created to avoid exceptions, if the SMSC is          * not available when the Camel producer is started. Camel will check          * the in message headers 'CamelSmppSystemId' and 'CamelSmppPassword' of          * the first exchange. If they are present, Camel will use these data to          * connect to the SMSC.          */
DECL|field|lazySessionCreation
specifier|private
name|Boolean
name|lazySessionCreation
init|=
literal|false
decl_stmt|;
comment|/**          * If you need to tunnel SMPP through a HTTP proxy, set this attribute          * to the hostname or ip address of your HTTP proxy.          */
DECL|field|httpProxyHost
specifier|private
name|String
name|httpProxyHost
decl_stmt|;
comment|/**          * If you need to tunnel SMPP through a HTTP proxy, set this attribute          * to the port of your HTTP proxy.          */
DECL|field|httpProxyPort
specifier|private
name|Integer
name|httpProxyPort
init|=
literal|3128
decl_stmt|;
comment|/**          * If your HTTP proxy requires basic authentication, set this attribute          * to the username required for your HTTP proxy.          */
DECL|field|httpProxyUsername
specifier|private
name|String
name|httpProxyUsername
decl_stmt|;
comment|/**          * If your HTTP proxy requires basic authentication, set this attribute          * to the password required for your HTTP proxy.          */
DECL|field|httpProxyPassword
specifier|private
name|String
name|httpProxyPassword
decl_stmt|;
comment|/**          * You can refer to a org.jsmpp.session.SessionStateListener in the          * Registry to receive callbacks when the session state changed.          */
DECL|field|sessionStateListener
specifier|private
name|SessionStateListener
name|sessionStateListener
decl_stmt|;
comment|/**          * You can specify the address range for the SmppConsumer as defined in          * section 5.2.7 of the SMPP 3.4 specification. The SmppConsumer will          * receive messages only from SMSC's which target an address (MSISDN or          * IP address) within this range.          */
DECL|field|addressRange
specifier|private
name|String
name|addressRange
decl_stmt|;
comment|/**          * You can specify a policy for handling long messages:          *<ul>          *<li>ALLOW - the default, long messages are split to 140 bytes per          * message</li>          *<li>TRUNCATE - long messages are split and only the first fragment          * will be sent to the SMSC. Some carriers drop subsequent fragments so          * this reduces load on the SMPP connection sending parts of a message          * that will never be delivered.</li>          *<li>REJECT - if a message would need to be split, it is rejected with          * an SMPP NegativeResponseException and the reason code signifying the          * message is too long.</li>          *</ul>          */
DECL|field|splittingPolicy
specifier|private
name|SmppSplittingPolicy
name|splittingPolicy
init|=
name|SmppSplittingPolicy
operator|.
name|ALLOW
decl_stmt|;
comment|/**          * These headers will be passed to the proxy server while establishing          * the connection.          */
DECL|field|proxyHeaders
specifier|private
name|Map
name|proxyHeaders
decl_stmt|;
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getSystemId ()
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
name|systemId
return|;
block|}
DECL|method|setSystemId (String systemId)
specifier|public
name|void
name|setSystemId
parameter_list|(
name|String
name|systemId
parameter_list|)
block|{
name|this
operator|.
name|systemId
operator|=
name|systemId
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getDataCoding ()
specifier|public
name|Byte
name|getDataCoding
parameter_list|()
block|{
return|return
name|dataCoding
return|;
block|}
DECL|method|setDataCoding (Byte dataCoding)
specifier|public
name|void
name|setDataCoding
parameter_list|(
name|Byte
name|dataCoding
parameter_list|)
block|{
name|this
operator|.
name|dataCoding
operator|=
name|dataCoding
expr_stmt|;
block|}
DECL|method|getAlphabet ()
specifier|public
name|Byte
name|getAlphabet
parameter_list|()
block|{
return|return
name|alphabet
return|;
block|}
DECL|method|setAlphabet (Byte alphabet)
specifier|public
name|void
name|setAlphabet
parameter_list|(
name|Byte
name|alphabet
parameter_list|)
block|{
name|this
operator|.
name|alphabet
operator|=
name|alphabet
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
DECL|method|getEnquireLinkTimer ()
specifier|public
name|Integer
name|getEnquireLinkTimer
parameter_list|()
block|{
return|return
name|enquireLinkTimer
return|;
block|}
DECL|method|setEnquireLinkTimer (Integer enquireLinkTimer)
specifier|public
name|void
name|setEnquireLinkTimer
parameter_list|(
name|Integer
name|enquireLinkTimer
parameter_list|)
block|{
name|this
operator|.
name|enquireLinkTimer
operator|=
name|enquireLinkTimer
expr_stmt|;
block|}
DECL|method|getTransactionTimer ()
specifier|public
name|Integer
name|getTransactionTimer
parameter_list|()
block|{
return|return
name|transactionTimer
return|;
block|}
DECL|method|setTransactionTimer (Integer transactionTimer)
specifier|public
name|void
name|setTransactionTimer
parameter_list|(
name|Integer
name|transactionTimer
parameter_list|)
block|{
name|this
operator|.
name|transactionTimer
operator|=
name|transactionTimer
expr_stmt|;
block|}
DECL|method|getSystemType ()
specifier|public
name|String
name|getSystemType
parameter_list|()
block|{
return|return
name|systemType
return|;
block|}
DECL|method|setSystemType (String systemType)
specifier|public
name|void
name|setSystemType
parameter_list|(
name|String
name|systemType
parameter_list|)
block|{
name|this
operator|.
name|systemType
operator|=
name|systemType
expr_stmt|;
block|}
DECL|method|getRegisteredDelivery ()
specifier|public
name|Byte
name|getRegisteredDelivery
parameter_list|()
block|{
return|return
name|registeredDelivery
return|;
block|}
DECL|method|setRegisteredDelivery (Byte registeredDelivery)
specifier|public
name|void
name|setRegisteredDelivery
parameter_list|(
name|Byte
name|registeredDelivery
parameter_list|)
block|{
name|this
operator|.
name|registeredDelivery
operator|=
name|registeredDelivery
expr_stmt|;
block|}
DECL|method|getServiceType ()
specifier|public
name|String
name|getServiceType
parameter_list|()
block|{
return|return
name|serviceType
return|;
block|}
DECL|method|setServiceType (String serviceType)
specifier|public
name|void
name|setServiceType
parameter_list|(
name|String
name|serviceType
parameter_list|)
block|{
name|this
operator|.
name|serviceType
operator|=
name|serviceType
expr_stmt|;
block|}
DECL|method|getSourceAddrTon ()
specifier|public
name|Byte
name|getSourceAddrTon
parameter_list|()
block|{
return|return
name|sourceAddrTon
return|;
block|}
DECL|method|setSourceAddrTon (Byte sourceAddrTon)
specifier|public
name|void
name|setSourceAddrTon
parameter_list|(
name|Byte
name|sourceAddrTon
parameter_list|)
block|{
name|this
operator|.
name|sourceAddrTon
operator|=
name|sourceAddrTon
expr_stmt|;
block|}
DECL|method|getDestAddrTon ()
specifier|public
name|Byte
name|getDestAddrTon
parameter_list|()
block|{
return|return
name|destAddrTon
return|;
block|}
DECL|method|setDestAddrTon (Byte destAddrTon)
specifier|public
name|void
name|setDestAddrTon
parameter_list|(
name|Byte
name|destAddrTon
parameter_list|)
block|{
name|this
operator|.
name|destAddrTon
operator|=
name|destAddrTon
expr_stmt|;
block|}
DECL|method|getSourceAddrNpi ()
specifier|public
name|Byte
name|getSourceAddrNpi
parameter_list|()
block|{
return|return
name|sourceAddrNpi
return|;
block|}
DECL|method|setSourceAddrNpi (Byte sourceAddrNpi)
specifier|public
name|void
name|setSourceAddrNpi
parameter_list|(
name|Byte
name|sourceAddrNpi
parameter_list|)
block|{
name|this
operator|.
name|sourceAddrNpi
operator|=
name|sourceAddrNpi
expr_stmt|;
block|}
DECL|method|getDestAddrNpi ()
specifier|public
name|Byte
name|getDestAddrNpi
parameter_list|()
block|{
return|return
name|destAddrNpi
return|;
block|}
DECL|method|setDestAddrNpi (Byte destAddrNpi)
specifier|public
name|void
name|setDestAddrNpi
parameter_list|(
name|Byte
name|destAddrNpi
parameter_list|)
block|{
name|this
operator|.
name|destAddrNpi
operator|=
name|destAddrNpi
expr_stmt|;
block|}
DECL|method|getProtocolId ()
specifier|public
name|Byte
name|getProtocolId
parameter_list|()
block|{
return|return
name|protocolId
return|;
block|}
DECL|method|setProtocolId (Byte protocolId)
specifier|public
name|void
name|setProtocolId
parameter_list|(
name|Byte
name|protocolId
parameter_list|)
block|{
name|this
operator|.
name|protocolId
operator|=
name|protocolId
expr_stmt|;
block|}
DECL|method|getPriorityFlag ()
specifier|public
name|Byte
name|getPriorityFlag
parameter_list|()
block|{
return|return
name|priorityFlag
return|;
block|}
DECL|method|setPriorityFlag (Byte priorityFlag)
specifier|public
name|void
name|setPriorityFlag
parameter_list|(
name|Byte
name|priorityFlag
parameter_list|)
block|{
name|this
operator|.
name|priorityFlag
operator|=
name|priorityFlag
expr_stmt|;
block|}
DECL|method|getReplaceIfPresentFlag ()
specifier|public
name|Byte
name|getReplaceIfPresentFlag
parameter_list|()
block|{
return|return
name|replaceIfPresentFlag
return|;
block|}
DECL|method|setReplaceIfPresentFlag (Byte replaceIfPresentFlag)
specifier|public
name|void
name|setReplaceIfPresentFlag
parameter_list|(
name|Byte
name|replaceIfPresentFlag
parameter_list|)
block|{
name|this
operator|.
name|replaceIfPresentFlag
operator|=
name|replaceIfPresentFlag
expr_stmt|;
block|}
DECL|method|getSourceAddr ()
specifier|public
name|String
name|getSourceAddr
parameter_list|()
block|{
return|return
name|sourceAddr
return|;
block|}
DECL|method|setSourceAddr (String sourceAddr)
specifier|public
name|void
name|setSourceAddr
parameter_list|(
name|String
name|sourceAddr
parameter_list|)
block|{
name|this
operator|.
name|sourceAddr
operator|=
name|sourceAddr
expr_stmt|;
block|}
DECL|method|getDestAddr ()
specifier|public
name|String
name|getDestAddr
parameter_list|()
block|{
return|return
name|destAddr
return|;
block|}
DECL|method|setDestAddr (String destAddr)
specifier|public
name|void
name|setDestAddr
parameter_list|(
name|String
name|destAddr
parameter_list|)
block|{
name|this
operator|.
name|destAddr
operator|=
name|destAddr
expr_stmt|;
block|}
DECL|method|getTypeOfNumber ()
specifier|public
name|Byte
name|getTypeOfNumber
parameter_list|()
block|{
return|return
name|typeOfNumber
return|;
block|}
DECL|method|setTypeOfNumber (Byte typeOfNumber)
specifier|public
name|void
name|setTypeOfNumber
parameter_list|(
name|Byte
name|typeOfNumber
parameter_list|)
block|{
name|this
operator|.
name|typeOfNumber
operator|=
name|typeOfNumber
expr_stmt|;
block|}
DECL|method|getNumberingPlanIndicator ()
specifier|public
name|Byte
name|getNumberingPlanIndicator
parameter_list|()
block|{
return|return
name|numberingPlanIndicator
return|;
block|}
DECL|method|setNumberingPlanIndicator (Byte numberingPlanIndicator)
specifier|public
name|void
name|setNumberingPlanIndicator
parameter_list|(
name|Byte
name|numberingPlanIndicator
parameter_list|)
block|{
name|this
operator|.
name|numberingPlanIndicator
operator|=
name|numberingPlanIndicator
expr_stmt|;
block|}
DECL|method|getUsingSSL ()
specifier|public
name|Boolean
name|getUsingSSL
parameter_list|()
block|{
return|return
name|usingSSL
return|;
block|}
DECL|method|setUsingSSL (Boolean usingSSL)
specifier|public
name|void
name|setUsingSSL
parameter_list|(
name|Boolean
name|usingSSL
parameter_list|)
block|{
name|this
operator|.
name|usingSSL
operator|=
name|usingSSL
expr_stmt|;
block|}
DECL|method|getInitialReconnectDelay ()
specifier|public
name|Long
name|getInitialReconnectDelay
parameter_list|()
block|{
return|return
name|initialReconnectDelay
return|;
block|}
DECL|method|setInitialReconnectDelay (Long initialReconnectDelay)
specifier|public
name|void
name|setInitialReconnectDelay
parameter_list|(
name|Long
name|initialReconnectDelay
parameter_list|)
block|{
name|this
operator|.
name|initialReconnectDelay
operator|=
name|initialReconnectDelay
expr_stmt|;
block|}
DECL|method|getReconnectDelay ()
specifier|public
name|Long
name|getReconnectDelay
parameter_list|()
block|{
return|return
name|reconnectDelay
return|;
block|}
DECL|method|setReconnectDelay (Long reconnectDelay)
specifier|public
name|void
name|setReconnectDelay
parameter_list|(
name|Long
name|reconnectDelay
parameter_list|)
block|{
name|this
operator|.
name|reconnectDelay
operator|=
name|reconnectDelay
expr_stmt|;
block|}
DECL|method|getMaxReconnect ()
specifier|public
name|Integer
name|getMaxReconnect
parameter_list|()
block|{
return|return
name|maxReconnect
return|;
block|}
DECL|method|setMaxReconnect (Integer maxReconnect)
specifier|public
name|void
name|setMaxReconnect
parameter_list|(
name|Integer
name|maxReconnect
parameter_list|)
block|{
name|this
operator|.
name|maxReconnect
operator|=
name|maxReconnect
expr_stmt|;
block|}
DECL|method|getLazySessionCreation ()
specifier|public
name|Boolean
name|getLazySessionCreation
parameter_list|()
block|{
return|return
name|lazySessionCreation
return|;
block|}
DECL|method|setLazySessionCreation (Boolean lazySessionCreation)
specifier|public
name|void
name|setLazySessionCreation
parameter_list|(
name|Boolean
name|lazySessionCreation
parameter_list|)
block|{
name|this
operator|.
name|lazySessionCreation
operator|=
name|lazySessionCreation
expr_stmt|;
block|}
DECL|method|getHttpProxyHost ()
specifier|public
name|String
name|getHttpProxyHost
parameter_list|()
block|{
return|return
name|httpProxyHost
return|;
block|}
DECL|method|setHttpProxyHost (String httpProxyHost)
specifier|public
name|void
name|setHttpProxyHost
parameter_list|(
name|String
name|httpProxyHost
parameter_list|)
block|{
name|this
operator|.
name|httpProxyHost
operator|=
name|httpProxyHost
expr_stmt|;
block|}
DECL|method|getHttpProxyPort ()
specifier|public
name|Integer
name|getHttpProxyPort
parameter_list|()
block|{
return|return
name|httpProxyPort
return|;
block|}
DECL|method|setHttpProxyPort (Integer httpProxyPort)
specifier|public
name|void
name|setHttpProxyPort
parameter_list|(
name|Integer
name|httpProxyPort
parameter_list|)
block|{
name|this
operator|.
name|httpProxyPort
operator|=
name|httpProxyPort
expr_stmt|;
block|}
DECL|method|getHttpProxyUsername ()
specifier|public
name|String
name|getHttpProxyUsername
parameter_list|()
block|{
return|return
name|httpProxyUsername
return|;
block|}
DECL|method|setHttpProxyUsername (String httpProxyUsername)
specifier|public
name|void
name|setHttpProxyUsername
parameter_list|(
name|String
name|httpProxyUsername
parameter_list|)
block|{
name|this
operator|.
name|httpProxyUsername
operator|=
name|httpProxyUsername
expr_stmt|;
block|}
DECL|method|getHttpProxyPassword ()
specifier|public
name|String
name|getHttpProxyPassword
parameter_list|()
block|{
return|return
name|httpProxyPassword
return|;
block|}
DECL|method|setHttpProxyPassword (String httpProxyPassword)
specifier|public
name|void
name|setHttpProxyPassword
parameter_list|(
name|String
name|httpProxyPassword
parameter_list|)
block|{
name|this
operator|.
name|httpProxyPassword
operator|=
name|httpProxyPassword
expr_stmt|;
block|}
DECL|method|getSessionStateListener ()
specifier|public
name|SessionStateListener
name|getSessionStateListener
parameter_list|()
block|{
return|return
name|sessionStateListener
return|;
block|}
DECL|method|setSessionStateListener ( SessionStateListener sessionStateListener)
specifier|public
name|void
name|setSessionStateListener
parameter_list|(
name|SessionStateListener
name|sessionStateListener
parameter_list|)
block|{
name|this
operator|.
name|sessionStateListener
operator|=
name|sessionStateListener
expr_stmt|;
block|}
DECL|method|getAddressRange ()
specifier|public
name|String
name|getAddressRange
parameter_list|()
block|{
return|return
name|addressRange
return|;
block|}
DECL|method|setAddressRange (String addressRange)
specifier|public
name|void
name|setAddressRange
parameter_list|(
name|String
name|addressRange
parameter_list|)
block|{
name|this
operator|.
name|addressRange
operator|=
name|addressRange
expr_stmt|;
block|}
DECL|method|getSplittingPolicy ()
specifier|public
name|SmppSplittingPolicy
name|getSplittingPolicy
parameter_list|()
block|{
return|return
name|splittingPolicy
return|;
block|}
DECL|method|setSplittingPolicy (SmppSplittingPolicy splittingPolicy)
specifier|public
name|void
name|setSplittingPolicy
parameter_list|(
name|SmppSplittingPolicy
name|splittingPolicy
parameter_list|)
block|{
name|this
operator|.
name|splittingPolicy
operator|=
name|splittingPolicy
expr_stmt|;
block|}
DECL|method|getProxyHeaders ()
specifier|public
name|Map
name|getProxyHeaders
parameter_list|()
block|{
return|return
name|proxyHeaders
return|;
block|}
DECL|method|setProxyHeaders (Map proxyHeaders)
specifier|public
name|void
name|setProxyHeaders
parameter_list|(
name|Map
name|proxyHeaders
parameter_list|)
block|{
name|this
operator|.
name|proxyHeaders
operator|=
name|proxyHeaders
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

