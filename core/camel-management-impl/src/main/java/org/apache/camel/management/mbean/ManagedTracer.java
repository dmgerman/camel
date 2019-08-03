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
name|BacklogTracerEventMessage
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
name|ManagedBacklogTracerMBean
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
name|ManagedTracerMBean
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
name|processor
operator|.
name|interceptor
operator|.
name|BacklogTracer
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
name|ManagementStrategy
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
name|Tracer
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Tracer"
argument_list|)
DECL|class|ManagedTracer
specifier|public
class|class
name|ManagedTracer
implements|implements
name|ManagedTracerMBean
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|tracer
specifier|private
specifier|final
name|Tracer
name|tracer
decl_stmt|;
DECL|method|ManagedTracer (CamelContext camelContext, Tracer tracer)
specifier|public
name|ManagedTracer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Tracer
name|tracer
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|tracer
operator|=
name|tracer
expr_stmt|;
block|}
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
comment|// do nothing
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|getTracer ()
specifier|public
name|Tracer
name|getTracer
parameter_list|()
block|{
return|return
name|tracer
return|;
block|}
DECL|method|getEnabled ()
specifier|public
name|boolean
name|getEnabled
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|isEnabled
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelId ()
specifier|public
name|String
name|getCamelId
parameter_list|()
block|{
return|return
name|camelContext
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelManagementName ()
specifier|public
name|String
name|getCamelManagementName
parameter_list|()
block|{
return|return
name|camelContext
operator|.
name|getManagementName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|tracer
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|isEnabled
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setTracePattern (String pattern)
specifier|public
name|void
name|setTracePattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|tracer
operator|.
name|setTracePattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getTracePattern ()
specifier|public
name|String
name|getTracePattern
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|getTracePattern
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getTraceCounter ()
specifier|public
name|long
name|getTraceCounter
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|getTraceCounter
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|resetTraceCounter ()
specifier|public
name|void
name|resetTraceCounter
parameter_list|()
block|{
name|tracer
operator|.
name|resetTraceCounter
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

