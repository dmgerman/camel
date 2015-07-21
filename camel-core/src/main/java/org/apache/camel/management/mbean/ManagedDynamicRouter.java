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
name|ManagedDynamicRouterMBean
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
name|DynamicRouter
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
name|util
operator|.
name|URISupport
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed DynamicRouter"
argument_list|)
DECL|class|ManagedDynamicRouter
specifier|public
class|class
name|ManagedDynamicRouter
extends|extends
name|ManagedProcessor
implements|implements
name|ManagedDynamicRouterMBean
block|{
DECL|field|processor
specifier|private
specifier|final
name|DynamicRouter
name|processor
decl_stmt|;
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
DECL|method|ManagedDynamicRouter (CamelContext context, DynamicRouter processor, ProcessorDefinition<?> definition)
specifier|public
name|ManagedDynamicRouter
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|DynamicRouter
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
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
name|super
operator|.
name|init
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|boolean
name|sanitize
init|=
name|strategy
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMask
argument_list|()
operator|!=
literal|null
condition|?
name|strategy
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMask
argument_list|()
else|:
literal|false
decl_stmt|;
name|uri
operator|=
name|processor
operator|.
name|getExpression
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
if|if
condition|(
name|sanitize
condition|)
block|{
name|uri
operator|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getExpression ()
specifier|public
name|String
name|getExpression
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
annotation|@
name|Override
DECL|method|getUriDelimiter ()
specifier|public
name|String
name|getUriDelimiter
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getUriDelimiter
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCacheSize ()
specifier|public
name|Integer
name|getCacheSize
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getCacheSize
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isIgnoreInvalidEndpoints ()
specifier|public
name|Boolean
name|isIgnoreInvalidEndpoints
parameter_list|()
block|{
return|return
name|processor
operator|.
name|isIgnoreInvalidEndpoints
argument_list|()
return|;
block|}
block|}
end_class

end_unit

