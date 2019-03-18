begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|health
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_comment
comment|/**  * A repository for health checks.  */
end_comment

begin_interface
DECL|interface|HealthCheckRepository
specifier|public
interface|interface
name|HealthCheckRepository
block|{
comment|/**      * Returns a sequential {@code Stream} with the known {@link HealthCheck}      * as its source.      */
DECL|method|stream ()
name|Stream
argument_list|<
name|HealthCheck
argument_list|>
name|stream
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

