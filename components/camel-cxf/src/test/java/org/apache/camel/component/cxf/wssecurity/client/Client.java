begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.wssecurity.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|wssecurity
operator|.
name|client
package|;
end_package

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
name|apache
operator|.
name|camel
operator|.
name|hello_world_soap_http
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|WSS4JInInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|WSS4JOutInterceptor
import|;
end_import

begin_class
DECL|class|Client
specifier|public
specifier|final
class|class
name|Client
block|{
comment|//private static final String WSU_NS
comment|//    = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
DECL|field|bean
specifier|private
name|JaxWsProxyFactoryBean
name|bean
decl_stmt|;
DECL|method|Client (String address)
specifier|public
name|Client
parameter_list|(
name|String
name|address
parameter_list|)
throws|throws
name|Exception
block|{
name|bean
operator|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
expr_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|getWSS4JInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|getWSS4JOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|getClient ()
specifier|public
name|Greeter
name|getClient
parameter_list|()
block|{
return|return
name|bean
operator|.
name|create
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getWSS4JOutInterceptor ()
specifier|public
specifier|static
name|WSS4JOutInterceptor
name|getWSS4JOutInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProps
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"action"
argument_list|,
literal|"Signature"
argument_list|)
expr_stmt|;
comment|// outProps.put("action", "UsernameToken Timestamp Signature Encrypt");
name|outProps
operator|.
name|put
argument_list|(
literal|"passwordType"
argument_list|,
literal|"PasswordDigest"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"user"
argument_list|,
literal|"clientx509v1"
argument_list|)
expr_stmt|;
comment|// If you are using the patch WSS-194, then uncomment below two lines
comment|// and comment the above "user" prop line.
comment|// outProps.put("user", "abcd");
comment|// outProps.put("signatureUser", "clientx509v1");
name|outProps
operator|.
name|put
argument_list|(
literal|"passwordCallbackClass"
argument_list|,
literal|"org.apache.camel.component.cxf.wssecurity.client.UTPasswordCallback"
argument_list|)
expr_stmt|;
comment|// outProps.put("encryptionUser", "serverx509v1");
comment|// outProps.put("encryptionPropFile",
comment|// "wssecurity/etc/Client_Encrypt.properties");
comment|// outProps.put("encryptionKeyIdentifier", "IssuerSerial");
comment|// outProps.put("encryptionParts",
comment|// "{Element}{" + WSU_NS + "}Timestamp;"
comment|// + "{Content}{http://schemas.xmlsoap.org/soap/envelope/}Body");
name|outProps
operator|.
name|put
argument_list|(
literal|"signaturePropFile"
argument_list|,
literal|"wssecurity/etc/Client_Sign.properties"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signatureKeyIdentifier"
argument_list|,
literal|"DirectReference"
argument_list|)
expr_stmt|;
name|outProps
operator|.
name|put
argument_list|(
literal|"signatureParts"
argument_list|,
comment|// "{Element}{" + WSU_NS + "}Timestamp;"
literal|"{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body"
argument_list|)
expr_stmt|;
return|return
operator|new
name|WSS4JOutInterceptor
argument_list|(
name|outProps
argument_list|)
return|;
block|}
DECL|method|getWSS4JInInterceptor ()
specifier|public
specifier|static
name|WSS4JInInterceptor
name|getWSS4JInInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inProps
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"action"
argument_list|,
literal|"Signature"
argument_list|)
expr_stmt|;
comment|// inProps.put("action", "UsernameToken Timestamp Signature Encrypt");
comment|// inProps.put("passwordType", "PasswordText");
comment|// inProps.put("passwordCallbackClass",
comment|// "org.apache.camel.component.cxf.wssecurity.client.UTPasswordCallback");
comment|// inProps.put("decryptionPropFile",
comment|// "wssecurity/etc/Client_Sign.properties");
comment|// inProps.put("encryptionKeyIdentifier", "IssuerSerial");
name|inProps
operator|.
name|put
argument_list|(
literal|"signaturePropFile"
argument_list|,
literal|"wssecurity/etc/Client_Encrypt.properties"
argument_list|)
expr_stmt|;
name|inProps
operator|.
name|put
argument_list|(
literal|"signatureKeyIdentifier"
argument_list|,
literal|"DirectReference"
argument_list|)
expr_stmt|;
return|return
operator|new
name|WSS4JInInterceptor
argument_list|(
name|inProps
argument_list|)
return|;
block|}
block|}
end_class

end_unit

