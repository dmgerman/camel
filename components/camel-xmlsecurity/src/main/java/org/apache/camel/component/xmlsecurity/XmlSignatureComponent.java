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
name|net
operator|.
name|MalformedURLException
import|;
end_import

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
name|Endpoint
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"xmlsecurity"
argument_list|)
DECL|class|XmlSignatureComponent
specifier|public
class|class
name|XmlSignatureComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|signerConfiguration
specifier|private
name|XmlSignerConfiguration
name|signerConfiguration
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|verifierConfiguration
specifier|private
name|XmlVerifierConfiguration
name|verifierConfiguration
decl_stmt|;
DECL|method|XmlSignatureComponent ()
specifier|public
name|XmlSignatureComponent
parameter_list|()
block|{     }
DECL|method|XmlSignatureComponent (CamelContext context)
specifier|public
name|XmlSignatureComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"CamelContext"
argument_list|)
expr_stmt|;
name|String
name|scheme
decl_stmt|;
name|String
name|name
decl_stmt|;
try|try
block|{
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|scheme
operator|=
name|u
operator|.
name|getScheme
argument_list|()
expr_stmt|;
name|name
operator|=
name|u
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MalformedURLException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"An invalid xmlsecurity uri was provided '%s'."
operator|+
literal|" Check the uri matches the format xmlsecurity:sign://<name> or xmlsecurity:verify:<name>"
argument_list|,
name|uri
argument_list|)
argument_list|)
throw|;
block|}
name|XmlSignatureEndpoint
name|endpoint
decl_stmt|;
if|if
condition|(
literal|"sign"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|XmlSignerConfiguration
name|config
init|=
name|getSignerConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
name|endpoint
operator|=
operator|new
name|XmlSignerEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"verify"
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|XmlVerifierConfiguration
name|config
init|=
name|getVerifierConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
name|endpoint
operator|=
operator|new
name|XmlVerifierEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Endpoint uri '%s'"
operator|+
literal|" is wrong configured. Operation '%s'"
operator|+
literal|" is not supported. Supported operations are: sign, verify"
argument_list|,
name|uri
argument_list|,
name|scheme
argument_list|)
argument_list|)
throw|;
block|}
name|setProperties
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCommand
argument_list|(
name|XmlCommand
operator|.
name|valueOf
argument_list|(
name|scheme
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getSignerConfiguration ()
specifier|public
name|XmlSignerConfiguration
name|getSignerConfiguration
parameter_list|()
block|{
if|if
condition|(
name|signerConfiguration
operator|==
literal|null
condition|)
block|{
name|signerConfiguration
operator|=
operator|new
name|XmlSignerConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|signerConfiguration
return|;
block|}
comment|/**      * To use a shared XmlSignerConfiguration configuration to use as base for configuring endpoints.      */
DECL|method|setSignerConfiguration (XmlSignerConfiguration signerConfiguration)
specifier|public
name|void
name|setSignerConfiguration
parameter_list|(
name|XmlSignerConfiguration
name|signerConfiguration
parameter_list|)
block|{
name|this
operator|.
name|signerConfiguration
operator|=
name|signerConfiguration
expr_stmt|;
block|}
DECL|method|getVerifierConfiguration ()
specifier|public
name|XmlVerifierConfiguration
name|getVerifierConfiguration
parameter_list|()
block|{
if|if
condition|(
name|verifierConfiguration
operator|==
literal|null
condition|)
block|{
name|verifierConfiguration
operator|=
operator|new
name|XmlVerifierConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|verifierConfiguration
return|;
block|}
comment|/**      * To use a shared XmlVerifierConfiguration configuration to use as base for configuring endpoints.      */
DECL|method|setVerifierConfiguration (XmlVerifierConfiguration verifierConfiguration)
specifier|public
name|void
name|setVerifierConfiguration
parameter_list|(
name|XmlVerifierConfiguration
name|verifierConfiguration
parameter_list|)
block|{
name|this
operator|.
name|verifierConfiguration
operator|=
name|verifierConfiguration
expr_stmt|;
block|}
block|}
end_class

end_unit

