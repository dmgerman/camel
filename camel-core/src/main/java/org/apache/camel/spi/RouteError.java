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

begin_interface
DECL|interface|RouteError
specifier|public
interface|interface
name|RouteError
block|{
DECL|enum|Phase
enum|enum
name|Phase
block|{
DECL|enumConstant|START
name|START
block|,
DECL|enumConstant|STOP
name|STOP
block|,
DECL|enumConstant|SUSPEND
name|SUSPEND
block|,
DECL|enumConstant|RESUME
name|RESUME
block|,
DECL|enumConstant|SHUTDOWN
name|SHUTDOWN
block|,
DECL|enumConstant|REMOVE
name|REMOVE
block|}
comment|/**      * Gets the phase associated with the error.      *      * @return the phase.      */
DECL|method|getPhase ()
name|Phase
name|getPhase
parameter_list|()
function_decl|;
comment|/**      * Gets the error.      *      * @return the error.      */
DECL|method|getException ()
name|Throwable
name|getException
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

