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
comment|/**  * An interface to represent an object such as a {@link org.apache.camel.Processor} that uses an {@link Endpoint}  */
end_comment

begin_interface
DECL|interface|EndpointAware
specifier|public
interface|interface
name|EndpointAware
block|{
comment|/**      * Gets the endpoint associated with an object.      * It's the endpoint for sending to for components like {@link org.apache.camel.Producer}      * or for consuming from for components like {@link org.apache.camel.Consumer} or {@link org.apache.camel.Route}      *      * @return the endpoint      */
DECL|method|getEndpoint ()
name|Endpoint
name|getEndpoint
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

