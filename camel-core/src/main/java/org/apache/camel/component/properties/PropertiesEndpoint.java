begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
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
name|DelegateEndpoint
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
name|Endpoint
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
name|spi
operator|.
name|Metadata
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
name|UriPath
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

begin_comment
comment|/**  * The properties component is used for using property placeholders in endpoint uris.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.3.0"
argument_list|,
name|scheme
operator|=
literal|"properties"
argument_list|,
name|title
operator|=
literal|"Properties"
argument_list|,
name|syntax
operator|=
literal|"properties:key"
argument_list|,
name|label
operator|=
literal|"core,endpoint"
argument_list|)
DECL|class|PropertiesEndpoint
specifier|public
class|class
name|PropertiesEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|DelegateEndpoint
block|{
DECL|field|endpoint
specifier|private
specifier|volatile
name|Endpoint
name|endpoint
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|key
specifier|private
name|String
name|key
decl_stmt|;
annotation|@
name|UriParam
DECL|field|locations
specifier|private
name|String
name|locations
decl_stmt|;
annotation|@
name|UriParam
DECL|field|ignoreMissingLocation
specifier|private
name|boolean
name|ignoreMissingLocation
decl_stmt|;
DECL|method|PropertiesEndpoint (String endpointUri, Endpoint delegate, Component component)
specifier|public
name|PropertiesEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Endpoint
name|delegate
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
name|this
operator|.
name|endpoint
operator|=
name|delegate
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/**      * Property key to use as placeholder      */
DECL|method|setKey (String key)
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
DECL|method|getLocations ()
specifier|public
name|String
name|getLocations
parameter_list|()
block|{
return|return
name|locations
return|;
block|}
comment|/**      * A list of locations to load properties. You can use comma to separate multiple locations.      * This option will override any default locations and only use the locations from this option.      */
DECL|method|setLocations (String locations)
specifier|public
name|void
name|setLocations
parameter_list|(
name|String
name|locations
parameter_list|)
block|{
name|this
operator|.
name|locations
operator|=
name|locations
expr_stmt|;
block|}
DECL|method|isIgnoreMissingLocation ()
specifier|public
name|boolean
name|isIgnoreMissingLocation
parameter_list|()
block|{
return|return
name|ignoreMissingLocation
return|;
block|}
comment|/**      * Whether to silently ignore if a location cannot be located, such as a properties file not found.      */
DECL|method|setIgnoreMissingLocation (boolean ignoreMissingLocation)
specifier|public
name|void
name|setIgnoreMissingLocation
parameter_list|(
name|boolean
name|ignoreMissingLocation
parameter_list|)
block|{
name|this
operator|.
name|ignoreMissingLocation
operator|=
name|ignoreMissingLocation
expr_stmt|;
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
name|endpoint
operator|.
name|createProducer
argument_list|()
return|;
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
return|return
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
return|;
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
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// add the endpoint as a service so Camel can manage the endpoint and enlist the endpoint in JMX etc.
name|getCamelContext
argument_list|()
operator|.
name|addService
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
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
comment|// noop
block|}
block|}
end_class

end_unit

