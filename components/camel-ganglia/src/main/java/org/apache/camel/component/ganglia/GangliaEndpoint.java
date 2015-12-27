begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ganglia
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
package|;
end_package

begin_import
import|import
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|Publisher
import|;
end_import

begin_import
import|import
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|gmetric
operator|.
name|GMetric
import|;
end_import

begin_import
import|import
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|gmetric
operator|.
name|GMetricPublisher
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The ganglia component is used for sending metrics to the Ganglia monitoring system.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"ganglia"
argument_list|,
name|title
operator|=
literal|"Ganglia"
argument_list|,
name|syntax
operator|=
literal|"ganglia:host:port"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"monitoring"
argument_list|)
DECL|class|GangliaEndpoint
specifier|public
class|class
name|GangliaEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|publisher
specifier|private
name|Publisher
name|publisher
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|GangliaConfiguration
name|configuration
decl_stmt|;
DECL|method|GangliaEndpoint ()
specifier|public
name|GangliaEndpoint
parameter_list|()
block|{     }
DECL|method|GangliaEndpoint (String endpointUri, Component component)
specifier|public
name|GangliaEndpoint
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
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"configuration"
argument_list|)
expr_stmt|;
return|return
operator|new
name|GangliaProducer
argument_list|(
name|this
argument_list|,
name|getPublisher
argument_list|()
argument_list|)
return|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Ganglia consumer not supported"
argument_list|)
throw|;
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
name|GangliaConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (GangliaConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|GangliaConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getPublisher ()
specifier|public
specifier|synchronized
name|Publisher
name|getPublisher
parameter_list|()
block|{
if|if
condition|(
name|publisher
operator|==
literal|null
condition|)
block|{
name|GMetric
name|gmetric
init|=
name|configuration
operator|.
name|createGMetric
argument_list|()
decl_stmt|;
name|publisher
operator|=
operator|new
name|GMetricPublisher
argument_list|(
name|gmetric
argument_list|)
expr_stmt|;
block|}
return|return
name|publisher
return|;
block|}
DECL|method|setPublisher (Publisher publisher)
specifier|public
name|void
name|setPublisher
parameter_list|(
name|Publisher
name|publisher
parameter_list|)
block|{
name|this
operator|.
name|publisher
operator|=
name|publisher
expr_stmt|;
block|}
block|}
end_class

end_unit

