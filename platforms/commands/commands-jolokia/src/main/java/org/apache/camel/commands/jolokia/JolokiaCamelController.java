begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands.jolokia
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
operator|.
name|jolokia
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
name|commands
operator|.
name|CamelController
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jolokia
operator|.
name|client
operator|.
name|J4pClient
import|;
end_import

begin_interface
DECL|interface|JolokiaCamelController
specifier|public
interface|interface
name|JolokiaCamelController
extends|extends
name|CamelController
block|{
comment|/**      * To use the existing {@link org.jolokia.client.J4pClient} with this controller.      *      * @param client the client to use      */
DECL|method|using (J4pClient client)
name|void
name|using
parameter_list|(
name|J4pClient
name|client
parameter_list|)
function_decl|;
comment|/**      * Connects to the remote JVM using the given url to the remote Jolokia agent      *      * @param url the url for the remote jolokia agent      * @param username optional username      * @param password optional password      * @throws Exception can be thrown      */
DECL|method|connect (String url, String username, String password)
name|void
name|connect
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * After connecting the ping command can be used to check if the connection works.      *      * @return<tt>true</tt> if connection works,<tt>false</tt> otherwise      */
DECL|method|ping ()
name|boolean
name|ping
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

