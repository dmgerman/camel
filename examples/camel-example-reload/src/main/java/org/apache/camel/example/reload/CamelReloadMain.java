begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.reload
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|reload
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
name|spring
operator|.
name|Main
import|;
end_import

begin_comment
comment|/**  * A main class to run the example from your editor.  */
end_comment

begin_class
DECL|class|CamelReloadMain
specifier|public
specifier|final
class|class
name|CamelReloadMain
block|{
DECL|method|CamelReloadMain ()
specifier|private
name|CamelReloadMain
parameter_list|()
block|{     }
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
comment|// Main makes it easier to run a Spring application
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
comment|// configure the location of the Spring XML file
name|main
operator|.
name|setApplicationContextUri
argument_list|(
literal|"META-INF/spring/camel-context.xml"
argument_list|)
expr_stmt|;
comment|// turn on reload when the XML file is updated in the source code
name|main
operator|.
name|setFileWatchDirectory
argument_list|(
literal|"src/main/resources/META-INF/spring"
argument_list|)
expr_stmt|;
comment|// run and block until Camel is stopped (or JVM terminated)
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

