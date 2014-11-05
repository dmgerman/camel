begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|NoSuchBeanException
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
name|spi
operator|.
name|Registry
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

begin_comment
comment|/**  * An implementation of a {@link BeanHolder} which will look up a bean from the registry and act as a cache of its metadata  *  * @version   */
end_comment

begin_class
DECL|class|RegistryBean
specifier|public
class|class
name|RegistryBean
implements|implements
name|BeanHolder
block|{
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|registry
specifier|private
specifier|final
name|Registry
name|registry
decl_stmt|;
DECL|field|beanInfo
specifier|private
specifier|volatile
name|BeanInfo
name|beanInfo
decl_stmt|;
DECL|field|clazz
specifier|private
specifier|volatile
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
decl_stmt|;
DECL|field|parameterMappingStrategy
specifier|private
name|ParameterMappingStrategy
name|parameterMappingStrategy
decl_stmt|;
DECL|method|RegistryBean (CamelContext context, String name)
specifier|public
name|RegistryBean
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|registry
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
expr_stmt|;
block|}
DECL|method|RegistryBean (Registry registry, CamelContext context, String name)
specifier|public
name|RegistryBean
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
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
literal|"bean: "
operator|+
name|name
return|;
block|}
comment|/**      * Creates a cached and constant {@link org.apache.camel.component.bean.BeanHolder} from this holder.      *      * @return a new {@link org.apache.camel.component.bean.BeanHolder} that has cached the lookup of the bean.      */
DECL|method|createCacheHolder ()
specifier|public
name|ConstantBeanHolder
name|createCacheHolder
parameter_list|()
block|{
name|Object
name|bean
init|=
name|getBean
argument_list|()
decl_stmt|;
name|BeanInfo
name|info
init|=
name|createBeanInfo
argument_list|(
name|bean
argument_list|)
decl_stmt|;
return|return
operator|new
name|ConstantBeanHolder
argument_list|(
name|bean
argument_list|,
name|info
argument_list|)
return|;
block|}
DECL|method|getBean ()
specifier|public
name|Object
name|getBean
parameter_list|()
throws|throws
name|NoSuchBeanException
block|{
comment|// must always lookup bean first
name|Object
name|value
init|=
name|lookupBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
comment|// could be a class then create an instance of it
if|if
condition|(
name|value
operator|instanceof
name|Class
condition|)
block|{
comment|// bean is a class so create an instance of it
name|value
operator|=
name|context
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
comment|// okay bean is not in registry, so try to resolve if its a class name and create a shared instance
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
name|clazz
operator|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
comment|// no its not a class then we cannot find the bean
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|name
argument_list|)
throw|;
block|}
comment|// bean is a class so create an instance of it
return|return
name|context
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|getBeanInfo ()
specifier|public
name|BeanInfo
name|getBeanInfo
parameter_list|()
block|{
if|if
condition|(
name|beanInfo
operator|==
literal|null
condition|)
block|{
name|Object
name|bean
init|=
name|getBean
argument_list|()
decl_stmt|;
name|this
operator|.
name|beanInfo
operator|=
name|createBeanInfo
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
return|return
name|beanInfo
return|;
block|}
DECL|method|getBeanInfo (Object bean)
specifier|public
name|BeanInfo
name|getBeanInfo
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
return|return
name|createBeanInfo
argument_list|(
name|bean
argument_list|)
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getRegistry ()
specifier|public
name|Registry
name|getRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|getParameterMappingStrategy ()
specifier|public
name|ParameterMappingStrategy
name|getParameterMappingStrategy
parameter_list|()
block|{
if|if
condition|(
name|parameterMappingStrategy
operator|==
literal|null
condition|)
block|{
name|parameterMappingStrategy
operator|=
name|createParameterMappingStrategy
argument_list|()
expr_stmt|;
block|}
return|return
name|parameterMappingStrategy
return|;
block|}
DECL|method|setParameterMappingStrategy (ParameterMappingStrategy parameterMappingStrategy)
specifier|public
name|void
name|setParameterMappingStrategy
parameter_list|(
name|ParameterMappingStrategy
name|parameterMappingStrategy
parameter_list|)
block|{
name|this
operator|.
name|parameterMappingStrategy
operator|=
name|parameterMappingStrategy
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|createBeanInfo (Object bean)
specifier|protected
name|BeanInfo
name|createBeanInfo
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
return|return
operator|new
name|BeanInfo
argument_list|(
name|context
argument_list|,
name|bean
operator|.
name|getClass
argument_list|()
argument_list|,
name|getParameterMappingStrategy
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createParameterMappingStrategy ()
specifier|protected
name|ParameterMappingStrategy
name|createParameterMappingStrategy
parameter_list|()
block|{
return|return
name|BeanInfo
operator|.
name|createParameterMappingStrategy
argument_list|(
name|context
argument_list|)
return|;
block|}
DECL|method|lookupBean ()
specifier|protected
name|Object
name|lookupBean
parameter_list|()
block|{
return|return
name|registry
operator|.
name|lookupByName
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

