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
name|ManagedRollbackMBean
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
name|processor
operator|.
name|RollbackProcessor
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Rollback"
argument_list|)
DECL|class|ManagedRollback
specifier|public
class|class
name|ManagedRollback
extends|extends
name|ManagedProcessor
implements|implements
name|ManagedRollbackMBean
block|{
DECL|field|processor
specifier|private
specifier|final
name|RollbackProcessor
name|processor
decl_stmt|;
DECL|method|ManagedRollback (CamelContext context, RollbackProcessor processor, ProcessorDefinition<?> definition)
specifier|public
name|ManagedRollback
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|RollbackProcessor
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
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isMarkRollbackOnly ()
specifier|public
name|Boolean
name|isMarkRollbackOnly
parameter_list|()
block|{
return|return
name|processor
operator|.
name|isMarkRollbackOnly
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isMarkRollbackOnlyLast ()
specifier|public
name|Boolean
name|isMarkRollbackOnlyLast
parameter_list|()
block|{
return|return
name|processor
operator|.
name|isMarkRollbackOnlyLast
argument_list|()
return|;
block|}
block|}
end_class

end_unit

