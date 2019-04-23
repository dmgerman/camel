begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|language
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|Expression
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
name|Predicate
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * To use XPath (XML) in Camel expressions or predicates.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"1.1.0"
argument_list|,
name|label
operator|=
literal|"language,core,xml"
argument_list|,
name|title
operator|=
literal|"XPath"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"xpath"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|XPathExpression
specifier|public
class|class
name|XPathExpression
extends|extends
name|NamespaceAwareExpression
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"documentType"
argument_list|)
DECL|field|documentTypeName
specifier|private
name|String
name|documentTypeName
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"resultType"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"NODESET"
argument_list|,
name|enums
operator|=
literal|"NUMBER,STRING,BOOLEAN,NODESET,NODE"
argument_list|)
DECL|field|resultTypeName
specifier|private
name|String
name|resultTypeName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|saxon
specifier|private
name|Boolean
name|saxon
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|factoryRef
specifier|private
name|String
name|factoryRef
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|objectModel
specifier|private
name|String
name|objectModel
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|logNamespaces
specifier|private
name|Boolean
name|logNamespaces
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|documentType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|documentType
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|resultType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|xpathFactory
specifier|private
name|XPathFactory
name|xpathFactory
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|threadSafety
specifier|private
name|Boolean
name|threadSafety
decl_stmt|;
DECL|method|XPathExpression ()
specifier|public
name|XPathExpression
parameter_list|()
block|{     }
DECL|method|XPathExpression (String expression)
specifier|public
name|XPathExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|XPathExpression (Expression expression)
specifier|public
name|XPathExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|setExpressionValue
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
literal|"xpath"
return|;
block|}
DECL|method|getDocumentType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getDocumentType
parameter_list|()
block|{
return|return
name|documentType
return|;
block|}
comment|/**      * Class for document type to use      *<p/>      * The default value is org.w3c.dom.Document      */
DECL|method|setDocumentType (Class<?> documentType)
specifier|public
name|void
name|setDocumentType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|documentType
parameter_list|)
block|{
name|this
operator|.
name|documentType
operator|=
name|documentType
expr_stmt|;
block|}
DECL|method|getDocumentTypeName ()
specifier|public
name|String
name|getDocumentTypeName
parameter_list|()
block|{
return|return
name|documentTypeName
return|;
block|}
comment|/**      * Name of class for document type      *<p/>      * The default value is org.w3c.dom.Document      */
DECL|method|setDocumentTypeName (String documentTypeName)
specifier|public
name|void
name|setDocumentTypeName
parameter_list|(
name|String
name|documentTypeName
parameter_list|)
block|{
name|this
operator|.
name|documentTypeName
operator|=
name|documentTypeName
expr_stmt|;
block|}
DECL|method|getResultType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResultType
parameter_list|()
block|{
return|return
name|resultType
return|;
block|}
comment|/**      * Sets the class of the result type (type from output).      *<p/>      * The default result type is NodeSet      */
DECL|method|setResultType (Class<?> resultType)
specifier|public
name|void
name|setResultType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|this
operator|.
name|resultType
operator|=
name|resultType
expr_stmt|;
block|}
DECL|method|getResultTypeName ()
specifier|public
name|String
name|getResultTypeName
parameter_list|()
block|{
return|return
name|resultTypeName
return|;
block|}
comment|/**      * Sets the class name of the result type (type from output)      *<p/>      * The default result type is NodeSet      */
DECL|method|setResultTypeName (String resultTypeName)
specifier|public
name|void
name|setResultTypeName
parameter_list|(
name|String
name|resultTypeName
parameter_list|)
block|{
name|this
operator|.
name|resultTypeName
operator|=
name|resultTypeName
expr_stmt|;
block|}
comment|/**      * Whether to use Saxon.      */
DECL|method|setSaxon (Boolean saxon)
specifier|public
name|void
name|setSaxon
parameter_list|(
name|Boolean
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
DECL|method|getSaxon ()
specifier|public
name|Boolean
name|getSaxon
parameter_list|()
block|{
return|return
name|saxon
return|;
block|}
comment|/**      * References to a custom XPathFactory to lookup in the registry      */
DECL|method|setFactoryRef (String factoryRef)
specifier|public
name|void
name|setFactoryRef
parameter_list|(
name|String
name|factoryRef
parameter_list|)
block|{
name|this
operator|.
name|factoryRef
operator|=
name|factoryRef
expr_stmt|;
block|}
DECL|method|getFactoryRef ()
specifier|public
name|String
name|getFactoryRef
parameter_list|()
block|{
return|return
name|factoryRef
return|;
block|}
comment|/**      * The XPath object model to use      */
DECL|method|setObjectModel (String objectModel)
specifier|public
name|void
name|setObjectModel
parameter_list|(
name|String
name|objectModel
parameter_list|)
block|{
name|this
operator|.
name|objectModel
operator|=
name|objectModel
expr_stmt|;
block|}
DECL|method|getObjectModel ()
specifier|public
name|String
name|getObjectModel
parameter_list|()
block|{
return|return
name|objectModel
return|;
block|}
comment|/**      * Whether to log namespaces which can assist during trouble shooting      */
DECL|method|setLogNamespaces (Boolean logNamespaces)
specifier|public
name|void
name|setLogNamespaces
parameter_list|(
name|Boolean
name|logNamespaces
parameter_list|)
block|{
name|this
operator|.
name|logNamespaces
operator|=
name|logNamespaces
expr_stmt|;
block|}
DECL|method|getLogNamespaces ()
specifier|public
name|Boolean
name|getLogNamespaces
parameter_list|()
block|{
return|return
name|logNamespaces
return|;
block|}
DECL|method|getHeaderName ()
specifier|public
name|String
name|getHeaderName
parameter_list|()
block|{
return|return
name|headerName
return|;
block|}
comment|/**      * Name of header to use as input, instead of the message body      */
DECL|method|setHeaderName (String headerName)
specifier|public
name|void
name|setHeaderName
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|this
operator|.
name|headerName
operator|=
name|headerName
expr_stmt|;
block|}
DECL|method|getThreadSafety ()
specifier|public
name|Boolean
name|getThreadSafety
parameter_list|()
block|{
return|return
name|threadSafety
return|;
block|}
comment|/**      * Whether to enable thread-safety for the returned result of the xpath expression.      * This applies to when using NODESET as the result type, and the returned set has      * multiple elements. In this situation there can be thread-safety issues if you      * process the NODESET concurrently such as from a Camel Splitter EIP in parallel processing mode.      * This option prevents concurrency issues by doing defensive copies of the nodes.      *<p/>      * It is recommended to turn this option on if you are using camel-saxon or Saxon in your application.      * Saxon has thread-safety issues which can be prevented by turning this option on.      */
DECL|method|setThreadSafety (Boolean threadSafety)
specifier|public
name|void
name|setThreadSafety
parameter_list|(
name|Boolean
name|threadSafety
parameter_list|)
block|{
name|this
operator|.
name|threadSafety
operator|=
name|threadSafety
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createExpression (CamelContext camelContext)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|documentType
operator|==
literal|null
operator|&&
name|documentTypeName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|documentType
operator|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|documentTypeName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|resultType
operator|==
literal|null
operator|&&
name|resultTypeName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|resultType
operator|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|resultTypeName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|resolveXPathFactory
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|createExpression
argument_list|(
name|camelContext
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createPredicate (CamelContext camelContext)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|documentType
operator|==
literal|null
operator|&&
name|documentTypeName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|documentType
operator|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|documentTypeName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|resolveXPathFactory
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|createPredicate
argument_list|(
name|camelContext
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|configureExpression (CamelContext camelContext, Expression expression)
specifier|protected
name|void
name|configureExpression
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|boolean
name|isSaxon
init|=
name|getSaxon
argument_list|()
operator|!=
literal|null
operator|&&
name|getSaxon
argument_list|()
decl_stmt|;
name|boolean
name|isLogNamespaces
init|=
name|getLogNamespaces
argument_list|()
operator|!=
literal|null
operator|&&
name|getLogNamespaces
argument_list|()
decl_stmt|;
if|if
condition|(
name|documentType
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|expression
argument_list|,
literal|"documentType"
argument_list|,
name|documentType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resultType
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|expression
argument_list|,
literal|"resultType"
argument_list|,
name|resultType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isSaxon
condition|)
block|{
name|setProperty
argument_list|(
name|expression
argument_list|,
literal|"useSaxon"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xpathFactory
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|expression
argument_list|,
literal|"xPathFactory"
argument_list|,
name|xpathFactory
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|objectModel
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|expression
argument_list|,
literal|"objectModelUri"
argument_list|,
name|objectModel
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|threadSafety
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|expression
argument_list|,
literal|"threadSafety"
argument_list|,
name|threadSafety
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isLogNamespaces
condition|)
block|{
name|setProperty
argument_list|(
name|expression
argument_list|,
literal|"logNamespaces"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getHeaderName
argument_list|()
argument_list|)
condition|)
block|{
name|setProperty
argument_list|(
name|expression
argument_list|,
literal|"headerName"
argument_list|,
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// moved the super configuration to the bottom so that the namespace init picks up the newly set XPath Factory
name|super
operator|.
name|configureExpression
argument_list|(
name|camelContext
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configurePredicate (CamelContext camelContext, Predicate predicate)
specifier|protected
name|void
name|configurePredicate
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Predicate
name|predicate
parameter_list|)
block|{
name|boolean
name|isSaxon
init|=
name|getSaxon
argument_list|()
operator|!=
literal|null
operator|&&
name|getSaxon
argument_list|()
decl_stmt|;
name|boolean
name|isLogNamespaces
init|=
name|getLogNamespaces
argument_list|()
operator|!=
literal|null
operator|&&
name|getLogNamespaces
argument_list|()
decl_stmt|;
if|if
condition|(
name|documentType
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|predicate
argument_list|,
literal|"documentType"
argument_list|,
name|documentType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resultType
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|predicate
argument_list|,
literal|"resultType"
argument_list|,
name|resultType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isSaxon
condition|)
block|{
name|setProperty
argument_list|(
name|predicate
argument_list|,
literal|"useSaxon"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xpathFactory
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|predicate
argument_list|,
literal|"xPathFactory"
argument_list|,
name|xpathFactory
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|objectModel
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|predicate
argument_list|,
literal|"objectModelUri"
argument_list|,
name|objectModel
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|threadSafety
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|predicate
argument_list|,
literal|"threadSafety"
argument_list|,
name|threadSafety
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isLogNamespaces
condition|)
block|{
name|setProperty
argument_list|(
name|predicate
argument_list|,
literal|"logNamespaces"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getHeaderName
argument_list|()
argument_list|)
condition|)
block|{
name|setProperty
argument_list|(
name|predicate
argument_list|,
literal|"headerName"
argument_list|,
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// moved the super configuration to the bottom so that the namespace init picks up the newly set XPath Factory
name|super
operator|.
name|configurePredicate
argument_list|(
name|camelContext
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
DECL|method|resolveXPathFactory (CamelContext camelContext)
specifier|private
name|void
name|resolveXPathFactory
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
comment|// Factory and Object Model can be set simultaneously. The underlying XPathBuilder allows for setting Saxon too, as it is simply a shortcut for
comment|// setting the appropriate Object Model, it is not wise to allow this in XML because the order of invocation of the setters by JAXB may cause undeterministic behaviour
if|if
condition|(
operator|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|factoryRef
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|objectModel
argument_list|)
operator|)
operator|&&
operator|(
name|saxon
operator|!=
literal|null
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The saxon attribute cannot be set on the xpath element if any of the following is also set: factory, objectModel"
operator|+
name|this
argument_list|)
throw|;
block|}
comment|// Validate the factory class
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|factoryRef
argument_list|)
condition|)
block|{
name|xpathFactory
operator|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|factoryRef
argument_list|,
name|XPathFactory
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|xpathFactory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The provided XPath Factory is invalid; either it cannot be resolved or it is not an XPathFactory instance"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

