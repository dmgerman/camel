begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
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
name|Component
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
name|UriParam
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
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|Ignite
import|;
end_import

begin_comment
comment|/**  * Base class for all Ignite endpoints.   */
end_comment

begin_class
DECL|class|AbstractIgniteEndpoint
specifier|public
specifier|abstract
class|class
name|AbstractIgniteEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|component
specifier|protected
name|AbstractIgniteComponent
name|component
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|propagateIncomingBodyIfNoReturnValue
specifier|private
name|boolean
name|propagateIncomingBodyIfNoReturnValue
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|treatCollectionsAsCacheObjects
specifier|private
name|boolean
name|treatCollectionsAsCacheObjects
decl_stmt|;
DECL|method|AbstractIgniteEndpoint (String endpointUri, Component component)
specifier|public
name|AbstractIgniteEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|igniteComponent ()
specifier|protected
name|AbstractIgniteComponent
name|igniteComponent
parameter_list|()
block|{
if|if
condition|(
name|component
operator|==
literal|null
condition|)
block|{
name|component
operator|=
operator|(
name|AbstractIgniteComponent
operator|)
name|getComponent
argument_list|()
expr_stmt|;
block|}
return|return
name|component
return|;
block|}
DECL|method|ignite ()
specifier|protected
name|Ignite
name|ignite
parameter_list|()
block|{
return|return
name|igniteComponent
argument_list|()
operator|.
name|getIgnite
argument_list|()
return|;
block|}
comment|/**      * Gets whether to propagate the incoming body if the return type of the underlying       * Ignite operation is void.      *       * @return      */
DECL|method|isPropagateIncomingBodyIfNoReturnValue ()
specifier|public
name|boolean
name|isPropagateIncomingBodyIfNoReturnValue
parameter_list|()
block|{
return|return
name|propagateIncomingBodyIfNoReturnValue
return|;
block|}
comment|/**      * Sets whether to propagate the incoming body if the return type of the underlying       * Ignite operation is void.      *       * @param propagateIncomingBodyIfNoReturnValue      */
DECL|method|setPropagateIncomingBodyIfNoReturnValue (boolean propagateIncomingBodyIfNoReturnValue)
specifier|public
name|void
name|setPropagateIncomingBodyIfNoReturnValue
parameter_list|(
name|boolean
name|propagateIncomingBodyIfNoReturnValue
parameter_list|)
block|{
name|this
operator|.
name|propagateIncomingBodyIfNoReturnValue
operator|=
name|propagateIncomingBodyIfNoReturnValue
expr_stmt|;
block|}
comment|/**      * Gets whether to treat Collections as cache objects or as Collections of items to       * insert/update/compute, etc.      *       * @return      */
DECL|method|isTreatCollectionsAsCacheObjects ()
specifier|public
name|boolean
name|isTreatCollectionsAsCacheObjects
parameter_list|()
block|{
return|return
name|treatCollectionsAsCacheObjects
return|;
block|}
comment|/**      * Sets whether to treat Collections as cache objects or as Collections of items to       * insert/update/compute, etc.      *       * @param treatCollectionsAsCacheObjects      */
DECL|method|setTreatCollectionsAsCacheObjects (boolean treatCollectionsAsCacheObjects)
specifier|public
name|void
name|setTreatCollectionsAsCacheObjects
parameter_list|(
name|boolean
name|treatCollectionsAsCacheObjects
parameter_list|)
block|{
name|this
operator|.
name|treatCollectionsAsCacheObjects
operator|=
name|treatCollectionsAsCacheObjects
expr_stmt|;
block|}
block|}
end_class

end_unit

