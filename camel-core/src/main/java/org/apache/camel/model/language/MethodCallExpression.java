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
name|language
operator|.
name|bean
operator|.
name|BeanExpression
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
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * For expresions and predicates using the  *<a href="http://activemq.apache.org/camel/bean-language.html">bean language</a>  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"methodCall"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|MethodCallExpression
specifier|public
class|class
name|MethodCallExpression
extends|extends
name|ExpressionType
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|bean
specifier|private
name|String
name|bean
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
DECL|method|MethodCallExpression ()
specifier|public
name|MethodCallExpression
parameter_list|()
block|{     }
DECL|method|MethodCallExpression (String beanName)
specifier|public
name|MethodCallExpression
parameter_list|(
name|String
name|beanName
parameter_list|)
block|{
name|super
argument_list|(
name|beanName
argument_list|)
expr_stmt|;
block|}
DECL|method|MethodCallExpression (String beanName, String method)
specifier|public
name|MethodCallExpression
parameter_list|(
name|String
name|beanName
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|super
argument_list|(
name|beanName
argument_list|)
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
literal|"bean"
return|;
block|}
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
DECL|method|setMethod (String method)
specifier|public
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createExpression (RouteContext routeContext)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
return|return
operator|new
name|BeanExpression
argument_list|(
name|beanName
argument_list|()
argument_list|,
name|getMethod
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createPredicate (RouteContext routeContext)
specifier|public
name|Predicate
argument_list|<
name|Exchange
argument_list|>
name|createPredicate
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
return|return
operator|new
name|BeanExpression
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|beanName
argument_list|()
argument_list|,
name|getMethod
argument_list|()
argument_list|)
return|;
block|}
DECL|method|beanName ()
specifier|protected
name|String
name|beanName
parameter_list|()
block|{
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
return|return
name|bean
return|;
block|}
return|return
name|getExpression
argument_list|()
return|;
block|}
block|}
end_class

end_unit

