begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
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
name|Consumer
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
name|ManagedAttribute
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
name|twitter
operator|.
name|data
operator|.
name|EndpointType
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
name|DefaultPollingEndpoint
import|;
end_import

begin_comment
comment|/**  * The base Twitter Endpoint.  */
end_comment

begin_class
DECL|class|AbstractTwitterEndpoint
specifier|public
specifier|abstract
class|class
name|AbstractTwitterEndpoint
extends|extends
name|DefaultPollingEndpoint
implements|implements
name|TwitterEndpoint
block|{
DECL|field|DEFAULT_CONSUMER_DELAY
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_CONSUMER_DELAY
init|=
literal|30
operator|*
literal|1000L
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|optionalPrefix
operator|=
literal|"consumer."
argument_list|,
name|defaultValue
operator|=
literal|""
operator|+
name|DEFAULT_CONSUMER_DELAY
argument_list|,
name|label
operator|=
literal|"consumer,scheduler"
argument_list|,
name|description
operator|=
literal|"Milliseconds before the next poll."
argument_list|)
DECL|field|delay
specifier|private
name|long
name|delay
init|=
name|DEFAULT_CONSUMER_DELAY
decl_stmt|;
annotation|@
name|UriParam
DECL|field|properties
specifier|private
name|TwitterConfiguration
name|properties
decl_stmt|;
DECL|method|AbstractTwitterEndpoint (String uri, AbstractTwitterComponent component, TwitterConfiguration properties)
specifier|public
name|AbstractTwitterEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|AbstractTwitterComponent
name|component
parameter_list|,
name|TwitterConfiguration
name|properties
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|setDelay
argument_list|(
name|DEFAULT_CONSUMER_DELAY
argument_list|)
expr_stmt|;
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|properties
operator|.
name|getType
argument_list|()
operator|==
name|EndpointType
operator|.
name|EVENT
operator|&&
name|properties
operator|.
name|getTwitterStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|getTwitterStream
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|configureConsumer (Consumer consumer)
specifier|public
name|void
name|configureConsumer
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|ManagedAttribute
DECL|method|getProperties ()
specifier|public
name|TwitterConfiguration
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|setProperties (TwitterConfiguration properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|TwitterConfiguration
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getLocations ()
specifier|public
name|String
name|getLocations
parameter_list|()
block|{
return|return
name|getProperties
argument_list|()
operator|.
name|getLocations
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setLocations (String locations)
specifier|public
name|void
name|setLocations
parameter_list|(
name|String
name|locations
parameter_list|)
block|{
name|getProperties
argument_list|()
operator|.
name|setLocations
argument_list|(
name|locations
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setFilterOld (boolean filterOld)
specifier|public
name|void
name|setFilterOld
parameter_list|(
name|boolean
name|filterOld
parameter_list|)
block|{
name|getProperties
argument_list|()
operator|.
name|setFilterOld
argument_list|(
name|filterOld
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|isFilterOld ()
specifier|public
name|boolean
name|isFilterOld
parameter_list|()
block|{
return|return
name|getProperties
argument_list|()
operator|.
name|isFilterOld
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setSinceId (long sinceId)
specifier|public
name|void
name|setSinceId
parameter_list|(
name|long
name|sinceId
parameter_list|)
block|{
name|getProperties
argument_list|()
operator|.
name|setSinceId
argument_list|(
name|sinceId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getSinceId ()
specifier|public
name|long
name|getSinceId
parameter_list|()
block|{
return|return
name|getProperties
argument_list|()
operator|.
name|getSinceId
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setLang (String lang)
specifier|public
name|void
name|setLang
parameter_list|(
name|String
name|lang
parameter_list|)
block|{
name|getProperties
argument_list|()
operator|.
name|setLang
argument_list|(
name|lang
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getLang ()
specifier|public
name|String
name|getLang
parameter_list|()
block|{
return|return
name|getProperties
argument_list|()
operator|.
name|getLang
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setCount (Integer count)
specifier|public
name|void
name|setCount
parameter_list|(
name|Integer
name|count
parameter_list|)
block|{
name|getProperties
argument_list|()
operator|.
name|setCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getCount ()
specifier|public
name|Integer
name|getCount
parameter_list|()
block|{
return|return
name|getProperties
argument_list|()
operator|.
name|getCount
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setNumberOfPages (Integer numberOfPages)
specifier|public
name|void
name|setNumberOfPages
parameter_list|(
name|Integer
name|numberOfPages
parameter_list|)
block|{
name|getProperties
argument_list|()
operator|.
name|setNumberOfPages
argument_list|(
name|numberOfPages
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getNumberOfPages ()
specifier|public
name|Integer
name|getNumberOfPages
parameter_list|()
block|{
return|return
name|getProperties
argument_list|()
operator|.
name|getNumberOfPages
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpointType ()
specifier|public
name|EndpointType
name|getEndpointType
parameter_list|()
block|{
return|return
name|getProperties
argument_list|()
operator|.
name|getType
argument_list|()
return|;
block|}
comment|/**      * Milliseconds before the next poll.      */
annotation|@
name|Override
DECL|method|setDelay (long delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|super
operator|.
name|setDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
block|}
end_class

end_unit

