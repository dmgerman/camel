begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmlsecurity.util
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
operator|.
name|util
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|dom
operator|.
name|DOMStructure
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
name|dsig
operator|.
name|CanonicalizationMethod
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
name|dsig
operator|.
name|Reference
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
name|dsig
operator|.
name|SignatureProperties
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
name|dsig
operator|.
name|SignatureProperty
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
name|dsig
operator|.
name|Transform
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
name|dsig
operator|.
name|XMLObject
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
name|dsig
operator|.
name|spec
operator|.
name|TransformParameterSpec
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
name|component
operator|.
name|xmlsecurity
operator|.
name|api
operator|.
name|XmlSignatureProperties
import|;
end_import

begin_comment
comment|/**  * Example for a XmlSignatureProperties implementation which adds a timestamp  * signature property.  */
end_comment

begin_class
DECL|class|TimestampProperty
specifier|public
class|class
name|TimestampProperty
implements|implements
name|XmlSignatureProperties
block|{
annotation|@
name|Override
DECL|method|get (Input input)
specifier|public
name|Output
name|get
parameter_list|(
name|Input
name|input
parameter_list|)
throws|throws
name|Exception
block|{
name|Transform
name|transform
init|=
name|input
operator|.
name|getSignatureFactory
argument_list|()
operator|.
name|newTransform
argument_list|(
name|CanonicalizationMethod
operator|.
name|INCLUSIVE
argument_list|,
operator|(
name|TransformParameterSpec
operator|)
literal|null
argument_list|)
decl_stmt|;
name|Reference
name|ref
init|=
name|input
operator|.
name|getSignatureFactory
argument_list|()
operator|.
name|newReference
argument_list|(
literal|"#propertiesObject"
argument_list|,
name|input
operator|.
name|getSignatureFactory
argument_list|()
operator|.
name|newDigestMethod
argument_list|(
name|input
operator|.
name|getContentDigestAlgorithm
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|transform
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|doc2
init|=
literal|"<ts:timestamp xmlns:ts=\"http:/timestamp\">"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|"</ts:timestamp>"
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|doc2
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|XmlSignatureHelper
operator|.
name|newDocumentBuilder
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
operator|.
name|parse
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|DOMStructure
name|structure
init|=
operator|new
name|DOMStructure
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
name|SignatureProperty
name|prop
init|=
name|input
operator|.
name|getSignatureFactory
argument_list|()
operator|.
name|newSignatureProperty
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|structure
argument_list|)
argument_list|,
name|input
operator|.
name|getSignatureId
argument_list|()
argument_list|,
literal|"property"
argument_list|)
decl_stmt|;
name|SignatureProperties
name|properties
init|=
name|input
operator|.
name|getSignatureFactory
argument_list|()
operator|.
name|newSignatureProperties
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|prop
argument_list|)
argument_list|,
literal|"properties"
argument_list|)
decl_stmt|;
name|XMLObject
name|propertiesObject
init|=
name|input
operator|.
name|getSignatureFactory
argument_list|()
operator|.
name|newXMLObject
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|properties
argument_list|)
argument_list|,
literal|"propertiesObject"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|XmlSignatureProperties
operator|.
name|Output
name|result
init|=
operator|new
name|Output
argument_list|()
decl_stmt|;
name|result
operator|.
name|setReferences
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|ref
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|setObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|propertiesObject
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

