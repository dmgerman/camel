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
comment|/**  * A<a href="http://activemq.apache.org/camel/component.html">component</a> is  * a factory of {@link Endpoint} objects.  *   * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Component
specifier|public
interface|interface
name|Component
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
block|{
comment|/**      * Returns the context      *       * @return the context of this component      */
DECL|method|getCamelContext ()
name|CamelContext
name|getCamelContext
parameter_list|()
function_decl|;
comment|/**      * The {@link CamelContext} is injected into the component when it is added      * to it      */
DECL|method|setCamelContext (CamelContext context)
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Attempt to resolve an endpoint for the given URI if the component is      * capable of handling the URI      *       * @param uri the URI to create      * @return a newly created endpoint or null if this component cannot create      *         instances of the given uri      */
DECL|method|createEndpoint (String uri)
name|Endpoint
argument_list|<
name|E
argument_list|>
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

