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
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|CamelContextAware
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
name|Service
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
name|StaticService
import|;
end_import

begin_comment
comment|/**  * SPI strategy for reloading Camel routes in an existing running {@link org.apache.camel.CamelContext}  */
end_comment

begin_interface
DECL|interface|ReloadStrategy
specifier|public
interface|interface
name|ReloadStrategy
extends|extends
name|Service
extends|,
name|StaticService
extends|,
name|CamelContextAware
block|{
comment|/**      * A reload is triggered when a XML resource is changed which contains Camel routes.      *      * @param camelContext  the running CamelContext      * @param name          name of resource such as a file name (can be null)      * @param resource      the changed resource      */
DECL|method|onReloadXml (CamelContext camelContext, String name, InputStream resource)
name|void
name|onReloadXml
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|name
parameter_list|,
name|InputStream
name|resource
parameter_list|)
function_decl|;
comment|/**      * Number of reloads succeeded.      */
DECL|method|getReloadCounter ()
name|int
name|getReloadCounter
parameter_list|()
function_decl|;
comment|/**      * Number of reloads failed.      */
DECL|method|getFailedCounter ()
name|int
name|getFailedCounter
parameter_list|()
function_decl|;
comment|/**      * Reset the counters.      */
DECL|method|resetCounters ()
name|void
name|resetCounters
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

