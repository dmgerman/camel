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
comment|/**  * Exception when failing to create a {@link org.apache.camel.Route}.  *  * @version   */
end_comment

begin_class
DECL|class|FailedToCreateRouteException
specifier|public
class|class
name|FailedToCreateRouteException
extends|extends
name|CamelException
block|{
DECL|field|routeId
specifier|private
specifier|final
name|String
name|routeId
decl_stmt|;
DECL|method|FailedToCreateRouteException (String routeId, String route, Throwable cause)
specifier|public
name|FailedToCreateRouteException
parameter_list|(
name|String
name|routeId
parameter_list|,
name|String
name|route
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
literal|"Failed to create route "
operator|+
name|routeId
operator|+
literal|": "
operator|+
name|getRouteMessage
argument_list|(
name|route
argument_list|)
operator|+
literal|" because of "
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
name|routeId
operator|=
name|routeId
expr_stmt|;
block|}
DECL|method|FailedToCreateRouteException (String routeId, String route, String at, Throwable cause)
specifier|public
name|FailedToCreateRouteException
parameter_list|(
name|String
name|routeId
parameter_list|,
name|String
name|route
parameter_list|,
name|String
name|at
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
literal|"Failed to create route "
operator|+
name|routeId
operator|+
literal|" at:>>> "
operator|+
name|at
operator|+
literal|"<<< in route: "
operator|+
name|getRouteMessage
argument_list|(
name|route
argument_list|)
operator|+
literal|" because of "
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
name|routeId
operator|=
name|routeId
expr_stmt|;
block|}
DECL|method|getRouteId ()
specifier|public
name|String
name|getRouteId
parameter_list|()
block|{
return|return
name|routeId
return|;
block|}
DECL|method|getRouteMessage (String route)
specifier|protected
specifier|static
name|String
name|getRouteMessage
parameter_list|(
name|String
name|route
parameter_list|)
block|{
comment|// cut the route after 60 chars so it won't be too big in the message
comment|// users just need to be able to identify the route so they know where to look
if|if
condition|(
name|route
operator|.
name|length
argument_list|()
operator|>
literal|60
condition|)
block|{
return|return
name|route
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|60
argument_list|)
operator|+
literal|"..."
return|;
block|}
else|else
block|{
return|return
name|route
return|;
block|}
block|}
block|}
end_class

end_unit

