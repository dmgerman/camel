begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_class
DECL|class|ResourceUtils
specifier|public
specifier|final
class|class
name|ResourceUtils
block|{
DECL|method|ResourceUtils ()
specifier|private
name|ResourceUtils
parameter_list|()
block|{
comment|// noop
block|}
DECL|method|getResourceAsFile (String pathToFile)
specifier|public
specifier|static
name|File
name|getResourceAsFile
parameter_list|(
name|String
name|pathToFile
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|File
argument_list|(
name|ResourceUtils
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|pathToFile
argument_list|)
operator|.
name|getFile
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

