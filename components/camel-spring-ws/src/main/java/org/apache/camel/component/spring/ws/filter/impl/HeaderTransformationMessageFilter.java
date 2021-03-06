begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.filter.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|filter
operator|.
name|impl
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
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|ErrorListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
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
name|Exchange
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
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|filter
operator|.
name|MessageFilter
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
name|xslt
operator|.
name|XsltUriResolver
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
name|xslt
operator|.
name|saxon
operator|.
name|XsltSaxonComponent
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
name|ClassResolver
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
name|ObjectHelper
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|WebServiceMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|SoapMessage
import|;
end_import

begin_comment
comment|/**  * Message filter that transforms the header of a soap message  */
end_comment

begin_class
DECL|class|HeaderTransformationMessageFilter
specifier|public
class|class
name|HeaderTransformationMessageFilter
implements|implements
name|MessageFilter
block|{
DECL|field|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
init|=
literal|"net.sf.saxon.TransformerFactoryImpl"
decl_stmt|;
DECL|field|SOAP_HEADER_TRANSFORMATION_PROBLEM
specifier|private
specifier|static
specifier|final
name|String
name|SOAP_HEADER_TRANSFORMATION_PROBLEM
init|=
literal|"Soap header transformation problem"
decl_stmt|;
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
name|HeaderTransformationMessageFilter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|xslt
specifier|private
name|String
name|xslt
decl_stmt|;
DECL|field|saxon
specifier|private
name|boolean
name|saxon
decl_stmt|;
comment|/**      * @param xslt      */
DECL|method|HeaderTransformationMessageFilter (String xslt)
specifier|public
name|HeaderTransformationMessageFilter
parameter_list|(
name|String
name|xslt
parameter_list|)
block|{
name|this
operator|.
name|xslt
operator|=
name|xslt
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|filterProducer (Exchange exchange, WebServiceMessage webServiceMessage)
specifier|public
name|void
name|filterProducer
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|WebServiceMessage
name|webServiceMessage
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|processHeader
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|webServiceMessage
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|filterConsumer (Exchange exchange, WebServiceMessage webServiceMessage)
specifier|public
name|void
name|filterConsumer
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|WebServiceMessage
name|webServiceMessage
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|Message
name|responseMessage
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|processHeader
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|responseMessage
argument_list|,
name|webServiceMessage
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Transform the header      * @param context      * @param inOrOut      * @param webServiceMessage      */
DECL|method|processHeader (CamelContext context, Message inOrOut, WebServiceMessage webServiceMessage)
specifier|private
name|void
name|processHeader
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Message
name|inOrOut
parameter_list|,
name|WebServiceMessage
name|webServiceMessage
parameter_list|)
block|{
if|if
condition|(
name|webServiceMessage
operator|instanceof
name|SoapMessage
condition|)
block|{
name|SoapMessage
name|soapMessage
init|=
operator|(
name|SoapMessage
operator|)
name|webServiceMessage
decl_stmt|;
try|try
block|{
name|XsltUriResolver
name|resolver
init|=
operator|new
name|XsltUriResolver
argument_list|(
name|context
argument_list|,
name|xslt
argument_list|)
decl_stmt|;
name|Source
name|stylesheetResource
init|=
name|resolver
operator|.
name|resolve
argument_list|(
name|xslt
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|TransformerFactory
name|transformerFactory
init|=
name|getTransformerFactory
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Transformer
name|transformer
init|=
name|transformerFactory
operator|.
name|newTransformer
argument_list|(
name|stylesheetResource
argument_list|)
decl_stmt|;
name|addParameters
argument_list|(
name|inOrOut
argument_list|,
name|transformer
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|soapMessage
operator|.
name|getSoapHeader
argument_list|()
operator|.
name|getSource
argument_list|()
argument_list|,
name|soapMessage
operator|.
name|getSoapHeader
argument_list|()
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot transform the header of the soap message"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Adding the headers of the message as parameter to the transformer      *       * @param inOrOut      * @param transformer      */
DECL|method|addParameters (Message inOrOut, Transformer transformer)
specifier|private
name|void
name|addParameters
parameter_list|(
name|Message
name|inOrOut
parameter_list|,
name|Transformer
name|transformer
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|inOrOut
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headerEntry
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|headerEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
comment|// Key's with '$' are not allowed in XSLT
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
operator|!
name|key
operator|.
name|startsWith
argument_list|(
literal|"$"
argument_list|)
condition|)
block|{
name|transformer
operator|.
name|setParameter
argument_list|(
name|key
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|headerEntry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Getting a {@link TransformerFactory} with logging      *      * @return {@link TransformerFactory}      */
DECL|method|getTransformerFactory (CamelContext context)
specifier|private
name|TransformerFactory
name|getTransformerFactory
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|TransformerFactory
name|transformerFactory
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|saxon
condition|)
block|{
name|transformerFactory
operator|=
name|getSaxonTransformerFactory
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|transformerFactory
operator|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|transformerFactory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot resolve a transformer factory"
argument_list|)
throw|;
block|}
try|try
block|{
name|transformerFactory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerConfigurationException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|transformerFactory
operator|.
name|setErrorListener
argument_list|(
operator|new
name|ErrorListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|warning
parameter_list|(
name|TransformerException
name|exception
parameter_list|)
throws|throws
name|TransformerException
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|SOAP_HEADER_TRANSFORMATION_PROBLEM
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|fatalError
parameter_list|(
name|TransformerException
name|exception
parameter_list|)
throws|throws
name|TransformerException
block|{
name|LOG
operator|.
name|error
argument_list|(
name|SOAP_HEADER_TRANSFORMATION_PROBLEM
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|error
parameter_list|(
name|TransformerException
name|exception
parameter_list|)
throws|throws
name|TransformerException
block|{
name|LOG
operator|.
name|error
argument_list|(
name|SOAP_HEADER_TRANSFORMATION_PROBLEM
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|transformerFactory
return|;
block|}
comment|/**      * Loading the saxon transformer class      *       * @param context      * @return      */
DECL|method|getSaxonTransformerFactory (CamelContext context)
specifier|private
name|TransformerFactory
name|getSaxonTransformerFactory
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
specifier|final
name|ClassResolver
name|resolver
init|=
name|context
operator|.
name|getClassResolver
argument_list|()
decl_stmt|;
try|try
block|{
name|Class
argument_list|<
name|TransformerFactory
argument_list|>
name|factoryClass
init|=
name|resolver
operator|.
name|resolveMandatoryClass
argument_list|(
name|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
argument_list|,
name|TransformerFactory
operator|.
name|class
argument_list|,
name|XsltSaxonComponent
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|factoryClass
operator|!=
literal|null
condition|)
block|{
return|return
name|ObjectHelper
operator|.
name|newInstance
argument_list|(
name|factoryClass
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot load the saxon transformer class"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getXslt ()
specifier|public
name|String
name|getXslt
parameter_list|()
block|{
return|return
name|xslt
return|;
block|}
DECL|method|setXslt (String xslt)
specifier|public
name|void
name|setXslt
parameter_list|(
name|String
name|xslt
parameter_list|)
block|{
name|this
operator|.
name|xslt
operator|=
name|xslt
expr_stmt|;
block|}
DECL|method|isSaxon ()
specifier|public
name|boolean
name|isSaxon
parameter_list|()
block|{
return|return
name|saxon
return|;
block|}
DECL|method|setSaxon (boolean saxon)
specifier|public
name|void
name|setSaxon
parameter_list|(
name|boolean
name|saxon
parameter_list|)
block|{
name|this
operator|.
name|saxon
operator|=
name|saxon
expr_stmt|;
block|}
block|}
end_class

end_unit

