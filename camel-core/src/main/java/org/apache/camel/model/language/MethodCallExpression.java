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
name|ExpressionIllegalSyntaxException
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
name|BeanInfo
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
name|MethodNotFoundException
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
name|util
operator|.
name|CamelContextHelper
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
name|OgnlHelper
import|;
end_import

begin_comment
comment|/**  * For expressions and predicates using the  *<a href="http://camel.apache.org/bean-language.html">bean language</a>  *  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"method"
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
name|ExpressionDefinition
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Deprecated
DECL|field|bean
specifier|private
name|String
name|bean
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ref
specifier|private
name|String
name|ref
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"beanType"
argument_list|)
DECL|field|beanTypeName
specifier|private
name|String
name|beanTypeName
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|beanType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|beanType
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|instance
specifier|private
name|Object
name|instance
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
name|this
argument_list|(
name|beanName
argument_list|,
literal|null
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
DECL|method|MethodCallExpression (Object instance)
specifier|public
name|MethodCallExpression
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
name|this
argument_list|(
name|instance
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|MethodCallExpression (Object instance, String method)
specifier|public
name|MethodCallExpression
parameter_list|(
name|Object
name|instance
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|super
argument_list|(
name|instance
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|instance
operator|=
name|instance
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|MethodCallExpression (Class<?> type)
specifier|public
name|MethodCallExpression
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|this
argument_list|(
name|type
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|MethodCallExpression (Class<?> type, String method)
specifier|public
name|MethodCallExpression
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|super
argument_list|(
name|type
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|beanType
operator|=
name|type
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
DECL|method|getBean ()
specifier|public
name|String
name|getBean
parameter_list|()
block|{
return|return
name|bean
return|;
block|}
DECL|method|setBean (String bean)
specifier|public
name|void
name|setBean
parameter_list|(
name|String
name|bean
parameter_list|)
block|{
name|this
operator|.
name|bean
operator|=
name|bean
expr_stmt|;
block|}
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
DECL|method|setRef (String ref)
specifier|public
name|void
name|setRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
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
DECL|method|getBeanType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getBeanType
parameter_list|()
block|{
return|return
name|beanType
return|;
block|}
DECL|method|setBeanType (Class<?> beanType)
specifier|public
name|void
name|setBeanType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanType
parameter_list|)
block|{
name|this
operator|.
name|beanType
operator|=
name|beanType
expr_stmt|;
block|}
DECL|method|getBeanTypeName ()
specifier|public
name|String
name|getBeanTypeName
parameter_list|()
block|{
return|return
name|beanTypeName
return|;
block|}
DECL|method|setBeanTypeName (String beanTypeName)
specifier|public
name|void
name|setBeanTypeName
parameter_list|(
name|String
name|beanTypeName
parameter_list|)
block|{
name|this
operator|.
name|beanTypeName
operator|=
name|beanTypeName
expr_stmt|;
block|}
DECL|method|getInstance ()
specifier|public
name|Object
name|getInstance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
DECL|method|setInstance (Object instance)
specifier|public
name|void
name|setInstance
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
name|this
operator|.
name|instance
operator|=
name|instance
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
name|Expression
name|answer
decl_stmt|;
if|if
condition|(
name|beanType
operator|==
literal|null
operator|&&
name|beanTypeName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|beanType
operator|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|beanTypeName
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
if|if
condition|(
name|beanType
operator|!=
literal|null
condition|)
block|{
comment|// create a bean if there is a default public no-arg constructor
if|if
condition|(
name|ObjectHelper
operator|.
name|hasDefaultPublicNoArgConstructor
argument_list|(
name|beanType
argument_list|)
condition|)
block|{
name|instance
operator|=
name|camelContext
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|beanType
argument_list|)
expr_stmt|;
name|answer
operator|=
operator|new
name|BeanExpression
argument_list|(
name|instance
argument_list|,
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|BeanExpression
argument_list|(
name|beanType
argument_list|,
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|BeanExpression
argument_list|(
name|instance
argument_list|,
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|ref
init|=
name|beanName
argument_list|()
decl_stmt|;
comment|// if its a ref then check that the ref exists
name|BeanHolder
name|holder
init|=
operator|new
name|RegistryBean
argument_list|(
name|camelContext
argument_list|,
name|ref
argument_list|)
decl_stmt|;
comment|// get the bean which will check that it exists
name|instance
operator|=
name|holder
operator|.
name|getBean
argument_list|()
expr_stmt|;
name|answer
operator|=
operator|new
name|BeanExpression
argument_list|(
name|instance
argument_list|,
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|validateHasMethod
argument_list|(
name|camelContext
argument_list|,
name|instance
argument_list|,
name|beanType
argument_list|,
name|getMethod
argument_list|()
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
return|return
operator|(
name|BeanExpression
operator|)
name|createExpression
argument_list|(
name|camelContext
argument_list|)
return|;
block|}
comment|/**      * Validates the given bean has the method.      *<p/>      * This implementation will skip trying to validate OGNL method name expressions.      *      * @param context  camel context      * @param bean     the bean instance      * @param type     the bean type      * @param method   the method, can be<tt>null</tt> if no method name provided      * @throws org.apache.camel.RuntimeCamelException is thrown if bean does not have the method      */
DECL|method|validateHasMethod (CamelContext context, Object bean, Class<?> type, String method)
specifier|protected
name|void
name|validateHasMethod
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Object
name|bean
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|String
name|method
parameter_list|)
block|{
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|bean
operator|==
literal|null
operator|&&
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Either bean or type should be provided on "
operator|+
name|this
argument_list|)
throw|;
block|}
comment|// do not try to validate ognl methods
if|if
condition|(
name|OgnlHelper
operator|.
name|isValidOgnlExpression
argument_list|(
name|method
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// if invalid OGNL then fail
if|if
condition|(
name|OgnlHelper
operator|.
name|isInvalidValidOgnlExpression
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|ExpressionIllegalSyntaxException
name|cause
init|=
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
name|method
argument_list|)
decl_stmt|;
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
operator|new
name|MethodNotFoundException
argument_list|(
name|bean
operator|!=
literal|null
condition|?
name|bean
else|:
name|type
argument_list|,
name|method
argument_list|,
name|cause
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
name|BeanInfo
name|info
init|=
operator|new
name|BeanInfo
argument_list|(
name|context
argument_list|,
name|bean
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|info
operator|.
name|hasMethod
argument_list|(
name|method
argument_list|)
condition|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
operator|new
name|MethodNotFoundException
argument_list|(
literal|null
argument_list|,
name|bean
argument_list|,
name|method
argument_list|)
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|BeanInfo
name|info
init|=
operator|new
name|BeanInfo
argument_list|(
name|context
argument_list|,
name|type
argument_list|)
decl_stmt|;
comment|// must be a static method as we do not have a bean instance to invoke
if|if
condition|(
operator|!
name|info
operator|.
name|hasStaticMethod
argument_list|(
name|method
argument_list|)
condition|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
operator|new
name|MethodNotFoundException
argument_list|(
literal|null
argument_list|,
name|type
argument_list|,
name|method
argument_list|,
literal|true
argument_list|)
argument_list|)
throw|;
block|}
block|}
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
elseif|else
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
return|return
name|ref
return|;
block|}
elseif|else
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
return|return
name|ObjectHelper
operator|.
name|className
argument_list|(
name|instance
argument_list|)
return|;
block|}
return|return
name|getExpression
argument_list|()
return|;
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
literal|"bean{"
operator|+
name|beanName
argument_list|()
operator|+
operator|(
name|method
operator|!=
literal|null
condition|?
literal|", method="
operator|+
name|method
else|:
literal|""
operator|)
operator|+
literal|"}"
return|;
block|}
block|}
end_class

end_unit

