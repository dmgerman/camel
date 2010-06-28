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
name|Processor
import|;
end_import

begin_comment
comment|/**  * A strategy capable of applying interceptors to a processor  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Policy
specifier|public
interface|interface
name|Policy
block|{
comment|// TODO: Should support async routing engine
comment|// we may need the same bridge as we do with InterceptStrategy in DefaultChannel
comment|/**      * Wraps any applicable interceptors around the given processor      *      * @param routeContext the route context      * @param processor the processor to be intercepted      * @return either the original processor or a processor wrapped in one or more interceptors      */
DECL|method|wrap (RouteContext routeContext, Processor processor)
name|Processor
name|wrap
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

