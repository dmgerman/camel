begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sparkrest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
package|;
end_package

begin_import
import|import
name|spark
operator|.
name|Route
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Spark
import|;
end_import

begin_class
DECL|class|CamelSpark
specifier|public
specifier|final
class|class
name|CamelSpark
block|{
DECL|method|CamelSpark ()
specifier|private
name|CamelSpark
parameter_list|()
block|{     }
comment|/**      * Stops the Spark Server      */
DECL|method|stop ()
specifier|public
specifier|static
name|void
name|stop
parameter_list|()
block|{
name|Spark
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|/**      * Configures the port number to use      */
DECL|method|port (int port)
specifier|public
specifier|static
name|void
name|port
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|Spark
operator|.
name|port
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
comment|/**      * Configures the IP address to use      */
DECL|method|ipAddress (String ip)
specifier|public
specifier|static
name|void
name|ipAddress
parameter_list|(
name|String
name|ip
parameter_list|)
block|{
name|Spark
operator|.
name|ipAddress
argument_list|(
name|ip
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a Spark REST verb that routes to the given Camel route      *      * @param verb   the HTTP verb      * @param path   the context path      * @param accept the accept header      * @param route  the Camel route      */
DECL|method|spark (String verb, String path, String accept, Route route)
specifier|public
specifier|static
name|void
name|spark
parameter_list|(
name|String
name|verb
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|accept
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
if|if
condition|(
literal|"get"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
if|if
condition|(
name|accept
operator|!=
literal|null
condition|)
block|{
name|Spark
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|accept
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Spark
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"post"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
if|if
condition|(
name|accept
operator|!=
literal|null
condition|)
block|{
name|Spark
operator|.
name|post
argument_list|(
name|path
argument_list|,
name|accept
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Spark
operator|.
name|post
argument_list|(
name|path
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"put"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
if|if
condition|(
name|accept
operator|!=
literal|null
condition|)
block|{
name|Spark
operator|.
name|put
argument_list|(
name|path
argument_list|,
name|accept
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Spark
operator|.
name|put
argument_list|(
name|path
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"patch"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
if|if
condition|(
name|accept
operator|!=
literal|null
condition|)
block|{
name|Spark
operator|.
name|patch
argument_list|(
name|path
argument_list|,
name|accept
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Spark
operator|.
name|patch
argument_list|(
name|path
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"delete"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
if|if
condition|(
name|accept
operator|!=
literal|null
condition|)
block|{
name|Spark
operator|.
name|delete
argument_list|(
name|path
argument_list|,
name|accept
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Spark
operator|.
name|delete
argument_list|(
name|path
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"head"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
if|if
condition|(
name|accept
operator|!=
literal|null
condition|)
block|{
name|Spark
operator|.
name|head
argument_list|(
name|path
argument_list|,
name|accept
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Spark
operator|.
name|head
argument_list|(
name|path
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"trace"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
if|if
condition|(
name|accept
operator|!=
literal|null
condition|)
block|{
name|Spark
operator|.
name|trace
argument_list|(
name|path
argument_list|,
name|accept
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Spark
operator|.
name|trace
argument_list|(
name|path
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"connect"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
if|if
condition|(
name|accept
operator|!=
literal|null
condition|)
block|{
name|Spark
operator|.
name|connect
argument_list|(
name|path
argument_list|,
name|accept
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Spark
operator|.
name|connect
argument_list|(
name|path
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"options"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
if|if
condition|(
name|accept
operator|!=
literal|null
condition|)
block|{
name|Spark
operator|.
name|options
argument_list|(
name|path
argument_list|,
name|accept
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Spark
operator|.
name|options
argument_list|(
name|path
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

