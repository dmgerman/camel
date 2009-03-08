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
name|ExchangePattern
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
name|component
operator|.
name|bean
operator|.
name|BeanHolder
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
name|bean
operator|.
name|BeanProcessor
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
name|bean
operator|.
name|ConstantBeanHolder
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
name|bean
operator|.
name|RegistryBean
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
comment|/**  * Evaluates an expression using a bean method invocation  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|BeanExpression
specifier|public
class|class
name|BeanExpression
implements|implements
name|Expression
implements|,
name|Predicate
block|{
DECL|field|beanName
specifier|private
name|String
name|beanName
decl_stmt|;
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
DECL|field|bean
specifier|private
name|Object
name|bean
decl_stmt|;
DECL|method|BeanExpression (Object bean, String method)
specifier|public
name|BeanExpression
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|bean
operator|=
name|bean
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|BeanExpression (String beanName, String method)
specifier|public
name|BeanExpression
parameter_list|(
name|String
name|beanName
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|beanName
operator|=
name|beanName
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"BeanExpression[bean:"
operator|+
operator|(
name|bean
operator|==
literal|null
condition|?
name|beanName
else|:
name|bean
operator|)
operator|+
literal|" method: "
operator|+
name|method
operator|+
literal|"]"
return|;
block|}
DECL|method|assertionFailureMessage (Exchange exchange)
specifier|protected
name|String
name|assertionFailureMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
literal|"bean: "
operator|+
name|beanName
operator|+
literal|" method: "
operator|+
name|method
return|;
block|}
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// either use registry lookup or a constant bean
name|BeanHolder
name|holder
decl_stmt|;
if|if
condition|(
name|bean
operator|==
literal|null
condition|)
block|{
name|holder
operator|=
operator|new
name|RegistryBean
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|holder
operator|=
operator|new
name|ConstantBeanHolder
argument_list|(
name|bean
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|BeanProcessor
name|processor
init|=
operator|new
name|BeanProcessor
argument_list|(
name|holder
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Exchange
name|newExchange
init|=
name|exchange
operator|.
name|copy
argument_list|()
decl_stmt|;
comment|// The BeanExperession always has a result regardless of the ExchangePattern,
comment|// so I add a checker here to make sure we can get the result.
if|if
condition|(
operator|!
name|newExchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|newExchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
name|processor
operator|.
name|process
argument_list|(
name|newExchange
argument_list|)
expr_stmt|;
return|return
name|newExchange
operator|.
name|getOut
argument_list|(
literal|true
argument_list|)
operator|.
name|getBody
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeBeanExpressionException
argument_list|(
name|exchange
argument_list|,
name|beanName
argument_list|,
name|method
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|evaluate (Exchange exchange, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|result
init|=
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|result
argument_list|)
return|;
block|}
DECL|method|matches (Exchange exchange)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|value
init|=
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|evaluateValuePredicate
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|assertMatches (String text, Exchange exchange)
specifier|public
name|void
name|assertMatches
parameter_list|(
name|String
name|text
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|AssertionError
block|{     }
block|}
end_class

end_unit

