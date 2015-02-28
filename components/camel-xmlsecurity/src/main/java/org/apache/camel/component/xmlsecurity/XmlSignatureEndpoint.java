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
name|URIDereferencer
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
name|component
operator|.
name|xmlsecurity
operator|.
name|processor
operator|.
name|XmlSignatureConfiguration
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
name|processor
operator|.
name|XmlSignerConfiguration
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
name|processor
operator|.
name|XmlVerifierConfiguration
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
name|DefaultEndpoint
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

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"xmlsecurity"
argument_list|,
name|syntax
operator|=
literal|"xmlsecurity:command/name"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"security,transformation"
argument_list|)
DECL|class|XmlSignatureEndpoint
specifier|public
specifier|abstract
class|class
name|XmlSignatureEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|command
specifier|private
name|XmlCommand
name|command
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
comment|// to include both kind of configuration params
annotation|@
name|UriParam
DECL|field|signerConfiguration
specifier|private
name|XmlSignerConfiguration
name|signerConfiguration
decl_stmt|;
annotation|@
name|UriParam
DECL|field|verifierConfiguration
specifier|private
name|XmlVerifierConfiguration
name|verifierConfiguration
decl_stmt|;
DECL|method|XmlSignatureEndpoint (String uri, XmlSignatureComponent component)
specifier|public
name|XmlSignatureEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|XmlSignatureComponent
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
DECL|method|getCommand ()
specifier|public
name|XmlCommand
name|getCommand
parameter_list|()
block|{
return|return
name|command
return|;
block|}
DECL|method|setCommand (XmlCommand command)
specifier|public
name|void
name|setCommand
parameter_list|(
name|XmlCommand
name|command
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
block|{
return|return
operator|new
name|XmlSignatureProducer
argument_list|(
name|this
argument_list|,
name|createProcessor
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createProcessor ()
specifier|abstract
name|Processor
name|createProcessor
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"XML Signature endpoints are not meant to be consumed from."
argument_list|)
throw|;
block|}
annotation|@
name|Override
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
DECL|method|getManagedObject (XmlSignatureEndpoint endpoint)
specifier|public
name|Object
name|getManagedObject
parameter_list|(
name|XmlSignatureEndpoint
name|endpoint
parameter_list|)
block|{
return|return
name|this
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
specifier|abstract
name|XmlSignatureConfiguration
name|getConfiguration
parameter_list|()
function_decl|;
DECL|method|getUriDereferencer ()
specifier|public
name|URIDereferencer
name|getUriDereferencer
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getUriDereferencer
argument_list|()
return|;
block|}
DECL|method|setUriDereferencer (URIDereferencer uriDereferencer)
specifier|public
name|void
name|setUriDereferencer
parameter_list|(
name|URIDereferencer
name|uriDereferencer
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setUriDereferencer
argument_list|(
name|uriDereferencer
argument_list|)
expr_stmt|;
block|}
DECL|method|getBaseUri ()
specifier|public
name|String
name|getBaseUri
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getBaseUri
argument_list|()
return|;
block|}
DECL|method|setBaseUri (String baseUri)
specifier|public
name|void
name|setBaseUri
parameter_list|(
name|String
name|baseUri
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setBaseUri
argument_list|(
name|baseUri
argument_list|)
expr_stmt|;
block|}
DECL|method|getCryptoContextProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|getCryptoContextProperties
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getCryptoContextProperties
argument_list|()
return|;
block|}
DECL|method|setCryptoContextProperties (Map<String, ? extends Object> cryptoContextProperties)
specifier|public
name|void
name|setCryptoContextProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|cryptoContextProperties
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setCryptoContextProperties
argument_list|(
name|cryptoContextProperties
argument_list|)
expr_stmt|;
block|}
DECL|method|getDisallowDoctypeDecl ()
specifier|public
name|Boolean
name|getDisallowDoctypeDecl
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getDisallowDoctypeDecl
argument_list|()
return|;
block|}
DECL|method|setDisallowDoctypeDecl (Boolean disallowDoctypeDecl)
specifier|public
name|void
name|setDisallowDoctypeDecl
parameter_list|(
name|Boolean
name|disallowDoctypeDecl
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setDisallowDoctypeDecl
argument_list|(
name|disallowDoctypeDecl
argument_list|)
expr_stmt|;
block|}
DECL|method|getOmitXmlDeclaration ()
specifier|public
name|Boolean
name|getOmitXmlDeclaration
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getOmitXmlDeclaration
argument_list|()
return|;
block|}
DECL|method|setOmitXmlDeclaration (Boolean omitXmlDeclaration)
specifier|public
name|void
name|setOmitXmlDeclaration
parameter_list|(
name|Boolean
name|omitXmlDeclaration
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setOmitXmlDeclaration
argument_list|(
name|omitXmlDeclaration
argument_list|)
expr_stmt|;
block|}
DECL|method|getSchemaResourceUri ()
specifier|public
name|String
name|getSchemaResourceUri
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getSchemaResourceUri
argument_list|()
return|;
block|}
DECL|method|setSchemaResourceUri (String schemaResourceUri)
specifier|public
name|void
name|setSchemaResourceUri
parameter_list|(
name|String
name|schemaResourceUri
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setSchemaResourceUri
argument_list|(
name|schemaResourceUri
argument_list|)
expr_stmt|;
block|}
DECL|method|getOutputXmlEncoding ()
specifier|public
name|String
name|getOutputXmlEncoding
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getOutputXmlEncoding
argument_list|()
return|;
block|}
DECL|method|setOutputXmlEncoding (String encoding)
specifier|public
name|void
name|setOutputXmlEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setOutputXmlEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

