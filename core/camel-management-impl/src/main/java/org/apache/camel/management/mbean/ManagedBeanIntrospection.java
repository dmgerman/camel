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
name|ManagedBeanIntrospectionMBean
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
name|BeanIntrospection
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed BeanIntrospection"
argument_list|)
DECL|class|ManagedBeanIntrospection
specifier|public
class|class
name|ManagedBeanIntrospection
extends|extends
name|ManagedService
implements|implements
name|ManagedBeanIntrospectionMBean
block|{
DECL|field|beanIntrospection
specifier|private
specifier|final
name|BeanIntrospection
name|beanIntrospection
decl_stmt|;
DECL|method|ManagedBeanIntrospection (CamelContext context, BeanIntrospection beanIntrospection)
specifier|public
name|ManagedBeanIntrospection
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|BeanIntrospection
name|beanIntrospection
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|beanIntrospection
argument_list|)
expr_stmt|;
name|this
operator|.
name|beanIntrospection
operator|=
name|beanIntrospection
expr_stmt|;
block|}
DECL|method|getBeanIntrospection ()
specifier|public
name|BeanIntrospection
name|getBeanIntrospection
parameter_list|()
block|{
return|return
name|beanIntrospection
return|;
block|}
annotation|@
name|Override
DECL|method|getInvokedCounter ()
specifier|public
name|Long
name|getInvokedCounter
parameter_list|()
block|{
return|return
name|beanIntrospection
operator|.
name|getInvokedCounter
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isExtendedStatistics ()
specifier|public
name|Boolean
name|isExtendedStatistics
parameter_list|()
block|{
return|return
name|beanIntrospection
operator|.
name|isExtendedStatistics
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setExtendedStatistics (Boolean extendedStatistics)
specifier|public
name|void
name|setExtendedStatistics
parameter_list|(
name|Boolean
name|extendedStatistics
parameter_list|)
block|{
name|beanIntrospection
operator|.
name|setExtendedStatistics
argument_list|(
name|extendedStatistics
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resetCounters ()
specifier|public
name|void
name|resetCounters
parameter_list|()
block|{
name|beanIntrospection
operator|.
name|resetCounters
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

