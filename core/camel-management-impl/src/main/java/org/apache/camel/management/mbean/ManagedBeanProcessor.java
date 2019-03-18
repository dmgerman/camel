begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedBeanMBean
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
name|model
operator|.
name|ProcessorDefinition
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

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Bean Processor"
argument_list|)
DECL|class|ManagedBeanProcessor
specifier|public
class|class
name|ManagedBeanProcessor
extends|extends
name|ManagedProcessor
implements|implements
name|ManagedBeanMBean
block|{
DECL|field|beanClassName
specifier|private
specifier|transient
name|String
name|beanClassName
decl_stmt|;
DECL|method|ManagedBeanProcessor (CamelContext context, BeanProcessor processor, ProcessorDefinition<?> definition)
specifier|public
name|ManagedBeanProcessor
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|BeanProcessor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProcessor ()
specifier|public
name|BeanProcessor
name|getProcessor
parameter_list|()
block|{
return|return
operator|(
name|BeanProcessor
operator|)
name|super
operator|.
name|getProcessor
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getInstance ()
specifier|public
name|Object
name|getInstance
parameter_list|()
block|{
return|return
name|getProcessor
argument_list|()
operator|.
name|getBean
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|getProcessor
argument_list|()
operator|.
name|getMethod
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getBeanClassName ()
specifier|public
name|String
name|getBeanClassName
parameter_list|()
block|{
if|if
condition|(
name|beanClassName
operator|!=
literal|null
condition|)
block|{
return|return
name|beanClassName
return|;
block|}
try|try
block|{
name|Object
name|bean
init|=
name|getProcessor
argument_list|()
operator|.
name|getBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
name|beanClassName
operator|=
name|ObjectHelper
operator|.
name|className
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchBeanException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|beanClassName
return|;
block|}
block|}
end_class

end_unit

