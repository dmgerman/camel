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
comment|/**  * A constant (singleton) bean implementation of {@link BeanHolder}  *  * @version   */
end_comment

begin_class
DECL|class|ConstantBeanHolder
specifier|public
class|class
name|ConstantBeanHolder
implements|implements
name|BeanHolder
block|{
DECL|field|bean
specifier|private
specifier|final
name|Object
name|bean
decl_stmt|;
DECL|field|beanInfo
specifier|private
specifier|final
name|BeanInfo
name|beanInfo
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|method|ConstantBeanHolder (Object bean, BeanInfo beanInfo)
specifier|public
name|ConstantBeanHolder
parameter_list|(
name|Object
name|bean
parameter_list|,
name|BeanInfo
name|beanInfo
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|beanInfo
argument_list|,
literal|"beanInfo"
argument_list|)
expr_stmt|;
name|this
operator|.
name|bean
operator|=
name|bean
expr_stmt|;
name|this
operator|.
name|beanInfo
operator|=
name|beanInfo
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|CamelContextHelper
operator|.
name|convertTo
argument_list|(
name|beanInfo
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|Processor
operator|.
name|class
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
DECL|method|ConstantBeanHolder (Object bean, CamelContext context)
specifier|public
name|ConstantBeanHolder
parameter_list|(
name|Object
name|bean
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
name|this
argument_list|(
name|bean
argument_list|,
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
argument_list|)
expr_stmt|;
block|}
DECL|method|ConstantBeanHolder (Object bean, CamelContext context, ParameterMappingStrategy parameterMappingStrategy)
specifier|public
name|ConstantBeanHolder
parameter_list|(
name|Object
name|bean
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|ParameterMappingStrategy
name|parameterMappingStrategy
parameter_list|)
block|{
name|this
argument_list|(
name|bean
argument_list|,
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
name|parameterMappingStrategy
argument_list|)
argument_list|)
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
comment|// avoid invoke toString on bean as it may be a remote proxy
return|return
name|ObjectHelper
operator|.
name|className
argument_list|(
name|bean
argument_list|)
operator|+
literal|"("
operator|+
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|bean
argument_list|)
operator|+
literal|")"
return|;
block|}
DECL|method|getBean ()
specifier|public
name|Object
name|getBean
parameter_list|()
block|{
return|return
name|bean
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
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
literal|null
return|;
block|}
block|}
end_class

end_unit

