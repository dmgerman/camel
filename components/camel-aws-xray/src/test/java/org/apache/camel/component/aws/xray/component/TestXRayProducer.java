begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|component
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
name|Exchange
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
name|aws
operator|.
name|xray
operator|.
name|XRayTracer
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
name|aws
operator|.
name|xray
operator|.
name|bean
operator|.
name|SomeBackingService
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
name|DefaultProducer
import|;
end_import

begin_class
DECL|class|TestXRayProducer
specifier|public
class|class
name|TestXRayProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|backingService
specifier|private
specifier|final
name|SomeBackingService
name|backingService
decl_stmt|;
DECL|field|state
specifier|private
specifier|final
name|String
name|state
decl_stmt|;
DECL|method|TestXRayProducer (final TestXRayEndpoint endpoint, String state)
specifier|public
name|TestXRayProducer
parameter_list|(
specifier|final
name|TestXRayEndpoint
name|endpoint
parameter_list|,
name|String
name|state
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
name|backingService
operator|=
operator|new
name|SomeBackingService
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|byte
index|[]
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|trim
argument_list|(
name|CommonEndpoints
operator|.
name|RECEIVED
argument_list|)
operator|.
name|equals
argument_list|(
name|this
operator|.
name|state
argument_list|)
operator|||
name|trim
argument_list|(
name|CommonEndpoints
operator|.
name|READY
argument_list|)
operator|.
name|equals
argument_list|(
name|this
operator|.
name|state
argument_list|)
condition|)
block|{
name|String
name|traceId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|XRayTracer
operator|.
name|XRAY_TRACE_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|backingService
operator|.
name|performMethod
argument_list|(
name|body
argument_list|,
name|state
argument_list|,
name|traceId
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|trim (String endpoint)
specifier|private
specifier|static
name|String
name|trim
parameter_list|(
name|String
name|endpoint
parameter_list|)
block|{
return|return
name|endpoint
operator|.
name|substring
argument_list|(
name|endpoint
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
end_class

end_unit

