begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
operator|.
name|client
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|component
operator|.
name|iec60870
operator|.
name|AbstractIecEndpoint
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
name|iec60870
operator|.
name|ObjectAddress
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
name|DefaultComponent
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

begin_comment
comment|/**  * IEC 60870 component used for telecontrol (supervisory control and data acquisition)  * such as controlling electric power transmission grids and other geographically widespread control systems.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.20.0"
argument_list|,
name|scheme
operator|=
literal|"iec60870-client"
argument_list|,
name|syntax
operator|=
literal|"iec60870-client:uriPath"
argument_list|,
name|title
operator|=
literal|"IEC 60870 Client"
argument_list|,
name|consumerClass
operator|=
name|ClientConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"iot"
argument_list|)
DECL|class|ClientEndpoint
specifier|public
class|class
name|ClientEndpoint
extends|extends
name|AbstractIecEndpoint
argument_list|<
name|ClientConnectionMultiplexor
argument_list|>
block|{
DECL|method|ClientEndpoint (final String uri, final DefaultComponent component, final ClientConnectionMultiplexor connection, final ObjectAddress address)
specifier|public
name|ClientEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|DefaultComponent
name|component
parameter_list|,
specifier|final
name|ClientConnectionMultiplexor
name|connection
parameter_list|,
specifier|final
name|ObjectAddress
name|address
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|requireNonNull
argument_list|(
name|connection
argument_list|)
argument_list|,
name|address
argument_list|)
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
operator|new
name|ClientProducer
argument_list|(
name|this
argument_list|,
name|getConnection
argument_list|()
operator|.
name|getConnection
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (final Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
specifier|final
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|ClientConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|getConnection
argument_list|()
operator|.
name|getConnection
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

