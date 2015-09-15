begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmlsecurity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmlsecurity
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
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
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|KeySelector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|CamelContext
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
name|Message
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
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|xmlsecurity
operator|.
name|api
operator|.
name|KeyAccessor
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
name|xmlsecurity
operator|.
name|api
operator|.
name|XmlSignatureHelper
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
name|JndiRegistry
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
name|SpringCamelContext
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

begin_class
DECL|class|SpringXmlSignatureTest
specifier|public
class|class
name|SpringXmlSignatureTest
extends|extends
name|XmlSignatureTest
block|{
DECL|field|rsaPair
specifier|private
specifier|static
name|KeyPair
name|rsaPair
decl_stmt|;
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|rsaPair
operator|=
name|getKeyPair
argument_list|(
literal|"RSA"
argument_list|,
literal|1024
argument_list|)
expr_stmt|;
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
literal|"/org/apache/camel/component/xmlsecurity/SpringXmlSignatureTests.xml"
argument_list|)
return|;
block|}
DECL|method|getDsaKeyAccessor ()
specifier|public
specifier|static
name|KeyAccessor
name|getDsaKeyAccessor
parameter_list|()
block|{
return|return
name|getKeyAccessor
argument_list|(
name|getKeyPair
argument_list|(
literal|"DSA"
argument_list|,
literal|1024
argument_list|)
operator|.
name|getPrivate
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getRsaKeyAccessor ()
specifier|public
specifier|static
name|KeyAccessor
name|getRsaKeyAccessor
parameter_list|()
block|{
return|return
name|getKeyAccessor
argument_list|(
name|rsaPair
operator|.
name|getPrivate
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getDsaKeySelector ()
specifier|public
specifier|static
name|KeySelector
name|getDsaKeySelector
parameter_list|()
block|{
return|return
name|KeySelector
operator|.
name|singletonKeySelector
argument_list|(
name|getKeyPair
argument_list|(
literal|"DSA"
argument_list|,
literal|1024
argument_list|)
operator|.
name|getPublic
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getRsaKeySelector ()
specifier|public
specifier|static
name|KeySelector
name|getRsaKeySelector
parameter_list|()
block|{
return|return
name|KeySelector
operator|.
name|singletonKeySelector
argument_list|(
name|rsaPair
operator|.
name|getPublic
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|super
operator|.
name|createRegistry
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilders ()
specifier|protected
name|RouteBuilder
index|[]
name|createRouteBuilders
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
index|[]
block|{}
return|;
block|}
annotation|@
name|Override
DECL|method|getDetachedSignerEndpoint ()
name|XmlSignerEndpoint
name|getDetachedSignerEndpoint
parameter_list|()
block|{
name|XmlSignerEndpoint
name|endpoint
init|=
operator|(
name|XmlSignerEndpoint
operator|)
name|context
argument_list|()
operator|.
name|getEndpoint
argument_list|(
literal|"xmlsecurity:sign://detached?keyAccessor=#accessorRsa&xpathsToIdAttributes=#xpathsToIdAttributes&"
comment|//
operator|+
literal|"schemaResourceUri=org/apache/camel/component/xmlsecurity/Test.xsd&signatureId=&clearHeaders=false"
argument_list|)
decl_stmt|;
return|return
name|endpoint
return|;
block|}
annotation|@
name|Override
DECL|method|getSignatureEncpointForSignException ()
name|XmlSignerEndpoint
name|getSignatureEncpointForSignException
parameter_list|()
block|{
name|XmlSignerEndpoint
name|endpoint
init|=
operator|(
name|XmlSignerEndpoint
operator|)
name|context
argument_list|()
operator|.
name|getEndpoint
argument_list|(
comment|//
literal|"xmlsecurity:sign://signexceptioninvalidkey?keyAccessor=#accessorRsa"
argument_list|)
decl_stmt|;
return|return
name|endpoint
return|;
block|}
annotation|@
name|Override
DECL|method|getVerifierEndpointURIEnveloped ()
name|String
name|getVerifierEndpointURIEnveloped
parameter_list|()
block|{
return|return
literal|"xmlsecurity:verify://enveloped?keySelector=#selectorRsa"
return|;
block|}
annotation|@
name|Override
DECL|method|getSignerEndpointURIEnveloped ()
name|String
name|getSignerEndpointURIEnveloped
parameter_list|()
block|{
return|return
literal|"xmlsecurity:sign://enveloped?keyAccessor=#accessorRsa&parentLocalName=root&parentNamespace=http://test/test"
return|;
block|}
annotation|@
name|Override
DECL|method|getVerifierEndpointURIEnveloping ()
name|String
name|getVerifierEndpointURIEnveloping
parameter_list|()
block|{
return|return
literal|"xmlsecurity:verify://enveloping?keySelector=#selectorRsa"
return|;
block|}
annotation|@
name|Override
DECL|method|getSignerEndpointURIEnveloping ()
name|String
name|getSignerEndpointURIEnveloping
parameter_list|()
block|{
return|return
literal|"xmlsecurity:sign://enveloping?keyAccessor=#accessorRsa"
return|;
block|}
annotation|@
name|Test
DECL|method|xades ()
specifier|public
name|void
name|xades
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:xades"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Message
name|message
init|=
name|getMessage
argument_list|(
name|mock
argument_list|)
decl_stmt|;
name|byte
index|[]
name|body
init|=
name|message
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|XmlSignatureHelper
operator|.
name|newDocumentBuilder
argument_list|(
literal|true
argument_list|)
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|body
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|prefix2Ns
init|=
name|XAdESSignaturePropertiesTest
operator|.
name|getPrefix2NamespaceMap
argument_list|()
decl_stmt|;
name|prefix2Ns
operator|.
name|put
argument_list|(
literal|"t"
argument_list|,
literal|"http://test.com/"
argument_list|)
expr_stmt|;
name|XAdESSignaturePropertiesTest
operator|.
name|checkXpath
argument_list|(
name|doc
argument_list|,
literal|"/ds:Signature/ds:Object/etsi:QualifyingProperties/etsi:SignedProperties/etsi:SignedSignatureProperties/etsi:SignerRole/etsi:ClaimedRoles/etsi:ClaimedRole/t:test"
argument_list|,
name|prefix2Ns
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

