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
name|builder
operator|.
name|SimpleBuilder
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

begin_comment
comment|/**  * To use Camels built-in Simple language in Camel expressions or predicates.  */
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
literal|"language,core,java"
argument_list|,
name|title
operator|=
literal|"Simple"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"simple"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|SimpleExpression
specifier|public
class|class
name|SimpleExpression
extends|extends
name|ExpressionDefinition
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
name|XmlTransient
DECL|field|resultType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
decl_stmt|;
DECL|method|SimpleExpression ()
specifier|public
name|SimpleExpression
parameter_list|()
block|{     }
DECL|method|SimpleExpression (String expression)
specifier|public
name|SimpleExpression
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
DECL|method|SimpleExpression (Expression expression)
specifier|public
name|SimpleExpression
parameter_list|(
name|Expression
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
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
literal|"simple"
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
comment|/**      * Sets the class of the result type (type from output)      */
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
comment|/**      * Sets the class name of the result type (type from output)      */
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
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|String
name|exp
init|=
name|getExpression
argument_list|()
decl_stmt|;
comment|// should be true by default
name|boolean
name|isTrim
init|=
name|getTrim
argument_list|()
operator|==
literal|null
operator|||
name|getTrim
argument_list|()
decl_stmt|;
if|if
condition|(
name|exp
operator|!=
literal|null
operator|&&
name|isTrim
condition|)
block|{
name|exp
operator|=
name|exp
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
name|SimpleBuilder
name|answer
init|=
operator|new
name|SimpleBuilder
argument_list|(
name|exp
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
return|return
name|answer
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
comment|// SimpleBuilder is also a Predicate
return|return
operator|(
name|Predicate
operator|)
name|createExpression
argument_list|(
name|camelContext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

