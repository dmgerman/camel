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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * For XQuery expressions and predicates  *  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"xquery"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|XQueryExpression
specifier|public
class|class
name|XQueryExpression
extends|extends
name|NamespaceAwareExpression
block|{
annotation|@
name|XmlAttribute
DECL|field|type
specifier|private
name|String
name|type
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
name|XmlAttribute
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
DECL|method|XQueryExpression ()
specifier|public
name|XQueryExpression
parameter_list|()
block|{     }
DECL|method|XQueryExpression (String expression)
specifier|public
name|XQueryExpression
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
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
literal|"xquery"
return|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
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
name|type
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
name|type
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
name|super
operator|.
name|configureExpression
argument_list|(
name|camelContext
argument_list|,
name|expression
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|configurePredicate
argument_list|(
name|camelContext
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
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
block|}
block|}
end_class

end_unit

