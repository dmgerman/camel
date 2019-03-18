begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * To allow objects to be injected with an id, such as EIP {@link org.apache.camel.Processor}s which has been defined from Camel routes.  *<p/>  * This allows access to the id of the processor at runtime, which makes it easier to map it to the corresponding model definition.  */
end_comment

begin_interface
DECL|interface|IdAware
specifier|public
interface|interface
name|IdAware
extends|extends
name|HasId
block|{
comment|/**      * Sets the id      *      * @param id the id      */
DECL|method|setId (String id)
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

