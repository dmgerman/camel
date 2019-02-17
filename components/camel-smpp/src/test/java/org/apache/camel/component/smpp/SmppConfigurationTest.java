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
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|extra
operator|.
name|SessionState
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
name|Session
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
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  * JUnit test class for<code>org.apache.camel.component.smpp.SmppConfiguration</code>  */
end_comment

begin_class
DECL|class|SmppConfigurationTest
specifier|public
class|class
name|SmppConfigurationTest
block|{
DECL|field|configuration
specifier|private
name|SmppConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|configuration
operator|=
operator|new
name|SmppConfiguration
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|setServiceType
argument_list|(
literal|"CMT"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getterShouldReturnTheDefaultValues ()
specifier|public
name|void
name|getterShouldReturnTheDefaultValues
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"1717"
argument_list|,
name|configuration
operator|.
name|getDestAddr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x00
argument_list|,
name|configuration
operator|.
name|getDestAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x00
argument_list|,
name|configuration
operator|.
name|getDestAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|configuration
operator|.
name|getAddressRange
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|5000
argument_list|)
argument_list|,
name|configuration
operator|.
name|getEnquireLinkTimer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"password"
argument_list|,
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2775
argument_list|)
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x01
argument_list|,
name|configuration
operator|.
name|getPriorityFlag
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x00
argument_list|,
name|configuration
operator|.
name|getProtocolId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x01
argument_list|,
name|configuration
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x00
argument_list|,
name|configuration
operator|.
name|getReplaceIfPresentFlag
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CMT"
argument_list|,
name|configuration
operator|.
name|getServiceType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1616"
argument_list|,
name|configuration
operator|.
name|getSourceAddr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x00
argument_list|,
name|configuration
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x00
argument_list|,
name|configuration
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"smppclient"
argument_list|,
name|configuration
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|configuration
operator|.
name|getSystemType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|10000
argument_list|)
argument_list|,
name|configuration
operator|.
name|getTransactionTimer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ISO-8859-1"
argument_list|,
name|configuration
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x00
argument_list|,
name|configuration
operator|.
name|getNumberingPlanIndicator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x00
argument_list|,
name|configuration
operator|.
name|getTypeOfNumber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|configuration
operator|.
name|getUsingSSL
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5000
argument_list|,
name|configuration
operator|.
name|getInitialReconnectDelay
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5000
argument_list|,
name|configuration
operator|.
name|getReconnectDelay
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|configuration
operator|.
name|getHttpProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|3128
argument_list|)
argument_list|,
name|configuration
operator|.
name|getHttpProxyPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|configuration
operator|.
name|getHttpProxyUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|configuration
operator|.
name|getHttpProxyPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|configuration
operator|.
name|getSessionStateListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getterShouldReturnTheSetValues ()
specifier|public
name|void
name|getterShouldReturnTheSetValues
parameter_list|()
block|{
name|setNoneDefaultValues
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1919"
argument_list|,
name|configuration
operator|.
name|getDestAddr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x08
argument_list|,
name|configuration
operator|.
name|getDestAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x02
argument_list|,
name|configuration
operator|.
name|getDestAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|5001
argument_list|)
argument_list|,
name|configuration
operator|.
name|getEnquireLinkTimer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"secret"
argument_list|,
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2776
argument_list|)
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x00
argument_list|,
name|configuration
operator|.
name|getPriorityFlag
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x01
argument_list|,
name|configuration
operator|.
name|getProtocolId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x00
argument_list|,
name|configuration
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x01
argument_list|,
name|configuration
operator|.
name|getReplaceIfPresentFlag
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XXX"
argument_list|,
name|configuration
operator|.
name|getServiceType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1818"
argument_list|,
name|configuration
operator|.
name|getSourceAddr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x08
argument_list|,
name|configuration
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x02
argument_list|,
name|configuration
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"client"
argument_list|,
name|configuration
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xx"
argument_list|,
name|configuration
operator|.
name|getSystemType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|10001
argument_list|)
argument_list|,
name|configuration
operator|.
name|getTransactionTimer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"UTF-8"
argument_list|,
name|configuration
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x08
argument_list|,
name|configuration
operator|.
name|getNumberingPlanIndicator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x02
argument_list|,
name|configuration
operator|.
name|getTypeOfNumber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|configuration
operator|.
name|getUsingSSL
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5001
argument_list|,
name|configuration
operator|.
name|getInitialReconnectDelay
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5002
argument_list|,
name|configuration
operator|.
name|getReconnectDelay
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|configuration
operator|.
name|getHttpProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|3129
argument_list|)
argument_list|,
name|configuration
operator|.
name|getHttpProxyPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"user"
argument_list|,
name|configuration
operator|.
name|getHttpProxyUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"secret"
argument_list|,
name|configuration
operator|.
name|getHttpProxyPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|configuration
operator|.
name|getSessionStateListener
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|configuration
operator|.
name|getProxyHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"X-Proxy-Header"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getterShouldReturnTheConfigureValuesFromURI ()
specifier|public
name|void
name|getterShouldReturnTheConfigureValuesFromURI
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|configuration
operator|.
name|configureFromURI
argument_list|(
operator|new
name|URI
argument_list|(
literal|"smpp://client@127.0.0.1:2776"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2776
argument_list|)
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"client"
argument_list|,
name|configuration
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|hostPortAndSystemIdFromComponentConfigurationShouldBeUsedIfAbsentFromUri ()
specifier|public
name|void
name|hostPortAndSystemIdFromComponentConfigurationShouldBeUsedIfAbsentFromUri
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|configuration
operator|.
name|setHost
argument_list|(
literal|"host"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setPort
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setSystemId
argument_list|(
literal|"systemId"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|configureFromURI
argument_list|(
operator|new
name|URI
argument_list|(
literal|"smpp://?password=pw"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"host"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|123
argument_list|)
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"systemId"
argument_list|,
name|configuration
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cloneShouldReturnAnEqualInstance ()
specifier|public
name|void
name|cloneShouldReturnAnEqualInstance
parameter_list|()
block|{
name|setNoneDefaultValues
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|SmppConfiguration
name|config
init|=
name|configuration
operator|.
name|copy
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getDestAddr
argument_list|()
argument_list|,
name|configuration
operator|.
name|getDestAddr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getDestAddrNpi
argument_list|()
argument_list|,
name|configuration
operator|.
name|getDestAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getDestAddrTon
argument_list|()
argument_list|,
name|configuration
operator|.
name|getDestAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getEnquireLinkTimer
argument_list|()
argument_list|,
name|configuration
operator|.
name|getEnquireLinkTimer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getPassword
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getPort
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getPriorityFlag
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPriorityFlag
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getProtocolId
argument_list|()
argument_list|,
name|configuration
operator|.
name|getProtocolId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|,
name|configuration
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getReplaceIfPresentFlag
argument_list|()
argument_list|,
name|configuration
operator|.
name|getReplaceIfPresentFlag
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getServiceType
argument_list|()
argument_list|,
name|configuration
operator|.
name|getServiceType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getSourceAddr
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSourceAddr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getSystemId
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getSystemType
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSystemType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getTransactionTimer
argument_list|()
argument_list|,
name|configuration
operator|.
name|getTransactionTimer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getEncoding
argument_list|()
argument_list|,
name|configuration
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getNumberingPlanIndicator
argument_list|()
argument_list|,
name|configuration
operator|.
name|getNumberingPlanIndicator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getTypeOfNumber
argument_list|()
argument_list|,
name|configuration
operator|.
name|getTypeOfNumber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getUsingSSL
argument_list|()
argument_list|,
name|configuration
operator|.
name|getUsingSSL
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getInitialReconnectDelay
argument_list|()
argument_list|,
name|configuration
operator|.
name|getInitialReconnectDelay
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getReconnectDelay
argument_list|()
argument_list|,
name|configuration
operator|.
name|getReconnectDelay
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getHttpProxyHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getHttpProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getHttpProxyPort
argument_list|()
argument_list|,
name|configuration
operator|.
name|getHttpProxyPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getHttpProxyUsername
argument_list|()
argument_list|,
name|configuration
operator|.
name|getHttpProxyUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getHttpProxyPassword
argument_list|()
argument_list|,
name|configuration
operator|.
name|getHttpProxyPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getSessionStateListener
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSessionStateListener
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getProxyHeaders
argument_list|()
argument_list|,
name|configuration
operator|.
name|getProxyHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|toStringShouldListAllInstanceVariables ()
specifier|public
name|void
name|toStringShouldListAllInstanceVariables
parameter_list|()
block|{
name|String
name|expected
init|=
literal|"SmppConfiguration["
operator|+
literal|"usingSSL=false, "
operator|+
literal|"enquireLinkTimer=5000, "
operator|+
literal|"host=localhost, "
operator|+
literal|"password=password, "
operator|+
literal|"port=2775, "
operator|+
literal|"systemId=smppclient, "
operator|+
literal|"systemType=, "
operator|+
literal|"dataCoding=0, "
operator|+
literal|"alphabet=0, "
operator|+
literal|"encoding=ISO-8859-1, "
operator|+
literal|"transactionTimer=10000, "
operator|+
literal|"registeredDelivery=1, "
operator|+
literal|"serviceType=CMT, "
operator|+
literal|"sourceAddrTon=0, "
operator|+
literal|"destAddrTon=0, "
operator|+
literal|"sourceAddrNpi=0, "
operator|+
literal|"destAddrNpi=0, "
operator|+
literal|"addressRange=, "
operator|+
literal|"protocolId=0, "
operator|+
literal|"priorityFlag=1, "
operator|+
literal|"replaceIfPresentFlag=0, "
operator|+
literal|"sourceAddr=1616, "
operator|+
literal|"destAddr=1717, "
operator|+
literal|"typeOfNumber=0, "
operator|+
literal|"numberingPlanIndicator=0, "
operator|+
literal|"initialReconnectDelay=5000, "
operator|+
literal|"reconnectDelay=5000, "
operator|+
literal|"maxReconnect=2147483647, "
operator|+
literal|"lazySessionCreation=false, "
operator|+
literal|"httpProxyHost=null, "
operator|+
literal|"httpProxyPort=3128, "
operator|+
literal|"httpProxyUsername=null, "
operator|+
literal|"httpProxyPassword=null, "
operator|+
literal|"splittingPolicy=ALLOW, "
operator|+
literal|"proxyHeaders=null]"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|configuration
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setNoneDefaultValues (SmppConfiguration config)
specifier|private
name|void
name|setNoneDefaultValues
parameter_list|(
name|SmppConfiguration
name|config
parameter_list|)
block|{
name|config
operator|.
name|setDestAddr
argument_list|(
literal|"1919"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setDestAddrNpi
argument_list|(
name|NumberingPlanIndicator
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setDestAddrTon
argument_list|(
name|TypeOfNumber
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setEnquireLinkTimer
argument_list|(
operator|new
name|Integer
argument_list|(
literal|5001
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|setHost
argument_list|(
literal|"127.0.0.1"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPassword
argument_list|(
literal|"secret"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPort
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2776
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPriorityFlag
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
expr_stmt|;
name|config
operator|.
name|setProtocolId
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|)
expr_stmt|;
name|config
operator|.
name|setRegisteredDelivery
argument_list|(
name|SMSCDeliveryReceipt
operator|.
name|DEFAULT
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setReplaceIfPresentFlag
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|)
expr_stmt|;
name|config
operator|.
name|setServiceType
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSourceAddr
argument_list|(
literal|"1818"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSourceAddrNpi
argument_list|(
name|NumberingPlanIndicator
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSourceAddrTon
argument_list|(
name|TypeOfNumber
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSystemId
argument_list|(
literal|"client"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSystemType
argument_list|(
literal|"xx"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setTransactionTimer
argument_list|(
operator|new
name|Integer
argument_list|(
literal|10001
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|setEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setNumberingPlanIndicator
argument_list|(
name|NumberingPlanIndicator
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setTypeOfNumber
argument_list|(
name|TypeOfNumber
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUsingSSL
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|setInitialReconnectDelay
argument_list|(
literal|5001
argument_list|)
expr_stmt|;
name|config
operator|.
name|setReconnectDelay
argument_list|(
literal|5002
argument_list|)
expr_stmt|;
name|config
operator|.
name|setHttpProxyHost
argument_list|(
literal|"127.0.0.1"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setHttpProxyPort
argument_list|(
operator|new
name|Integer
argument_list|(
literal|3129
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|setHttpProxyUsername
argument_list|(
literal|"user"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setHttpProxyPassword
argument_list|(
literal|"secret"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSessionStateListener
argument_list|(
operator|new
name|SessionStateListener
argument_list|()
block|{
specifier|public
name|void
name|onStateChange
parameter_list|(
name|SessionState
name|arg0
parameter_list|,
name|SessionState
name|arg1
parameter_list|,
name|Session
name|arg2
parameter_list|)
block|{             }
block|}
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|proxyHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|proxyHeaders
operator|.
name|put
argument_list|(
literal|"X-Proxy-Header"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setProxyHeaders
argument_list|(
name|proxyHeaders
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

