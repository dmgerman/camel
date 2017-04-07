begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.connector
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|connector
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
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Connector Endpoint"
argument_list|)
DECL|class|DefaultConnectorEndpoint
specifier|public
class|class
name|DefaultConnectorEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|DelegateEndpoint
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|inputDataType
specifier|private
specifier|final
name|DataType
name|inputDataType
decl_stmt|;
DECL|field|outputDataType
specifier|private
specifier|final
name|DataType
name|outputDataType
decl_stmt|;
DECL|method|DefaultConnectorEndpoint (String endpointUri, ConnectorComponent component, Endpoint endpoint, DataType inputDataType, DataType outputDataType)
specifier|public
name|DefaultConnectorEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|ConnectorComponent
name|component
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|DataType
name|inputDataType
parameter_list|,
name|DataType
name|outputDataType
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
name|endpoint
expr_stmt|;
name|this
operator|.
name|inputDataType
operator|=
name|inputDataType
expr_stmt|;
name|this
operator|.
name|outputDataType
operator|=
name|outputDataType
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
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
return|return
operator|new
name|ConnectorProducer
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|,
name|getComponent
argument_list|()
operator|.
name|getBeforeProducer
argument_list|()
argument_list|,
name|getComponent
argument_list|()
operator|.
name|getAfterProducer
argument_list|()
argument_list|)
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
name|ConnectorConsumerProcessor
name|delegate
init|=
operator|new
name|ConnectorConsumerProcessor
argument_list|(
name|processor
argument_list|,
name|getComponent
argument_list|()
operator|.
name|getBeforeConsumer
argument_list|()
argument_list|,
name|getComponent
argument_list|()
operator|.
name|getAfterConsumer
argument_list|()
argument_list|)
decl_stmt|;
name|Consumer
name|consumer
init|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|ConnectorComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|ConnectorComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
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
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Delegate Endpoint URI"
argument_list|,
name|mask
operator|=
literal|true
argument_list|)
DECL|method|getDelegateEndpointUri ()
specifier|public
name|String
name|getDelegateEndpointUri
parameter_list|()
block|{
return|return
name|endpoint
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
literal|"Input data type"
argument_list|)
DECL|method|getInputDataType ()
specifier|public
name|DataType
name|getInputDataType
parameter_list|()
block|{
return|return
name|inputDataType
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Output data type"
argument_list|)
DECL|method|getOutputDataType ()
specifier|public
name|DataType
name|getOutputDataType
parameter_list|()
block|{
return|return
name|outputDataType
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|endpoint
argument_list|)
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

