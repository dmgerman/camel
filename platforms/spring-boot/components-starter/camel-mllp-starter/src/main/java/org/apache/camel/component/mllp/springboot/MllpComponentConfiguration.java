begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|springboot
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
name|DeprecatedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The MLLP component is designed to handle the MLLP protocol and provide the  * functionality required by Healthcare providers to communicate with other  * systems using the MLLP protocol.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.mllp"
argument_list|)
DECL|class|MllpComponentConfiguration
specifier|public
class|class
name|MllpComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Set the component to log PHI data.      */
DECL|field|logPhi
specifier|private
name|Boolean
name|logPhi
decl_stmt|;
comment|/**      * Set the maximum number of bytes of PHI that will be logged in a log      * entry.      */
DECL|field|logPhiMaxBytes
specifier|private
name|Integer
name|logPhiMaxBytes
decl_stmt|;
comment|/**      * Sets the default configuration to use when creating MLLP endpoints.      */
DECL|field|configuration
specifier|private
name|MllpConfigurationNestedConfiguration
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
DECL|method|getLogPhi ()
specifier|public
name|Boolean
name|getLogPhi
parameter_list|()
block|{
return|return
name|logPhi
return|;
block|}
DECL|method|setLogPhi (Boolean logPhi)
specifier|public
name|void
name|setLogPhi
parameter_list|(
name|Boolean
name|logPhi
parameter_list|)
block|{
name|this
operator|.
name|logPhi
operator|=
name|logPhi
expr_stmt|;
block|}
DECL|method|getLogPhiMaxBytes ()
specifier|public
name|Integer
name|getLogPhiMaxBytes
parameter_list|()
block|{
return|return
name|logPhiMaxBytes
return|;
block|}
DECL|method|setLogPhiMaxBytes (Integer logPhiMaxBytes)
specifier|public
name|void
name|setLogPhiMaxBytes
parameter_list|(
name|Integer
name|logPhiMaxBytes
parameter_list|)
block|{
name|this
operator|.
name|logPhiMaxBytes
operator|=
name|logPhiMaxBytes
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|MllpConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( MllpConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|MllpConfigurationNestedConfiguration
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
DECL|class|MllpConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|MllpConfigurationNestedConfiguration
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
name|mllp
operator|.
name|MllpConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * Set the CamelCharsetName property on the exchange          *           * @param charsetName          *            the charset          */
DECL|field|charsetName
specifier|private
name|String
name|charsetName
decl_stmt|;
comment|/**          * The maximum queue length for incoming connection indications (a          * request to connect) is set to the backlog parameter. If a connection          * indication arrives when the queue is full, the connection is refused.          */
DECL|field|backlog
specifier|private
name|Integer
name|backlog
init|=
literal|5
decl_stmt|;
comment|/**          * TCP Server Only - The number of milliseconds to retry binding to a          * server port          */
DECL|field|bindTimeout
specifier|private
name|Integer
name|bindTimeout
init|=
literal|30000
decl_stmt|;
comment|/**          * TCP Server Only - The number of milliseconds to wait between bind          * attempts          */
DECL|field|bindRetryInterval
specifier|private
name|Integer
name|bindRetryInterval
init|=
literal|5000
decl_stmt|;
comment|/**          * Timeout (in milliseconds) while waiting for a TCP connection          *<p/>          * TCP Server Only          *           * @param acceptTimeout          *            timeout in milliseconds          */
DECL|field|acceptTimeout
specifier|private
name|Integer
name|acceptTimeout
init|=
literal|60000
decl_stmt|;
comment|/**          * Timeout (in milliseconds) for establishing for a TCP connection          *<p/>          * TCP Client only          *           * @param connectTimeout          *            timeout in milliseconds          */
DECL|field|connectTimeout
specifier|private
name|Integer
name|connectTimeout
init|=
literal|30000
decl_stmt|;
comment|/**          * The SO_TIMEOUT value (in milliseconds) used when waiting for the          * start of an MLLP frame          *           * @param receiveTimeout          *            timeout in milliseconds          */
DECL|field|receiveTimeout
specifier|private
name|Integer
name|receiveTimeout
init|=
literal|15000
decl_stmt|;
comment|/**          * The maximum number of concurrent MLLP Consumer connections that will          * be allowed. If a new connection is received and the maximum is number          * are already established, the new connection will be reset          * immediately.          *           * @param maxConcurrentConsumers          *            the maximum number of concurrent consumer connections          *            allowed          */
DECL|field|maxConcurrentConsumers
specifier|private
name|Integer
name|maxConcurrentConsumers
init|=
literal|5
decl_stmt|;
comment|/**          * The maximum number of timeouts (specified by receiveTimeout) allowed          * before the TCP Connection will be reset.          *           * @param maxReceiveTimeouts          *            maximum number of receiveTimeouts          * @deprecated Use the idleTimeout URI parameter. For backward          *             compibility, setting this parameter will result in an          *             idle timeout of maxReceiveTimeouts * receiveTimeout. If          *             idleTimeout is also specified, this parameter will be          *             ignored.          */
annotation|@
name|Deprecated
DECL|field|maxReceiveTimeouts
specifier|private
name|Integer
name|maxReceiveTimeouts
decl_stmt|;
comment|/**          * The approximate idle time allowed before the Client TCP Connection          * will be reset. A null value or a value less than or equal to zero          * will disable the idle timeout.          *           * @param idleTimeout          *            timeout in milliseconds          */
DECL|field|idleTimeout
specifier|private
name|Integer
name|idleTimeout
decl_stmt|;
comment|/**          * The SO_TIMEOUT value (in milliseconds) used after the start of an          * MLLP frame has been received          *           * @param readTimeout          *            timeout in milliseconds          */
DECL|field|readTimeout
specifier|private
name|Integer
name|readTimeout
init|=
literal|500
decl_stmt|;
comment|/**          * Enable/disable the SO_KEEPALIVE socket option.          *           * @param keepAlive          *            enable SO_KEEPALIVE when true; disable SO_KEEPALIVE when          *            false; use system default when null          */
DECL|field|keepAlive
specifier|private
name|Boolean
name|keepAlive
init|=
literal|true
decl_stmt|;
comment|/**          * Enable/disable the TCP_NODELAY socket option.          *           * @param tcpNoDelay          *            enable TCP_NODELAY when true; disable TCP_NODELAY when          *            false; use system default when null          */
DECL|field|tcpNoDelay
specifier|private
name|Boolean
name|tcpNoDelay
init|=
literal|true
decl_stmt|;
comment|/**          * Sets the SO_RCVBUF option to the specified value (in bytes)          *           * @param receiveBufferSize          *            the SO_RCVBUF option value. If null, the system default is          *            used          */
DECL|field|receiveBufferSize
specifier|private
name|Integer
name|receiveBufferSize
init|=
literal|8192
decl_stmt|;
comment|/**          * Sets the SO_SNDBUF option to the specified value (in bytes)          *           * @param sendBufferSize          *            the SO_SNDBUF option value. If null, the system default is          *            used          */
DECL|field|sendBufferSize
specifier|private
name|Integer
name|sendBufferSize
init|=
literal|8192
decl_stmt|;
comment|/**          * Enable/Disable the automatic generation of a MLLP Acknowledgement          * MLLP Consumers only          *           * @param autoAck          *            enabled if true, otherwise disabled          */
DECL|field|autoAck
specifier|private
name|Boolean
name|autoAck
init|=
literal|true
decl_stmt|;
comment|/**          * Enable/Disable the automatic generation of message headers from the          * HL7 Message MLLP Consumers only          *           * @param hl7Headers          *            enabled if true, otherwise disabled          */
DECL|field|hl7Headers
specifier|private
name|Boolean
name|hl7Headers
init|=
literal|true
decl_stmt|;
comment|/**          * Enable disable strict compliance to the MLLP standard. The MLLP          * standard specifies [START_OF_BLOCK]hl7          * payload[END_OF_BLOCK][END_OF_DATA], however, some systems do not send          * the final END_OF_DATA byte. This setting controls whether or not the          * final END_OF_DATA byte is required or optional.          *           * @param requireEndOfData          *            the trailing END_OF_DATA byte is required if true;          *            optional otherwise          */
DECL|field|requireEndOfData
specifier|private
name|Boolean
name|requireEndOfData
init|=
literal|true
decl_stmt|;
comment|/**          * Enable/Disable converting the payload to a String. If enabled, HL7          * Payloads received from external systems will be validated converted          * to a String. If the charsetName property is set, that character set          * will be used for the conversion. If the charsetName property is not          * set, the value of MSH-18 will be used to determine th appropriate          * character set. If MSH-18 is not set, then the default ASCII character          * set will be use.          *           * @param stringPayload          *            enabled if true, otherwise disabled          */
DECL|field|stringPayload
specifier|private
name|Boolean
name|stringPayload
init|=
literal|true
decl_stmt|;
comment|/**          * Enable/Disable the validation of HL7 Payloads If enabled, HL7          * Payloads received from external systems will be validated (see          * Hl7Util.generateInvalidPayloadExceptionMessage for details on the          * validation). If and invalid payload is detected, a          * MllpInvalidMessageException (for consumers) or a          * MllpInvalidAcknowledgementException will be thrown.          *           * @param validatePayload          *            enabled if true, otherwise disabled          */
DECL|field|validatePayload
specifier|private
name|Boolean
name|validatePayload
init|=
literal|false
decl_stmt|;
comment|/**          * Enable/Disable the validation of HL7 Payloads          *           * @deprecated the parameter will be ignored          * @param bufferWrites          *            enabled if true, otherwise disabled          */
annotation|@
name|Deprecated
DECL|field|bufferWrites
specifier|private
name|Boolean
name|bufferWrites
init|=
literal|false
decl_stmt|;
DECL|method|getCharsetName ()
specifier|public
name|String
name|getCharsetName
parameter_list|()
block|{
return|return
name|charsetName
return|;
block|}
DECL|method|setCharsetName (String charsetName)
specifier|public
name|void
name|setCharsetName
parameter_list|(
name|String
name|charsetName
parameter_list|)
block|{
name|this
operator|.
name|charsetName
operator|=
name|charsetName
expr_stmt|;
block|}
DECL|method|getBacklog ()
specifier|public
name|Integer
name|getBacklog
parameter_list|()
block|{
return|return
name|backlog
return|;
block|}
DECL|method|setBacklog (Integer backlog)
specifier|public
name|void
name|setBacklog
parameter_list|(
name|Integer
name|backlog
parameter_list|)
block|{
name|this
operator|.
name|backlog
operator|=
name|backlog
expr_stmt|;
block|}
DECL|method|getBindTimeout ()
specifier|public
name|Integer
name|getBindTimeout
parameter_list|()
block|{
return|return
name|bindTimeout
return|;
block|}
DECL|method|setBindTimeout (Integer bindTimeout)
specifier|public
name|void
name|setBindTimeout
parameter_list|(
name|Integer
name|bindTimeout
parameter_list|)
block|{
name|this
operator|.
name|bindTimeout
operator|=
name|bindTimeout
expr_stmt|;
block|}
DECL|method|getBindRetryInterval ()
specifier|public
name|Integer
name|getBindRetryInterval
parameter_list|()
block|{
return|return
name|bindRetryInterval
return|;
block|}
DECL|method|setBindRetryInterval (Integer bindRetryInterval)
specifier|public
name|void
name|setBindRetryInterval
parameter_list|(
name|Integer
name|bindRetryInterval
parameter_list|)
block|{
name|this
operator|.
name|bindRetryInterval
operator|=
name|bindRetryInterval
expr_stmt|;
block|}
DECL|method|getAcceptTimeout ()
specifier|public
name|Integer
name|getAcceptTimeout
parameter_list|()
block|{
return|return
name|acceptTimeout
return|;
block|}
DECL|method|setAcceptTimeout (Integer acceptTimeout)
specifier|public
name|void
name|setAcceptTimeout
parameter_list|(
name|Integer
name|acceptTimeout
parameter_list|)
block|{
name|this
operator|.
name|acceptTimeout
operator|=
name|acceptTimeout
expr_stmt|;
block|}
DECL|method|getConnectTimeout ()
specifier|public
name|Integer
name|getConnectTimeout
parameter_list|()
block|{
return|return
name|connectTimeout
return|;
block|}
DECL|method|setConnectTimeout (Integer connectTimeout)
specifier|public
name|void
name|setConnectTimeout
parameter_list|(
name|Integer
name|connectTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectTimeout
operator|=
name|connectTimeout
expr_stmt|;
block|}
DECL|method|getReceiveTimeout ()
specifier|public
name|Integer
name|getReceiveTimeout
parameter_list|()
block|{
return|return
name|receiveTimeout
return|;
block|}
DECL|method|setReceiveTimeout (Integer receiveTimeout)
specifier|public
name|void
name|setReceiveTimeout
parameter_list|(
name|Integer
name|receiveTimeout
parameter_list|)
block|{
name|this
operator|.
name|receiveTimeout
operator|=
name|receiveTimeout
expr_stmt|;
block|}
DECL|method|getMaxConcurrentConsumers ()
specifier|public
name|Integer
name|getMaxConcurrentConsumers
parameter_list|()
block|{
return|return
name|maxConcurrentConsumers
return|;
block|}
DECL|method|setMaxConcurrentConsumers (Integer maxConcurrentConsumers)
specifier|public
name|void
name|setMaxConcurrentConsumers
parameter_list|(
name|Integer
name|maxConcurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|maxConcurrentConsumers
operator|=
name|maxConcurrentConsumers
expr_stmt|;
block|}
annotation|@
name|Deprecated
annotation|@
name|DeprecatedConfigurationProperty
DECL|method|getMaxReceiveTimeouts ()
specifier|public
name|Integer
name|getMaxReceiveTimeouts
parameter_list|()
block|{
return|return
name|maxReceiveTimeouts
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setMaxReceiveTimeouts (Integer maxReceiveTimeouts)
specifier|public
name|void
name|setMaxReceiveTimeouts
parameter_list|(
name|Integer
name|maxReceiveTimeouts
parameter_list|)
block|{
name|this
operator|.
name|maxReceiveTimeouts
operator|=
name|maxReceiveTimeouts
expr_stmt|;
block|}
DECL|method|getIdleTimeout ()
specifier|public
name|Integer
name|getIdleTimeout
parameter_list|()
block|{
return|return
name|idleTimeout
return|;
block|}
DECL|method|setIdleTimeout (Integer idleTimeout)
specifier|public
name|void
name|setIdleTimeout
parameter_list|(
name|Integer
name|idleTimeout
parameter_list|)
block|{
name|this
operator|.
name|idleTimeout
operator|=
name|idleTimeout
expr_stmt|;
block|}
DECL|method|getReadTimeout ()
specifier|public
name|Integer
name|getReadTimeout
parameter_list|()
block|{
return|return
name|readTimeout
return|;
block|}
DECL|method|setReadTimeout (Integer readTimeout)
specifier|public
name|void
name|setReadTimeout
parameter_list|(
name|Integer
name|readTimeout
parameter_list|)
block|{
name|this
operator|.
name|readTimeout
operator|=
name|readTimeout
expr_stmt|;
block|}
DECL|method|getKeepAlive ()
specifier|public
name|Boolean
name|getKeepAlive
parameter_list|()
block|{
return|return
name|keepAlive
return|;
block|}
DECL|method|setKeepAlive (Boolean keepAlive)
specifier|public
name|void
name|setKeepAlive
parameter_list|(
name|Boolean
name|keepAlive
parameter_list|)
block|{
name|this
operator|.
name|keepAlive
operator|=
name|keepAlive
expr_stmt|;
block|}
DECL|method|getTcpNoDelay ()
specifier|public
name|Boolean
name|getTcpNoDelay
parameter_list|()
block|{
return|return
name|tcpNoDelay
return|;
block|}
DECL|method|setTcpNoDelay (Boolean tcpNoDelay)
specifier|public
name|void
name|setTcpNoDelay
parameter_list|(
name|Boolean
name|tcpNoDelay
parameter_list|)
block|{
name|this
operator|.
name|tcpNoDelay
operator|=
name|tcpNoDelay
expr_stmt|;
block|}
DECL|method|getReceiveBufferSize ()
specifier|public
name|Integer
name|getReceiveBufferSize
parameter_list|()
block|{
return|return
name|receiveBufferSize
return|;
block|}
DECL|method|setReceiveBufferSize (Integer receiveBufferSize)
specifier|public
name|void
name|setReceiveBufferSize
parameter_list|(
name|Integer
name|receiveBufferSize
parameter_list|)
block|{
name|this
operator|.
name|receiveBufferSize
operator|=
name|receiveBufferSize
expr_stmt|;
block|}
DECL|method|getSendBufferSize ()
specifier|public
name|Integer
name|getSendBufferSize
parameter_list|()
block|{
return|return
name|sendBufferSize
return|;
block|}
DECL|method|setSendBufferSize (Integer sendBufferSize)
specifier|public
name|void
name|setSendBufferSize
parameter_list|(
name|Integer
name|sendBufferSize
parameter_list|)
block|{
name|this
operator|.
name|sendBufferSize
operator|=
name|sendBufferSize
expr_stmt|;
block|}
DECL|method|getAutoAck ()
specifier|public
name|Boolean
name|getAutoAck
parameter_list|()
block|{
return|return
name|autoAck
return|;
block|}
DECL|method|setAutoAck (Boolean autoAck)
specifier|public
name|void
name|setAutoAck
parameter_list|(
name|Boolean
name|autoAck
parameter_list|)
block|{
name|this
operator|.
name|autoAck
operator|=
name|autoAck
expr_stmt|;
block|}
DECL|method|getHl7Headers ()
specifier|public
name|Boolean
name|getHl7Headers
parameter_list|()
block|{
return|return
name|hl7Headers
return|;
block|}
DECL|method|setHl7Headers (Boolean hl7Headers)
specifier|public
name|void
name|setHl7Headers
parameter_list|(
name|Boolean
name|hl7Headers
parameter_list|)
block|{
name|this
operator|.
name|hl7Headers
operator|=
name|hl7Headers
expr_stmt|;
block|}
DECL|method|getRequireEndOfData ()
specifier|public
name|Boolean
name|getRequireEndOfData
parameter_list|()
block|{
return|return
name|requireEndOfData
return|;
block|}
DECL|method|setRequireEndOfData (Boolean requireEndOfData)
specifier|public
name|void
name|setRequireEndOfData
parameter_list|(
name|Boolean
name|requireEndOfData
parameter_list|)
block|{
name|this
operator|.
name|requireEndOfData
operator|=
name|requireEndOfData
expr_stmt|;
block|}
DECL|method|getStringPayload ()
specifier|public
name|Boolean
name|getStringPayload
parameter_list|()
block|{
return|return
name|stringPayload
return|;
block|}
DECL|method|setStringPayload (Boolean stringPayload)
specifier|public
name|void
name|setStringPayload
parameter_list|(
name|Boolean
name|stringPayload
parameter_list|)
block|{
name|this
operator|.
name|stringPayload
operator|=
name|stringPayload
expr_stmt|;
block|}
DECL|method|getValidatePayload ()
specifier|public
name|Boolean
name|getValidatePayload
parameter_list|()
block|{
return|return
name|validatePayload
return|;
block|}
DECL|method|setValidatePayload (Boolean validatePayload)
specifier|public
name|void
name|setValidatePayload
parameter_list|(
name|Boolean
name|validatePayload
parameter_list|)
block|{
name|this
operator|.
name|validatePayload
operator|=
name|validatePayload
expr_stmt|;
block|}
annotation|@
name|Deprecated
annotation|@
name|DeprecatedConfigurationProperty
DECL|method|getBufferWrites ()
specifier|public
name|Boolean
name|getBufferWrites
parameter_list|()
block|{
return|return
name|bufferWrites
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setBufferWrites (Boolean bufferWrites)
specifier|public
name|void
name|setBufferWrites
parameter_list|(
name|Boolean
name|bufferWrites
parameter_list|)
block|{
name|this
operator|.
name|bufferWrites
operator|=
name|bufferWrites
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

