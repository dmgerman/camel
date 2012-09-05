begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cdi.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cdi
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Consume
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
name|EndpointInject
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
name|Produce
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
name|cdi
operator|.
name|ContextName
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
name|impl
operator|.
name|DefaultCamelBeanPostProcessor
import|;
end_import

begin_comment
comment|/**  * Contains the bean and the consume methods  */
end_comment

begin_class
DECL|class|BeanAdapter
specifier|public
class|class
name|BeanAdapter
block|{
DECL|field|consumeMethods
specifier|private
specifier|final
name|List
argument_list|<
name|Method
argument_list|>
name|consumeMethods
init|=
operator|new
name|ArrayList
argument_list|<
name|Method
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|produceMethods
specifier|private
specifier|final
name|List
argument_list|<
name|Method
argument_list|>
name|produceMethods
init|=
operator|new
name|ArrayList
argument_list|<
name|Method
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|endpointMethods
specifier|private
specifier|final
name|List
argument_list|<
name|Method
argument_list|>
name|endpointMethods
init|=
operator|new
name|ArrayList
argument_list|<
name|Method
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|produceFields
specifier|private
specifier|final
name|List
argument_list|<
name|Field
argument_list|>
name|produceFields
init|=
operator|new
name|ArrayList
argument_list|<
name|Field
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|endpointFields
specifier|private
specifier|final
name|List
argument_list|<
name|Field
argument_list|>
name|endpointFields
init|=
operator|new
name|ArrayList
argument_list|<
name|Field
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|startup
specifier|private
specifier|final
name|ContextName
name|startup
decl_stmt|;
DECL|method|BeanAdapter (ContextName startup)
specifier|public
name|BeanAdapter
parameter_list|(
name|ContextName
name|startup
parameter_list|)
block|{
name|this
operator|.
name|startup
operator|=
name|startup
expr_stmt|;
block|}
comment|/**      * Returns true if this adapter is empty (i.e. has no custom adapter code)      */
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|consumeMethods
operator|.
name|isEmpty
argument_list|()
operator|&&
name|produceMethods
operator|.
name|isEmpty
argument_list|()
operator|&&
name|produceFields
operator|.
name|isEmpty
argument_list|()
operator|&&
name|endpointMethods
operator|.
name|isEmpty
argument_list|()
operator|&&
name|endpointFields
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|addConsumeMethod (Method method)
specifier|public
name|void
name|addConsumeMethod
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|consumeMethods
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
DECL|method|addProduceMethod (Method method)
specifier|public
name|void
name|addProduceMethod
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|produceMethods
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
DECL|method|addProduceField (Field field)
specifier|public
name|void
name|addProduceField
parameter_list|(
name|Field
name|field
parameter_list|)
block|{
name|produceFields
operator|.
name|add
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
DECL|method|addEndpointField (Field field)
specifier|public
name|void
name|addEndpointField
parameter_list|(
name|Field
name|field
parameter_list|)
block|{
name|endpointFields
operator|.
name|add
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
DECL|method|addEndpointMethod (Method method)
specifier|public
name|void
name|addEndpointMethod
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|endpointMethods
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
comment|/**      * Perform injections      */
DECL|method|inject (CamelExtension camelExtension, Object reference, String beanName)
specifier|public
name|void
name|inject
parameter_list|(
name|CamelExtension
name|camelExtension
parameter_list|,
name|Object
name|reference
parameter_list|,
name|String
name|beanName
parameter_list|)
block|{
for|for
control|(
name|Method
name|method
range|:
name|consumeMethods
control|)
block|{
name|Consume
name|annotation
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|Consume
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|String
name|contextName
init|=
name|CamelExtension
operator|.
name|getCamelContextName
argument_list|(
name|annotation
operator|.
name|context
argument_list|()
argument_list|,
name|startup
argument_list|)
decl_stmt|;
name|DefaultCamelBeanPostProcessor
name|postProcessor
init|=
name|camelExtension
operator|.
name|getPostProcessor
argument_list|(
name|contextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|postProcessor
operator|!=
literal|null
condition|)
block|{
name|postProcessor
operator|.
name|getPostProcessorHelper
argument_list|()
operator|.
name|consumerInjection
argument_list|(
name|method
argument_list|,
name|reference
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Method
name|method
range|:
name|produceMethods
control|)
block|{
name|Produce
name|annotation
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|Produce
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|String
name|contextName
init|=
name|CamelExtension
operator|.
name|getCamelContextName
argument_list|(
name|annotation
operator|.
name|context
argument_list|()
argument_list|,
name|startup
argument_list|)
decl_stmt|;
name|DefaultCamelBeanPostProcessor
name|postProcessor
init|=
name|camelExtension
operator|.
name|getPostProcessor
argument_list|(
name|contextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|postProcessor
operator|!=
literal|null
operator|&&
name|postProcessor
operator|.
name|getPostProcessorHelper
argument_list|()
operator|.
name|matchContext
argument_list|(
name|contextName
argument_list|)
condition|)
block|{
name|postProcessor
operator|.
name|setterInjection
argument_list|(
name|method
argument_list|,
name|reference
argument_list|,
name|beanName
argument_list|,
name|annotation
operator|.
name|uri
argument_list|()
argument_list|,
name|annotation
operator|.
name|ref
argument_list|()
argument_list|,
name|annotation
operator|.
name|property
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Method
name|method
range|:
name|endpointMethods
control|)
block|{
name|EndpointInject
name|annotation
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|EndpointInject
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|String
name|contextName
init|=
name|CamelExtension
operator|.
name|getCamelContextName
argument_list|(
name|annotation
operator|.
name|context
argument_list|()
argument_list|,
name|startup
argument_list|)
decl_stmt|;
name|DefaultCamelBeanPostProcessor
name|postProcessor
init|=
name|camelExtension
operator|.
name|getPostProcessor
argument_list|(
name|contextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|postProcessor
operator|!=
literal|null
operator|&&
name|postProcessor
operator|.
name|getPostProcessorHelper
argument_list|()
operator|.
name|matchContext
argument_list|(
name|contextName
argument_list|)
condition|)
block|{
name|postProcessor
operator|.
name|setterInjection
argument_list|(
name|method
argument_list|,
name|reference
argument_list|,
name|beanName
argument_list|,
name|annotation
operator|.
name|uri
argument_list|()
argument_list|,
name|annotation
operator|.
name|ref
argument_list|()
argument_list|,
name|annotation
operator|.
name|property
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Field
name|field
range|:
name|produceFields
control|)
block|{
name|Produce
name|annotation
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|Produce
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|String
name|contextName
init|=
name|CamelExtension
operator|.
name|getCamelContextName
argument_list|(
name|annotation
operator|.
name|context
argument_list|()
argument_list|,
name|startup
argument_list|)
decl_stmt|;
name|DefaultCamelBeanPostProcessor
name|postProcessor
init|=
name|camelExtension
operator|.
name|getPostProcessor
argument_list|(
name|contextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|postProcessor
operator|!=
literal|null
operator|&&
name|postProcessor
operator|.
name|getPostProcessorHelper
argument_list|()
operator|.
name|matchContext
argument_list|(
name|contextName
argument_list|)
condition|)
block|{
name|postProcessor
operator|.
name|injectField
argument_list|(
name|field
argument_list|,
name|annotation
operator|.
name|uri
argument_list|()
argument_list|,
name|annotation
operator|.
name|ref
argument_list|()
argument_list|,
name|annotation
operator|.
name|property
argument_list|()
argument_list|,
name|reference
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Field
name|field
range|:
name|endpointFields
control|)
block|{
name|EndpointInject
name|annotation
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|EndpointInject
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|contextName
init|=
name|CamelExtension
operator|.
name|getCamelContextName
argument_list|(
name|annotation
operator|.
name|context
argument_list|()
argument_list|,
name|startup
argument_list|)
decl_stmt|;
name|DefaultCamelBeanPostProcessor
name|postProcessor
init|=
name|camelExtension
operator|.
name|getPostProcessor
argument_list|(
name|contextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|postProcessor
operator|!=
literal|null
operator|&&
name|postProcessor
operator|.
name|getPostProcessorHelper
argument_list|()
operator|.
name|matchContext
argument_list|(
name|contextName
argument_list|)
condition|)
block|{
name|postProcessor
operator|.
name|injectField
argument_list|(
name|field
argument_list|,
name|annotation
operator|.
name|uri
argument_list|()
argument_list|,
name|annotation
operator|.
name|ref
argument_list|()
argument_list|,
name|annotation
operator|.
name|property
argument_list|()
argument_list|,
name|reference
argument_list|,
name|beanName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

