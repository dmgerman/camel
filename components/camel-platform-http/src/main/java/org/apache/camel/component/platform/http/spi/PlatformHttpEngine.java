begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.platform.http.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|platform
operator|.
name|http
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
name|Consumer
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
name|Processor
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
name|component
operator|.
name|platform
operator|.
name|http
operator|.
name|PlatformHttpEndpoint
import|;
end_import

begin_comment
comment|/**  * An abstraction of an HTTP Server engine on which HTTP endpoints can be deployed.  */
end_comment

begin_interface
DECL|interface|PlatformHttpEngine
specifier|public
interface|interface
name|PlatformHttpEngine
block|{
comment|/**      * Creates a new {@link Consumer} for the given {@link PlatformHttpEndpoint}.      *      * @param platformHttpEndpoint the {@link PlatformHttpEndpoint} to create a consumer for      * @param processor the Processor to pass to      * @return a new {@link Consumer}      */
DECL|method|createConsumer (PlatformHttpEndpoint platformHttpEndpoint, Processor processor)
name|Consumer
name|createConsumer
parameter_list|(
name|PlatformHttpEndpoint
name|platformHttpEndpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

