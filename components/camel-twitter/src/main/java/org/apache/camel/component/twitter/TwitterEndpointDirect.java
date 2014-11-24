begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ExchangePattern
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
name|Producer
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
name|ServiceStatus
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
name|direct
operator|.
name|DirectEndpoint
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
name|consumer
operator|.
name|Twitter4JConsumer
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
name|consumer
operator|.
name|TwitterConsumerDirect
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
name|UriEndpoint
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

begin_comment
comment|/**  * Twitter direct endpoint  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"twitter"
argument_list|,
name|consumerClass
operator|=
name|Twitter4JConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"api,social"
argument_list|)
DECL|class|TwitterEndpointDirect
specifier|public
class|class
name|TwitterEndpointDirect
extends|extends
name|DirectEndpoint
implements|implements
name|TwitterEndpoint
block|{
annotation|@
name|UriParam
DECL|field|properties
specifier|private
name|TwitterConfiguration
name|properties
decl_stmt|;
DECL|method|TwitterEndpointDirect (String uri, TwitterComponent component, TwitterConfiguration properties)
specifier|public
name|TwitterEndpointDirect
parameter_list|(
name|String
name|uri
parameter_list|,
name|TwitterComponent
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
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|Twitter4JConsumer
name|twitter4jConsumer
init|=
name|Twitter4JFactory
operator|.
name|getConsumer
argument_list|(
name|this
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|TwitterConsumerDirect
name|answer
init|=
operator|new
name|TwitterConsumerDirect
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|twitter4jConsumer
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|Twitter4JFactory
operator|.
name|getProducer
argument_list|(
name|this
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
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
argument_list|(
name|description
operator|=
literal|"Camel ID"
argument_list|)
DECL|method|getCamelId ()
specifier|public
name|String
name|getCamelId
parameter_list|()
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel ManagementName"
argument_list|)
DECL|method|getCamelManagementName ()
specifier|public
name|String
name|getCamelManagementName
parameter_list|()
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Endpoint Uri"
argument_list|,
name|mask
operator|=
literal|true
argument_list|)
annotation|@
name|Override
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
return|return
name|super
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Service State"
argument_list|)
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
name|ServiceStatus
name|status
init|=
name|this
operator|.
name|getStatus
argument_list|()
decl_stmt|;
comment|// if no status exists then its stopped
if|if
condition|(
name|status
operator|==
literal|null
condition|)
block|{
name|status
operator|=
name|ServiceStatus
operator|.
name|Stopped
expr_stmt|;
block|}
return|return
name|status
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getKeywords ()
specifier|public
name|String
name|getKeywords
parameter_list|()
block|{
return|return
name|getProperties
argument_list|()
operator|.
name|getKeywords
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|setKeywords (String keywords)
specifier|public
name|void
name|setKeywords
parameter_list|(
name|String
name|keywords
parameter_list|)
block|{
name|getProperties
argument_list|()
operator|.
name|setKeywords
argument_list|(
name|keywords
argument_list|)
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
DECL|method|setDate (String date)
specifier|public
name|void
name|setDate
parameter_list|(
name|String
name|date
parameter_list|)
block|{
name|getProperties
argument_list|()
operator|.
name|setDate
argument_list|(
name|date
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getDate ()
specifier|public
name|String
name|getDate
parameter_list|()
block|{
return|return
name|getProperties
argument_list|()
operator|.
name|getDate
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
name|EndpointType
operator|.
name|DIRECT
return|;
block|}
annotation|@
name|Override
DECL|method|getExchangePattern ()
specifier|public
name|ExchangePattern
name|getExchangePattern
parameter_list|()
block|{
return|return
name|ExchangePattern
operator|.
name|InOptionalOut
return|;
block|}
block|}
end_class

end_unit

