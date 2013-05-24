begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|bean
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
name|IsSingleton
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
name|Language
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
name|ExpressionToPredicateAdapter
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
comment|/**  * A<a href="http://camel.apache.org/bean-language.html">bean language</a>  * which uses a simple text notation to invoke methods on beans to evaluate predicates or expressions  *<p/>  * The notation is essentially<code>beanName.methodName</code> which is then invoked using the  * beanName to lookup in the<a href="http://camel.apache.org/registry.html>registry</a>  * then the method is invoked to evaluate the expression using the  *<a href="http://camel.apache.org/bean-integration.html">bean integration</a> to bind the  * {@link org.apache.camel.Exchange} to the method arguments.  *<p/>  * As of Camel 1.5 the bean language also supports invoking a provided bean by  * its classname or the bean itself.  *  * @version   */
end_comment

begin_class
DECL|class|BeanLanguage
specifier|public
class|class
name|BeanLanguage
implements|implements
name|Language
implements|,
name|IsSingleton
block|{
comment|/**      * Creates the expression based on the string syntax.      *      * @param expression the string syntax<tt>beanRef.methodName</tt> where methodName can be omitted      * @return the expression      */
DECL|method|bean (String expression)
specifier|public
specifier|static
name|Expression
name|bean
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|BeanLanguage
name|language
init|=
operator|new
name|BeanLanguage
argument_list|()
decl_stmt|;
return|return
name|language
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Creates the expression for invoking the bean type.      *      * @param beanType  the bean type to invoke      * @param method optional name of method to invoke for instance to avoid ambiguity      * @return the expression      */
DECL|method|bean (Class<?> beanType, String method)
specifier|public
specifier|static
name|Expression
name|bean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanType
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|Object
name|bean
init|=
name|ObjectHelper
operator|.
name|newInstance
argument_list|(
name|beanType
argument_list|)
decl_stmt|;
return|return
name|bean
argument_list|(
name|bean
argument_list|,
name|method
argument_list|)
return|;
block|}
comment|/**      * Creates the expression for invoking the bean type.      *      * @param bean  the bean to invoke      * @param method optional name of method to invoke for instance to avoid ambiguity      * @return the expression      */
DECL|method|bean (Object bean, String method)
specifier|public
specifier|static
name|Expression
name|bean
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|BeanLanguage
name|language
init|=
operator|new
name|BeanLanguage
argument_list|()
decl_stmt|;
return|return
name|language
operator|.
name|createExpression
argument_list|(
name|bean
argument_list|,
name|method
argument_list|)
return|;
block|}
DECL|method|createPredicate (String expression)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|ExpressionToPredicateAdapter
operator|.
name|toPredicate
argument_list|(
name|createExpression
argument_list|(
name|expression
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createExpression (String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|String
name|beanName
init|=
name|expression
decl_stmt|;
name|String
name|method
init|=
literal|null
decl_stmt|;
comment|// we support both the .method name and the ?method= syntax
comment|// as the ?method= syntax is very common for the bean component
if|if
condition|(
name|expression
operator|.
name|contains
argument_list|(
literal|"?method="
argument_list|)
condition|)
block|{
name|beanName
operator|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|expression
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
name|method
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|expression
argument_list|,
literal|"?method="
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|idx
init|=
name|expression
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|beanName
operator|=
name|expression
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|method
operator|=
name|expression
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|BeanExpression
argument_list|(
name|beanName
argument_list|,
name|method
argument_list|)
return|;
block|}
DECL|method|createExpression (Object bean, String method)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|bean
argument_list|,
literal|"bean"
argument_list|)
expr_stmt|;
return|return
operator|new
name|BeanExpression
argument_list|(
name|bean
argument_list|,
name|method
argument_list|)
return|;
block|}
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
block|}
end_class

end_unit

