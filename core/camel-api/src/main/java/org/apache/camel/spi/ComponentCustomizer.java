begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_interface
annotation|@
name|FunctionalInterface
DECL|interface|ComponentCustomizer
specifier|public
interface|interface
name|ComponentCustomizer
parameter_list|<
name|T
extends|extends
name|Component
parameter_list|>
block|{
comment|/**      * Customize the specified {@link Component}.      *      * @param component the component to customize      */
DECL|method|customize (T component)
name|void
name|customize
parameter_list|(
name|T
name|component
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

