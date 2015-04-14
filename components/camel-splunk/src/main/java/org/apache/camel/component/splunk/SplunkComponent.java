begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.splunk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|splunk
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|impl
operator|.
name|UriEndpointComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link SplunkEndpoint}.  */
end_comment

begin_class
DECL|class|SplunkComponent
specifier|public
class|class
name|SplunkComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|splunkConfigurationFactory
specifier|private
name|SplunkConfigurationFactory
name|splunkConfigurationFactory
init|=
operator|new
name|DefaultSplunkConfigurationFactory
argument_list|()
decl_stmt|;
DECL|method|SplunkComponent ()
specifier|public
name|SplunkComponent
parameter_list|()
block|{
name|super
argument_list|(
name|SplunkEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|SplunkConfiguration
name|configuration
init|=
name|splunkConfigurationFactory
operator|.
name|parseMap
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
operator|new
name|SplunkEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
DECL|method|getSplunkConfigurationFactory ()
specifier|public
name|SplunkConfigurationFactory
name|getSplunkConfigurationFactory
parameter_list|()
block|{
return|return
name|splunkConfigurationFactory
return|;
block|}
comment|/**      * To use the {@link SplunkConfigurationFactory}      */
DECL|method|setSplunkConfigurationFactory (SplunkConfigurationFactory splunkConfigurationFactory)
specifier|public
name|void
name|setSplunkConfigurationFactory
parameter_list|(
name|SplunkConfigurationFactory
name|splunkConfigurationFactory
parameter_list|)
block|{
name|this
operator|.
name|splunkConfigurationFactory
operator|=
name|splunkConfigurationFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

