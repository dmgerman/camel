begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
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
name|Endpoint
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
name|Producer
import|;
end_import

begin_comment
comment|/**  * Allows to plugin custom strategy for rewriting url.  *<p/>  * This allows for example to proxy http services and plugin a url rewrite  * strategy such as the<a href="http://camel.apache.org/urlrewrite">url-rewrite</a> component.  */
end_comment

begin_interface
DECL|interface|UrlRewrite
specifier|public
interface|interface
name|UrlRewrite
block|{
comment|/**      * Rewrite the url.      *      * @param url  the url      * @param producer the producer to use the rewritten url      * @return the rewritten url, or<tt>null</tt> to use the original url      * @throws Exception is thrown if error rewriting the url      */
DECL|method|rewrite (String url, Producer producer)
name|String
name|rewrite
parameter_list|(
name|String
name|url
parameter_list|,
name|Producer
name|producer
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

