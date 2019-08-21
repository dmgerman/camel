begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

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
name|model
operator|.
name|Model
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
name|model
operator|.
name|language
operator|.
name|ExpressionDefinition
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
name|model
operator|.
name|validator
operator|.
name|CustomValidatorDefinition
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
name|model
operator|.
name|validator
operator|.
name|EndpointValidatorDefinition
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
name|model
operator|.
name|validator
operator|.
name|PredicateValidatorDefinition
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
name|model
operator|.
name|validator
operator|.
name|ValidatorDefinition
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
name|AsPredicate
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
name|DataType
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
name|Validator
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/dsl.html">Java DSL</a> which is used to  * build a {@link org.apache.camel.spi.Validator} and register into  * {@link org.apache.camel.CamelContext}. It requires a 'type' to be specified  * by type() method. And then you can choose a type of validator by withUri(),  * withPredicate(), withJava() or withBean() method.  */
end_comment

begin_class
DECL|class|ValidatorBuilder
specifier|public
class|class
name|ValidatorBuilder
block|{
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
DECL|field|expression
specifier|private
name|ExpressionDefinition
name|expression
decl_stmt|;
DECL|field|clazz
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Validator
argument_list|>
name|clazz
decl_stmt|;
DECL|field|beanRef
specifier|private
name|String
name|beanRef
decl_stmt|;
comment|/**      * Set the data type name. If you specify 'xml:XYZ', the validator will be      * picked up if source type is 'xml:XYZ'. If you specify just 'xml', the      * validator matches with all of 'xml' source type like 'xml:ABC' or      * 'xml:DEF'.      *      * @param type 'from' data type name      */
DECL|method|type (String type)
specifier|public
name|ValidatorBuilder
name|type
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
return|return
name|this
return|;
block|}
comment|/**      * Set the data type using Java class.      *      * @param type Java class represents data type      */
DECL|method|type (Class<?> type)
specifier|public
name|ValidatorBuilder
name|type
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
operator|new
name|DataType
argument_list|(
name|type
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the URI to be used for the endpoint {@link Validator}.      *       * @see EndpointValidatorDefinition, ProcessorValidator      * @param uri endpoint URI      */
DECL|method|withUri (String uri)
specifier|public
name|ValidatorBuilder
name|withUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|resetType
argument_list|()
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the {@link Expression} to be used for the predicate      * {@link Validator}.      *       * @see PredicateValidatorDefinition, ProcessorValidator      * @param expression validation expression      */
DECL|method|withExpression (@sPredicate Expression expression)
specifier|public
name|ValidatorBuilder
name|withExpression
parameter_list|(
annotation|@
name|AsPredicate
name|Expression
name|expression
parameter_list|)
block|{
name|resetType
argument_list|()
expr_stmt|;
name|this
operator|.
name|expression
operator|=
operator|new
name|ExpressionDefinition
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the {@link Predicate} to be used for the predicate {@link Validator}.      *       * @see PredicateValidatorDefinition, ProcessorValidator      * @param predicate validation predicate      */
DECL|method|withExpression (@sPredicate Predicate predicate)
specifier|public
name|ValidatorBuilder
name|withExpression
parameter_list|(
annotation|@
name|AsPredicate
name|Predicate
name|predicate
parameter_list|)
block|{
name|resetType
argument_list|()
expr_stmt|;
name|this
operator|.
name|expression
operator|=
operator|new
name|ExpressionDefinition
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the Java {@code Class} represents a custom {@code Validator}      * implementation class.      *       * @see CustomValidatorDefinition      * @param clazz {@code Class} object represents custom validator      *            implementation      */
DECL|method|withJava (Class<? extends Validator> clazz)
specifier|public
name|ValidatorBuilder
name|withJava
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Validator
argument_list|>
name|clazz
parameter_list|)
block|{
name|resetType
argument_list|()
expr_stmt|;
name|this
operator|.
name|clazz
operator|=
name|clazz
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the Java Bean name to be used for custom {@code Validator}.      *       * @see CustomValidatorDefinition      * @param ref bean name for the custom {@code Validator}      */
DECL|method|withBean (String ref)
specifier|public
name|ValidatorBuilder
name|withBean
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|resetType
argument_list|()
expr_stmt|;
name|this
operator|.
name|beanRef
operator|=
name|ref
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|resetType ()
specifier|private
name|void
name|resetType
parameter_list|()
block|{
name|this
operator|.
name|uri
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|expression
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|clazz
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|beanRef
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Configures a new Validator according to the configurations built on this      * builder and register it into the given {@code CamelContext}.      *       * @param camelContext the given CamelContext      */
DECL|method|configure (CamelContext camelContext)
specifier|public
name|void
name|configure
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|ValidatorDefinition
name|validator
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|EndpointValidatorDefinition
name|etd
init|=
operator|new
name|EndpointValidatorDefinition
argument_list|()
decl_stmt|;
name|etd
operator|.
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|validator
operator|=
name|etd
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
name|PredicateValidatorDefinition
name|dtd
init|=
operator|new
name|PredicateValidatorDefinition
argument_list|()
decl_stmt|;
name|dtd
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|validator
operator|=
name|dtd
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|CustomValidatorDefinition
name|ctd
init|=
operator|new
name|CustomValidatorDefinition
argument_list|()
decl_stmt|;
name|ctd
operator|.
name|setClassName
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|validator
operator|=
name|ctd
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|beanRef
operator|!=
literal|null
condition|)
block|{
name|CustomValidatorDefinition
name|ctd
init|=
operator|new
name|CustomValidatorDefinition
argument_list|()
decl_stmt|;
name|ctd
operator|.
name|setRef
argument_list|(
name|beanRef
argument_list|)
expr_stmt|;
name|validator
operator|=
name|ctd
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No Validator type was specified"
argument_list|)
throw|;
block|}
name|validator
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|getExtension
argument_list|(
name|Model
operator|.
name|class
argument_list|)
operator|.
name|getValidators
argument_list|()
operator|.
name|add
argument_list|(
name|validator
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

