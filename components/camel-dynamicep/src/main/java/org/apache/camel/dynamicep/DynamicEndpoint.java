begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dynamicep
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dynamicep
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

begin_comment
comment|/**  * Dynamic Endpoint   *   * Allows to create a list of consumers that feed into the same route.  * Example: from("dynamicep:jms:myQueue,file:myDir").to("log:test)  *   * As it is possible to use properties in the endpoint definition this  * allows to define a dynamical list of endpoints a route should listen on  */
end_comment

begin_class
DECL|class|DynamicEndpoint
specifier|public
class|class
name|DynamicEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|method|DynamicEndpoint ()
specifier|public
name|DynamicEndpoint
parameter_list|()
block|{     }
DECL|method|DynamicEndpoint (String uri, DynamicComponent component)
specifier|public
name|DynamicEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|DynamicComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|DynamicEndpoint (String endpointUri)
specifier|public
name|DynamicEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
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
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This component only supports consumers"
argument_list|)
throw|;
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
return|return
operator|new
name|DynamicConsumer
argument_list|(
name|this
argument_list|,
name|processor
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
block|}
end_class

end_unit

