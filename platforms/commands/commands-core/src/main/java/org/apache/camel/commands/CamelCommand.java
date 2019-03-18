begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A Camel command.  */
end_comment

begin_interface
DECL|interface|CamelCommand
specifier|public
interface|interface
name|CamelCommand
block|{
comment|/**      * Executes the given command.      *      * @param camelController the Camel controller to access Camel information      * @param out             the output printer stream      * @param err             the error print stream      * @return response from command, or<tt>null</tt> if nothing to return      * @throws Exception is thrown if error executing command      */
DECL|method|execute (CamelController camelController, PrintStream out, PrintStream err)
name|Object
name|execute
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

