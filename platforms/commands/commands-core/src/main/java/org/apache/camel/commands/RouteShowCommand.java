begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_comment
comment|/**  * Command to show the route marshaled in XML.  */
end_comment

begin_class
DECL|class|RouteShowCommand
specifier|public
class|class
name|RouteShowCommand
extends|extends
name|AbstractRouteCommand
block|{
DECL|method|RouteShowCommand (String route, String context)
specifier|public
name|RouteShowCommand
parameter_list|(
name|String
name|route
parameter_list|,
name|String
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|route
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|executeOnRoute (CamelController camelController, String contextName, String routeId, PrintStream out, PrintStream err)
specifier|public
name|void
name|executeOnRoute
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
name|String
name|contextName
parameter_list|,
name|String
name|routeId
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
name|camelController
operator|.
name|getRouteModelAsXml
argument_list|(
name|routeId
argument_list|,
name|contextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|xml
operator|==
literal|null
condition|)
block|{
name|err
operator|.
name|println
argument_list|(
literal|"Definition of route "
operator|+
name|routeId
operator|+
literal|" not found."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
name|xml
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

