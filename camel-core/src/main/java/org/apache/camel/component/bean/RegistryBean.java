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
name|NoTypeConversionAvailableException
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
comment|/**  * An implementation of a {@link BeanHolder} which will look up a bean from the registry and act as a cache of its metadata  *  * @version $Revision$  */
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
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|field|beanInfo
specifier|private
name|BeanInfo
name|beanInfo
decl_stmt|;
DECL|field|bean
specifier|private
name|Object
name|bean
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
DECL|method|RegistryBean (CamelContext context, String name, ParameterMappingStrategy parameterMappingStrategy)
specifier|public
name|RegistryBean
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|,
name|ParameterMappingStrategy
name|parameterMappingStrategy
parameter_list|)
block|{
name|this
argument_list|(
name|context
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|parameterMappingStrategy
operator|=
name|parameterMappingStrategy
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
DECL|method|createCacheHolder ()
specifier|public
name|ConstantBeanHolder
name|createCacheHolder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|ConstantBeanHolder
argument_list|(
name|getBean
argument_list|()
argument_list|,
name|getBeanInfo
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getBean ()
specifier|public
name|Object
name|getBean
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
name|lookupBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoBeanAvailableException
argument_list|(
name|name
argument_list|)
throw|;
block|}
if|if
condition|(
name|value
operator|!=
name|bean
condition|)
block|{
name|bean
operator|=
name|value
expr_stmt|;
name|processor
operator|=
literal|null
expr_stmt|;
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|ObjectHelper
operator|.
name|type
argument_list|(
name|bean
argument_list|)
argument_list|,
name|ObjectHelper
operator|.
name|type
argument_list|(
name|value
argument_list|)
argument_list|)
condition|)
block|{
name|beanInfo
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|value
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
if|if
condition|(
name|processor
operator|==
literal|null
operator|&&
name|bean
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|processor
operator|=
name|CamelContextHelper
operator|.
name|convertTo
argument_list|(
name|context
argument_list|,
name|Processor
operator|.
name|class
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
name|processor
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
operator|&&
name|bean
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|beanInfo
operator|=
name|createBeanInfo
argument_list|()
expr_stmt|;
block|}
return|return
name|beanInfo
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
DECL|method|createBeanInfo ()
specifier|protected
name|BeanInfo
name|createBeanInfo
parameter_list|()
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
throws|throws
name|Exception
block|{
return|return
name|registry
operator|.
name|lookup
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

