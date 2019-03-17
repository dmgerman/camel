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
comment|/**  * An interface to represent an object which wishes to be injected with  * a {@link CamelContext} such as when working with Spring or Guice  */
end_comment

begin_interface
DECL|interface|CamelContextAware
specifier|public
interface|interface
name|CamelContextAware
block|{
comment|/**      * Injects the {@link CamelContext}      *      * @param camelContext the Camel context      */
DECL|method|setCamelContext (CamelContext camelContext)
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
function_decl|;
comment|/**      * Get the {@link CamelContext}      *      * @return camelContext the Camel context      */
DECL|method|getCamelContext ()
name|CamelContext
name|getCamelContext
parameter_list|()
function_decl|;
comment|/**      * Set the {@link CamelContext} context if the object is an instance of {@link CamelContextAware}.      */
DECL|method|trySetCamelContext (T object, CamelContext camelContext)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|trySetCamelContext
parameter_list|(
name|T
name|object
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|object
operator|)
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
return|return
name|object
return|;
block|}
block|}
end_interface

end_unit

