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
name|ManagedFilterMBean
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
name|ExpressionNode
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
name|FilterProcessor
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Filter"
argument_list|)
DECL|class|ManagedFilter
specifier|public
class|class
name|ManagedFilter
extends|extends
name|ManagedProcessor
implements|implements
name|ManagedFilterMBean
block|{
DECL|field|processor
specifier|private
specifier|final
name|FilterProcessor
name|processor
decl_stmt|;
DECL|method|ManagedFilter (CamelContext context, FilterProcessor processor, ExpressionNode definition)
specifier|public
name|ManagedFilter
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|FilterProcessor
name|processor
parameter_list|,
name|ExpressionNode
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
DECL|method|getDefinition ()
specifier|public
name|ExpressionNode
name|getDefinition
parameter_list|()
block|{
return|return
operator|(
name|ExpressionNode
operator|)
name|super
operator|.
name|getDefinition
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|processor
operator|.
name|reset
argument_list|()
expr_stmt|;
name|super
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPredicate ()
specifier|public
name|String
name|getPredicate
parameter_list|()
block|{
return|return
name|getDefinition
argument_list|()
operator|.
name|getExpression
argument_list|()
operator|.
name|getExpression
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getPredicateLanguage ()
specifier|public
name|String
name|getPredicateLanguage
parameter_list|()
block|{
return|return
name|getDefinition
argument_list|()
operator|.
name|getExpression
argument_list|()
operator|.
name|getLanguage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getFilteredCount ()
specifier|public
name|Long
name|getFilteredCount
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getFilteredCount
argument_list|()
return|;
block|}
block|}
end_class

end_unit

