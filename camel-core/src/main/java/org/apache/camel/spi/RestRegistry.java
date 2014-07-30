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
name|Service
import|;
end_import

begin_interface
DECL|interface|RestRegistry
specifier|public
interface|interface
name|RestRegistry
extends|extends
name|Service
block|{
DECL|interface|RestService
interface|interface
name|RestService
block|{
DECL|method|getConsumer ()
name|Consumer
name|getConsumer
parameter_list|()
function_decl|;
DECL|method|getUrl ()
name|String
name|getUrl
parameter_list|()
function_decl|;
DECL|method|getPath ()
name|String
name|getPath
parameter_list|()
function_decl|;
DECL|method|getVerb ()
name|String
name|getVerb
parameter_list|()
function_decl|;
DECL|method|getConsumes ()
name|String
name|getConsumes
parameter_list|()
function_decl|;
DECL|method|getProduces ()
name|String
name|getProduces
parameter_list|()
function_decl|;
DECL|method|getState ()
name|String
name|getState
parameter_list|()
function_decl|;
block|}
DECL|method|addRestService (Consumer consumer, String url, String path, String verb, String consumes, String produces)
name|void
name|addRestService
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|)
function_decl|;
DECL|method|removeRestService (Consumer consumer)
name|void
name|removeRestService
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
function_decl|;
DECL|method|listAllRestServices ()
name|List
argument_list|<
name|RestService
argument_list|>
name|listAllRestServices
parameter_list|()
function_decl|;
comment|/**      * Number of rest services in the registry.      *      * @return number of rest services in the registry.      */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

