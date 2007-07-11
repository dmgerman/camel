begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementRef
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_interface
DECL|interface|RouteContainer
specifier|public
interface|interface
name|RouteContainer
block|{
comment|/**      * A list of routes      *      * @return      */
annotation|@
name|XmlElementRef
DECL|method|getRoutes ()
name|List
argument_list|<
name|RouteType
argument_list|>
name|getRoutes
parameter_list|()
function_decl|;
comment|/**      * Sets the routes to use      *      * @param routes      */
DECL|method|setRoutes (List<RouteType> routes)
name|void
name|setRoutes
parameter_list|(
name|List
argument_list|<
name|RouteType
argument_list|>
name|routes
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

