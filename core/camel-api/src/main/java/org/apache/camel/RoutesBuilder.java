begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * A routes builder is capable of building routes using the builder and model classes.  *<p/>  * Eventually the routes are added to a {@link org.apache.camel.CamelContext} where they  * run inside.  */
end_comment

begin_interface
DECL|interface|RoutesBuilder
specifier|public
interface|interface
name|RoutesBuilder
block|{
comment|/**      * Adds the routes from this Route Builder to the CamelContext.      *      * @param context the Camel context      * @throws Exception is thrown if initialization of routes failed      */
DECL|method|addRoutesToCamelContext (CamelContext context)
name|void
name|addRoutesToCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

