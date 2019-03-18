begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_comment
comment|/**  * A {@link org.apache.camel.commands.CamelController} that runs locally, eg within the same JVM as the {@link CamelContext}s  * it manages.  *  * For example the Apache Camel Karaf commands does this.  */
end_comment

begin_interface
DECL|interface|LocalCamelController
specifier|public
interface|interface
name|LocalCamelController
extends|extends
name|CamelController
block|{
comment|/**      * Get the list of Camel context.      *      * @return the list of Camel contexts.      * @throws Exception can be thrown      */
DECL|method|getLocalCamelContexts ()
name|List
argument_list|<
name|CamelContext
argument_list|>
name|getLocalCamelContexts
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Get a Camel context identified by the given name.      *      * @param name the Camel context name.      * @return the Camel context or null if not found.      * @throws Exception can be thrown      */
DECL|method|getLocalCamelContext (String name)
name|CamelContext
name|getLocalCamelContext
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

