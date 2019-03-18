begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * Thrown if Camel failed to create a producer for a given endpoint.  */
end_comment

begin_class
DECL|class|FailedToCreateProducerException
specifier|public
class|class
name|FailedToCreateProducerException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1341435621084082033L
decl_stmt|;
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
DECL|method|FailedToCreateProducerException (Endpoint endpoint, Throwable cause)
specifier|public
name|FailedToCreateProducerException
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
literal|"Failed to create Producer for endpoint: "
operator|+
name|endpoint
operator|+
literal|". Reason: "
operator|+
name|cause
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
expr_stmt|;
block|}
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
block|}
end_class

end_unit

