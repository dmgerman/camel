begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
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
comment|/**  * For XPath expressions and predicates  */
end_comment

begin_class
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
literal|"resultType"
argument_list|)
DECL|field|resultTypeName
specifier|private
name|String
name|resultTypeName
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"saxon"
argument_list|)
DECL|field|saxon
specifier|private
name|Boolean
name|saxon
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"factoryRef"
argument_list|)
DECL|field|factoryRef
specifier|private
name|String
name|factoryRef
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"objectModel"
argument_list|)
DECL|field|objectModel
specifier|private
name|String
name|objectModel
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"traceNamespaces"
argument_list|)
DECL|field|traceNamespaces
specifier|private
name|Boolean
name|traceNamespaces
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
DECL|method|isSaxon ()
specifier|public
name|boolean
name|isSaxon
parameter_list|()
block|{
return|return
name|saxon
operator|!=
literal|null
operator|&&
name|saxon
return|;
block|}
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
DECL|method|setTraceNamespaces (Boolean traceNamespaces)
specifier|public
name|void
name|setTraceNamespaces
parameter_list|(
name|Boolean
name|traceNamespaces
parameter_list|)
block|{
name|this
operator|.
name|traceNamespaces
operator|=
name|traceNamespaces
expr_stmt|;
block|}
DECL|method|getTraceNamespaces ()
specifier|public
name|Boolean
name|getTraceNamespaces
parameter_list|()
block|{
return|return
name|traceNamespaces
return|;
block|}
DECL|method|isTraceNamespaces ()
specifier|public
name|boolean
name|isTraceNamespaces
parameter_list|()
block|{
return|return
name|traceNamespaces
operator|!=
literal|null
operator|&&
name|traceNamespaces
return|;
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
name|ObjectHelper
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
argument_list|()
condition|)
block|{
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|XPathBuilder
operator|.
name|class
argument_list|,
name|expression
argument_list|)
operator|.
name|enableSaxon
argument_list|()
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
name|isTraceNamespaces
argument_list|()
condition|)
block|{
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|XPathBuilder
operator|.
name|class
argument_list|,
name|expression
argument_list|)
operator|.
name|setTraceNamespaces
argument_list|(
literal|true
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
argument_list|()
condition|)
block|{
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|XPathBuilder
operator|.
name|class
argument_list|,
name|predicate
argument_list|)
operator|.
name|enableSaxon
argument_list|()
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
name|isTraceNamespaces
argument_list|()
condition|)
block|{
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|XPathBuilder
operator|.
name|class
argument_list|,
name|predicate
argument_list|)
operator|.
name|setTraceNamespaces
argument_list|(
literal|true
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
name|lookup
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

