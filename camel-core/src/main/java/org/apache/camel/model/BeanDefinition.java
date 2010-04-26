begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|Processor
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
name|spi
operator|.
name|RouteContext
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

begin_comment
comment|/**  * Represents an XML&lt;bean/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"bean"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|BeanDefinition
specifier|public
class|class
name|BeanDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|ProcessorDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|ref
specifier|private
name|String
name|ref
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
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
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
DECL|field|bean
specifier|private
name|Object
name|bean
decl_stmt|;
DECL|method|BeanDefinition ()
specifier|public
name|BeanDefinition
parameter_list|()
block|{     }
DECL|method|BeanDefinition (String ref)
specifier|public
name|BeanDefinition
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
DECL|method|BeanDefinition (String ref, String method)
specifier|public
name|BeanDefinition
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
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
literal|"Bean["
operator|+
name|getLabel
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"bean"
return|;
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
DECL|method|setBean (Object bean)
specifier|public
name|void
name|setBean
parameter_list|(
name|Object
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
comment|// Fluent API
comment|//-------------------------------------------------------------------------
comment|/**      * Sets the ref String on camel bean      *      * @param ref  the bean's id in the registry      * @return the builder      */
DECL|method|ref (String ref)
specifier|public
name|BeanDefinition
name|ref
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|setRef
argument_list|(
name|ref
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the calling method name of camel bean      *      * @param method  the bean's method name which wants camel to call      * @return the builder      */
DECL|method|method (String method)
specifier|public
name|BeanDefinition
name|method
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the bean's instance that camel to call      *      * @param bean the instance of the bean      * @return the builder      */
DECL|method|bean (Object bean)
specifier|public
name|BeanDefinition
name|bean
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
name|setBean
argument_list|(
name|bean
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the Class of the bean that camel will instantiation it for calling      *      * @param beanType the Class of the bean      * @return the builder      */
DECL|method|beanType (Class<?> beanType)
specifier|public
name|BeanDefinition
name|beanType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanType
parameter_list|)
block|{
name|setBean
argument_list|(
name|beanType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|BeanProcessor
name|answer
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|ref
argument_list|)
condition|)
block|{
name|answer
operator|=
operator|new
name|BeanProcessor
argument_list|(
operator|new
name|RegistryBean
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|ref
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|bean
operator|==
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|beanType
argument_list|,
literal|"bean, ref or beanType"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|bean
operator|=
name|CamelContextHelper
operator|.
name|newInstance
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|beanType
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
operator|new
name|BeanProcessor
argument_list|(
name|bean
argument_list|,
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|String
name|methodText
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|methodText
operator|=
literal|" method: "
operator|+
name|method
expr_stmt|;
block|}
return|return
literal|"ref:"
operator|+
name|ref
operator|+
name|methodText
return|;
block|}
elseif|else
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
return|return
name|bean
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|beanType
operator|!=
literal|null
condition|)
block|{
return|return
name|beanType
operator|.
name|getName
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
block|}
end_class

end_unit

