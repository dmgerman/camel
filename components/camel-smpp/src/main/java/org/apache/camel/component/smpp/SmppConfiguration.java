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
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|RuntimeCamelException
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
name|Alphabet
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
name|NumberingPlanIndicator
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
name|ReplaceIfPresentFlag
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
name|SMSCDeliveryReceipt
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
name|TypeOfNumber
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Contains the SMPP component configuration properties</a>  *   * @version   */
end_comment

begin_class
DECL|class|SmppConfiguration
specifier|public
class|class
name|SmppConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SmppConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
init|=
literal|"localhost"
decl_stmt|;
DECL|field|port
specifier|private
name|Integer
name|port
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
literal|2775
argument_list|)
decl_stmt|;
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
init|=
literal|"password"
decl_stmt|;
DECL|field|systemType
specifier|private
name|String
name|systemType
init|=
literal|"cp"
decl_stmt|;
DECL|field|dataCoding
specifier|private
name|byte
name|dataCoding
init|=
operator|(
name|byte
operator|)
literal|0
decl_stmt|;
DECL|field|alphabet
specifier|private
name|byte
name|alphabet
init|=
name|Alphabet
operator|.
name|ALPHA_DEFAULT
operator|.
name|value
argument_list|()
decl_stmt|;
DECL|field|encoding
specifier|private
name|String
name|encoding
init|=
literal|"ISO-8859-1"
decl_stmt|;
DECL|field|enquireLinkTimer
specifier|private
name|Integer
name|enquireLinkTimer
init|=
literal|5000
decl_stmt|;
DECL|field|transactionTimer
specifier|private
name|Integer
name|transactionTimer
init|=
literal|10000
decl_stmt|;
DECL|field|registeredDelivery
specifier|private
name|byte
name|registeredDelivery
init|=
name|SMSCDeliveryReceipt
operator|.
name|SUCCESS_FAILURE
operator|.
name|value
argument_list|()
decl_stmt|;
DECL|field|serviceType
specifier|private
name|String
name|serviceType
init|=
literal|"CMT"
decl_stmt|;
DECL|field|sourceAddr
specifier|private
name|String
name|sourceAddr
init|=
literal|"1616"
decl_stmt|;
DECL|field|destAddr
specifier|private
name|String
name|destAddr
init|=
literal|"1717"
decl_stmt|;
DECL|field|sourceAddrTon
specifier|private
name|byte
name|sourceAddrTon
init|=
name|TypeOfNumber
operator|.
name|UNKNOWN
operator|.
name|value
argument_list|()
decl_stmt|;
DECL|field|destAddrTon
specifier|private
name|byte
name|destAddrTon
init|=
name|TypeOfNumber
operator|.
name|UNKNOWN
operator|.
name|value
argument_list|()
decl_stmt|;
DECL|field|sourceAddrNpi
specifier|private
name|byte
name|sourceAddrNpi
init|=
name|NumberingPlanIndicator
operator|.
name|UNKNOWN
operator|.
name|value
argument_list|()
decl_stmt|;
DECL|field|destAddrNpi
specifier|private
name|byte
name|destAddrNpi
init|=
name|NumberingPlanIndicator
operator|.
name|UNKNOWN
operator|.
name|value
argument_list|()
decl_stmt|;
DECL|field|addressRange
specifier|private
name|String
name|addressRange
init|=
literal|""
decl_stmt|;
DECL|field|protocolId
specifier|private
name|byte
name|protocolId
init|=
operator|(
name|byte
operator|)
literal|0
decl_stmt|;
DECL|field|priorityFlag
specifier|private
name|byte
name|priorityFlag
init|=
operator|(
name|byte
operator|)
literal|1
decl_stmt|;
DECL|field|replaceIfPresentFlag
specifier|private
name|byte
name|replaceIfPresentFlag
init|=
name|ReplaceIfPresentFlag
operator|.
name|DEFAULT
operator|.
name|value
argument_list|()
decl_stmt|;
DECL|field|typeOfNumber
specifier|private
name|byte
name|typeOfNumber
init|=
name|TypeOfNumber
operator|.
name|UNKNOWN
operator|.
name|value
argument_list|()
decl_stmt|;
DECL|field|numberingPlanIndicator
specifier|private
name|byte
name|numberingPlanIndicator
init|=
name|NumberingPlanIndicator
operator|.
name|UNKNOWN
operator|.
name|value
argument_list|()
decl_stmt|;
DECL|field|usingSSL
specifier|private
name|boolean
name|usingSSL
decl_stmt|;
DECL|field|initialReconnectDelay
specifier|private
name|long
name|initialReconnectDelay
init|=
literal|5000
decl_stmt|;
DECL|field|reconnectDelay
specifier|private
name|long
name|reconnectDelay
init|=
literal|5000
decl_stmt|;
DECL|field|lazySessionCreation
specifier|private
name|boolean
name|lazySessionCreation
decl_stmt|;
DECL|field|httpProxyHost
specifier|private
name|String
name|httpProxyHost
decl_stmt|;
DECL|field|httpProxyPort
specifier|private
name|Integer
name|httpProxyPort
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
literal|3128
argument_list|)
decl_stmt|;
DECL|field|httpProxyUsername
specifier|private
name|String
name|httpProxyUsername
decl_stmt|;
DECL|field|httpProxyPassword
specifier|private
name|String
name|httpProxyPassword
decl_stmt|;
DECL|field|sessionStateListener
specifier|private
name|SessionStateListener
name|sessionStateListener
decl_stmt|;
DECL|field|splittingPolicy
specifier|private
name|SmppSplittingPolicy
name|splittingPolicy
init|=
name|SmppSplittingPolicy
operator|.
name|ALLOW
decl_stmt|;
comment|/**      * A POJO which contains all necessary configuration parameters for the SMPP connection      *       * @param uri the full URI of the endpoint      */
DECL|method|configureFromURI (URI uri)
specifier|public
name|void
name|configureFromURI
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|setSystemId
argument_list|(
name|uri
operator|.
name|getUserInfo
argument_list|()
argument_list|)
expr_stmt|;
name|setHost
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|setPort
argument_list|(
name|uri
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|uri
operator|.
name|getScheme
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"smpps"
argument_list|)
condition|)
block|{
name|setUsingSSL
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|copy ()
specifier|public
name|SmppConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|SmppConfiguration
operator|)
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
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
DECL|method|getDataCoding ()
specifier|public
name|byte
name|getDataCoding
parameter_list|()
block|{
return|return
name|dataCoding
return|;
block|}
DECL|method|setDataCoding (byte dataCoding)
specifier|public
name|void
name|setDataCoding
parameter_list|(
name|byte
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
name|byte
name|getAlphabet
parameter_list|()
block|{
return|return
name|alphabet
return|;
block|}
DECL|method|setAlphabet (byte alphabet)
specifier|public
name|void
name|setAlphabet
parameter_list|(
name|byte
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
if|if
condition|(
operator|!
name|Charset
operator|.
name|isSupported
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unsupported encoding \"{}\" is being set."
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
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
name|byte
name|getRegisteredDelivery
parameter_list|()
block|{
return|return
name|registeredDelivery
return|;
block|}
DECL|method|setRegisteredDelivery (byte registeredDelivery)
specifier|public
name|void
name|setRegisteredDelivery
parameter_list|(
name|byte
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
name|byte
name|getSourceAddrTon
parameter_list|()
block|{
return|return
name|sourceAddrTon
return|;
block|}
DECL|method|setSourceAddrTon (byte sourceAddrTon)
specifier|public
name|void
name|setSourceAddrTon
parameter_list|(
name|byte
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
name|byte
name|getDestAddrTon
parameter_list|()
block|{
return|return
name|destAddrTon
return|;
block|}
DECL|method|setDestAddrTon (byte destAddrTon)
specifier|public
name|void
name|setDestAddrTon
parameter_list|(
name|byte
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
name|byte
name|getSourceAddrNpi
parameter_list|()
block|{
return|return
name|sourceAddrNpi
return|;
block|}
DECL|method|setSourceAddrNpi (byte sourceAddrNpi)
specifier|public
name|void
name|setSourceAddrNpi
parameter_list|(
name|byte
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
name|byte
name|getDestAddrNpi
parameter_list|()
block|{
return|return
name|destAddrNpi
return|;
block|}
DECL|method|setDestAddrNpi (byte destAddrNpi)
specifier|public
name|void
name|setDestAddrNpi
parameter_list|(
name|byte
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
name|byte
name|getProtocolId
parameter_list|()
block|{
return|return
name|protocolId
return|;
block|}
DECL|method|setProtocolId (byte protocolId)
specifier|public
name|void
name|setProtocolId
parameter_list|(
name|byte
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
name|byte
name|getPriorityFlag
parameter_list|()
block|{
return|return
name|priorityFlag
return|;
block|}
DECL|method|setPriorityFlag (byte priorityFlag)
specifier|public
name|void
name|setPriorityFlag
parameter_list|(
name|byte
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
name|byte
name|getReplaceIfPresentFlag
parameter_list|()
block|{
return|return
name|replaceIfPresentFlag
return|;
block|}
DECL|method|setReplaceIfPresentFlag (byte replaceIfPresentFlag)
specifier|public
name|void
name|setReplaceIfPresentFlag
parameter_list|(
name|byte
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
name|byte
name|getTypeOfNumber
parameter_list|()
block|{
return|return
name|typeOfNumber
return|;
block|}
DECL|method|setTypeOfNumber (byte typeOfNumber)
specifier|public
name|void
name|setTypeOfNumber
parameter_list|(
name|byte
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
name|byte
name|getNumberingPlanIndicator
parameter_list|()
block|{
return|return
name|numberingPlanIndicator
return|;
block|}
DECL|method|setNumberingPlanIndicator (byte numberingPlanIndicator)
specifier|public
name|void
name|setNumberingPlanIndicator
parameter_list|(
name|byte
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
name|boolean
name|getUsingSSL
parameter_list|()
block|{
return|return
name|usingSSL
return|;
block|}
DECL|method|setUsingSSL (boolean usingSSL)
specifier|public
name|void
name|setUsingSSL
parameter_list|(
name|boolean
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
name|long
name|getInitialReconnectDelay
parameter_list|()
block|{
return|return
name|initialReconnectDelay
return|;
block|}
DECL|method|setInitialReconnectDelay (long initialReconnectDelay)
specifier|public
name|void
name|setInitialReconnectDelay
parameter_list|(
name|long
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
name|long
name|getReconnectDelay
parameter_list|()
block|{
return|return
name|reconnectDelay
return|;
block|}
DECL|method|setReconnectDelay (long reconnectDelay)
specifier|public
name|void
name|setReconnectDelay
parameter_list|(
name|long
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
DECL|method|isLazySessionCreation ()
specifier|public
name|boolean
name|isLazySessionCreation
parameter_list|()
block|{
return|return
name|lazySessionCreation
return|;
block|}
DECL|method|setLazySessionCreation (boolean lazySessionCreation)
specifier|public
name|void
name|setLazySessionCreation
parameter_list|(
name|boolean
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
DECL|method|setSessionStateListener (SessionStateListener sessionStateListener)
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SmppConfiguration[usingSSL="
operator|+
name|usingSSL
operator|+
literal|", enquireLinkTimer="
operator|+
name|enquireLinkTimer
operator|+
literal|", host="
operator|+
name|host
operator|+
literal|", password="
operator|+
name|password
operator|+
literal|", port="
operator|+
name|port
operator|+
literal|", systemId="
operator|+
name|systemId
operator|+
literal|", systemType="
operator|+
name|systemType
operator|+
literal|", dataCoding="
operator|+
name|dataCoding
operator|+
literal|", alphabet="
operator|+
name|alphabet
operator|+
literal|", encoding="
operator|+
name|encoding
operator|+
literal|", transactionTimer="
operator|+
name|transactionTimer
operator|+
literal|", registeredDelivery="
operator|+
name|registeredDelivery
operator|+
literal|", serviceType="
operator|+
name|serviceType
operator|+
literal|", sourceAddrTon="
operator|+
name|sourceAddrTon
operator|+
literal|", destAddrTon="
operator|+
name|destAddrTon
operator|+
literal|", sourceAddrNpi="
operator|+
name|sourceAddrNpi
operator|+
literal|", destAddrNpi="
operator|+
name|destAddrNpi
operator|+
literal|", addressRange="
operator|+
name|addressRange
operator|+
literal|", protocolId="
operator|+
name|protocolId
operator|+
literal|", priorityFlag="
operator|+
name|priorityFlag
operator|+
literal|", replaceIfPresentFlag="
operator|+
name|replaceIfPresentFlag
operator|+
literal|", sourceAddr="
operator|+
name|sourceAddr
operator|+
literal|", destAddr="
operator|+
name|destAddr
operator|+
literal|", typeOfNumber="
operator|+
name|typeOfNumber
operator|+
literal|", numberingPlanIndicator="
operator|+
name|numberingPlanIndicator
operator|+
literal|", initialReconnectDelay="
operator|+
name|initialReconnectDelay
operator|+
literal|", reconnectDelay="
operator|+
name|reconnectDelay
operator|+
literal|", lazySessionCreation="
operator|+
name|lazySessionCreation
operator|+
literal|", httpProxyHost="
operator|+
name|httpProxyHost
operator|+
literal|", httpProxyPort="
operator|+
name|httpProxyPort
operator|+
literal|", httpProxyUsername="
operator|+
name|httpProxyUsername
operator|+
literal|", httpProxyPassword="
operator|+
name|httpProxyPassword
operator|+
literal|", splittingPolicy="
operator|+
name|splittingPolicy
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

