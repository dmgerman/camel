begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servlet
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|http
operator|.
name|CamelServlet
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
name|http
operator|.
name|HttpConsumer
import|;
end_import

begin_comment
comment|/**  * Service which binds {@link CamelServlet} to the consumers it should service.  */
end_comment

begin_interface
DECL|interface|CamelServletService
specifier|public
interface|interface
name|CamelServletService
block|{
comment|/**      * Adds the given consumer to this service.      *      * @param consumer the consumer      */
DECL|method|addConsumer (HttpConsumer consumer)
name|void
name|addConsumer
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
function_decl|;
comment|/**      * Gets the known consumers this service services.      *      * @return the consumers.      */
DECL|method|getConsumers ()
name|Set
argument_list|<
name|HttpConsumer
argument_list|>
name|getConsumers
parameter_list|()
function_decl|;
comment|/**      * Sets the servlet to use.      *      * @param camelServlet the servlet to use.      */
DECL|method|setCamelServlet (CamelServlet camelServlet)
name|void
name|setCamelServlet
parameter_list|(
name|CamelServlet
name|camelServlet
parameter_list|)
function_decl|;
comment|/**      * Connect the given consumer to the servlet.      *      * @param consumer the consumer      */
DECL|method|connect (HttpConsumer consumer)
name|void
name|connect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
function_decl|;
comment|/**      * Disconnects the given consumer from the servlet.      *      * @param consumer the consumer      */
DECL|method|disconnect (HttpConsumer consumer)
name|void
name|disconnect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
function_decl|;
comment|/**      * Gets the name of the servlet used.      *      * @return the name of the servlet used.      */
DECL|method|getServletName ()
name|String
name|getServletName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

