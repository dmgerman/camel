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
comment|/**  * A runtime exception thrown if an {@link Endpoint} cannot be resolved via URI  *   * @version   */
end_comment

begin_class
DECL|class|ResolveEndpointFailedException
specifier|public
class|class
name|ResolveEndpointFailedException
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
operator|-
literal|9121465713858552263L
decl_stmt|;
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
DECL|method|ResolveEndpointFailedException (String uri, Throwable cause)
specifier|public
name|ResolveEndpointFailedException
parameter_list|(
name|String
name|uri
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
literal|"Failed to resolve endpoint: "
operator|+
name|uri
operator|+
literal|" due to: "
operator|+
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|ResolveEndpointFailedException (String uri, String message)
specifier|public
name|ResolveEndpointFailedException
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
literal|"Failed to resolve endpoint: "
operator|+
name|uri
operator|+
literal|" due to: "
operator|+
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|ResolveEndpointFailedException (String uri)
specifier|public
name|ResolveEndpointFailedException
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|super
argument_list|(
literal|"Failed to resolve endpoint: "
operator|+
name|uri
argument_list|)
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
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

