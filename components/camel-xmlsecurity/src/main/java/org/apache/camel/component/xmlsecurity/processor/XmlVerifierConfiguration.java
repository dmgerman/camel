begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmlsecurity.processor
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
name|processor
package|;
end_package

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
name|RuntimeCamelException
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
name|DefaultValidationFailedHandler
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
name|DefaultXmlSignature2Message
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
name|ValidationFailedHandler
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
name|XmlSignature2Message
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
name|XmlSignatureChecker
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

begin_class
DECL|class|XmlVerifierConfiguration
specifier|public
class|class
name|XmlVerifierConfiguration
extends|extends
name|XmlSignatureConfiguration
block|{
annotation|@
name|UriParam
DECL|field|keySelector
specifier|private
name|KeySelector
name|keySelector
decl_stmt|;
annotation|@
name|UriParam
DECL|field|keySelectorName
specifier|private
name|String
name|keySelectorName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|xmlSignatureChecker
specifier|private
name|XmlSignatureChecker
name|xmlSignatureChecker
decl_stmt|;
annotation|@
name|UriParam
DECL|field|xmlSignatureCheckerName
specifier|private
name|String
name|xmlSignatureCheckerName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|xmlSignature2Message
specifier|private
name|XmlSignature2Message
name|xmlSignature2Message
init|=
operator|new
name|DefaultXmlSignature2Message
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
DECL|field|xmlSignature2MessageName
specifier|private
name|String
name|xmlSignature2MessageName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|validationFailedHandler
specifier|private
name|ValidationFailedHandler
name|validationFailedHandler
init|=
operator|new
name|DefaultValidationFailedHandler
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
DECL|field|validationFailedHandlerName
specifier|private
name|String
name|validationFailedHandlerName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|outputNodeSearch
specifier|private
name|Object
name|outputNodeSearch
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
name|DefaultXmlSignature2Message
operator|.
name|OUTPUT_NODE_SEARCH_TYPE_DEFAULT
argument_list|)
DECL|field|outputNodeSearchType
specifier|private
name|String
name|outputNodeSearchType
init|=
name|DefaultXmlSignature2Message
operator|.
name|OUTPUT_NODE_SEARCH_TYPE_DEFAULT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|removeSignatureElements
specifier|private
name|Boolean
name|removeSignatureElements
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|secureValidation
specifier|private
name|Boolean
name|secureValidation
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
DECL|method|XmlVerifierConfiguration ()
specifier|public
name|XmlVerifierConfiguration
parameter_list|()
block|{     }
DECL|method|copy ()
specifier|public
name|XmlVerifierConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|XmlVerifierConfiguration
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
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|setKeySelector
argument_list|(
name|keySelectorName
argument_list|)
expr_stmt|;
name|setXmlSignatureChecker
argument_list|(
name|xmlSignatureCheckerName
argument_list|)
expr_stmt|;
name|setXmlSignature2Message
argument_list|(
name|xmlSignature2MessageName
argument_list|)
expr_stmt|;
name|setValidationFailedHandler
argument_list|(
name|validationFailedHandlerName
argument_list|)
expr_stmt|;
block|}
DECL|method|setKeySelector (KeySelector keySelector)
specifier|public
name|void
name|setKeySelector
parameter_list|(
name|KeySelector
name|keySelector
parameter_list|)
block|{
name|this
operator|.
name|keySelector
operator|=
name|keySelector
expr_stmt|;
block|}
DECL|method|getKeySelector ()
specifier|public
name|KeySelector
name|getKeySelector
parameter_list|()
block|{
return|return
name|keySelector
return|;
block|}
comment|/**      * Sets the reference name for a KeySelector that can be found in the      * registry.      */
DECL|method|setKeySelector (String keySelectorName)
specifier|public
name|void
name|setKeySelector
parameter_list|(
name|String
name|keySelectorName
parameter_list|)
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
operator|&&
name|keySelectorName
operator|!=
literal|null
condition|)
block|{
name|KeySelector
name|selector
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|keySelectorName
argument_list|,
name|KeySelector
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|selector
operator|!=
literal|null
condition|)
block|{
name|setKeySelector
argument_list|(
name|selector
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|keySelectorName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|keySelectorName
operator|=
name|keySelectorName
expr_stmt|;
block|}
block|}
DECL|method|getXmlSignatureChecker ()
specifier|public
name|XmlSignatureChecker
name|getXmlSignatureChecker
parameter_list|()
block|{
return|return
name|xmlSignatureChecker
return|;
block|}
DECL|method|setXmlSignatureChecker (XmlSignatureChecker xmlSignatureChecker)
specifier|public
name|void
name|setXmlSignatureChecker
parameter_list|(
name|XmlSignatureChecker
name|xmlSignatureChecker
parameter_list|)
block|{
name|this
operator|.
name|xmlSignatureChecker
operator|=
name|xmlSignatureChecker
expr_stmt|;
block|}
comment|/**      * Sets the reference name for a application checker that can be found in      * the registry.      */
DECL|method|setXmlSignatureChecker (String xmlSignatureCheckerName)
specifier|public
name|void
name|setXmlSignatureChecker
parameter_list|(
name|String
name|xmlSignatureCheckerName
parameter_list|)
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
operator|&&
name|xmlSignatureCheckerName
operator|!=
literal|null
condition|)
block|{
name|XmlSignatureChecker
name|checker
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|xmlSignatureCheckerName
argument_list|,
name|XmlSignatureChecker
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|checker
operator|!=
literal|null
condition|)
block|{
name|setXmlSignatureChecker
argument_list|(
name|checker
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|xmlSignatureCheckerName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|xmlSignatureCheckerName
operator|=
name|xmlSignatureCheckerName
expr_stmt|;
block|}
block|}
DECL|method|getXmlSignature2Message ()
specifier|public
name|XmlSignature2Message
name|getXmlSignature2Message
parameter_list|()
block|{
return|return
name|xmlSignature2Message
return|;
block|}
DECL|method|setXmlSignature2Message (XmlSignature2Message xmlSignature2Message)
specifier|public
name|void
name|setXmlSignature2Message
parameter_list|(
name|XmlSignature2Message
name|xmlSignature2Message
parameter_list|)
block|{
name|this
operator|.
name|xmlSignature2Message
operator|=
name|xmlSignature2Message
expr_stmt|;
block|}
comment|/**      * Sets the reference name for the to-message instance that can be found in      * the registry.      */
DECL|method|setXmlSignature2Message (String xmlSignature2Message)
specifier|public
name|void
name|setXmlSignature2Message
parameter_list|(
name|String
name|xmlSignature2Message
parameter_list|)
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
operator|&&
name|xmlSignature2Message
operator|!=
literal|null
condition|)
block|{
name|XmlSignature2Message
name|maper
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|xmlSignature2Message
argument_list|,
name|XmlSignature2Message
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|maper
operator|!=
literal|null
condition|)
block|{
name|setXmlSignature2Message
argument_list|(
name|maper
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|xmlSignature2Message
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|xmlSignature2MessageName
operator|=
name|xmlSignature2Message
expr_stmt|;
block|}
block|}
DECL|method|getValidationFailedHandler ()
specifier|public
name|ValidationFailedHandler
name|getValidationFailedHandler
parameter_list|()
block|{
return|return
name|validationFailedHandler
return|;
block|}
DECL|method|setValidationFailedHandler (ValidationFailedHandler validationFailedHandler)
specifier|public
name|void
name|setValidationFailedHandler
parameter_list|(
name|ValidationFailedHandler
name|validationFailedHandler
parameter_list|)
block|{
name|this
operator|.
name|validationFailedHandler
operator|=
name|validationFailedHandler
expr_stmt|;
block|}
DECL|method|setValidationFailedHandler (String validationFailedHandlerName)
specifier|public
name|void
name|setValidationFailedHandler
parameter_list|(
name|String
name|validationFailedHandlerName
parameter_list|)
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
operator|&&
name|validationFailedHandlerName
operator|!=
literal|null
condition|)
block|{
name|ValidationFailedHandler
name|vailFailedHandler
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|validationFailedHandlerName
argument_list|,
name|ValidationFailedHandler
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|vailFailedHandler
operator|!=
literal|null
condition|)
block|{
name|setValidationFailedHandler
argument_list|(
name|vailFailedHandler
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|validationFailedHandlerName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|validationFailedHandlerName
operator|=
name|validationFailedHandlerName
expr_stmt|;
block|}
block|}
DECL|method|getOutputNodeSearch ()
specifier|public
name|Object
name|getOutputNodeSearch
parameter_list|()
block|{
return|return
name|outputNodeSearch
return|;
block|}
comment|/**      * Sets the output node search value for determining the node from the XML      * signature document which shall be set to the output message body. The      * class of the value depends on the type of the output node search. The      * output node search is forwarded to {@link XmlSignature2Message}.      *       */
DECL|method|setOutputNodeSearch (Object outputNodeSearch)
specifier|public
name|void
name|setOutputNodeSearch
parameter_list|(
name|Object
name|outputNodeSearch
parameter_list|)
block|{
name|this
operator|.
name|outputNodeSearch
operator|=
name|outputNodeSearch
expr_stmt|;
block|}
DECL|method|getOutputNodeSearchType ()
specifier|public
name|String
name|getOutputNodeSearchType
parameter_list|()
block|{
return|return
name|outputNodeSearchType
return|;
block|}
comment|/**      * Determines the search type for determining the output node which is      * serialized into the output message bodyF. See      * {@link #setOutputNodeSearch(Object)}. The supported default search types      * you can find in {@link DefaultXmlSignature2Message}.      *       * @param outputNodeSearchType      */
DECL|method|setOutputNodeSearchType (String outputNodeSearchType)
specifier|public
name|void
name|setOutputNodeSearchType
parameter_list|(
name|String
name|outputNodeSearchType
parameter_list|)
block|{
name|this
operator|.
name|outputNodeSearchType
operator|=
name|outputNodeSearchType
expr_stmt|;
block|}
DECL|method|getRemoveSignatureElements ()
specifier|public
name|Boolean
name|getRemoveSignatureElements
parameter_list|()
block|{
return|return
name|removeSignatureElements
return|;
block|}
comment|/**      * Indicator whether the XML signature elements (elements with local name      * "Signature" and namesapce ""http://www.w3.org/2000/09/xmldsig#"") shall      * be removed from the document set to the output message. Normally, this is      * only necessary, if the XML signature is enveloped. The default value is      * {@link Boolean#FALSE}. This parameter is forwarded to      * {@link XmlSignature2Message}.      *<p>      * This indicator has no effect if the output node search is of type      * {@link DefaultXmlSignature2Message#OUTPUT_NODE_SEARCH_TYPE_DEFAULT}.F      */
DECL|method|setRemoveSignatureElements (Boolean removeSignatureElements)
specifier|public
name|void
name|setRemoveSignatureElements
parameter_list|(
name|Boolean
name|removeSignatureElements
parameter_list|)
block|{
name|this
operator|.
name|removeSignatureElements
operator|=
name|removeSignatureElements
expr_stmt|;
block|}
DECL|method|getSecureValidation ()
specifier|public
name|Boolean
name|getSecureValidation
parameter_list|()
block|{
return|return
name|secureValidation
return|;
block|}
DECL|method|setSecureValidation (Boolean secureValidation)
specifier|public
name|void
name|setSecureValidation
parameter_list|(
name|Boolean
name|secureValidation
parameter_list|)
block|{
name|this
operator|.
name|secureValidation
operator|=
name|secureValidation
expr_stmt|;
block|}
block|}
end_class

end_unit

