begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
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
name|impl
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
comment|/**  * The spring-ws component is used for SOAP WebServices using Spring WebServices.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"spring-ws"
argument_list|,
name|title
operator|=
literal|"Spring WebService"
argument_list|,
name|syntax
operator|=
literal|"spring-ws:type:lookupKey:webServiceEndpointUri"
argument_list|,
name|consumerClass
operator|=
name|SpringWebserviceConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"soap,webservice"
argument_list|)
DECL|class|SpringWebserviceEndpoint
specifier|public
class|class
name|SpringWebserviceEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|SpringWebserviceConfiguration
name|configuration
decl_stmt|;
DECL|method|SpringWebserviceEndpoint (Component component, String uri, SpringWebserviceConfiguration configuration)
specifier|public
name|SpringWebserviceEndpoint
parameter_list|(
name|Component
name|component
parameter_list|,
name|String
name|uri
parameter_list|,
name|SpringWebserviceConfiguration
name|configuration
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
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
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
name|SpringWebserviceConsumer
name|consumer
init|=
operator|new
name|SpringWebserviceConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getEndpointDispatcher
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|getEndpointDispatcher
argument_list|()
operator|.
name|setConsumerMessageEndpoint
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SpringWebserviceProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
DECL|method|getConfiguration ()
specifier|public
name|SpringWebserviceConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
block|}
end_class

end_unit

