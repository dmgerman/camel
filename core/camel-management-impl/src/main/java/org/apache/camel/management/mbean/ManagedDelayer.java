begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ManagedDelayerMBean
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
name|builder
operator|.
name|ExpressionBuilder
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
name|Delayer
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Delayer"
argument_list|)
DECL|class|ManagedDelayer
specifier|public
class|class
name|ManagedDelayer
extends|extends
name|ManagedProcessor
implements|implements
name|ManagedDelayerMBean
block|{
DECL|field|delayer
specifier|private
specifier|final
name|Delayer
name|delayer
decl_stmt|;
DECL|method|ManagedDelayer (CamelContext context, Delayer delayer, ProcessorDefinition<?> definition)
specifier|public
name|ManagedDelayer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Delayer
name|delayer
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
name|delayer
argument_list|,
name|definition
argument_list|)
expr_stmt|;
name|this
operator|.
name|delayer
operator|=
name|delayer
expr_stmt|;
block|}
DECL|method|getDelayer ()
specifier|public
name|Delayer
name|getDelayer
parameter_list|()
block|{
return|return
name|delayer
return|;
block|}
DECL|method|getDelay ()
specifier|public
name|Long
name|getDelay
parameter_list|()
block|{
return|return
name|delayer
operator|.
name|getDelayValue
argument_list|()
return|;
block|}
DECL|method|constantDelay (Integer millis)
specifier|public
name|void
name|constantDelay
parameter_list|(
name|Integer
name|millis
parameter_list|)
block|{
name|Expression
name|delay
init|=
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|millis
argument_list|)
decl_stmt|;
name|delayer
operator|.
name|setDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
DECL|method|getDelayedCount ()
specifier|public
name|int
name|getDelayedCount
parameter_list|()
block|{
return|return
name|delayer
operator|.
name|getDelayedCount
argument_list|()
return|;
block|}
DECL|method|isAsyncDelayed ()
specifier|public
name|Boolean
name|isAsyncDelayed
parameter_list|()
block|{
return|return
name|delayer
operator|.
name|isAsyncDelayed
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isCallerRunsWhenRejected ()
specifier|public
name|Boolean
name|isCallerRunsWhenRejected
parameter_list|()
block|{
return|return
name|delayer
operator|.
name|isCallerRunsWhenRejected
argument_list|()
return|;
block|}
block|}
end_class

end_unit
