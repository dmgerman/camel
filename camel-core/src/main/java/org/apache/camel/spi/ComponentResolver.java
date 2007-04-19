begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|Component
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
name|CamelContext
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
name|impl
operator|.
name|DefaultComponentResolver
import|;
end_import

begin_comment
comment|/**  * Represents a resolver of components from a URI to be able to auto-load them using some  * discovery mechanism like {@link DefaultComponentResolver}  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|ComponentResolver
specifier|public
interface|interface
name|ComponentResolver
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
block|{
comment|/**      * Attempts to resolve the component for the given URI      *      * @param name the component name to resolve      * @param context the context to load the component if it can be resolved      * @return the component which is added to the context or null if it can not be resolved      */
DECL|method|resolveComponent (String name, CamelContext context)
name|Component
argument_list|<
name|E
argument_list|>
name|resolveComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

