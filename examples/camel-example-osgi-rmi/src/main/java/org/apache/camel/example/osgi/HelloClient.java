begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|LocateRegistry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|Registry
import|;
end_import

begin_comment
comment|/**  * Client to invoke the RMI service hosted on another JVM running on localhost.  *  * @version   */
end_comment

begin_class
DECL|class|HelloClient
specifier|public
specifier|final
class|class
name|HelloClient
block|{
DECL|method|HelloClient ()
specifier|private
name|HelloClient
parameter_list|()
block|{
comment|// use Main
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Getting registry"
argument_list|)
expr_stmt|;
name|Registry
name|registry
init|=
name|LocateRegistry
operator|.
name|getRegistry
argument_list|(
literal|"localhost"
argument_list|,
literal|37541
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Lookup service"
argument_list|)
expr_stmt|;
name|HelloService
name|hello
init|=
operator|(
name|HelloService
operator|)
name|registry
operator|.
name|lookup
argument_list|(
literal|"helloServiceBean"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking RMI ..."
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|hello
operator|.
name|hello
argument_list|(
literal|"Client"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

