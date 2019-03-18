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
comment|/**  * An interface to represent an object which wishes to be injected with  * a {@link Route} such as {@link Consumer} which is the consumer for a route.  */
end_comment

begin_interface
DECL|interface|RouteAware
specifier|public
interface|interface
name|RouteAware
block|{
comment|/**      * Injects the {@link Route}      *      * @param route the route      */
DECL|method|setRoute (Route route)
name|void
name|setRoute
parameter_list|(
name|Route
name|route
parameter_list|)
function_decl|;
comment|/**      * Gets the {@link Route}      *      * @return the route, or<tt>null</tt> if no route has been set.      */
DECL|method|getRoute ()
name|Route
name|getRoute
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

