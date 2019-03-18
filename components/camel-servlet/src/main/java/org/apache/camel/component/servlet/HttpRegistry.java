begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
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
name|http
operator|.
name|common
operator|.
name|HttpConsumer
import|;
end_import

begin_comment
comment|/**  * Keeps track of HttpConsumers and CamelServlets and  * connects them to each other. In OSGi there should  * be one HttpRegistry per bundle.  *   * A CamelServlet that should serve more than one  * bundle should be registered as an OSGi service.  * The {@link DefaultHttpRegistry} can then be configured to listen  * to service changes.  * See /examples/camel-example-servlet-httpregistry-blueprint  * for an example how to use this.  */
end_comment

begin_interface
DECL|interface|HttpRegistry
specifier|public
interface|interface
name|HttpRegistry
block|{
DECL|method|register (HttpConsumer consumer)
name|void
name|register
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
function_decl|;
DECL|method|unregister (HttpConsumer consumer)
name|void
name|unregister
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
function_decl|;
DECL|method|register (CamelServlet provider)
name|void
name|register
parameter_list|(
name|CamelServlet
name|provider
parameter_list|)
function_decl|;
DECL|method|unregister (CamelServlet provider)
name|void
name|unregister
parameter_list|(
name|CamelServlet
name|provider
parameter_list|)
function_decl|;
DECL|method|getCamelServlet (String servletName)
name|CamelServlet
name|getCamelServlet
parameter_list|(
name|String
name|servletName
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

