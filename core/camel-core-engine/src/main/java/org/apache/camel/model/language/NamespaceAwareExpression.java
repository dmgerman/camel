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
name|XmlTransient
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
name|spi
operator|.
name|NamespaceAware
import|;
end_import

begin_comment
comment|/**  * A useful base class for any expression which may be namespace or XML content  * aware such as {@link XPathExpression} or {@link XQueryExpression}  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|NamespaceAwareExpression
specifier|public
specifier|abstract
class|class
name|NamespaceAwareExpression
extends|extends
name|ExpressionDefinition
implements|implements
name|NamespaceAware
block|{
annotation|@
name|XmlTransient
DECL|field|namespaces
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
decl_stmt|;
DECL|method|NamespaceAwareExpression ()
specifier|public
name|NamespaceAwareExpression
parameter_list|()
block|{     }
DECL|method|NamespaceAwareExpression (String expression)
specifier|public
name|NamespaceAwareExpression
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
annotation|@
name|Override
DECL|method|getNamespaces ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNamespaces
parameter_list|()
block|{
return|return
name|namespaces
return|;
block|}
comment|/**      * Injects the XML Namespaces of prefix -> uri mappings      *      * @param namespaces the XML namespaces with the key of prefixes and the      *            value the URIs      */
annotation|@
name|Override
DECL|method|setNamespaces (Map<String, String> namespaces)
specifier|public
name|void
name|setNamespaces
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|this
operator|.
name|namespaces
operator|=
name|namespaces
expr_stmt|;
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
name|configureNamespaceAware
argument_list|(
name|expression
argument_list|)
expr_stmt|;
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
name|configureNamespaceAware
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
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
DECL|method|configureNamespaceAware (Object builder)
specifier|protected
name|void
name|configureNamespaceAware
parameter_list|(
name|Object
name|builder
parameter_list|)
block|{
if|if
condition|(
name|namespaces
operator|!=
literal|null
operator|&&
name|builder
operator|instanceof
name|NamespaceAware
condition|)
block|{
name|NamespaceAware
name|namespaceAware
init|=
operator|(
name|NamespaceAware
operator|)
name|builder
decl_stmt|;
name|namespaceAware
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

