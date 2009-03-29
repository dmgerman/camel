begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Thrown if Camel failed to create a consumer for a given endpoint.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|FailedToCreateConsumerException
specifier|public
class|class
name|FailedToCreateConsumerException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|method|FailedToCreateConsumerException (Endpoint endpoint, Throwable cause)
specifier|public
name|FailedToCreateConsumerException
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
literal|"Failed to create Consumer for endpoint: "
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
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
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
block|}
end_class

end_unit

